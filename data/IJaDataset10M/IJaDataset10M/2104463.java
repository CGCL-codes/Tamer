package org.bluesock.bluemud.driver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;

/**
 * The FileManager is responsible for the management of all access
 * to disk, including reading files, writing files, providing
 * directory listings, etc. All disk access should go through this
 * class.
 *
 * TODO: Add decent error handling. At the moment, this class just
 * dumps a stack trace and returns failure.
 */
public class FileManager {

    /**
   * Write a file to disk.
   *
   * @param content The content to write to a file.
   * @param filename The name of the file to write to.
   *
   * @returns true if the write was successful, false otherwise.
   */
    static boolean writeFile(String content, String filename) {
        return writeFile(content, filename, false);
    }

    /**
   * Write a file to disk.
   *
   * @param content The content to write to a file.
   * @param filename The name of the file to write to.
   * @param append Whether to append to the end of the file or
   *               just replace the file.
   *
   * @returns true if the write was successful, false otherwise.
   */
    static boolean writeFile(String content, String filename, boolean append) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename, append));
            out.write(content.toCharArray());
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("FileManager: Could not write file.");
            e.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.err.println("FileManager: Could not close file.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
   * Creates a new empty file with the specified path if that file
   * doesn't already exist.
   *
   * @param filename The path to the file to create.
   *
   * @return true if the file was created, false otherwise.
   */
    static boolean touchFile(String filename) {
        try {
            File f = new File(filename);
            return f.createNewFile();
        } catch (IOException e) {
            System.err.println("FileManager: Could not touch file.");
            e.printStackTrace();
            return false;
        }
    }

    /**
   * Read a file from disk and don't cache it.
   *
   * @param filename The name of the file to read.
   *
   * @returns The contents of the file, as a String, or null if
   *          the file could not be read successfully.
   */
    static String readFile(String filename) {
        return readFile(filename, false);
    }

    /**
   * Read a file from disk.
   *
   * @param filename The name of the file to read.
   * @param cachethis True if we want to cache the file
   *
   * @returns The contents of the file, as a String, or null if
   *          the file could not be read successfully.
   */
    static String readFile(String filename, boolean cachethis) {
        LazyCache lc = LazyCache.instance();
        if (cachethis == true && lc.contains(filename)) {
            return (String) lc.get(filename);
        }
        BufferedReader in = null;
        char[] buffer = new char[4096];
        int bytesRead = 0;
        StringBuffer contents = new StringBuffer();
        try {
            in = new BufferedReader(new FileReader(filename));
            do {
                bytesRead = in.read(buffer);
                if (bytesRead > 0) {
                    contents.append(buffer, 0, bytesRead);
                }
            } while (bytesRead != -1);
            if (cachethis == true) {
                lc.put(filename, contents.toString());
            }
            return contents.toString();
        } catch (FileNotFoundException e) {
            System.err.println("FileManager: File not found: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("FileManager: could not read file");
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.err.println("FileManager: could not close file");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
   * Returns the directory contents as an array of Strings.
   * Throws an IOException for almost any issues.
   *
   * @returns a String[] of the contents of dirname if it
   *          exists.
   */
    static String[] listDirectory(String dirname) throws IOException {
        File f = new File(dirname);
        if (!f.isDirectory()) {
            throw new IOException("Not a directory.");
        }
        return f.list();
    }

    /**
   * Returns an array of strings in this order:
   *   file name
   *   length (size)
   *   last modified
   *
   * Throws an IOException for almost any issues.
   *
   * @returns a String[] of attributes about the file
   */
    static String[] fstatFile(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists() || !f.isFile()) throw new IOException("File does not exist.");
        String d = (new Date(f.lastModified())).toString();
        return new String[] { f.getName(), String.valueOf(f.length()), d };
    }

    /**
   * Allows you to discern if a certain path is a directory or
   * not.
   *
   * @param dirname name of the directory to query.
   * @return boolean true if it's a directory and false if not.
   */
    static boolean isDirectory(String dirname) throws IOException {
        File f = new File(dirname);
        return f.isDirectory();
    }

    /**
   * Allows you to discern if a certain file is a file
   * or not.
   *
   * @param filename name of the file in question.
   * @return boolean true if it is a file.
   */
    static boolean isFile(String filename) throws IOException {
        File f = new File(filename);
        return f.isFile();
    }

    /**
   * Removes a directory.
   *
   * @param dirname name of the directory to remove.
   * @return boolean true if it removed the directory and false if
   *         it didn't.
   */
    static boolean removeDirectory(String dirname) throws IOException {
        File f = new File(dirname);
        if (!f.exists()) {
            throw new IOException("Directory does not exist.");
        }
        if (!f.isDirectory()) {
            throw new IOException("Not a directory.");
        }
        return f.delete();
    }

    /**
   * Removes a file.
   *
   * @param filename name of the file to re move.
   * @return true if the file was successfully removed, false otherwise.
   */
    static boolean removeFile(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new IOException("File does not exist.");
        } else {
            return f.delete();
        }
    }

    /**
   * Creates a directory.
   *
   * @param dirname name of the directory to create.
   * @return boolean true if it made the directory and false if
   *         it didn't.
   */
    static boolean createDirectory(String dirname) throws IOException {
        File f = new File(dirname);
        if (f.exists()) {
            throw new IOException("File/directory exists.");
        }
        return f.mkdirs();
    }
}
