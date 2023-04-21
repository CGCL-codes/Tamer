package org.phylowidget.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import org.andrewberman.ui.Point;
import org.andrewberman.ui.TextField;
import org.andrewberman.ui.UIEvent;
import org.phylowidget.PWContext;
import org.phylowidget.PWPlatform;
import org.phylowidget.PhyloTree;
import org.phylowidget.render.BasicTreeRenderer;
import org.phylowidget.render.NodeRange;
import org.phylowidget.tree.RootedTree;
import processing.core.PApplet;

public class PhyloTextField extends TextField {

    PWContext context;

    NodeRange curRange;

    String oldValue;

    int editMode;

    static final int LABEL = 0;

    static final int BRANCH_LENGTH = 1;

    public PhyloTextField(PApplet p) {
        super(p);
        this.context = PWPlatform.getInstance().getThisAppContext();
        hidden = true;
        alwaysAnchorLeft = true;
    }

    public void draw() {
        if (!hidden) {
            curRange.render.positionText(curRange.node, this);
            super.draw();
        }
    }

    protected void startEditing(NodeRange r, int editMode) {
        context.getPW().setMessage("Enter to commit, Esc to revert.");
        this.editMode = editMode;
        curRange = r;
        RootedTree t = r.render.getTree();
        reset();
        oldValue = null;
        switch(editMode) {
            case (LABEL):
                oldValue = t.getLabel(r.node);
                break;
            case (BRANCH_LENGTH):
                oldValue = String.valueOf(t.getBranchLength(r.node));
                break;
        }
        text.replace(0, text.length(), oldValue);
        show();
        selectAll();
        context.focus().setModalFocus(this);
    }

    public void hide() {
        super.hide();
        context.focus().removeFromFocus(this);
        context.getPW().setMessage("");
    }

    void hideAndCommit() {
        hide();
        RootedTree t = curRange.render.getTree();
        if (t instanceof PhyloTree) {
            PhyloTree pt = (PhyloTree) t;
        }
        context.ui().layout();
    }

    void hideAndReject() {
        hide();
        updateValue(oldValue);
    }

    void updateValue(String s) {
        synchronized (this) {
            BasicTreeRenderer r = curRange.render;
            switch(editMode) {
                case (LABEL):
                    r.getTree().setLabel(curRange.node, s);
                    break;
                case (BRANCH_LENGTH):
                    try {
                        double value = Double.parseDouble(s);
                        r.getTree().setBranchLength(curRange.node, value);
                    } catch (Exception e) {
                        context.ui().layout();
                        return;
                    }
            }
            r.layoutTrigger();
        }
    }

    public void fireEvent(int id) {
        super.fireEvent(id);
        if (id == UIEvent.TEXT_VALUE) {
            updateValue(getText());
            this.layout();
        }
    }

    public void keyEvent(KeyEvent e) {
        super.keyEvent(e);
        if (hidden) return;
        switch(e.getKeyCode()) {
            case (KeyEvent.VK_ESCAPE):
                hideAndReject();
                e.consume();
                break;
            case (KeyEvent.VK_ENTER):
                hideAndCommit();
                e.consume();
                break;
        }
    }

    public void mouseEvent(MouseEvent e, Point screen, Point model) {
        super.mouseEvent(e, screen, model);
        if (hidden) return;
        if (e.getID() != MouseEvent.MOUSE_PRESSED) return;
        Point p1;
        if (useCameraCoordinates) p1 = model; else p1 = screen;
        if (!withinOuterRect(p1)) {
            hideAndCommit();
        }
    }
}
