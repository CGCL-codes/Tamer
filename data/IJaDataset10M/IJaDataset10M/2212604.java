package com.lineadecodigo.java.util.collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Alumnos {

    private int age;

    private String name;

    public static void main(String[] args) {
        Alumnos person1 = new Alumnos("Juan", 18);
        Alumnos person2 = new Alumnos("Miguel", 25);
        Alumnos person3 = new Alumnos("Luis", 18);
        Alumnos person4 = new Alumnos("Luis", 18);
        HashSet<Alumnos> personas = new HashSet<Alumnos>();
        personas.add(person1);
        personas.add(person2);
        personas.add(person3);
        personas.add(person4);
        System.out.println(personas.size());
        Map<Alumnos, String> m = new HashMap<Alumnos, String>();
        m.put(person1, "valor1");
        m.put(person2, "valor2");
        m.put(person3, "valor1");
        m.put(person4, "mi valor");
        System.out.println(m.keySet());
        System.out.println(m.size());
    }

    Alumnos(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Alumnos) {
            Alumnos p = (Alumnos) o;
            return this.name.equals(p.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return age * this.name.length();
    }
}
