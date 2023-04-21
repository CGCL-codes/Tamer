package com.extentech.luminet;

import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;

public class Logstream extends OutputStream {

    private JTextArea log;

    PrintStream s;

    public Logstream() {
        super();
    }

    public Logstream(JTextArea l, OutputStream out) {
        super();
        s = new PrintStream(out);
        log = l;
    }

    public PrintStream getStream() {
        return s;
    }

    public void setStream(OutputStream out) {
        PrintStream s = new PrintStream(out);
    }

    public void setLog(JTextArea l) {
        log = l;
    }

    public void write(int i) {
        println(String.valueOf(i));
    }

    public void println(String txt) {
        log.append(txt + "\r\n");
    }
}
