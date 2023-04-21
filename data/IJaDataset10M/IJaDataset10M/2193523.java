package com.tcmj.test.jna.example;

/**
 * https://jna.dev.java.net/#demos
 *
 */
public class App {

    private void test1() throws Exception {
    }

    public static void main(String[] args) {
        try {
            new App().test1();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
