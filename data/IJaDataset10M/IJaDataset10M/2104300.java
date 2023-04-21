package svc.ui.event;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolItem;
import svc.core.Configuration;
import svc.ui.GUI;
import svc.util.ImageUtils;

/**
 * Action to power on or off a computer.
 *
 * @author Allen Charlton
 */
public class PowerAction extends Action {

    private GUI gui;

    public PowerAction(GUI gui) {
        this.gui = gui;
    }

    public void widgetSelected(SelectionEvent event) {
        ToolItem item = (ToolItem) event.widget;
        if (item.getSelection()) {
            Image original = item.getImage();
            item.setImage(ImageUtils.loadResourceImage(item.getDisplay(), "poweroff.gif"));
            item.setToolTipText(Configuration.getString("window.toolbar.powerOff"));
            original.dispose();
            gui.getComputer().powerOn();
        } else {
            Image original = item.getImage();
            item.setImage(ImageUtils.loadResourceImage(item.getDisplay(), "poweron.gif"));
            item.setToolTipText(Configuration.getString("window.toolbar.power"));
            original.dispose();
            gui.getComputer().powerOff();
        }
    }
}
