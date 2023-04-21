package org.primordion.user.app.SpringIdol;

import org.primordion.xholon.base.XholonWithPorts;

/**
 * Reimplementation of "Spring Idol" contest from:
 * Walls, Craig. (2008). Spring in Action, 2nd edition. Greenwich, CT: Manning.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.7.1 (Created on December 24, 2007)
 */
public class Harmonica extends XholonWithPorts implements Instrument {

    public Harmonica() {
    }

    public void play() {
        System.out.println("HUM HUM HUM");
    }
}
