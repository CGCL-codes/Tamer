package game.visualizations;

import game.gui.GraphCanvas;
import java.awt.*;

public class Clasification3dm extends game.visualizations.graph.Graph implements Graph {

    private transient GraphCanvas gc;

    private transient Classification3D cut;

    public void init(GraphCanvas gic) {
        gc = gic;
        cut = new Classification3D(gc);
    }

    public void takeData() {
        cut.takeData();
    }

    public void freeMemory() {
        cut = null;
        System.gc();
    }

    public void paint(Graphics2D g) {
    }

    public GConfig getGraphConfigClass() {
        return (GConfig) cut.getConfigurationPanel();
    }

    public Class getConfigClass() {
        return null;
    }

    public String getMenuLabel() {
        return "Clasification 3D";
    }

    public int getType() {
        return GraphCanvas.API3D;
    }

    public void setConfigClass(GConfig cfg) {
    }
}
