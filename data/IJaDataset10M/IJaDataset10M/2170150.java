package org.rakiura.mbot;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * PingThread.java
 * Created: Wed Jul 14 19:38:55 1999
 *
 *@author Mariusz Nowostawski
 *@version $Revision: 1.2 $
 */
public class PingThread extends SafeThread {

    Engine engine;

    PingThread(Engine f) {
        engine = f;
    }

    public void run() {
        try {
            String line, buf;
            int index;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (engine) {
                line = engine.readLine();
            }
            while (active) {
                while (line != null) {
                    if (line.startsWith(":")) line = line.substring(1);
                    if (Main.DEBUG) System.out.println(line);
                    StringTokenizer st = new StringTokenizer(line, " ");
                    if (st.hasMoreTokens()) {
                        buf = st.nextToken().trim();
                        if (buf.equals("PING")) {
                            String t = st.nextToken();
                            engine.out.println("PONG " + t);
                        } else if (buf.equals("ERROR") || (buf.equals("NOTICE"))) System.out.println(line); else if (buf.equals(engine.getNick())) System.out.println("*" + line); else if (buf.indexOf("!") == -1) {
                            String swhat;
                            if (st.hasMoreTokens()) swhat = st.nextToken(); else swhat = "0";
                            swhat = swhat.trim();
                            int what;
                            try {
                                what = Integer.parseInt(swhat);
                            } catch (NumberFormatException e) {
                                what = 0;
                                if (line.toLowerCase().indexOf(" mode ") != -1) {
                                    engine.modeHackVector.addString(line);
                                }
                            }
                            switch(what) {
                                case 321:
                                case 322:
                                case 323:
                                    engine.listLineVector.addString(line);
                                    break;
                                case 432:
                                case 433:
                                case 436:
                                    engine.nextNick();
                                    break;
                                case 346:
                                case 347:
                                    engine.chanInvitationLineVector.addString(line);
                                    break;
                                case 348:
                                case 349:
                                    engine.chanExceptionLineVector.addString(line);
                                    break;
                                case 352:
                                case 315:
                                    engine.whoLineVector.addString(line);
                                    break;
                                case 451:
                                    engine.nextNick();
                                    break;
                                case 471:
                                case 473:
                                case 474:
                                case 475:
                                    engine.joinRejectLineVector.addString(line);
                                case 482:
                                    if (!engine.BlindMODES) engine.IamOP.remove(engine.LastMode_chan.toLowerCase());
                                    break;
                                default:
                                    if (Main.DEBUG) System.out.println("??: " + line);
                                    break;
                            }
                        } else {
                            String what = st.nextToken().trim();
                            if (what.toLowerCase().equals("notice") || what.trim().toLowerCase().equals("privmsg")) engine.msgLineVector.addString(line); else if (what.toLowerCase().equals("kick")) engine.kickLineVector.addString(line); else if (what.toLowerCase().equals("join")) engine.joinLineVector.addString(line); else if (what.toLowerCase().equals("part") || what.toLowerCase().equals("quit")) engine.partLineVector.addString(line); else if (what.toLowerCase().equals("mode")) engine.modeLineVector.addString(line); else if (what.toLowerCase().equals("nick")) engine.nickLineVector.addString(line); else ;
                        }
                    }
                    line = engine.readLine();
                    while (line != null && line.startsWith(engine.getNick())) line = engine.readLine();
                }
                engine.rejoinToServer();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                line = "REJOIN... ";
            }
        } catch (Exception e) {
            System.out.println("Very BAD ERROR --- quiting..... \n Report this bug to the author, please!");
            e.printStackTrace();
            System.exit(0);
        }
    }

    /*********/
    public static final int RPL_LISTSTART = 321;

    public static final int RPL_LIST = 322;

    public static final int RPL_LISTEND = 323;

    public static final int RPL_WHOREPLY = 352;

    public static final int RPL_ENDOFWHO = 315;

    public static final int RPL_EXPNSTART = 346;

    public static final int RPL_EXPNEND = 347;

    public static final int RPL_INVSTART = 348;

    public static final int RPL_INVEND = 349;

    public static final int ERR_ERRONEUSNICKNAME = 432;

    public static final int ERR_NICKNAMEINUSE = 433;

    public static final int ERR_NICKCOLLISION = 436;

    public static final int ERR_CHANNELISFULL = 471;

    public static final int ERR_INVITEONLYCHAN = 473;

    public static final int ERR_BANNEDFROMCHAN = 474;

    public static final int ERR_BADCHANNELKEY = 475;

    public static final int ERR_CHANOPRIVSNEEDED = 482;
}
