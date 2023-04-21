package engine.turtle;

import java.awt.Color;

public class Turtle {

    private Color color = Color.black;

    private double x, y;

    private double angle = 0;

    private final Parser parent;

    private boolean penDown = true;

    public Turtle(Parser parent) {
        this.parent = parent;
        home();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void changeAngle(@SuppressWarnings("hiding") double angle) {
        this.angle += angle;
    }

    public boolean isPenDown() {
        return penDown;
    }

    public void setPenDown(boolean penDown) {
        this.penDown = penDown;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        parent.g2d.setColor(color);
    }

    public void move(double d) {
        if (penDown) {
            double oldX = x, oldY = y;
            moveUnstored(d);
            parent.g2d.setColor(color);
            parent.g2d.drawLine((int) Math.round(oldX), (int) Math.round(oldY), (int) Math.round(x), (int) Math.round(y));
            parent.update();
        } else moveUnstored(d);
    }

    private void moveUnstored(double d) {
        double rad = Math.toRadians(angle);
        x += Math.cos(rad) * d;
        y += Math.sin(rad) * d;
    }

    public void home() {
        x = parent.getWidth() / 2;
        y = parent.getHeight() / 2;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void circleRight(int a, int d) {
        double a2 = angle - 90;
        double x2 = x - d - d * Math.cos(Math.toRadians(-a2));
        double y2 = y - d - d * Math.sin(Math.toRadians(-a2));
        parent.g2d.drawArc((int) Math.round(x2), (int) Math.round(y2), d * 2, d * 2, (int) a2, a);
        x = x2 + d + d * Math.cos(Math.toRadians(angle + a - 90));
        y = y2 + d - d * Math.sin(Math.toRadians(angle + a - 90));
        angle += a;
    }

    public void circleLeft(int a, int d) {
        double a2 = (angle - 90);
        double x2 = x - d - d * Math.cos(Math.toRadians(-a2));
        double y2 = y - d - d * Math.sin(Math.toRadians(-a2));
        parent.g2d.drawArc((int) Math.round(x2), (int) Math.round(y2), d * 2, d * 2, (int) Math.round(a2), a);
        x = x2 + d + d * Math.cos(Math.toRadians(angle + a - 90));
        y = y2 + d - d * Math.sin(Math.toRadians(angle + a - 90));
        angle += a;
    }
}
