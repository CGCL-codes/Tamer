package de.syfy.project.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Eigentlich nur ein Versuch... mit Listen und so :D
 * 
 * Soll eine Sammlung von Wuerfeln sein.
 * Auf diese Sammlung sollen dann diese Methoden 
 * im gesammten ausgeführt werden.
 */
public class DiceBag implements Diceable {

    List<Dice> bag = new ArrayList<Dice>();

    public DiceBag() {
    }

    /**
     * 
     * Jeder Wuerfel im Beutel wird einmal gewuerfelt... 
     * und die Ergebnisse addiert.
     * oder lieber fuer jeden Euerfel eine eigene Ausgabe?
     * 
     */
    @Override
    public int roll() {
        int erg = 0;
        for (Dice d : bag) {
            erg += d.roll();
        }
        return erg;
    }

    /**
     * 
     * Mehrfachwuerfe auf jedes Element im Beutel
     * 
     * 
     */
    @Override
    public int multiRoll(int times) {
        int erg = 0;
        for (Dice d : bag) {
            erg += d.multiRoll(times);
        }
        return erg;
    }

    public void addDice(int sides, int n) {
        int i;
        for (i = 0; i < n; i++) {
            bag.add(new Dice(sides));
        }
    }
}
