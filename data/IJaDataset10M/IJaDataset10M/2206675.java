package com.keggview.application.test;

/**
 * @author Pablo
 *
 */
public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String test1 = "#00d0a4";
        String test2 = "#00d0ag";
        Boolean b = test1.matches("(#)([0-9a-f]{6})");
        System.out.println(b.toString());
        b = test2.matches("(#)([0-9a-f]{6})");
        System.out.println(b.toString());
    }
}
