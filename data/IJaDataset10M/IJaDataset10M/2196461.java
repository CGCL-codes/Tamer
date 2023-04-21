package org.databene.commons;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Provides ZIP-related convenience methods.<br/><br/>
 * Created: 20.10.2011 15:19:07
 * @since 0.5.10
 * @author Volker Bergmann
 */
public class ZipUtil {

    private static final int BUFFER_SIZE = 2048;

    public static void compress(File source, File zipFile) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        out.setMethod(ZipOutputStream.DEFLATED);
        try {
            addFileOrDirectory(source, source, out);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Zipping the report failed");
        }
    }

    private static void addFileOrDirectory(File source, File root, ZipOutputStream out) throws IOException {
        if (source.isFile()) addFile(source, root, out); else if (source.isDirectory()) addDirectory(source, root, out);
    }

    private static void addDirectory(File source, File root, ZipOutputStream out) throws IOException {
        for (File file : source.listFiles()) addFileOrDirectory(file, root, out);
    }

    private static void addFile(File source, File root, ZipOutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream in = new BufferedInputStream(new FileInputStream(source));
        ZipEntry entry = new ZipEntry(FileUtil.relativePath(root, source));
        out.putNextEntry(entry);
        int count;
        while ((count = in.read(buffer, 0, BUFFER_SIZE)) != -1) out.write(buffer, 0, count);
        in.close();
    }
}
