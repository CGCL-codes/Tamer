package org.jazzteam.edu.oop.hotelComplex.createObjects;

import java.util.ArrayList;
import java.util.List;
import org.jazzteam.edu.oop.hotelComplex.Administrator;
import org.jazzteam.edu.oop.hotelComplex.Director;
import org.jazzteam.edu.oop.hotelComplex.Human;
import org.jazzteam.edu.oop.hotelComplex.Visitors;

public class Polimorph {

    public static void main(String[] args) {
        List<Human> poli = new ArrayList<Human>();
        poli.add(new Director(1, 35, "Vasili", "men", "full", 1000, 5000));
        poli.add(new Administrator(2, 20, "Karman", "men", "vip", 5000, 10));
        poli.add(new Visitors(1, 40, "Bobik", "men", 4));
        for (int i = 0; i < poli.size(); i++) {
            Human p = poli.get(i);
            System.out.println(p.getName() + " " + p.getAge());
        }
        System.out.println("______________________");
        for (int i = 0; i < poli.size(); i++) {
            Human pa = poli.get(i);
            pa.vozrastPlus();
            System.out.println(pa.getName() + " " + pa.getAge());
        }
        System.out.println("______________________");
        for (int i = 0; i < poli.size(); i++) {
            Human pa = poli.get(i);
            pa.vozrastPlus();
            System.out.println(pa.getName() + " " + pa.getAge());
        }
    }
}
