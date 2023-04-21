package world.destructable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import graphics.Camera;
import utilities.Location;
import world.World;

public class Unit extends Destructable implements Serializable {

    private static final long serialVersionUID = 1L;

    public Unit(String name, Location l, int width, int height) {
        super(name, l, width, height);
    }

    public void destroy(World w) {
    }

    public void drawElementLG(Graphics2D g) {
        g.setColor(Color.blue);
        g.fillOval((int) l.x - width / 2, (int) l.y - height / 2, width, height);
    }
}
