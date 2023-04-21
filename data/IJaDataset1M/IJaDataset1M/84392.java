package com.onehao.io;

import java.io.File;

public class FileTest4 {

    public static void main(String[] args) {
        File file = new File("src/com/onehao");
        String[] names = file.list();
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println("----------------");
        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println(f.getName());
            System.out.println(f.getParent());
        }
    }
}
