package org.rakiura.mbot;

import java.util.*;
import java.io.*;

/**
 * Main, bootstrapping class for the entire bot. Main startup for irc bot, eMbot.
 *
 * <br>
 * <br>Created: Sun Jun 13 18:35:34 1999<br>
 *
 *@author Mariusz Nowostawski
 *@version $Revision: 1.4 $
 */
public class Main {

    static String VERSION = "1.2beta ($Revision: 1.4 $)";

    static String sConfigFile = "embot.config";

    static String sChanFile = "embot.channels";

    static String sUsersFile = "embot.users";

    static boolean DEBUG = true;

    public static boolean DCCActive = true;

    public static String DCCversion = "eMbot ver" + VERSION + " by Marni";

    public static String DCCuserinfo = "eMbot user, so what?";

    public static String DCCosversion = "GNU NASA OS 0.127.5334beta by US Navy";

    Vector servers;

    Vector nicks;

    Hashtable channels;

    String host, user, ircname = "deep";

    int EDCCport, DCCport, MasterPort = 0, ircport = 6667;

    String EDCCpass, MasterHost = "#";

    boolean blind = true;

    boolean scripts = true;

    /**
   * Constructs the Engine object, and connect to the IRC server.
   */
    public Main() {
        System.out.println("\nLoading:   eMbot version " + VERSION);
        System.out.println("Copyright (C) 1998-2001 by Mariusz Nowostawski (Marni)");
        System.out.println("Project site at: http://sourceforge.net/projects/embot");
        System.out.println(" ");
        nicks = new Vector(2);
        channels = new Hashtable(5);
        servers = new Vector(5);
        ConfigProcess();
        Engine engine = new Engine(servers, ircport, nicks, channels, DCCport, EDCCport, EDCCpass, user, host, ircname, MasterHost, MasterPort, blind, scripts);
        engine.rejoinToServer();
    }

    /**
   * Process the configuration file.
   */
    public void ConfigProcess() {
        boolean koniec = false;
        BufferedReader in;
        String s = new String("");
        String buf_temp = new String("");
        String temp;
        int i;
        try {
            in = new BufferedReader(new FileReader(Main.sConfigFile));
            while (!koniec) {
                try {
                    s = in.readLine();
                    if (s == null) koniec = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    koniec = true;
                }
                if (s != null) {
                    if (!s.startsWith("#")) {
                        StringTokenizer tt = new StringTokenizer(s, "\t=\n\r");
                        if (tt.countTokens() == 2) {
                            buf_temp = tt.nextToken();
                            if (buf_temp.equals("Server")) {
                                temp = tt.nextToken();
                                servers.addElement(new String(temp));
                            } else if (buf_temp.equals("Nick")) {
                                temp = tt.nextToken();
                                nicks.addElement(new String(temp));
                            } else if (buf_temp.equals("IRCname")) {
                                ircname = tt.nextToken();
                            } else if (buf_temp.equals("BlindMODES")) {
                                blind = tt.nextToken().trim().toLowerCase().startsWith("true");
                            } else if (buf_temp.equals("SCRIPTS")) {
                                scripts = tt.nextToken().trim().toLowerCase().startsWith("true");
                            } else if (buf_temp.equals("EDCCport")) {
                                temp = tt.nextToken();
                                EDCCport = Integer.parseInt(temp);
                            } else if (buf_temp.equals("EDCCpass")) {
                                temp = tt.nextToken();
                                EDCCpass = temp;
                            } else if (buf_temp.equals("DCCport")) {
                                temp = tt.nextToken();
                                DCCport = Integer.parseInt(temp);
                            } else if (buf_temp.equals("IRCport")) {
                                temp = tt.nextToken();
                                ircport = Integer.parseInt(temp);
                            } else if (buf_temp.equals("Host")) {
                                temp = tt.nextToken();
                                host = new String(temp);
                            } else if (buf_temp.equals("User")) {
                                temp = tt.nextToken();
                                user = new String(temp);
                            } else if (buf_temp.equals("MasterHost")) {
                                temp = tt.nextToken();
                                MasterHost = new String(temp);
                            } else if (buf_temp.equals("MasterPort")) {
                                temp = tt.nextToken();
                                MasterPort = Integer.parseInt(temp);
                            } else if (buf_temp.equals("DCCversion")) {
                                temp = tt.nextToken();
                                DCCversion = temp;
                            } else if (buf_temp.equals("DCCuserinfo")) {
                                temp = tt.nextToken();
                                DCCuserinfo = temp;
                            } else if (buf_temp.equals("DCCosversion")) {
                                temp = tt.nextToken();
                                DCCosversion = temp;
                            } else if (buf_temp.equals("DCCActive")) {
                                DCCActive = tt.nextToken().trim().toLowerCase().startsWith("true");
                            }
                        } else if (tt.countTokens() > 2) {
                            if (buf_temp.equals("IRCname")) {
                                temp = tt.nextToken();
                                ircname = new String(temp);
                                while (tt.hasMoreTokens()) ircname = ircname + " " + tt.nextToken();
                            } else if (buf_temp.equals("DCCversion")) {
                                temp = tt.nextToken();
                                DCCversion = new String(temp);
                                while (tt.hasMoreTokens()) DCCversion = DCCversion + " " + tt.nextToken();
                            } else if (buf_temp.equals("DCCuserinfo")) {
                                temp = tt.nextToken();
                                DCCuserinfo = new String(temp);
                                while (tt.hasMoreTokens()) DCCuserinfo = DCCuserinfo + " " + tt.nextToken();
                            } else if (buf_temp.equals("DCCosversion")) {
                                temp = tt.nextToken();
                                DCCosversion = new String(temp);
                                while (tt.hasMoreTokens()) DCCosversion = DCCosversion + " " + tt.nextToken();
                            }
                        }
                    }
                }
            }
            System.out.println("Engine data read.");
            in.close();
        } catch (IOException e) {
            System.out.println("ERROR: Cannot access file with configuration!");
            System.out.println("       Please create <embot.config> <embot.users> and <embot.channels>");
            System.out.println("       files and put them in the local directory, for bot to read them.");
            System.exit(0);
        }
        try {
            in = new BufferedReader(new FileReader(Main.sChanFile));
            boolean ok = true;
            while (ok) {
                Channel chan = new Channel();
                ok = chan.readFromTextData(in);
                if (ok) channels.put(chan.getName(), chan);
            }
            in.close();
            System.out.println("Channel data read.");
        } catch (IOException ex) {
            System.out.println("ERROR: Cannot access file with channels!");
            System.exit(0);
        }
    }

    /**
   * Startup.
   */
    public static void main(String[] args) throws IOException {
        Main agent = new Main();
    }
}
