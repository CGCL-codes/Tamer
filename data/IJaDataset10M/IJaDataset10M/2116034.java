package com.intel.gpe.services.jms.common;

/**
 * @author Alexander Lukichev
 * @version $Id$
 */
public class StageInFailedException extends JMSException {

    public StageInFailedException(String message, Throwable e) {
        super(message, e);
    }

    public StageInFailedException(String message) {
        super(message);
    }
}
