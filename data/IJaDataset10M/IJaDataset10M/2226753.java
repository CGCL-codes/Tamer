package org.apache.servicemix.smpp.marshaler;

import ie.omk.smpp.Connection;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SubmitSM;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import javax.xml.transform.TransformerException;

/**
 * @author lhein
 */
public interface SmppMarshalerSupport {

    /**
     * converts the received smpp packet into a normalized message
     * 
     * @param message   the message to fill the packet into
     * @param packet    the received packet to convert into message
     * @throws MessagingException       on errors
     */
    void toNMS(NormalizedMessage message, SMPPPacket packet) throws MessagingException;

    /**
     * converts a normalized message from the nmr into smpp packets
     * 
     * @param connection        the smpp connection
     * @param exchange          the message exchange
     * @param message           the normalized message
     * @return                  a submit sm object
     * @throws TransformerException     on errors in transformation
     */
    SubmitSM fromNMS(Connection connection, MessageExchange exchange, NormalizedMessage message) throws TransformerException;
}
