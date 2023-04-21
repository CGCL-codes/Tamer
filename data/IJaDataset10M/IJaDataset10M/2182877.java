package de.hu.gralog.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import de.hu.gralog.gui.MainPad;

public class ZoomOutAction extends AbstractAction {

    public ZoomOutAction() {
        super("Zoom Out", MainPad.createImageIcon("stock_zoom-out.png"));
        super.putValue(SHORT_DESCRIPTION, "Zoom Out");
    }

    public void actionPerformed(ActionEvent arg0) {
        MainPad.getInstance().getDesktop().getCurrentGraph().setScale(MainPad.getInstance().getDesktop().getCurrentGraph().getScale() / 2);
    }
}
