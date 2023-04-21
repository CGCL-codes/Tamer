package com.lineadecodigo.java.util;

import java.util.Formatter;

public class CerosALaIzquierda {

    public static void main(String[] args) {
        int numero = 425;
        int numero2 = 893;
        Formatter fmt = new Formatter();
        fmt.format("%08d", numero);
        System.out.println("El numero formateado " + fmt);
        fmt.format("Primer n�mero %1$08d - Segundo n�mero %2$08d ", numero, numero2);
        System.out.println(fmt);
    }
}
