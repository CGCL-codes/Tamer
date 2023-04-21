package world.resource;

import graphics.Camera;
import world.unit.Unit;
import utilities.Location;
import java.awt.Graphics2D;
import java.awt.Color;
import world.resource.spawnFormat.SpawnFormat;
import world.World;

/**
 * A game resource that can be harvested by a player. All resources
 * must be returned to a refinery to be converted into mass before
 * use in building more units
 */
public abstract class Resource extends Unit {

    Color c;

    boolean visible = true;

    SpawnFormat sf;

    public Resource(Location location, String name, int maxLife, Color c, int size, int harvestMass) {
        super(null, location, name, maxLife, 0, 0, 0, 0, size);
        this.c = c;
        harvestable = true;
        this.harvestMass = harvestMass;
    }

    public void updateSpawnCounter(World w) {
        if (sf != null) {
            sf.updateCounter(w);
        }
    }

    public void setSpawnFormat(SpawnFormat sf) {
        this.sf = sf;
    }

    public void setVisible(boolean setter) {
        visible = setter;
    }

    public void drawElement(Graphics2D g, Camera c) {
        Location location = getLocation();
        int size = getWidth();
        g.setColor(this.c);
        int x = c.getScreenLocation(location).x;
        int y = c.getScreenLocation(location).y;
        g.fillOval(x - size / 2, y - size / 2, size, size);
        g.setColor(Color.black);
        g.drawOval(x - size / 2, y - size / 2, size, size);
    }
}
