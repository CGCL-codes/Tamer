package jf.exam.paint.graphics.obj;

import java.awt.Graphics;
import jf.exam.paint.util.Config;

/**
 * @version 	1.0
 * @author
 */
public class Clear extends GraphicsObject {

    public void draw(Graphics g) {
        getRootNode().getEvent().setClear();
        g.setColor(java.awt.Color.white);
        g.fillRect(0, 0, Config.CanvasW, Config.CanvasH);
    }

    public Point getEndP() {
        return null;
    }

    public int getMaxNumOfDraw() {
        return 0;
    }

    public Point getMiddleP() {
        return null;
    }

    public int getNumOfDraw() {
        return 0;
    }

    public Point getStartP() {
        return null;
    }

    public void setEndP(Point p) {
    }

    public void setMiddleP(Point p) {
    }

    public void setNumOfDraw(int num) {
    }

    public void setStartP(Point p) {
    }

    public String getRegisterName() {
        return "clear";
    }
}
