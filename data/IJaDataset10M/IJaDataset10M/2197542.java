package net.sourceforge.simplegamenet.strategymaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Frederik
 * Date: 1-okt-2004
 * Time: 16:13:14
 * To change this template use File | Settings | File Templates.
 */
public class SMSpyPanel extends JPanel implements MouseListener {

    private ImageIcon smSpy;

    private SMPlayerClient smPlayerClient;

    SMSpyPanel(SMPlayerClient smPlayerClient) {
        smSpy = new ImageIcon(getClass().getResource("SMPieces.png"));
        this.smPlayerClient = smPlayerClient;
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(smSpy.getImage(), 0, 0, 40, 40, 360, 0, 400, 40, this);
    }

    public void mouseClicked(MouseEvent e) {
        smPlayerClient.setMessageSMPanel("Choose a place on the map to put your Spy");
        smPlayerClient.setSMSpyState(true);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
