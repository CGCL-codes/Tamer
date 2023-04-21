package fr.ign.cogit.geoxygene.contrib.geometrie;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;

/**
 * Rectangle.
 * 
 * NB: utiliser autant que possible directement la classe GM_Envelope qui
 * représente aussi des rectangles. Cette classe n'est là que pour optimiser
 * quelques méthodes, et est essentiellement utilisée pour l'index de dallage.
 * 
 * @author Mustière
 * @version 1.0
 */
public class Rectangle {

    public double xmin;

    public double xmax;

    public double ymin;

    public double ymax;

    public Rectangle() {
    }

    public static Rectangle rectangleEnglobant(ILineString L) {
        IDirectPositionList listepoints = L.coord();
        IDirectPosition point;
        Rectangle R = new Rectangle();
        R.xmin = listepoints.get(0).getX();
        R.xmax = listepoints.get(0).getX();
        R.ymin = listepoints.get(0).getY();
        R.ymax = listepoints.get(0).getY();
        for (int i = 1; i < listepoints.size(); i++) {
            point = listepoints.get(i);
            if (point.getX() < R.xmin) {
                R.xmin = point.getX();
            }
            if (point.getX() > R.xmax) {
                R.xmax = point.getX();
            }
            if (point.getY() < R.ymin) {
                R.ymin = point.getY();
            }
            if (point.getY() > R.ymax) {
                R.ymax = point.getY();
            }
        }
        return R;
    }

    public Rectangle dilate(double dilatation) {
        Rectangle R = new Rectangle();
        R.xmin = this.xmin - dilatation;
        R.xmax = this.xmax + dilatation;
        R.ymin = this.ymin - dilatation;
        R.ymax = this.ymax + dilatation;
        return R;
    }

    public boolean intersecte(Rectangle R) {
        boolean intersecteX = false;
        boolean intersecteY = false;
        if (R.xmin < this.xmin && R.xmax > this.xmin) {
            intersecteX = true;
        }
        if (R.xmin > this.xmin && R.xmin < this.xmax) {
            intersecteX = true;
        }
        if (R.ymin < this.ymin && R.ymax > this.ymin) {
            intersecteY = true;
        }
        if (R.ymin > this.ymin && R.ymin < this.ymax) {
            intersecteY = true;
        }
        return (intersecteX && intersecteY);
    }
}
