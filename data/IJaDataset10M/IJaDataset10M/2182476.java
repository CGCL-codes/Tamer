package co.edu.unal.ungrid.common;

import javax.media.opengl.GL;
import co.edu.unal.ungrid.client.util.Point2D;

public class Quad2D {

    public Quad2D(final Point2D[] vertex) {
        setVertex(vertex);
    }

    public Point2D getVertex(int idx) {
        assert 0 <= idx && idx < NUM_VERTEX;
        return m_vertex[idx];
    }

    public void setVertex(final Point2D[] vertex) {
        assert vertex != null;
        assert vertex.length >= NUM_VERTEX;
        m_vertex = vertex;
    }

    public void setVertex(int idx, final Point2D vertex) {
        assert 0 <= idx && idx < NUM_VERTEX;
        m_vertex[idx] = vertex;
    }

    public void setX(double x) {
        for (int i = 0; i < NUM_VERTEX; i++) {
            m_vertex[i].x = x;
        }
    }

    public double getX() {
        return m_vertex[0].x;
    }

    public void setY(double y) {
        for (int i = 0; i < NUM_VERTEX; i++) {
            m_vertex[i].y = y;
        }
    }

    public double getY() {
        return m_vertex[0].y;
    }

    public int getName() {
        return m_name;
    }

    public void setName(int name) {
        m_name = name;
    }

    public void display(GL gl) {
        gl.glBegin(GL.GL_LINE_STRIP);
        gl.glVertex2d(m_vertex[0].x, m_vertex[0].y);
        gl.glVertex2d(m_vertex[1].x, m_vertex[1].y);
        gl.glVertex2d(m_vertex[2].x, m_vertex[2].y);
        gl.glVertex2d(m_vertex[3].x, m_vertex[3].y);
        gl.glVertex2d(m_vertex[0].x, m_vertex[0].y);
        gl.glEnd();
    }

    private Point2D[] m_vertex;

    private int m_name;

    public static final int NUM_VERTEX = 4;
}
