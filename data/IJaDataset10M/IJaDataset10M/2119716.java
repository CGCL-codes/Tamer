package com.enerjy.analyzer.java.rules.testfiles.T0244;

/**
 * @author Peter Carr
 */
public class FTest03 {

    /**
     * Error: declaration of Clone differs from super class 'clone'.
     * This gets logged even through the arguments differ.
     */
    public Object Clone(Object obj) throws CloneNotSupportedException {
        System.out.println("obj: " + obj.toString());
        return super.clone();
    }
}
