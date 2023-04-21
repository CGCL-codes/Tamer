package ru.ifmo.fcdesigner.gui.tools;

import ru.ifmo.fcdesigner.flowchart.view.FlowchartElementView;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.*;

/**
 * Description.
 *
 * @author Dmitry Paraschenko
 */
public abstract class AbstractTool implements MouseListener, MouseMotionListener {

    private final FlowchartToolManager flowchartToolManager;

    protected AbstractTool(FlowchartToolManager flowchartToolManager) {
        this.flowchartToolManager = flowchartToolManager;
    }

    public FlowchartToolManager getFlowchartToolManager() {
        return flowchartToolManager;
    }

    public FlowchartElementView getFlowchart() {
        return flowchartToolManager.getFlowchart();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void paint(Graphics2D g) {
    }
}
