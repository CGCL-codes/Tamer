package jade.core.messaging;

import java.util.Date;
import java.util.Hashtable;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.core.ContainerID;
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.VerticalCommand;
import jade.core.AgentContainer;
import jade.core.Filter;
import jade.core.AID;
import jade.core.management.AgentManagementSlice;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.ACLCodec;
import jade.lang.acl.StringACLCodec;
import jade.lang.acl.LEAPACLCodec;
import jade.util.Logger;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import jade.util.leap.Map;

/**
 * Class that filters outgoing commands related to the encoding of ACL messages
 *
 * @author Jerome Picault - Motorola Labs
 * @author Nicolas Lhuillier - Motorola Labs
 * @version $Date: 2011-06-06 10:34:35 +0200(lun, 06 giu 2011) $ $Revision: 6416 $
 */
public class OutgoingEncodingFilter extends Filter {

    private Map messageEncodings;

    private AgentContainer myAgentContainer;

    private MessagingService myService;

    public OutgoingEncodingFilter(Map m, AgentContainer ac, MessagingService ms) {
        messageEncodings = m;
        myAgentContainer = ac;
        myService = ms;
        setPreferredPosition(10);
    }

    /**
	 * Process the SEND_MESSAGE VCommand encoding the ACLMessage with the 
	 * proper representation and adjusting Envelope fields:
	 * 1) If the receiver lives in the local container 
	 *   --> Do not encode (to speed up performances)
	 *   --> Don't touch the envelope
	 * 2) If the receiver lives in a remote container
	 *   --> Encode using the specified representation or "LEAP" if no representation is specified
	 *   --> If an envelope is present adjust its fields
	 * 3) If the receiver lives in a remote platform
	 *   --> Encode using the specified representation or "String" if no representation is specified
	 *   --> Create a default envelope if not present and adjust its fields
	 */
    public boolean accept(VerticalCommand cmd) {
        String name = cmd.getName();
        if (name.equals(MessagingSlice.SEND_MESSAGE)) {
            GenericMessage gmsg = (GenericMessage) cmd.getParam(1);
            myService.stamp(gmsg);
            AID sender = (AID) cmd.getParam(0);
            AID receiver = (AID) cmd.getParam(2);
            ACLMessage msg = gmsg.getACLMessage();
            try {
                if (msg.getSender().getName().length() < 1) msg.setSender(sender);
            } catch (NullPointerException e) {
                msg.setSender(sender);
            }
            if (myAgentContainer.isLocalAgent(receiver)) {
                return true;
            } else {
                prepareEnvelope(msg, receiver, gmsg);
            }
            try {
                byte[] payload = encodeMessage(msg);
                Envelope env = msg.getEnvelope();
                if (env != null) env.setPayloadLength(new Long(payload.length));
                ((GenericMessage) cmd.getParams()[1]).update(msg, env, payload);
            } catch (MessagingService.UnknownACLEncodingException ee) {
                ee.printStackTrace();
            }
        } else if (name.equals(AgentManagementSlice.INFORM_KILLED)) {
            myService.removeLocalAliases((AID) cmd.getParam(0));
        }
        return true;
    }

    public void postProcess(VerticalCommand cmd) {
        String name = cmd.getName();
        if (name.equals(jade.core.mobility.AgentMobilityHelper.INFORM_MOVED)) {
            AID agent = (AID) cmd.getParam(0);
            Location destination = (Location) cmd.getParam(1);
            if (!myAgentContainer.isLocalAgent(agent)) {
                if (destination instanceof ContainerID) {
                    myService.transferLocalAliases(agent, (ContainerID) destination);
                }
            }
        }
    }

    /**
	 * This method puts into the envelope the missing information if required
	 */
    public void prepareEnvelope(ACLMessage msg, AID receiver, GenericMessage gmsg) {
        Envelope env = msg.getEnvelope();
        String defaultRepresentation = null;
        if (myService.livesHere(receiver)) {
            if (env == null) {
                return;
            } else {
                defaultRepresentation = LEAPACLCodec.NAME;
            }
        } else {
            gmsg.setForeignReceiver(true);
            if (env == null) {
                msg.setDefaultEnvelope();
                env = msg.getEnvelope();
            } else {
                defaultRepresentation = StringACLCodec.NAME;
            }
        }
        String rep = env.getAclRepresentation();
        if (rep == null) env.setAclRepresentation(defaultRepresentation);
        Iterator itTo = env.getAllTo();
        if (!itTo.hasNext()) {
            Iterator itReceiver = msg.getAllReceiver();
            while (itReceiver.hasNext()) env.addTo((AID) itReceiver.next());
        }
        AID from = env.getFrom();
        if (from == null) {
            env.setFrom(msg.getSender());
        }
        Date d = env.getDate();
        if (d == null) env.setDate(new Date());
        env.clearAllIntendedReceiver();
        env.addIntendedReceiver(receiver);
        Long payloadLength = env.getPayloadLength();
        if (payloadLength == null) env.setPayloadLength(new Long(-1));
    }

    /**
	 * Encodes an ACL message according to the acl-representation described 
	 * in the envelope. If there is no explicit acl-representation in the
	 * envelope, uses the String representation
	 * @param msg the message to be encoded
	 * @return the payload of the message
	 */
    public byte[] encodeMessage(ACLMessage msg) throws MessagingService.UnknownACLEncodingException {
        Envelope env = msg.getEnvelope();
        String enc = (env != null ? env.getAclRepresentation() : LEAPACLCodec.NAME);
        if (enc != null) {
            ACLCodec codec = (ACLCodec) messageEncodings.get(enc.toLowerCase());
            if (codec != null) {
                String charset;
                if ((env == null) || ((charset = env.getPayloadEncoding()) == null)) {
                    charset = ACLCodec.DEFAULT_CHARSET;
                }
                return codec.encode(msg, charset);
            } else {
                throw new MessagingService.UnknownACLEncodingException("Unknown ACL encoding: " + enc + ".");
            }
        } else {
            throw new MessagingService.UnknownACLEncodingException("No ACL encoding set.");
        }
    }
}
