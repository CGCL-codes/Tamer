package edu.java.lectures.lec08.exceptions.basics;

import java.util.Scanner;

public class TryCatchFinallyConstruct2 {

    public static void main(String[] args) {
        System.out.println("Enter a number: ");
        Scanner input = new Scanner(System.in);
        int number = input.nextInt();
        if (input != null) {
            input.close();
            input = null;
        }
        doTryAndCatch(number);
    }

    public static void doTryAndFinally(int number) {
        try {
            int a = 5 / number;
            Object obj = null;
            obj.toString();
            System.out.println("Pesho");
        } finally {
            System.out.println("in the finally block");
        }
        System.out.println("Out of try-catch-block.");
    }

    public static void doTryAndCatch(int number) {
        try {
            int a = 5 / number;
            Object obj = null;
            obj.toString();
            System.out.println("Pesho");
        } catch (ArithmeticException ae) {
            System.out.println("in the arithmetic catch block");
        } catch (NullPointerException npe) {
            System.out.println("in the null-pointer catch block");
        }
        System.out.println("Out of try-catch-block.");
    }
}
