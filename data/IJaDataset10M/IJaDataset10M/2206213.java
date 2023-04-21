package ch.hslu.ta.prg2.f11.group6.yatzy;

import ch.hslu.ta.prg2.f11.group6.yatzy.control.Yatzy;

/**
 * The landing class into this yatzy game.
 * 
 * @author Claudio Spizzi <claudio.spizzi@stud.hslu.ch>
 */
public class Main {

    /**
     * The main method for jump into the game.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LookAndFeel.setLookAndFeel(LookAndFeel.JAVA);
        Yatzy.getInstance();
    }
}
