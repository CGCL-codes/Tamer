package cedview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Helper {

    public static BufferedReader getBufferedReader(String filePath) {
        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(fis, "8859_9");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(isr);
        return bufferedReader;
    }

    public static BufferedReader getBufferedReader(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(fis, "8859_9");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(isr);
        return bufferedReader;
    }

    public static BufferedWriter getBufferedWriter(File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        OutputStreamWriter osr = null;
        try {
            osr = new OutputStreamWriter(fos, "8859_9");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(osr);
        return bufferedWriter;
    }

    public static BufferedWriter getBufferedWriter(String filePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        OutputStreamWriter osr = null;
        try {
            osr = new OutputStreamWriter(fos, "8859_9");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(osr);
        return bufferedWriter;
    }
}
