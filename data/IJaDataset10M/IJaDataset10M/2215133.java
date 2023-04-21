package org.chernovia.net.games.parlour.acro.client;

import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.net.*;
import java.io.*;

public class AcroApplet extends Applet implements KeyListener, Runnable {

    private static final long serialVersionUID = -4921518268100468289L;

    static final String CR = System.getProperty("line.separator");

    static final String ACRO_DG = "&";

    static final int DG_NEW_ACRO = 0;

    static final int DG_ACRO_TIME = 1;

    static final int DG_ACRO_ENTERED = 2;

    static final int DG_SHOW_ACROS = 3;

    static final int DG_NEXT_ACRO = 4;

    static final int DG_VOTE_TIME = 5;

    static final int DG_RESULTS = 6;

    static final int DG_SCORES = 7;

    static final int DG_SUMMARY = 8;

    static final int DEF_WIDTH = 640, DEF_HEIGHT = 480, MAX_TEXT = 4096;

    Panel gamePan;

    TextField cmdText;

    TextArea servText;

    Socket servSock;

    PrintWriter out;

    BufferedReader in;

    static int port = 5678;

    boolean PLAYING = false;

    public void init() {
        setLayout(new BorderLayout());
        servText = new TextArea(8, 25);
        cmdText = new TextField(8);
        cmdText.addKeyListener(this);
        gamePan = new Panel();
        gamePan.setLayout(new BorderLayout());
        gamePan.add(servText, BorderLayout.CENTER);
        gamePan.add(cmdText, BorderLayout.SOUTH);
        add(gamePan, BorderLayout.CENTER);
    }

    public void start() {
        connect();
    }

    public void connect() {
        while (servSock == null) {
            try {
                servSock = new Socket("localhost", port);
            } catch (Exception e) {
                blurb("Connection Error: " + e.getMessage());
                servSock = null;
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException i) {
                }
            }
        }
        blurb("Connected!  Please enter your handle.");
        try {
            out = new PrintWriter(servSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(servSock.getInputStream()));
        } catch (IOException e) {
            blurb("IO Error: " + e.getMessage());
            return;
        }
        if (servSock != null) new Thread(this).start();
    }

    public void run() {
        String line = "";
        while (connected()) {
            try {
                line = in.readLine();
            } catch (IOException augh) {
            }
            if (line.startsWith(ACRO_DG)) parseAcroDG(line); else blurb(line);
        }
    }

    public void parseAcroDG(String line) {
    }

    public boolean connected() {
        return servSock == null ? false : servSock.isConnected();
    }

    public void blurb(String txt) {
        blurb(txt, true);
    }

    public void blurb(String txt, boolean newline) {
        if (servText == null) {
            System.out.println(txt);
            return;
        } else if (newline) servText.append(txt + CR); else servText.append(txt);
        if (servText.getText().length() > MAX_TEXT) {
            String text = servText.getText();
            servText.setText(text.substring(text.length() - MAX_TEXT / 8));
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getSource() == cmdText && e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendServ(cmdText.getText());
            cmdText.setText("");
        }
    }

    public void sendServ(String txt) {
        if (connected()) out.println(txt);
    }
}
