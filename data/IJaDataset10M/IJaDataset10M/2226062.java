package java.graphics;

import java.util.*;

public class Polyline {

    Vector pList = new Vector();

    float mY;

    public Polyline() {
    }

    public Polyline(Polyline pl) {
        for (int i = 0; i < pl.size(); i++) add(pl.pointAt(i));
        mY = pl.mY;
    }

    public String toString() {
        String s = pList.size() + " ";
        for (int i = 0; i < size(); i++) s += (pointAt(i).x + " " + pointAt(i).y + " " + pointAt(i).z + " ");
        s += "  ";
        return s;
    }

    public void add(Point p) {
        pList.addElement(new Point(p));
    }

    public void add(float x, float y, float z) {
        pList.addElement(new Point(x, y, z));
    }

    public void add(float x, float y) {
        add(x, y, 0);
    }

    public Point pointAt(int i) {
        return (Point) pList.elementAt(i);
    }

    public int size() {
        return pList.size();
    }

    public int whichHasMinY() {
        int which = -1;
        mY = 10000000;
        for (int i = 0; i < size(); i++) if (pointAt(i).getY() < mY) {
            mY = pointAt(i).getY();
            which = i;
        }
        if (which == -1) System.out.println("hier stimmt was nicht");
        return which;
    }

    public float whichIsMinY() {
        whichHasMinY();
        return mY;
    }
}
