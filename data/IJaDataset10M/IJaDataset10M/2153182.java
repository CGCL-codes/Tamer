package org.dragonfly.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class FileUtil {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private FileUtil() {
    }

    public static void copy(final InputStream input, final OutputStream output) throws IOException {
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }

    public static void unjar(JarFile jar, JarEntry entry, String outputDirectory) throws IOException {
        InputStream is = null;
        FileOutputStream out = null;
        try {
            is = jar.getInputStream(entry);
            File outFile = new File(outputDirectory, entry.getName());
            if (!outFile.getParentFile().exists()) FileUtil.mkDir(outFile.getParentFile().getAbsolutePath());
            out = new FileOutputStream(outFile);
            copy(is, out);
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void unjar(JarFile jar, String entryName, String ouputDir) throws IOException {
        Enumeration<JarEntry> enums = jar.entries();
        while (enums.hasMoreElements()) {
            JarEntry entry = enums.nextElement();
            if (!entry.isDirectory() && entry.getName().startsWith(entryName)) {
                unjar(jar, entry, ouputDir);
            }
        }
    }

    public static void mkDir(String dir) {
        File target = new File(dir);
        if (!target.exists()) {
            if (!target.mkdirs()) throw new RuntimeException("The Directory can't create:" + target.getPath());
        }
    }

    public static void mkDir(String parentDir, String dirName) {
        File target = new File(parentDir + File.separatorChar + dirName);
        if (!target.exists()) {
            if (!target.mkdirs()) throw new RuntimeException("The Directory can't create:" + target.getPath());
        }
    }

    public static void copyFile(File file, String destDir) throws IOException {
        if (!isCanReadFile(file)) throw new RuntimeException("The File can't read:" + file.getPath());
        if (!isCanWriteDirectory(destDir)) throw new RuntimeException("The Directory can't write:" + destDir);
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new FileInputStream(file).getChannel();
            dstChannel = new FileOutputStream(destDir + "/" + file.getName()).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } catch (IOException e) {
            throw e;
        } finally {
            if (srcChannel != null) try {
                srcChannel.close();
            } catch (IOException e) {
            }
            if (dstChannel != null) try {
                dstChannel.close();
            } catch (IOException e) {
            }
        }
    }

    public static void copyFile(String srcFile, String destDir) throws IOException {
        copyFile(new File(srcFile), destDir);
    }

    public static void copySubFiles(String srcDir, String destDir) throws IOException {
        if (!isCanReadDirectory(srcDir)) throw new RuntimeException("The Directory can't read:" + srcDir);
        if (!isCanWriteDirectory(destDir)) FileUtil.mkDir(destDir);
        File dir = new File(srcDir + "/");
        File[] files = dir.listFiles(new DefaultFileFilter());
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    copyFile(file, destDir);
                } else {
                    FileUtil.copySubFiles(file.getPath(), destDir + File.separatorChar + file.getName());
                }
            }
        }
    }

    public static boolean deleteAllSubFiles(String path) {
        File f = new File(path);
        if (!f.exists()) return false;
        if (f.isDirectory()) {
            File delFile[] = f.listFiles();
            int len = delFile.length;
            for (int j = 0; j < len; j++) {
                if (delFile[j].isDirectory()) {
                    deleteAllSubFiles(delFile[j].getAbsolutePath());
                }
                delFile[j].delete();
            }
        }
        return true;
    }

    public static boolean deleteAll(String path, boolean needDelSelf) {
        File f = new File(path);
        if (!f.exists() || !f.isDirectory()) return false;
        deleteAllSubFiles(f.getAbsolutePath());
        if (needDelSelf) f.delete();
        return true;
    }

    public static boolean isCanReadFile(File file) {
        return file.exists() && file.isFile() && file.canRead();
    }

    public static boolean isCanReadDirectory(String destDir) {
        File dir = new File(destDir);
        return dir.exists() && dir.isDirectory() && dir.canRead();
    }

    public static boolean isCanReadDirectory(File dir) {
        return dir != null && dir.exists() && dir.isDirectory() && dir.canRead();
    }

    public static boolean isCanWriteDirectory(String destDir) {
        File dir = new File(destDir);
        return dir.exists() && dir.isDirectory() && dir.canWrite();
    }

    public static void appendTextFile(String destDir, String fileName, String content) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(destDir + File.separatorChar + fileName, true));
            bw.write(content);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException e) {
            }
        }
    }

    /**
	 * Create an encoding text-file.
	 * 
	 * @param destDir
	 * @param fileName
	 * @param content
	 * @param encoding
	 * @throws Exception
	 */
    public static void createTextFile(String destDir, String fileName, String content) throws IOException {
        if (!isCanWriteDirectory(destDir)) mkDir(destDir);
        FileWriter writer = new FileWriter(new File(destDir + File.separatorChar + fileName));
        try {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
            }
            writer = null;
        }
    }

    /**
	 * List all files and directories of the directories.
	 * 
	 * @param directory
	 * @return Array of Files
	 */
    public static File[] listFilesAndDirs(String directory) {
        return listFilesAndDirs(directory, null);
    }

    /**
	 * List all files and directories of the directories with file suffix.
	 * 
	 * @param directory
	 * @param suffix
	 * @return Array of Files
	 */
    public static File[] listFilesAndDirs(String directory, String suffix) {
        File dir = new File(directory);
        if (!dir.isDirectory()) return null;
        return dir.listFiles(new DefaultFileFilter(suffix));
    }

    /**
	 * List only files of the directories.
	 * 
	 * @param directory
	 * @return Array of Files
	 */
    public static List<File> listFilesOnly(String directory) {
        return listFilesOnly(directory, null);
    }

    /**
	 * List all files of the directories with file suffix.
	 * 
	 * @param directory
	 * @param suffix
	 * @return Array of Files
	 */
    public static List<File> listFilesOnly(String directory, String suffix) {
        File[] listFiles = listFilesAndDirs(directory, suffix);
        if (listFiles == null) return null;
        List<File> files = new ArrayList<File>();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isFile()) files.add(listFiles[i]);
        }
        return files;
    }

    /**
	 * List all directories of the directories.
	 * 
	 * @param directory
	 * @return Array of Files
	 */
    public static List<File> listDirsOnly(String directory) {
        File[] listFiles = listFilesAndDirs(directory);
        if (listFiles == null) return null;
        List<File> dirs = new ArrayList<File>();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory()) dirs.add(listFiles[i]);
        }
        return dirs;
    }

    /**
	 * List all files under the directories.
	 * 
	 * @param directory
	 * @return Array of Files
	 */
    public static List<File> treeFilesOnly(String directory) {
        return treeFilesOnly(directory, null);
    }

    /**
	 * List all files under the directories with file suffix.
	 * 
	 * @param directory
	 * @param suffix
	 * @return Array of Files
	 */
    public static List<File> treeFilesOnly(String directory, String suffix) {
        return iterTreeFiles(directory, suffix, null);
    }

    /**
	 * Get all files under the directories by self.
	 * 
	 * @param directory
	 * @param files
	 * @return Array of Files
	 */
    private static List<File> iterTreeFiles(String directory, String suffix, List<File> files) {
        if (files == null) files = new ArrayList<File>();
        List<File> subFiles = listFilesOnly(directory, suffix);
        List<File> subDirs = listDirsOnly(directory);
        if (subFiles != null) {
            for (int i = 0; i < subFiles.size(); i++) {
                files.add(subFiles.get(i));
            }
        }
        if (subDirs != null) {
            for (int i = 0; i < subDirs.size(); i++) {
                iterTreeFiles((subDirs.get(i)).getPath(), suffix, files);
            }
        }
        return files;
    }

    /**
	 * Judge the file type.
	 * 
	 * @param file
	 * @param fileType
	 * @return Yes or No
	 */
    public static boolean isWhatFile(File file, String fileType) {
        return file.isFile() && file.getName().toLowerCase().endsWith(fileType.toLowerCase());
    }

    public static void main(String[] args) {
        FileUtil.deleteAll("D:\\work\\org", true);
    }
}
