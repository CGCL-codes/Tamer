package org.matsim.core.controler.events;

import org.matsim.core.controler.Controler;

/**
 * ControlerEvent to notify all observers of the controler that the controler instance is setup
 *
 * @author dgrether
 */
public class StartupEvent extends ControlerEvent {

    public StartupEvent(final Controler controler) {
        super(controler);
    }
}
