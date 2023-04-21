package edu.bsu.monopd;

import java.util.Scanner;
import sun.misc.Regexp;

public class test {

    public static void main(String[] args) {
        System.out.println("THis is a test #ARG".substring("THis is a test #ARG".indexOf("#ARG", 0)));
        String josh = "this is a test #ARG 2 khkjhkjh";
        int pos = josh.indexOf("#ARG", 0);
        System.out.println(josh);
        System.out.println(new Scanner(josh.substring(pos + 4)).nextInt());
        josh = josh.replaceFirst("#ARG " + new Scanner(josh.substring(pos + 4)).nextInt(), "" + 65);
        System.out.println(josh);
    }
}
