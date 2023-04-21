package net.sf.freecol.common.model;

import java.util.logging.Logger;

/**
 * Helper container to remember the Europe state prior to some
 * change, and fire off any consequent property changes.
 */
public class EuropeWas {

    private static final Logger logger = Logger.getLogger(EuropeWas.class.getName());

    private Europe europe;

    private int unitCount;

    public EuropeWas(Europe europe) {
        this.europe = europe;
        this.unitCount = europe.getUnitCount();
    }

    /**
     * Fire any property changes resulting from actions in Europe.
     */
    public void fireChanges() {
        int newUnitCount = europe.getUnitCount();
        if (newUnitCount != unitCount) {
            String pc = Europe.UNIT_CHANGE.toString();
            europe.firePropertyChange(pc, unitCount, newUnitCount);
        }
    }
}
