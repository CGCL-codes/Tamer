package org.ladybug.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Aurelian Pop
 */
public final class FileSystem {

    private FileSystem() {
    }

    public static char[] readFile(final File file, final int bucketCapacity) throws IOException {
        final Reader reader = new BufferedReader(new FileReader(file));
        final List<char[]> buckets = new LinkedList<char[]>();
        char[] bucket = new char[bucketCapacity];
        int readBytes = 0;
        while (true) {
            if (readBytes == bucketCapacity) {
                buckets.add(bucket);
                readBytes = 0;
                bucket = new char[bucketCapacity];
            }
            final int readAttempt = reader.read(bucket, readBytes, bucketCapacity - readBytes);
            if (readAttempt == -1) {
                if (readBytes > 0) {
                    buckets.add(bucket);
                }
                break;
            }
            readBytes += readAttempt;
        }
        reader.close();
        final char[] data = new char[(buckets.size() - 1) * bucketCapacity + readBytes];
        int copiedChars = 0;
        for (final char[] tempBucket : buckets) {
            final int bucketCopySize = copiedChars > data.length - bucketCapacity ? data.length - copiedChars : bucketCapacity;
            System.arraycopy(tempBucket, 0, data, copiedChars, bucketCopySize);
            copiedChars += bucketCopySize;
        }
        return data;
    }

    public static void writeFile(final File file, final char[] cbuf, final boolean append) throws IOException {
        final Writer writer = new BufferedWriter(new FileWriter(file, append));
        writer.write(cbuf);
        writer.close();
    }

    public static void writeFile(final File file, final String data, final boolean append) throws IOException {
        writeFile(file, data.toCharArray(), append);
    }

    public static boolean deleteFile(final File file) {
        boolean result = true;
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            for (final File f : files) {
                result &= deleteFile(f);
            }
        }
        return result & file.delete();
    }

    public static String getRelativePathToRunningDirectory(final File file) {
        final String runPath = Constants.RUNNING_DIRECTORY.getAbsoluteFile().getParentFile().getAbsolutePath();
        String filePath = file.getAbsolutePath();
        if (filePath.startsWith(runPath)) {
            filePath = filePath.substring(runPath.length());
            if (filePath.startsWith(File.separator)) {
                filePath = filePath.substring(1);
            }
        }
        return filePath;
    }
}
