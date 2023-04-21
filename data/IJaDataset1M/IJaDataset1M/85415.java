package self.gee.editor;

import java.awt.*;
import java.awt.event.*;
import self.awt.event.*;
import self.gee.*;

public class EventManager implements IEditingListener {

    private IEditingTool currentTool;

    public final Point touchDown = new Point();

    public IDiagramLayer diagramLayer;

    public void mouseClicked(MouseEvent e) {
        if (currentTool != null) currentTool.mouseClicked(e);
    }

    public void mousePressed(MouseEvent e) {
        Object src = e.getSource();
        if (src instanceof Component) ((Component) src).requestFocus();
        touchDown.x = e.getX();
        touchDown.y = e.getY();
        if (currentTool != null) currentTool.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (currentTool != null) currentTool.mouseReleased(e);
    }

    public void mouseEntered(MouseEvent e) {
        if (currentTool != null) currentTool.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        if (currentTool != null) currentTool.mouseExited(e);
    }

    public void mouseDragged(MouseEvent e) {
        if (currentTool != null) currentTool.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
        if (currentTool != null) currentTool.mouseMoved(e);
    }

    public void keyTyped(KeyEvent e) {
        if (currentTool != null) currentTool.keyTyped(e);
    }

    public void keyPressed(KeyEvent e) {
        if (currentTool != null) currentTool.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        if (currentTool != null) currentTool.keyReleased(e);
    }

    public void setEditingTool(IEditingTool toolWithFocus) {
        currentTool = toolWithFocus;
    }
}
