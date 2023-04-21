package org.cresques.px.gml;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Vector;
import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;
import org.cresques.px.PxObj;

/**
 * Clase base para geometr�as.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public abstract class Geometry extends PxObj implements Projected {

    protected IProjection proj;

    Vector data = null;

    /**
     * Constructor de Geometry.
     */
    public Geometry() {
        extent = new Extent();
        data = new Vector();
    }

    /**
     * Permite a�adir un punto a la Geometry.
     * @param pt
     */
    public void add(Point2D pt) {
        extent.add(pt);
        data.add(pt);
    }

    /**
     * Devuelve un punto de la Geometry dado por su �ndice.
     * @param i, �ndice.
     * @return Point2D.
     */
    public Point2D get(int i) {
        return (Point2D) data.get(i);
    }

    /**
     * Devuelve el n�mero de puntos que componen la Geometry.
     * @return int
     */
    public int pointNr() {
        return data.size();
    }

    /**
     * Devuelve el conjunto de objetos que conforman la Geometry en forma de Vector.
     * @return Vector de objetos.
     */
    public Vector getData() {
        return data;
    }

    /**
     * Devuelve el extent de la Geometry.
     * @return Extent, el rect�ngulo que contiene la Geometry.
     */
    public Extent getExtent() {
        return extent;
    }

    /**
     * Devuelve la proyecci�n cartogr�fica en la que se encuentra la Geometry.
     * @return IProjection, la proyecci�n cartogr�fica.
     */
    public abstract IProjection getProjection();

    /**
     * Permite reproyectar la Geometry en funci�n de unas coordenadas de transformaci�n.
     * @param rp, Coordenadas de transformaci�n.
     */
    public abstract void reProject(ICoordTrans rp);

    /**
     * Permite dibujar la Geometry.
     * @param g, Graphics2D.
     * @param vp, ViewPortData.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
    }
}
