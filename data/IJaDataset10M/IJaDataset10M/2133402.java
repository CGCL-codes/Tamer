package org.openymsg.legacy.network.chatroom;

import java.util.HashSet;
import java.util.Set;
import org.openymsg.legacy.network.Util;

/**
 * Represents a single chat room, either public (Yahoo owned) or private (user owned). Each room is divided into
 * multiple independent chat spaces, known as 'lobbies'.
 * 
 * Rooms are placed into a hierarchy structure of named categories. *
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 * @author S.E. Morris
 * 
 * @see YahooChatLobby
 * @see YahooChatCategory
 */
public class YahooChatRoom {

    private String name, rawName;

    private String topic;

    private long id;

    private boolean isPublic;

    private Set<YahooChatLobby> lobbies;

    public YahooChatRoom(long id, String rawName, String topic, boolean ac) {
        this.id = id;
        this.rawName = rawName;
        this.name = Util.entityDecode(this.rawName);
        this.topic = topic;
        this.isPublic = ac;
        lobbies = new HashSet<YahooChatLobby>();
    }

    /**
     * Creates a new lobby inside this room.
     * 
     * @param lobbyNumber
     *            The number of the lobby.
     */
    public YahooChatLobby createLobby(int lobbyNumber) {
        final YahooChatLobby lobby = new YahooChatLobby(rawName, id, lobbyNumber);
        addLobby(lobby);
        return lobby;
    }

    /**
     * Adds the provided lobby to this room.
     * 
     * @param lobby
     *            The lobby to add to this room.
     */
    public void addLobby(YahooChatLobby lobby) {
        lobbies.add(lobby);
    }

    /**
     * Checks if this room contains the provided lobby.
     * 
     * @param lobby
     *            The lobby to search for in this room.
     * @return ''true'' if the provided lobby exists in this room, ''false'' otherwise.
     */
    public boolean containsLobby(YahooChatLobby lobby) {
        return lobbies.contains(lobby);
    }

    /**
     * Returns all lobbies in this room.
     * 
     * @return All lobbies of this room.
     */
    public Set<YahooChatLobby> getLobbies() {
        return lobbies;
    }

    /**
     * Returns the number of lobbies in this room.
     * 
     * @return the number of lobbies in this room.
     */
    public int lobbyCount() {
        return lobbies.size();
    }

    /**
     * The 'raw' name of this room is the unescaped name, as sent by the Yahoo domain.
     * 
     * @return The 'raw' roomname.
     */
    public String getRawName() {
        return rawName;
    }

    /**
     * Returns the 'pretty-print' name of this room. If you need to identify a room on the Yahoo network, use
     * {@link #getRawName()} instead.
     * 
     * @return The (escaped) name of this room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the topic of this room.
     * 
     * @return the topic of this room.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Returns the long id of this room.
     * 
     * @return room id.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns ''true'' if this room is a public room, ''false'' if it is a private one.
     * 
     * @return ''true'' if this room is "Yahoo owned" (public) or ''false'' if this room is "user owned" (private).
     */
    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return "YahooChatRoom '" + rawName + "'";
    }
}
