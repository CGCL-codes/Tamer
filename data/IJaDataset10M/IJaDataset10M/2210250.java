package bol2;

import bol1.Buzon;

public class Productor implements Runnable {

    private String nombre = null;

    private Buzon buzon = null;

    private int id, numelementos;

    private Thread t;

    public Productor(String nombre, Buzon buzon, int id, int numelementos) {
        this.nombre = nombre;
        this.buzon = buzon;
        this.id = id;
        this.numelementos = numelementos;
        t = new Thread(this);
    }

    public void run() {
        for (int i = 0; i < numelementos; i++) {
            buzon.escribir(id, new Integer(i));
        }
        buzon.escribir(id, new Integer(-1));
        System.out.println("Fin Productor " + nombre + " ----------------------");
    }

    public void start() {
        t.start();
    }
}
