package com.enerjy.analyzer.java.rules.testfiles.T0266;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

public class PTest01 {

    public void aMethod() {
        try {
            System.out.write('c');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class System {

        static Writer out = new CharArrayWriter();
    }
}
