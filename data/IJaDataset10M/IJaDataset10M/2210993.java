package org.uasd.jalgor.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Edwin Bratini <edwin.bratini@gmail.com>
 */
public class FileManager {

    public static void makeFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     *
     * Method used to load the file into a StringBuilder object for later use.
     *
     * @param inputFile a File object from wich to read.
     * @return a StringBuilder object with the all the lines of the file argument read.
     */
    public static StringBuilder loadFile(File inputFile) {
        StringBuilder sbInputFile = new StringBuilder();
        String recLine = "";
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(inputFile);
            br = new BufferedReader(fr);
            while ((recLine = br.readLine()) != null) {
                sbInputFile.append(String.format("%s%s", recLine, System.getProperty("line.separator")));
            }
        } catch (IOException ioe) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ioe);
        } finally {
            try {
                br.close();
            } catch (IOException ioe) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ioe);
            }
        }
        return sbInputFile;
    }

    /**
     *
     * Method used to write the files into physical directory structure.
     *
     * @param content a StringBuilder object with the future content of the file
     * @param file a File object to which the content is to be intended to be written.
     */
    public static void writeToFile(StringBuilder content, File file, boolean append) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            if (!append) {
                bw.write(content.toString());
            } else {
                bw.append(content);
            }
        } catch (IOException ioe) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ioe);
        } finally {
            try {
                bw.flush();
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void copy(String sourcePath, String destPath) {
        FileReader in = null;
        FileWriter out = null;
        try {
            in = new FileReader(new File(sourcePath));
            out = new FileWriter(new File(destPath));
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void extractJarFile(JarFile jarFile, String destPath) {
        throw new NotImplementedException();
    }

    public static File extractFileFromJar(JarFile jarFile, String destPath, String entryName) {
        ZipEntry entry = jarFile.getEntry(entryName);
        File entryFile = new File(destPath, entry.getName());
        new File(entryFile.getParent()).mkdirs();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(jarFile.getInputStream(entry));
            out = new BufferedOutputStream(new FileOutputStream(entryFile));
            byte[] buffer = new byte[2048];
            for (; ; ) {
                int nBytes = in.read(buffer);
                if (nBytes <= 0) {
                    break;
                }
                out.write(buffer, 0, nBytes);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entryFile;
    }
}
