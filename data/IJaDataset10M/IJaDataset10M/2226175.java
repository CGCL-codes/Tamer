package plotter;

import java.awt.*;

class PlotSettings {

    public boolean isparametric;

    public String eqn1, eqn2, eqn3;

    public double xmin, xmax, ymin, ymax;

    public boolean fillsurface, drawaxes;

    public Color bkcolor;

    public Color gridcolor;

    public Color surface1;

    public Color surface2;

    public PlotSettings() {
        isparametric = true;
        eqn1 = "sin(u)*cos(v)\n";
        eqn2 = "sin(u)*sin(v)\n";
        eqn3 = "cos(u)+sin(u)\n";
        xmin = 0;
        xmax = 3.14;
        ymin = -3.14;
        ymax = 3.14;
        fillsurface = drawaxes = true;
        bkcolor = Color.WHITE;
        gridcolor = Color.BLACK;
        surface1 = new Color(255, 255, 128);
        surface2 = new Color(0, 200, 0);
    }
}
