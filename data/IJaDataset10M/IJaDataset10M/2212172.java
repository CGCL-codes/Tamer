package org.sink.swing;

import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class ToolBar extends JToolBar {

    private static final String CHAT_ICON = "data/pixmaps/send-im.png";

    private static final String VIDEO_ICON = "data/pixmaps/video-preview.png";

    private static final String VIDEO_STOP_ICON = "data/pixmaps/video-stop.png";

    private static final String GTK_CONNECT = "data/pixmaps/stock_connect.png";

    private static final String GTK_DISCONNECT = "data/pixmaps/stock_disconnect.png";

    private JButton btnConnect;

    private JButton btnDisconnect;

    private JButton btnChat;

    private JButton btnVideo;

    private ActionListener listener;

    private BuddyListController blc;

    public ToolBar() {
        setBorderPainted(false);
        setFloatable(false);
        setRollover(true);
    }

    private URL getResource(String url) {
        return ClassLoader.getSystemResource(url);
    }

    public void initMainButtons(BuddyListController blc) {
        this.listener = blc;
        this.blc = blc;
        add(btnConnect = makeButton(ToolBar.GTK_CONNECT, "connect", "Connect to jabber server.", listener));
        add(btnChat = makeButton(ToolBar.CHAT_ICON, "newIM", "Send a text message to the selected buddy.", listener));
        add(btnVideo = makeButton(ToolBar.VIDEO_ICON, "videoChat", "Request a media chat with the selected buddy.", listener));
        setConnected(blc.isConnected());
        setWithOrWithoutBuddies(blc.hasBuddies());
    }

    protected JButton makeButton(String image, String actionCommand, String toolTipText, ActionListener listener) {
        URL iconurl = this.getResource(image);
        JButton button = new JButton(new ImageIcon(iconurl));
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(listener);
        return button;
    }

    public void setVideoInProgress(boolean inProgress) {
        URL iconurl;
        if (inProgress) {
            iconurl = this.getResource(VIDEO_STOP_ICON);
            btnVideo.setIcon(new ImageIcon(iconurl));
            btnVideo.setToolTipText("Stop the media chat");
        } else {
            iconurl = this.getResource(VIDEO_ICON);
            btnVideo.setIcon(new ImageIcon(iconurl));
            btnVideo.setToolTipText("Request a media chat with the selected buddy");
        }
    }

    public void setConnected(boolean connected) {
        URL iconurl;
        if (connected == false) {
            iconurl = this.getResource(GTK_CONNECT);
            btnConnect.setIcon(new ImageIcon(iconurl));
            btnConnect.setToolTipText("Connect to jabber server.");
        } else {
            iconurl = this.getResource(GTK_DISCONNECT);
            btnConnect.setIcon(new ImageIcon(iconurl));
            btnConnect.setToolTipText("Disconnect from jabber server.");
        }
    }

    public void setWithOrWithoutBuddies(boolean hasBuddies) {
        btnVideo.setEnabled(hasBuddies);
        btnChat.setEnabled(hasBuddies);
    }
}
