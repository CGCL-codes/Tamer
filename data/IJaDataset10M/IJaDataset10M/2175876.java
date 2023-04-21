package gui;

import data.HyperTextElement;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class CreateHyperTextElementTool extends NodeTool {

    public void mouseClicked(MouseEvent e) {
        NodePanel np = (NodePanel) e.getSource();
        HyperTextElement hte = new HyperTextElement();
        hte.setPosition(new Point(e.getX(), e.getY() - 10));
        NodeElementUI neui = np.addElementToNode(hte);
        neui.activate();
        np.setActiveElementUI(neui);
        ((HyperFrame) np.getTopLevelAncestor()).updateAllControls();
    }

    public void activateTool(NodePanel np) {
        super.activateTool(np);
        np.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    public void deactivateTool(NodePanel np) {
        super.deactivateTool(np);
        np.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
