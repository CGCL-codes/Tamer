package de.iritgo.aktera.aktario;

import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktera.aktario.gui.AkteraAktarioGUI;

/**
 * Login command
 */
public class TestShowBuddyListCommand extends Command {

    /**
	 * Login command
	 */
    public TestShowBuddyListCommand() {
        super("test.aktera-aktario.showBuddyListCommand");
    }

    /**
	 * Perform a login
	 *
	 * @see de.buerobyte.iritgo.core.command.Command#perform()
	 */
    @Override
    public void perform() {
        AkteraAktarioGUI gui = (AkteraAktarioGUI) Client.instance().getClientGUI();
        gui.getDesktopManager().getDisplay("BuddyListPane").bringToFront();
        gui.getDesktopManager().getDisplay("BuddyListPane").show();
    }
}
