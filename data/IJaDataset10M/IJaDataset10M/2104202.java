package tests;

import java.util.Locale;

public class FormatTests {

    public static void main(String args[]) {
        String bf[] = null;
        int prec = 3;
        double val = 1.9999999;
        String buf = "1.999999999";
        int expo = 0;
        String str;
        str = String.format("%04d", 1);
        System.out.println("Result:" + str);
    }
}
