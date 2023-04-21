package com.b2.Ch03;

public class MathFunctionsApp {

    public static void main(String[] args) {
        int a = 100;
        int b = -50;
        int c = 3;
        double x = 25.0;
        double y = 3.0;
        double z = 4.0;
        System.out.println("abs(b)    = " + Math.abs(b));
        System.out.println("cbrt(x)   = " + Math.cbrt(x));
        System.out.println("exp(y)    = " + Math.exp(z));
        System.out.println("hypot(y,z)= " + Math.hypot(y, z));
        System.out.println("log(y)    = " + Math.log(y));
        System.out.println("log10(y)  = " + Math.log10(y));
        System.out.println("max(a, b) = " + Math.max(a, b));
        System.out.println("min(a, b) = " + Math.min(a, b));
        System.out.println("pow(a, c) = " + Math.pow(a, c));
        System.out.println("random()  = " + Math.random());
        System.out.println("signum(b) = " + Math.signum(b));
        System.out.println("sqrt(x)   = " + Math.sqrt(y));
    }
}
