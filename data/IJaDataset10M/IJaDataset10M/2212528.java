package com.ibm.wala.dila.tests.data.callgraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Exception1 {

    public static void main(String[] args) {
        A.foo();
    }

    static class A {

        public static void foo() {
            File f = new File("nosuchfile.file");
            try {
                FileReader fr = new FileReader(f);
                fr.read();
                fr.close();
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
