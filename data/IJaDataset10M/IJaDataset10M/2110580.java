package br.guj.chat.model.room;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import br.guj.chat.dao.DAOException;
import br.guj.chat.model.message.Message;
import br.guj.chat.model.message.SystemMessage;
import br.guj.chat.model.server.ChatServer;
import br.guj.chat.user.GujChatUser;
import br.guj.chat.user.User;

/**
 * A moderated room
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.12 $, $Date: 2003/10/27 11:20:53 $
 */
public class ModeratedRoom extends HtmlChatRoom {

    private int msgCount;

    private String moderatorName;

    private String moderatorUsername;

    private String moderatorPassword;

    private String link = getServerID() + "?page=br.guj.chat.model.room.ModeratedRoom/users.vm&room=" + getID() + "&service=br.guj.chat.plugin.room.moderated.service.ManageMessages&do";

    private ArrayList bannedIPS = new ArrayList();

    /** whether the room is allowing new users to enter */
    private boolean allowsNewUsers = true;

    private br.guj.chat.model.readonly.Room readOnly = new ModeratedReadOnly(this);

    private ArrayList propertiesPages = new ArrayList();

    /** if the moderator is connected, he is linked here */
    private User moderator = null;

    /** list of messages in the queue */
    private HashMap messages = new HashMap();

    /**
	 * Basic moderation room
	 * @param server	the server
	 * @param id		the id
	 */
    public ModeratedRoom(ChatServer server, String id) {
        super(server, id);
        setModeratorName(id);
        setModeratorUsername(id);
        setModeratorPassword(id);
    }

    /**
	 * Returns the moderator name
	 * @return	the name
	 */
    public String getModeratorName() {
        return moderatorName;
    }

    /**
	 * Returns the password
	 * @return	password
	 */
    public String getModeratorPassword() {
        return moderatorPassword;
    }

    /**
	 * Returns the username
	 * @return	the username
	 */
    public String getModeratorUsername() {
        return moderatorUsername;
    }

    /**
	 * Sets the new name
	 * @param name	the name
	 */
    public void setModeratorName(String name) {
        moderatorName = name;
    }

    /**
	 * Sets the new password
	 * @param string	the password
	 */
    public void setModeratorPassword(String pass) {
        moderatorPassword = pass;
    }

    /**
	 * Sets the new username
	 * @param string	the username
	 */
    public void setModeratorUsername(String user) {
        moderatorUsername = user;
    }

    /**
	 * Copies this room to a new one
	 * @param id	the new id
	 * @see br.guj.chat.model.room.Room#copy(java.lang.String)
	 */
    public Room copy(String id) throws DAOException {
        ModeratedRoom room = new ModeratedRoom(getServer(), getID());
        room.setName(getName());
        room.setDescription(getDescription());
        if (!this.getOptions().isUseDefaultActionGroup()) {
            room.setActionGroup(this.getActionGroup().getID());
        } else {
            room.setActionGroup(null);
        }
        room.setAllowsSimilarNames(this.isAllowsSimilarNames());
        room.setAllowsGUJTags(this.isAllowsGUJTags());
        room.setAllowsHtml(this.isAllowsHtml());
        room.setAllowsImageTags(this.isAllowsImageTags());
        room.setAllowsLinkTags(this.isAllowsLinkTags());
        room.getOptions().setMaximumUsers(this.getOptions().getMaximumUsers());
        if (this.getPassword() != null) {
            room.setPassword(this.getPassword());
        }
        if (this.usesDefaultTopFrame()) {
            room.usesDefaultTopFrame(true);
        } else {
            room.setTopFrame(this.getTopFrame());
        }
        room.setLanguage(getLanguage());
        room.setModeratorName(getModeratorName());
        room.setModeratorPassword(getModeratorPassword());
        room.setModeratorUsername(getModeratorUsername());
        return room;
    }

    /**
	 * Exports the data to a properties object
	 * @see br.guj.chat.model.room.Room#exportTo(java.lang.String, java.util.Properties)
	 */
    public void exportTo(String str, Properties p) throws DAOException {
        super.exportTo(str, p);
        p.setProperty(str + "Moderator.Name", getModeratorName());
        p.setProperty(str + "Moderator.Password", getModeratorPassword());
        p.setProperty(str + "Moderator.Username", getModeratorUsername());
    }

    /**
	 * Imports the data from a properties object
	 * @see br.guj.chat.model.room.Room#importFrom(java.util.Properties)
	 */
    public void importFrom(Properties p) throws DAOException {
        super.importFrom(p);
        setModeratorName(p.getProperty("Moderator.Name"));
        setModeratorUsername(p.getProperty("Moderator.Username"));
        setModeratorPassword(p.getProperty("Moderator.Password"));
    }

    /**
	 * Sends a message to the queue
	 * @see br.guj.chat.model.room.Room#addMessage(br.guj.chat.model.message.Message)
	 */
    public void addMessage(Message msg) throws InvalidMessageException {
        if (msg instanceof SystemMessage || msg.getTo() != null || msg.getFrom().equals(getModerator())) {
            super.addMessage(msg);
        } else {
            int msgID = ++msgCount;
            writeModeratorMessage(msg, msgID);
            synchronized (messages) {
                messages.put(msgCount + "", msg);
            }
        }
    }

    /**
	 * Returns the moderator or null if he is not connected
	 * @return	the moderator
	 */
    public User getModerator() {
        return moderator;
    }

    /**
	 * Sets the user moderator
	 * @param user	the moderator user
	 */
    public void setModerator(User user) {
        moderator = user;
    }

    /**
	 * Approves a message from the queue
	 * @param i the number
	 */
    public void approveMessage(String id) throws InvalidMessageException {
        synchronized (messages) {
            super.addMessage((Message) messages.remove(id));
        }
    }

    /**
	 * Removes a message from the queue
	 * @param i the number
	 */
    public void removeMessage(String i) {
        synchronized (messages) {
            messages.remove(i);
        }
    }

    /**
	 * Returns a list of extra properties pages
	 * @return	the array list
	 */
    public ArrayList getExtraPropertiesPages() {
        return propertiesPages;
    }

    /**
	 * Returns a read only version of this room
	 * @return the read only object
	 */
    public br.guj.chat.model.readonly.Room getReadOnly() {
        return readOnly;
    }

    {
        propertiesPages.add("Moderator");
    }

    /**
	 * Whether this room is allowing new users
	 * @return true or false
	 */
    public boolean isAllowsNewUsers() {
        return allowsNewUsers;
    }

    /**
	 * Sets whether this room is allowing new users
	 * @param b true or false
	 */
    public void setAllowsNewUsers(boolean b) {
        allowsNewUsers = b;
    }

    /**
	 * Tries to add an user to this room
	 * @param user
	 */
    public void addUser(GujChatUser user, HttpServletRequest req) throws RoomException {
        if (user.getName().equals(getModeratorUsername())) {
            if (req.getParameter("moderator.password") == null || !req.getParameter("moderator.password").equals(getModeratorPassword())) {
                throw new RoomException(getServer(), "exception.plugins.room.ModeratedRoom.InvalidPassword");
            }
            moderator = user;
            super.addUser(user, req);
        } else {
            if (!isAllowsNewUsers()) {
                throw new RoomException(server, "exception.plugins.room.ModeratedRoom.NoNewUsersAreAllowed");
            }
            for (int i = 0; i != bannedIPS.size(); i++) {
                String bannedIP = (String) bannedIPS.get(i);
                bannedIP = starPattern.matcher(bannedIP).replaceAll("[\\\\p{Alnum}]+");
                bannedIP = pointPattern.matcher(bannedIP).replaceAll("\\\\.");
                Pattern pattern = Pattern.compile(bannedIP);
                String value = pattern.matcher(user.getIp()).replaceAll("TRUE");
                if (value.equals("TRUE")) {
                    throw new RoomException(server, "exception.plugins.room.ModeratedRoom.BannedIP");
                }
            }
            super.addUser(user, req);
        }
    }

    public class ModeratedReadOnly extends br.guj.chat.model.readonly.Room {

        ModeratedReadOnly(ModeratedRoom room) {
            super(room);
        }

        public User getModerator() {
            return moderator;
        }

        public String getModeratorUsername() {
            return moderatorUsername;
        }

        public boolean isAllowsNewUsers() {
            return ModeratedRoom.this.isAllowsNewUsers();
        }
    }

    /**
	 * The moderator might ban an user for this room
	 * @param ip
	 */
    public void banIP(String ip) {
        bannedIPS.add(ip);
    }

    private static Pattern starPattern = Pattern.compile("\\*");

    private static Pattern pointPattern = Pattern.compile("\\.");

    /**
	 * Removed the list of banned ips 
	 */
    public void cleanBannedIPs() {
        bannedIPS.clear();
    }

    /**
	 * Removes all users from this room
	 */
    public void removeAllUsers() {
        ArrayList users = getUsers();
        for (int i = 0; i != users.size(); i++) {
            try {
                if (!getModerator().equals(users.get(i))) {
                    removeUser((User) users.get(i));
                }
            } catch (Exception e) {
            }
        }
    }

    /**
	 * Writes the message to the user
	 */
    protected void writeModeratorMessage(Message msg, int msgID) {
        try {
            if (moderator != null) {
                String message = "&message=" + msgID;
                ServletOutputStream writer = getOutputStream(moderator);
                writer.println(parseMessageText(moderator, msg) + "(<a target='users' href='" + link + "=approve" + message + "'>approve</a> | <a target='users' href='" + link + "=disapprove" + message + "'>disapprove</a>)<br>");
                writer.flush();
            }
        } catch (SocketException sockex) {
            this.removeUser(moderator);
        } catch (IOException sockex) {
            this.removeUser(moderator);
        }
    }

    /**
	 * Cleans the entire message queue
	 */
    public void cleanMessageQueue() {
        messages.clear();
    }

    /**
	 * Displays the entire message queue for the moderator
	 */
    public void displayMessageQueue() {
        synchronized (messages) {
            Iterator i = messages.keySet().iterator();
            while (i.hasNext()) {
                String id = (String) i.next();
                writeModeratorMessage((Message) messages.get(id), Integer.parseInt(id));
            }
        }
    }
}
