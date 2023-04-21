package com.objectwave.simpleSockets;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *   This extension of the WatchdogServer manages mutual exclusion across
 *  process boundaries.  It's actually a pretty simple procedure, and this
 *  implementation doesn't implement many safeguards.  A client will
 *  connect briefly, and send a string in the format
 *  "request:<string>", where <string> is a string that has some meaning 
 *   among the clients.  The request will prompt the server to
 *  return "granted" if and only if <string> is either currently associated
 *  with the client, or if it is not associated with any clients.  It will
 *  otherwise return "denied".  Any other response is an internal error.
 *
 *    The client can also send a "release:<string>" message, which will
 *  cause the server to destroy any association between the client and 
 *  <string>.
 *  This method will always succeed (having no associated id to destroy is
 *  not an error condition).
 *
 *    The client can also send a "releaseAll" message, which will, well, 
 *  release all string associated with the client'd is.
 */
public class MutexServer extends WatchdogServer {

    private Hashtable hashtable = new Hashtable(10);

    public MutexServer() {
    }

    public MutexServer(int port) {
        super(port);
    }

    protected void clientDied(long id) {
        releaseAll(id);
        System.out.println("Removed dead client: id# " + id);
    }

    protected void clientLeft(long id) {
        releaseAll(id);
        System.out.println("Removed client: id# " + id);
    }

    /**
	* Fire up the mutual exclusion server.
	*/
    public static void main(String args[]) {
        System.out.println("remote mutual exclusion server");
        System.out.println("");
        if (args.length > 0) new MutexServer(new Integer(args[0]).intValue()).startServer(); else new MutexServer().startServer();
    }

    protected void newClient(long id) {
        System.out.println("New client: id# " + id);
    }

    protected void processGoodbye(long id) {
        System.out.println("processGoodbye");
        clientLeft(id);
    }

    protected String processMessage(long id, String message) {
        int separator = message.indexOf(':');
        if (separator < 0) {
            System.err.println("Error: no separator \":\" in message from " + id + ": \"" + message + "\"");
            return "error";
        }
        String command = message.substring(0, separator);
        String string = message.substring(separator + 1);
        synchronized (hashtable) {
            if (command.equalsIgnoreCase("request")) {
                System.out.println("Process request for id " + id + ":\"" + string + "\"");
                Long id2 = (Long) hashtable.get(string);
                if (id2 == null) {
                    hashtable.put(string, new Long(id));
                    System.out.println("Associated id with string.");
                    return "granted";
                } else if (id2.longValue() == id) {
                    System.out.println("Id and string are already associated.");
                    return "granted";
                }
                System.out.println("String is associated with another id: " + id2);
                return "denied";
            } else if (command.equalsIgnoreCase("release")) {
                System.out.println("Process release for id " + id + ":\"" + string + "\"");
                Long id2 = (Long) hashtable.get(string);
                if (id2 != null && id2.longValue() == id) {
                    hashtable.remove(string);
                    System.out.println("Removed association.");
                } else System.out.println("Cannot release: held by " + id2);
            } else if (command.equalsIgnoreCase("releaseAll")) {
                System.out.println("Process releaseAll");
                releaseAll(id);
            }
        }
        return null;
    }

    protected void releaseAll(long clientId) {
        synchronized (hashtable) {
            Enumeration keys = hashtable.keys();
            Enumeration elements = hashtable.elements();
            Vector deathList = new Vector();
            while (keys.hasMoreElements() && elements.hasMoreElements()) {
                String key = (String) keys.nextElement();
                Long id = (Long) elements.nextElement();
                if (id.longValue() == clientId) deathList.addElement(key);
            }
            keys = deathList.elements();
            while (keys.hasMoreElements()) hashtable.remove(keys.nextElement());
            System.out.println("releaseAll: released " + deathList.size() + " items from " + clientId);
        }
    }
}
