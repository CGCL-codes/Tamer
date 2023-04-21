package at.filemonkey.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import at.filemonkey.controller.MonkeyMainController;
import at.filemonkey.data.FTPSite;

public class MonkeyMainView extends JFrame implements WindowListener {

    private StatusBar statusbar;

    private QuickConnectBar quickconnectbar;

    private MonkeyMenuBar monkeymenubar;

    private MonkeyMainController controller;

    private FileListPanel filelistpanel;

    private FileTreeListPanel filetreelistpanel;

    private JSplitPane split;

    public MonkeyMainView(MonkeyMainController controller) {
        super();
        this.pack();
        this.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 1024;
        int height = 768;
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        this.setBounds(x, y, width, height);
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle(controller.getProperty("title"));
        ImageIcon filemonkeyicon = new ImageIcon(controller.getProperty("filemonkey"));
        this.setIconImage(filemonkeyicon.getImage());
        this.controller = controller;
        initComponents();
        layoutComponents();
        this.addWindowListener(this);
    }

    public void initComponents() {
        statusbar = new StatusBar(controller);
        quickconnectbar = new QuickConnectBar(controller);
        monkeymenubar = new MonkeyMenuBar(controller);
        filelistpanel = new FileListPanel(controller);
        filelistpanel.setDirectoryNavigator(controller.getFtpNavigator());
        filelistpanel.setTransferHandler(controller.getFtpTransferHandler());
        filetreelistpanel = new FileTreeListPanel(controller);
        filetreelistpanel.setDirectoryNavigator(controller.getLocalNavigator());
        filetreelistpanel.setTransferHandler(controller.getLocalTransferHandler());
        split = new JSplitPane();
        split.setOneTouchExpandable(true);
    }

    public void layoutComponents() {
        this.setLayout(new BorderLayout());
        this.setJMenuBar(monkeymenubar);
        this.add(quickconnectbar, BorderLayout.NORTH);
        this.add(statusbar, BorderLayout.SOUTH);
        split.setLeftComponent(new JScrollPane(filetreelistpanel));
        split.setRightComponent(new JScrollPane(filelistpanel));
        this.add(split, BorderLayout.CENTER);
    }

    public void setStatus(String status, Color color) {
        statusbar.setStatus(status, color);
    }

    public StatusBar getStatusbar() {
        return statusbar;
    }

    private void setStatusbar(StatusBar statusbar) {
        this.statusbar = statusbar;
    }

    public void windowClosing(WindowEvent e) {
        controller.exit();
    }

    public void updateFilePanels() {
        filelistpanel.updateListAndURI();
        filetreelistpanel.updateListAndURI();
    }

    public void setConnected(FTPSite site) {
        if (site != null) {
            quickconnectbar.getHosttf().setText(site.getHost());
            quickconnectbar.getUsertf().setText(site.getUser());
            quickconnectbar.getPasstf().setText(site.getPassword());
            quickconnectbar.getPorttf().setText(Integer.toString(site.getPort()));
            try {
                quickconnectbar.setConnection(true);
                quickconnectbar.updateButton(quickconnectbar.isConnection());
            } catch (IOException e) {
                setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
            }
        } else {
            try {
                quickconnectbar.setConnection(false);
                quickconnectbar.updateButton(quickconnectbar.isConnection());
            } catch (IOException e) {
                setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
            }
        }
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }
}
