package org.octave.ide.script.virtualconsole.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows VirtualConsole component.
 */
public class VirtualConsoleAction extends AbstractAction {

    public VirtualConsoleAction() {
        super(NbBundle.getMessage(VirtualConsoleAction.class, "CTL_VirtualConsoleAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(VirtualConsoleTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = VirtualConsoleTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
