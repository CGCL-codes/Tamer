package asd;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
   Instances represent edges in an ASD grammar for
   the graphical grammar editor.  Access is package access.
   @author James A. Mason
   @version 1.03 2001 Oct 25, 30; Nov 1-2, 6-7, 9; Dec 31; 2003 Jul
 */
class ASDEditEdge extends JRadioButton implements MouseMotionListener, ChangeListener {

    ASDEditEdge(Container given) {
        context = given;
        addMouseMotionListener((MouseMotionListener) this);
        addChangeListener((ChangeListener) this);
        addMouseListener(new PopupListener(new EditEdgeMenu(this)));
        setOpaque(false);
    }

    ASDEditEdge(Container given, int xPos, int yPos) {
        this(given);
        setLocation(xPos, yPos);
        setSize(SIZE, SIZE);
    }

    Container getContext() {
        return context;
    }

    ASDEditor getEditor() {
        return editor;
    }

    ASDEditNode getFromNode() {
        return ((ASDDigraphNode) getDigraphEdge().getFromNode()).getEditNode();
    }

    ASDEditNode getToNode() {
        return ((ASDDigraphNode) getDigraphEdge().getToNode()).getEditNode();
    }

    public void mouseDragged(MouseEvent e) {
        context.remove(this);
        context.add(this, 0);
        int newX = getX() + e.getX();
        int newY = getY() + e.getY();
        setLocation(newX, newY);
        successor.setXCoordinate((short) newX);
        successor.setYCoordinate((short) newY);
        editor.setGrammarChanged(true);
        context.repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    void setEditor(ASDEditor givenEditor) {
        editor = givenEditor;
    }

    void setContext(Container c) {
        context = c;
    }

    void setDefaultCoordinates() {
        ASDEditNode eNode1 = ((ASDDigraphNode) dEdge.getFromNode()).getEditNode();
        ASDEditNode eNode2 = ((ASDDigraphNode) dEdge.getToNode()).getEditNode();
        int x1 = eNode1.rightConnectorX();
        int y1 = eNode1.rightConnectorY();
        int x2 = eNode2.leftConnectorX();
        int y2 = eNode2.leftConnectorY();
        if (eNode1 != eNode2) setPosition((x1 + x2) / 2 - (SIZE / 2), (y1 + y2) / 2 - (SIZE / 2)); else setPosition((x1 + x2) / 2 - (SIZE / 2), y1 - eNode1.getHeight() - (SIZE / 2));
        successor.setXCoordinate((short) getX());
        successor.setYCoordinate((short) getY());
    }

    void setPosition(int newX, int newY) {
        Insets insets = context.getInsets();
        setBounds(newX + insets.left, newY + insets.top, SIZE, SIZE);
    }

    ASDDigraphEdge getDigraphEdge() {
        return dEdge;
    }

    void setDigraphEdge(ASDDigraphEdge newEdge) {
        dEdge = newEdge;
    }

    ASDGrammarSuccessor getGrammarSuccessor() {
        return successor;
    }

    void setGrammarSuccessor(ASDGrammarSuccessor s) {
        successor = s;
    }

    public void stateChanged(ChangeEvent e) {
        if (this.isSelected() && this.getModel().isPressed()) {
            editor.edgeSelected(this);
        } else if (!this.isSelected() && this.getModel().isPressed()) {
            this.setSelected(true);
        }
    }

    static final int SIZE = 12;

    private Container context;

    private ASDEditor editor;

    private ASDGrammarSuccessor successor;

    private ASDDigraphEdge dEdge;
}

class EditEdgeMenu extends JPopupMenu implements ActionListener {

    EditEdgeMenu(ASDEditEdge edge) {
        eEdge = edge;
        setInvoker(eEdge);
        JMenuItem deleteMenuItem = new JMenuItem("Delete edge");
        deleteMenuItem.addActionListener(this);
        add(deleteMenuItem);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Delete edge")) {
            ASDEditor editor = eEdge.getEditor();
            if (editor != null) editor.deleteEdge(eEdge);
        }
    }

    ASDEditEdge eEdge;
}
