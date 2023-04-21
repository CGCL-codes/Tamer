package com.googlecode.javatips4u.effectivejava.unnecessary;

public class AutoboxIssue {

    public static void main(String[] args) {
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}
