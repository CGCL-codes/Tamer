package br.com.manish.ahy.kernel.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import br.com.manish.ahy.kernel.exception.OopsException;

public final class FileUtil {

    private static Log log = LogFactory.getLog(FileUtil.class);

    private FileUtil() {
        super();
    }

    public static String readFileAsString(String path) {
        String ret = null;
        try {
            byte[] buffer = new byte[(int) new File(path).length()];
            BufferedInputStream f = new BufferedInputStream(new FileInputStream(path));
            f.read(buffer);
            ret = new String(buffer);
        } catch (IOException e) {
            throw new OopsException(e, "Error reading: " + path);
        }
        return ret;
    }

    public static byte[] readFileAsBytes(String path) {
        byte[] fileArray = null;
        try {
            File file = new File(path);
            if (file.length() > Integer.MAX_VALUE) {
                throw new OopsException("Oversized file :-( can't read it, sorry: " + path);
            }
            fileArray = new byte[(int) file.length()];
            DataInputStream dis;
            dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileArray);
            dis.close();
        } catch (Exception e) {
            throw new OopsException(e, "Problems when reading: [" + path + "].");
        }
        return fileArray;
    }

    public static byte[] readResourceAsBytes(String path) {
        byte[] byteData = null;
        try {
            InputStream is = FileUtil.class.getResourceAsStream(path);
            if (is.available() > Integer.MAX_VALUE) {
                throw new OopsException("Oversized file :-( can't read it, sorry: " + path);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            byte[] bytes = new byte[512];
            int readBytes;
            while ((readBytes = is.read(bytes)) > 0) {
                os.write(bytes, 0, readBytes);
            }
            byteData = os.toByteArray();
            is.close();
            os.close();
        } catch (Exception e) {
            throw new OopsException(e, "Problems when reading: [" + path + "].");
        }
        return byteData;
    }

    public static void copyFile(String from, String to) {
        copyFile(from, to, Boolean.FALSE);
    }

    public static void copyFile(String from, String to, Boolean overwrite) {
        try {
            File fromFile = new File(from);
            File toFile = new File(to);
            if (!fromFile.exists()) {
                throw new IOException("File not found: " + from);
            }
            if (!fromFile.isFile()) {
                throw new IOException("Can't copy directories: " + from);
            }
            if (!fromFile.canRead()) {
                throw new IOException("Can't read file: " + from);
            }
            if (toFile.isDirectory()) {
                toFile = new File(toFile, fromFile.getName());
            }
            if (toFile.exists() && !overwrite) {
                throw new IOException("File already exists.");
            } else {
                String parent = toFile.getParent();
                if (parent == null) {
                    parent = System.getProperty("user.dir");
                }
                File dir = new File(parent);
                if (!dir.exists()) {
                    throw new IOException("Destination directory does not exist: " + parent);
                }
                if (dir.isFile()) {
                    throw new IOException("Destination is not a valid directory: " + parent);
                }
                if (!dir.canWrite()) {
                    throw new IOException("Can't write on destination: " + parent);
                }
            }
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(fromFile);
                fos = new FileOutputStream(toFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } finally {
                if (from != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
                if (to != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            }
        } catch (Exception e) {
            throw new OopsException(e, "Problems when copying file.");
        }
    }

    public static void removeFile(String path) {
        try {
            File file = new File(path);
            int i = 0;
            boolean success = false;
            while (!success && i < 15) {
                success = file.delete();
                Thread.sleep(1000);
                i++;
            }
            if (!success) {
                throw new IOException("Sorry, I can't delete :" + path);
            }
        } catch (Exception e) {
            throw new OopsException(e, "Problems when removing file.");
        }
    }
}
