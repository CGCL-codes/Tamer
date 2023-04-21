package org.susan.java.io;

import java.io.RandomAccessFile;

public class RandomAccessFileMain {

    public static void main(String args[]) throws Exception {
        RandomAccessFile file = new RandomAccessFile("D:/test.txt", "rw");
        for (int i = 0; i <= 6; i++) {
            System.out.println(file.readLine());
        }
        long current = file.getFilePointer();
        file.seek(current + 0);
        file.write("34".getBytes());
        for (int i = 0; i <= 6; i++) {
            System.out.println(file.readLine());
        }
        current = file.getFilePointer();
        file.seek(current + 6);
        file.write("27".getBytes());
        file.close();
    }
}
