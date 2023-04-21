package org.happy.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import com.sun.istack.internal.NotNull;

/**
 * a helper class to zip and unzip data
 * Heer is a good introduction to the zip-package:
 * http://java.sun.com/developer/technicalArticles/Programming/compression/
 * @author Andreas Hollmann
 *
 */
public class Zip_1x0 {

    private static final int BUFFER = 2048;

    /**
	 * extract content of a zip archive to a directory in the file system. This method don't support the unzipping of sub-directories inside zip archive
	 * @param zipFile file to the zip archive
	 * @param destDir destanation directory
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
    public static void extractZipToFileSystem(@NotNull File zipFile, @NotNull File destDir) throws IOException, IllegalArgumentException {
        if (zipFile == null) {
            throw new IllegalArgumentException("zipFile can't be null!");
        }
        if (!zipFile.exists()) {
            throw new IllegalArgumentException("zipFile " + zipFile.getCanonicalPath() + " don't exist");
        }
        if (destDir == null) {
            throw new IllegalArgumentException("zipFile can't be null!");
        }
        if (!destDir.exists()) {
            throw new IllegalArgumentException("directory " + destDir.getCanonicalPath() + " don't exist");
        }
        if (!destDir.isDirectory()) {
            throw new IllegalArgumentException("directory " + destDir.getCanonicalPath() + " is a file, but the directory is expected!");
        }
        FileInputStream fis = new FileInputStream(zipFile);
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) {
            File file = new File(destDir.getCanonicalPath() + "\\" + entry.getName());
            if (!file.createNewFile()) {
                throw new IllegalArgumentException("file " + file.getCanonicalPath() + " couldn't be created!");
            }
            if (!file.canWrite()) {
                throw new IllegalArgumentException("file " + file.getCanonicalPath() + " can't be written!");
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
            int count = 0;
            byte[] data = new byte[BUFFER];
            while ((count = zin.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
        zin.close();
    }

    /**
	 * makes a new zip archive from contens of the folder
	 * Note: Entries can be added to a ZIP file either in a compressed (DEFLATED) or uncompressed (STORED) form. 
	 * The setMethod can be used to set the method of storage. 
	 * For example, to set the method to DEFLATED (compressed) use: out.setMethod(ZipOutputStream.DEFLATED) and 
	 * to set it to STORED (not compressed) use: out.setMethod(ZipOutputStream.STORED). 
	 * read more http://java.sun.com/developer/technicalArticles/Programming/compression/
	 * @param folder folder which should be used to create zip archive
	 * @param toZipFile the file which should be used to save created zip archive
	 * @param method 
	 * @throws IOException 
	 * @throws IllegalArgumentException
	 */
    public static void makeArchiveFromFolder(@NotNull File folder, File toZipFile, int method) throws IOException, IllegalArgumentException {
        if (folder == null) {
            throw new IllegalArgumentException("the folder can't be null!");
        }
        if (!folder.exists()) {
            throw new IllegalArgumentException("the folder = " + folder.getAbsolutePath() + " don't exist!");
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("the folder = " + folder.getAbsolutePath() + " is a file and not a folder!");
        }
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(toZipFile);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        out.setMethod(method);
        byte data[] = new byte[BUFFER];
        File files[] = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            FileInputStream fi = new FileInputStream(files[i]);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(files[i].getName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
    }
}
