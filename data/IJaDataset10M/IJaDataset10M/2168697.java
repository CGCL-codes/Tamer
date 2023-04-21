package com.lineadecodigo.java.basico;

import java.util.ArrayList;
import java.util.List;

/**
 * @file ForEach.java
 * @version 1.0
 * @author Linea de Codigo (http://lineadecodigo.com)
 * @date   01-febrero-2009
 * @url    http://lineadecodigo.com/2009/02/02/bucle-for-each-en-java/
 * @description Utilizar de bucle for-each 
 */
public class ForEach {

    public static void main(String[] args) {
        String array[] = { "Avila", "Burgos", "Le�n", "Palencia", "Salamanca", "Segovia", "Soria", "Valladolid", "Zamora" };
        for (String elemento : array) System.out.println(elemento);
        List<String> list = new ArrayList<String>();
        list.add("Victor");
        list.add("Amaya");
        list.add("Julio");
        for (String nombre : list) System.out.println(nombre);
    }
}
