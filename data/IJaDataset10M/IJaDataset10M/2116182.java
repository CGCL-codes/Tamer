package com.onehao.io2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class InputStreamTest1 {

    public static void main(String[] args) throws Exception {
        File file = new File("src/com/onehao/io2/InputStreamTest1.java");
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[200];
        int length = 0;
        while (-1 != (length = is.read(buffer, 0, 200))) {
            String str = new String(buffer, 0, length);
            System.out.println(str);
        }
        is.close();
    }
}
