package org.jcrontab.gui;

/**
 * This class is an Interface of the different Listeners of the Jcrontab GUI
 * @author $Author: iolalla $
 * @version $Revision: 1.2 $
 */
public interface Listener {

    public void processEvent(Event event);

    public String getName();
}
