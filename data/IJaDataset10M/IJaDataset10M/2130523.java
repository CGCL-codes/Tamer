package geometry.objects.editors;

import geometry.base.SelectableEntity;
import java.awt.Point;

/**
 * Hier handelt es sich um einen ganz speziellen Typ von Resizer,
 * da er nur fuer die Linienfeldobjekte verfuegbar ist
 * 
 * @author Etzlstorfer Andreas
 *
 */
public class LineMover extends Resizer {

    public static final long serialVersionUID = 1;

    /**
     * Punkt den der Resizer vertritt
     */
    private Point point;

    /**
	 * @param x x-Weite
	 * @param y y-Weite
	 * @param ent Entity
	 * @param t Typ
	 * @param point Überwachungspunkt
	 */
    public LineMover(int x, int y, SelectableEntity ent, ResizerTypen t, Point point) {
        super(x, y, ent, t);
        this.point = point;
    }

    /**
     * @param xy xy-Weite
     * @param ent Entity
     * @param t Typ
     * @param point Überwachungspunkt
     */
    public LineMover(Point xy, SelectableEntity ent, ResizerTypen t, Point point) {
        super(xy, ent, t);
        this.point = point;
    }

    public void setX(int x) {
        super.setX(x);
        point.x = x;
    }

    public void setXY(int x, int y) {
        super.setXY(x, y);
        point.x = x;
        point.y = y;
    }

    public void setY(int y) {
        super.setY(y);
        point.y = y;
    }
}
