package javacode.net.sf.capit.replayer.model;

/**
 * A Couplet represents a packet, and the response
 * that is generated from the server based on that
 * packet request.
 * 
 * The response is usually used in protocols that
 * keep/change state (e.g. cookie sessions of HTTP )
 * to pre-process the coming packets so as they follow
 * the communication state and be valid.
 * 
 * @author Issle
 *
 */
public class Couplet {

    /**
	 * The packet request, generated from
	 * the client.
	 */
    private VirtualPacket packet;

    /**
	 * The string response generated from
	 * the server for the above packet.
	 */
    private String response;

    /**
	 * Generates a Couplet based on the input
	 * packet request with an empty response.
	 * 
	 * @param vPacket
	 */
    public Couplet(VirtualPacket vPacket) {
        this.packet = vPacket;
    }

    /**
	 * Returns the request packet of this
	 * Couplet.
	 * @return
	 */
    public VirtualPacket getRequest() {
        return packet;
    }

    /**
	 * Sets the response.
	 * 
	 * @param response
	 */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
	 * Gets the response.
	 * 
	 * @return
	 */
    public String getResponse() {
        return response;
    }
}
