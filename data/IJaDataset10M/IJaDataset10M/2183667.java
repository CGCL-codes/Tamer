package net.sf.jml.protocol.outgoing;

import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Change the default list for people who aren't explicitly 
 * allowed or blocked.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: BLP trId AL|BL
 * 
 * @author Roger Chen
 */
public class OutgoingBLP extends MsnOutgoingMessage {

    public OutgoingBLP(MsnProtocol protocol) {
        super(protocol);
        setCommand("BLP");
        setOnlyNotifyAllowList(true);
    }

    public void setOnlyNotifyAllowList(boolean b) {
        setParam(0, b ? "BL" : "AL");
    }
}
