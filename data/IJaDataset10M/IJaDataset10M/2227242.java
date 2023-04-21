package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Vector;
import org.argouml.application.api.Notation;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import ru.novosoft.uml.behavior.state_machines.MState;

/** Class to display graphics for a UML MState in a diagram. */
public class FigState extends FigStateVertex {

    public final int MARGIN = 2;

    public final int X = 0;

    public final int Y = 0;

    public final int W = 70;

    public final int H = 40;

    /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */
    FigRect _bigPort;

    FigRect _cover;

    FigText _internal;

    FigLine _divider;

    public FigState() {
        _bigPort = new FigRRect(X + 1, Y + 1, W - 2, H - 2, Color.cyan, Color.cyan);
        _cover = new FigRRect(X, Y, W, H, Color.black, Color.white);
        _bigPort.setLineWidth(0);
        _name.setLineWidth(0);
        _name.setBounds(X + 2, Y + 2, W - 4, _name.getBounds().height);
        _name.setFilled(false);
        _divider = new FigLine(X, Y + 2 + _name.getBounds().height + 1, W - 1, Y + 2 + _name.getBounds().height + 1, Color.black);
        _internal = new FigText(X + 2, Y + 2 + _name.getBounds().height + 4, W - 4, H - (Y + 2 + _name.getBounds().height + 4));
        _internal.setFont(LABEL_FONT);
        _internal.setTextColor(Color.black);
        _internal.setLineWidth(0);
        _internal.setFilled(false);
        _internal.setExpandOnly(true);
        _internal.setMultiLine(true);
        _internal.setJustification(FigText.JUSTIFY_LEFT);
        addFig(_bigPort);
        addFig(_cover);
        addFig(_name);
        addFig(_divider);
        addFig(_internal);
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    public FigState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public String placeString() {
        return "new MState";
    }

    public Object clone() {
        FigState figClone = (FigState) super.clone();
        Vector v = figClone.getFigs();
        figClone._bigPort = (FigRect) v.elementAt(0);
        figClone._cover = (FigRect) v.elementAt(1);
        figClone._name = (FigText) v.elementAt(2);
        figClone._divider = (FigLine) v.elementAt(3);
        figClone._internal = (FigText) v.elementAt(4);
        return figClone;
    }

    public Selection makeSelection() {
        return new SelectionState(this);
    }

    public void setOwner(Object node) {
        super.setOwner(node);
        bindPort(node, _bigPort);
    }

    public Dimension getMinimumSize() {
        Dimension nameDim = _name.getMinimumSize();
        Dimension internalDim = _internal.getMinimumSize();
        int h = nameDim.height + 4 + internalDim.height;
        int w = Math.max(nameDim.width + 4, internalDim.width + 4);
        return new Dimension(w, h);
    }

    public void setBounds(int x, int y, int w, int h) {
        if (_name == null) return;
        Rectangle oldBounds = getBounds();
        Dimension nameDim = _name.getMinimumSize();
        _name.setBounds(x + 2, y + 2, w - 4, nameDim.height);
        _divider.setShape(x, y + nameDim.height + 1, x + w - 1, y + nameDim.height + 1);
        _internal.setBounds(x + 2, y + nameDim.height + 4, w - 4, h - nameDim.height - 6);
        _bigPort.setBounds(x, y, w, h);
        _cover.setBounds(x, y, w, h);
        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    public void setLineColor(Color col) {
        _cover.setLineColor(col);
        _divider.setLineColor(col);
    }

    public Color getLineColor() {
        return _cover.getLineColor();
    }

    public void setFillColor(Color col) {
        _cover.setFillColor(col);
    }

    public Color getFillColor() {
        return _cover.getFillColor();
    }

    public void setFilled(boolean f) {
        _cover.setFilled(f);
    }

    public boolean getFilled() {
        return _cover.getFilled();
    }

    public void setLineWidth(int w) {
        _cover.setLineWidth(w);
        _divider.setLineWidth(w);
    }

    public int getLineWidth() {
        return _cover.getLineWidth();
    }

    /** Update the text labels */
    protected void modelChanged() {
        super.modelChanged();
        MState s = (MState) getOwner();
        if (s == null) return;
        String newText = Notation.generateStateBody(this, s);
        _internal.setText(newText);
        calcBounds();
        Rectangle rect = getBounds();
        setBounds(rect.x, rect.y, rect.width, rect.height);
        firePropChange("bounds", rect, getBounds());
    }

    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == _internal) {
            MState st = (MState) getOwner();
            if (st == null) return;
            String s = ft.getText();
            ParserDisplay.SINGLETON.parseStateBody(st, s);
        }
    }
}
