package net.woodstock.rockapi.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import net.woodstock.rockapi.io.InputOutputStream;
import net.woodstock.rockapi.io.ReaderWriter;

public class TestStream {

    protected static void test1() {
        try {
            OutputStream os = new InputOutputStream();
            Writer writer = new OutputStreamWriter(os);
            writer.write("Lourival Sabino");
            writer.close();
            InputStream input = ((InputOutputStream) os).getInputStream();
            Scanner scanner = new Scanner(input);
            System.out.println(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void test2() {
        try {
            Writer writer = new ReaderWriter();
            writer.write("Lourival Sabino XXX");
            Scanner scanner = new Scanner(((ReaderWriter) writer).getReader());
            System.out.println(scanner.nextLine());
            writer = new ReaderWriter();
            writer.write("Lourival Sabino YYY");
            scanner = new Scanner(((ReaderWriter) writer).getReader());
            System.out.println(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestStream.test2();
    }
}
