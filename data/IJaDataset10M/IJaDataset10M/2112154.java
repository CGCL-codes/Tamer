package com.google.code.nanorm.test.common;

/**
 * Functions to be used for stored procedures call test.
 * 
 * @author Ivan Dubrov
 */
public class Funcs {

    /**
     * Return concatenation of two strings.
     * 
     * @param a first string
     * @param b second string
     * @return concatenation of two strings
     */
    public static String concat(String a, String b) {
        return a + b;
    }

    /**
     * Return concatenation of two strings. Set the result to the string holder.
     * 
     * @param a first string
     * @param b second string
     * @param holder string holder to set result to
     * @return concatenation of two strings
     */
    public static String concat2(String a, String b, StringHolder holder) {
        holder.setValue(a + b);
        return holder.getValue();
    }
}
