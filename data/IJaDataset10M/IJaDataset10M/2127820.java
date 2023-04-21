package org.helios.euler;

import org.helios.crivello.Crivello;
import java.util.*;

public class Problem7 {

    public Problem7() {
        Crivello c = new Crivello();
        ArrayList<Integer> listone = new ArrayList<Integer>();
        ArrayList<Integer> listaFinita = new ArrayList<Integer>();
        int limite = 200000;
        listone = c.creaListone(limite);
        listaFinita = c.generaNumeri(limite, listone);
        System.out.println(listaFinita.get(10000));
    }

    public static void main(String[] args) {
        new Problem7();
    }
}
