package com.peterhi.client.impl;

import org.eclipse.swt.events.SelectionEvent;
import com.peterhi.client.ui.widgets.AbstractAction;
import com.peterhi.client.ui.widgets.Action;
import com.peterhi.client.ui.widgets.ActionFactory;
import com.peterhi.client.ui.widgets.Whiteboard;
import com.peterhi.client.ui.widgets.whiteboard.Element;
import com.peterhi.client.ui.widgets.whiteboard.Oval;
import com.peterhi.client.ui.widgets.whiteboard.Rectangle;
import com.peterhi.client.ui.widgets.whiteboard.editors.OvalEditor;
import com.peterhi.client.ui.widgets.whiteboard.editors.RectangleEditor;

public class WhiteboardActionFactory implements ActionFactory {

    private Action current;

    private Whiteboard wboard;

    public WhiteboardActionFactory(Whiteboard wboard) {
        this.wboard = wboard;
    }

    @Override
    public Action[] actions() {
        return new Action[] { current = new AbstractAction("pointer", "pointer.png", "Pointer", true, true) {

            @Override
            public void action(SelectionEvent e) {
                if (toggle(this)) {
                    wboard.setNew(null);
                    wboard.setEditor(null, null);
                }
            }
        }, new AbstractAction("rectangle", "rect.png", "Rectangle", true) {

            @Override
            public void action(SelectionEvent e) {
                if (toggle(this)) {
                    Element ele = wboard.create(Rectangle.class);
                    wboard.setNew(ele);
                    wboard.setEditor(RectangleEditor.class, ele);
                }
            }
        }, new AbstractAction("oval", "ellipse.png", "Oval", true) {

            @Override
            public void action(SelectionEvent e) {
                if (toggle(this)) {
                    Element ele = wboard.create(Oval.class);
                    wboard.setNew(ele);
                    wboard.setEditor(OvalEditor.class, ele);
                }
            }
        } };
    }

    private boolean toggle(Action a) {
        if (current == a) {
            current.setSelection(true);
            return false;
        }
        if (current != null) {
            current.setSelection(false);
        }
        current = a;
        return true;
    }
}
