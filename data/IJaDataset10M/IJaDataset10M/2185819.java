package org.dyndns.doujindb.log.impl;

import org.dyndns.doujindb.log.*;

/**  
* Factory.java - Logger service factory.
* @author  nozomu
* @version 1.0
*/
public final class Factory {

    public static Logger getService() throws Exception {
        return new StdOutLogger();
    }
}
