package org.exmaralda.folker.actions.playeractions;

import java.awt.event.ActionEvent;
import org.exmaralda.folker.actions.AbstractApplicationAction;
import javax.swing.*;
import org.exmaralda.folker.application.AbstractTimeviewPartiturPlayerControl;

/**
 *
 * @author thomas
 */
public class PlayAction extends AbstractApplicationAction {

    /** Creates a new instance of PlayAction */
    public PlayAction(AbstractTimeviewPartiturPlayerControl ac, String name, Icon icon) {
        super(ac, name, icon);
    }

    public void actionPerformed(ActionEvent e) {
        applicationControl.play();
    }
}
