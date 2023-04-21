package ch.iserver.ace.net.protocol.filter;

import org.apache.log4j.Logger;
import org.beepcore.beep.core.Channel;
import org.beepcore.beep.core.MessageMSG;
import org.beepcore.beep.core.OutputDataStream;
import ch.iserver.ace.net.protocol.ProtocolConstants;
import ch.iserver.ace.net.protocol.Request;

/**
 * NullRequestFilter. If the channel of the request is available, it sends
 * a empty reply.
 * 
 * @see ch.iserver.ace.net.protocol.filter.AbstractRequestFilter
 */
public class NullRequestFilter extends AbstractRequestFilter {

    private static Logger LOG = Logger.getLogger(NullRequestFilter.class);

    /**
	 * Public constructor.
	 * 
	 * @param successor
	 */
    public NullRequestFilter(RequestFilter successor) {
        super(successor);
    }

    /**
	 * {@inheritDoc}
	 */
    public void process(Request request) {
        try {
            if (request.getType() == ProtocolConstants.NULL) {
                LOG.info("--> process()");
                MessageMSG message = request.getMessage();
                if (message.getChannel() != null && message.getChannel().getState() == Channel.STATE_ACTIVE) {
                    LOG.debug("send empty reply");
                    OutputDataStream os = new OutputDataStream();
                    os.setComplete();
                    message.sendRPY(os);
                } else {
                    LOG.debug("cannot send empty reply [" + ((message.getChannel() != null) ? message.getChannel() + "] [" + message.getChannel().getState() + "]" : "null") + "]");
                }
                LOG.info("<-- process()");
            } else {
                super.process(request);
            }
        } catch (Exception e) {
            LOG.error("exception processing request [" + e + ", " + e.getMessage() + "]");
        }
    }
}
