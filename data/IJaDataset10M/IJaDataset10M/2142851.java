package jsattrak.utilities;

import jsattrak.gui.SatPropertyPanel;
import java.awt.Dimension;
import java.io.Serializable;

/**
 *
 * @author sgano
 */
public class SatPropertyPanelSave implements Serializable {

    private int xPos;

    private int yPos;

    private int width;

    private int height;

    private String name;

    /** Creates a new instance of SatPropertyPanelSave */
    public SatPropertyPanelSave(SatPropertyPanel panel, int x, int y, Dimension d) {
        xPos = x;
        yPos = y;
        width = d.width;
        height = d.height;
        name = panel.getSatName();
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
