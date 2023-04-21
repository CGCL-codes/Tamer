package ca.ucalgary.cpsc.ebe.fitClipse.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class File {

    public static String readAll(java.io.File file) throws IOException {
        StringBuffer contents = new StringBuffer();
        BufferedReader input = new BufferedReader(new FileReader(file));
        try {
            String line = null;
            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append("\n");
            }
            if (contents.length() >= 2) {
                contents.deleteCharAt(contents.length() - 1);
            }
        } finally {
            input.close();
        }
        return contents.toString();
    }

    public static void write(String text, java.io.File file) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        try {
            output.write(text);
        } finally {
            output.close();
        }
    }

    public static void copy(java.io.File fromFile, java.io.File toFile) throws IOException {
        write(readAll(fromFile), toFile);
    }
}
