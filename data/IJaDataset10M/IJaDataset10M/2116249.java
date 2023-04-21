package org.apache.axis.handlers;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;

/**
 *
 * @author Doug Davis (dug@us.ibm.com)
 * @author Glen Daniels (gdaniels@allaire.com)
 */
public class ErrorHandler extends BasicHandler {

    protected static Log log = LogFactory.getLog(ErrorHandler.class.getName());

    public void invoke(MessageContext msgContext) throws AxisFault {
        log.debug("Enter: ErrorHandler::invoke");
        throw new AxisFault("Server.Whatever", "ERROR", null, null);
    }
}

;
