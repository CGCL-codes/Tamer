package net.sf.dslrunner.runner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

class FileUtil {

    private final String ENCODING = "UTF-8";

    StringBuilder read(File path) {
        if (path == null || !path.exists() || path.isDirectory()) return null;
        try {
            ReadableByteChannel channel = new FileInputStream(path).getChannel();
            ByteBuffer buf = ByteBuffer.allocateDirect(1024);
            byte[] array = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = 0;
            while ((read = channel.read(buf)) >= 0) {
                buf.rewind();
                buf.get(array, 0, read);
                baos.write(array, 0, read);
                buf.rewind();
            }
            baos.close();
            channel.close();
            return new StringBuilder(baos.toString(ENCODING));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void write(File file, String content) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            final FileOutputStream fos = new FileOutputStream(file);
            final OutputStreamWriter osw = new OutputStreamWriter(fos, ENCODING);
            osw.write(content);
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
