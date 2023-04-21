package com.silveregg.wrapper.test;

import java.io.FileWriter;
import java.io.IOException;

public class OutputLoader {

    public static void main(String[] args) {
        System.out.println("Start outputting lots of data.");
        long start = System.currentTimeMillis();
        long now;
        int count = 0;
        while ((now = System.currentTimeMillis()) < start + 20000) {
            System.out.println("Testing line Out #" + (++count));
            System.err.println("Testing line Err #" + (++count));
        }
        System.out.println("Printed " + count + " lines of output in 20 seconds");
        try {
            FileWriter fw = new FileWriter("OutputLoader.log", true);
            fw.write("Printed " + count + " lines of output in 20 seconds\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
