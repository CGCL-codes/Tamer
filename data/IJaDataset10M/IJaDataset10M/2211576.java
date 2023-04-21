package com.sitechasia.webx.core.utils.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * 文件和流工具类
 *
 * @author Administrator
 * @author todd
 * @version 1.2 , 2008/5/7
 * @since JDK1.5
 */
public final class FileUtils {

    private FileUtils() {
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
	 * 统一转换为标准文件格式，即以"/"作为分隔
	 *
	 * @param strPath
	 * @return
	 */
    public static String convertPath(String strPath) {
        return strPath.replaceAll("\\\\", "/");
    }

    /**
	 * 根据目录名,文件名生成完整路径.
	 *
	 * @param strPath
	 * @param strFileName
	 * @return 完整路径
	 */
    public static String assembleFilePath(String strPath, String strFileName) {
        String path = convertPath(strPath);
        if (path.charAt(path.length() - 1) != '/') {
            path = path + "/";
        }
        return (path + strFileName).replaceAll("//", "/");
    }

    /**
	 * 获得输入流
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    /**
	 * 关闭输入流
	 *
	 * @param input
	 */
    public static void closeInputStream(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
        }
    }

    /**
	 * 获得输出流
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    /**
	 * 关闭输出流
	 *
	 * @param output
	 */
    public static void closeOutputStream(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
        }
    }

    /**
	 * 将byte数组写入OutputStream 。
	 *
	 * @param data
	 * @param output
	 * @throws IOException
	 */
    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
	 * 将byte数组写入文件。
	 *
	 * @param data
	 * @param file
	 * @throws IOException
	 */
    public static void write(byte[] data, File file) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            write(data, out);
        } finally {
            closeOutputStream(out);
        }
    }

    /**
	 * 将String按指定编码格式写入OutputStream 。
	 *
	 * @param data
	 * @param output
	 * @param encoding
	 * @throws IOException
	 */
    public static void write(String data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            if (encoding == null) {
                write(data, output);
            } else {
                output.write(data.getBytes(encoding));
            }
        }
    }

    /**
	 * 将String按指定编码格式写入文件。
	 *
	 * @param data
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
    public static void write(String data, File file, String encoding) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            write(data, out, encoding);
        } finally {
            closeOutputStream(out);
        }
    }

    /**
	 * 将String写入OutputStream 。
	 *
	 * @param data
	 * @param output
	 * @throws IOException
	 */
    public static void write(String data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.getBytes());
        }
    }

    /**
	 * 将String写入指定文件。
	 *
	 * @param data
	 * @param file
	 * @throws IOException
	 */
    public static void write(String data, File file) throws IOException {
        write(data, file, null);
    }

    /**
	 * 将InputStream中的内容写入OuputStream中，并返回实际写入的字节数。
	 *
	 * @param input
	 * @param output
	 * @return
	 * @throws IOException
	 */
    public static long write(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
	 * 将文件中的内容写入OutputSteam中，并返回实际写入的字节数。
	 * @param input
	 * @param output
	 * @return
	 * @throws IOException
	 */
    public static long write(File input, OutputStream output) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(input);
            return write(in, output);
        } finally {
            closeInputStream(in);
        }
    }

    /**
	 * 将InputStream中的内容写入文件中，并返回实际写入的字节数。
	 * @param input
	 * @param file
	 * @return
	 * @throws IOException
	 */
    public static long write(InputStream input, File file) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            return write(input, out);
        } finally {
            closeOutputStream(out);
        }
    }

    /**
	 * 读取InputStream中的内容，返回byte数组。
	 *
	 * @param input
	 * @return
	 * @throws IOException
	 */
    public static byte[] readToByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, output);
        return output.toByteArray();
    }

    /**
	 * 读取文件中的内容，返回byte数组。
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
    public static byte[] readToByteArray(File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return readToByteArray(in);
        } finally {
            closeInputStream(in);
        }
    }

    /**
	 * 得到目录以及其子目录下的所有文件。
	 *
	 * @param directory
	 * @param recursive
	 * @return
	 */
    public static Collection listFiles(File directory) {
        Collection files = new ArrayList();
        innerListFiles(files, directory);
        return files;
    }

    /**
	 * 列出目录下的文件。recursive为true时，循环获取子目录下的文件。
	 *
	 * @param files
	 * @param directory
	 * @param recursive
	 */
    private static void innerListFiles(Collection files, File directory) {
        File[] found = directory.listFiles();
        if (found != null) {
            for (int i = 0; i < found.length; i++) {
                if (found[i].isDirectory()) {
                    innerListFiles(files, found[i]);
                } else {
                    files.add(found[i]);
                }
            }
        }
    }

    /**
	 * 删除目录以及该目录下的子目录和文件
	 *
	 * @param directory
	 * @throws IOException
	 */
    public static void deleteDirectory(File directory) throws IOException {
        if (directory == null || !directory.exists()) {
            return;
        }
        cleanDirectory(directory);
        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
	 * 清除目录下的子目录和文件
	 *
	 * @param directory
	 * @throws IOException
	 */
    public static void cleanDirectory(File directory) throws IOException {
        if (directory == null || !directory.exists() || !directory.isDirectory()) return;
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
        }
        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }
        if (null != exception) {
            throw exception;
        }
    }

    /**
	 * 删除文件或目录。如果该目录下有文件或子目录，将一并删除。
	 *
	 * @param file
	 *            文件或目录
	 * @throws IOException
	 */
    public static void forceDelete(File file) throws IOException {
        if (file == null || !file.exists()) return;
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.delete()) {
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
}
