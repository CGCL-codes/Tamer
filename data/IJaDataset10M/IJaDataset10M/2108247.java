package net.tinyos.util;

import java.io.*;

public class PrintStreamMessenger implements Messenger {

    private PrintStream ps;

    public PrintStreamMessenger(PrintStream ps) {
        this.ps = ps;
    }

    public void message(String s) {
        ps.println(s);
    }

    public static PrintStreamMessenger err = new PrintStreamMessenger(System.err);

    public static PrintStreamMessenger out = new PrintStreamMessenger(System.out);
}
