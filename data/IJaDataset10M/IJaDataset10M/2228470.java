package de.juwimm.cms.gui.event;

import static de.juwimm.cms.client.beans.Application.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;

/**
 * Interface for the Listener-Class who wants to receive the Informations about a save-operation from a EditpaneHandler.
 * <p><b>Copyright: JuwiMacMillan Group GmbH (c) 2002</b></p>
 * @author <a href="mailto:d.bogun@juwimm.com">Dirk Bogun</a>
 * @version $Id: MyWindowListener.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class MyWindowListener extends WindowAdapter {

    private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));

    public MyWindowListener() {
    }

    public void windowClosing(WindowEvent e) {
        if (Constants.CMS_CLIENT_VIEW == Constants.CLIENT_VIEW_LOGIN) {
            System.exit(0);
        }
        if (ActionHub.fireExitPerformed(new ExitEvent())) {
            comm.getDbHelper().shutdown();
            System.exit(0);
        }
    }
}
