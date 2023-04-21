package gov.nasa.gsfc.visbard.gui.resourcemanip.remote;

import gov.nasa.gsfc.vspo.client.util.DateRange;
import javax.swing.JDialog;

/**
 * Blank implementation of a Remote Navigation Panel. Abstract methods defined
 * herein throw UnsupportedOperationExceptions if called. This is meant to be
 * used as a visual placeholder until an actual remote navigation panel is
 * selected.
 *
 * @author Aaron Smith, Aquilent
 */
public final class BlankNavPane extends RemoteNavPane {

    private static final String DATA_ERROR_MSG = "No data retrieval mechanism are supported for this panel.";

    private static final String DATE_ERROR_MSG = "No date ranges are supported for this panel.";

    /**
     * @param owner
     */
    public BlankNavPane(JDialog owner) {
        super(owner, null);
        init();
        _showAllButton.setEnabled(false);
        _showRecognizedButton.setEnabled(false);
    }

    /** 
     * @throws UnsupportedOperationException in all cases because this panel
     * does not support data retrieval.
     */
    public String[] retrieveData(int resolution) {
        throw new UnsupportedOperationException(DATA_ERROR_MSG);
    }

    /** 
     * @throws UnsupportedOperationException in all cases because this panel
     * does not support date ranges.
     */
    public DateRange getDateRange() {
        throw new UnsupportedOperationException(DATE_ERROR_MSG);
    }
}
