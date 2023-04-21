package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;
import org.argouml.uml.diagram.ui.SelectionMoveClarifiers;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigText;
import ru.novosoft.uml.foundation.core.MElement;

/** Class to display graphics for a UML MState in a diagram. */
public class FigShallowHistoryState extends FigStateVertex {

    public final int MARGIN = 2;

    public int x = 0;

    public int y = 0;

    public int width = 24;

    public int height = 24;

    /** The main label on this icon. */
    FigText _name;

    /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */
    FigCircle _bigPort;

    FigCircle _head;

    public FigShallowHistoryState() {
        _bigPort = new FigCircle(x, y, width, height, Color.cyan, Color.cyan);
        _head = new FigCircle(x, y, width, height, Color.black, Color.white);
        _name = new FigText(x + 5, y + 5, width - 10, height - 10);
        _name.setText("H");
        _name.setTextColor(Color.black);
        _name.setFilled(false);
        _name.setLineWidth(0);
        addFig(_bigPort);
        addFig(_head);
        addFig(_name);
        setBlinkPorts(false);
        Rectangle r = getBounds();
    }

    public String placeString() {
        return "H";
    }

    public FigShallowHistoryState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public Object clone() {
        FigShallowHistoryState figClone = (FigShallowHistoryState) super.clone();
        Vector v = figClone.getFigs();
        figClone._bigPort = (FigCircle) v.elementAt(0);
        figClone._head = (FigCircle) v.elementAt(1);
        figClone._name = (FigText) v.elementAt(2);
        return figClone;
    }

    public void setOwner(Object node) {
        super.setOwner(node);
        bindPort(node, _bigPort);
        if (node instanceof MElement) ((MElement) node).addMElementListener(this);
    }

    /** History states are fixed size. */
    public boolean isResizable() {
        return false;
    }

    public Selection makeSelection() {
        return new SelectionMoveClarifiers(this);
    }

    public void setLineColor(Color col) {
        _head.setLineColor(col);
    }

    public Color getLineColor() {
        return _head.getLineColor();
    }

    public void setFillColor(Color col) {
        _head.setFillColor(col);
    }

    public Color getFillColor() {
        return _head.getFillColor();
    }

    public void setFilled(boolean f) {
    }

    public boolean getFilled() {
        return true;
    }

    public void setLineWidth(int w) {
        _head.setLineWidth(w);
    }

    public int getLineWidth() {
        return _head.getLineWidth();
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void keyPressed(KeyEvent ke) {
    }

    static final long serialVersionUID = 6572261327347541373L;
}
