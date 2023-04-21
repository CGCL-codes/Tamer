package issrg.utils.webdav;

/**
 * The WebDAV PROPPATCH method requests to set and/or remove properties, 
 * specified on the request body, defined on the resource.
 * @author Sean Antony
 * @version 19/03/2007
 */
public class PROPPATCH extends HTTPMessage {

    private String entityBody;

    /**
	 * @param socket inherited from HTTPMessage
	 * @param server inherited from HTTPMessage
	 * @param port inherited from HTTPMessage
	 * @param URI inherited from HTTPMessage, in this case should specify the
	 * location of the resource/collection you want to apply properties to
	 * @param entityBody the XML encoded message specifying what properties to
	 * apply
	 */
    public PROPPATCH(WebDAVSocket socket, String server, int port, String URI, String entityBody) {
        super(socket, server, port, "PROPPATCH", URI);
        this.entityBody = entityBody;
    }

    /**
	 * Completes the request message with an XML encoded header specifying the
	 * properties to apply.
	 */
    String completeRequestMessage() {
        String header = "Content-Type: text/xml; charset=\"utf-8\"" + CRLF + "Content-Length: " + entityBody.length() + CRLF + CRLF;
        return header + entityBody;
    }

    /**
	 * Completes the request message with an XML encoded header specifying the
	 * properties to apply in binary.
	 */
    byte[] completeRequestMessageByte() {
        return null;
    }
}
