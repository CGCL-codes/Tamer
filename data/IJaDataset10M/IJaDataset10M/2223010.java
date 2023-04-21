package net.jini.discovery;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Encapsulate the details of marshaling a unicast request.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @see IncomingUnicastRequest
 */
public class OutgoingUnicastRequest {

    /**
	 * The current version of the unicast discovery protocol.
	 */
    protected static final int protoVersion = 1;

    /**
	 * Marshal a unicast request to the given output stream. The stream is
	 * flushed afterwards.
	 * 
	 * @param str
	 *            the stream to marshal to
	 * @exception IOException
	 *                a problem occurred during marshaling
	 */
    public static void marshal(OutputStream str) throws IOException {
        DataOutputStream dstr = new DataOutputStream(str);
        dstr.writeInt(protoVersion);
        dstr.flush();
    }
}
