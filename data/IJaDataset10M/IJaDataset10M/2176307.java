package ezsudoku;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Gathers all different options.
 *
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
public final class GamingOptions implements Serializable {

    /**
     */
    public static final String AUTO_CANDIDATE_OPTION = "autoCandidate";

    /**
     */
    public static final String DISPLAY_DEAD_END_OPTION = "displayDeadEnd";

    /**
     */
    public static final int OPTION_NUMBER = 2;

    /**
     */
    private boolean autoCandidate = false;

    /**
     */
    private boolean displayDeadEnd = false;

    /**
     */
    private PropertyChangeSupport pcs = null;

    /**
     * Gets the value of autoCandidate
     *
     * @return the value of autoCandidate
     */
    public boolean isAutoCandidate() {
        return this.autoCandidate;
    }

    /**
     * Sets the value of autoCandidate
     *
     * @param argAutoCandidate Value to assign to this.autoCandidate
     */
    public void setAutoCandidate(boolean argAutoCandidate) {
        this.pcs.firePropertyChange(AUTO_CANDIDATE_OPTION, this.autoCandidate, this.autoCandidate = argAutoCandidate);
    }

    /**
     * Gets the value of displayDeadEnd
     *
     * @return the value of displayDeadEnd
     */
    public boolean isDisplayDeadEnd() {
        return this.displayDeadEnd;
    }

    /**
     * Sets the value of displayDeadEnd
     *
     * @param argDisplayDeadEnd Value to assign to this.displayDeadEnd
     */
    public void setDisplayDeadEnd(boolean argDisplayDeadEnd) {
        this.pcs.firePropertyChange(DISPLAY_DEAD_END_OPTION, this.displayDeadEnd, this.displayDeadEnd = argDisplayDeadEnd);
    }

    /**
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     */
    public void activateAll() {
        setAutoCandidate(true);
        setDisplayDeadEnd(true);
    }

    /**
     * Sole constructor
     */
    public GamingOptions() {
        this.pcs = new PropertyChangeSupport(this);
    }
}
