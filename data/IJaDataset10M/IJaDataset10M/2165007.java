package org.jazzteam.edu.junit4;

/**
 * @author nastena
 * @version 1.0
 */
public class SimpleMath {

    public SimpleMath() {
    }

    public int add(int a, int b) {
        return a + b;
    }

    public int substract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        return a / b;
    }

    public int sqrt(int a) {
        return (int) Math.sqrt(a);
    }

    public int pow(int a, int b) {
        return (int) Math.pow(a, b);
    }

    public int max(int a, int b) {
        return Math.max(a, b);
    }

    public int min(int a, int b) {
        return Math.min(a, b);
    }
}
