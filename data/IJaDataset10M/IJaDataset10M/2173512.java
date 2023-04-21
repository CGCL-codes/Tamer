package tralesld.visual.tree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class SelectionAreaExtension extends TreeViewExtension {

    public void paintOnTreePanel(TreeViewPanel panel, Graphics2D canvas) {
        canvas.setColor(Color.RED);
        canvas.setStroke(new BasicStroke(1));
        int width = panel.t.getTotalTreeWidth();
        int height = panel.t.getTotalTreeHeight();
        for (int i = 0; i < width; i += 10) {
            for (int j = 0; j < height; j += 3) {
                int nodeID = panel.t.getNodeAtCoordinates(i, j, panel);
                if (nodeID != -1) {
                    canvas.drawLine(i, j, panel.t.treeNodes.get(nodeID).x, panel.t.treeNodes.get(nodeID).y);
                } else {
                    canvas.drawLine(i, j, i, j);
                }
            }
        }
    }
}
