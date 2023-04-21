package fr.helmet.javafx.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class FileUtils {

    public static byte[] readFileContents(File file) throws IOException {
        InputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int offset = 0;
        int numRead = 0;
        while ((offset < data.length) && ((numRead = fis.read(data, offset, data.length - offset)) >= 0)) {
            offset += numRead;
        }
        if (offset < data.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        return data;
    }

    public static byte[] readInputStreamAsBytes(InputStream stream) throws java.io.IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int l = -1;
        do {
            l = stream.read(buffer);
            if (l > 0) out.write(buffer, 0, l);
        } while (l > 0);
        byte[] data = out.toByteArray();
        stream.close();
        out.close();
        return data;
    }

    public static void readArrayToFile(Integer[] ints, String path) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(path);
        byte[] valueBytes = integersTOBytes(ints);
        fos.write(valueBytes);
        fos.close();
    }

    public static void readArrayToFile(Byte[] bytes, String path) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(path);
        byte[] valueBytes = byteObjectsTOBytes(bytes);
        fos.write(valueBytes);
        fos.close();
    }

    public static void byteToFile(byte[] content, String path) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

    public static String getMimeType(byte[] content) throws MagicParseException, MagicMatchNotFoundException, MagicException {
        MagicMatch match = Magic.getMagicMatch(content);
        return match.getMimeType();
    }

    public static String getMimeType(Integer[] ints) throws MagicParseException, MagicMatchNotFoundException, MagicException {
        byte[] content = integersTOBytes(ints);
        MagicMatch match = Magic.getMagicMatch(content);
        return match.getMimeType();
    }

    public static boolean checkValidMimeType(byte[] content, String requestMimeType) throws MagicParseException, MagicMatchNotFoundException, MagicException {
        MagicMatch match = Magic.getMagicMatch(content);
        return match.getMimeType().equals(requestMimeType);
    }

    public static String getFileExtention(Byte[] bytes) throws MagicParseException, MagicMatchNotFoundException, MagicException {
        byte[] content = byteObjectsTOBytes(bytes);
        MagicMatch match = Magic.getMagicMatch(content);
        return match.getExtension();
    }

    public static String getFileExtention(Integer[] ints) throws MagicParseException, MagicMatchNotFoundException, MagicException {
        byte[] content = integersTOBytes(ints);
        MagicMatch match = Magic.getMagicMatch(content);
        return match.getExtension();
    }

    public static byte[] integersTOBytes(Integer[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = ints[i].byteValue();
        }
        return bytes;
    }

    public static byte[] byteObjectsTOBytes(Byte[] objectsByte) {
        byte[] bytes = new byte[objectsByte.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = objectsByte[i].byteValue();
            ;
        }
        return bytes;
    }
}
