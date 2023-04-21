package org.exmaralda.folker.actions;

import javax.swing.*;
import org.exmaralda.folker.application.AbstractTimeviewPartiturPlayerControl;

/**
 *
 * @author thomas
 */
public abstract class AbstractApplicationAction extends javax.swing.AbstractAction {

    public AbstractTimeviewPartiturPlayerControl applicationControl;

    /** Creates a new instance of AbstractApplicationAction */
    public AbstractApplicationAction(AbstractTimeviewPartiturPlayerControl ac, String name, Icon icon) {
        super(name, icon);
        applicationControl = ac;
    }
}
