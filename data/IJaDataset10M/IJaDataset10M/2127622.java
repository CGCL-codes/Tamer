package com.coderdream.chapter21.proxy.sample;

/**
 * <pre>
 * Main.java
 * </pre>
 * 
 */
public class Main {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Printable p = new PrinterProxy("Alice");
        System.out.println("Now Name is " + p.getPrintName() + ".");
        p.setPrinterName("Bob");
        System.out.println("Now Name is " + p.getPrintName() + ".");
        p.print("Hello, world");
    }
}
