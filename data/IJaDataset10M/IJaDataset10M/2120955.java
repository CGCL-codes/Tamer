package com.nex.context.tm;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.*;
import java.util.*;
import com.nex.context.*;

/**
 * Title:        Nexist
 * Description:  Collaboratory testbed
 * Copyright:    Copyright (c) 2001 Jack Park
 * Company:      nex
 * @author       Jack Park
 * @version 1.0
 * @license  NexistLicense (based on Apache)
 */
public class TopicPanel extends JPanel {

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel jPanel1 = new JPanel();

    FlowLayout flowLayout1 = new FlowLayout();

    JLabel jLabel1 = new JLabel();

    JComboBox topicMapCombo = new JComboBox();

    DefaultComboBoxModel topicMapComboModel = new DefaultComboBoxModel();

    JScrollPane jScrollPane1 = new JScrollPane();

    JList topicList = new JList();

    DefaultListModel topicModel = new DefaultListModel();

    TitledBorder titledBorder1;

    Border border1;

    TitledBorder titledBorder2;

    MainFrame host = null;

    JButton newTopicButton = new JButton();

    JButton deleteTopicButton = new JButton();

    JPanel jPanel2 = new JPanel();

    JButton newTopicMapButton = new JButton();

    NewTopicMapDialog newTMDialog = null;

    NewTopicDialog newTopicDialog = null;

    NEXClient adaptor = null;

    String currentTopicID = null;

    String currentTopicMapID = null;

    Vector topicIDVec = null;

    boolean isFilling = false;

    JButton deleteTMButton = new JButton();

    XTMPanel intermediary = null;

    TitledBorder titledBorder3;

    JButton importButton = new JButton();

    JButton exportButton = new JButton();

    JButton mergeButton = new JButton();

    public TopicPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHost(MainFrame h, XTMPanel inter) {
        this.host = h;
        this.intermediary = inter;
    }

    public void setAdaptor(NEXClient ad) {
        this.adaptor = ad;
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        border1 = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new Color(151, 145, 140), new Color(105, 101, 98));
        titledBorder2 = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.blue, Color.blue, Color.gray, Color.gray), "Topic IDs");
        titledBorder3 = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.blue, Color.blue, new Color(151, 145, 140), new Color(105, 101, 98)), "Topic Map");
        this.setLayout(borderLayout1);
        jPanel1.setLayout(flowLayout1);
        jLabel1.setToolTipText("");
        jLabel1.setText("Topic Map:");
        topicMapCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                topicMapCombo_actionPerformed(e);
            }
        });
        topicList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                topicList_mouseClicked(e);
            }
        });
        jScrollPane1.setBorder(titledBorder2);
        newTopicButton.setEnabled(false);
        newTopicButton.setBorder(BorderFactory.createRaisedBevelBorder());
        newTopicButton.setToolTipText("Add a new Topic to this Topic Map");
        newTopicButton.setText("New Topic");
        newTopicButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newTopicButton_actionPerformed(e);
            }
        });
        deleteTopicButton.setEnabled(false);
        deleteTopicButton.setBorder(BorderFactory.createRaisedBevelBorder());
        deleteTopicButton.setToolTipText("Delete selected Topic");
        deleteTopicButton.setText("Delete Topic");
        deleteTopicButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteTopicButton_actionPerformed(e);
            }
        });
        newTopicMapButton.setBorder(BorderFactory.createRaisedBevelBorder());
        newTopicMapButton.setToolTipText("Create a new Topic Map");
        newTopicMapButton.setText("New ");
        newTopicMapButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newTopicMapButton_actionPerformed(e);
            }
        });
        deleteTMButton.setEnabled(false);
        deleteTMButton.setBorder(BorderFactory.createRaisedBevelBorder());
        deleteTMButton.setToolTipText("Delete Selected Topic Map");
        deleteTMButton.setText("Delete ");
        deleteTMButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteTMButton_actionPerformed(e);
            }
        });
        jPanel2.setBorder(titledBorder3);
        importButton.setBorder(BorderFactory.createRaisedBevelBorder());
        importButton.setToolTipText("Import an XTM file");
        importButton.setText("Import");
        importButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                importButton_actionPerformed(e);
            }
        });
        exportButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exportButton.setToolTipText("Export current Topic Map");
        exportButton.setText("Export");
        exportButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exportButton_actionPerformed(e);
            }
        });
        mergeButton.setBorder(BorderFactory.createRaisedBevelBorder());
        mergeButton.setToolTipText("Merge current Topic Map with selected Topic Map");
        mergeButton.setText("Merge");
        mergeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mergeButton_actionPerformed(e);
            }
        });
        this.add(jPanel1, BorderLayout.NORTH);
        jPanel1.add(jLabel1, null);
        jPanel1.add(topicMapCombo, null);
        jPanel1.add(newTopicButton, null);
        jPanel1.add(deleteTopicButton, null);
        this.add(jScrollPane1, BorderLayout.CENTER);
        this.add(jPanel2, BorderLayout.SOUTH);
        jPanel2.add(newTopicMapButton, null);
        jPanel2.add(deleteTMButton, null);
        jPanel2.add(importButton, null);
        jPanel2.add(exportButton, null);
        jPanel2.add(mergeButton, null);
        jScrollPane1.getViewport().add(topicList, null);
        topicMapCombo.setModel(topicMapComboModel);
        topicList.setModel(topicModel);
    }

    public void setTopicMapIDs(Vector v) {
        isFilling = true;
        if (v != null) {
            topicMapComboModel.removeAllElements();
            for (int i = 0; i < v.size(); i++) topicMapComboModel.addElement(v.elementAt(i));
        }
        isFilling = false;
        deleteTMButton.setEnabled(false);
        topicModel.clear();
    }

    void fillList() {
        topicMapComboModel.removeAllElements();
        adaptor.getTopicMapIDs();
    }

    public void clearTopics() {
        topicModel.clear();
    }

    public void addTopicName(String id) {
        topicModel.addElement(id);
    }

    public void addTopicMap(String id) {
        topicMapComboModel.addElement(id);
        topicMapComboModel.setSelectedItem(id);
    }

    /**
   * User has selected a TopicMap to display
   */
    void topicMapCombo_actionPerformed(ActionEvent e) {
        if (isFilling) return;
        Object top = topicMapCombo.getSelectedItem();
        if (top == null) return;
        this.currentTopicMapID = (String) top;
        host.setWaitCursor(true);
        adaptor.setSelectedTopicMap((String) top);
        deleteTMButton.setEnabled(true);
        newTopicButton.setEnabled(true);
        intermediary.newTopicSelected();
    }

    public void setTopicIDs(Vector v) {
        if (v == null) return;
        this.topicIDVec = v;
        clearTopics();
        if (v != null) {
            for (int i = 0; i < v.size(); i++) addTopicName((String) v.elementAt(i));
        }
    }

    public Vector getTopicIDs() {
        return this.topicIDVec;
    }

    void topicList_mouseClicked(MouseEvent e) {
        Object obj = topicList.getSelectedValue();
        if (obj == null) return;
        this.currentTopicID = (String) obj;
        intermediary.setCurrentTopicID(currentTopicID);
        host.setWaitCursor(true);
        adaptor.setSelectedTopic((String) obj);
        deleteTopicButton.setEnabled(true);
        intermediary.newTopicSelected();
    }

    public String getCurrentTopicID() {
        return this.currentTopicID;
    }

    void newTopicButton_actionPerformed(ActionEvent e) {
        System.out.println("New Topic");
        if (newTopicDialog == null) {
            newTopicDialog = new NewTopicDialog(intermediary);
            newTopicDialog.setSize(400, 300);
            Dimension dlgSize = newTopicDialog.getPreferredSize();
            Dimension frmSize = host.getSize();
            Point loc = host.getLocation();
            newTopicDialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
            newTopicDialog.setModal(true);
        }
        newTopicDialog.showSelf(intermediary.getTopicIDList());
    }

    void deleteTopicButton_actionPerformed(ActionEvent e) {
        int which = JOptionPane.showConfirmDialog(host, "Sure you want to delete this Topic?");
        if (which == JOptionPane.OK_OPTION) {
            adaptor.deleteTopic();
            deleteTopicButton.setEnabled(false);
        }
    }

    void newTopicMapButton_actionPerformed(ActionEvent e) {
        if (newTMDialog == null) {
            newTMDialog = new NewTopicMapDialog(adaptor);
            newTMDialog.setSize(300, 140);
            Dimension dlgSize = newTMDialog.getPreferredSize();
            Dimension frmSize = host.getSize();
            Point loc = host.getLocation();
            newTMDialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
            newTMDialog.setModal(true);
        }
        host.setWaitCursor(true);
        newTMDialog.showSelf();
    }

    void deleteTMButton_actionPerformed(ActionEvent e) {
        int which = JOptionPane.showConfirmDialog(host, "Sure you want to delete this Topic Map?");
        if (which == JOptionPane.OK_OPTION) {
            adaptor.deleteTopicMap();
            deleteTMButton.setEnabled(false);
        }
    }

    void importButton_actionPerformed(ActionEvent e) {
        JFileChooser filer = new JFileChooser();
        filer.setDialogTitle("Choose a Topic Map");
        filer.setCurrentDirectory(new java.io.File(""));
        int what = filer.showOpenDialog(this);
        if (what != JFileChooser.APPROVE_OPTION) return;
        host.setWaitCursor(true);
        java.io.File tmFile = filer.getSelectedFile();
        intermediary.importTopicMap(tmFile);
        host.setWaitCursor(false);
    }

    void exportButton_actionPerformed(ActionEvent e) {
    }

    void mergeButton_actionPerformed(ActionEvent e) {
    }
}
