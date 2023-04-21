package org.exmaralda.folker.actions.waveformactions;

import org.exmaralda.folker.actions.AbstractApplicationAction;
import java.awt.event.ActionEvent;
import javax.swing.*;
import org.exmaralda.folker.application.AbstractTimeviewPartiturPlayerControl;

/**
 *
 * @author thomas
 */
public class FineTuneSelectionAction extends AbstractApplicationAction {

    int boundary;

    int amount;

    public static int LEFT_BOUNDARY = 0;

    public static int RIGHT_BOUNDARY = 1;

    /** Creates a new instance of OpenAction */
    public FineTuneSelectionAction(AbstractTimeviewPartiturPlayerControl ac, String name, Icon icon, int whichBoundary, int whichAmount) {
        super(ac, name, icon);
        boundary = whichBoundary;
        amount = whichAmount;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("[*** FineTuneSelectionAction ***]");
        applicationControl.fineTuneSelection(boundary, amount);
    }
}
