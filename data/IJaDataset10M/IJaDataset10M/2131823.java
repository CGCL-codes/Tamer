package net.jlnx.Uebung4_1.start;

import net.jlnx.Uebung4_1.data.Ellipse;
import net.jlnx.Uebung4_1.data.Figur;
import net.jlnx.Uebung4_1.data.Kreis;
import net.jlnx.Uebung4_1.data.Quadrat;
import net.jlnx.Uebung4_1.data.RWDreieck;
import net.jlnx.Uebung4_1.data.Rechteck;
import net.jlnx.Uebung4_1.util.FigurenHelper;

public class FigurStarter extends net.jlnx.Uebung1_2.start.FigurStarter {

    public static void main(String[] args) {
        new Rechteck(5, 5, -10, 10, 20);
        new RWDreieck(30, 2, 80, 3, 4);
        new Ellipse(4, 10, 31, 10, 20);
        new Kreis(10, 10, 31, 5);
        new Quadrat(100, 200, 13, 35);
        new Rechteck(5, 5, 7, 10, 20);
        new RWDreieck(30, 2, -11, 3, 4);
        new Ellipse(4, 10, -17, 10, 20);
        new Kreis(10, 10, 50, 5);
        new Quadrat(100, 200, 30, 35);
        new Rechteck(5, 5, 25, 10, 20);
        new RWDreieck(30, 2, 7, 3, 4);
        new Ellipse(4, 10, 4, 10, 20);
        new Kreis(10, 10, 10, 5);
        new Quadrat(100, 200, 5, 35);
        FigurenHelper.getGesamtFlaecheZIndex(0, 50, Figur.getZSortierteFiguren());
    }
}
