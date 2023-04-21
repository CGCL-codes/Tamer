package messages.session;

import org.apache.log4j.Logger;
import server.ClientInfo;
import server.ServerGameSession;
import client.ClientGameSession;
import db.DBGameUser;

/**
 * This message represents a chat message that is sent and received in the
 * SessionLobby, which players enter after joining a session.
 * 
 * @author Steffen
 */
public class SessionChatMsg implements SessionClientMsg, SessionServerMsg {

    private Logger logger;

    private String sender;

    private String msg;

    public SessionChatMsg(String sender, String msg) {
        this.sender = sender;
        this.msg = msg;
    }

    public SessionChatMsg(String msg) {
        this("", msg);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void execute(ServerGameSession session, ClientInfo sender) {
        this.sender = sender.getName();
        session.broadcastMsg(this);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void execute(ClientGameSession session) {
        session.addChat(sender, msg);
    }
}
