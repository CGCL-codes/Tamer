package com.dm;

import java.io.File;

public class FileTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        File dir = new File("c:/");
        File[] subDir = dir.listFiles();
        for (int i = 0; i < subDir.length; i++) {
            if (subDir[i].isDirectory()) System.out.println(subDir[i].getName());
        }
    }
}
