package org.pluginbuilder.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy {

    private static final int BUFFER_SIZE = 1024;

    public static void copyDirectory(final File sourceDir, final File destinationDir) throws IOException {
        if (sourceDir.getName().equals(".svn")) {
            return;
        }
        checkSourceDir(sourceDir);
        checkDestinationDir(destinationDir);
        File[] children = sourceDir.listFiles();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                copy(new File(sourceDir, child.getName()), new File(destinationDir, child.getName()));
            }
        }
    }

    public static void copyFile(final File sourceFile, final File destinationFile) throws IOException {
        destinationFile.getParentFile().mkdirs();
        FileInputStream fis = new FileInputStream(sourceFile);
        try {
            FileOutputStream fos = new FileOutputStream(destinationFile);
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = fis.read(buffer);
                while (bytesRead != -1) {
                    fos.write(buffer, 0, bytesRead);
                    bytesRead = fis.read(buffer);
                }
            } finally {
                fos.close();
            }
        } finally {
            fis.close();
        }
    }

    private static void copy(final File sourceDir, final File destination) throws IOException {
        if (sourceDir.isDirectory()) {
            copyDirectory(sourceDir, destination);
        } else if (sourceDir.isFile()) {
            copyFile(sourceDir, destination);
        }
    }

    private static void checkSourceDir(final File sourceDir) {
        if (sourceDir == null) {
            throw new NullPointerException("Parameter sourceDir must not be null.");
        }
        if (!sourceDir.exists()) {
            throw new IllegalArgumentException("Parameter sourceDir does not extist on the filesystem.");
        }
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Parameter sourceDir is not a directory.");
        }
    }

    private static void checkDestinationDir(final File destDir) {
        if (destDir == null) {
            throw new NullPointerException("Parameter sourceDir must not be null.");
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        if (!destDir.isDirectory()) {
            throw new IllegalArgumentException("Parameter sourceDir is not a directory.");
        }
    }
}
