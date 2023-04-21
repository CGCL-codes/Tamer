package org.helios.euler;

import java.math.*;

public class Problem16 {

    public Problem16() {
        BigInteger numero = new BigInteger("2");
        numero = numero.pow(1000);
        StringBuffer buffer = new StringBuffer(numero.toString());
        int somma = 0;
        for (int i = 0; i < buffer.length(); i++) {
            somma = somma + Integer.parseInt("" + buffer.charAt(i));
        }
        System.out.println(somma);
    }

    public static void main(String[] args) {
        new Problem16();
    }
}
