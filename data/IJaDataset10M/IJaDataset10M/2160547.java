package org.susan.java.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class SingleLineScanner {

    public static void main(String args[]) throws FileNotFoundException {
        InputStream in = new FileInputStream("D:/work/AutoSubmit.java");
        Scanner scanner = new Scanner(in);
        int number = 1;
        while (scanner.hasNextLine()) {
            System.out.print(number + ".  ");
            System.out.println(scanner.nextLine());
            number++;
        }
    }
}
