package org.matsim.utils.vis.otfivs.opengl.gl;

import static javax.media.opengl.GL.GL_MODELVIEW_MATRIX;
import static javax.media.opengl.GL.GL_QUADS;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import org.matsim.utils.vis.otfivs.opengl.gl.InfoText;
import com.sun.opengl.util.j2d.TextRenderer;

public class InfoText {

    private static LinkedList<InfoText> elements = new LinkedList<InfoText>();

    private static Set<InfoText> elementsPermanent = new HashSet<InfoText>();

    private static TextRenderer renderer = null;

    String line = null;

    public float x = 30, y = 10, z = -1, size = 1.0f, fill = 0.0f;

    boolean isValid = true;

    public boolean draw2D = false;

    public Color color = new Color(50, 50, 128, 200);

    public boolean isValid() {
        return isValid;
    }

    ;

    public InfoText(String line) {
        this.line = line;
    }

    public InfoText(String text, float x2, float y2, float z2) {
        this(text);
        x = x2;
        y = y2;
        z = z2;
        size = -0.5f / z;
    }

    public InfoText(String text, float x2, float y2, float z2, float size2) {
        this(text, x2, y2, z2);
        this.size = size2;
    }

    public Rectangle2D getBounds() {
        return renderer.getBounds(line);
    }

    public void draw3D(GL gl) {
        float size = this.size;
        float border = 15.f;
        if (size < 0) {
            double[] modelview = new double[16];
            gl.glGetDoublev(GL_MODELVIEW_MATRIX, modelview, 0);
            size *= modelview[14];
        }
        GLU glu = new GLU();
        Rectangle2D rect = renderer.getBounds(line);
        float halfh = (float) rect.getHeight() / 2;
        gl.glPushMatrix();
        gl.glEnable(GL.GL_BLEND);
        gl.glColor4f(0.9f, 0.9f, 0.9f, (color.getAlpha() / 255.f));
        gl.glTranslatef(x, y, z);
        gl.glScalef(size, size, 1);
        gl.glTranslatef(-border, halfh, 0);
        GLUquadric quad1 = glu.gluNewQuadric();
        glu.gluPartialDisk(quad1, 0, halfh, 12, 2, 180, 180);
        glu.gluDeleteQuadric(quad1);
        gl.glBegin(GL_QUADS);
        gl.glVertex3d(0, -halfh, 0);
        gl.glVertex3d(0, halfh, 0);
        gl.glVertex3d(rect.getWidth() + 2 * border, halfh, 0);
        gl.glVertex3d(rect.getWidth() + 2 * border, -halfh, 0);
        gl.glEnd();
        if (fill > 0.0f) {
            gl.glColor4f(0.9f, 0.7f, 0.7f, (color.getAlpha() / 255.f));
            gl.glBegin(GL_QUADS);
            gl.glVertex3d(0, -halfh, 0);
            gl.glVertex3d(0, -halfh - 7, 0);
            gl.glVertex3d(rect.getWidth() + 2 * border, -halfh - 7, 0);
            gl.glVertex3d(rect.getWidth() + 2 * border, -halfh, 0);
            gl.glEnd();
            gl.glColor4f(0.9f, 0.5f, 0.5f, 0.9f);
            gl.glBegin(GL_QUADS);
            gl.glVertex3d(0, -halfh, 0);
            gl.glVertex3d(0, -halfh - 7, 0);
            gl.glVertex3d(rect.getWidth() * fill + 2 * border, -halfh - 7, 0);
            gl.glVertex3d(rect.getWidth() * fill + 2 * border, -halfh, 0);
            gl.glEnd();
            gl.glColor4f(0.9f, 0.9f, 0.9f, (color.getAlpha() / 255.f));
        }
        GLUquadric quad2 = glu.gluNewQuadric();
        gl.glTranslatef(2 * border + (float) rect.getWidth(), 0, 0);
        glu.gluPartialDisk(quad2, 0, halfh, 12, 2, 0, 180);
        glu.gluDeleteQuadric(quad2);
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
        renderer.begin3DRendering();
        renderer.setColor(color);
        renderer.draw3D(line, x, y + 0.25f * halfh * size, z, size);
        renderer.end3DRendering();
    }

    void draw(GLAutoDrawable drawable) {
        if (draw2D) {
            renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
            renderer.setColor(0.2f, 0.2f, 0.5f, color.getAlpha() / 255.f);
            renderer.draw(line, (int) x, (int) y);
            renderer.endRendering();
        } else {
            draw3D(drawable.getGL());
        }
        if (color.getAlpha() <= 0) isValid = false;
    }

    public static void showText(String text) {
        elements.add(new InfoText(text));
    }

    public static void showText(String text, float x, float y, float z) {
        elements.addFirst(new InfoText(text, x, y, z));
    }

    public static InfoText showTextPermanent(String text, float x, float y, float size) {
        InfoText tt = null;
        tt = new InfoText(text, x, y, 0, size);
        elementsPermanent.add(tt);
        return tt;
    }

    public static void removeTextPermanent(InfoText text) {
        elementsPermanent.remove(text);
    }

    public static void drawInfoTexts(GLAutoDrawable drawable) {
        for (InfoText text : elements) {
            text.y += 1;
            text.setAlpha(text.getAlpha() - 0.02f);
            if (renderer != null) text.draw(drawable);
        }
        for (InfoText text : elementsPermanent) {
            if (renderer != null) text.draw(drawable);
        }
        ListIterator<InfoText> iter = elements.listIterator();
        while (iter.hasNext()) {
            if (!iter.next().isValid()) iter.remove();
        }
    }

    public static void setRenderer(TextRenderer renderer) {
        InfoText.renderer = renderer;
    }

    public float getAlpha() {
        return color.getAlpha() / 255.f;
    }

    public void setAlpha(float alpha) {
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
    }
}
