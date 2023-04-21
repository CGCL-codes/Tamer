package jshomeorg.simplytrain.gui.panels;

import java.awt.BorderLayout;
import java.text.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import jshomeorg.event.*;
import jshomeorg.simplytrain.*;
import jshomeorg.simplytrain.editor.EditorActionEvent;
import jshomeorg.simplytrain.editor.EditorEvent;
import jshomeorg.simplytrain.gui.panels.readypanels.trackObjectRequirements;
import jshomeorg.simplytrain.gui.renderer.genericPaintInterfaceComboBoxRenderer;
import jshomeorg.simplytrain.service.track;
import javax.swing.JFormattedTextField.AbstractFormatter;
import jshomeorg.simplytrain.service.trackObjects.pathableObject;
import jshomeorg.simplytrain.service.trackObjects.trackObject;

/**
 *
 * @author  js
 */
public class hidepanel_edittrackobjects extends javax.swing.JPanel {

    private boolean CBeventDisable = false;

    private trackObject workingTO = null;

    private trackObjectRequirements tor = null;

    /** Creates new form mainpanel_file */
    public hidepanel_edittrackobjects() {
        initComponents();
        tor = new trackObjectRequirements();
        reqPanel.add(tor, BorderLayout.CENTER);
        dataCollector.collector.editorEventListeners.addListener(new AbstractListener() {

            public void action(AbstractEvent e) {
                editorEvent((EditorEvent) e);
            }
        });
        CBeventDisable = true;
        for (String s : dataCollector.collector.tol.values()) {
            trackObject to = dataCollector.collector.tol.load(s);
            ((DefaultListModel) toList.getModel()).addElement(to);
        }
        CBeventDisable = false;
        toList.setSelectedIndex(-1);
        setRequirements(null);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        toList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        add_ToggleButton = new javax.swing.JToggleButton();
        del_Button = new javax.swing.JButton();
        rotate_Button = new javax.swing.JButton();
        reqPanel = new javax.swing.JPanel();
        setLayout(new java.awt.BorderLayout());
        jScrollPane1.setMinimumSize(new java.awt.Dimension(80, 24));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 132));
        toList.setModel(new DefaultListModel());
        toList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        toList.setCellRenderer(new genericPaintInterfaceComboBoxRenderer());
        toList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                toListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(toList);
        add(jScrollPane1, java.awt.BorderLayout.WEST);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jLabel1.setText("als");
        jPanel1.add(jLabel1);
        add_ToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jshomeorg/simplytrain/gui/resources/add.png")));
        add_ToggleButton.setText("neues Gleisobjekt");
        add_ToggleButton.setActionCommand("add");
        add_ToggleButton.setEnabled(false);
        add_ToggleButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        add_ToggleButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButtonActionPerformed(evt);
            }
        });
        jPanel1.add(add_ToggleButton);
        del_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jshomeorg/simplytrain/gui/resources/delete.png")));
        del_Button.setText("löschen");
        del_Button.setActionCommand("del");
        del_Button.setEnabled(false);
        del_Button.setMargin(new java.awt.Insets(2, 5, 2, 5));
        del_Button.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(del_Button);
        rotate_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jshomeorg/simplytrain/gui/resources/rotate.png")));
        rotate_Button.setText("drehen");
        rotate_Button.setActionCommand("rotate");
        rotate_Button.setEnabled(false);
        rotate_Button.setMargin(new java.awt.Insets(2, 5, 2, 5));
        rotate_Button.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(rotate_Button);
        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);
        reqPanel.setLayout(new java.awt.BorderLayout());
        jPanel2.add(reqPanel, java.awt.BorderLayout.WEST);
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }

    private void ToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dataCollector.collector.thegame.runAction(new EditorActionEvent(evt.getActionCommand(), add_ToggleButton.isSelected()));
    }

    private void toListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting() == false) {
            trackObject to = null;
            if (toList.getSelectedIndex() != -1) to = (trackObject) ((DefaultListModel) toList.getModel()).getElementAt(toList.getSelectedIndex());
            if (to != null) {
                add_ToggleButton.setEnabled(true);
                add_ToggleButton.setSelected(true);
                if (!CBeventDisable) {
                    dataCollector.collector.thegame.runAction(new EditorActionEvent<String>("trackobjectselect", to.getTypeName()));
                    dataCollector.collector.thegame.runAction(new EditorActionEvent(add_ToggleButton.getActionCommand(), true));
                }
            } else add_ToggleButton.setEnabled(false);
        }
    }

    private void ButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dataCollector.collector.thegame.runAction(new EditorActionEvent(evt.getActionCommand()));
    }

    private javax.swing.JToggleButton add_ToggleButton;

    private javax.swing.JButton del_Button;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JPanel reqPanel;

    private javax.swing.JButton rotate_Button;

    private javax.swing.JList toList;

    void editorEvent(EditorEvent e) {
        switch(e.getType()) {
            case EditorEvent.TRACKOBJECT_SELECTED:
                workingTO = e.getTrackObject();
                if (!(workingTO instanceof pathableObject) || !((pathableObject) workingTO).isEnabled()) {
                    del_Button.setEnabled(true);
                    rotate_Button.setEnabled(true);
                } else {
                    del_Button.setEnabled(false);
                    rotate_Button.setEnabled(false);
                }
                break;
            case EditorEvent.TRACKOBJECT_UNSELECTED:
                workingTO = e.getTrackObject();
                del_Button.setEnabled(false);
                rotate_Button.setEnabled(false);
                break;
            case EditorEvent.TRACKOBJECT_MOVEMODE:
                add_ToggleButton.setSelected(false);
                break;
            case EditorEvent.TRACKOBJECT_GUITYPECHANGED:
                CBeventDisable = true;
                toList.setSelectedIndex(dataCollector.collector.tol.getIndex(e.getString()));
                setRequirements((trackObject) toList.getSelectedValue());
                CBeventDisable = false;
                break;
        }
    }

    void setRequirements(trackObject to) {
        tor.setRequirements(to);
    }
}
