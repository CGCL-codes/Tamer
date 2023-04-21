package gj.ui;

import gj.layout.Graph2D;
import java.awt.Graphics2D;

/**
 * A renderer for graphs
 */
public interface GraphRenderer {

    public void render(Graph2D layout, Graphics2D graphics);
}
