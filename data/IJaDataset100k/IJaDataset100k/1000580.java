package tiled.core;

import java.awt.Rectangle;
import java.util.Properties;

/**
 * @version $Id: MapUnit.java 767 2008-07-06 10:34:18Z bjorn $
 */
public class MapUnit {

    private Properties properties = new Properties();

    protected float x, y;

    protected Rectangle bounds = new Rectangle();

    protected boolean bVisible = true;

    protected String name = "Unit";

    protected String source;

    protected String type = "";

    protected int width, height;

    public MapUnit(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String s) {
        name = s;
    }

    public void setSource(String s) {
        source = s;
    }

    public void setType(String s) {
        type = s;
    }

    public void setWidth(int w) {
        width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void setProperties(Properties p) {
        properties = p;
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Properties getProperties() {
        return properties;
    }

    public String toString() {
        return type + " (" + x + "," + y + ")";
    }
}
