package com.duroty.service;

/**
 * @author durot
 *
 */
public interface Servible {

    /**
    * DOCUMENT ME!
    *
    * @param key DOCUMENT ME!
    */
    public void addPool(String key);

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void removePool(String key);

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean poolContains(String key);
}
