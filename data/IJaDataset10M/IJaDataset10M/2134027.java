package tuwien.auto.eicl.struct.eibnetip;

import java.io.*;
import tuwien.auto.eicl.struct.eibnetip.util.EIBNETIP_Constants;
import tuwien.auto.eicl.struct.eibnetip.util.HPAI;
import tuwien.auto.eicl.util.*;

/**
 * <p>
 * This class is used to create an EIBnet/IP description request message. The
 * byte stream representation can be obtained through the toByteArray() method.
 * In reply to this message a description response is sent. Use the Discoverer
 * class to automate this process.
 * 
 * @see tuwien.auto.eicl.struct.eibnetip.Description_Response
 * @see tuwien.auto.eicl.Discoverer
 * @author Bernhard Erb
 */
public class Description_Request {

    private HPAI endpoint;

    /**
     * Creates a new Description request message, with the local port parameter.
     * 
     * @param _LocalPort
     *            The local client data end point port.
     * @throws EICLException
     *             Forwards the HPAI exception.
     * @see HPAI
     */
    public Description_Request(int _LocalPort) throws EICLException {
        endpoint = new HPAI(_LocalPort);
    }

    /**
     * Get the message as byte array.
     * 
     * @return The message as byte array
     * @throws EICLException
     *             Forwards the IOException.
     */
    public byte[] toByteArray() throws EICLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            EIBnetIPPacket header = new EIBnetIPPacket(EIBNETIP_Constants.DESCRIPTION_REQUEST, (char) (EIBNETIP_Constants.HEADER_SIZE_10 + endpoint.getStructLength()));
            baos.write(header.toByteArray());
            baos.write(endpoint.toByteArray());
        } catch (IOException ex) {
            throw new EICLException(ex.getMessage());
        }
        return baos.toByteArray();
    }

    /**
     * Get the client end point
     * 
     * @return client end point
     */
    public HPAI getEndpoint() {
        return endpoint;
    }
}
