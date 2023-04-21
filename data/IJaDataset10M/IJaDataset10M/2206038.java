package com.enerjy.analyzer.java.rules.testfiles.T0080;

import java.math.*;

public class PTest22 {

    static double test03() {
        BigDecimal bd = new BigDecimal("0.001");
        return bd.doubleValue();
    }
}
