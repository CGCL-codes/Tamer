package com.ymd.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import com.ymd.gui.ChatGui;

/**
 * This is the thread which runs the chat client 
 * window.
 * 
 * @author yaragalla Muralidhar
 *
 */
public class ChatClient implements Runnable {

    String ip;

    public ChatClient(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            socket = new Socket(ip, 1986);
            InetAddress recipentAddr = socket.getInetAddress();
            String recipentIp = recipentAddr.getHostAddress();
            String recipentName = recipentAddr.getHostName();
            in = socket.getInputStream();
            out = socket.getOutputStream();
            ChatGui chat = new ChatGui(recipentIp + ":-IPMessenger", out);
            JTextPane mainArea = chat.getMa();
            Document doc = mainArea.getDocument();
            SimpleAttributeSet bold = new SimpleAttributeSet();
            StyleConstants.setBold(bold, true);
            StringBuffer msg = new StringBuffer();
            while (true) {
                int value = in.read();
                if (value == 255) break;
                if (value != 254) {
                    char ch = (char) value;
                    msg.append(ch);
                } else {
                    try {
                        doc.insertString(doc.getLength(), recipentName + " : ", bold);
                        doc.insertString(doc.getLength(), msg.toString() + "\n", null);
                    } catch (BadLocationException ble) {
                        System.out.println(ble);
                    }
                    msg = new StringBuffer();
                }
            }
        } catch (Exception ioe) {
            System.out.println(ioe);
        } finally {
            try {
                out.write(-1);
                in.close();
                out.close();
                socket.close();
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }
}
