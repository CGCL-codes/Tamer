package org.eclipse.epsilon.antlr.dev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtils {

    public static void main(String[] args) throws Exception {
        File f = new File("E:\\Projects\\Eclipse\\3.3\\workspace\\org.epsilon.antlr\\src\\org\\epsilon\\antlr\\Test.txt");
        FileUtils.prepend("Hello World\r\n\r\n", f);
    }

    protected static void prepend(String str, File f) throws Exception {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        StringBuffer buffer = new StringBuffer();
        while (line != null) {
            buffer.append(line + "\r\n");
            line = br.readLine();
        }
        br.close();
        FileWriter fw = new FileWriter(f);
        fw.write(str + buffer.toString());
        fw.close();
    }
}
