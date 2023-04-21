package com.rapidminer.gui.plotter;

import java.awt.Color;

/** Helper class for the plotter point positions and colors. 
 * 
 *  @author Ingo Mierswa
 */
public class PlotterPoint {

    private double x;

    private double y;

    private double color;

    private Color borderColor;

    public PlotterPoint(double x, double y, double color, Color borderColor) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.borderColor = borderColor;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getColor() {
        return color;
    }

    public Color getBorderColor() {
        return borderColor;
    }
}
