package br.guj.chat.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import br.guj.chat.model.room.Room;
import br.guj.chat.model.room.RoomException;
import br.guj.chat.model.server.ChatServer;
import br.guj.chat.user.GujChatUser;

/**
 * A session wrapper
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.4 $, $Date: 2003/04/07 15:36:25 $
 */
public class Session {

    /** the session object */
    HttpSession session = null;

    /**
	 * Requests the session from a request object
	 */
    public Session(HttpServletRequest req) {
        session = req.getSession(true);
    }

    /**
	 * Returns the user connected to this room
	 * @param room				room
	 * @return GujChatUser		the user or null if not in the room
	 */
    public GujChatUser getUser(ChatServer instance, Room room) throws RoomException {
        String att = "user." + instance.getID() + "." + room.getID();
        GujChatUser o = (GujChatUser) session.getAttribute(att);
        if (o == null) {
            throw new RoomException(instance, "exception.notloggedin");
        }
        return o;
    }

    /**
	 * Returns whether this session is a new one
	 */
    public boolean isNew() {
        return session.isNew();
    }
}
