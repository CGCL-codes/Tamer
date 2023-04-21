package ch.iserver.ace.net.impl.protocol;

import org.apache.log4j.Logger;
import org.beepcore.beep.core.OutputDataStream;
import ch.iserver.ace.net.impl.NetworkServiceImpl;
import ch.iserver.ace.net.impl.PublishedDocument;
import ch.iserver.ace.net.impl.protocol.RequestImpl.DocumentInfo;

/**
 *
 */
public class InvitationRejectedRecipientFilter extends AbstractRequestFilter {

    private Logger LOG = Logger.getLogger(InvitationRejectedRecipientFilter.class);

    public InvitationRejectedRecipientFilter(RequestFilter successor) {
        super(successor);
    }

    public void process(Request request) {
        if (request.getType() == ProtocolConstants.INVITE_REJECTED) {
            LOG.info("--> process()");
            DocumentInfo info = (DocumentInfo) request.getPayload();
            String docId = info.getDocId();
            PublishedDocument doc = (PublishedDocument) NetworkServiceImpl.getInstance().getPublishedDocuments().get(docId);
            if (doc != null) {
                String userId = info.getUserId();
                doc.rejectInvitedUser(userId);
            } else {
                LOG.debug("received 'invitation rejected' from [" + info.getUserId() + "] " + "for already concealed document [" + docId + "]");
            }
            try {
                OutputDataStream os = new OutputDataStream();
                os.setComplete();
                request.getMessage().sendRPY(os);
            } catch (Exception e) {
                LOG.error("could not send confirmation [" + e.getMessage() + "]");
            }
            LOG.info("<-- process()");
        } else {
            super.process(request);
        }
    }
}
