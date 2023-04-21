package net.sf.jml.protocol.outgoing;

import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnUserPropertyType;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Change property for other user.
 * <p>
 * Supported Protocol: >= MSNP11
 * <p>
 * Syntax: SBP trId id propertyType property
 *
 * @author Daniel Henninger
 */
public class OutgoingSBP extends MsnOutgoingMessage {

    public OutgoingSBP(MsnProtocol protocol) {
        super(protocol);
        setCommand("SBP");
    }

    public void setId(String id) {
        setParam(0, id);
    }

    public void setPropertyType(MsnUserPropertyType propertyType) {
        setParam(1, propertyType.toString());
    }

    public void setProperty(String property) {
        setParam(2, property);
    }
}
