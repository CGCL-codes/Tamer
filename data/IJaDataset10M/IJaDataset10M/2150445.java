package scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PrintFile {

    public static void main(String[] args) {
        try {
            Scanner fs = new Scanner(new File("scanner/PrintFile1.java"));
            int num = 1;
            while (fs.hasNext()) {
                String line = fs.nextLine();
                System.out.printf("%s: %s\n", num, line);
                ++num;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oops, I couldn't fidn the file.");
        }
    }
}
