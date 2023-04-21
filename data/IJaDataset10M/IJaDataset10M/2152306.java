package com.interactive.internal.ant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.apache.tools.ant.BuildException;

public class InjectionUtils {

    private InjectionUtils() {
    }

    public static StringBuffer readFile(String fileName) throws BuildException {
        try {
            return read(new BufferedReader(new FileReader(fileName)));
        } catch (IOException ex) {
            throw new BuildException(ex);
        }
    }

    public static StringBuffer readResourceFile(String resourceName) throws BuildException {
        try {
            InputStream inputStream = GenerateVersionMain.class.getResourceAsStream(resourceName);
            if (inputStream == null) throw new BuildException("Cannot find " + resourceName);
            return read(new BufferedReader(new InputStreamReader(inputStream)));
        } catch (IOException ex) {
            throw new BuildException(ex);
        }
    }

    private static StringBuffer read(BufferedReader reader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String line = reader.readLine();
            while (line != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
                line = reader.readLine();
            }
        } finally {
            reader.close();
        }
        return stringBuffer;
    }

    public static void writeFile(String fileName, String text) throws BuildException {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            try {
                writer.print(text);
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    public static void replaceAll(StringBuffer stringBuffer, String tag, String value) {
        int index = stringBuffer.indexOf(tag);
        while (index != -1) {
            int end = index + tag.length();
            stringBuffer.replace(index, end, value);
            index = stringBuffer.indexOf(tag, end);
        }
    }

    public static void replaceLine(StringBuffer stringBuffer, String tag, String value, boolean onlyOneTime) {
        int index = stringBuffer.indexOf(tag);
        if (index == -1) throw new BuildException("Injection point \"" + tag + "\" not found.");
        if (onlyOneTime && stringBuffer.indexOf(tag, index + tag.length()) != -1) throw new BuildException("Injection point \"" + tag + "\" occurs more than once.");
        int start = stringBuffer.lastIndexOf("\n", index) + 1;
        int end = stringBuffer.indexOf("\n", index) + 1;
        stringBuffer.delete(start, end);
        stringBuffer.insert(start, value);
    }
}
