package org.dbe.servent;

import org.apache.log4j.Logger;
import org.dbe.servent.http.ServentHandler;

/**
 * 
 * @author bob 
 */
public abstract class GenericServentHandler implements ServentHandler {

    /** Servent Context */
    protected ServentContext context;

    /** Logger */
    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * @see org.dbe.servent.http.ServentHandler#initialize(org.dbe.servent.ServentContextImpl)
     */
    public void initialize(ServentContext context) {
        this.context = context;
    }
}
