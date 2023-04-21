package forms;

import java.awt.*;
import javax.swing.*;
import java.lang.Math.*;

public class Ellipse extends Shape {

    public Ellipse() {
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void draw(forms.Point p1, forms.Point p2) {
        this.setInitPoints(p1, p2);
        pointsList.add(new forms.Point());
        pointsList.add(new forms.Point());
        pointsList.add(new forms.Point());
        pointsList.add(new forms.Point());
        this.pointsList.get(0).x = java.lang.Math.min(p2.x, p1.x);
        this.pointsList.get(0).y = java.lang.Math.min(p2.y, p1.y);
        this.pointsList.get(1).x = java.lang.Math.max(p2.x, p1.x);
        this.pointsList.get(1).y = java.lang.Math.min(p2.y, p1.y);
        this.pointsList.get(2).x = java.lang.Math.max(p2.x, p1.x);
        this.pointsList.get(2).y = java.lang.Math.max(p2.y, p1.y);
        this.pointsList.get(3).x = java.lang.Math.min(p2.x, p1.x);
        this.pointsList.get(3).y = java.lang.Math.max(p2.y, p1.y);
        this.centre.x = java.lang.Math.abs(this.pointsList.get(1).x - this.pointsList.get(0).x);
        this.centre.y = java.lang.Math.abs(this.pointsList.get(1).y - this.pointsList.get(0).y);
    }

    public void paint(Graphics g) {
        g.setColor(drawColor);
        g.drawOval(this.pointsList.get(0).x, this.pointsList.get(0).y, java.lang.Math.abs(this.pointsList.get(1).x - this.pointsList.get(0).x), java.lang.Math.abs(this.pointsList.get(3).y - this.pointsList.get(0).y));
    }

    public void paint(Graphics g, Color myColor) {
        g.setColor(myColor);
        g.drawOval(this.pointsList.get(0).x, this.pointsList.get(0).y, java.lang.Math.abs(this.pointsList.get(1).x - this.pointsList.get(0).x), java.lang.Math.abs(this.pointsList.get(3).y - this.pointsList.get(0).y));
    }
}
