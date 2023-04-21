package ants.p2p.gui;

import java.awt.*;
import javax.swing.tree.*;
import javax.swing.*;
import java.beans.*;
import java.awt.event.*;
import ants.p2p.messages.*;
import ants.p2p.filesharing.*;
import org.apache.log4j.*;
import javax.swing.border.*;

public class UploadAntPanel extends JPanel implements PropertyChangeListener {

    public static String pic = "backgrounds/bg5.jpg";

    static Logger _logger = Logger.getLogger(UploadAntPanel.class.getName());

    Image buffer;

    JScrollPane jScrollPane1 = new JScrollPane();

    JLabel jLabel1 = new JLabel();

    DefaultMutableTreeNode uploadRoot = new DefaultMutableTreeNode("Uploads");

    DefaultTreeModel uploadTree = new DefaultTreeModel(uploadRoot);

    JTree jTree1 = new JTree(uploadTree);

    GuiAnt container = null;

    Border border1;

    FlowLayout flowLayout1 = new FlowLayout();

    public UploadAntPanel(GuiAnt container) {
        this.container = container;
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            MediaTracker mt = new MediaTracker(this);
            buffer = tk.getImage(this.getClass().getClassLoader().getResource(pic));
            mt.waitForAll();
        } catch (Exception e) {
        }
        try {
            jbInit();
        } catch (Exception ex) {
            _logger.error("", ex);
        }
    }

    void jbInit() throws Exception {
        border1 = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        this.setLayout(flowLayout1);
        jLabel1.setPreferredSize(new Dimension(400, 15));
        jLabel1.setText(ji.JI.i("Files in Upload"));
        jTree1.addMouseListener(new UploadAntPanel_jTree1_mouseAdapter(this));
        jTree1.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTree1.setOpaque(true);
        jTree1.setToolTipText(ji.JI.i("Current files in upload, altough you can interrupt an active upload " + "with the contestual menu (right click), it is better to leave them " + "running..."));
        jTree1.setRootVisible(false);
        jScrollPane1.setPreferredSize(new Dimension(400, 324));
        this.setBorder(border1);
        this.addComponentListener(new UploadAntPanel_this_componentAdapter(this));
        this.add(jLabel1, null);
        this.add(jScrollPane1, null);
        jScrollPane1.getViewport().add(jTree1, null);
    }

    public void interruptAllUploads() {
        DefaultMutableTreeNode fpmpInDownload = null;
        FilePullMessageProcessor fpmp = null;
        for (int x = 0; x < this.uploadRoot.getChildCount(); x++) {
            fpmpInDownload = (DefaultMutableTreeNode) this.uploadRoot.getChildAt(x);
            fpmp = ((FilePullMessageProcessor) fpmpInDownload.getUserObject());
            fpmp.terminate();
            this.uploadTree.removeNodeFromParent(fpmpInDownload);
            this.jTree1.setModel(this.uploadTree);
            this.uploadTree.reload();
            this.repaint();
        }
    }

    public void propertyChange(final PropertyChangeEvent e) {
        handleEvent(e);
    }

    public void handleEvent(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("filePullInit")) {
            synchronized (this) {
                this.uploadRoot.add(new DefaultMutableTreeNode((FilePullMessageProcessor) e.getNewValue()));
                this.jTree1.setModel(this.uploadTree);
                this.uploadTree.reload();
                this.repaint();
            }
        } else if (e.getPropertyName().equals("filePullUpdate")) {
            this.repaint();
        } else if (e.getPropertyName().equals("filePullError") || e.getPropertyName().equals("filePullEnd")) {
            synchronized (this) {
                DefaultMutableTreeNode fpmpInDownload = null;
                FilePullMessageProcessor fpmp = null;
                for (int x = 0; x < this.uploadRoot.getChildCount(); x++) {
                    fpmpInDownload = (DefaultMutableTreeNode) this.uploadRoot.getChildAt(x);
                    fpmp = ((FilePullMessageProcessor) fpmpInDownload.getUserObject());
                    if (fpmp.getFilePullMessage().getHash().equals(((FilePullMessage) e.getNewValue()).getHash())) {
                        this.uploadTree.removeNodeFromParent(fpmpInDownload);
                        this.jTree1.setModel(this.uploadTree);
                        this.uploadTree.reload();
                        this.repaint();
                        break;
                    }
                }
            }
        }
    }

    void jTree1_mousePressed(MouseEvent e) {
        int selRow = jTree1.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = jTree1.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1) {
            if (e.getClickCount() == 1 && (e.getButton() == MouseEvent.BUTTON3 || (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()))) {
                if (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject() instanceof FilePullMessageProcessor) {
                    FilePullMessageProcessor uploader = (FilePullMessageProcessor) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
                    AntPopupMenu popup = new AntPopupMenu(this, uploader, selRow, selPath);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    public void repaint() {
        super.repaint();
        this.setUploadsOverallSpeed();
    }

    public String getUploadsOverallSpeed() {
        double uploadOverallSpeed = 0;
        String s_uploadOverallSpeed = "0KB/s";
        int totalUploads = 0;
        if (this.uploadRoot != null) {
            for (int x = 0; x < this.uploadRoot.getChildCount(); x++) {
                FilePullMessageProcessor currentNode = (FilePullMessageProcessor) ((DefaultMutableTreeNode) this.container.uap.uploadRoot.getChildAt(x)).getUserObject();
                uploadOverallSpeed += currentNode.getSpeedValue();
            }
            s_uploadOverallSpeed = ("" + uploadOverallSpeed).substring(0, ("" + uploadOverallSpeed).indexOf(".") + 2) + "KB/s ";
            totalUploads = this.uploadRoot.getChildCount();
        }
        return ji.JI.i("Uploads") + " [" + ji.JI.i("Total") + ": " + totalUploads + " " + ji.JI.i("Speed") + ": " + s_uploadOverallSpeed + "]";
    }

    public void setUploadsOverallSpeed() {
        if (this.container != null && this.container.jTabbedPane1 != null) {
            Component[] tabs = this.container.jTabbedPane1.getComponents();
            for (int x = 0; tabs != null && x < tabs.length; x++) {
                if (tabs[x] == this) {
                    try {
                        this.container.jTabbedPane1.setIconAt(x, new LabelIcon(this.getUploadsOverallSpeed(), DoubleBuffer.getInstance().getButton(18), this));
                    } catch (Exception e) {
                    }
                    return;
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (buffer != null) {
            int posX = (this.getWidth() - 650) / 2;
            int posY = (this.getHeight() - 550) / 2;
            if (posX < 0) posX = 0;
            if (posY < 0) posY = 0;
            g.drawImage(buffer, posX, posY, this);
        }
    }

    void this_componentResized(ComponentEvent e) {
        this.jLabel1.setPreferredSize(new Dimension(this.getWidth() - 40, this.jLabel1.getHeight()));
        this.jScrollPane1.setPreferredSize(new Dimension(this.getWidth() - 40, this.getHeight() - 100));
        this.jLabel1.setSize(new Dimension(this.getWidth() - 40, this.jLabel1.getHeight()));
        this.jScrollPane1.setSize(new Dimension(this.getWidth() - 40, this.getHeight() - 100));
    }
}

class UploadAntPanel_jTree1_mouseAdapter extends java.awt.event.MouseAdapter {

    UploadAntPanel adaptee;

    UploadAntPanel_jTree1_mouseAdapter(UploadAntPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void mousePressed(MouseEvent e) {
        adaptee.jTree1_mousePressed(e);
    }
}

class UploadAntPanel_this_componentAdapter extends java.awt.event.ComponentAdapter {

    UploadAntPanel adaptee;

    UploadAntPanel_this_componentAdapter(UploadAntPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void componentResized(ComponentEvent e) {
        adaptee.this_componentResized(e);
    }
}
