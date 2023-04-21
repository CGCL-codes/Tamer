package org.moxy.irc;

import java.util.StringTokenizer;
import java.util.Vector;

public class Channel {

    private static final int MODE_TOPIC = 0;

    private static final int MODE_NOEXTMSG = 1;

    private static final int MODE_SECRET = 2;

    private static final int MODE_INVITE = 3;

    private static final int MODE_PRIVATE = 4;

    private static final int MODE_MODERATED = 5;

    private static final int MODE_LIMIT = 6;

    private static final int MODE_KEY = 7;

    private String name;

    private boolean kicked = false;

    private String topic = new String();

    private boolean modes[];

    private int limitValue;

    private String keyValue;

    private Vector ban_list;

    private IRCConnection connection;

    private ChannelListener listeners[] = new ChannelListener[0];

    private Vector nicks;

    boolean ban_list_complete;

    boolean nick_list_complete;

    /** Creates a Channel with the specified name on the specified connection.
     * @since 1.0
     * @param name The name of the channel.
     * @param connection The connection the channel in on
     */
    public Channel(String name, IRCConnection connection) {
        this.name = name;
        modes = new boolean[9];
        ban_list = new Vector();
        for (int x = 0; x < modes.length; x++) modes[x] = false;
        this.connection = connection;
        nicks = new Vector();
        ban_list_complete = false;
        nick_list_complete = false;
    }

    private ListNick getNickAt(int i) {
        return (ListNick) nicks.elementAt(i);
    }

    private String getNameAt(int i) {
        return getNickAt(i).getName();
    }

    private ListNick findNick(String name) {
        int i;
        for (i = 0; i < nicks.size(); i++) if (getNickAt(i).getName().equalsIgnoreCase(name)) return getNickAt(i);
        return null;
    }

    private boolean removeNickByName(String name) {
        ListNick nick;
        nick = findNick(name);
        if (nick == null) return false;
        nicks.removeElement(nick);
        return true;
    }

    /**
       * Sets wether the room is invite only or not.
       * @since 1.0
       * @param b true to set false to unset.
       */
    void setInviteOnly(boolean b, String chanop) {
        modes[MODE_INVITE] = b;
        for (int x = 0; x < listeners.length; x++) listeners[x].setInviteOnly(b, chanop);
    }

    /**
       * Sets wether the room is private or not.
       * @since 1.0
       * @param b true to set false to unset.
       */
    void setPrivate(boolean b, String chanop) {
        modes[MODE_PRIVATE] = b;
        for (int x = 0; x < listeners.length; x++) listeners[x].setPrivate(b, chanop);
    }

    /**
       * Sets wether the room is secret or not.
       * @since 1.0
       * @param b true to set false to unset.
       */
    void setSecret(boolean b, String chanop) {
        modes[MODE_SECRET] = b;
        for (int x = 0; x < listeners.length; x++) listeners[x].setSecret(b, chanop);
    }

    /**
       * Sets wether the room is moderated or not.
       * @since 1.0
       * @param b true to set false to unset.
       */
    void setModerated(boolean b, String chanop) {
        modes[MODE_MODERATED] = b;
        for (int x = 0; x < listeners.length; x++) listeners[x].setModerated(b, chanop);
    }

    /**
       * Sets wether external messages are allowed or not.
       * @since 1.0
       * @param b true to set false to unset.
       */
    void setNoExtMsg(boolean b, String chanop) {
        modes[MODE_NOEXTMSG] = b;
        for (int x = 0; x < listeners.length; x++) listeners[x].setNoExtMsg(b, chanop);
    }

    /**
       * Sets wether only ops can change the topic or not.
       * @since 1.0
       * @param b true to set false to unset.
       */
    void setOpTopic(boolean b, String chanop) {
        modes[MODE_TOPIC] = b;
        for (int x = 0; x < listeners.length; x++) listeners[x].setOpTopic(b, chanop);
    }

    /**
       * Sets the value of the key for the room.
       * @since 1.0
       * @param k the key.
       **/
    void setKey(String key, String chanop) {
        if (key == null) modes[MODE_KEY] = false; else {
            modes[MODE_KEY] = true;
            keyValue = key;
        }
        for (int x = 0; x < listeners.length; x++) listeners[x].setKey(key, chanop);
    }

    /**
       * Sets the limit of the number of nicks in the channel
       * at one time.
       * @since 1.0
       * @param l max the number of nicks.
       */
    void setLimit(int limit, String chanop) {
        if (limit == 0) modes[MODE_LIMIT] = false; else {
            modes[MODE_LIMIT] = true;
            limitValue = limit;
        }
        for (int x = 0; x < listeners.length; x++) listeners[x].setLimit(limit, chanop);
    }

    /**
       * Adds a nickmask to the ban list of the channel.
       * @since 1.0
       * @param mask the nick mask.
       */
    void ban(String mask, boolean mode, String chanop) {
        if (!mode) ban_list.removeElement(mask); else ban_list.addElement(mask);
        for (int x = 0; x < listeners.length; x++) listeners[x].ban(mask, mode, chanop);
    }

    /**
       * Adds a nick to the channel.
       * @since 1.0
       * @param nick the nick to add.
       */
    void join(String nick) {
        String name;
        String ident;
        String host;
        name = IRCMessage.getNick(nick);
        ident = IRCMessage.getUser(nick);
        host = IRCMessage.getHost(nick);
        nicks.addElement(new ListNick(name, ident, host, false, false));
        for (int x = 0; x < listeners.length; x++) listeners[x].join(name, ident, host);
    }

    /**
       * Removes a nick from the channel.
       * @since 1.0
       * @param nick the nick to remove.
       */
    void part(String name, String ident, String host, String msg) {
        if (!removeNickByName(name)) return;
        for (int x = 0; x < listeners.length; x++) listeners[x].part(name, ident, host, msg);
    }

    /**
       * Removes a nick from the channel.
       * @since 1.0
       * @param nick the nick to remove.
       */
    void quit(String name, String ident, String host, String msg) {
        if (!removeNickByName(name)) return;
        for (int x = 0; x < listeners.length; x++) listeners[x].quit(name, ident, host, msg);
    }

    /**
       * Changes the nick of a nick.
       * @since 1.0
       * @param oldNick the old nick to look up.
       * @param newNick the new nick to change it to.
       */
    void nickChange(String oldName, String newName) {
        ListNick nick;
        if (!isHere(oldName)) {
            return;
        }
        nick = findNick(oldName);
        if (nick == null) return;
        nick.setName(newName);
        for (int x = 0; x < listeners.length; x++) listeners[x].nickChange(oldName, newName);
    }

    /**
       * Removes a nick from the channel.
       * @since 1.0
       * @param nick the nick to remove.
       */
    void kick(String name, String reason, String chanop) {
        if (!removeNickByName(name)) return;
        for (int x = 0; x < listeners.length; x++) listeners[x].kick(name, reason, chanop);
    }

    /**
       * Ops or Deops a nick in the channel.
       * @since 1.0
       * @param nick the nick to op or deop.
       * @param mode true to op false to deop.
       */
    void op(String name, boolean mode, String chanop) {
        ListNick nick;
        nick = findNick(name);
        if (nick == null) return;
        nick.setChanop(mode);
        for (int x = 0; x < listeners.length; x++) listeners[x].op(name, mode, chanop);
    }

    /**
       * Voices or devoices a nick in the channel.
       * @since 1.0
       * @param nick the nick to voice or devoice.
       * @param mode true to voice false to devoice.
       */
    void voice(String name, boolean mode, String chanop) {
        ListNick nick;
        nick = findNick(name);
        if (nick == null) return;
        nick.setVoice(mode);
        for (int x = 0; x < listeners.length; x++) listeners[x].voice(name, mode, chanop);
    }

    void handleMsg(String sender, IRCLine message) {
        int i;
        for (i = 0; i < listeners.length; i++) listeners[i].handleMessage(sender, message);
    }

    void handleNotice(String sender, IRCLine message) {
        int i;
        for (i = 0; i < listeners.length; i++) listeners[i].handleNotice(sender, message);
    }

    void handleCTCPMsg(String sender, IRCLine message) {
        int i;
        String command;
        command = message.getNextToken();
        if (command.equals("ACTION")) for (i = 0; i < listeners.length; i++) listeners[i].handleAction(sender, message); else {
            message.putBack(command);
            for (i = 0; i < listeners.length; i++) listeners[i].handleCTCPMessage(sender, message);
        }
    }

    void addInitialNick(ListNick nick) {
        nicks.addElement(nick);
    }

    void initialNickListComplete() {
        int i;
        nick_list_complete = true;
        for (i = 0; i < listeners.length; i++) listeners[i].initialNickList(nicks);
    }

    void initialTopic(String topic) {
        int i;
        this.topic = topic;
        for (i = 0; i < listeners.length; i++) listeners[i].initialTopic(topic);
    }

    void initialOpTopic(boolean mode) {
        int i;
        modes[MODE_TOPIC] = mode;
        for (i = 0; i < listeners.length; i++) listeners[i].initialOpTopic(mode);
    }

    void initialNoExtMsg(boolean mode) {
        int i;
        modes[MODE_NOEXTMSG] = mode;
        for (i = 0; i < listeners.length; i++) listeners[i].initialNoExtMsg(mode);
    }

    void initialSecret(boolean mode) {
        int i;
        modes[MODE_SECRET] = mode;
        for (i = 0; i < listeners.length; i++) listeners[i].initialSecret(mode);
    }

    void initialInviteOnly(boolean mode) {
        int i;
        modes[MODE_INVITE] = mode;
        for (i = 0; i < listeners.length; i++) listeners[i].initialInviteOnly(mode);
    }

    void initialPrivate(boolean mode) {
        int i;
        modes[MODE_PRIVATE] = mode;
        for (i = 0; i < listeners.length; i++) listeners[i].initialPrivate(mode);
    }

    void initialModerated(boolean mode) {
        int i;
        modes[MODE_MODERATED] = mode;
        for (i = 0; i < listeners.length; i++) listeners[i].initialModerated(mode);
    }

    void initialLimit(boolean mode, int limit) {
        int i;
        modes[MODE_LIMIT] = mode;
        if (mode) this.limitValue = limit;
        for (i = 0; i < listeners.length; i++) listeners[i].initialLimit(mode, limit);
    }

    void initialKey(boolean mode, String key) {
        int i;
        modes[MODE_KEY] = mode;
        if (mode) this.keyValue = key;
        for (i = 0; i < listeners.length; i++) listeners[i].initialKey(mode, key);
    }

    void addInitialBan(String mask) {
        ban_list.addElement(mask);
    }

    void initialBanListComplete() {
        int i;
        ban_list_complete = true;
        for (i = 0; i < listeners.length; i++) listeners[i].initialBan(ban_list);
    }

    /**
       * Returns the connection wich the room is on.
       * @since 1.0
       * @return IRCConnection the room is on.
       */
    public IRCConnection getConnection() {
        return connection;
    }

    /**
      * Returns the name of the room.
      * @ since 1.0
      * @return the name of the room.
      */
    public String getName() {
        return name;
    }

    /**
       * Returns wether the local user has been kicked from
       * the room or not.
       * @since 1.0
       * @return True if the users has been kicked and is no longer
       *   in the room false otherwise.
       */
    public boolean getKicked() {
        return kicked;
    }

    /**
       * Returns the current topic for the room.
       * @since 1.0
       * @return the topic of the room
       */
    public String getTopic() {
        return topic;
    }

    /**
       * Returns a Vector of all the bans the channel currently has.
       * @since 1.0
       * @return the ban list.
       **/
    public Vector getBanList() {
        return ban_list;
    }

    /**
       * Returns wether the channel is invite only or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getInviteOnly() {
        return modes[0];
    }

    /**
       * Returns wether the channel has a limit or not.
       * @since 1.0
       * @return true if it does false otherwise.
       */
    public boolean getLimit() {
        return modes[1];
    }

    /**
       * Returns wether the channel is private or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getPrivate() {
        return modes[2];
    }

    /**
       * Returns wether the channel is secret or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getSecret() {
        return modes[3];
    }

    /**
       * Returns wether the channel has a key or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getKey() {
        return modes[4];
    }

    /**
       * Returns wether the channel is moderated or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getModerated() {
        return modes[5];
    }

    /**
       * Returns wether external messages are allowed or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getNoExtMsg() {
        return modes[6];
    }

    /**
       * Returns wether only ops can change the topic or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getOpTopic() {
        return modes[7];
    }

    /**
       * Returns wether the channel is registered or not.
       * @since 1.0
       * @return true if it is false otherwise.
       */
    public boolean getRegistered() {
        return modes[8];
    }

    /**
       * Returns the maximum number of nicks allowed in the channel
       * if the limit value is set.
       * @since 1.0
       * @return the max number of nicks allowed in the channel.
       **/
    public int getLimitValue() {
        return limitValue;
    }

    /**
       * Returns the key of the channel if one is set.
       * @since 1.0
       * @return the value of the key for the room.
       **/
    public String getKeyValue() {
        return keyValue;
    }

    /**
       * Returns all the nicks in the room.
       * @since 1.0
       * @return the nicks in the room.
       */
    public Vector getNames() {
        Vector names;
        int i;
        names = new Vector();
        for (i = 0; i < nicks.size(); i++) names.addElement(getNameAt(i));
        return names;
    }

    /**
	 * Get an array of all the people on this channel.
	 * @returns an array of ListNick objects, one ListNick for every user on the channel.
	 */
    public ListNick[] getNickList() {
        ListNick[] nickArray;
        nickArray = new ListNick[nicks.size()];
        nicks.copyInto(nickArray);
        return nickArray;
    }

    /**
       * Returns all the ops in the channel.
       * @since 1.0
       * @return the nicks of the ops in the channel.
       */
    public Vector getOpList() {
        Vector opList;
        ListNick nick;
        int i;
        opList = new Vector();
        for (i = 0; i < nicks.size(); i++) {
            nick = getNickAt(i);
            if (nick.isChanop()) opList.addElement(nick.getName());
        }
        return opList;
    }

    /**
       * Returns all the voices in the channel.
       * @since 1.0
       * @return the nicks of the voices in the channel.
       */
    public Vector getVoiceList() {
        Vector voiceList;
        ListNick nick;
        int i;
        voiceList = new Vector();
        for (i = 0; i < nicks.size(); i++) {
            nick = getNickAt(i);
            if (nick.hasVoice()) voiceList.addElement(nick.getName());
        }
        return voiceList;
    }

    /**
       * Returns all the non op's non voices in the channel.
       * @since 1.0
       * @return the nicks that aren't ops or voices.
       */
    public Vector getRegList() {
        Vector regList;
        ListNick nick;
        int i;
        regList = new Vector();
        for (i = 0; i < nicks.size(); i++) {
            nick = getNickAt(i);
            if (!nick.isChanop() && !nick.hasVoice()) regList.addElement(nick.getName());
        }
        return regList;
    }

    /**
       * Sets a specified mode.
       * @since 1.0
       * @param mode the mode to set.
       * @param type true to set it on or false to set it off.
       */
    public void setOtherMode(char mode, boolean type, String chanop) {
        for (int x = 0; x < listeners.length; x++) listeners[x].setOtherMode(mode, type, chanop);
    }

    /**
       * Sets the topic for the room.
       **/
    public void setTopic(String topic, String chanop) {
        this.topic = topic;
        for (int x = 0; x < listeners.length; x++) listeners[x].setTopic(topic, chanop);
    }

    /**
       * Returns wether a nick is an op in the channel or not.
       * @since 1.0
       * @param nick the nick to look up
       * @return true if the nick is an op false otherwise
       */
    public boolean isOp(String name) {
        ListNick nick;
        nick = findNick(name);
        if (nick == null) return false;
        return nick.isChanop();
    }

    /**
       * Returns wether a nick is a voice in the channel or not.
       * @since 1.0
       * @param nick the nick to look up.
       * @param true if the nick is a voice, false otherwise.
       */
    public boolean isVoice(String name) {
        ListNick nick;
        nick = findNick(name);
        if (nick == null) return false;
        return nick.hasVoice();
    }

    /**
       * Returns wether a nick is NOT an op or a voice.
       * @since 1.0
       * @param nick the nick to look up.
       * @return true if the nick is not an op or a voice, false otherwise.
       */
    public boolean isReg(String name) {
        ListNick nick;
        nick = findNick(name);
        if (nick == null) return false;
        return !nick.isChanop() && !nick.hasVoice();
    }

    /**
       * Returns wether the specified nick is on the channel or not
       * @since 1.0
       * @param nick the nick to search for.
       * @return true if the nick is on the channel false otherwise.
       */
    public boolean isHere(String name) {
        return findNick(name) != null;
    }

    public void addListener(ChannelListener cl) {
        ChannelListener newlisteners[] = new ChannelListener[listeners.length + 1];
        for (int x = 0; x < listeners.length; x++) newlisteners[x] = listeners[x];
        newlisteners[newlisteners.length - 1] = cl;
        listeners = null;
        listeners = newlisteners;
        if (ban_list_complete) cl.initialBan(ban_list);
        if (nick_list_complete) cl.initialNickList(nicks);
        if (!topic.equals("")) cl.initialTopic(topic);
        if (modes[0]) cl.initialOpTopic(true);
        if (modes[1]) cl.initialNoExtMsg(true);
        if (modes[2]) cl.initialSecret(true);
        if (modes[3]) cl.initialInviteOnly(true);
        if (modes[4]) cl.initialPrivate(true);
        if (modes[5]) cl.initialModerated(true);
        if (modes[6]) cl.initialLimit(true, limitValue);
        if (modes[7]) cl.initialKey(true, keyValue);
    }

    public void removeListener(ChannelListener cl) {
        int index = -1;
        for (int x = 0; x < listeners.length; x++) if (listeners[x] == cl) {
            index = x;
            break;
        }
        if (index == -1) return;
        listeners[index] = null;
        ChannelListener newlisteners[] = new ChannelListener[listeners.length - 1];
        for (int x = 0; x < index; x++) newlisteners[x] = listeners[x];
        for (int x = index + 1; x < listeners.length; x++) newlisteners[x - 1] = listeners[x];
        listeners = null;
        listeners = newlisteners;
    }
}
