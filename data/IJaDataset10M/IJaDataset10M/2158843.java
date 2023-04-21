package org.openthinclient.console;

import java.awt.event.ActionEvent;
import java.util.Collection;
import com.levigo.util.swing.action.AbstractCommand;

/**
 * Action to exit the application.
 */
public class ExitCommand extends AbstractCommand {

    public void actionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    @Override
    public boolean checkDeeply(Collection args) {
        return true;
    }

    @Override
    protected void doExecute(Collection args) {
        System.exit(0);
    }
}
