package com.enerjy.analyzer.java.rules.testfiles.T0262;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

public class PTest07 {

    public void aMethod() {
        Writer out = new CharArrayWriter();
        try {
            out.write('c');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
