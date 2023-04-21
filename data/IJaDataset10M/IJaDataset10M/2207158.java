package ch.iserver.ace.net.impl.protocol;

import org.apache.log4j.Logger;
import org.beepcore.beep.core.ReplyListener;
import ch.iserver.ace.net.impl.protocol.RequestImpl.DocumentInfo;

/**
 *
 */
public class JoinRejectedSenderFilter extends AbstractRequestFilter {

    private Logger LOG = Logger.getLogger(JoinRejectedSenderFilter.class);

    private Serializer serializer;

    private ReplyListener listener;

    public JoinRejectedSenderFilter(RequestFilter successor, Serializer serializer, ReplyListener listener) {
        super(successor);
        this.serializer = serializer;
        this.listener = listener;
    }

    public void process(Request request) {
        try {
            if (request.getType() == ProtocolConstants.JOIN_REJECTED) {
                LOG.info("--> process()");
                DocumentInfo info = (DocumentInfo) request.getPayload();
                String code = info.getData();
                byte[] data = serializer.createResponse(ProtocolConstants.JOIN_REJECTED, info.getDocId(), code);
                RemoteUserSession session = SessionManager.getInstance().getSession(request.getUserId());
                MainConnection connection = session.getMainConnection();
                connection.send(data, session.getUser().getUserDetails().getUsername(), listener);
                LOG.info("<-- process()");
            } else {
                super.process(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("exception processing request [" + e + ", " + e.getMessage() + "]");
        }
    }
}
