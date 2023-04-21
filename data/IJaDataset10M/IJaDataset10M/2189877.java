package net.infonode.gui.action;

import java.awt.event.*;
import javax.swing.*;
import net.infonode.gui.icon.*;

/**
 * An action with an icon and a title.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 * @since IDW 1.3.0
 */
public abstract class SimpleAction implements IconProvider {

    public abstract String getName();

    public abstract void perform();

    public abstract boolean isEnabled();

    protected SimpleAction() {
    }

    /**
   * Converts this action into a Swing {@link Action}.
   *
   * @return the Swing {@link Action}
   */
    public Action toSwingAction() {
        AbstractAction action = new AbstractAction(getName(), getIcon()) {

            public void actionPerformed(ActionEvent e) {
                perform();
            }
        };
        action.setEnabled(isEnabled());
        return action;
    }
}
