package com.fddtool.pd.common;

/**
 * A class implements <code>INamedEntry</code> interface if it represents
 * a business object that has a name.
 *
 * @author Serguei Khramtchenko
 */
public interface INamedEntry {

    /**
     * Returns a name of the object.
     *
     * @return String containing a name that may be used in UI.
     */
    public String getName();
}
