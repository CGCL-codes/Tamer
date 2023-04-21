package com.tirsen.hanoi.engine;

/**
 *
 *
 * <!-- $Id: OutChannel.java,v 1.1.1.1 2002/07/06 16:34:59 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&eacute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.1.1.1 $
 */
public abstract class OutChannel extends Channel {

    public boolean isBusy() {
        return false;
    }

    protected void demarshal(Object response) {
    }
}
