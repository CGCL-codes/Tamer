package com.wendal.java.tmpfilesystem;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import com.wendal.java.vfilesystem.IFileSystem;
import com.wendal.java.vfilesystem.VFileSystem;

public class Main {

    private static int filesystemSize = 100 * 1024 * 1024;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File dataFile = File.createTempFile("vfs", null);
        dataFile.deleteOnExit();
        System.out.println(dataFile.getPath());
        IFileSystem fileSystem = new VFileSystem(dataFile, filesystemSize, 4 * 1024);
        Random random = new Random();
        for (int i = 1; i < 10000; i++) {
            fileSystem.add("key_" + i, new byte[5106]);
            fileSystem.remove("key_" + random.nextInt(i));
            fileSystem.read("key_" + i);
            fileSystem.add("key_c_" + i, new byte[9134]);
            fileSystem.remove("key_c_" + random.nextInt(i));
            fileSystem.read("key_c_" + i);
        }
        fileSystem.add("Wendal", "Hi,����!");
        System.out.println(fileSystem.read("Wendal"));
        System.out.println("Before reset: " + dataFile.length());
        fileSystem.reset();
        System.out.println("After reset: " + dataFile.length());
    }
}
