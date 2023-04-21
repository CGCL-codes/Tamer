package org.thenesis.planetino2.game;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import org.thenesis.planetino2.graphics.Color;
import org.thenesis.planetino2.graphics3D.Overlay;
import org.thenesis.planetino2.math3D.ViewWindow;

public class MessageQueue implements Overlay {

    static class Message {

        String text;

        long remainingTime;
    }

    private static final long MESSAGE_TIME = 5000;

    private static final long MAX_SIZE = 10;

    private static MessageQueue instance;

    private Vector messages;

    private boolean debug;

    public static synchronized MessageQueue getInstance() {
        if (instance == null) {
            instance = new MessageQueue();
        }
        return instance;
    }

    private MessageQueue() {
        messages = new Vector();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void debug(String text) {
        if (debug) {
            add(text);
        }
    }

    public boolean isEnabled() {
        return true;
    }

    public void add(String text) {
        Message message = new Message();
        message.text = text;
        message.remainingTime = MESSAGE_TIME;
        messages.addElement(message);
        if (messages.size() > MAX_SIZE) {
            messages.removeElementAt(0);
        }
    }

    public void update(long elapsedTime) {
        Enumeration i = messages.elements();
        while (i.hasMoreElements()) {
            Message message = (Message) i.nextElement();
            message.remainingTime -= elapsedTime;
            if (message.remainingTime < 0) {
                messages.removeElement(message);
            }
        }
    }

    public void draw(Graphics g, ViewWindow window) {
        int fontHeight = Font.getDefaultFont().getHeight();
        int x = window.getLeftOffset();
        int y = window.getTopOffset();
        g.setColor(Color.WHITE.getRGB());
        Enumeration i = messages.elements();
        while (i.hasMoreElements()) {
            String text = ((Message) i.nextElement()).text;
            g.drawString(text, x, y, Graphics.TOP | Graphics.LEFT);
            y += fontHeight;
        }
    }
}
