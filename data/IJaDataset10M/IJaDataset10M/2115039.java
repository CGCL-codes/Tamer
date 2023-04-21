package net.jlnx.Uebung1.data;

import java.util.Vector;

public class Figur {

    protected int X;

    protected int Y;

    private static Vector<Figur> figuren = new Vector<Figur>();

    public Figur(int posX, int posY) {
        this.X = posX;
        this.Y = posY;
        Figur.figuren.add(this);
    }

    public static final int anzFiguren() {
        return figuren.size();
    }

    public static Vector<Figur> getAlleFiguren() {
        return Figur.figuren;
    }
}
