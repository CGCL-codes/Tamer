package net.sourceforge.libairc;

import java.util.*;

/**
 * Abstracts a channel and all of the operations that can be performed on one
 * 
 * @author alx
 * @author p-static
 */
public class Channel {

    /**
	 * Client who joined this channel
	 */
    protected Client client;

    /**
	 * Channel name ( including # )
	 */
    protected String name;

    /**
	 * Users currently in the channel
	 */
    protected Map users;

    protected boolean amOp;

    protected boolean amHop;

    protected boolean amVoice;

    protected List ops;

    protected List hops;

    protected List voices;

    protected String myModes;

    public static final int MODE_OPERATOR = 3;

    public static final int MODE_HALFOP = 2;

    public static final int MODE_VOICE = 1;

    public static final int MODE_NONE = 0;

    /**
	 * Have we received names yet?
	 */
    private boolean names;

    /**
	 * Constructor - create the channel
	 *
	 * @param client the client
	 * @param name the channel name with #
	 */
    public Channel(Client client, String name) {
        super();
        this.client = client;
        this.name = name;
        users = new HashMap();
        ops = new ArrayList();
        hops = new ArrayList();
        voices = new ArrayList();
        names = false;
        amOp = false;
        amHop = false;
        amVoice = false;
    }

    /**
	 * Get the channel name
	 *
	 * @return the channel name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Set the channel name
	 *
	 * @param name the new channel name
	 */
    protected void setName(String name) {
        this.name = name;
    }

    /**
	 * Sends a message to the channel at the given priority level
	 *
	 * @param message text of the message
	 * @param priority level of priority of the message
	 */
    public void sendMessage(String message, int priority) {
        client.sendRaw("PRIVMSG " + name + " :" + message, priority);
    }

    /**
	 * Sends an action to the channel at the given priority level
	 *
	 * @param action text of the action
	 * @param priority level of priority of the action
	 */
    public void sendAction(String action, int priority) {
        client.sendRaw("PRIVMSG " + name + " :" + libairc.CTCP + "ACTION " + action + libairc.CTCP, priority);
    }

    /**
	 * Sends a notice to the channel at the given priority level
	 *
	 * @param notice text of the notice
	 * @param priority level of priority of the notice
	 */
    public void sendNotice(String notice, int priority) {
        client.sendRaw("NOTICE " + name + " :" + notice, priority);
    }

    /**
	 * Sends a notice to the channel operators at the given priority level
	 *
	 * @param notice text of the notice
	 * @param priority level of priority of the notice
	 */
    public void sendOpNotice(String notice, int priority) {
        client.sendRaw("NOTICE @" + name + " :" + notice, priority);
    }

    /**
	 * Sends a block message to the channel at the given priority level
	 * Note: to intermix regular messages and actions, use libairc.makeAction( String text )
	 * 
	 * @param messages list of messages
	 * @param priority level of priority of the block
	 */
    public void sendMessage(List messages, int priority) {
        ArrayList newMsgs = new ArrayList(messages.size());
        for (int k = 0; k < messages.size(); k++) {
            newMsgs.add("PRIVMSG " + name + " :" + (String) messages.get(k));
        }
        client.sendRaw(newMsgs, priority);
    }

    /**
	 * Sends a block of actions to the channel at the given priority level
	 *
	 * @param actions list of actions
	 * @param priority level of priority of the block
	 */
    public void sendAction(List actions, int priority) {
        ArrayList newMsgs = new ArrayList(actions.size());
        for (int k = 0; k < actions.size(); k++) {
            newMsgs.add("PRIVMSG " + name + " :" + libairc.CTCP + "ACTION " + (String) actions.get(k) + libairc.CTCP);
        }
        client.sendRaw(newMsgs, priority);
    }

    /**
	 * Sends a block of notices to the channel at the given priority level
	 *
	 * @param messages list of notices
	 * @param priority level of priority of the block
	 */
    public void sendNotice(List messages, int priority) {
        ArrayList newMsgs = new ArrayList(messages.size());
        for (int k = 0; k < messages.size(); k++) {
            newMsgs.add("NOTICE " + name + " :" + (String) messages.get(k));
        }
        client.sendRaw(newMsgs, priority);
    }

    /**
	 * Sends a block of notices to the channel operators at the given priority level
	 *
	 * @param messages list of notices
	 * @param priority level of priority of the block
	 */
    public void sendOpNotice(List messages, int priority) {
        ArrayList newMsgs = new ArrayList(messages.size());
        for (int k = 0; k < messages.size(); k++) {
            newMsgs.add("NOTICE @" + name + " :" + (String) messages.get(k));
        }
        client.sendRaw(newMsgs, priority);
    }

    /**
	 * Kick a user from the channel with a given reason
	 * 
	 * @param user the user to kick (should be in the channel)
	 * @param reason the kick reason
	 * @param priority the priority to kick with
	 */
    public void kick(User user, String reason, int priority) {
        client.sendRaw("KICK " + name + " " + user.getNick() + " :" + reason, priority);
    }

    /**
	 * Get a user from the channel collection
	 * @param nick the nick of the user to get
	 * @return the User object for nick, or null if not found
	 */
    public User getUser(String nick) {
        synchronized (users) {
            if (!users.containsKey(nick)) {
                return null;
            }
            return (User) users.get(nick);
        }
    }

    void addUser(User user) {
        synchronized (users) {
            users.put(user.getNick(), user);
            user.addChannel(this);
        }
    }

    void remUser(String nick) {
        synchronized (users) {
            if (!users.containsKey(nick)) {
                return;
            }
            ((User) users.remove(nick)).remChannel(this);
        }
    }

    void cleanUsers() {
        synchronized (users) {
            Object[] keys = users.keySet().toArray();
            for (int index = 0; index < keys.length; index++) {
                ((User) users.remove(keys[index])).remChannel(this);
            }
        }
    }

    void addOp(User op) {
        if (op == null) {
            libairc.debug("Channel Mode", "user passed as null in addOp, why?");
            return;
        }
        synchronized (users) {
            if (!users.containsKey(op.getNick())) {
                users.put(op.getNick(), op);
                libairc.debug("Channel", "Asked to add " + op.getNick() + " to operators list for " + name + " but user not in channel, adding...");
            }
        }
        synchronized (ops) {
            ops.add(op);
            libairc.debug("Channel", op.getNick() + " added to operator list for " + name);
        }
    }

    void delOp(String nick) {
        synchronized (ops) {
            ops.remove(users.get(nick));
            libairc.debug("Channel", nick + " deleted from operator list for " + name);
        }
    }

    void addHop(User hop) {
        if (hop == null) {
            libairc.debug("Channel Mode", "user passed as null in addHop, why?");
            return;
        }
        synchronized (users) {
            if (!users.containsKey(hop.getNick())) {
                users.put(hop.getNick(), hop);
                libairc.debug("Channel", "Asked to add " + hop.getNick() + " to hoperators list for " + name + " but user not in channel, adding...");
            }
        }
        synchronized (hops) {
            ops.add(hop);
            libairc.debug("Channel", hop.getNick() + " added to hoperator list for " + name);
        }
    }

    void delHop(String nick) {
        synchronized (hops) {
            hops.remove(users.get(nick));
            libairc.debug("Channel", nick + " deleted from hoperator list for " + name);
        }
    }

    void addVoice(User voice) {
        if (voice == null) {
            libairc.debug("Channel Mode", "user passed as null in addVoice, why?");
            return;
        }
        if (voice == null) {
            libairc.debug("Channel", "Bot voiced on " + name);
            return;
        }
        synchronized (users) {
            if (!users.containsKey(voice.getNick())) {
                users.put(voice.getNick(), voice);
                libairc.debug("Channel", "Asked to add " + voice.getNick() + " to voiced list for " + name + " but user not in channel, adding...");
            }
        }
        synchronized (voices) {
            voices.add(voice);
            libairc.debug("Channel", voice.getNick() + " added to voiced list for " + name);
        }
    }

    void delVoice(String nick) {
        synchronized (voices) {
            voices.remove(users.get(nick));
            libairc.debug("Channel", nick + " deleted from voiced list for " + name);
        }
    }

    void setMyVoice(boolean voiced) {
        libairc.debug("Channel", "Am I voiced: " + voiced);
        amVoice = voiced;
    }

    void setMyHop(boolean hopped) {
        libairc.debug("Channel", "Am I hopped: " + hopped);
        amHop = hopped;
    }

    void setMyOp(boolean opped) {
        libairc.debug("Channel", "Am I opped: " + opped);
        amOp = opped;
    }

    void setKey(String key) {
        libairc.debug("Channel", "Key of " + name + " set to " + key);
    }

    void clearKey() {
        libairc.debug("Channel", "Key of " + name + " cleared");
    }

    void setLimit(String limit) {
        libairc.debug("Channel", "Limit of " + name + " set to " + limit);
    }

    void clearLimit() {
        libairc.debug("Channel", "Limit of " + name + " cleared");
    }

    void setModeFlag(char flag) {
        libairc.debug("Channel", "Set mode " + flag);
    }

    void clearModeFlag(char flag) {
        libairc.debug("Channel", "Cleared mode " + flag);
    }

    /**
	 * NOTE: some IRC servers have modes to make people invisible
	 *
	 * @param nick the nick to check
	 */
    public boolean containsUser(String nick) {
        synchronized (users) {
            return users.containsKey(nick);
        }
    }

    /**
	 * Check if a user is in this channel
	 * NOTE: some IRC servers have modes to make people invisible
	 * 
	 * @param user the user to check
	 */
    public boolean containsUser(User user) {
        synchronized (users) {
            return users.containsValue(user);
        }
    }

    public boolean isOp() {
        return amOp;
    }

    public boolean isVoice() {
        return amVoice;
    }

    public boolean isHop() {
        return amHop;
    }
}
