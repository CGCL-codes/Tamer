package org.apache.axis;

import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.utils.Messages;
import org.apache.commons.logging.Log;
import javax.xml.namespace.QName;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A <code>FaultableHandler</code> is essentially a wrapper for any other
 * Handler which provides flexible fault handling semantics.
 *
 *
 * @author Doug Davis (dug@us.ibm.com)
 * @author Glen Daniels (gdaniels@apache.org)
 */
public class FaultableHandler extends BasicHandler {

    /**
     * The <code>Log</code> used to log all events that would be of general
     * interest.
     */
    protected static Log log = LogFactory.getLog(FaultableHandler.class.getName());

    /**
     * The <code>Log</code> used for enterprise-centric logging.
     *
     * The enterprise category is for stuff that an enterprise product might
     * want to track, but in a simple environment (like the AXIS build) would
     * be nothing more than a nuisance.
     */
    protected static Log entLog = LogFactory.getLog(Constants.ENTERPRISE_LOG_CATEGORY);

    /**
     * The <code>Handler</code> that will do the actual work of handeling the
     * fault. 
     */
    protected Handler workHandler;

    /**
     * Create a new FaultHandler.
     *
     * @param workHandler the Handler we're going to wrap with Fault semantics.
     */
    public FaultableHandler(Handler workHandler) {
        this.workHandler = workHandler;
    }

    public void init() {
        workHandler.init();
    }

    public void cleanup() {
        workHandler.cleanup();
    }

    /**
     * Invokes the specified handler.  If there's a fault the appropriate
     * key will be calculated and used to find the fault chain to be
     * invoked.  This assumes that the workHandler has caught the exception
     * and already done its fault processing - as needed.
     *
     * @param msgContext  the <code>MessageContext</code> to process
     * @throws AxisFault  if anything goes terminally wrong
     */
    public void invoke(MessageContext msgContext) throws AxisFault {
        log.debug("Enter: FaultableHandler::invoke");
        try {
            workHandler.invoke(msgContext);
        } catch (Exception e) {
            entLog.info(Messages.getMessage("toAxisFault00"), e);
            AxisFault fault = AxisFault.makeFault(e);
            Handler faultHandler = null;
            Hashtable options = getOptions();
            if (options != null) {
                Enumeration enumeration = options.keys();
                while (enumeration.hasMoreElements()) {
                    String s = (String) enumeration.nextElement();
                    if (s.equals("fault-" + fault.getFaultCode().getLocalPart())) {
                        faultHandler = (Handler) options.get(s);
                    }
                }
            }
            if (faultHandler != null) {
                faultHandler.invoke(msgContext);
            } else {
                throw fault;
            }
        }
        log.debug("Exit: FaultableHandler::invoke");
    }

    /**
     * Some handler later on has faulted so we need to process the fault.
     *
     * @param msgContext  the context to process
     */
    public void onFault(MessageContext msgContext) {
        log.debug("Enter: FaultableHandler::onFault");
        workHandler.onFault(msgContext);
        log.debug("Exit: FaultableHandler::onFault");
    }

    public boolean canHandleBlock(QName qname) {
        return (workHandler.canHandleBlock(qname));
    }
}

;
