package com.mibviewer.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.common.ui.controls.UtilTree;
import com.common.ui.gridbaglayout.GridBagUtils;
import com.mibviewer.common.HavingLifeCycle;
import com.mibviewer.ui.model.MibTreeNode;

public class MibViewerWindow implements HavingLifeCycle {

    private JFrame frame;

    private JPanel contentPanel;

    private UtilTree mibTree;

    public MibViewerWindow() {
        frame = new JFrame("MibViewer");
        frame.setSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPanel = new JPanel(new GridBagLayout());
        frame.setContentPane(contentPanel);
    }

    public void init() throws Exception {
        buildUI();
    }

    public void start() throws Exception {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public boolean stop() {
        return false;
    }

    public void dispose() {
    }

    private void buildUI() {
        mibTree = new UtilTree(new MibTreeNode("Loaded MIBS"));
        GridBagUtils.fillContainer(contentPanel, mibTree.getComponent());
    }
}
