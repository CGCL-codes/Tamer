package com.volantis.mcs.cli.uaprofclient.output;

import java.util.Map;

/**
 * Instances of this interface may be used to serialise MCS device attributes.
 */
public interface ProfileSerialiser {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Serialise the device attributes contained in the collection provided.
     * 
     * @param attributes the device attributes to be serialised.
     */
    void serialise(Map attributes);
}
