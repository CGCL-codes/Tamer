package sample.chapter6.patterMatching;

import java.io.Console;

public class Formatter {

    public static void main(String[] args) {
        Console c = System.console();
        double i;
        String format;
        String input;
        while (true) {
            input = c.readLine("%s", "Integer: ");
            if (input.equals("exit")) System.exit(0);
            i = Double.parseDouble(input);
            format = c.readLine("%s", "Pattern: ");
            System.out.printf("'" + format + "'\n", i);
            System.out.format("'" + format + "'\n", i);
        }
    }
}
