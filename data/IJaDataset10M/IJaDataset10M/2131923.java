package org.imajie.server.web.imajiematch.matchsServers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.imajie.server.web.imajiematch.matchsServers.sockets.ImajieMatchClientProtocol;
import org.imajie.server.web.imajiematch.matchsServers.sockets.MainServerClientProtocol;

/**
 *
 * @author Carl Tremblay
 */
public class StartMatchsJspBean implements Serializable {

    private static Logger logger = Logger.getLogger(StartMatchsJspBean.class.getName());

    public static String match;

    public static boolean saveFinish;

    private String gameStarted;

    private static String username = "";

    private Boolean matchStarted = false;

    public static boolean processFinish = false;

    public String start(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        username = session.getAttribute("username").toString();
        match = request.getParameter("match");
        System.out.println("Match request received..." + match + "\n");
        String msg = startSelected(request);
        System.out.println("Start cartridge Process finish...\n");
        return msg;
    }

    public String start2(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String text = "";
        String media = "";
        String host = "localhost";
        try {
            Socket imajieMatchSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                long currentDate = System.currentTimeMillis() + 2000;
                while (System.currentTimeMillis() < currentDate) {
                }
                imajieMatchSocket = new Socket(host, Integer.parseInt(session.getAttribute("PlayerServerSocketPort").toString()));
                out = new PrintWriter(imajieMatchSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(imajieMatchSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: " + host + ":::::Port:." + Integer.parseInt(session.getAttribute("PlayerServerSocketPort").toString()));
            } catch (IOException e) {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (imajieMatchSocket != null) {
                    imajieMatchSocket.close();
                }
                System.err.println("Couldn't get I/O for the connection to: " + host + ":::::Port:." + Integer.parseInt(session.getAttribute("PlayerServerSocketPort").toString()));
            }
            String fromServer;
            String fromUser;
            ImajieMatchClientProtocol imp = new ImajieMatchClientProtocol();
            fromUser = imp.processInput(null, session, request);
            out.println(fromUser);
            while ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
                fromUser = imp.processInput(fromServer, session, request);
                if (fromUser != null) {
                    System.out.println(fromUser);
                    out.println(fromUser);
                }
                if (fromServer.equals("Bye.")) {
                    break;
                }
                if (fromServer.equals("Me?")) {
                    break;
                }
            }
            out.close();
            in.close();
            imajieMatchSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RefreshMatchsJspBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (session.getAttribute("DIALOG") == null) {
        }
        if (session.getAttribute("dialogTexts") != null) {
            text = session.getAttribute("dialogTexts").toString().replace("DIALOGTEXT", "");
        }
        if (session.getAttribute("dialogMedia") != null) {
            media = session.getAttribute("dialogMedia").toString().replace("DIALOGMEDIA", "");
        }
        if (session.getAttribute("button1") != null) {
        }
        if (session.getAttribute("button2") != null) {
        }
        if (session.getAttribute("callback") != null) {
        }
        String msg = "";
        if (matchStarted == true) {
            msg = session.getAttribute("DIALOG").toString();
        }
        return msg;
    }

    public String startSelected(HttpServletRequest request) {
        saveFinish = false;
        processFinish = false;
        HttpSession session = request.getSession(true);
        match = request.getParameter("match");
        gameStarted = "none";
        if (session.getAttribute("gameStarted") != null) {
            gameStarted = session.getAttribute("gameStarted").toString();
        }
        if (gameStarted.equals("none")) {
            System.out.println("Engine instance initiated...to: -" + username + "\n");
            int port = 4000;
            String host = "localhost";
            try {
                Socket imajieMatchSocket = null;
                PrintWriter out2 = null;
                BufferedReader in = null;
                try {
                    imajieMatchSocket = new Socket(host, port);
                    out2 = new PrintWriter(imajieMatchSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(imajieMatchSocket.getInputStream()));
                } catch (UnknownHostException e) {
                    System.err.println("Don't know about host: " + host + ".");
                } catch (IOException e) {
                    System.err.println("Couldn't get I/O for the connection to: " + host + ".");
                }
                String fromServer;
                String fromUser;
                MainServerClientProtocol imp = new MainServerClientProtocol(session, 1);
                fromUser = imp.processInput(null, session, request);
                out2.println(fromUser);
                while ((fromServer = in.readLine()) != null) {
                    System.out.println(fromServer);
                    fromUser = imp.processInput(fromServer, session, request);
                    if (fromUser != null) {
                        System.out.println(fromUser);
                        out2.println(fromUser);
                    }
                    if (fromServer.equals("Bye.")) {
                        break;
                    }
                    if (fromServer.equals("Me?")) {
                        break;
                    }
                }
                System.out.println("Process finish");
                out2.close();
                in.close();
                imajieMatchSocket.close();
                System.out.println("Process finish");
            } catch (IOException ex) {
                Logger.getLogger(StartMatchsJspBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            matchStarted = true;
            session.setAttribute("gameStarted", match);
            return "";
        }
        matchStarted = false;
        return "";
    }

    public static void waiting(int n) {
        long t0, t1;
        t0 = System.currentTimeMillis();
        do {
            t1 = System.currentTimeMillis();
        } while ((t1 - t0) < (n * 1000));
    }
}
