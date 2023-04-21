package PolishNatation.PolishNatation;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class WriteAndReadToFile {

    private static Charset charset = Charset.forName("utf-8");

    private static CharsetDecoder decoder = charset.newDecoder();

    public static String writeToFile(String filename, String strPage) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), "windows-1251"));
            writer.write(strPage);
            writer.close();
        } catch (Exception ex) {
        }
        return strPage;
    }

    static String readFile(String filename, String str) {
        FileInputStream fileInputStream;
        FileChannel fileChannel;
        StringBuilder sbl = new StringBuilder(2048);
        long fileSize;
        MappedByteBuffer mBuf;
        try {
            fileInputStream = new FileInputStream(filename);
            fileChannel = fileInputStream.getChannel();
            fileSize = fileChannel.size();
            mBuf = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            CharBuffer cb = decoder.decode(mBuf);
            while (cb.hasRemaining()) {
                sbl.append(String.valueOf((char) cb.get()));
            }
            fileChannel.close();
            fileInputStream.close();
        } catch (IOException exc) {
            System.out.println(exc);
            System.exit(1);
        }
        return sbl.toString();
    }

    public static void main(String[] args) {
        String test = "test test tess aaaa";
        String filename = "./filename.txt";
        writeToFile(filename, test);
        String out = "";
        out = readFile("./filename.txt", "");
        System.out.println("out  " + out);
    }
}
