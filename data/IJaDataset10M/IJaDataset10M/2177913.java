package Juego;

import Enlace.mySingleton;
import java.util.ArrayList;
import java.util.Random;

/**
 *carlos
 * @author dilaang
 */
public class Baraja {

    private ArrayList<Carta> cartas = new ArrayList<Carta>(52);

    public void se() {
        System.out.println(cartas.size());
    }

    Baraja() {
        String[] pintas = { "c", "d", "p", "t" };
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) cartas.add(new Carta(j, pintas[i]));
        }
    }

    private Byte obtenerRandom() {
        byte num = 0;
        Random ra = new Random();
        num = Byte.valueOf(String.valueOf(ra.nextInt() % cartas.size()));
        return (byte) Math.abs(num);
    }

    public Carta getCarta() {
        Byte num = obtenerRandom();
        Carta c = new Carta(cartas.get(num));
        cartas.remove((int) num);
        return c;
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}
