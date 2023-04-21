package gui;

import java.awt.event.*;
import javax.swing.JToggleButton;

public class NodeTool implements MouseListener, MouseMotionListener {

    public NodeTool() {
    }

    public void activateTool(NodePanel np) {
        np.addMouseListener(this);
        np.addMouseMotionListener(this);
    }

    public void deactivateTool(NodePanel np) {
        np.removeMouseListener(this);
        np.removeMouseMotionListener(this);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public static class ToggleButton extends JToggleButton implements ActionListener {

        NodePanel target;

        NodeTool tool;

        ToggleButton(NodeTool nt, NodePanel np) {
            target = np;
            tool = nt;
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            if (target.currentNodeTool != null) target.currentNodeTool.deactivateTool(target);
            tool.activateTool(target);
            target.currentNodeTool = tool;
        }
    }
}
