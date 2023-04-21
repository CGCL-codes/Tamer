package jflag.editors;

import java.awt.*;

public class MapObject {

    private String id;

    private int x;

    private int y;

    private int radius;

    MapObject(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        radius = getRadius(id);
    }

    public static int getRadius(String id) {
        if (id.compareTo("tree") == 0) return (50); else if (id.compareTo("wall") == 0) return (50); else return (0);
    }

    public String getId() {
        return (id);
    }

    public int getX() {
        return (x);
    }

    public int getY() {
        return (y);
    }

    public int getRadius() {
        return (radius);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean contains(int x, int y) {
        if (id.compareTo("tree") == 0) {
            int x1 = this.x + radius;
            int y1 = this.y + radius + (radius / 2);
            if (x >= this.x && x <= x1 && y >= this.y && y <= y1) return (true);
        } else if (id.compareTo("wall") == 0) {
            int x1 = this.x + radius;
            int y1 = this.y + radius;
            if (x > this.x && x < x1 && y > this.y && y < y1) return (true);
        }
        return (false);
    }

    public boolean canContain(int x, int y) {
        if (id.compareTo("tree") == 0) {
            int x1 = this.x + radius;
            int y1 = this.y + radius + (radius / 2);
            if (x >= this.x && x <= x1 && y >= this.y && y <= y1) return (true);
            int y2 = y + radius + (radius / 2);
            if (x >= this.x && x <= x1 && y2 >= this.y && y2 <= y1) return (true);
            int x2 = x + radius;
            if (x2 >= this.x && x2 <= x1 && y2 >= this.y && y2 <= y1) return (true);
            if (x2 >= this.x && x2 <= x1 && y >= this.y && y <= y1) return (true);
        } else if (id.compareTo("wall") == 0) {
            int x1 = this.x + radius;
            int y1 = this.y + radius;
            if (x > this.x && x < x1 && y > this.y && y < y1) return (true);
            int y2 = y + radius;
            if (x > this.x && x < x1 && y2 > this.y && y2 < y1) return (true);
            int x2 = x + radius;
            if (x2 > this.x && x2 < x1 && y2 > this.y && y2 < y1) return (true);
            if (x2 > this.x && x2 < x1 && y > this.y && y < y1) return (true);
        }
        return (false);
    }

    public void paint(Graphics2D g2d) {
        if (id.compareTo("tree") == 0) {
            Shape[] shapes = new Shape[2];
            Polygon tree = new Polygon();
            tree.addPoint(x + (radius / 2), y);
            tree.addPoint(x + radius, y + radius - (radius / 4));
            tree.addPoint(x, y + radius - (radius / 4));
            Rectangle trunk = new Rectangle(x - (radius / 8) + (radius / 2), y + radius - (radius / 4), (radius / 4), radius - (radius / 4));
            shapes[0] = tree;
            shapes[1] = trunk;
            g2d.setColor(new Color(128, 50, 15));
            g2d.fill(shapes[1]);
            g2d.setColor(Color.BLACK);
            g2d.draw(shapes[1]);
            g2d.setColor(Color.GREEN);
            g2d.fill(shapes[0]);
            g2d.setColor(Color.BLACK);
            g2d.draw(shapes[0]);
            g2d.drawRect(x, y, radius, radius + (radius / 2));
        } else if (id.compareTo("wall") == 0) {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(x, y, radius, radius);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, radius, radius);
        }
    }
}
