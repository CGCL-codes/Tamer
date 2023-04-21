package jupiter.node;

public class SessionNode extends Node {

    /**
	 * Represents a connection, these are the only things not directly added to the DomainNode
	 * 
	 * 
	 * @param sessionid - the session id to send to, something like hank#jmvc.com@jabbify.com/123456
	 * user#domain@jabbify.com/sessionid
	 * 
	 * setup rules for session
	 */
    public SessionNode(Address userid) {
        super(userid);
        DomainNode d = GlobalNode.getDomain(userid);
        Node user = d.getNode(userid);
    }
}
