package main;

import java.io.*;
import java.awt.*;
import message.*;
import server.RTServer;
import worker.RTWorker;
import client.RTClient;

/**
 *
 * @author Sean Halle
 */
public class RTMsgWindow {

    protected TextArea textArea;

    protected int currMsgNum = 1;

    /** Creates a new instance of RTMsgWindow */
    public RTMsgWindow(String frameName) {
        Frame f = new Frame(frameName);
        f.add("Center", textArea = new TextArea(70, 65));
        f.pack();
        f.setVisible(true);
    }

    public void displayMsg(String contextText, RTMsgBase msg) {
        textArea.append("Message: " + currMsgNum++ + "\n");
        textArea.append(contextText);
        textArea.append(" " + msg.toString() + "\n");
    }

    public void displayText(String msgText) {
        textArea.append(msgText + "\n");
    }
}
