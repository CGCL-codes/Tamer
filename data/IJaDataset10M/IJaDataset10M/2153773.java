package com.lars_albrecht.foldergen.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Some helper functions.
 * 
 * @author lalbrecht
 * @version 1.0.0.0
 */
public class Utilities {

    /**
	 * Returns the number of appearces of "character" in "string".
	 * 
	 * @param string
	 *            String
	 * @param findStr
	 *            String
	 * 
	 * @return Integer
	 */
    public static Integer countChars(final String string, final String findStr) {
        String strCopy = new String(string);
        return strCopy.replaceAll("[" + findStr + "]", "").length();
    }

    /**
	 * Returns true is the given String is a boolean.
	 * 
	 * @param strBool
	 * @return Boolean
	 */
    public static Boolean isBoolean(final String strBool) {
        return strBool.equalsIgnoreCase("false") || strBool.equalsIgnoreCase("true");
    }

    /**
	 * Returns the content of a file as string.
	 * 
	 * @param srcFile
	 *            File
	 * @return String
	 * @throws IOException
	 */
    public static String getFileContent(final File srcFile) throws IOException {
        if ((srcFile != null) && srcFile.exists() && srcFile.isFile()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
            StringBuffer contentOfFile = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                contentOfFile.append(line);
            }
            return contentOfFile.toString();
        }
        return null;
    }

    /**
	 * 
	 * @param srcUrl
	 *            URL
	 * @param destination
	 *            FileOutputStream
	 * @return String
	 * @throws IOException
	 */
    public static Boolean getFileContentFromWeb(final URL srcUrl, final FileOutputStream destination) throws IOException {
        if ((srcUrl != null)) {
            HttpURLConnection conn = (HttpURLConnection) srcUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                byte tmp_buffer[] = new byte[4096];
                InputStream is = conn.getInputStream();
                int n;
                while ((n = is.read(tmp_buffer)) > 0) {
                    destination.write(tmp_buffer, 0, n);
                    destination.flush();
                }
            } else {
                throw new IllegalStateException("HTTP response: " + responseCode);
            }
            destination.close();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
	 * Copy a file from source to target.
	 * 
	 * @param source
	 *            File
	 * @param target
	 *            File
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static void copyFile(final File source, final File target) throws FileNotFoundException, IOException {
        FileChannel in = new FileInputStream(source).getChannel(), out = new FileOutputStream(target).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (in.read(buffer) != -1) {
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
        out.close();
        in.close();
    }

    /**
	 * Copy a directory from source to target.
	 * 
	 * @param source
	 *            File
	 * @param target
	 *            File
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static void copyDir(final File source, final File target) throws FileNotFoundException, IOException {
        File[] files = source.listFiles();
        target.mkdirs();
        for (File file : files) {
            if (file.isDirectory()) {
                Utilities.copyDir(file, new File(target.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
            } else {
                Utilities.copyFile(file, new File(target.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
            }
        }
    }

    /**
	 * Deletes files and folders.
	 * 
	 * @param path
	 *            File
	 * @return Boolean
	 */
    public static Boolean delete(final File path) {
        if (path.isDirectory()) {
            String[] children = path.list();
            for (String element : children) {
                boolean success = Utilities.delete(new File(path, element));
                if (!success) {
                    return false;
                }
            }
        }
        return path.delete();
    }
}
