package org.ugr.bluerose.messages;

import org.ugr.bluerose.Identity;

/**
* Header for a requesting message. 
* Definition taken from ICE (zeroc.com) for compatibility reasons.
*
* @author Carlos Rodriguez Dominguez
* @date 07-10-2009
*/
public class RequestMessageHeader extends MessageHeader {

    public int requestId;

    /**< Identifier of the request */
    public Identity identity;

    /**< Identity of the servant */
    public java.util.Vector<String> facet;

    /**< Facet of the servant */
    public String operation;

    /**< Requested operation */
    public byte mode;

    /**< Operation mode */
    public java.util.Dictionary<String, String> context;

    /** < Context of the message */
    public RequestMessageHeader() {
        super();
        messageType = MessageHeader.REQUEST_MSG;
        requestId = 0;
        identity = new Identity();
        facet = new java.util.Vector<String>();
        context = new java.util.Hashtable<String, String>();
        ;
        mode = MessageHeader.TWOWAY_MODE;
    }

    @Override
    public java.util.Vector<Byte> getBytes() {
        java.util.Vector<Byte> bytes = super.getBytes();
        java.util.Vector<Byte> result = null;
        synchronized (mutex) {
            writer.writeRawBytes(bytes);
            writer.writeInteger(requestId);
            writer.writeString(identity.id_name);
            writer.writeString(identity.category);
            writer.writeStringSeq(facet);
            writer.writeString(operation);
            writer.writeByte(mode);
            writer.writeSize(context.size());
            java.util.Enumeration<String> e = context.keys();
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                String value = context.get(key);
                writer.writeString(key);
                writer.writeString(value);
            }
            result = writer.toVector();
            writer.reset();
        }
        return result;
    }
}
