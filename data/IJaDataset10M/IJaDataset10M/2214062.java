package ie.ucd.searchengine.repository;

import java.io.File;
import java.util.Vector;

/**
 * Repesents a filesystem (a collection of files and folders).
 * This class treats ZIP and JAR files as directories
 * @author Brendan Maguire
 */
public class FileSystem implements IFileSystem {

    private File root = null;

    /**
	 * Initializes the filesystem with the specified root
	 * @param s Root of the filesystem
	 */
    public FileSystem(String s) {
        if (s != null && !s.equals("")) root = new de.schlichtherle.io.File(s);
    }

    /**
	 * Returns the root of the filesystem
	 * @return Returns the root of the filesystem
	 */
    public File getFileSystemRoot() {
        return root;
    }

    /**
	 * Returns all files in the main directory of the filesystem including the filesystem root (if 
	 * the filesystem root is a file)
	 * @return returns all files in the main directory of the filesystem
	 */
    public Vector<File> getFiles() {
        if (root == null || !root.exists()) return new Vector<File>();
        Vector<File> files = new Vector<File>();
        File[] filesOrDirectories = root.listFiles();
        if (filesOrDirectories == null) {
            if (root.isFile()) files.add(root);
            return files;
        }
        for (File f : filesOrDirectories) {
            if (f.isFile()) files.add(f);
        }
        return files;
    }

    /**
	 * Returns all directories in the main directory of the filesystem including the filesystem root (if 
	 * the filesystem root is a directory)
	 * @return returns all directories in the main directory of the filesystem
	 */
    public Vector<File> getDirectories() {
        if (root == null || !root.exists()) return new Vector<File>();
        Vector<File> directories = new Vector<File>();
        File[] filesOrDirectories = root.listFiles();
        if (root.isDirectory()) directories.add(root);
        if (filesOrDirectories == null) {
            return directories;
        }
        for (File f : filesOrDirectories) {
            if (f.isDirectory()) directories.add(f);
        }
        return directories;
    }

    /**
	 * Recursively returns all the files in the filesystem including the filesystem root (if 
	 * the filesystem root is a file)
	 * @return Recursively returns all files in the filesystem
	 */
    public Vector<File> getAllFiles() {
        if (root == null || !root.exists()) return new Vector<File>();
        Vector<File> files = new Vector<File>();
        Vector<File> filesOrDirectories = new Vector<File>();
        filesOrDirectories.add(root);
        while (!filesOrDirectories.isEmpty()) {
            File f = filesOrDirectories.remove(0);
            if (f.isFile()) {
                files.add(f);
            } else {
                File[] tempFiles = f.listFiles();
                if (tempFiles != null) {
                    for (File f1 : tempFiles) {
                        filesOrDirectories.add(f1);
                    }
                }
            }
        }
        return files;
    }

    /**
	 * Recursively returns all the directories in the filesystem including the filesystem root (if 
	 * the filesystem root is a directories)
	 * @return Recursively returns all directories in the filesystem
	 */
    public Vector<File> getAllDirectories() {
        if (root == null || !root.exists()) return new Vector<File>();
        Vector<File> directories = new Vector<File>();
        Vector<File> filesOrDirectories = new Vector<File>();
        filesOrDirectories.add(root);
        while (!filesOrDirectories.isEmpty()) {
            File f = filesOrDirectories.remove(0);
            if (f.isDirectory()) {
                directories.add(f);
                File[] tempFiles = f.listFiles();
                if (tempFiles != null) {
                    for (File f1 : tempFiles) {
                        filesOrDirectories.add(f1);
                    }
                }
            }
        }
        return directories;
    }
}
