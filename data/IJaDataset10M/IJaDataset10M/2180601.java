package cartas;

import java.awt.Image;
import java.util.LinkedList;
import java.util.Random;

public class mazo {

    LinkedList<Naipe> mazo;

    Naipe palo;

    public mazo() {
        mazo = new LinkedList<Naipe>();
    }

    public mazo(mazo m) {
        mazo = new LinkedList<Naipe>();
        for (int i = 0; i < m.getSize(); i++) {
            mazo.add(m.getNaipe(i));
        }
    }

    public void sacarNaipe(Naipe n) {
        mazo.remove(n);
    }

    public void anadirNaipe(Naipe n) {
        mazo.add(n);
    }

    public Naipe getNaipe(int index) {
        return mazo.get(index);
    }

    public void barajar() {
        Random r = new Random();
        LinkedList<Naipe> temp = new LinkedList<Naipe>();
        for (int i = 40; i > 0; i--) {
            temp.add(sacarNaipe(r.nextInt(i)));
        }
        for (int i = 40; i > 0; i--) {
            anadirNaipe(temp.remove(r.nextInt(i)));
        }
    }

    public int getSize() {
        return mazo.size();
    }

    public Naipe sacarNaipe() {
        return mazo.remove(mazo.size() - 1);
    }

    public Naipe sacarNaipe(int index) {
        return mazo.remove(index);
    }

    public Naipe palo() {
        return palo;
    }

    public int contar() {
        int puntos = 0;
        for (int i = 0; i < mazo.size(); i++) {
            Naipe n = mazo.get(i);
            puntos = puntos + n.valor();
        }
        return puntos;
    }

    public void printMazo() {
        for (int i = 0; i < mazo.size(); i++) {
            Naipe n = mazo.get(i);
            System.out.println(n.cara());
        }
    }

    public void llenarMazo() {
        for (int i = 0; i < 4; i++) {
            As a = new As(i);
            anadirNaipe(a);
        }
        for (int i = 0; i < 4; i++) {
            Tres a = new Tres(i);
            anadirNaipe(a);
        }
        for (int i = 0; i < 4; i++) {
            Sota a = new Sota(i);
            anadirNaipe(a);
        }
        for (int i = 0; i < 4; i++) {
            Caballo a = new Caballo(i);
            anadirNaipe(a);
        }
        for (int i = 0; i < 4; i++) {
            Rey a = new Rey(i);
            anadirNaipe(a);
        }
        for (int i = 0; i < 4; i++) {
            Blanca a = new Blanca(i, 2);
            anadirNaipe(a);
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 4; j < 8; j++) {
                Blanca a = new Blanca(i, j);
                anadirNaipe(a);
            }
        }
    }

    public void sacarPalo() {
        Random r = new Random();
        palo = mazo.remove(r.nextInt(40));
        mazo.add(0, palo);
    }

    public LinkedList<String> getImages() {
        LinkedList<String> l = new LinkedList<String>();
        for (int i = 0; i < mazo.size(); i++) {
            Naipe n = mazo.get(i);
            l.add(n.imagen());
        }
        return l;
    }

    public mazo JoinMazo(mazo x, mazo y) {
        mazo temp = x;
        temp.mazo.addAll(y.mazo);
        return temp;
    }
}
