package logic;

import java.awt.Graphics;
import java.awt.Point;

public abstract class BaseGameObject {

    protected Point position;

    public BaseGameObject() {
        position = new Point();
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public abstract void initialize();

    public abstract void draw(Graphics g);

    public abstract void destroy();
}
