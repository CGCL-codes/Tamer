package tralesld.visual.tree;

import java.awt.event.*;
import java.util.List;

public class TreeViewMouseMoveListener extends TreeViewMouseListener implements MouseMotionListener {

    int movedNodeID = -1;

    public TreeViewMouseMoveListener(TreeViewPanel viewPanel) {
        super(viewPanel);
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        TreeViewNode candidateNode = viewPanel.t.treeNodes.get(viewPanel.t.rootID);
        while (candidateNode.y < y && candidateNode.children.size() > 0) {
            List<Integer> children = candidateNode.children;
            candidateNode = viewPanel.t.treeNodes.get(children.get(0));
            for (int i : children) {
                if (viewPanel.t.treeNodes.get(i).x < x) {
                    candidateNode = viewPanel.t.treeNodes.get(i);
                }
            }
        }
        movedNodeID = candidateNode.id;
        viewPanel.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        if (movedNodeID != -1) {
            TreeViewNode n = viewPanel.t.treeNodes.get(movedNodeID);
            n.x = e.getX();
            n.y = e.getY();
        }
        movedNodeID = -1;
        viewPanel.repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (movedNodeID != -1) {
            TreeViewNode n = viewPanel.t.treeNodes.get(movedNodeID);
            n.x = e.getX();
            n.y = e.getY();
        }
        viewPanel.repaint();
    }
}
