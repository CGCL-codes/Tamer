package com.aol.omp.base.interfaces;

/**
 *
 * @author raviaz
 */
public interface IOMPMain {

    /**
     * getApplicationProperty() to get the configuration values from the descriptor.
     *  e.g In J2me getAppProperty("OK_keycode");
     * @param name
     * @return Returns the value mapped to the name.
     */
    public String getApplicationProperty(String name);

    /**
     * returns the FactoryImpl object.
     * @return
     */
    public IFactory getFactory();

    public void toBrowser(String URL);

    /**
     * shell calls on exit.
     */
    public void exit();
}
