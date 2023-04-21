package org.ln.millesimus.gui.views;

/**
 * This interface defines the view for displaying SportsTracker entries
 * (e.g. in a list or a calendar).
 * 
 * @author  Stefan Saring
 * @version 1.0
 */
public interface EntryView {

    /** Enumeration of all available entry view types. */
    enum ViewType {
    }

    /** 
     * Initializes the view after startup (not visible yet).
     */
    void initView();

    void createView();

    /** 
     * Updates the view after data was modified.
     */
    void updateView();

    /** 
     * Reset the view.
     */
    void resetView();

    /**
     * This methods returns true if all requested input are valid.
     * @return true if all requested input are valid, false otherware
     */
    Boolean isValidInput();
}
