package com.semp.gu.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class GalleryUploaderUI extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2645976361078606389L;

    private JMenuBar topMenuBar;

    private JComponent folderPanel;

    private JComponent imagePanel;

    private JComponent toolbarPanel;

    public void init() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                setTitle("Semp's gallery uploader");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(800, 600);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setJMenuBar(topMenuBar);
                Container contentPane = getContentPane();
                contentPane.setLayout(new BorderLayout());
                contentPane.add(folderPanel, BorderLayout.NORTH);
                contentPane.add(imagePanel, BorderLayout.CENTER);
                contentPane.add(toolbarPanel, BorderLayout.EAST);
                setVisible(true);
            }
        });
    }

    public JMenuBar getTopMenuBar() {
        return topMenuBar;
    }

    public void setTopMenuBar(JMenuBar topMenuBar) {
        this.topMenuBar = topMenuBar;
    }

    public JComponent getFolderPanel() {
        return folderPanel;
    }

    public void setFolderPanel(JComponent folderPanel) {
        this.folderPanel = folderPanel;
    }

    public JComponent getImagePanel() {
        return imagePanel;
    }

    public void setImagePanel(JComponent imagePanel) {
        this.imagePanel = imagePanel;
    }

    public JComponent getToolbarPanel() {
        return toolbarPanel;
    }

    public void setToolbarPanel(JComponent toolbarPanel) {
        this.toolbarPanel = toolbarPanel;
    }
}
