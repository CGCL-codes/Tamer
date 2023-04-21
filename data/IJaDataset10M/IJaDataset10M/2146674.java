package com.zatysoft.jutty.selenium;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class SeleniumSuiteGenerator {

    public static void main(String[] args) {
        try {
            File suite = new File("src/test/selenium/MasterSuite.html");
            suite.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(suite));
            writeFileHeader(out);
            File dir = new File("src/test/selenium");
            ArrayList<String> testFiles = new ArrayList<String>();
            getFiles(testFiles, dir);
            Iterator<String> testIt = testFiles.iterator();
            while (testIt.hasNext()) {
                String fileName = testIt.next();
                writeTestToSuite(fileName, out);
            }
            writeFileFooter(out);
            out.close();
            System.out.println("Added " + testFiles.size() + " test classes to suite");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeFileFooter(BufferedWriter out) throws IOException {
        out.write("</table>");
        out.newLine();
        out.newLine();
        out.write("</body>");
        out.newLine();
        out.write("</html>");
        out.newLine();
    }

    private static void writeFileHeader(BufferedWriter out) throws IOException {
        out.write("<html>");
        out.newLine();
        out.write("<head>");
        out.newLine();
        out.write("<title>Generic EStore Master Test Suite</title>");
        out.newLine();
        out.write("</head>");
        out.newLine();
        out.write("<body>");
        out.newLine();
        out.newLine();
        out.write("<table>");
        out.newLine();
        out.write("<tr><td><b>Suite Of Tests</b></td></tr>");
        out.newLine();
    }

    private static void writeTestToSuite(String fileName, BufferedWriter out) throws IOException {
        System.out.println("Adding " + fileName);
        fileName = fileName.replace('\\', '/');
        String relativePath = fileName.replaceFirst("src/test/selenium/", "/");
        fileName = fileName.replace('/', '-');
        String[] values = fileName.split("-");
        String testName = "";
        for (int i = 3; i < values.length; i++) {
            String value = values[i];
            if (value.endsWith(".html")) {
                testName += value.substring(0, value.indexOf("."));
                break;
            }
            testName += value + " - ";
        }
        if (testName.indexOf("~") == -1) {
            out.write("<tr><td><a href=\"." + relativePath + "\">" + testName + "</a></td></tr>");
            out.newLine();
        }
    }

    public static void getFiles(ArrayList<String> testFiles, File dir) throws Exception {
        File[] files = dir.listFiles();
        String test = System.getProperty("dotest");
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getFiles(testFiles, files[i]);
            }
            int index = files[i].getName().indexOf(".html");
            int badIndex = files[i].getCanonicalPath().indexOf(".svn");
            int badIndex2 = files[i].getAbsolutePath().indexOf("database");
            if (index > -1 && badIndex == -1 && badIndex2 == -1) {
                if (test == null || files[i].getName().startsWith(test)) {
                    testFiles.add(files[i].getPath());
                }
            }
        }
    }
}
