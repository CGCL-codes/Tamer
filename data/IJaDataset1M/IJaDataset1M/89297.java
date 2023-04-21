package org.kati;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

/**
 * 
 * @author csn
 *
 */
public class KWindow extends JFrame implements WindowListener {

    private static final long serialVersionUID = 1L;

    public Kati k;

    /**
	 * 
	 *
	 */
    public KWindow(Kati k) {
        super("Kati xml Editor.");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        this.k = k;
        KatiMenu kBar = new KatiMenu(k);
        JScrollPane treeView = new JScrollPane(this.k.ktree);
        this.k.kMes = new KMessenger();
        JScrollPane msgView = new JScrollPane(this.k.kMes);
        this.k.kEd = new KEditor(k);
        JScrollPane editorView = new JScrollPane(this.k.kEd);
        JPanel treePanel = new JPanel(new GridLayout(1, 0));
        JTabbedPane tabbedPanel = new JTabbedPane();
        this.k.kCom = new KatiCommand(this.k);
        JScrollPane comView = new JScrollPane(this.k.kCom);
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        tabbedPanel.addTab("Editor", editorView);
        tabbedPanel.addTab("Messages", msgView);
        treePanel.add(treeView);
        setJMenuBar(kBar);
        rightSplitPane.setOneTouchExpandable(true);
        rightSplitPane.setDividerLocation(250);
        rightSplitPane.setTopComponent(comView);
        rightSplitPane.setBottomComponent(tabbedPanel);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerLocation(150);
        mainSplitPane.setLeftComponent(treePanel);
        mainSplitPane.setRightComponent(rightSplitPane);
        add(mainSplitPane);
        setSize(800, 600);
        setVisible(true);
    }

    /**
	 * 
	 * @return
	 */
    public XmlTree getTree() {
        return this.k.ktree;
    }

    public void windowActivated(WindowEvent arg0) {
    }

    public void windowClosed(WindowEvent arg0) {
    }

    public void windowClosing(WindowEvent we) {
        this.k.ktree.closeDocument();
        dispose();
    }

    public void windowDeactivated(WindowEvent arg0) {
    }

    public void windowDeiconified(WindowEvent arg0) {
    }

    public void windowIconified(WindowEvent arg0) {
    }

    public void windowOpened(WindowEvent arg0) {
    }
}
