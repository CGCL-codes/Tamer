package net.infonode.docking.action;

import java.io.*;
import javax.swing.*;
import net.infonode.docking.*;
import net.infonode.gui.action.*;
import net.infonode.gui.icon.*;

/**
 * An action that can be performed on a {@link DockingWindow}. It has a name and an optional icon.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 * @since IDW 1.3.0
 */
public abstract class DockingWindowAction implements Serializable, IconProvider {

    private static final long serialVersionUID = 1;

    /**
   * Returns the name of this action.
   *
   * @return the name of this action
   */
    public abstract String getName();

    /**
   * Performs this action on a window.
   *
   * @param window the window on which to perform the action
   */
    public abstract void perform(DockingWindow window);

    /**
   * Returns true if this action is performable on a window.
   *
   * @param window the window on which the action will be performed
   * @return true if this action is performable on the window
   */
    public abstract boolean isPerformable(DockingWindow window);

    /**
   * Creates a simple action that performs this action on a window.
   *
   * @param window the window on which to perform the action
   * @return the action that performs this action on a window.
   */
    public SimpleAction getAction(final DockingWindow window) {
        return new SimpleAction() {

            public String getName() {
                return DockingWindowAction.this.getName();
            }

            public boolean isEnabled() {
                return DockingWindowAction.this.isPerformable(window);
            }

            public void perform() {
                DockingWindowAction.this.perform(window);
            }

            public Icon getIcon() {
                return DockingWindowAction.this.getIcon();
            }
        };
    }

    /**
   * Returns the optional icon of this action.
   *
   * @return the optional icon of this action, null if there is no icon
   */
    public Icon getIcon() {
        return null;
    }

    public String toString() {
        return getName();
    }
}
