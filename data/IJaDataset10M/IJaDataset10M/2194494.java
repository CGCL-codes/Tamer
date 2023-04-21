package issrg.utils.webdav;

/**
 * The WebDAV LOCK method requests a resource be locked, returns a lock token on
 * success used to unlock the resource, or let the client specify a time-out 
 * value in seconds.
 * @author Sean Antony
 * @version 19/03/2007
 */
public class LOCK extends HTTPMessage {

    private int timeout;

    private String lockOwner;

    /**
	 * @param socket inherited from HTTPMessage
	 * @param server inherited from HTTPMessage
	 * @param port inherited from HTTPMessage
	 * @param URI inherited from HTTPMessage, in this case should specify the
	 * location of the resource/collection you want to lock.
	 * @param timeout for the lock specified in seconds
	 * @param lockOwner the name of the lock owner, could be an email address
	 */
    public LOCK(WebDAVSocket socket, String server, int port, String URI, int timeout, String lockOwner) {
        super(socket, server, port, "LOCK", URI);
        this.lockOwner = lockOwner;
        this.timeout = timeout;
    }

    /**
	 * Completes the request message with an XML encoded header specifying the
	 * lock owner and timeout.
	 */
    String completeRequestMessage() {
        String entity = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + CRLF + "<D:lockinfo xmlns:D='DAV:'>" + CRLF + "<D:lockscope><D:exclusive/></D:lockscope>" + CRLF + "<D:locktype><D:write/></D:locktype>" + CRLF + "<D:owner>" + lockOwner + "</D:owner>" + CRLF + "</D:lockinfo>" + CRLF;
        String header = "Timeout: Second-" + timeout + CRLF + "Depth: 0" + CRLF + "Content-Type: text/xml; charset=\"utf-8\"" + CRLF + "Content-Length: " + entity.length() + CRLF + CRLF;
        return header + entity;
    }

    /**
	 * Completes the request message with an XML encoded header specifying the
	 * lock owner and timeout in binary.
	 */
    byte[] completeRequestMessageByte() {
        return null;
    }

    /**
	 * @return the lock token in the server response message
	 */
    public String getLockToken() {
        String stripAboveUL = "(?i)(?s)(<)(.+?)(<D:locktoken>)";
        String stripBelowUL = "(?i)(?s)(</D:locktoken>)(.+?)(</D:prop>)";
        String stripAboveName = "(?i)(?s)(.+?)(<D:href>)";
        String stripBelowName = "(?i)(?s)(</D:href>)(.+?)";
        String[] result = getResponseEntity().split(stripAboveUL);
        result = result[1].split(stripBelowUL);
        result = result[0].split(stripAboveName);
        result = result[1].split(stripBelowName);
        return result[0];
    }
}
