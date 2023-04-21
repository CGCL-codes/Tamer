package net.sourceforge.hlagile.hlaabstraction;

import java.nio.ByteBuffer;
import hla.rti1516e.MessageRetractionHandle;

public class MessageRetractionHandleImpl implements MessageRetractionHandle {

    /**
	 * 
	 */
    private static final long serialVersionUID = -106321484604402661L;

    private byte[] _raw = null;

    public MessageRetractionHandleImpl(String otherHandleAsString) {
        _raw = otherHandleAsString.getBytes();
    }

    public boolean equals(Object otherMRHandle) {
        return (toString().equals(otherMRHandle.toString()));
    }

    public int hashCode() {
        return _raw.hashCode();
    }

    public String toString() {
        ByteBuffer bb = ByteBuffer.wrap(_raw);
        return bb.toString();
    }
}
