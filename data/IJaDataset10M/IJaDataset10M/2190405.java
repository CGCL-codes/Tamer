package org.lacson.jscripture;

import org.lacson.*;
import org.lacson.utils.*;
import org.lacson.toolkit.*;
import java.io.*;
import java.net.*;

/**
 * $Id: Main.java,v 1.2 2001/11/12 08:15:36 placson Exp $
 * Main loader for suite of Jscripture applications
 *
 * @author Patrick Lacson (patrick@lacson.org)
 */
public class Main {

    public static void main(String[] args) {
        if (args.length <= 0) {
            showOptions();
        } else {
            if (args[0].equals("jsclient")) {
                System.out.println("Launching JSClient!");
                JSClient jsc = new JSClient();
                jsc.setVisible(true);
                jsc.start();
            } else if (args[0].equals("gospelcom")) {
                GospelComDriver.main(args);
            } else if (args[0].equals("chatclient")) {
                if (args.length < 4) {
                    System.out.println("Usage: ChatClient host port id <optional-debug>");
                    System.exit(1);
                }
                String host = args[1];
                int portNumber = Integer.parseInt(args[2]);
                String id = args[3];
                ChatClient td = new ChatClient(host, portNumber, id);
                td.setVisible(true);
                if (args.length == 5) if (args[4].equalsIgnoreCase("debug")) td.setDebug(true);
                td.start();
            } else if (args[0].equals("chatserver")) {
                int port = 19760;
                String groupName = "JScripture Chat Room";
                int MAX_CLIENTS = 10;
                if (args.length == 4) {
                    try {
                        groupName = args[1];
                        port = Integer.parseInt(args[2]);
                        MAX_CLIENTS = Integer.parseInt(args[3]);
                    } catch (NumberFormatException nfe) {
                    }
                }
                try {
                    ServerSocket server = new ServerSocket(port);
                    ThreadGroup tg = new ThreadGroup(groupName);
                    System.out.println("Starting " + groupName + " Room!");
                    while (true) {
                        Socket connection = server.accept();
                        ChatServer cs = new ChatServer(tg, groupName, connection, MAX_CLIENTS);
                        cs.start();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                showOptions();
            }
        }
    }

    public static void showOptions() {
        System.out.println("Usage: Main <option-list>");
        System.out.println();
        System.out.println();
        System.out.println("(case-sensitive) Option List");
        System.out.println("------------");
        System.out.println("jsclient");
        System.out.println("chatclient");
        System.out.println("chatserver");
        System.out.println("gospelcom");
    }
}
