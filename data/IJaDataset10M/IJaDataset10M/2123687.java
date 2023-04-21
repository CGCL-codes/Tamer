package edu.ucla.stat.SOCR.util;

import java.io.*;

public class FileHelper {

    String fileName = "";

    FileWriter fw = null;

    public FileHelper() {
    }

    public boolean openWriter(String fileName) {
        try {
            fw = new FileWriter(fileName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean write(String outputString) {
        char[] charsToWrite = outputString.toCharArray();
        try {
            fw.write(charsToWrite);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean closeWriter() {
        try {
            fw.flush();
            fw.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
