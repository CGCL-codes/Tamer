package org.lamb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static void deleteDirectory(String directory) throws IOException {
        deleteDirectory(new File(directory));
    }

    public static void deleteDirectory(File dir) throws IOException {
        if (!dir.exists()) return;
        if (dir.isFile()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                deleteDirectory(file);
            }
        }
        dir.delete();
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination is not a directory");
        }
        copyFile(srcFile, new File(destDir, srcFile.getName()));
    }

    private static void copyFile(File source, File destination) throws IOException {
        if (!source.exists()) throw new IOException("File " + source + " does not exist");
        if (destination.getParentFile() != null && !destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
        }
        if (destination.exists() && !destination.canWrite()) {
            String message = "Unable to open file " + destination + " for writing.";
            throw new IOException(message);
        }
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(destination);
            final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } finally {
            input.close();
            output.close();
        }
        if (source.length() != destination.length()) {
            String message = "Failed to copy full contents from " + source + " to " + destination;
            throw new IOException(message);
        }
    }

    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) throw new IOException("source directory can't be null.");
        if (destDir == null) throw new IOException("destination directory can't be null.");
        if (!srcDir.exists()) throw new IOException("Source directory doesn't exists (" + srcDir.getAbsolutePath() + ").");
        if (srcDir.equals(destDir)) throw new IOException("source and destination are the same directory.");
        if (destDir.getAbsolutePath().startsWith(srcDir.getAbsolutePath())) throw new IOException("The destination folder is a subfolder of the source folder.");
        File dest = new File(destDir, srcDir.getName());
        if (!dest.exists()) dest.mkdirs();
        copyDirectoryStructure(srcDir, dest, false);
    }

    private static void copyDirectoryStructure(File srcDir, File destDir, boolean onlyModifiedFiles) throws IOException {
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            File destination = new File(destDir, file.getName());
            if (file.isFile()) {
                copyFile(file, destination);
            } else if (file.isDirectory()) {
                if (!destination.exists() && !destination.mkdirs()) {
                    throw new IOException("Could not create destination directory '" + destination.getAbsolutePath() + "'.");
                }
                copyDirectoryStructure(file, destination, onlyModifiedFiles);
            } else {
                throw new IOException("Unknown file type: " + file.getAbsolutePath());
            }
        }
    }
}
