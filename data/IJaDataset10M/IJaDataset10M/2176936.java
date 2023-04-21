package com.sun.tools.example.debug.gui;

import java.io.*;

public class OutputSink extends PrintWriter {

    OutputSink(Writer writer) {
        super(writer);
    }

    public void show() {
    }
}
