package cen5501c.utilities;

import java.math.BigInteger;
import java.security.*;
import java.io.*;

public class MD5 {

    public static String md5code(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(b);
            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5code(File f) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
            int len = (int) f.length();
            byte[] bt = new byte[len];
            fin.read(bt, 0, len);
            return md5code(bt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(MD5.md5code(new File("test.txt")));
        System.out.println(MD5.md5code(new File("test1.txt")));
    }
}
