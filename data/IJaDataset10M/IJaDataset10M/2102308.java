package com.agentfactory.platform.mts;

import com.agentfactory.platform.core.AgentID;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Implementation of a Frame as specified in the FIPA ALPHAAgent Message
 * Transport Service Standard.
 *
 * @author  Rem Collier
 */
public abstract class Envelope {

    protected ArrayList to;

    protected AgentID from;

    protected String aclRepresentation;

    protected Calendar date;

    protected String payloadEncoding;

    protected long payloadLength;

    protected ArrayList intendedReceiver;

    public void setIntendedReceiver(ArrayList intendedReceiver) {
        this.intendedReceiver = intendedReceiver;
    }

    public ArrayList getIntendedReceiver() {
        return intendedReceiver;
    }

    public void setTo(ArrayList to) {
        this.to = to;
    }

    public ArrayList getTo() {
        return to;
    }

    public void setFrom(AgentID from) {
        this.from = from;
    }

    public AgentID getFrom() {
        return from;
    }

    public void setAclRepresentation(String aclRepresentation) {
        this.aclRepresentation = aclRepresentation;
    }

    public String getAclRepresentation() {
        return aclRepresentation;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

    /** Getter for property payloadEncoding.
     * @return Value of property payloadEncoding.
     */
    public java.lang.String getPayloadEncoding() {
        return payloadEncoding;
    }

    /** Setter for property payloadEncoding.
     * @param payloadEncoding New value of property payloadEncoding.
     */
    public void setPayloadEncoding(java.lang.String payloadEncoding) {
        this.payloadEncoding = payloadEncoding;
    }

    /** Getter for property payloadLength.
     * @return Value of property payloadLength.
     */
    public long getPayloadLength() {
        return payloadLength;
    }

    /** Setter for property payloadLength.
     * @param payloadLength New value of property payloadLength.
     */
    public void setPayloadLength(long payloadLength) {
        this.payloadLength = payloadLength;
    }

    public String getLogMessage() {
        StringBuffer buf = new StringBuffer();
        buf.append("From: ").append(from.toFOSString()).append(" To: [");
        boolean first = true;
        for (int i = 0; i < to.size(); i++) {
            if (first) {
                first = false;
            } else {
                buf.append(",");
            }
            buf.append(((AgentID) to.get(i)).toFOSString());
        }
        buf.append("]");
        return buf.toString();
    }
}
