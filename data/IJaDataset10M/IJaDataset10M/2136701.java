package net.messze.jimposition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class Processor {

    /** Creates a new instance of Processor */
    public Processor() {
    }

    public static byte[] processor(Collection<Transformation> processors, byte[] input) {
        byte[] bytes = input;
        Iterator<Transformation> it = processors.iterator();
        while (it.hasNext()) {
            bytes = it.next().alter(bytes);
        }
        return bytes;
    }

    public static void processor(Collection<Transformation> processors, File input, File output) throws FileNotFoundException {
        try {
            byte[] inputStream = readFile(input);
            byte[] outputStream = null;
            outputStream = processor(processors, inputStream);
            saveFile(output, outputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] readFile(File file) throws FileNotFoundException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FileInputStream input = new FileInputStream(file);
        int inputbyte;
        while ((inputbyte = input.read()) != -1) {
            output.write(inputbyte);
        }
        input.close();
        return output.toByteArray();
    }

    public static void saveFile(File file, byte[] bytes) throws FileNotFoundException, IOException {
        FileOutputStream output = new FileOutputStream(file);
        output.write(bytes);
        output.close();
    }
}
