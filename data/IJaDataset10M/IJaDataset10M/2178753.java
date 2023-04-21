package regnumhelper.server;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import regnumhelper.EnemyData;
import regnumhelper.GuiGroup;
import regnumhelper.PlayerData;

/**
 *
 * @author Niels
 */
public class RegnumProtokoll {

    /** This event marks player data following */
    public static final int EVENT_PLAYER_DATA = 0;

    /** This event marks enemy data following */
    public static final int EVENT_ENEMY_DATA = 1;

    /** This event marks version data following */
    public static final int EVENT_VERSION_DATA = 2;

    /** This event marks group chat data following */
    public static final int EVENT_CHAT_DATA = 3;

    /** This event marks register group data following */
    public static final int EVENT_REGISTER_GROUP = 4;

    /** This event marks unregister group data following */
    public static final int EVENT_UNREGISTER_GROUP = 5;

    /** This event marks global chat data following */
    public static final int EVENT_ALL_CHAT_DATA = 6;

    /** This event marks popup chat data from server following */
    public static final int EVENT_SERVER_POPUP_DATA = 7;

    /** This event marks global chat from server data following */
    public static final int EVENT_SERVER_MSG_DATA = 8;

    /** A command sent to server */
    public static final int EVENT_SERVER_COMMAND = 9;

    /** A command answer from server */
    public static final int EVENT_SERVER_COMMAND_ANSWER = 10;

    /** This event marks register group data following for password protected groups*/
    public static final int EVENT_REGISTER_PRIVATE_GROUP = 11;

    /** This event marks answer from server that group registering failed*/
    public static final int EVENT_REGISTER_GROUP_FAILED = 12;

    /** This event marks a list of groups */
    public static final int EVENT_AVAILABLE_GROUPS = 13;

    /** This event marks a request for list of groups */
    public static final int EVENT_REQUEST_AVAILABLE_GROUPS = 14;

    /** This event marks a request for list of names for a group */
    public static final int EVENT_REQUEST_PLAYERLIST_OF_GROUP = 15;

    /** This event marks an answer to a request for list of names for a group */
    public static final int EVENT_ANSWER_PLAYERLIST_OF_GROUP = 16;

    /** Server commands */
    public static enum Command {

        users, groups, msg, max, pop, traffic, clear, exit, log, welcome, unknown
    }

    public static String createRequestPlayerList(String groupname) {
        String dat = "|" + EVENT_REQUEST_PLAYERLIST_OF_GROUP + "|" + groupname + "|\n";
        return dat;
    }

    public static String getRequestPlayerList(String data) {
        String groupname = "";
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    groupname = ((String) v.get(i)).trim();
                    break;
                default:
                    break;
            }
        }
        return groupname;
    }

    public static String createAnswerPlayerList(List<String> names, String groupname) {
        String dat = "|" + EVENT_ANSWER_PLAYERLIST_OF_GROUP + "|" + groupname;
        Iterator<String> iter = names.iterator();
        while (iter.hasNext()) {
            dat += "|" + iter.next();
        }
        dat += "|\n";
        return dat;
    }

    public static List<String> getAnswerPlayerList(String data) {
        List<String> names = new Vector();
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                default:
                    names.add(((String) v.get(i)).trim());
                    break;
            }
        }
        return names;
    }

    /**
     * Creates a server command to be xecuted on server
     * @param password - password of the user
     * @param cmd - the command
     * @param argument - arguments to the command
     * @return a protocol string for the command
     */
    public static String createServerCommand(String password, Command cmd, String argument) {
        String dat = "|" + EVENT_SERVER_COMMAND + "|" + password + "|" + cmd.toString() + "|" + ((argument == null) ? "" : argument) + "|\n";
        return dat;
    }

    /**
     * Returns the command data
     * rv [0] = password
     * rv [1] = command string
     * rv [2] = argument
     * @param data - protocol data to analyse
     * @return an array with password, command and argument
     */
    public static String[] getServerCommandData(String data) {
        String[] rv = new String[3];
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    rv[0] = ((String) v.get(i)).trim();
                    break;
                case 2:
                    rv[1] = ((String) v.get(i)).trim();
                    break;
                case 3:
                    rv[2] = ((String) v.get(i)).trim();
                    break;
            }
        }
        return rv;
    }

    /**
     * Creates a server command answer to be displayed on client
     * @param cmd - the command that was answered
     * @param answer - the answer
     * @return a protocol string for the command answer
     */
    public static String createServerCommandAnswer(Command cmd, String answer) {
        String dat = "|" + EVENT_SERVER_COMMAND_ANSWER + "|" + cmd.toString() + "|" + answer.replaceAll("\n", "<<<>>>") + "|\n";
        return dat;
    }

    /**
     * Returns the command data
     * rv [0] = command string
     * rv [1] = answer
     * @param data - protocol data to analyse
     * @return an array with command and answer
     */
    public static String[] getServerCommandAnswerData(String data) {
        String[] rv = new String[2];
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    rv[0] = ((String) v.get(i)).trim();
                    break;
                case 2:
                    rv[1] = ((String) v.get(i)).trim().replaceAll("<<<>>>", "\n");
                    break;
            }
        }
        return rv;
    }

    /**
     * Creates a new Protocol string for a player data
     * @param data - data to create protocol string from
     * @return the protocol string for a player data
     */
    public static String createPlayerData(PlayerData data) {
        String dat = "|" + EVENT_PLAYER_DATA + "|" + data.getName() + "|" + data.getRealm() + "|" + data.getPosX() + "|" + data.getPosY() + "|" + data.getSpeed() + "|" + data.getHealth() + "|" + data.getMana() + "|" + data.getDirection() + "|" + data.getServername() + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for a enemy data
     * @param data - data to create protocol string from
     * @return the protocol string for a enemy data
     */
    public static String createEnemyData(EnemyData data) {
        String dat = "|" + EVENT_ENEMY_DATA + "|" + data.getEnemy() + "|" + data.getPosX() + "|" + data.getPosY() + "|" + data.getType() + "|" + data.getServername() + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for a version info
     * @return the protocol string for a version info
     */
    public static String createVersionData() {
        String dat = "|" + EVENT_VERSION_DATA + "|" + Server.VERSION + "|" + Server.versionInfos + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for group chat
     * @param playername - name of player who sends message
     * @param text - text for chat
     * @return a new Protocol string for group chat
     */
    public static String createChatData(String playername, String text) {
        String txt = text.replaceAll("\n", "<<<>>>");
        String dat = "|" + EVENT_CHAT_DATA + "|" + playername + "|" + txt + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for global chat
     * @param playername - name of player who sends message
     * @param text - text for chat
     * @return a new Protocol string for global chat
     */
    public static String createAllChatData(String playername, String text) {
        String txt = text.replaceAll("\n", "<<<>>>");
        String dat = "|" + EVENT_ALL_CHAT_DATA + "|" + playername + "|" + txt + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for global chat data
     * @param text - text to display
     * @return a new Protocol string for global chat data
     */
    public static String createServerMessageData(String text) {
        String dat = "|" + EVENT_SERVER_MSG_DATA + "|" + text + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for popup data
     * @param text - text to display
     * @return a new Protocol string for popup data
     */
    public static String createServerPopUpData(String text) {
        String dat = "|" + EVENT_SERVER_POPUP_DATA + "|" + text + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for failure of group registering
     * @param text - text to display
     * @return a new Protocol string for popup data
     */
    public static String createServerGroupRegisterFailed(String text) {
        String dat = "|" + EVENT_REGISTER_GROUP_FAILED + "|" + text + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for requesting groups
     * @param text - text to display
     * @return a new Protocol string for popup data
     */
    public static String createGroupListRequest() {
        String dat = "|" + EVENT_REQUEST_AVAILABLE_GROUPS + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for requesting groups
     * @param text - text to display
     * @return a new Protocol string for popup data
     */
    public static String createGroupListRequestAnswer(List<GuiGroup> groups) {
        String dat = "|" + EVENT_AVAILABLE_GROUPS;
        Iterator<GuiGroup> iter = groups.iterator();
        while (iter.hasNext()) {
            GuiGroup grp = iter.next();
            dat += "|" + grp.getName() + "|" + grp.getMode().toString() + "|" + grp.getCount();
        }
        dat += "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for registering groups
     * @param realm - realm of the player thats registered
     * @param groupname - the groupname of the new group - can be empty ""
     * @return a new Protocol string for registering groups
     */
    public static String createGroupRegister(int realm, String groupname, String playername, String server) {
        String dat = "|" + EVENT_REGISTER_GROUP + "|" + realm + "|" + groupname + "|" + playername + "|" + server + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for registering to provate groups
     * @param realm - realm of the player thats registered
     * @param groupname - the groupname of the new group - can be empty ""
     * @return a new Protocol string for registering groups
     */
    public static String createPrivateGroupRegister(int realm, String groupname, String password, String playername, String server) {
        String dat = "|" + EVENT_REGISTER_PRIVATE_GROUP + "|" + realm + "|" + groupname + "|" + password + "|" + playername + "|" + server + "|\n";
        return dat;
    }

    /**
     * Creates a new Protocol string for unregistering groups
     * @param oldRealm - old realm of the player thats registered
     * @param groupname - the groupname of the  group - can be empty ""
     * @return a new Protocol string for unregistering groups
     */
    public static String createGroupUnregister(int oldRealm, String groupname, String server) {
        String dat = "|" + EVENT_UNREGISTER_GROUP + "|" + oldRealm + "|" + groupname + "|" + server + "|\n";
        return dat;
    }

    /**
     * Returns the event of the protocol
     * @param data - protocol data to analyse
     * @return the event of the protocol, or -1 if not found
     */
    public static int getEvent(String data) {
        int rv = -1;
        Vector<String> v = getTokens(data);
        if (v.size() > 0) {
            String evt = v.get(0).trim();
            try {
                rv = Integer.parseInt(evt);
            } catch (NumberFormatException exc) {
                rv = -1;
            }
        }
        return rv;
    }

    /**
     * Returns all tokens of the protocol
     * @param data - protocol data to analyse
     * @return all tokens of the protocol
     */
    public static Vector<String> getTokens(String data) {
        Vector v = new Vector();
        boolean hasMoreTokens = true;
        int index1 = 0;
        int index2 = 0;
        while (hasMoreTokens) {
            index1 = data.indexOf("|", index2);
            if (index1 >= 0) {
                index2 = data.indexOf("|", index1 + 1);
                if (index2 > index1) {
                    String t = data.substring(index1 + 1, index2);
                    v.add(t);
                } else {
                    hasMoreTokens = false;
                }
            } else {
                hasMoreTokens = false;
            }
        }
        return v;
    }

    /**
     * Creates a enemy data, out of the protocol data
     * @param data - the protocol data to analyse
     * @return am EnemyData object or null, if protocol does not match
     */
    public static EnemyData getEnemyData(String data) {
        EnemyData rv = null;
        int enemy = 0;
        double posX = 0;
        double posY = 0;
        int type = 0;
        Vector v = getTokens(data);
        try {
            for (int i = 0; i < v.size(); i++) {
                switch(i) {
                    case 0:
                        break;
                    case 1:
                        enemy = Integer.parseInt(((String) v.get(i)).trim());
                        break;
                    case 2:
                        posX = Double.parseDouble(((String) v.get(i)).trim());
                        break;
                    case 3:
                        posY = Double.parseDouble(((String) v.get(i)).trim());
                        break;
                    case 4:
                        type = Integer.parseInt(((String) v.get(i)).trim());
                        rv = new EnemyData(posX, posY, type, enemy);
                        break;
                    case 5:
                        rv.setServername(((String) v.get(i)).trim());
                        break;
                    default:
                        break;
                }
            }
        } catch (NumberFormatException exc) {
            rv = null;
        }
        return rv;
    }

    /**
     * Creates a player data, out of the protocol data
     * @param data - the protocol data to analyse
     * @param ownPlayerName - own player name for setting ownPlayer flag
     * @return am PlayerData object or null, if protocol does not match
     */
    public static PlayerData getPlayerData(String data, String ownPlayerName) {
        PlayerData rv = null;
        int realm = 0;
        String name = "";
        double posX = 0;
        double posY = 0;
        Vector v = getTokens(data);
        try {
            for (int i = 0; i < v.size(); i++) {
                switch(i) {
                    case 0:
                        break;
                    case 1:
                        name = ((String) v.get(i)).trim();
                        break;
                    case 2:
                        realm = Integer.parseInt(((String) v.get(i)).trim());
                        break;
                    case 3:
                        posX = Double.parseDouble(((String) v.get(i)).trim());
                        break;
                    case 4:
                        posY = Double.parseDouble(((String) v.get(i)).trim());
                        boolean ownPlayer = false;
                        if (name != null && name.equals(ownPlayerName)) ownPlayer = true;
                        rv = new PlayerData(realm, name, posX, posY, ownPlayer);
                        break;
                    case 5:
                        rv.setSpeed(Double.parseDouble(((String) v.get(i)).trim()));
                        break;
                    case 6:
                        rv.setHealth(Integer.parseInt(((String) v.get(i)).trim()));
                        break;
                    case 7:
                        rv.setMana(Integer.parseInt(((String) v.get(i)).trim()));
                        break;
                    case 8:
                        try {
                            rv.setDirection(Double.parseDouble((String) v.get(i)));
                        } catch (NumberFormatException exc) {
                        }
                        break;
                    case 9:
                        rv.setServername((String) v.get(i));
                        break;
                    default:
                        break;
                }
            }
        } catch (NumberFormatException exc) {
            rv = null;
        }
        return rv;
    }

    /**
     * returns the version out of the protocol
     * @param data - the protocol data to analyse
     * @return the version or -1 if protocol does not match
     */
    public static int getVersion(String data) {
        int rv = -1;
        Vector v = getTokens(data);
        try {
            for (int i = 0; i < v.size(); i++) {
                switch(i) {
                    case 0:
                        break;
                    case 1:
                        rv = Integer.parseInt(((String) v.get(i)).trim());
                        break;
                }
            }
        } catch (NumberFormatException exc) {
            rv = -1;
        }
        return rv;
    }

    /**
     * Returns the version information text out of the protocol
     * @param data - the protocol data to analyse
     * @return the version information text out of the protocol
     */
    public static String getVersionInfo(String data) {
        String rv = "";
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    String tmp = ((String) v.get(i)).trim();
                    if (tmp != null) rv = tmp.replaceAll("<<<>>>", "\n");
                    break;
            }
        }
        return rv;
    }

    /**
     * Returns the Group the a player wants to register to out of protocol data
     * @param data - the protocol data to analyse
     * @return the Group the a player wants to register to out of protocol data
     */
    public static Group getRegisterGroup(String data) {
        Group rv = new Group(-1, null, null, null, "");
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    try {
                        rv.realm = Integer.parseInt(((String) v.get(i)).trim());
                    } catch (NumberFormatException exc) {
                    }
                    break;
                case 2:
                    rv.groupname = ((String) v.get(i)).trim();
                    break;
                case 3:
                    rv.playername = ((String) v.get(i)).trim();
                    break;
                case 4:
                    rv.servername = ((String) v.get(i)).trim();
                    break;
            }
        }
        return rv;
    }

    /**
     * Returns the Group the a player wants to register to out of protocol data
     * @param data - the protocol data to analyse
     * @return The Group the a player wants to register to out of protocol data
     */
    public static Group getRegisterPrivateGroup(String data) {
        Group rv = new Group(-1, null, null, null, "");
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    try {
                        rv.realm = Integer.parseInt(((String) v.get(i)).trim());
                    } catch (NumberFormatException exc) {
                    }
                    break;
                case 2:
                    rv.groupname = ((String) v.get(i)).trim();
                    break;
                case 3:
                    rv.password = ((String) v.get(i)).trim();
                    break;
                case 4:
                    rv.playername = ((String) v.get(i)).trim();
                    break;
                case 5:
                    rv.servername = ((String) v.get(i)).trim();
                    break;
            }
        }
        return rv;
    }

    /**
     * Returns the Group the a player wants to unregister from out of protocol data
     * @param data - the protocol data to analyse
     * @return the Group the a player wants to unregister from out of protocol data
     */
    public static Group getUnregisterGroup(String data) {
        Group rv = new Group(-1, null, null, null, "");
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    try {
                        rv.realm = Integer.parseInt(((String) v.get(i)).trim());
                    } catch (NumberFormatException exc) {
                    }
                    break;
                case 2:
                    rv.groupname = ((String) v.get(i)).trim();
                    break;
                case 3:
                    rv.servername = ((String) v.get(i)).trim();
                    break;
            }
        }
        return rv;
    }

    /**
     * Returns the Group the a player can register to
     * @param data - the protocol data to analyse
     * @return the Groups  a player can register to
     */
    public static List<GuiGroup> getGroupListRequestAnswer(String data) {
        Vector<GuiGroup> grps = new Vector();
        Vector v = getTokens(data);
        GuiGroup tmp = null;
        int index = 0;
        boolean failure = false;
        for (int i = 1; i < v.size(); i++) {
            index = ((i - 1) % 3);
            if (index == 0) failure = false;
            if (!failure) {
                try {
                    switch(index) {
                        case 0:
                            String name = ((String) v.get(i)).trim();
                            tmp = new GuiGroup();
                            tmp.setName(name);
                            break;
                        case 1:
                            tmp.setMode(Group.GroupMode.valueOf(((String) v.get(i)).trim()));
                            break;
                        case 2:
                            tmp.setCount(Integer.parseInt(((String) v.get(i)).trim()));
                            grps.add(tmp);
                            break;
                    }
                } catch (Exception exc) {
                    failure = true;
                }
            }
        }
        return grps;
    }

    /**
     * Returns the chat text out of the protocol data
     * @param data - the protocol data
     * @return the chat text out of the protocol data
     */
    public static String[] getChatData(String data) {
        String[] rv = new String[2];
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    rv[0] = ((String) v.get(i)).trim();
                    break;
                case 2:
                    String tmp = ((String) v.get(i)).trim();
                    if (tmp != null) rv[1] = tmp.replaceAll("<<<>>>", "\n");
                    break;
            }
        }
        return rv;
    }

    /**
     * Returns the chat text for a global chat out of the protocol.
     * @param data - the protocol data
     * @return the chat text for a global chat out of the protocol.
     */
    public static String[] getAllChatData(String data) {
        return getChatData(data);
    }

    /**
     * Returns the global chat message from the server out of the protocol
     * @param data - the protocol data
     * @return the global chat message from the server out of the protocol
     */
    public static String getServerMessageData(String data) {
        String rv = null;
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    rv = ((String) v.get(i)).trim();
                    break;
            }
        }
        return rv;
    }

    /**
     * Returns the group registration failure message out of the protocol
     * @param data - the protocol data
     * @return the group registration failure message out of the protocol
     */
    public static String getServerGroupRegisterFailedData(String data) {
        String rv = null;
        Vector v = getTokens(data);
        for (int i = 0; i < v.size(); i++) {
            switch(i) {
                case 0:
                    break;
                case 1:
                    rv = ((String) v.get(i)).trim();
                    break;
            }
        }
        return rv;
    }

    /**
     * Return the popup message from the server out of the protocol
     * @param data - the protocol data
     * @return the popup message from the server out of the protocol
     */
    public static String getServerPopUpData(String data) {
        return getServerMessageData(data);
    }
}
