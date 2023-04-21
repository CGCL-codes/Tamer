package org.deft.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

public class FileUtil {

    /**
	 * Recursively deletes a directory and its content
	 */
    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        boolean result = directory.delete();
        return result;
    }

    public static boolean saveToFile(byte[] content, File file, boolean overwrite) {
        if (file.exists() && !overwrite) {
            throw new IllegalArgumentException("File " + file.getAbsolutePath() + " already exists.");
        }
        try {
            if (file.exists() && overwrite) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content);
            fos.close();
            return true;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public static boolean saveToFile(URL sourceUrl, File targetFile, boolean overwrite) {
        if (targetFile.exists() && !overwrite) {
            throw new IllegalArgumentException("File " + targetFile.getAbsolutePath() + " already exists.");
        }
        try {
            if (targetFile.exists() && overwrite) {
                targetFile.delete();
            }
            targetFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(targetFile);
            InputStream is = sourceUrl.openStream();
            byte[] content = StreamUtil.readByteContent(is);
            is.close();
            fos.write(content);
            fos.close();
            return true;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public static File createTempFile(byte[] content) {
        try {
            File tmpFile = File.createTempFile("dft", null);
            saveToFile(content, tmpFile, true);
            tmpFile.deleteOnExit();
            return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File createTempFile(URL url) {
        try {
            InputStream is = url.openStream();
            byte[] content = StreamUtil.readByteContent(is);
            File tmpFile = File.createTempFile("dft", null);
            saveToFile(content, tmpFile, true);
            return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getTempDir() {
        String tempDir = System.getProperty("java.io.tmpdir");
        File dir = new File(tempDir);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        }
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (success) {
                return dir;
            }
        }
        throw new RuntimeException("Could not get temp directory: " + tempDir);
    }

    public static File getUniqueSubDirIn(File parentDir) {
        String dirName = parentDir.getAbsolutePath() + "/" + System.currentTimeMillis();
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        }
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (success) {
                return dir;
            }
        }
        throw new RuntimeException("Could not get temp directory: " + dirName);
    }

    public static File getTempDirWithUniqueSubDir() {
        String tempDirName = System.getProperty("java.io.tmpdir");
        File tempDir = new File(tempDirName);
        File tempSubDir = getUniqueSubDirIn(tempDir);
        return tempSubDir;
    }

    /**
	 * Copies a file or directory into another directory 
	 */
    public static boolean copy(File source, File destDir) {
        if (!source.exists()) {
            throw new IllegalArgumentException("source file or dir does not exist");
        }
        if (!destDir.exists()) {
            throw new IllegalArgumentException("dest dir does not exist");
        }
        if (source.isFile()) {
            boolean result = doCopyFile(source, destDir);
            return result;
        } else if (source.isDirectory()) {
            boolean result = doCopyDir(source, destDir);
            return result;
        }
        return false;
    }

    private static boolean doCopyFile(File sourceFile, File destDir) {
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            String fileName = sourceFile.getName();
            File destFile = new File(destDir, fileName);
            from = new FileInputStream(sourceFile);
            to = new FileOutputStream(destFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead);
            }
            from.close();
            to.close();
            return true;
        } catch (Exception e) {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException ioe) {
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException ioe) {
                }
            }
        }
        return false;
    }

    private static boolean doCopyDir(File sourceLocation, File targetLocation) {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            File destDir = new File(targetLocation, sourceLocation.getName());
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String[] children = sourceLocation.list();
            boolean result = true;
            for (int i = 0; i < children.length; i++) {
                result |= doCopyDir(new File(sourceLocation, children[i]), destDir);
            }
            return result;
        } else {
            boolean fileCopyResult = doCopyFile(sourceLocation, targetLocation);
            return fileCopyResult;
        }
    }

    public static File[] getFilesFromDirectory(File directory) {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                boolean accept = pathname.isFile();
                return accept;
            }
        };
        if (directory.isDirectory()) {
            File[] files = directory.listFiles(filter);
            return files;
        }
        return new File[] {};
    }

    public static File[] getSubDirectories(File directory) {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                boolean accept = pathname.isDirectory();
                return accept;
            }
        };
        if (directory.isDirectory()) {
            File[] dirs = directory.listFiles(filter);
            return dirs;
        }
        return new File[] {};
    }

    public static String getCommonRootDirectory(Collection<String> paths) {
        String result = null;
        for (String path : paths) {
            String directory = path.substring(0, Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\")));
            if (result == null) result = directory; else {
                if (!directory.startsWith(result)) {
                    while (!directory.startsWith(result)) {
                        result = result.substring(0, Math.max(result.lastIndexOf("/"), result.lastIndexOf("\\")));
                    }
                }
            }
        }
        return result;
    }
}
