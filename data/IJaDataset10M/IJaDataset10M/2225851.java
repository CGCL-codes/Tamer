package net.srcz.jsjvmold;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    private static final int[] MAGIC_NUMBER = { 0xCA, 0xFE, 0xBA, 0xBE };

    public Main() throws Exception {
        File f = new File("./bin/net/srcz/jsjvm/Essai.class");
        FileInputStream fr = new FileInputStream(f);
        byte[] magic = readBytes(fr, 4);
        byte[] minor = readBytes(fr, 2);
        System.out.println("minor : " + Tool.byteArrayToHexString(minor));
        byte[] major = readBytes(fr, 2);
        System.out.println("major : " + Tool.byteArrayToHexString(major));
        int poolCount = readUshort(fr);
        System.out.println("pool : " + poolCount);
        int length = readUshort(fr);
        byte[] string = readBytes(fr, length);
        System.out.println(new String(string));
        fr.close();
    }

    private byte[] readBytes(InputStream is, int nb) throws IOException {
        byte[] bytes = new byte[nb];
        is.read(bytes);
        return bytes;
    }

    private int readUshort(InputStream is) throws IOException {
        byte[] major = new byte[2];
        is.read(major);
        return ushort(major);
    }

    private int ushort(byte[] bytes) {
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            new Main();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
