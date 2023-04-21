package org.plugger.ui.base;

/**
 * Definition for a controller.
 *
 * @author "Antonio Begue"
 * @version $Revision: 1.0 $
 */
public interface IViewModel {

    /**
     * Returns the view of this controller.
     * @return An IView object.
     */
    IView getView();

    /**
     * Close the view of this controller.
     */
    void closeView();
}
