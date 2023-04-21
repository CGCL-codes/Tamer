package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import fr.soleil.bensikin.components.BensikinMenuBar;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.snapshot.Snapshot;

/**
 * An action that resets the display, removing currently opened and selected
 * contexts and snapshots
 * 
 * @author CLAISSE
 */
public class ResetAction extends BensikinAction {

    /**
	 * Standard action constructor that sets the action's name and icon.
	 * 
	 * @param name
	 *            The action name
	 * @param icon
	 *            The action icon
	 */
    public ResetAction(String name, Icon icon) {
        putValue(Action.NAME, name);
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.SHORT_DESCRIPTION, name);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Context.reset();
        Snapshot.reset(true, true);
        BensikinMenuBar.getInstance().resetRegisterItem();
        ContextActionPanel.getInstance().resetRegisterButton();
        ContextActionPanel.getInstance().allowPrint(false);
    }
}
