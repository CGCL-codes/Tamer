package ch.iserver.ace.net.protocol.filter;

import org.apache.log4j.Logger;
import org.beepcore.beep.core.BEEPError;
import org.beepcore.beep.core.BEEPException;
import org.beepcore.beep.core.MessageMSG;
import ch.iserver.ace.net.protocol.Request;

/**
 * Catches all requests which could not be filtered.
 * This class is used at the end of the request filter chain.
 */
public class FailureFilter extends AbstractRequestFilter {

    private static Logger LOG = Logger.getLogger(FailureFilter.class);

    /**
	 * Constructor.
	 * 
	 * @param successor the successor
	 */
    public FailureFilter(AbstractRequestFilter successor) {
        super(successor);
    }

    /**
	 * {@inheritDoc}
	 */
    public void process(Request request) {
        LOG.debug(request + " could not be processed, reply with error code.");
        MessageMSG message = request.getMessage();
        if (message != null) {
            try {
                message.sendERR(BEEPError.CODE_SERVICE_NOT_AVAILABLE, "could not process request.");
            } catch (BEEPException be) {
                LOG.error("could not send BEEPError [" + be.getMessage() + "]");
            }
        } else {
            LOG.error("unknown request could not be serialized [" + request.getType() + "]");
        }
    }
}
