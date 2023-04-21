package org.geoforge.guitlc.dialog.panel.group.single;

import java.awt.event.KeyListener;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class GfrPnlGrpSngTxfFullEditable extends GfrPnlGrpSngTxfFullAbs {

    public GfrPnlGrpSngTxfFullEditable(String strWhat, KeyListener klr) {
        this(strWhat);
        super.addKeyListenerToTxf(klr);
    }

    public GfrPnlGrpSngTxfFullEditable(String strWhat) {
        super(strWhat);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        this.setMandatory(false);
        return true;
    }
}
