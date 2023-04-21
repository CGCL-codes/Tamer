package com.cs.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * StreamConverter can be used to convert Stream objects to various formats.
 * @author dimitris@jmike.gr
 */
public class IOConverter {

    public static File InputStream2File(InputStream in) throws FileNotFoundException, IOException {
        File file = new File("temp.jpg");
        OutputStream out = new FileOutputStream(file);
        int b = 0;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        out.close();
        return file;
    }
}
