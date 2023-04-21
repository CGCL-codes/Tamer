package com.nhncorp.cubridqa.test.xstream;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import junit.framework.TestCase;

public class NumberTest extends TestCase {

    public void testA() {
        float a = 10.222f;
        NumberFormat nf = new DecimalFormat("0.0");
        System.out.println(nf.format(a));
        String test = "0?222";
        System.out.println("new--" + test.substring(0, test.lastIndexOf("?")));
    }
}
