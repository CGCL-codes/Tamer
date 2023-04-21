package org.stumeikle.NeuroCoSA.Debug;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.swing.tree.*;
import javax.accessibility.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;
import org.stumeikle.NeuroCoSA.Brain;
import org.stumeikle.NeuroCoSA.BrainInfoService;
import org.stumeikle.NeuroCoSA.NIS.*;
import org.stumeikle.NeuroCoSA.PubSub.*;

/**
 * Debug info at the brain - body interface level
 *
 * @version 2006-halloween
 * @author Stuart Meikle
 */
public class BrainBodyDEBUG extends DemoModule implements ComponentListener, Subscriber {

    int windowCount = 0;

    JDesktopPane desktop = null;

    ImageIcon brainicon;

    ImageIcon smIcon1, smIcon2, smIcon3, smIcon4;

    ImageIcon icon1, icon2, icon3, icon4;

    private Brain iBrain;

    private JScrollPane iBISTree;

    JTree tree;

    public Integer FIRST_FRAME_LAYER = new Integer(1);

    public Integer DEMO_FRAME_LAYER = new Integer(2);

    public Integer PALETTE_LAYER = new Integer(3);

    public int FRAME0_X = 15;

    public int FRAME0_Y = 280;

    public int FRAME0_WIDTH = 320;

    public int FRAME0_HEIGHT = 230;

    public int FRAME_WIDTH = 225;

    public int FRAME_HEIGHT = 150;

    public int PALETTE_X = 375;

    public int PALETTE_Y = 20;

    public int PALETTE_WIDTH = 260;

    public int PALETTE_HEIGHT = 260;

    JCheckBox windowResizable = null;

    JCheckBox windowClosable = null;

    JCheckBox windowIconifiable = null;

    JCheckBox windowMaximizable = null;

    JTextField windowTitleField = null;

    JLabel windowTitleLabel = null;

    JInternalFrame iBrainFrame;

    JInternalFrame iBodyFrame;

    JInternalFrame iNervousSystemFrame;

    JInternalFrame iWorldFrame;

    DefaultMutableTreeNode[] iTreeStrings;

    Publication[] iTreePubs;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        BrainBodyDEBUG demo = new BrainBodyDEBUG(null);
        demo.mainImpl();
    }

    /**
     * Constructor
     */
    public BrainBodyDEBUG(NeuroCoSADEBUG swingset) {
        super(swingset, "BrainBodyDEBUG", "toolbar/bodybrain.gif");
        iBrain = swingset.getBrain();
        brainicon = createImageIcon("bodyandbrain3.jpg", getString("BrainBodyDEBUG.bg"));
        iBISTree = createTree();
        desktop = new BrainBodyDEBUGDesktopPane(brainicon.getImage());
        getDemoPanel().add(desktop, BorderLayout.CENTER);
        desktop.addComponentListener(this);
        try {
            JInternalFrame jif = new JInternalFrame();
            jif.setTitle("Brain");
            jif.setClosable(false);
            jif.setMaximizable(true);
            jif.setIconifiable(true);
            jif.setResizable(true);
            jif.setBounds(100, 100, 300, 300);
            iBrainFrame = jif;
            desktop.add(jif, 5);
            jif.getContentPane().add(iBISTree);
            jif.show();
            jif.setIcon(true);
            jif = new JInternalFrame();
            jif.setTitle("NervousSystem");
            jif.setClosable(false);
            jif.setMaximizable(true);
            jif.setIconifiable(true);
            jif.setResizable(true);
            jif.setBounds(100, 100, 300, 300);
            iNervousSystemFrame = jif;
            desktop.add(jif, 6);
            jif.show();
            jif.setIcon(true);
            jif = new JInternalFrame();
            jif.setTitle("Body");
            jif.setClosable(false);
            jif.setMaximizable(true);
            jif.setIconifiable(true);
            jif.setResizable(true);
            jif.setBounds(100, 100, 300, 300);
            iBodyFrame = jif;
            desktop.add(jif, 6);
            jif.show();
            jif.setIcon(true);
            jif = new JInternalFrame();
            jif.setTitle("World");
            jif.setClosable(false);
            jif.setMaximizable(true);
            jif.setIconifiable(true);
            jif.setResizable(true);
            jif.setBounds(100, 100, 300, 300);
            iWorldFrame = jif;
            desktop.add(jif, 6);
            jif.show();
            jif.setIcon(true);
        } catch (Exception e) {
        }
        positionIcons();
    }

    public void notifyActivated() {
        positionIcons();
    }

    public void positionIcons() {
        Dimension size = desktop.getSize();
        int width = size.width / 2;
        int height = size.height / 2;
        Component[] carray = desktop.getComponents();
        for (int j = 0; j < carray.length; j++) {
            Component c = carray[j];
            try {
                JInternalFrame.JDesktopIcon icon = (JInternalFrame.JDesktopIcon) c;
                if (icon.getInternalFrame() == iBrainFrame) {
                    icon.setLocation(width - 50, height - 200);
                }
                if (icon.getInternalFrame() == iWorldFrame) {
                    icon.setLocation(width - 200, height + 100);
                }
                if (icon.getInternalFrame() == iBodyFrame) {
                    icon.setLocation(width + 100, height);
                }
                if (icon.getInternalFrame() == iNervousSystemFrame) {
                    icon.setLocation(width, height - 100);
                }
            } catch (Exception e) {
            }
        }
    }

    public int getFrameWidth() {
        return FRAME_WIDTH;
    }

    public int getFrameHeight() {
        return FRAME_HEIGHT;
    }

    public Integer getDemoFrameLayer() {
        return DEMO_FRAME_LAYER;
    }

    void updateDragEnabled(boolean dragEnabled) {
        windowTitleField.setDragEnabled(dragEnabled);
    }

    public JScrollPane createTree() {
        ListIterator i = iBrain.getInfoService().getPublications().listIterator();
        DefaultMutableTreeNode record = null;
        BrainInfoService bis = iBrain.getInfoService();
        iTreeStrings = new DefaultMutableTreeNode[bis.getNumPublications() + 10];
        iTreePubs = new Publication[bis.getNumPublications() + 10];
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Brain Info Service");
        for (; i.hasNext(); ) {
            Info nis = (Info) i.next();
            record = null;
            try {
                InfoLong nl = (InfoLong) nis;
                record = new DefaultMutableTreeNode(nis.getName() + ":" + nl.getLongValue());
                iTreeStrings[(int) nl.getID()] = record;
                iTreePubs[(int) nl.getID()] = nl;
                nl.subscribe(this);
            } catch (Exception e) {
            }
            try {
                InfoString nl = (InfoString) nis;
                record = new DefaultMutableTreeNode(nis.getName() + ":" + nl.getValue());
                iTreeStrings[(int) nl.getID()] = record;
                iTreePubs[(int) nl.getID()] = nl;
                nl.subscribe(this);
            } catch (Exception e) {
            }
            if (record != null) top.add(record);
        }
        tree = new JTree(top) {

            public Insets getInsets() {
                return new Insets(5, 5, 5, 5);
            }
        };
        tree.setEditable(true);
        return new JScrollPane(tree);
    }

    public void componentResized(ComponentEvent e) {
        positionIcons();
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void notify(long iID, Object iValue, long iVersion) {
        DefaultMutableTreeNode record = iTreeStrings[(int) iID];
        Publication p = iTreePubs[(int) iID];
        if (p == null || record == null) return;
        try {
            InfoLong nl = (InfoLong) p;
            record.setUserObject(new String(nl.getName() + ":" + nl.getLongValue()));
        } catch (Exception e) {
            try {
                InfoString ns = (InfoString) p;
                record.setUserObject(new String(ns.getName() + ":" + ns.getValue()));
            } catch (Exception f) {
            }
        }
        iBrainFrame.repaint();
    }
}
