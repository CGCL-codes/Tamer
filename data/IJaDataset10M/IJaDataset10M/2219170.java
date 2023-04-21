package com.martiansoftware.jsap.xml;

import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.Switch;

/**
 * Provides support for loading JSAP configurations at runtime
 * via an xml file.  You don't need to access this class directly;
 * instead, use JSAP's constructors that support xml.
 * 
 * @author <a href="http://www.martiansoftware.com/contact.html">Marty Lamb</a>
 */
class SwitchConfig extends FlaggedConfig {

    public Parameter getConfiguredParameter() {
        Switch result = new Switch(getId());
        super.configure(result);
        result.setShortFlag(getShortFlag());
        result.setLongFlag(getLongFlag());
        return (result);
    }
}
