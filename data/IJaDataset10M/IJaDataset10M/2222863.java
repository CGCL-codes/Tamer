package com.ymd.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import com.ymd.gui.listner.JTreeMouseListener;
import com.ymd.net.Packets;

/**
 * This is the main GUI window.
 * 
 * @author yaragalla Muralidhar
 *
 */
public class MainGui extends JFrame {

    private static final long serialVersionUID = -4819025906370311549L;

    private DefaultMutableTreeNode top;

    private JTree mainTree;

    /**
	 * This constructor configures and creates the main 
	 * GUI Interface for the chat application.
	 * @param title
	 * @param multicastSoc
	 * @param group
	 */
    public MainGui(String title, final MulticastSocket multicastSoc, final InetAddress group) {
        super(title);
        JDesktopPane dp = new JDesktopPane();
        dp.setLayout(new BorderLayout());
        top = new DefaultMutableTreeNode("All Online Systems..");
        mainTree = new JTree(top);
        mainTree.addMouseListener(new JTreeMouseListener());
        JScrollPane mtsp = new JScrollPane(mainTree);
        dp.add(mtsp, BorderLayout.CENTER);
        this.setContentPane(dp);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                try {
                    Packets.sendGoodbyePacket(multicastSoc, group);
                    multicastSoc.leaveGroup(group);
                } catch (IOException ioe) {
                    System.out.println(ioe);
                }
                System.exit(0);
            }
        });
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(200, 550);
        this.setVisible(true);
    }

    public DefaultMutableTreeNode getTop() {
        return top;
    }

    public JTree getMainTree() {
        return mainTree;
    }
}
