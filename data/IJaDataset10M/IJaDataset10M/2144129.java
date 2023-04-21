package jSXtester;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

/**
 * <p>
 * Title: SX tester
 * </p>
 * <p>
 * Description: tester for the SX bus
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: GSoft
 * </p>
 * 
 * @author G. vd. Sel
 * @version 1.0
 */
public class clientThread extends Thread {

    private static Vector<String> commandMessages;

    private static Vector<String> receivedMessages;

    private Socket sSocket;

    private boolean bSending;

    private int iError;

    public static final int no_Error = 0x00000000;

    public static final int init_Error1 = 0x00000001;

    public static final int init_Error2 = 0x00000002;

    public static final int receive_Error = 0x00000004;

    public static final int send_Error = 0x00000008;

    public clientThread(Socket socket) {
        super("clientThread");
        stopSending();
        resetError();
        sSocket = socket;
    }

    public void stopSending() {
        bSending = false;
        if (commandMessages == null) {
            commandMessages = new Vector<String>();
        } else {
            commandMessages.clear();
        }
        if (receivedMessages == null) {
            receivedMessages = new Vector<String>();
        } else {
            receivedMessages.clear();
        }
    }

    public void sendMessage(String message) {
        if (commandMessages != null) {
            commandMessages.add(message);
        }
    }

    /**
	 * isMessageReceived: Report Line
	 * 
	 * returns false: no lines in buffer returns true: one or more line in
	 * buffer
	 * 
	 * @return boolean
	 */
    public boolean isMessageReceived() {
        if (receivedMessages != null) {
            return receivedMessages.size() > 0;
        }
        return false;
    }

    /**
	 * receivedMessage: get a line from the buffer
	 * 
	 * returns: empty if no line available
	 * 
	 * @return String
	 */
    public String receivedMessage() {
        String message;
        if (receivedMessages.size() > 0) {
            message = receivedMessages.elementAt(0);
            receivedMessages.removeElementAt(0);
            return message;
        }
        return "";
    }

    public void resetError() {
        iError = no_Error;
    }

    public int getError() {
        return iError;
    }

    public void run() {
        BufferedReader in;
        BufferedWriter out;
        String answer;
        iError = no_Error;
        try {
            answer = "";
            bSending = true;
            out = new BufferedWriter(new OutputStreamWriter(sSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
            if ((out != null) && (in != null)) {
                while (bSending) {
                    try {
                        if ((commandMessages.size() > 0) && (receivedMessages.size() == 0) && (answer == "")) {
                            out.write(commandMessages.elementAt(0) + "\n");
                            commandMessages.removeElementAt(0);
                            out.flush();
                        }
                    } catch (IOException e) {
                        iError |= send_Error;
                    }
                    try {
                        if (in.ready()) {
                            answer = answer + in.readLine();
                            if (answer.endsWith("\\")) {
                                answer.replace('\\', ' ');
                            } else {
                                receivedMessages.add(answer);
                                answer = "";
                            }
                        } else {
                            try {
                                sleep(100);
                            } catch (InterruptedException ex1) {
                            }
                        }
                    } catch (IOException e) {
                        iError |= receive_Error;
                    }
                }
                out.close();
                in.close();
            } else {
                iError |= init_Error1;
            }
        } catch (IOException ex) {
            iError |= init_Error2;
        }
    }
}
