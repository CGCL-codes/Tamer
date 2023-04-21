package com.ail.core.command;

import com.ail.core.BaseException;

/**
 * This exception is thrown by the JMS Accessor in response to exceptions thrown
 * by JMS itself. It acts as a simple wrapper.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class JMSServiceException extends BaseException {

    public JMSServiceException(Exception e) {
        super(e.toString(), e);
    }
}
