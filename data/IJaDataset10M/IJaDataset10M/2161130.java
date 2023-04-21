package phworld;

import dsc.netgame.*;
import dsc.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Mine extends MiscComponent {

    private int power;

    private int radius;

    public Mine(int id, String name, int owner, Location location, int power) {
        super(id, name, owner, location);
        this.power = power;
        radius = calcRadius(power);
    }

    public static int calcRadius(int power) {
        if (power < 4) power = 4;
        return (int) Math.sqrt(((double) power) / Math.PI);
    }

    public void drawAdditionalPreGraphs(ScaledGraphics g) {
        Color pri = Color.red;
        pri = pri.darker().darker().darker().darker();
        g.setColor(pri);
        g.fillOval(getLocation().getX() - getRadius(), getLocation().getY() - getRadius(), getRadius() * 2, getRadius() * 2);
    }

    private int getRadius() {
        return radius;
    }

    public void drawAdditionalGraphs(ScaledGraphics g) {
    }

    public String getMapImage() {
        return "data/graph/still/components/misc/mine.gif";
    }

    public String getPanelImageName() {
        return "data/graph/still/panel/mine.gif";
    }
}
