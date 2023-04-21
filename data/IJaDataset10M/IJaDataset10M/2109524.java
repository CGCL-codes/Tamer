package game.gui.sdraw;

import javax.media.opengl.GL;

/**
 * User: honza
 * Date: Aug 27, 2006
 * Time: 11:04:52 PM
 */
class GOBLine extends GOB {

    private double x1;

    private double y1;

    private double x2;

    private double y2;

    public GOBLine(double ox1, double oy1, double ox2, double oy2) {
        x1 = ox1;
        y1 = oy1;
        x2 = ox2;
        y2 = oy2;
    }

    public void draw(GL ogl) {
        super.draw(ogl);
        ogl.glBegin(GL.GL_LINES);
        ogl.glVertex2d(x1, y1);
        ogl.glVertex2d(x2, y2);
        ogl.glEnd();
    }
}
