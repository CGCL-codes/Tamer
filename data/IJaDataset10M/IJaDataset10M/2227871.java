package org.mooym.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class contains some often needed utility methods for {@link File}.
 * 
 * @author roesslerj
 */
public final class FileUtil {

    private FileUtil() {
    }

    /**
   * Executes {@link File#listFiles(FileFilter)}, but includes all subfolders,
   * no matter what depth.
   * 
   * @param file
   *          The folder to list the contained files.
   * @param fileFilter
   *          The {@link FileFilter} to apply, when listing the files.
   * @return A List containing all files of all subfolders.
   */
    public static List<File> listFilesIncludingSubfolders(File file, FileFilter fileFilter) {
        if (!file.isDirectory()) {
            return Collections.singletonList(file);
        }
        List<File> fileList = new ArrayList<File>();
        List<File> tempFileList = Arrays.asList(file.listFiles(fileFilter));
        for (File listedFile : tempFileList) {
            fileList.addAll(listFilesIncludingSubfolders(listedFile, fileFilter));
        }
        return fileList;
    }

    /**
   * Returns the specified folder. If the folder does not already exist, it is
   * created, including any parent directories.
   * 
   * @param resultFolder
   *          The folder as specified by the String.
   * @return The folder as an existing {@link File}.
   */
    public static File getOrCreateFolder(String resultFolder) {
        final File folder = new File(resultFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    /**
   * If the file does not already exit, it is created, including any parent
   * directories.
   * 
   * @param file
   *          The file to ensure.
   * @throws IOException
   *           If an exception occurs.
   */
    public static File ensureFile(File file) throws IOException {
        getOrCreateFolder(file.getParent());
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
   * Returns the specified file. If the file does not already exit, it is
   * created, including any parent directories.
   * 
   * @param name
   *          The name of the file.
   * @return The result as a {@link File}.
   * @throws IOException
   *           If an exception occurs.
   */
    public static File getOrCreateFile(String name) throws IOException {
        File file = new File(name);
        ensureFile(file);
        return file;
    }

    /**
   * Copies a file.
   * 
   * @param from
   *          The origination file.
   * @param to
   *          The destination file.
   * @throws IOException
   *           If an error occurs.
   */
    public static void copyFile(File from, File to) throws IOException {
        ensureFile(to);
        FileChannel srcChannel = new FileInputStream(from).getChannel();
        FileChannel dstChannel = new FileOutputStream(to).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
}
