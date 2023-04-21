package com.hp.hpl.MeetingMachine;

import java.net.*;
import java.io.*;
import javax.swing.*;

public class MM_ConfigurePointright {

    JPanel jPanel1 = new JPanel();

    JLabel mm_title = new JLabel();

    public MM_ConfigurePointright() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]) {
        MM_ConfigurePointright me = new MM_ConfigurePointright();
        String local_server = null;
        String prw2_ehl_file = "server.ehl";
        try {
            InetAddress thisIP = InetAddress.getLocalHost();
            FileOutputStream ehl = new FileOutputStream(prw2_ehl_file);
            PrintStream out = new PrintStream(ehl);
            String line = "EventHeapHost=" + thisIP.getHostAddress() + ":4535";
            out.println(line);
            ehl.close();
            System.out.println("Wrote \"" + line + "\" into " + prw2_ehl_file);
            local_server = "http://" + thisIP.getHostAddress() + ":8080/";
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        System.out.println("Trying to connect to " + local_server);
        try {
            Object content = null;
            while (content == null) {
                try {
                    URL server = new URL(local_server);
                    content = server.getContent();
                } catch (java.net.ConnectException ee) {
                    System.out.println("No connection to MM server, retrying...");
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(local_server + " up, exiting");
        return;
    }

    private void jbInit() throws Exception {
        mm_title.setFont(new java.awt.Font("Dialog", 0, 20));
        mm_title.setText("MeeingMachine Server");
        jPanel1.add(mm_title, null);
    }
}
