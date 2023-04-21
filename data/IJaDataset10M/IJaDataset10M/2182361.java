package com.akivasoftware.misc.domain;

import java.util.Properties;
import java.util.Enumeration;

/**
 *  Represents a StartupHandlerComponent (a line item in a simulation driver file)
 *  @author J.Varner
 */
public class AStartupHandlerComponent extends ADomainComponent {

    /** Constructor - builds new instance of <code>ADomainComponent</code> */
    public AStartupHandlerComponent() {
        super();
        init();
    }

    /**
     * Initailizes the DomainObject
     */
    public void init() {
        propMap = new Properties();
        setProperty("xmlName", "StartupHandlerComponent");
        setProperty("path", "_STUFF_HERE");
        setProperty("orbName", "_STUFF_HERE");
    }
}
