package net.sourceforge.maxim.server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.sql.ResultSet;

public class ServerProtocol {

    /**
   * state so we know what messages are valid to send
   */
    private String state = "00001";

    /**
   * temp vars to exchange data for efficency (saves looping)
   */
    private String budMods = "";

    private String budStatus = "";

    private int tid = 0;

    /**
   * Processes information SENT by the user.
   * @param message - A string of information that the client wants to send
   * with the appropriate header on the front
   * @param thread - the MaxIM_Server object that is currently being used
   * so we can perform actions such as disconnect() if the user types QUIT for
   * example.
   * @return String containing the correct data to send to server or empty
   * string if it did not match any of the headers
   */
    public String processInput(String message, ServerThread thread) {
        if (message.equals("00000")) {
            state = "00001";
            try {
                ResultSet bs = thread.obj.db.getBuddies(thread.thisUser.username);
                while (bs.next()) {
                    if (isOnline(bs.getString(2), thread)) {
                        thread.obj.users[tid].out.println("00017 " + thread.thisUser.username + ";" + "offline" + ";" + "1" + ";" + thread.thisUser.modules);
                    }
                }
            } catch (Exception e) {
            }
            return "QUIT";
        }
        if (state.equals("00001") || state.equals("00002")) {
            if (message.startsWith("00001 ")) {
                String temp = message.substring(6);
                String[] details = temp.split(",");
                String date = dateFormat("yyyy-MM-dd");
                thread.obj.db.setHash("SHA-1");
                boolean outcome = thread.obj.db.registerNewUser(details[0], details[1], date);
                thread.thisUser.username = details[0];
                if (outcome) {
                    state = "00003";
                    return "00001 001";
                } else {
                    return "00001 004 Username already taken. Try again.";
                }
            } else if (message.startsWith("00002 ")) {
                String temp = message.substring(6);
                String[] details = temp.split(",");
                boolean outcome = thread.obj.db.authenticateUser(details[0], details[1]);
                int p = 0;
                while (p < thread.obj.users.length) {
                    if ((thread.obj.users[p] != null) && (thread.obj.users[p].thisUser != null) && (thread.obj.users[p].thisUser.username != null)) {
                        if (thread.obj.users[p].thisUser.username.equals(details[0]) && (!thread.obj.users[p].dead)) {
                            return "00002 004 You are already signed in elsewhere!";
                        }
                    }
                    p++;
                }
                if (outcome) {
                    thread.thisUser.username = details[0];
                    state = "00003";
                    return "00002 001";
                } else {
                    return "00002 004 Incorrect username or password. Try again";
                }
            } else {
                return "00001 004 Invalid command at this stage";
            }
        }
        if (state.equals("00003")) {
            if (message.startsWith("00003 ")) {
                if (message.substring(6).length() > 0) {
                    thread.thisUser.modules = message.substring(6);
                    state = "00004";
                    return "00003 001";
                } else {
                    return "00003 004 Please resend your modules. " + "There was an error.";
                }
            } else {
                return "00003 004 Not a valid command for this state";
            }
        }
        if (state.equals("00004")) {
            if (message.equals("00004")) {
                String returner = "";
                ResultSet rs = thread.obj.db.getBuddies(thread.thisUser.username);
                try {
                    while (rs.next()) {
                        String name = rs.getString(2);
                        returner = returner + name + ";";
                        String status = "offline";
                        String modules = "none";
                        if (isOnline(name, thread)) {
                            status = budStatus;
                            modules = budMods;
                        }
                        returner = returner + status + ";";
                        returner = returner + rs.getString(3) + ";";
                        if (rs.isLast()) {
                            returner = returner + modules;
                        } else {
                            returner = returner + modules + ">";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("An error occured whilst trying to get " + "buddies for '" + thread.thisUser.username + "'\n");
                    return "00004 004";
                }
                if (returner.length() > 0) {
                    state = "00005";
                    return "00004 001 " + returner;
                } else {
                    state = "00005";
                    return "00004 002";
                }
            } else {
                return "00004 004 Not a valid command for this state";
            }
        }
        if (state.equals("00005")) {
            if (message.startsWith("00005 ")) {
                thread.thisUser.status = message.substring(6);
                ResultSet rs = thread.obj.db.getBuddies(thread.thisUser.username);
                try {
                    while (rs.next()) {
                        int x = 0;
                        while (x < thread.obj.users.length) {
                            if (thread.obj.users[x] != null) {
                                if (!thread.obj.users[x].dead) {
                                    if (thread.obj.users[x].thisUser.username.equals(rs.getString(2))) {
                                        ResultSet buddyBlocked = thread.obj.db.getBlockedBuddies(rs.getString(2));
                                        boolean blockedStat = false;
                                        while (buddyBlocked.next()) {
                                            if (buddyBlocked.getString(2).equals(thread.thisUser.username)) {
                                                blockedStat = true;
                                                break;
                                            }
                                        }
                                        if (thread.obj.users[x].thisUser.ready) {
                                            thread.obj.users[x].out.println("00016 " + thread.thisUser.username + ";" + thread.thisUser.status + ";" + blockedStat + ";" + thread.thisUser.modules);
                                        }
                                        break;
                                    }
                                }
                            }
                            x++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("An error occured whilst trying to " + "populate status for " + thread.thisUser.username + "\n");
                    return "00005 004";
                }
                state = "00006";
                return "00005 001";
            } else {
                return "00005 004";
            }
        }
        if (state.equals("00006")) {
            if (message.equals("00015")) {
                String requestingBuddys = "";
                try {
                    ResultSet requests = thread.obj.db.getBuddyRequests(thread.thisUser.username);
                    while (requests.next()) {
                        if (requests.isLast()) {
                            requestingBuddys = requestingBuddys + requests.getString(1);
                        } else {
                            requestingBuddys = requestingBuddys + requests.getString(1) + ",";
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error: could not get buddy requests for " + thread.thisUser.username);
                    return "00015 004";
                }
                thread.thisUser.ready = true;
                state = "00007";
                if (requestingBuddys.equals("")) {
                    return "00015 002 ";
                } else {
                    return "00015 001 " + requestingBuddys;
                }
            } else {
                return "00015 004";
            }
        }
        if (state.equals("00007")) {
            if (message.startsWith("00006 ")) {
                String usersAndMessage = message.substring(6);
                String[] components = usersAndMessage.split(" ");
                String theUser = components[0];
                String theMessage = "";
                String[] allUsers = theUser.split(",");
                int l = 0;
                boolean allowedToSend = false, blockedme = false;
                try {
                    while (l < allUsers.length) {
                        ResultSet bs = thread.obj.db.getBuddies(thread.thisUser.username);
                        ResultSet sb = thread.obj.db.getBuddies(allUsers[l]);
                        if (bs.wasNull()) {
                            allowedToSend = false;
                        }
                        while (bs.next()) {
                            if (bs.getString(2).equals(allUsers[l])) {
                                if (bs.getString(4).equals("1")) {
                                    allowedToSend = true;
                                    break;
                                }
                            }
                        }
                        if (!allowedToSend) {
                            break;
                        }
                        while (sb.next()) {
                            if (sb.getString(2).equals(thread.thisUser.username)) {
                                if (sb.getString(3).equals("1")) {
                                    blockedme = true;
                                    break;
                                }
                            }
                        }
                        if (blockedme) {
                            break;
                        }
                        l++;
                    }
                } catch (Exception e) {
                    System.err.println("Error: could not get buddys for " + thread.thisUser.username);
                    return "00006 004";
                }
                if (allowedToSend && !blockedme) {
                    int x = 1;
                    while (x < components.length) {
                        if (x == components.length - 1) {
                            theMessage = theMessage + components[x];
                        } else {
                            theMessage = theMessage + components[x] + " ";
                        }
                        x++;
                    }
                    x = 0;
                    int z = 0;
                    while (z < allUsers.length) {
                        while (x < thread.obj.users.length) {
                            if (thread.obj.users[x] != null) {
                                if (!thread.obj.users[x].dead) {
                                    if (thread.obj.users[x].thisUser.username.equals(allUsers[z])) {
                                        thread.obj.users[x].out.println("00006 001 " + thread.thisUser.username + " " + theMessage);
                                        break;
                                    }
                                }
                            }
                            x++;
                        }
                        x = 0;
                        z++;
                    }
                    return "00006 002";
                } else {
                    if (!allowedToSend) {
                        return "00006 004 Your buddy '" + allUsers[l] + "' has not authorised your add request yet!";
                    } else {
                        return "00006 004 " + allUsers[l] + " has blocked you!";
                    }
                }
            } else if (message.startsWith("00007 ")) {
                String[] Buds = message.substring(6).split(",");
                String asn = thread.obj.db.addBuddies(thread.thisUser.username, Buds);
                if (asn.equals("true")) {
                    int x = 0;
                    String[] allUsers = message.substring(6).split(",");
                    int z = 0;
                    while (z < allUsers.length) {
                        while (x < thread.obj.users.length) {
                            if (thread.obj.users[x] != null && thread.obj.users[x].thisUser != null) {
                                if (!thread.obj.users[x].dead) {
                                    if (thread.obj.users[x].thisUser.username.equals(allUsers[z])) {
                                        thread.obj.users[x].out.println("00015 001 " + thread.thisUser.username);
                                        break;
                                    }
                                }
                            }
                            x++;
                        }
                        x = 0;
                        z++;
                    }
                    String toReturn = "";
                    try {
                        ResultSet bs = thread.obj.db.getBuddies(thread.thisUser.username);
                        if (bs == null) {
                        }
                        while (bs.next()) {
                            if (bs.getString(2).equals(message.substring(6))) {
                                if (isOnline(message.substring(6), thread)) {
                                    toReturn = bs.getString(2) + ";" + "1" + ";" + bs.getString(3) + ";" + budMods;
                                    break;
                                } else {
                                    toReturn = bs.getString(2) + ";" + "0" + ";" + bs.getString(3) + ";" + "none";
                                }
                            }
                        }
                    } catch (Exception e) {
                        return "00007 004";
                    }
                    return "00007 001 " + toReturn;
                } else {
                    if (asn.equals("false")) {
                        return "00007 004";
                    } else {
                        return "00007 005";
                    }
                }
            } else if (message.startsWith("00008 ")) {
                String[] buddyDels = message.substring(6).split(",");
                int x = 0;
                boolean outcome = thread.obj.db.deleteBuddies(thread.thisUser.username, buddyDels);
                if (outcome) {
                    return "00008 001 " + message.substring(6);
                } else {
                    return "00008 004 Could not delete some buddies as they " + "have already been deleted!";
                }
            } else if (message.startsWith("00009 ")) {
                String[] buddyBlock = message.substring(6).split(",");
                int x = 0;
                boolean outcome = thread.obj.db.blockBuddies(thread.thisUser.username, buddyBlock);
                if (outcome) {
                    return "00009 001 " + message.substring(6);
                } else {
                    return "00009 004 Could not block some buddies " + "as they have already been blocked!";
                }
            } else if (message.startsWith("00010 ")) {
                String[] buddyUNBlock = message.substring(6).split(",");
                int x = 0;
                boolean outcome = thread.obj.db.unblockBuddies(thread.thisUser.username, buddyUNBlock);
                if (outcome) {
                    return "00010 001 " + message.substring(6);
                } else {
                    return "00010 004 Could not unblock some buddies as " + "they have already been unblocked!";
                }
            } else if (message.startsWith("00011 ")) {
                String[] buddyAuths = message.substring(6).split(",");
                int x = 0;
                boolean outcome = thread.obj.db.authBuddyAdd(thread.thisUser.username, buddyAuths);
                if (outcome) {
                    return "00011 001 " + message.substring(6);
                } else {
                    return "00011 004";
                }
            } else if (message.startsWith("00012 ")) {
                String[] buddyAuths = message.substring(6).split(",");
                int x = 0;
                boolean outcome = thread.obj.db.denyBuddyAdd(thread.thisUser.username, buddyAuths);
                if (outcome) {
                    return "00012 001";
                } else {
                    return "00012 004";
                }
            } else if (message.equals("00013")) {
                String[] users = new String[1];
                users[0] = thread.thisUser.username;
                thread.thisUser.username = "";
                boolean outcome = thread.obj.db.deleteUsers(users);
                if (outcome) {
                    state = "00001";
                    return "00013 001";
                } else {
                    return "00013 004";
                }
            } else if (message.equals("00015")) {
                String requestingBuddys = "";
                try {
                    ResultSet requests = thread.obj.db.getBuddyRequests(thread.thisUser.username);
                    while (requests.next()) {
                        if (requests.isLast()) {
                            requestingBuddys = requestingBuddys + requests.getString(1);
                        } else {
                            requestingBuddys = requestingBuddys + requests.getString(1) + ",";
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error: could not get buddy requests for " + thread.thisUser.username);
                    return "00015 004";
                }
                if (requestingBuddys.equals("")) {
                    return "00015 002 ";
                } else {
                    return "00015 001 " + requestingBuddys;
                }
            } else if (message.startsWith("00005 ")) {
                if (message.startsWith("00005 ")) {
                    thread.thisUser.status = message.substring(6);
                    ResultSet rs = thread.obj.db.getBuddies(thread.thisUser.username);
                    try {
                        while (rs.next()) {
                            int x = 0;
                            while (x < thread.obj.users.length) {
                                if (thread.obj.users[x] != null) {
                                    if (!thread.obj.users[x].dead) {
                                        if (thread.obj.users[x].thisUser.username.equals(rs.getString(2))) {
                                            ResultSet buddyBlocked = thread.obj.db.getBlockedBuddies(rs.getString(2));
                                            boolean blockedStat = false;
                                            while (buddyBlocked.next()) {
                                                if (buddyBlocked.getString(2).equals(thread.thisUser.username)) {
                                                    blockedStat = true;
                                                    break;
                                                }
                                            }
                                            if (thread.obj.users[x].thisUser.ready) {
                                                thread.obj.users[x].out.println("00017 " + thread.thisUser.username + ";" + thread.thisUser.status + ";" + blockedStat + ";" + thread.thisUser.modules);
                                            }
                                            break;
                                        }
                                    }
                                }
                                x++;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("An error occured whilst trying " + "to populate status for " + thread.thisUser.username + "\n");
                        return "00005 004";
                    }
                    return "00005 001";
                } else {
                    return "00005 004";
                }
            } else if (message.startsWith("00003 ")) {
                if (message.substring(6).length() > 0) {
                    thread.thisUser.modules = message.substring(6);
                    state = "00004";
                    return "00003 001";
                } else {
                    return "00003 004 Please resend your modules. " + "There was an error.";
                }
            } else {
                return "";
            }
        }
        return "";
    }

    /**
   * Checks if a specified username is currently online and alerts them
   * to the fact you are now online
   * @param username - string of the username
   * @return boolean if the user is online or not
   */
    public boolean isOnline(String username, ServerThread thread) {
        int x = 0;
        while (x < thread.obj.users.length) {
            if (thread.obj.users[x] != null) {
                if (!thread.obj.users[x].dead) {
                    if (thread.obj.users[x].thisUser.username.equals(username)) {
                        budMods = thread.obj.users[x].thisUser.modules;
                        budStatus = thread.obj.users[x].thisUser.status;
                        tid = x;
                        return true;
                    }
                }
            }
            x++;
        }
        return false;
    }

    /**
   * Gets todays date in format specified
   * @param format - specifying format you want date returned in
   * @return String containing todays date
   */
    public String dateFormat(String format) {
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String datenewformat = formatter.format(today);
        return datenewformat;
    }
}
