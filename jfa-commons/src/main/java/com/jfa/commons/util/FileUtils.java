/*
 * Copyright (c) 2015. Bond(China), java freestyle app
 */

package com.jfa.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件操作工具
 *
 * @author BondZhou
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 复制文件到指定目录，如果src为路径，则只复制其名下的所有子目录和子文件
     *
     * @param src  可为路径，也可是文件
     * @param dest 必需为路径
     * @throws IOException
     */
    public static void copy(File src, File dest) throws IOException {

        if (dest.isFile()) {
            return;
        }

        if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (File subFile : files) {
                File subDest = dest;
                if (subFile.isDirectory()) {
                    subDest = new File(dest.getAbsolutePath() + "/" + subFile.getName());
                    if (!subDest.exists()) {
                        subDest.mkdir();
                    }
                }
                copy(subFile, subDest);
            }
            return;
        }

        File realFile = new File(dest.getAbsoluteFile() + "/" + src.getName());
        if (!realFile.exists()) {
            realFile.createNewFile();
        }

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {

            fi = new FileInputStream(src);
            fo = new FileOutputStream(realFile);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);

        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw e;
            }

        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        boolean flag = false;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}