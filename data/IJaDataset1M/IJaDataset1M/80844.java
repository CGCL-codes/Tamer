package com.peralex.utilities.locale;

/**
 * A locale listener that fires on the event thread, making it simple to use.
 * 
 * @author Noel Grandin
 * @author Jaco Jooste
 */
public interface ILocaleListener {

    /**
   * This method is called when the locale has been changed. The listener should
	 * then update the visual components.
   */
    void componentsLocaleChanged();
}
