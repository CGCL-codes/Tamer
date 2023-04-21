package org.amse.audalov.view.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.amse.audalov.view.View;

public class AddPointAction extends AbstractToggleGeoAction {

    public AddPointAction(View view) {
        super(view, "Add point", "btAddPoint");
    }

    public void mousePressed(MouseEvent me) {
        super.mousePressed(me);
        if (me.getButton() == MouseEvent.BUTTON1 && myClickedPoint == null) {
            getGeoCanvas().getModel().createPoint(myX, myY);
            getGeoCanvas().repaint();
        }
    }

    public void mouseMoved(MouseEvent me) {
        super.mouseMoved(me);
    }
}
