package org.fredy.id3tidy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fredy
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * Gets the file listing.
     * @param file the file
     * @param extension the file extension
     * @return the list of files
     */
    public static List<File> getFileListing(File file, String... extension) {
        List<File> list = new ArrayList<File>();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                list.addAll(getFileListing(f, extension));
            }
        } else {
            List<File> l = new ArrayList<File>();
            for (String ext : extension) {
                if (file.getName().toLowerCase().endsWith(ext)) {
                    l.addAll(Arrays.asList(new File[] { file }));
                }
            }
            return l;
        }
        return list;
    }

    /**
     * Copy the file from the source to destination.
     * @param source the source file
     * @param destination the destination file
     * @throws IOException
     */
    public static void copy(File source, File destination) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);
        byte[] bytes = new byte[4096];
        int bytesRead;
        try {
            while ((bytesRead = fis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, bytesRead);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * Reads the whole lines from a file.
     * @param file the file
     * @return the lines
     * @throws IOException
     */
    public static String readLines(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();
    }

    /**
     * Reads the file line by line.
     * @param file the file
     * @param fileFunction the FileFunction instance
     * @throws IOException the IO exception
     */
    public static void eachLine(File file, FileFunction fileFunction) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                fileFunction.read(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
}
