package com.jxva.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author  The Jxva Framework
 * @since   1.0
 * @version 2008-11-27 09:23:18 by Jxva
 */
public abstract class FolderUtil {

    private static final String FILE = "file";

    private static final String FOLDER = "folder";

    private static final String ALL = "all";

    public static boolean deleteRecursively(File folder) {
        if (folder != null && folder.exists()) {
            if (folder.isDirectory()) {
                File[] children = folder.listFiles();
                if (children != null) {
                    for (int i = 0; i < children.length; i++) {
                        deleteRecursively(children[i]);
                    }
                }
            }
            return folder.delete();
        }
        return false;
    }

    public static void copyRecursively(File src, File dest) throws IOException {
        Assert.isTrue(src != null && (src.isDirectory() || src.isFile()), "Source File must denote a directory or file");
        Assert.notNull(dest, "Destination File must not be null");
        doCopyRecursively(src, dest);
    }

    private static void doCopyRecursively(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdir();
            File[] entries = src.listFiles();
            if (entries == null) {
                throw new IOException("Could not list files in directory: " + src);
            }
            for (int i = 0; i < entries.length; i++) {
                File file = entries[i];
                doCopyRecursively(file, new File(dest, file.getName()));
            }
        } else if (src.isFile()) {
            try {
                dest.createNewFile();
            } catch (IOException ex) {
                IOException ioex = new IOException("Failed to create file: " + dest);
                ioex.initCause(ex);
                throw ioex;
            }
            FileUtil.copy(src, dest);
        } else {
        }
    }

    /**
	 * 新建文件夹
	 * @param folder
	 * @return boolean 新建成功:true 新建失败:false
	 */
    public static boolean create(File folder) {
        if (!folder.exists()) {
            return folder.mkdirs();
        }
        return true;
    }

    /**
	 * 得到某个文件夹下的所有文件及文件夹列表
	 * @param folder 文件夹
	 * @return List<File> 文件与文件夹列表
	 */
    public static List<File> getAll(File folder) {
        return getAll(folder, ALL);
    }

    /**
	 * 得到文件夹下的所有文件列表,不包括子文件夹
	 * @param folder 文件夹名
	 * @return List<File> 文件列表
	 */
    public static List<File> getFiles(File folder) {
        return getAll(folder, FILE);
    }

    /**
	 * 得到某个文件夹下的所有文件夹列表
	 * @param folder 文件夹名
	 * @return List<File> 文件夹列表
	 */
    public static List<File> getFolders(File folder) {
        return getAll(folder, FOLDER);
    }

    /**
	 * 删除文件夹下所有文件,不包括文件夹本身及子文件夹
	 * @param folder 文件夹名
	 * @return boolean 删除成功:true 删除失败:false 
	 */
    public static boolean deleteFiles(File folder) {
        if (folder == null) return false;
        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }
        for (File file : getFiles(folder)) {
            file.delete();
        }
        return true;
    }

    private static List<File> getAll(File folder, String type) {
        List<File> list = new LinkedList<File>();
        File flist[] = folder.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (type.equals(FILE)) {
                if (!flist[i].isDirectory()) {
                    list.add(flist[i]);
                }
            } else if (type.equals(FOLDER)) {
                if (!flist[i].isDirectory()) {
                    list.add(flist[i]);
                }
            } else {
                list.add(flist[i]);
            }
        }
        return list;
    }
}
