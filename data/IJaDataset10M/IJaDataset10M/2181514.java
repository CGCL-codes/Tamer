package ch.ethz.inf.vs.wot.autowot.builders.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import ch.ethz.inf.vs.wot.autowot.commons.Constants;

/**
 * Utility class providing useful file system operations
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */
public class FileOperations {

    /**
	 * Create directory
	 */
    public static void makeDirectory(String dirName) {
        File outFile = new File(dirName);
        System.out.println("Creating " + outFile.getAbsolutePath());
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
    }

    /**
	 * Delete directory
	 */
    public static boolean deleteDir(String dirName) {
        System.out.println("Deleting " + dirName);
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(dir + System.getProperty("file.separator") + children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
	 * Copy directory
	 */
    public static void copyDirectory(File sourceLocation, File targetLocation, boolean appendToFiles) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (sourceLocation.getAbsolutePath().endsWith(".svn")) return;
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]), appendToFiles);
            }
        } else {
            if (appendToFiles) {
                System.out.println("Appending: " + targetLocation.getPath());
                File outFile = null;
                WriterUtils myWriter = new WriterUtils();
                try {
                    outFile = new File(targetLocation.getPath());
                    boolean success = outFile.createNewFile();
                    if (success) {
                        System.out.println(targetLocation.getPath() + " created!");
                    } else {
                    }
                    myWriter.writer = new BufferedWriter(new FileWriter(outFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myWriter.writeToFile("package " + Constants.RESOURCE_PACKAGE + ";\n");
                myWriter.end();
            }
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation, appendToFiles);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
	 * Create new file
	 */
    public static BufferedWriter createFile(String path) {
        File outFile = null;
        try {
            outFile = new File(path);
            boolean success = outFile.createNewFile();
            if (success) {
                System.out.println(path + " created!");
            } else {
            }
            return new BufferedWriter(new FileWriter(outFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Copy file
	 */
    public static void copyFile(File in, File out, boolean append) throws IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out, append).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }

    /**
	 * Read file as String
	 */
    public static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    public static String getCurrentSystemPath() {
        try {
            String path = (new java.io.File(".")).getCanonicalPath();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
