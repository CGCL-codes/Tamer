package com.enerjy.analyzer.java.rules.testfiles.T0269;

/**
 * @author twarin01
 */
public class PTest08 {

    public String aMethod() {
        StringBuffer buf = new StringBuffer();
        return buf.append("foo").append(false).append("bar").toString();
    }
}
