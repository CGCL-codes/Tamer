package uk.org.sgj.OHCApparatus;

import javax.swing.event.*;

/**
 *
 * @author Steffen
 */
class TabListener implements ChangeListener {

    private OHCCurrentRecordPanel ocrp;

    TabListener(OHCCurrentRecordPanel o) {
        ocrp = o;
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        ocrp.reactToNewSelectedTab();
    }
}
