package com.ebixio.virtmus;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows PlayList component.
 */
public class PlayListAction extends AbstractAction {

    public PlayListAction() {
        super(NbBundle.getMessage(PlayListAction.class, "CTL_PlayListAction"));
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage(PlayListTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = PlayListTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
