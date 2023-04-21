package tuwien.auto.eicl.struct.eibnetip;

import tuwien.auto.eicl.struct.eibnetip.util.HPAI;
import tuwien.auto.eicl.util.*;

/**
 * <p>
 * This class implements the EIBnet/IP search response message. This message is
 * sent in replie to a Search Response.
 * 
 * @see tuwien.auto.eicl.struct.eibnetip.Search_Request
 * @author Bernhard Erb
 */
public class Search_Response {

    private HPAI discovered_endpoint;

    /**
     * Initializes the object by parasing a byte array. Throws EICLException if
     * the parsing didn't succeed.
     * 
     * @param _Body
     *            The EIBnet/IP message body (after the EIBnet/IP header)
     * @throws EICLException
     *             Forward the HPAI constructor Exception.
     */
    public Search_Response(byte[] _Body) throws EICLException {
        discovered_endpoint = new HPAI(_Body);
    }

    /**
     * Returns the end point encapsulated in the search response
     * 
     * @return the discovered endpoint
     */
    public HPAI getDiscoveredEndpoint() {
        return discovered_endpoint;
    }
}
