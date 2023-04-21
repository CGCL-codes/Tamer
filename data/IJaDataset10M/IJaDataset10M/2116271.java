package au.gov.qld.dnr.dss.event;

import au.net.netstorm.util.event.ChangeListener;

/**
 * The listener interface for receiving notification of 
 * changes to an alternative.
 */
public interface AlternativeListener extends ChangeListener {

    /**
     * Invoked when <i>alternative</i> Description is changed.
     */
    public void changedDescription(AlternativeEvent e);

    /**
     * Invoked when <i>alternative</i> Short Description is changed.
     */
    public void changedShortDescription(AlternativeEvent e);

    /**
     * Invoked when <i>alternative</i> Comment is changed.
     */
    public void changedComment(AlternativeEvent e);
}
