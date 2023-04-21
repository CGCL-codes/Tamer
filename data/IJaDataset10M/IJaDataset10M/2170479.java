package de.juwimm.cms.gui.event;

import java.util.EventListener;

/**
 * Interface for the Listener-Class who wants to receive the Informations about a save-operation from a EditpaneHandler.
 * <p><b>Copyright: JuwiMacMillan Group GmbH (c) 2002</b></p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha Kulawik</a>
 * @version $Id: ViewComponentListener.java 8 2009-02-15 08:54:54Z skulawik $
 */
public interface ViewComponentListener extends EventListener {

    public void actionViewComponentPerformed(ViewComponentEvent ae);
}
