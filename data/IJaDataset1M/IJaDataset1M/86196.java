package org.parsel.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Tuple3d;

public class PolygonState implements IPolygonState {

    private static final long serialVersionUID = 8533879574269052233L;

    private Color color;

    private IPolygon current;

    private List<IPolygon> polygons;

    public PolygonState() {
        init();
    }

    public PolygonState(IPolygonState state) {
        setPolyState(state);
    }

    public void addPolygons(List<IPolygon> polys) {
        if (null == polys) {
            throw new IllegalArgumentException();
        }
        polygons.addAll(polys);
    }

    public void addVertex(Tuple3d vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException();
        }
        current.add(vertex);
    }

    public void addVertices(List<Tuple3d> tuples) {
        if (tuples == null) throw new IllegalArgumentException();
        current.addAll(tuples);
    }

    public void clear() {
        current = null;
        if (polygons != null) {
            polygons.clear();
            polygons = null;
        }
    }

    public Color getColor() {
        return color;
    }

    public int countPolygons() {
        return (polygons == null) ? 0 : polygons.size();
    }

    /**
     * Count the vertices in all the polygons
     */
    public int countVertices() {
        int v = 0;
        if (polygons != null) {
            Iterator<IPolygon> it = polygons.iterator();
            while (it.hasNext()) {
                v += it.next().size();
            }
        }
        return v;
    }

    public List<Tuple3d> getVertices() {
        return current.getVertices();
    }

    public List<IPolygon> getPolygons() {
        return polygons;
    }

    public IPolygonState getPolyState() {
        return new PolygonState(this);
    }

    public void init() {
        current = new Polygon(color);
        polygons = new ArrayList<IPolygon>();
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setPolygons(List<IPolygon> polygons) {
        if (polygons == null) {
            throw new IllegalArgumentException();
        }
        this.polygons = polygons;
    }

    public void setPolyState(IPolygonState state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }
        setColor(state.getColor());
        setPolygons(state.getPolygons());
        setVertices(state.getVertices());
    }

    public void setVertices(List<Tuple3d> tuples) {
        if (current == null) current = new Polygon(color);
        current.setVertices(tuples);
    }

    public void stopRecording() {
        if (current.size() >= 3) {
            current.setColor(color);
            polygons.add(current);
        }
        current = new Polygon(color);
    }

    public String toString() {
        return getClass().getName() + "@" + hashCode() + "\n{" + "\n\t color:" + color.toString() + ",\n\t vertices:" + countVertices() + ",\n\t polygons:" + countPolygons() + "\n}";
    }

    public Object clone() {
        Object state = this.getPolyState();
        return state;
    }
}
