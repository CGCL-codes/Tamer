package infinitewisdom.view.util;

import infinitewisdom.model.util.Vec2f;
import infinitewisdom.view.GameWorldView;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Rectangle {

    public Vec2f m0, m1;

    public Point v0, v1;

    public boolean modelUptoDate = false, viewUptoDate = false;

    public Rectangle() {
    }

    public Rectangle(Vec2f m0, Vec2f m1) {
        this.m0 = m0;
        this.m1 = m1;
        modelUptoDate = true;
    }

    public Rectangle(Vec2f center, double radius) {
        m0 = center.clone();
        m0.x = m0.x - radius;
        m0.y = m0.y - radius;
        m1 = center.clone();
        m1.x = m1.x + radius;
        m1.y = m1.y + radius;
        modelUptoDate = true;
    }

    public void setModel(Vec2f m0, Vec2f m1) {
        this.m0 = m0;
        this.m1 = m1;
        modelUptoDate = true;
        viewUptoDate = false;
    }

    public void setView(Point v0, Point v1) {
        this.v0 = v0;
        this.v1 = v1;
        viewUptoDate = true;
        modelUptoDate = false;
    }

    public void computeView(GameWorldView gameWorldView) {
        v0 = gameWorldView.WorldToUICoord(m0);
        v1 = gameWorldView.WorldToUICoord(m1);
        viewUptoDate = true;
    }

    public void computeModel(GameWorldView gameWorldView) {
        m0 = gameWorldView.UIToWorldCoord(v0);
        m1 = gameWorldView.UIToWorldCoord(v1);
        modelUptoDate = true;
    }

    public void draw(Graphics g) {
        g.drawRect(v0.x, v0.y, v1.x - v0.x, v1.y - v0.y);
    }
}
