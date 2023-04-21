package com.bluebrim.base.shared.geom;

public class CoTrapets implements java.io.Serializable {

    public CoTrapetsPoint m_p1 = new CoTrapetsPoint();

    public CoTrapetsPoint m_p2 = new CoTrapetsPoint();

    public CoTrapetsPoint m_p3 = new CoTrapetsPoint();

    public CoTrapetsPoint m_p4 = new CoTrapetsPoint();

    public CoTrapets() {
        super();
    }

    public CoTrapets(float x1, float x2, float x3, float x4, float y1, float y2) {
        this();
        setTrapetsPoints(x1, x2, x3, x4, y1, y2);
    }

    public CoTrapets(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        setTrapetsPoints(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public CoTrapets(CoTrapetsPoint p1, CoTrapetsPoint p2, CoTrapetsPoint p3, CoTrapetsPoint p4) {
        this();
        setTrapetsPoints(p1, p2, p3, p4);
    }

    public float getX0(float y) {
        if (y < getYMin() || y > getYMax()) return 0;
        return m_p1.m_x + (m_p4.m_x - m_p1.m_x) * (y - m_p1.m_y) / (m_p4.m_y - m_p1.m_y);
    }

    public float getX1(float y) {
        if (y < getYMin() || y > getYMax()) return 0;
        return m_p2.m_x + (m_p3.m_x - m_p2.m_x) * (y - m_p2.m_y) / (m_p3.m_y - m_p2.m_y);
    }

    public float getYMax() {
        return m_p3.m_y;
    }

    public float getYMin() {
        return m_p1.m_y;
    }

    public boolean include(CoTrapets trapets) {
        boolean included = false;
        if (m_p3.equals(trapets.m_p2) && m_p4.equals(trapets.m_p1)) {
            if (((trapets.m_p4.m_x - m_p1.m_x) / (trapets.m_p4.m_y - m_p1.m_y) == (m_p4.m_x - m_p1.m_x) / (m_p4.m_y - m_p1.m_y)) && ((trapets.m_p3.m_x - m_p2.m_x) / (trapets.m_p3.m_y - m_p2.m_y) == (m_p3.m_x - m_p2.m_x) / (m_p3.m_y - m_p2.m_y))) {
                m_p3.setPoint(trapets.m_p3);
                m_p4.setPoint(trapets.m_p4);
                included = true;
            }
        }
        return included;
    }

    public boolean isEquivalentTo(CoTrapets t) {
        return m_p1.isEquivalentTo(t.m_p1) && m_p2.isEquivalentTo(t.m_p2) && m_p3.isEquivalentTo(t.m_p3) && m_p4.isEquivalentTo(t.m_p4);
    }

    public void setTrapetsPoints(float x1, float x2, float x3, float x4, float y1, float y2) {
        setTrapetsPoints(x1, y1, x2, y1, x3, y2, x4, y2);
    }

    public void setTrapetsPoints(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        m_p1.setPoint(x1, y1);
        m_p2.setPoint(x2, y2);
        m_p3.setPoint(x3, y3);
        m_p4.setPoint(x4, y4);
    }

    public void setTrapetsPoints(CoTrapetsPoint p1, CoTrapetsPoint p2, CoTrapetsPoint p3, CoTrapetsPoint p4) {
        m_p1.setPoint(p1);
        m_p2.setPoint(p2);
        m_p3.setPoint(p3);
        m_p4.setPoint(p4);
    }

    public String toString() {
        return "Trapetsen : " + m_p1 + "  " + m_p2 + "  " + m_p3 + "  " + m_p4;
    }

    public void translateBy(float dx, float dy) {
        m_p1.translateBy(dx, dy);
        m_p2.translateBy(dx, dy);
        m_p3.translateBy(dx, dy);
        m_p4.translateBy(dx, dy);
    }
}
