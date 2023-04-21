package xplanetconfigurator.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *<br>
 * $Author: wiedthom $<br>
 * $Date: 2006/09/26 21:15:35 $<br>
 * $Revision: 1.3 $<br>
 */
public class FileUtil {

    public static final String KEY_FILE_ARCHIVE_SPLITTER = " @ ZIP-Archive ";

    private Logger logger;

    public FileUtil() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public void printFile(String file, String content) throws Exception {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.print(content);
            writer.close();
            writer = null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ioe) {
                }
            }
        }
    }

    public void appendLineToFile(String file, String line) throws Exception {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            writer.println(line);
            writer.close();
            writer = null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ioe) {
                }
            }
        }
    }

    /**
     * Gets the content of a file in the classpath as String.
     * @param fileInClasspath The file to read. Example: /com/mycompany/test/myfile
     * @return The file content as String
     */
    public String getRessourceAsString(String fileInClasspath) throws Exception {
        byte[] buffer = new byte[4096];
        BufferedInputStream in = null;
        java.io.ByteArrayOutputStream w = null;
        try {
            in = new BufferedInputStream(this.getClass().getResourceAsStream(fileInClasspath));
            w = new ByteArrayOutputStream();
            int x = 0;
            while ((x = in.read(buffer)) != -1) {
                w.write(buffer, 0, x);
            }
            in.close();
            in = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return w.toString();
    }

    public String copyRessourceFileToFileSystem(String fromFileInClasspath, String toFileInFileSystem) throws Exception {
        String fileContent = getRessourceAsString(fromFileInClasspath);
        printFile(toFileInFileSystem, fileContent);
        return fileContent;
    }

    /**
     * Uses an underlying DataInputStream (no String conversion).
     * @param fromFileInClasspath
     * @param toFileInFileSystem
     */
    public void copyRessourceToFileSystem(String fromFileInClasspath, String toFileInFileSystem) throws Exception {
        logger.finest("About to do a binary copy from file (classpath) '" + fromFileInClasspath + "' to (file system) '" + toFileInFileSystem + "' using an underlying DataOutputStream...");
        long byteCount = 0;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(this.getClass().getResourceAsStream(fromFileInClasspath));
            out = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(toFileInFileSystem)));
            byte[] bbuf = new byte[4096];
            int length = -1;
            while ((length = in.read(bbuf)) != -1) {
                out.write(bbuf, 0, length);
                byteCount = byteCount + length;
            }
            in.close();
            in = null;
            out.close();
            out = null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to copy (binary) file from '" + fromFileInClasspath + "' to '" + toFileInFileSystem + "'." + e.getMessage(), e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public String getFileAsString(File file) throws Exception {
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = reader.readLine()) != null) {
                buffer.append(s).append("\n");
            }
        } catch (SecurityException e) {
            throw new Exception("Failed to load File '" + file.toString() + "'. \n" + "Not sufficient Rigths.\n" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Failed to load File '" + file.toString() + "'. \n" + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ioe) {
                }
            }
        }
        String ret = buffer.toString();
        if (ret.endsWith("\n")) {
            ret = ret.substring(0, ret.lastIndexOf("\n"));
        }
        return ret;
    }

    /**
     * Gets the content of a file as String.
     * The internal implementation uses a character array.
     *
     * @param file The file to read
     * @return The file content as String.
     */
    public String getFileContent(File file) throws Exception {
        StringBuffer buf = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            char[] cbuf = new char[4096];
            while (in.read(cbuf) != -1) {
                buf.append(cbuf);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ioe) {
                }
            }
        }
        char[] cArray = { (char) 0 };
        String emptyChar = new String(cArray);
        String ret = null;
        int index = buf.indexOf(emptyChar);
        if (index != -1) {
            ret = buf.substring(0, index);
        } else {
            ret = buf.toString();
        }
        return ret;
    }

    /**
     * Does a binary file copy of a part of a file.
     *
     * @param fromFile Absolute path
     * @param toFile Absolute path
     * @param startIndex start index in file 'fromFile'
     * @param endIndex end index in file 'fromFile'
     */
    public void copyPartOfFile(String fromFile, String toFile, int startIndex, int endIndex) throws Exception {
        if (startIndex >= endIndex) {
            throw new Exception("Start index '" + startIndex + "' greater or equal end index '" + endIndex + "'.");
        }
        long byteCount = 0;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new DataInputStream(new FileInputStream(fromFile)));
            out = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(toFile)));
            long skippedBytes = in.skip((long) startIndex);
            if (skippedBytes != startIndex) {
                out.close();
                out = null;
                File f = new File(toFile);
                if (f.exists()) {
                    f.delete();
                }
                if (!f.createNewFile()) {
                    throw new Exception("Failed to create file '" + toFile + "'.");
                }
            } else {
                int lengthOfFilePart = endIndex - startIndex;
                byte[] buf = new byte[lengthOfFilePart];
                int length = -1;
                if ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                    byteCount = byteCount + length;
                }
                out.close();
                out = null;
                if (length == -1) {
                    File f = new File(toFile);
                    if (f.exists()) {
                        f.delete();
                    }
                    if (!f.createNewFile()) {
                        throw new Exception("Failed to create file '" + toFile + "'.");
                    }
                }
            }
            in.close();
            in = null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to copy (binary) file from '" + fromFile + "' to '" + toFile + "'." + e.getMessage(), e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Does a binary file copy
     * @param from Absolute path
     * @param to Absolute path
     */
    public void copyFile(String from, String to) throws Exception {
        long byteCount = 0;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new DataInputStream(new FileInputStream(from)));
            out = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(to)));
            byte[] bbuf = new byte[4096];
            int length = -1;
            while ((length = in.read(bbuf)) != -1) {
                out.write(bbuf, 0, length);
                byteCount = byteCount + length;
            }
            in.close();
            in = null;
            out.close();
            out = null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to copy (binary) file from '" + from + "' to '" + to + "'." + e.getMessage(), e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Copies a file matching a certain filter (regular search expression) from one
     * directory to another therby replacing a string the filter. The first found file
     * is copied only.
     * <h3>Example:</h3>
     * Provided you want to copy a file that matches the regular expression
     * (aa_)(.* )(_bb.txt) for example:
     * <ul>
     * 	<li>
     * 		c:/tmp/aa_YYYYYY_bb.txt
     * 	</li>
     * 	<li>
     * 		c:/tmp/aa_1234_bb.txt
     * 	</li>
     * 	<li>
     * 		c:/tmp/aa_cccc_bb.txt
     * 	</li>
     * </ul>
     * to "c:/myDir/aa_XXX_bb.txt".
     * The part ".*" (for example the "YYYYYY" or "1234" or "cccc") should be replaced by "XXX" in
     * the copied file.
     *
     *
     * @param fromDir Example: c:/tmp
     * @param toDir Example: c:/myDir
     * @param fileNameAsSearchExpression Example: (aa)(.*)(bb.txt)
     * @param replacement Example: $1XXX$3 where "$1" matches the first group "(aa_)",
     * "XXX" is inserted and the "$3" matches the third group "_bb.txt".
     * @return The absolute path of the copied file: Example: c/myDir/aa_XXX_bb.txt or null if nothing found to copy.
     */
    public String copyFile(String fromDir, String toDir, String fileNameAsSearchExpression, String replacement) throws Exception {
        Pattern pattern = Pattern.compile(fileNameAsSearchExpression);
        String foundFileNameToCopy = null;
        File directory = new File(fromDir);
        File[] files = directory.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                continue;
            }
            foundFileNameToCopy = file.getName();
            java.util.regex.Matcher matcher = pattern.matcher(foundFileNameToCopy);
            if (matcher.find()) {
                break;
            } else {
            }
        }
        if (foundFileNameToCopy == null) {
            return null;
        }
        AppendReplacement replacer = new AppendReplacement(foundFileNameToCopy, fileNameAsSearchExpression, replacement);
        String replacedFileName = replacer.replace();
        String fromFile = fromDir + File.separator + foundFileNameToCopy;
        String toFile = toDir + File.separator + replacedFileName;
        try {
            this.copyFile(fromFile, toFile);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to copy file from '" + fromFile + "' to file '" + toFile + "'.", e);
            throw e;
        }
        return toFile;
    }

    /**
     * Compares two files. The files can be binary and text files.
     * The internal implementation use DataInputStreams and compares the byte arrays of
     * both files.
     *
     * @param file1 Absolute path
     * @param file2 Absolute path
     * return true if equal, false otherwise
     */
    public boolean compareFiles(String file1, String file2) throws Exception {
        boolean areEqual = true;
        long byteCount = 0;
        BufferedInputStream in1 = null;
        BufferedInputStream in2 = null;
        try {
            in1 = new BufferedInputStream(new DataInputStream(new FileInputStream(file1)));
            in2 = new BufferedInputStream(new DataInputStream(new FileInputStream(file2)));
            byte[] bbuf1 = new byte[4096];
            byte[] bbuf2 = new byte[4096];
            int length1 = -1;
            int length2 = -1;
            while ((length1 = in1.read(bbuf1)) != -1) {
                if ((length2 = in2.read(bbuf2)) != -1) {
                    if (Arrays.equals(bbuf1, bbuf2)) {
                        byteCount = byteCount + length1;
                        continue;
                    } else {
                        areEqual = false;
                    }
                } else {
                    areEqual = false;
                }
            }
            if ((length1 = in1.read(bbuf1)) != -1) {
                areEqual = false;
            }
            in1.close();
            in1 = null;
            in2.close();
            in2 = null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to to compare files '" + file1 + "' to '" + file2 + "'." + e.getMessage(), e);
            throw e;
        } finally {
            if (in1 != null) {
                try {
                    in1.close();
                } catch (Exception e) {
                }
            }
            if (in2 != null) {
                try {
                    in2.close();
                } catch (Exception e) {
                }
            }
        }
        return areEqual;
    }

    /**
     * Deletes the last lines of a file and returns the new file content.
     *
     * @param file The file where to delete the last lines
     * @param numberOfLines How many lines to remove at the end of the file
     * @return The new content of the file
     * @throws Exception
     */
    public String deleteLastLines(String file, int numberOfLines) throws Exception {
        BufferedReader in = null;
        StringBuffer buffer = new StringBuffer();
        String msg = "Reading file '" + file + "'.";
        try {
            in = new BufferedReader(new FileReader(file));
            String line = null;
            List lines = new ArrayList();
            long counter = 0;
            while ((line = in.readLine()) != null) {
                lines.add(line);
                if (counter >= numberOfLines) {
                    String lineToAdd = (String) lines.get(0);
                    buffer.append(lineToAdd);
                    if (buffer.length() > 0) {
                        buffer.append("\n");
                    }
                    lines.remove(0);
                }
                counter++;
            }
            in.close();
            in = null;
        } catch (Exception e) {
            logger.log(Level.WARNING, msg, e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                }
            }
        }
        this.printFile(file, buffer.toString());
        return buffer.toString();
    }

    /**
     * Finds the first match of a search in a file.
     * @param file The file in wich to search
     * @param regExpession The search expression
     * @return The first match or null if nothing was found
     */
    public String find(String file, String regExpession) throws Exception {
        String foundString = null;
        String fileContent = this.getFileContent(new File(file));
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regExpession);
        java.util.regex.Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            foundString = matcher.group();
        } else {
        }
        return foundString;
    }

    /**
     * Finds the first match of a search in a file.
     * If it can not find a match it tries again and again
     * (500 milliseconds intervall) but not longer than
     * the maximum wait time. It the internal find() encounterns an
     * IOException it ignores it (does not end the find.)
     *
     * @param file The file in wich to search
     * @param regExpression The search expression
     * @param maximumWaitTime in milliseconds
     * @return The first match
     */
    public synchronized String find(String file, String regExpression, long maximumWaitTime) throws Exception {
        long startTime = System.currentTimeMillis();
        String foundString = null;
        while (foundString == null) {
            try {
                foundString = this.find(file, regExpression);
            } catch (IOException e) {
                logger.fine("Ignoring the exception '" + e.getMessage() + "' - while finding the regular expression '" + regExpression + "' in file '" + file + "'.");
            }
            if (foundString != null) {
                break;
            }
            long currentTime = System.currentTimeMillis();
            long lapTime = currentTime - startTime;
            if (lapTime > maximumWaitTime) {
                break;
            }
            try {
                this.wait(500);
            } catch (InterruptedException ie) {
                logger.log(Level.WARNING, ie.getMessage(), ie);
            }
        }
        return foundString;
    }

    /**
     * Checks wether a file has disappeared. If the file exists
     * it waits until the file has disappeared but no longer than the
     * maximum wait time. (The internal wait intervall is 500 milliseconds.)
     * @param file The file to check
     * @param maximumWaitTime in milliseconds
     * @return true if the file does not exist, false otherwise
     */
    public synchronized boolean hasFileDisappeared(String file, long maximumWaitTime) {
        long startTime = System.currentTimeMillis();
        boolean hasDisappeard = false;
        File f = new File(file);
        while (true) {
            boolean exists = f.exists();
            if (!exists) {
                hasDisappeard = true;
                break;
            }
            long currentTime = System.currentTimeMillis();
            long lapTime = currentTime - startTime;
            if (lapTime > maximumWaitTime) {
                break;
            }
            try {
                this.wait(500);
            } catch (InterruptedException ie) {
                logger.log(Level.WARNING, ie.getMessage(), ie);
            }
        }
        return hasDisappeard;
    }

    /**
     * Checks wether a file has appeared. If the file does not exist
     * it waits until the file exists but no longer than the
     * maximum wait time. (The internal wait intervall is 500 milliseconds.)
     *
     * @param file The file to check
     * @param maximumWaitTime in milliseconds.
     * If maximumWaitTime <= null the method does not wait and reacts like
     * an ordinary File.exists().
     * @return true if the file does exist, false otherwise
     */
    public synchronized boolean exists(String file, long maximumWaitTime) {
        if (file == null) {
            return false;
        }
        long startTime = System.currentTimeMillis();
        boolean hasAppeard = false;
        File f = new File(file);
        while (true) {
            boolean exists = f.exists();
            if (exists) {
                hasAppeard = true;
                break;
            }
            if (maximumWaitTime <= 0) {
                break;
            }
            long currentTime = System.currentTimeMillis();
            long lapTime = currentTime - startTime;
            if (lapTime > maximumWaitTime) {
                break;
            }
            try {
                this.wait(500);
            } catch (InterruptedException ie) {
                logger.log(Level.WARNING, ie.getMessage(), ie);
            }
        }
        return hasAppeard;
    }

    /**
     * !!! <font color='red'>Works for java version 1.4 and higher only. !!!</font>
     * List the files in a directory. The file names have to match a
     * regular expression.
     *
     * @param dir The directory where to look for the file.
     * @param fileNameAsRegularExpression The file name as regular expression.
     * 		  See java.util.regex.Pattern.
     * @return The list of Strings representing the absolute paths of the
     * 		   files found in the directory. Example file: "c:\tmp\myfile.txt".
     */
    public List list(String dir, String fileNameAsRegularExpression) {
        List fileList = new ArrayList();
        if (dir == null) {
            return fileList;
        }
        if (fileNameAsRegularExpression == null) {
            return fileList;
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(fileNameAsRegularExpression);
        File directory = new File(dir);
        File[] files = directory.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                continue;
            }
            String fileName = file.getName();
            java.util.regex.Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                fileList.add(file.getAbsolutePath());
            } else {
            }
        }
        return fileList;
    }

    /**
     * !!! <font color='red'>Works for java version 1.4 and higher only. !!!</font>
     * Checks wether a file has appeared. If the file does not exist
     * in the directory
     * it waits until the file exists but no longer than the
     * maximum wait time. (The internal wait intervall is 500 milliseconds.)
     *
     * @param dir The directory where to look for the file.
     * @param fileNameAsRegularExpression The file name as regular expression.
     * 		  See java.util.regex.Pattern.
     * @param maximumWaitTime in milliseconds.
     * 		  If maximumWaitTime <= null the method does not wait and reacts like
     * 		  an ordinary File.exists().
     * @return true if the file does exist, false otherwise
     */
    public synchronized boolean exists(String dir, String fileNameAsRegularExpression, long maximumWaitTime) {
        if (dir == null) {
            return false;
        }
        if (fileNameAsRegularExpression == null) {
            return false;
        }
        long startTime = System.currentTimeMillis();
        boolean hasAppeard = false;
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(fileNameAsRegularExpression);
        while (true) {
            File directory = new File(dir);
            File[] files = directory.listFiles();
            int length = files.length;
            for (int i = 0; i < length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    continue;
                }
                String fileName = file.getName();
                java.util.regex.Matcher matcher = pattern.matcher(fileName);
                if (matcher.find()) {
                    return true;
                } else {
                }
            }
            if (maximumWaitTime <= 0) {
                break;
            }
            long currentTime = System.currentTimeMillis();
            long lapTime = currentTime - startTime;
            if (lapTime > maximumWaitTime) {
                break;
            }
            try {
                this.wait(500);
            } catch (InterruptedException ie) {
                logger.log(Level.WARNING, ie.getMessage(), ie);
            }
        }
        return hasAppeard;
    }

    /**
     * <font color='red'>TODO: It does work for the 1st match only</font>
     *
     * <br><br>
     * This method replaces text in a file that contains
     * both text and binary content. It replaces all
     * found matches of the search expression.
     * <h3>
     * The task
     * </h3>
     * Imagine a file containing data sets each containing a header as String
     * and binary data. The header data has to be changed but
     * the binary part has to be left untouched.
     * <h3>
     * The solution
     * </h3>
     * <ul>
     * 	<li>
     * 		Open the file and read the content into a Byte Array.
     * 	</li>
     * 	<li>
     *      Transform the Byte array into a temporary String. The
     *      String is searched through for all occurances of the
     *      search expression. The start and end index of each
     *      occurance (match) is stored.
     * 	</li>
     * 	<li>
     * 		Loop through all parts of the file in which the search expression
     *      has a match. Store the part of the file in a byte array. Convert it to a String.
     *      (Start with the last found match from the end of the file
     *      and work from match to match to the beginning of the file.)
     * 	</li>
     * 	<li>
     * 		Do the replacement in the String (part of file). Convert it back to a
     *      byte array and replace the old part of the file with the
     *      new byte array. Do so for all matches (in the loop).
     * 	</li>
     * </ul>
     *
     * @param file The absolut path to the file
     * @param searchExpression A regular expression. See java.util.regex.Pattern
     * @param replacement The String that is inserted. It can refer to groups
     *                    of the search expression. Example: The search expression
     *                    is "(cat)(.+)(dog)". The replacement is "brave cat$2$3".
     *                    The text is "The cat bites the dog.". The result would
     *                    be "The brave cat bites the dog.". The first group of
     *                    the search expression "(cat)" finds "cat". The match
     *                    can be refered to in the replacement with "$1".
     *                    The second group of
     *                    the search expression "(.+)" finds " bites the ". The match
     *                    can be refered to in the replacement with "$2".
     *                    The third group of
     *                    the search expression "(dog)" finds "dog". The match
     *                    can be refered to in the replacement with "$3".
     * @throws Exception
     */
    public void replaceAllInMixedTextBinaryFile(String file, String searchExpression, String replacement) throws Exception {
        if (file == null) {
            throw new Exception("Failed to replace. File not set.");
        }
        if (searchExpression == null) {
            searchExpression = ".*";
        }
        if (replacement == null) {
            replacement = "";
        }
        String fileContent = this.getFileContent(new File(file));
        List indicesAndTextToInsert = new ArrayList();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(searchExpression);
        java.util.regex.Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()) {
            String occurance = matcher.group();
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            java.util.regex.Matcher m = pattern.matcher(occurance);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            String textToInsert = sb.toString();
            List indicesAndText = new ArrayList();
            indicesAndText.add(new Integer(startIndex));
            indicesAndText.add(new Integer(endIndex));
            indicesAndText.add(textToInsert);
            indicesAndTextToInsert.add(indicesAndText);
        }
        int size = indicesAndTextToInsert.size();
        for (int i = (size - 1); i >= 0; i--) {
            List indicesAndText = (List) indicesAndTextToInsert.get(i);
            int startIndex = ((Integer) indicesAndText.get(0)).intValue();
            int endIndex = ((Integer) indicesAndText.get(1)).intValue();
            String textToInsert = (String) indicesAndText.get(2);
            this.insertStringInFile(file, textToInsert, (long) startIndex, (long) endIndex);
        }
    }

    /**
     * This method replaces text in a file that contains
     * both text and binary content.
     * <h3>
     * The task
     * </h3>
     * Imagine a file containing header data as String
     * and binary data. The header data has to be changed but
     * the binary part has to be left untouched.
     * <h3>
     * The solution
     * </h3>
     * <ul>
     * 	<li>
     * 		Open the file and read the content into a Byte Array.
     * 	</li>
     * 	<li>
     * 		Create a new Byte Array from Byte number
     * 		'a' to Byte number 'b' (in the original Byte Array).
     * 		Convert this new Byte Array to a String.
     * 	</li>
     * 	<li>
     * 		Do the replacement in the String.
     * 	</li>
     * 	<li>
     * 		Convert the String back to a Byte Array.
     * 	</li>
     * 	<li>
     * 		Insert the changed Byte Array into the
     * 		original Byte Array.
     * 	</li>
     * 	<li>
     * 		Overwrite the file with the new Byte Array.
     * 	</li>
     * </ul>
     *
     * @param file The absolut path to the file
     * @param searchExpression A regular expression. See java.util.regex.Pattern
     * @param replacement The String that is inserted
     * @param occuranceNumber What found occurances of the search are replaced.
     * 		  '0' for all found occurances, '1' the first only,
     * 		  '2' for the second only, and so on.
     * @param fromByte The Byte where to start the replacement in the file.
     * 		  '0' is the first Byte of the file.
     * @param toByte The Byte where to end the replacement in the file
     * @return The replaced text (that is inserted into to file)
     * @throws Exception
     */
    public String replaceInMixedTextBinaryFile(String file, String searchExpression, String replacement, int occuranceNumber, int fromByte, int toByte) throws Exception {
        if (file == null) {
            throw new Exception("Failed to replace. File not set.");
        }
        if (searchExpression == null) {
            searchExpression = ".*";
        }
        if (replacement == null) {
            replacement = "";
        }
        if (fromByte < 0) {
            throw new Exception("Value 'from Byte' lower than '0'.");
        }
        if (toByte < 0) {
            throw new Exception("Value 'to Byte' lower than '0'.");
        }
        if (toByte <= fromByte) {
            String msg = "Value 'to Byte' (" + toByte + ") less or equal than 'from Byte' (" + fromByte + ").";
            throw new Exception(msg);
        }
        String textPart = null;
        try {
            textPart = this.readFile(file, fromByte, toByte);
        } catch (Exception e) {
            String msg = "Failed to read the file '" + file + "' from byte '" + fromByte + "' to byte '" + toByte + "'.";
            logger.log(Level.WARNING, msg, e);
            throw new Exception(msg);
        }
        try {
            textPart = this.replace(textPart, searchExpression, replacement, occuranceNumber);
        } catch (Exception e) {
            String msg = "Failed to replace in text '" + textPart + "', search expression '" + searchExpression + "', replacement '" + replacement + "'" + ", occurance number '" + occuranceNumber + "'.";
            logger.log(Level.WARNING, msg, e);
            throw new Exception(msg);
        }
        try {
            this.insertStringInFile(file, textPart, fromByte, toByte);
        } catch (Exception e) {
            String msg = "Failed to insert text '" + textPart + "', in file '" + file + "', from Byte '" + fromByte + "'" + ", to byte '" + toByte + "'.";
            logger.log(Level.WARNING, msg, e);
            throw new Exception(msg);
        }
        return textPart;
    }

    /**
     * Inserts a text in a file. The file can contain both
     * text or binary data. The file is read as a byte array
     * from the beginning to the fromByte 'a' and then from the
     * toByte 'b' to the end. Between the to resulting parts the
     * text is inserted as byte array. The resulting byte array
     * is writen to file.
     *
     * The new byte array containing the three parts is written to
     * a tmp file. The orginal file is deleted. The tmp file is renamed
     * to the original file.
     *
     * @param file Absolute path to the file
     * @param textToInsert The text to insert
     * @param fromByte The Byte where to start the replacement in the file.
     * 		  '0' is the first Byte of the file.
     * @param toByte The Byte where to end the replacement in the file
     * @throws Exception
     */
    public void insertStringInFile(String file, String textToInsert, long fromByte, long toByte) throws Exception {
        String tmpFile = file + ".tmp";
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        long byteCount = 0;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(file)));
            out = new BufferedOutputStream(new FileOutputStream(tmpFile));
            long size = fromByte;
            byte[] buf = null;
            if (size == 0) {
            } else {
                buf = new byte[(int) size];
                int length = -1;
                if ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                    byteCount = byteCount + length;
                } else {
                    String msg = "Failed to read the first '" + size + "' bytes of file '" + file + "'. This might be a programming error.";
                    logger.warning(msg);
                    throw new Exception(msg);
                }
            }
            buf = textToInsert.getBytes();
            int length = buf.length;
            out.write(buf, 0, length);
            byteCount = byteCount + length;
            long skipLength = toByte - fromByte;
            long skippedBytes = in.skip(skipLength);
            if (skippedBytes == -1) {
            } else {
                buf = new byte[4096];
                length = -1;
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                    byteCount = byteCount + length;
                }
            }
            in.close();
            in = null;
            out.close();
            out = null;
            File fileToDelete = new File(file);
            boolean wasDeleted = fileToDelete.delete();
            if (!wasDeleted) {
                String msg = "Failed to delete the original file '" + file + "' to replace it with the modified file after text insertion.";
                logger.warning(msg);
                throw new Exception(msg);
            }
            File fileToRename = new File(tmpFile);
            boolean wasRenamed = fileToRename.renameTo(fileToDelete);
            if (!wasRenamed) {
                String msg = "Failed to rename tmp file '" + tmpFile + "' to the name of the original file '" + file + "'";
                logger.warning(msg);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to read/write file '" + file + "'.", e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(Level.FINEST, "Ignoring error closing input file '" + file + "'.", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.log(Level.FINEST, "Ignoring error closing output file '" + tmpFile + "'.", e);
                }
            }
        }
    }

    /**
     * Reads a part of a file.
     *
     * @param file The absolute path to the file.
     * @param fromByte Starting point
     * @param toByte End point
     * @return The part of the file
     * @throws Exception
     */
    public String readFile(String file, int fromByte, int toByte) throws Exception {
        String returnString = null;
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(file)));
            long skippedBytes = in.skip(fromByte);
            if (skippedBytes == -1) {
                String msg = "File size is less than starting point (from byte) '" + fromByte + "' ...";
                throw new Exception(msg);
            }
            int length = toByte - fromByte;
            byte[] buf = new byte[length];
            int readNumberOfBytes = in.read(buf);
            byte[] fullByteArray = new byte[readNumberOfBytes];
            for (int i = 0; i < readNumberOfBytes; i++) {
                fullByteArray[i] = buf[i];
            }
            returnString = new String(fullByteArray);
            in.close();
            in = null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to read file '" + file + "'.", e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(Level.FINE, "Failed to read file '" + file + "'.", e);
                }
            }
        }
        return returnString;
    }

    /**
     * <font color='red'>!!! Works for java version 1.4 and higher only. !!!</font>
     * <br>
     * TODO: Move to a StringUtil class. It is "private".
     *
     * <h3>Example 1</h3>
     * The text
     * <pre>one cat two cats in the yard</pre>
     * The search expression
     * <pre>cat</pre>
     * The replacement
     * <pre>dog</pre>
     * The occurance number (replace all)
     * <pre>0</pre>
     * The result
     * <pre>one dog two dogs in the yard</pre>
     * <h3>Example 2 - using Groups</h3>
     * The text
     * <pre>one green cat two blue cats in the yard</pre>
     * The search expression
     * <pre>(\w+?\s)(cat)</pre>
     * The replacement
     * <pre>$1dog</pre>
     * The occurance number (replace all)
     * <pre>0</pre>
     * The result
     * <pre>one green dog two blue dogs in the yard</pre>
     *
     * @param text The text in which to replace
     * @param searchExpression The search expression
     * @param replacement The String to insert
     * @param occuranceNumber What occurances (of the search expression)
     * 		  have to be replaced. '0' for all occurances.
     * @return The text after the replacements
     * @throws Exception
     */
    private String replace(String text, String searchExpression, String replacement, int occuranceNumber) throws Exception {
        if (text == null) {
            throw new Exception("Failed to replace. Text not set.");
        }
        if (searchExpression == null) {
            searchExpression = ".*";
        }
        if (replacement == null) {
            replacement = "";
        }
        Pattern p = Pattern.compile(searchExpression);
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        int occurance = 1;
        while (m.find()) {
            if (occuranceNumber == occurance) {
                m.appendReplacement(sb, replacement);
                break;
            } else if (occuranceNumber == 0) {
                m.appendReplacement(sb, replacement);
            } else {
            }
            occurance++;
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * Checks wethe the content of file B is contained in file A.
     * It works for both text and binary files. The underlying
     * implementation uses byte arrays.
     *
     * @param fileA Absolute path to file
     * @param fileB Absolute path to file
     * @return true if file B is contained in file A, false otherwise
     */
    public boolean containsFile(String fileA, String fileB) throws Exception {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new DataInputStream(new FileInputStream(fileA)));
            byte[] buf = new byte[4096];
            java.util.List listA = new ArrayList();
            int length = -1;
            while ((length = in.read(buf)) != -1) {
                for (int i = 0; i < length; i++) {
                    listA.add(new Byte(buf[i]));
                }
            }
            in.close();
            in = null;
            in = new BufferedInputStream(new DataInputStream(new FileInputStream(fileB)));
            buf = new byte[4096];
            java.util.List listB = new ArrayList();
            while ((length = in.read(buf)) != -1) {
                for (int i = 0; i < length; i++) {
                    listB.add(new Byte(buf[i]));
                }
            }
            in.close();
            in = null;
            int sizeA = listA.size();
            int sizeB = listB.size();
            for (int i = 0; i < sizeA; i++) {
                List subListA = null;
                try {
                    subListA = listA.subList(i, i + sizeB);
                } catch (IndexOutOfBoundsException e) {
                    logger.log(Level.FINER, "End of file A reached. File A '" + fileA + "' starting with index '" + i + "' ending with index '" + (i + sizeB) + "' (length '" + sizeB + " - 1').", e);
                    return false;
                }
                boolean matches = true;
                for (int j = 0; j < sizeB; j++) {
                    Byte byteB = (Byte) listB.get(j);
                    Byte byteA = (Byte) subListA.get(j);
                    byte bB = byteB.byteValue();
                    byte bA = byteA.byteValue();
                    if (bB != bA) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to find content of file '" + fileB + "' in file '" + fileA + "'." + e.getMessage(), e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public long removeLinesContaining(String file, String regularExpression) throws Exception {
        logger.fine("Compiling pattern: " + regularExpression);
        Pattern p = Pattern.compile(regularExpression);
        StringBuffer newFileContent = new StringBuffer();
        long removedLines = 0;
        BufferedReader in = null;
        try {
            logger.fine("Open file...");
            in = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = in.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    removedLines++;
                } else {
                    if (newFileContent.length() > 0) {
                        newFileContent.append("\n");
                    }
                    newFileContent.append(line);
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ioe) {
                }
            }
        }
        logger.fine("Removed '" + removedLines + "' lines.");
        logger.fine("Overwrite file with new file content... File: '" + file + "'");
        this.printFile(file, newFileContent.toString());
        return removedLines;
    }

    /**
     * Get the IP address from an UNC path (Windows).
     *
     * @param UNCPathName Example: "\\172.22.1.5\C$\mydirectories\andOrFiles"
     * @return The IP or null if no IP was found. Example: "172.22.1.5"
     */
    public String getIPfromUNCPath(String UNCPathName) {
        String regExpession = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        String ip = null;
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regExpession);
        java.util.regex.Matcher matcher = pattern.matcher(UNCPathName);
        if (matcher.find()) {
            ip = matcher.group();
        } else {
        }
        return ip;
    }

    /**
     * Copies a file to the user home.
     * @param fileInClasspath to file as classpath.
     * @return The absolut path to the file in the filesystem
     * (in user home).
     */
    public String copyFileToUserHome(String fileInClasspath) throws Exception {
        String[] stArray = fileInClasspath.split("/");
        int length = stArray.length;
        String fileName = stArray[length - 1];
        logger.finer("Extracted the file name '" + fileName + "' from file in classpath '" + fileInClasspath + "'.");
        String userHome = System.getProperty("user.home");
        String userHomeFile = userHome + File.separator + fileName;
        FileUtil util = new FileUtil();
        logger.finer("Copy file from classpath '" + fileName + "' to user home '" + userHomeFile + "'.");
        util.copyRessourceFileToFileSystem(fileInClasspath, userHomeFile);
        logger.finer("Returning absolut path file in user home '" + userHomeFile + "'...");
        return userHomeFile;
    }

    /**
     * Replaces the Java System Properties in a String.
     * Replaces more the one Java Properties and all occurances of them.
     * Example:  "${user.home}${file.separator}tmp${file.separator}${java.version}"
     *
     * @param file The absolute path to a file where to replace the placeholders
     */
    public void replaceJavaProperties(String file) throws Exception {
        String fileContent = this.getFileAsString(new File(file));
        PropertiesReader reader = new PropertiesReader();
        fileContent = reader.replaceSystemProperties(fileContent);
        this.printFile(file, fileContent);
    }

    /**
     * Tries to find a rolled / renamed version of the file. <br>
     * <br>
     * Example: abc.0.dbg would find
     * <ul>
     * <li>abc.1.dbg,</li>
     * <li>abc.2.dbg,</li>
     * <li>...</li>
     * </ul>
     * Returns the file that has the newest last modified timestamp.
     *
     *
     * @return the rolled / renamed version of the file. Example: abc.1.dbg
     * @param file
     *          the file that might be rolled / renamed. Example: abc.0.dbg or
     *          null if no file could be found.
     * @throws java.lang.Exception
     *           if something is wrong with the path.
     */
    public String getLastRolledFile(String file) throws Exception {
        String patternString = "(.*)([^/\\\\]+)(([/\\\\]+))(.+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(file);
        String fileName = null;
        if (matcher.find()) {
            fileName = matcher.group(5);
            logger.finer("The name of the file is '" + fileName + "'.");
            if (fileName.equals("")) {
                throw new Exception("Empty filename. Something wrong with path of file '" + file + "' using the pattern '" + patternString + "'.");
            }
        } else {
            throw new Exception("Something wrong with path of file '" + file + "' using the pattern '" + patternString + "'.");
        }
        File tmpFile = new File(file);
        File directory = tmpFile.getParentFile();
        List foundUncheckedFiles = new ArrayList();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            int length = files.length;
            logger.finer("Try to get the old rolled verstions of the file named '" + fileName + "' from directory '" + directory + "' with '" + length + "' files (including directories) ...");
            for (int i = 0; i < length; i++) {
                File fileInDirectory = files[i];
                if (!fileInDirectory.isFile()) {
                    continue;
                }
                String listedFileName = fileInDirectory.getName();
                if (fileName.equalsIgnoreCase(listedFileName)) {
                    continue;
                }
                foundUncheckedFiles.add(listedFileName);
            }
            if (foundUncheckedFiles.isEmpty()) {
                logger.fine("No files found in directory '" + directory + "'.");
                return null;
            }
        } else {
            logger.fine("The parent '" + directory + "' does not exits or is not a directory");
            return null;
        }
        int size = foundUncheckedFiles.size();
        logger.finer("Found '" + size + "' files in the directory '" + directory + "'.");
        List namesOfRolledFiles = this.getRolledVersionsOfTheFile(fileName, foundUncheckedFiles);
        String lastModifiedRolledFile = null;
        String separator = File.separator;
        List rolledFiles = new ArrayList();
        Iterator it = namesOfRolledFiles.iterator();
        while (it.hasNext()) {
            String rolledFileName = (String) it.next();
            String rolledFile = directory + separator + rolledFileName;
            rolledFiles.add(rolledFile);
        }
        lastModifiedRolledFile = this.getLastModifiedFile(rolledFiles);
        logger.fine("Found the last rolled version '" + lastModifiedRolledFile + "' of the file named '" + fileName + "' in the directory '" + directory + "'.");
        return lastModifiedRolledFile;
    }

    /**
     * Get the last modified file out of a list of files.
     *
     * @param files
     *          List of Strings representing absolute paths to files
     * @return the file that was last modified.
     */
    public String getLastModifiedFile(List files) {
        String lastModifiedFile = null;
        long lastModifiedTime = 0;
        if (files == null) {
            logger.fine("No files in the list.");
            return lastModifiedFile;
        }
        Iterator it = files.iterator();
        while (it.hasNext()) {
            String f = (String) it.next();
            logger.finer("Examining file '" + f + "' ...");
            File file = new File(f);
            if (!file.exists()) {
                logger.finer("File '" + f + "' does not exitst. Continue with next file...");
                continue;
            }
            long lastModified = file.lastModified();
            if (lastModified > lastModifiedTime) {
                logger.finer("Yes, the file '" + f + "' has a newer timstamp '" + lastModified + "' than '" + lastModifiedTime + "'.");
                lastModifiedTime = lastModified;
                lastModifiedFile = f;
            } else {
                logger.finer("No, the file '" + f + "' has an older timstamp '" + lastModified + "' than '" + lastModifiedTime + "'.");
            }
        }
        logger.fine("The last modified time in the list of files is '" + lastModifiedFile + "' with the timestamp '" + lastModifiedTime + "'.");
        return lastModifiedFile;
    }

    /**
     * Try to get all file names that could be older (rolled) versions of the file
     * name. <br>
     * <br>
     * Example: Current file is abc.0.dbg. The the method would return all files
     * like abc.1.dbg, abc.2.dbg,.... <br>
     * <br>
     * Example: Current file is abc.21.0dbg. The the method would return all files
     * like abc.21.1.dbg, abc.21.2.dbg,....
     *
     *
     * @return a List of String holding the filenames of rolled files
     * @param fileName
     *          The filename of the current file that should have older rolled
     *          versions.
     * @param fileNames
     *          List of String representing file names that could be older
     *          versions (rolled files)
     */
    public List getRolledVersionsOfTheFile(String fileName, List fileNames) {
        List rolledFiles = new ArrayList();
        String patternString = "(([^\\d]+\\d+)*)((([^\\d]+)(\\d+))+)(.*)";
        logger.finer("Start to look for rolled versions of the filename '" + fileName + "' using the pattern '" + patternString + "'.");
        Pattern pattern = Pattern.compile(patternString);
        String fileNameGroup1 = null;
        String fileNameGroup5 = null;
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            fileNameGroup1 = matcher.group(1);
            fileNameGroup5 = matcher.group(5);
            logger.finer("Yes, found the pattern in the filename.");
        } else {
            logger.fine("No, did not find the pattern in the filename.");
            return rolledFiles;
        }
        String nameBeforRollingNumber = fileNameGroup1 + fileNameGroup5;
        Iterator it = fileNames.iterator();
        while (it.hasNext()) {
            String listedFileName = (String) it.next();
            String listedFileNameGroup1 = null;
            String listedFileNameGroup5 = null;
            matcher = pattern.matcher(listedFileName);
            if (matcher.find()) {
                String listedMatchingFileName = matcher.group(0);
                logger.finer("Found rolled version '" + listedMatchingFileName + "' for file name '" + fileName + "'.");
                if (fileName.equals(listedMatchingFileName)) {
                    logger.finer("Same file. Continue to next rolled version.");
                    continue;
                }
                listedFileNameGroup1 = matcher.group(1);
                listedFileNameGroup5 = matcher.group(5);
                String listedNameBeforRollingNumber = listedFileNameGroup1 + listedFileNameGroup5;
                if (nameBeforRollingNumber.equals(listedNameBeforRollingNumber)) {
                    logger.finer("Yes, the first part '" + listedFileNameGroup1 + "' is equal in both filenames. The rolled version: '" + listedMatchingFileName + "', original file name '" + fileName + "'.");
                    if (!rolledFiles.contains(listedMatchingFileName)) {
                        logger.finer("Adding the filename '" + listedMatchingFileName + "' to the list.");
                        rolledFiles.add(listedMatchingFileName);
                    }
                }
            } else {
                logger.finer("No rolled version found for file name '" + listedFileName + "'.");
            }
        }
        int size = rolledFiles.size();
        logger.fine("Returning a list of '" + size + "' files.");
        return rolledFiles;
    }
}
