package org.anton.z.music.backup;

import java.io.File;

public class Tests {

    public static void main(String[] args) {
        File file = new File("//anton-server/Public/mnt/Photos/ClipArts/����/766010.JPG");
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            file.lastModified();
        }
        System.out.println("lastModified=" + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            file.length();
        }
        System.out.println("length=" + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            file.length();
            file.lastModified();
        }
        System.out.println("length+lastModified=" + (System.currentTimeMillis() - time));
    }
}
