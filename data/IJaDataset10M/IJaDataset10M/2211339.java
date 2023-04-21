package ch.squix.nataware.service.rendezvous;

import java.util.Iterator;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.squix.nataware.node.PublicNode;

/**
 * Message handler that only logs what it receives
 * Used as a simple client for testing the NAT Discovery Server
 */
public class DummyRendezVousMessageHandler extends IoHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(DummyRendezVousMessageHandler.class);

    @Override
    public void messageReceived(IoSession session, Object objectMessage) throws Exception {
        if (!(objectMessage instanceof RendezVousMessage)) {
            logger.warn("Received message of unexpected type");
            return;
        }
        RendezVousMessage message = (RendezVousMessage) objectMessage;
        switch(message.getType()) {
            case PUBLIC_LIST_UPDATE_REQUEST:
                logger.info("Received Public List Update Request from " + session.getRemoteAddress() + " with session id " + message.getSessionId() + " containing the following nodes : ");
                Iterator<PublicNode> it = message.getPublicNodes().iterator();
                PublicNode n;
                while (it.hasNext()) {
                    n = it.next();
                    logger.info(n.getNodeID().toString() + " - " + n.getPublicSocketAddress() + " - isDead=" + n.isDead() + " - Charge=" + n.getRendezVousCharge());
                }
                break;
            case PUBLIC_LIST_UPDATE_ANSWER:
                logger.info("Received Public List Update Answer from " + session.getRemoteAddress() + " with session id " + message.getSessionId() + " containing the following nodes : ");
                Iterator<PublicNode> it2 = message.getPublicNodes().iterator();
                PublicNode n2;
                while (it2.hasNext()) {
                    n2 = it2.next();
                    logger.info(n2.getNodeID().toString() + " - " + n2.getPublicSocketAddress() + " - isDead=" + n2.isDead() + " - Charge=" + n2.getRendezVousCharge());
                }
                break;
            case RENDEZVOUS_REQUEST:
                logger.info("Received Rendezvous Request from " + session.getRemoteAddress() + " with session id " + message.getSessionId());
                break;
            case RENDEZVOUS_ANSWER:
                logger.info("Received Rendezvous Answer from " + session.getRemoteAddress() + " with session id " + message.getSessionId() + " which created a tunnel on port " + message.getTunnelPort() + " and the open side is on port " + message.getOpenPort());
                break;
            default:
                logger.info("Unknown Rendezvous Message received");
        }
    }
}
