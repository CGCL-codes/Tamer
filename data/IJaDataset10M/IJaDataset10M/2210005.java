package org.susan.java.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.jar.Pack200;

public class Pack200Tester {

    public static void main(String args[]) throws Exception {
        JarFile file = new JarFile("D:/work/study.jar");
        Pack200.Packer packer = Pack200.newPacker();
        OutputStream out = new FileOutputStream("D:/work/study.pack");
        packer.pack(file, out);
        out.close();
        File inputFile = new File("D:/work/study.jar");
        File outputFile = new File("D:/work/study.pack");
        System.out.println("Before Pack Size: " + inputFile.length());
        System.out.println("After Pack Size: " + outputFile.length());
    }
}
