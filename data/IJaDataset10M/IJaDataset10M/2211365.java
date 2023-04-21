package org.cake.game;

import java.util.List;
import java.util.Stack;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import org.cake.game.font.TextFont;
import org.cake.game.font.iFont;
import org.cake.game.geom.*;

/**
 *
 * @author Aaron
 */
public class Graphics {

    public static final int DEFAULT_CIRCLE_SEGMENTS = 50;

    private static iFont defaultFont = null;

    public static iFont getDefaultFont() {
        if (defaultFont == null) {
            defaultFont = new TextFont("arial", 30);
        }
        return defaultFont;
    }

    private GL2 gl;

    private GLU glu;

    private Vector2 tempVector = new Vector2();

    private iFont font;

    private int circleSegments = DEFAULT_CIRCLE_SEGMENTS;

    private iTransform2 transform;

    public Graphics() {
    }

    void initGL() {
        if (transform != null) {
            gl.glPushMatrix();
            transform.glMultMatrix(gl);
        }
        gl.glShadeModel(GL2.GL_SMOOTH);
    }

    void init(GL2 gl, GLU glu) {
        if (font == null) {
            font = getDefaultFont();
        }
        this.gl = gl;
        this.glu = glu;
    }

    public GL2 getGL() {
        return gl;
    }

    public GLU getGLU() {
        return glu;
    }

    public Color getColor() {
        return Color.glGetCurrentColor(gl);
    }

    public void setColor(iColor c) {
        c.glBindColor(gl);
    }

    public iFont getFont() {
        return font;
    }

    public void setFont(iFont font) {
        this.font = font;
    }

    public int getCircleSegments() {
        return circleSegments;
    }

    public void setCircleSegments(int segments) {
        circleSegments = segments;
    }

    public void setTransform(iTransform2 transform) {
        resetTransform();
        if (transform != null && !transform.isIdentity()) {
            gl.glPushMatrix();
            this.transform = transform.asAffineTransform();
            transform.glMultMatrix(gl);
        }
    }

    public void resetTransform() {
        if (this.transform != null) {
            gl.glPopMatrix();
            this.transform = null;
        }
    }

    public void drawImage(Image img, float x, float y) {
        Color.glPushColor(gl);
        gl.glColor3f(1, 1, 1);
        img.draw(gl, x, y);
        Color.glPopColor(gl);
    }

    public void drawImage(Image img, float x, float y, float width, float height) {
        Color.glPushColor(gl);
        gl.glColor3f(1, 1, 1);
        img.draw(gl, x, y, width, height);
        Color.glPopColor(gl);
    }

    public void drawImage(Image img, float x, float y, iColor filter) {
        Color.glPushColor(gl);
        filter.glBindColor(gl);
        img.draw(gl, x, y);
        Color.glPopColor(gl);
    }

    public void drawImage(Image img, float x, float y, float width, float height, iColor filter) {
        Color.glPushColor(gl);
        filter.glBindColor(gl);
        img.draw(gl, x, y, width, height);
        Color.glPopColor(gl);
    }

    public void drawImage(Image img, Vector2 pos) {
        drawImage(img, pos.x, pos.y);
    }

    public void drawImage(Image img, Vector2 pos, Vector2 size) {
        drawImage(img, pos.x, pos.y, size.x, size.y);
    }

    public void drawImage(Image img, Vector2 pos, iColor filter) {
        drawImage(img, pos.x, pos.y, filter);
    }

    public void drawImage(Image img, Vector2 pos, Vector2 size, iColor filter) {
        drawImage(img, pos.x, pos.y, size.x, size.y, filter);
    }

    public void drawSprite(Sprite sprite) {
        Color.glPushColor(gl);
        gl.glColor3f(1, 1, 1);
        sprite.draw(gl);
        Color.glPopColor(gl);
    }

    public void drawSprite(Sprite sprite, iColor filter) {
        Color.glPushColor(gl);
        filter.glBindColor(gl);
        sprite.draw(gl);
        Color.glPopColor(gl);
    }

    public void draw(iShape shape) {
        shape.draw(gl);
    }

    public void fill(iShape shape) {
        shape.fill(gl);
    }

    public void draw(iShape shape, iGradient2 gradient) {
        Color.glPushColor(gl);
        shape.draw(gl, gradient);
        Color.glPopColor(gl);
    }

    public void fill(iShape shape, iGradient2 gradient) {
        Color.glPushColor(gl);
        shape.fill(gl, gradient);
        Color.glPopColor(gl);
    }

    public void drawRectangle(float x, float y, float width, float height) {
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y + height);
        gl.glVertex2f(x + width, y + height);
        gl.glVertex2f(x + width, y);
        gl.glEnd();
    }

    public void drawRectangle(Vector2 pos, Vector2 size) {
        drawRectangle(pos.x, pos.y, size.x, size.y);
    }

    public void drawRectangle(float x, float y, float width, float height, iGradient2 gradient) {
        Color.glPushColor(gl);
        gl.glBegin(GL2.GL_LINE_LOOP);
        tempVector.set(x, y);
        gradient.point(gl, tempVector);
        gl.glVertex2f(x, y);
        tempVector.set(x, y + height);
        gradient.point(gl, tempVector);
        gl.glVertex2f(x, tempVector.y);
        tempVector.set(x + width, y + height);
        gradient.point(gl, tempVector);
        gl.glVertex2f(tempVector.x, tempVector.y);
        tempVector.set(x + width, y);
        gradient.point(gl, tempVector);
        gl.glVertex2f(tempVector.x, y);
        gl.glEnd();
        Color.glPopColor(gl);
    }

    public void drawRectangle(Vector2 pos, Vector2 size, iGradient2 gradient) {
        drawRectangle(pos.x, pos.y, size.x, size.y, gradient);
    }

    public void fillRectangle(float x, float y, float width, float height) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y + height);
        gl.glVertex2f(x + width, y + height);
        gl.glVertex2f(x + width, y);
        gl.glEnd();
    }

    public void fillRectangle(Vector2 pos, Vector2 size) {
        fillRectangle(pos.x, pos.y, size.x, size.y);
    }

    public void fillRectangle(float x, float y, float width, float height, iGradient2 gradient) {
        Color.glPushColor(gl);
        gl.glBegin(GL2.GL_QUADS);
        tempVector.set(x, y);
        gradient.point(gl, tempVector);
        gl.glVertex2f(x, y);
        tempVector.set(x, y + height);
        gradient.point(gl, tempVector);
        gl.glVertex2f(x, tempVector.y);
        tempVector.set(x + width, y + height);
        gradient.point(gl, tempVector);
        gl.glVertex2f(tempVector.x, tempVector.y);
        tempVector.set(x + width, y);
        gradient.point(gl, tempVector);
        gl.glVertex2f(tempVector.x, y);
        gl.glEnd();
        Color.glPopColor(gl);
    }

    public void fillRectangle(Vector2 pos, Vector2 size, iGradient2 gradient) {
        fillRectangle(pos.x, pos.y, size.x, size.y, gradient);
    }

    public void drawCircle(float centerX, float centerY, float radius) {
        drawElipse(centerX, centerY, radius, radius);
    }

    public void drawCircle(float centerX, float centerY, float radius, iGradient2 gradient) {
        drawElipse(centerX, centerY, radius, radius, gradient);
    }

    public void drawCircle(Vector2 center, float radius) {
        drawCircle(center.x, center.y, radius);
    }

    public void drawCircle(Vector2 center, float radius, iGradient2 gradient) {
        drawElipse(center.x, center.y, radius, radius, gradient);
    }

    public void fillCircle(float centerX, float centerY, float radius) {
        fillElipse(centerX, centerY, radius, radius);
    }

    public void fillCircle(Vector2 center, float radius) {
        fillElipse(center.x, center.y, radius, radius);
    }

    public void fillCircle(float centerX, float centerY, float radius, iGradient2 gradient) {
        fillElipse(centerX, centerY, radius, radius, gradient);
    }

    public void fillCircle(Vector2 center, float radius, iGradient2 gradient) {
        fillElipse(center.x, center.y, radius, radius, gradient);
    }

    public void drawString(Object str, float x, float y) {
        font.draw(gl, str, x, y);
    }

    public void drawString(Object str, Vector2 pos) {
        font.draw(gl, str, pos.x, pos.y);
    }

    public void drawElipse(float centerX, float centerY, float radiusX, float radiusY) {
        float dTheta = GeomUtil.PI2 / circleSegments, theta = 0;
        gl.glBegin(GL2.GL_LINE_LOOP);
        for (int i = 0; i < circleSegments; i++, theta += dTheta) {
            gl.glVertex2f(centerX + (float) Math.cos(theta) * radiusX, centerY + (float) Math.sin(theta) * radiusY);
        }
        gl.glEnd();
    }

    public void drawElipse(float centerX, float centerY, float radiusX, float radiusY, iGradient2 gradient) {
        Color.glPushColor(gl);
        float dTheta = GeomUtil.PI2 / circleSegments, theta = 0;
        gl.glBegin(GL2.GL_LINE_LOOP);
        for (int i = 0; i < circleSegments; i++, theta += dTheta) {
            tempVector.set(centerX + (float) Math.cos(theta) * radiusX, centerY + (float) Math.sin(theta) * radiusY);
            gradient.point(gl, tempVector);
            gl.glVertex2f(tempVector.x, tempVector.y);
        }
        gl.glEnd();
        Color.glPopColor(gl);
    }

    public void fillElipse(float centerX, float centerY, float radiusX, float radiusY) {
        float dTheta = GeomUtil.PI2 / circleSegments, theta = 0;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(centerX, centerY);
        for (int i = 0; i < circleSegments; i++, theta += dTheta) {
            gl.glVertex2f(centerX + (float) Math.cos(theta) * radiusX, centerY + (float) Math.sin(theta) * radiusY);
        }
        gl.glVertex2f(centerX + radiusX, centerY);
        gl.glEnd();
    }

    public void drawElipse(Vector2 center, float radiusX, float radiusY) {
        drawElipse(center.x, center.y, radiusX, radiusY);
    }

    public void drawElipse(Vector2 center, float radiusX, float radiusY, iGradient2 gradient) {
        drawElipse(center.x, center.y, radiusX, radiusY, gradient);
    }

    public void fillElipse(Vector2 center, float radiusX, float radiusY) {
        fillElipse(center.x, center.y, radiusX, radiusY);
    }

    public void fillElipse(Vector2 center, float radiusX, float radiusY, iGradient2 gradient) {
        fillElipse(center.x, center.y, radiusX, radiusY, gradient);
    }

    public void fillElipse(float centerX, float centerY, float radiusX, float radiusY, iGradient2 gradient) {
        Color.glPushColor(gl);
        float dTheta = GeomUtil.PI2 / circleSegments, theta = 0;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        tempVector.set(centerX, centerY);
        gradient.point(gl, tempVector);
        gl.glVertex2f(centerX, centerY);
        for (int i = 0; i < circleSegments; i++, theta += dTheta) {
            tempVector.set(centerX + (float) Math.cos(theta) * radiusX, centerY + (float) Math.sin(theta) * radiusY);
            gradient.point(gl, tempVector);
            gl.glVertex2f(tempVector.x, tempVector.y);
        }
        tempVector.set(centerX + radiusX, centerY);
        gradient.point(gl, tempVector);
        gl.glVertex2f(centerX + radiusX, centerY);
        gl.glEnd();
        Color.glPopColor(gl);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2f(x1, y1);
        gl.glVertex2f(x2, y2);
        gl.glEnd();
    }

    public void drawLine(Vector2 p1, Vector2 p2) {
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2f(p1.x, p1.y);
        gl.glVertex2f(p2.x, p2.y);
        gl.glEnd();
    }

    public void drawLineStrip(List<Vector2> pts) {
        gl.glBegin(GL2.GL_LINE_STRIP);
        for (Vector2 pt : pts) {
            gl.glVertex2f(pt.x, pt.y);
        }
        gl.glEnd();
    }

    public void drawLineLoop(List<Vector2> pts) {
        gl.glBegin(GL2.GL_LINE_LOOP);
        for (Vector2 pt : pts) {
            gl.glVertex2f(pt.x, pt.y);
        }
        gl.glEnd();
    }
}
