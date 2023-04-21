package com.enerjy.analyzer.java.rules.testfiles.T0251;

import java.io.PrintWriter;
import java.util.Locale;

public class FTest10 {

    PrintWriter pw = null;

    public void aMethod() {
        pw.printf(Locale.ENGLISH, "%s\n", "Message");
    }
}
