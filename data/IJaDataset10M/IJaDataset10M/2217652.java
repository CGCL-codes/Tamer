package loengud.kaheksas;

import java.awt.Point;

/**
 * Esinab aknale kuvatavat joont. Sisaldab endas
 * kahte {@link Point} t��pi muutujat joone otste
 * info hoidmiseks.
 * 
 * @see Point
 * @author A
 *
 */
public class Line {

    /**
	 * Joone otspunktid
	 */
    private Point from, to;

    /**
	 * Joone konstrueerimiseks objektiks n�uame
	 * joone alg- ja l�pp-punkti infot.
	 * 
	 * @param from Algpunkt
	 * @param to L�pppunkt
	 */
    public Line(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }
}
