package org.easyway.geometry2D;

import java.util.ArrayList;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.system.StaticRef;
import org.lwjgl.opengl.GL11;

class LineDrawer extends BaseObject implements IDrawing, ILayerID {

    private static final long serialVersionUID = -8792265397633463907L;

    private int layer;

    int idLayer = -1;

    ArrayList<DrawableLine> lines = new ArrayList<DrawableLine>();

    public static int red = 255, green = 255, blue = 255, alpha = 255;

    @Deprecated
    public static void drawLine(float x, float y, float x2, float y2) {
        GL11.glColor4b((byte) red, (byte) green, (byte) blue, (byte) alpha);
        GL11.glBegin(GL11.GL_LINE);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    public void render() {
        DrawableLine temp;
        for (int i = 0; i < lines.size(); ++i) {
            temp = lines.get(i);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glColor4b(temp.red, temp.green, temp.blue, (byte) 255);
            GL11.glVertex2f(temp.xstart, temp.ystart);
            GL11.glVertex2f(temp.xend, temp.yend);
            GL11.glEnd();
        }
    }

    public void addLine(float x, float y, float x2, float y2, int r, int g, int b, String name) {
        lines.add(new DrawableLine(x, y, x2, y2, (byte) r, (byte) g, (byte) b, name));
    }

    public void removeLine(String name) {
        for (int i = 0; i < lines.size(); ++i) {
            if (lines.get(i).name.equals(name)) {
                lines.remove(lines.get(i));
                --i;
            }
        }
    }

    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    public int getLayer() {
        return layer;
    }

    public int getIdLayer() {
        return idLayer;
    }

    public void setIdLayer(int id) {
        if (idLayer != -1) {
            StaticRef.layers[idLayer].remove(this);
        }
        if (id < 0) {
            id = 0;
        } else if (id > StaticRef.layers.length) {
            id = StaticRef.layers.length;
        }
        idLayer = id;
        StaticRef.layers[idLayer].add(this);
    }

    class DrawableLine {

        float xstart, ystart, xend, yend;

        byte red, green, blue;

        String name;

        public DrawableLine(float x, float y, float x2, float y2, byte r, byte g, byte b, String name) {
            xstart = x;
            ystart = y;
            xend = x2;
            yend = y2;
            red = (byte) r;
            green = (byte) g;
            blue = (byte) b;
            this.name = name;
        }

        public boolean equals(Object o) {
            if (o instanceof DrawableLine) {
                if (((DrawableLine) o).name.equals(name)) {
                    return true;
                }
                return false;
            }
            return o.equals(this);
        }
    }
}
