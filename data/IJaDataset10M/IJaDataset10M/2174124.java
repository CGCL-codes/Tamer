package w3duniverse.poc1.world;

import java.awt.Color;
import java.awt.Dimension;
import w3duniverse.poc1.interfaces.I_Displayable;

public class World extends SimpleWorld implements I_Displayable {

    private Dimension dimension;

    private Color color;

    public Dimension getDimension() {
        return dimension;
    }

    public World() {
    }

    public World(String id, String name, String worldUrl, Dimension dimension) {
        setId(id);
        setName(name);
        setWorldUrl(worldUrl);
        setDimension(dimension);
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
