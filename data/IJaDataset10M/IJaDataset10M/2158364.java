package org.jalajala.example.set;

import org.jalajala.test.*;
import java.util.*;
import java.lang.reflect.*;

public class SetGet extends org.jalajala.example.map.MapGet {

    Class testClass;

    Set testSet;

    public SetGet() {
    }

    public void setMapClass(String className) {
    }

    public void setSetClass(String className) {
        try {
            testClass = Class.forName(className);
            testSet = (Set) testClass.newInstance();
        } catch (Exception e) {
            System.out.println("Error en setClass:" + e);
        }
    }

    public void init() {
        Random randomizer = new Random();
        try {
            if (objectType == 1) {
                tabla = new Integer[tamanoTabla];
                tablaRecuperacion = new Integer[tamanoTabla];
            } else {
                tabla = new String[tamanoTabla];
                tablaRecuperacion = new String[tamanoTabla];
            }
            for (int i = 0; i < tamanoTabla; i++) {
                if (objectType == 1) {
                    tabla[i] = new Integer(i);
                    tablaRecuperacion[i] = new Integer(i);
                } else {
                    int longitud = randomizer.nextInt(150) + 1;
                    char tablaCaracteres[] = new char[longitud];
                    for (int j = 0; j < longitud; j++) {
                        tablaCaracteres[j] = (char) randomizer.nextInt(256);
                    }
                    tabla[i] = new String(tablaCaracteres);
                    tablaRecuperacion[i] = new String(tablaCaracteres);
                }
            }
            if (modoAleatorio == true) {
                for (int j = 0; j < tamanoTabla; j++) {
                    int first, second;
                    Object temp;
                    first = randomizer.nextInt(tamanoTabla);
                    second = randomizer.nextInt(tamanoTabla);
                    temp = tabla[first];
                    tabla[first] = tabla[second];
                    tabla[second] = temp;
                }
            }
            if (getAleatorio == true) {
                for (int j = 0; j < tamanoTabla; j++) {
                    int first, second;
                    Object temp;
                    first = randomizer.nextInt(tamanoTabla);
                    second = randomizer.nextInt(tamanoTabla);
                    temp = tabla[first];
                    tablaRecuperacion[first] = tablaRecuperacion[second];
                    tablaRecuperacion[second] = temp;
                }
            }
            for (int i = 0; i < tamanoTabla; i++) {
                testSet.add(tabla[i]);
            }
        } catch (Exception e) {
            System.out.println("Error en init:" + e);
        }
    }

    public void run() {
        for (int i = 0; i < numeroRecuperaciones; i++) {
            testSet.contains(tablaRecuperacion[i]);
        }
    }
}
