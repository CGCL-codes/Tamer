package molmaster.gui.tools;

import molmaster.*;
import molmaster.gui.*;
import molmaster.gui.tools.reflect.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.media.j3d.*;
import java.util.*;

/**
 *
 * @author  rjpower
 */
public class LightPanel extends javax.swing.JPanel {

    /** Creates new form LightPanel */
    public LightPanel() {
        initComponents();
        loadLights();
    }

    protected void loadLights() {
        Iterator i = MolLight.lightList.iterator();
        lightList.setModel(new DefaultListModel());
        while (i.hasNext()) {
            ((DefaultListModel) lightList.getModel()).addElement(i.next());
        }
    }

    private void initComponents() {
        splitter = new javax.swing.JSplitPane();
        propScroll = new javax.swing.JScrollPane();
        lightProperties = new ReflectTable(null);
        listPanel = new javax.swing.JPanel();
        lightList = new javax.swing.JList();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        setLayout(new java.awt.BorderLayout());
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        splitter.setDividerLocation(150);
        propScroll.setViewportView(lightProperties);
        splitter.setRightComponent(propScroll);
        listPanel.setLayout(new javax.swing.BoxLayout(listPanel, javax.swing.BoxLayout.Y_AXIS));
        lightList.setBorder(new javax.swing.border.TitledBorder(""));
        lightList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lightListValueChanged(evt);
            }
        });
        listPanel.add(lightList);
        btnAdd.setText("Add Light");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        listPanel.add(btnAdd);
        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        listPanel.add(btnRemove);
        splitter.setLeftComponent(listPanel);
        add(splitter, java.awt.BorderLayout.NORTH);
    }

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        int i = lightList.getSelectedIndex();
        if (i < 0) {
            return;
        }
        Object o = ((DefaultListModel) lightList.getModel()).remove(i);
        ((MolLight) o).removeFromScene();
    }

    private void formComponentShown(java.awt.event.ComponentEvent evt) {
        loadLights();
    }

    private void lightListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        int i = lightList.getSelectedIndex();
        if (i < 0) {
            return;
        }
        lightProperties.setModel(new ReflectModel(lightList.getModel().getElementAt(i)));
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        new MolLight();
        loadLights();
        lightList.setSelectedIndex(lightList.getModel().getSize() - 1);
    }

    private javax.swing.JTable lightProperties;

    private javax.swing.JButton btnAdd;

    private javax.swing.JList lightList;

    private javax.swing.JScrollPane propScroll;

    private javax.swing.JSplitPane splitter;

    private javax.swing.JPanel listPanel;

    private javax.swing.JButton btnRemove;
}
