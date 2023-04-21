package org.ln.millesimus.gui;

import org.ln.millesimus.gui.views.BaseView;

public interface IMainView {

    /**
     * Initualizes the view.
     */
    void initView();

    /**
     * Initialization of the View after it has been displayed.
     */
    void postInit();

    /**
     * Returns the current displayed entry view instance.
     * @return the current view
     */
    BaseView getCurrentView();

    /**
     * Updates the complete view to show the current application data.
     */
    void updateView();

    /**
     * Reset thr view.
     */
    void resetView();
}
