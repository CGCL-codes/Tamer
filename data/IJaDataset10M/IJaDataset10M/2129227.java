package com.joe.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTest {

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("e:\\rtest.txt", "rw");
        for (int i = 0; i < 10; i++) raf.writeDouble(i * 1.414);
        raf.close();
        raf = new RandomAccessFile("e:\\rtest.txt", "rw");
        raf.seek(5 * 8);
        raf.writeDouble(47.0001);
        raf.close();
        raf = new RandomAccessFile("e:\\rtest.txt", "r");
        for (int i = 0; i < 10; i++) System.out.println("Value " + i + ": " + raf.readDouble());
        raf.close();
    }
}
