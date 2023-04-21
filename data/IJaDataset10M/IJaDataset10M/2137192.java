package iwork.manager.launcher;

import iwork.manager.core.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

/**
* @author Ulf Ochsenfahrt (ulfjack@stanford.edu)
 */
class ServicePanel extends TreePanel {

    public static final String PREFIX = Launcher.PREFIX;

    static class StatusEntry {

        String machine;

        String group;

        String service;

        boolean up;

        public StatusEntry(String machine, String group, String service, boolean up) {
            this.machine = machine;
            this.group = group;
            this.service = service;
            this.up = up;
        }
    }

    JCheckBox depCheckBox;

    protected DefaultMutableTreeNode getNodes() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("iRoom");
        TreeBuilder b = new TreeBuilder(top);
        for (int i = 0; i < hosts.size(); i++) {
            Loader il = (Loader) hosts.get(i);
            String[] services = il.getServices();
            String s = (String) il.getHandle() + (il.isAlive() ? "" : " (down)");
            b.enter(s);
            for (int j = 0; j < services.length; j++) {
                Matcher m = Loader.serviceLinePattern.matcher(services[j]);
                if (!m.matches()) throw new NullPointerException("XXX");
                if (depCheckBox.isSelected()) {
                    if ("".equals(m.group(2)) & "".equals(m.group(3))) b.add(m.group(1)); else {
                        b.enter(m.group(1));
                        String temp = m.group(2);
                        if (!"".equals(temp)) {
                            b.enter("local dependencies");
                            Matcher m2 = dependencyLinePattern.matcher(temp);
                            while (m2.find()) {
                                b.add(m2.group(1));
                            }
                            b.leave();
                        }
                        temp = m.group(3);
                        if (!"".equals(temp)) {
                            b.enter("remote dependencies");
                            Matcher m2 = dependencyLinePattern.matcher(temp);
                            while (m2.find()) {
                                b.add(m2.group(1));
                            }
                            b.leave();
                        }
                        b.leave();
                    }
                } else b.add(m.group(1));
            }
            b.leave();
        }
        return top;
    }

    void update() {
        for (int i = 0; i < hosts.size(); i++) {
            Loader il = (Loader) hosts.get(i);
            try {
                il.refreshServices();
            } catch (Exception e) {
                System.err.println(PREFIX + "Could not obtain services from \"" + il.getHandle() + "\"!");
            }
        }
        updateView();
    }

    AbstractAction updateAction = new AbstractAction("Update") {

        public void actionPerformed(ActionEvent event) {
            update();
        }
    };

    AbstractAction reloadAction = new AbstractAction("Reload") {

        public void actionPerformed(ActionEvent event) {
            DefaultMutableTreeNode node;
            TreePath[] paths = tree.getSelectionPaths();
            if (paths == null) return;
            for (int j = 0; j < paths.length; j++) {
                TreePath path = paths[j];
                if (path == null) return;
                if (path.getPathCount() != 2) return;
                node = (DefaultMutableTreeNode) path.getPathComponent(1);
                String machine = (String) node.getUserObject();
                Loader parent = findMachine(machine);
                try {
                    parent.reloadConfiguration();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            update();
        }
    };

    private void showErrorDialog(String host, String msg) {
        JOptionPane.showMessageDialog(this, msg, "Errors of " + host, JOptionPane.ERROR_MESSAGE);
    }

    AbstractAction showErrorsAction = new AbstractAction("Show Errors") {

        public void actionPerformed(ActionEvent event) {
            DefaultMutableTreeNode node;
            TreePath[] paths = tree.getSelectionPaths();
            if (paths == null) return;
            for (int j = 0; j < paths.length; j++) {
                TreePath path = paths[j];
                if (path == null) return;
                if (path.getPathCount() != 2) return;
                node = (DefaultMutableTreeNode) path.getPathComponent(1);
                String machine = (String) node.getUserObject();
                Loader parent = findMachine(machine);
                try {
                    showErrorDialog((String) parent.getHandle(), parent.reloadErrors());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            updateView();
        }
    };

    public JPanel getSettingsPanel() {
        depCheckBox = new JCheckBox("Show Dependencies");
        depCheckBox.addActionListener(updateViewAction);
        JPanel settingsPanel = new JPanel();
        settingsPanel.add(depCheckBox);
        settingsPanel.add(new JButton(reloadAction));
        settingsPanel.add(new JButton(showErrorsAction));
        return settingsPanel;
    }

    public void init() {
        super.init();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                reloadAction.setEnabled(false);
                showErrorsAction.setEnabled(false);
                TreePath path = tree.getSelectionPath();
                if (path == null) return;
                if (path.getPathCount() == 2) reloadAction.setEnabled(true);
                if (path.getPathCount() == 2) showErrorsAction.setEnabled(true);
            }
        });
        tree.setTransferHandler(new StringTransferHandler() {

            protected boolean mayExport() {
                return true;
            }

            protected String exportString(JComponent c) {
                DefaultMutableTreeNode node;
                StringBuffer buffer = new StringBuffer();
                TreePath[] paths = tree.getSelectionPaths();
                for (int j = 0; j < paths.length; j++) {
                    if (j > 0) buffer.append("\n");
                    TreePath path = paths[j];
                    if (path == null) return null;
                    if (path.getPathCount() != 3) return null;
                    for (int i = 1; i < path.getPathCount(); i++) {
                        if (i > 1) buffer.append(":");
                        node = (DefaultMutableTreeNode) path.getPathComponent(i);
                        buffer.append((String) node.getUserObject());
                    }
                }
                return buffer.toString();
            }
        });
        tree.setDragEnabled(true);
    }

    public ServicePanel(List hosts) {
        super("Installed Services:", hosts);
        reloadAction.setEnabled(false);
        showErrorsAction.setEnabled(false);
        init();
    }
}
