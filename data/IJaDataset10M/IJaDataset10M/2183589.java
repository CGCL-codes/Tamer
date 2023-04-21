package com.camera;

import com.*;
import ade.*;
import com.laser.Leg;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

public class testCameraServer extends Thread {

    private static String prg = "testCameraServer";

    private String gotStr;

    private boolean useGenericServ = true;

    private CameraServer gcsi;

    private boolean gotGenericServ = false;

    private Updater u;

    private Thread t;

    private boolean mainRun = true;

    private String myUID = "admin";

    private BufferedReader br;

    public testCameraServer() {
        if (useGenericServ) {
            getGenericCliServerClient();
        } else {
            System.out.println("To test the server, useGenericServ must be true!");
            System.exit(-1);
        }
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.err.println("Error getting input stream:");
            System.err.println(e);
            System.exit(-1);
        }
    }

    private void getGenericCliServerClient() {
        try {
            ADERegistry ar = (ADERegistry) Naming.lookup("rmi://127.0.0.1:1099/ADERegistryImpl$ADERegistry");
            if ((gcsi = (CameraServer) ar.requestConnection(myUID, myUID, "CameraServerImpl", "any")) != null) {
                gotGenericServ = true;
                u = new Updater(myUID);
                u.start();
                System.out.println(prg + ": Got CameraServer reference " + gcsi);
            } else {
                System.out.println(prg + ": Error getting CameraServer reference");
            }
        } catch (Exception e) {
            gotGenericServ = false;
            System.err.println(prg + ": Exception getting CameraServer reference:");
            System.err.println(e);
        }
    }

    class Updater extends Thread {

        String myUID;

        boolean shouldRun = true;

        public Updater(String uid) {
            myUID = uid;
        }

        public void run() {
            while (shouldRun) {
                try {
                    gcsi.updateConnection(myUID);
                    Thread.sleep(2000);
                } catch (RemoteException re) {
                    System.err.println("RemoteException updating (setting mainRun to false):");
                    System.err.println(re);
                    mainRun = false;
                    shouldRun = false;
                } catch (Exception ignore) {
                }
            }
        }

        public void halt() {
            shouldRun = false;
        }
    }

    public void run() {
        String send = "";
        System.out.println(prg + ": starting");
        while (mainRun) {
            try {
                send = br.readLine();
                if (send.equalsIgnoreCase("close") || send.equalsIgnoreCase("exit") || send.equalsIgnoreCase("quit")) {
                    break;
                }
                parseKeyboard(send);
            } catch (Exception e) {
                System.err.println(prg + ": error in run()");
                System.err.println(e);
            }
        }
        u.halt();
    }

    public static void main(String[] args) {
        testCameraServer tgs = new testCameraServer();
        tgs.start();
    }

    private void parseKeyboard(String ip) {
        String[] tokens = ip.split("\\s");
        if (gotGenericServ) {
            try {
                if (tokens[0].equalsIgnoreCase("init")) {
                    System.out.println("testing init");
                    gcsi.init();
                } else if (tokens[0].equalsIgnoreCase("cancelLastCommand")) {
                    System.out.println("testing cancelLastCommand");
                    gcsi.cancelLastCommand();
                } else if (tokens[0].equalsIgnoreCase("fullStop")) {
                    System.out.println("testing fullStop");
                    gcsi.fullStop();
                } else if (tokens[0].equalsIgnoreCase("pan")) {
                    System.out.println("testing pan | args=[" + tokens[1] + "]");
                    gcsi.pan(new Integer(tokens[1]));
                } else if (tokens[0].equalsIgnoreCase("panRelative")) {
                    System.out.println("testing panRelative | args=[" + tokens[1] + "]");
                    gcsi.panRelative(new Integer(tokens[1]));
                } else if (tokens[0].equalsIgnoreCase("tilt")) {
                    System.out.println("testing tilt | args=[" + tokens[1] + "]");
                    gcsi.tilt(new Integer(tokens[1]));
                } else if (tokens[0].equalsIgnoreCase("tiltRelative")) {
                    System.out.println("testing tiltRelative | args=[" + tokens[1] + "]");
                    gcsi.tiltRelative(new Integer(tokens[1]));
                } else if (tokens[0].equalsIgnoreCase("panTilt")) {
                    System.out.println("testing panTilt | args=[" + tokens[1] + "," + tokens[2] + "]");
                    gcsi.panTilt(new Integer(tokens[1]), new Integer(tokens[2]));
                } else if (tokens[0].equalsIgnoreCase("panTiltRelative")) {
                    System.out.println("testing panTiltRelative | args=[" + tokens[1] + "," + tokens[2] + "]");
                    gcsi.panTiltRelative(new Integer(tokens[1]), new Integer(tokens[2]));
                } else if (tokens[0].equalsIgnoreCase("getPan")) {
                    System.out.println("pan = " + gcsi.getPanDeg() + " degrees");
                } else if (tokens[0].equalsIgnoreCase("getTilt")) {
                    System.out.println("tilt = " + gcsi.getTiltDeg() + " degrees");
                } else if (tokens[0].equalsIgnoreCase("getZoom")) {
                    System.out.println("zoom = " + gcsi.getZoom());
                } else if (tokens[0].equalsIgnoreCase("getPanSpeed")) {
                    System.out.println("pan speed  = " + gcsi.getPanSpeed() + " degrees/sec");
                } else if (tokens[0].equalsIgnoreCase("getTiltSpeed")) {
                    System.out.println("tilt speed  = " + gcsi.getTiltSpeed() + " degrees/sec");
                } else if (tokens[0].equalsIgnoreCase("getMaxPanSpeed")) {
                    System.out.println("max pan speed  = " + gcsi.getMaxPanSpeed() + " degrees/sec");
                } else if (tokens[0].equalsIgnoreCase("getMaxTiltSpeed")) {
                    System.out.println("max tilt speed  = " + gcsi.getMaxTiltSpeed() + " degrees/sec");
                } else if (tokens[0].equalsIgnoreCase("setPanSpeed")) {
                    gcsi.setPanSpeed(Integer.valueOf(tokens[1]));
                } else if (tokens[0].equalsIgnoreCase("setTiltSpeed")) {
                    gcsi.setTiltSpeed(Integer.valueOf(tokens[1]));
                } else {
                    System.out.println("Unrecognized command to send: " + tokens[0]);
                }
            } catch (Exception e) {
                System.err.println("Exception parsing keyboard input:");
                System.err.println(e);
            }
        } else {
            System.out.println("Don't have a reference to the server");
        }
    }
}
