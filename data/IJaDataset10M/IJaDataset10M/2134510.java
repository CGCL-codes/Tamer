package com.enerjy.analyzer.java.rules.testfiles.T0252;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Test of Java 5.0 Generic Types.
 * 
 * @author Peter Carr
 */
@SuppressWarnings("all")
public class PTest01 {

    int counter = 0;

    public void test01() {
        Vector v = new Vector();
        for (Enumeration e = v.elements(); e.hasMoreElements(); ) {
            System.out.println(e.nextElement());
        }
    }
}
