package tests.test;

import java.io.*;

public class NativeGetstatic {

    static int i = 300;

    static String hello = "Hello!";

    /**
       @return The value of the static field `hello'.
     */
    static native String getHello();

    /**
       @return The value of the static field `i'.
     */
    static native int getI();

    public static void main(String[] args) {
        try {
            System.out.println(getHello());
        } catch (NullPointerException e) {
            System.out.println("Field `hello' is not initialized yet.");
        }
        System.out.println(hello);
        System.out.println(getHello());
        System.out.println(getI());
    }
}
