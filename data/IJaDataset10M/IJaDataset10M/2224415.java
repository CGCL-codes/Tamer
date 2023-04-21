package jmodnews.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * An {@link javax.swing.Action} that fires a {@link UserAction}.
 * Used to add UserActions to key strokes.
 * 
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class UserActionAction extends AbstractAction {

    private final ModuleGroup mg;

    private final UserAction ua;

    public UserActionAction(ModuleGroup mg, UserAction ua) {
        this.mg = mg;
        this.ua = ua;
    }

    public void actionPerformed(ActionEvent e) {
        ua.reset();
        if (mg.fireUserAction(ua) < 0) Toolkit.getDefaultToolkit().beep();
    }
}
