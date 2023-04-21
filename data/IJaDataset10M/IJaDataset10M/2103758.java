package org.apache.axis.handlers.http;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.encoding.Base64;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.utils.Messages;
import org.apache.commons.logging.Log;

/** An <code>HTTPAuthHandler</code> simply sets the context's username
 * and password properties from the HTTP auth headers.
 * 
 * @author Glen Daniels (gdaniels@allaire.com)
 * @author Doug Davis (dug@us.ibm.com)
 */
public class HTTPAuthHandler extends BasicHandler {

    protected static Log log = LogFactory.getLog(HTTPAuthHandler.class.getName());

    public void invoke(MessageContext msgContext) throws AxisFault {
        log.debug("Enter: HTTPAuthHandler::invoke");
        String tmp = (String) msgContext.getProperty(HTTPConstants.HEADER_AUTHORIZATION);
        if (tmp != null) tmp = tmp.trim();
        if (tmp != null && tmp.startsWith("Basic ")) {
            String user = null;
            int i;
            tmp = new String(Base64.decode(tmp.substring(6)));
            i = tmp.indexOf(':');
            if (i == -1) user = tmp; else user = tmp.substring(0, i);
            msgContext.setUsername(user);
            log.debug(Messages.getMessage("httpUser00", user));
            if (i != -1) {
                String pwd = tmp.substring(i + 1);
                if (pwd != null && pwd.equals("")) pwd = null;
                if (pwd != null) {
                    msgContext.setPassword(pwd);
                    log.debug(Messages.getMessage("httpPassword00", pwd));
                }
            }
        }
        log.debug("Exit: HTTPAuthHandler::invoke");
    }
}
