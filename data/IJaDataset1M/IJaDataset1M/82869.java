package gui.listeners;

import map.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class MainComponentListener implements ComponentListener {

    private final Client2ServerInterface client2Server;

    private final JFrame mainFrame;

    public MainComponentListener(JFrame mainFrame, Client2ServerInterface client2Server) {
        this.mainFrame = mainFrame;
        this.client2Server = client2Server;
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        mainFrame.validate();
        Rectangle rect = mainFrame.getContentPane().getBounds();
        client2Server.mapStates.setVisibleDimension((int) rect.getWidth() - 250, (int) rect.getHeight());
        System.out.println("RESIZED");
        System.out.println(rect.toString());
    }

    public void componentShown(ComponentEvent e) {
    }
}
