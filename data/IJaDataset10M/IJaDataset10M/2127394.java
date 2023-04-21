package org.proteusframework.platformservice.persistence.util;

import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * @author Tacoma Four
 */
public final class FileUtil {

    private static final Logger logger = Logger.getLogger(FileUtil.class.getCanonicalName());

    private FileUtil() {
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(in);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        while (true) {
            int datum = bin.read();
            if (datum == -1) {
                break;
            }
            bout.write(datum);
        }
        bout.flush();
    }

    public static String readResource(String resourcePath, Class classpath) {
        int c;
        InputStream is = classpath.getResourceAsStream(resourcePath);
        StringBuffer buf = new StringBuffer();
        if (null != is) {
            try {
                while ((c = is.read()) != -1) {
                    buf.append((char) c);
                }
            } catch (IOException e) {
                logger.severe("Unable to load resource path (" + resourcePath + "): " + e.getMessage());
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        logger.warning("Unable to properly close the resource path (" + resourcePath + "): " + e.getMessage());
                    }
                }
            }
        } else {
            logger.severe("Unable to locate resource at path " + resourcePath);
        }
        return buf.toString();
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            String[] children = directory.list();
            for (String child : children) {
                boolean success = deleteDirectory(new File(directory, child));
                if (!success) {
                    return false;
                }
            }
        }
        return directory.delete();
    }

    public static void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
            }
        } else {
            copyFile(srcDir, dstDir);
        }
    }

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static File mkDirs(File rootDir, String packageName) {
        StringTokenizer tokenizer = new StringTokenizer(packageName, ".", false);
        File parentDir = new File(rootDir.getAbsolutePath());
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            File curDir = new File(parentDir, token);
            curDir.mkdirs();
            parentDir = new File(curDir.getAbsolutePath());
        }
        return parentDir;
    }
}
