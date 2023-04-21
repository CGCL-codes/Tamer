package org.jmove.zui.prefs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Displaying prefs.
 *
 * @author Michael Juergens
 */
public class PrefsDialog extends javax.swing.JDialog {

    private boolean myConfirmed = false;

    private HashMap myLnfNameMap = new HashMap();

    /** Creates new form PrefsDialog */
    public PrefsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initColorButton(b_defaultColor, "Default Color for nodes");
        initColorButton(b_selectedColor, "Color for selected nodes");
        initColorButton(b_usingColor, "Color for using nodes");
        initColorButton(b_usedColor, "Color for used nodes");
        initColorButton(b_coupledColor, "Color for coupled nodes");
    }

    private void initColorButton(AbstractButton aButton, final String pTitle) {
        aButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Component comp = (Component) e.getSource();
                Color newColor = JColorChooser.showDialog(null, pTitle, comp.getBackground());
                if (newColor != null) {
                    comp.setBackground(newColor);
                }
            }
        });
    }

    public boolean isConfirmed() {
        return myConfirmed;
    }

    public void readFrom(Prefs aPrefs) {
        cb_load_on_start.setSelected(aPrefs.isLoadOnStart());
        cb_save_on_exit.setSelected(aPrefs.isSaveOnExit());
        myLnfNameMap = new HashMap();
        Iterator lnf = aPrefs.allLookAndFeels().iterator();
        while (lnf.hasNext()) {
            String lnfClassName = (String) lnf.next();
            String name = aPrefs.nameForLookAndFeel(lnfClassName);
            if (name != null) {
                myLnfNameMap.put(name, lnfClassName);
                ((DefaultComboBoxModel) cbx_lookfeel.getModel()).addElement(name);
            }
        }
        String selected = aPrefs.createLookAndFeelInstance().getName();
        cbx_lookfeel.getModel().setSelectedItem(selected);
        b_selectedColor.setBackground(aPrefs.getSelectedNodeColor());
        b_usingColor.setBackground(aPrefs.getUsingNodeColor());
        b_usedColor.setBackground(aPrefs.getUsedNodeColor());
    }

    public void writeInto(Prefs aPrefs) {
        aPrefs.setLoadOnStart(cb_load_on_start.isSelected());
        aPrefs.setSaveOnExit(cb_save_on_exit.isSelected());
        String lnfName = (String) cbx_lookfeel.getModel().getSelectedItem();
        aPrefs.setLookAndFeel((String) myLnfNameMap.get(lnfName));
        aPrefs.setSelectedNodeColor(b_selectedColor.getBackground());
        aPrefs.setUsingNodeColor(b_usingColor.getBackground());
        aPrefs.setUsedNodeColor(b_usedColor.getBackground());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        p_buttons = new javax.swing.JPanel();
        b_ok = new javax.swing.JButton();
        b_cancel = new javax.swing.JButton();
        tp_prefs = new javax.swing.JTabbedPane();
        p_project = new javax.swing.JPanel();
        cb_save_on_exit = new javax.swing.JCheckBox();
        cb_load_on_start = new javax.swing.JCheckBox();
        p_ui = new javax.swing.JPanel();
        p_lookfeel = new javax.swing.JPanel();
        l_lookfeel = new javax.swing.JLabel();
        cbx_lookfeel = new javax.swing.JComboBox();
        p_colors = new javax.swing.JPanel();
        l_default = new javax.swing.JLabel();
        b_defaultColor = new javax.swing.JButton();
        l_selected = new javax.swing.JLabel();
        b_selectedColor = new javax.swing.JButton();
        l_using = new javax.swing.JLabel();
        b_usingColor = new javax.swing.JButton();
        l_used = new javax.swing.JLabel();
        b_usedColor = new javax.swing.JButton();
        l_coupled = new javax.swing.JLabel();
        b_coupledColor = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("jmover");
        setModal(true);
        setResizable(false);
        p_buttons.setPreferredSize(new java.awt.Dimension(170, 39));
        p_buttons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        b_ok.setText("OK");
        b_ok.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKClicked(evt);
            }
        });
        p_buttons.add(b_ok);
        b_cancel.setText("Cancel");
        b_cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelClicked(evt);
            }
        });
        p_buttons.add(b_cancel);
        getContentPane().add(p_buttons, java.awt.BorderLayout.SOUTH);
        tp_prefs.setMinimumSize(new java.awt.Dimension(400, 260));
        tp_prefs.setPreferredSize(new java.awt.Dimension(400, 260));
        p_project.setMaximumSize(new java.awt.Dimension(400, 260));
        p_project.setMinimumSize(new java.awt.Dimension(400, 260));
        p_project.setLayout(new java.awt.GridBagLayout());
        cb_save_on_exit.setText("Save project on exit");
        cb_save_on_exit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cb_save_on_exit.setNextFocusableComponent(cb_load_on_start);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        p_project.add(cb_save_on_exit, gridBagConstraints);
        cb_load_on_start.setText("Load last project on start");
        cb_load_on_start.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        p_project.add(cb_load_on_start, gridBagConstraints);
        tp_prefs.addTab("Project", p_project);
        p_ui.setMinimumSize(new java.awt.Dimension(400, 260));
        p_ui.setPreferredSize(new java.awt.Dimension(400, 260));
        p_ui.setLayout(new java.awt.BorderLayout());
        p_lookfeel.setLayout(new java.awt.GridBagLayout());
        l_lookfeel.setText("Look and Feel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 20);
        p_lookfeel.add(l_lookfeel, gridBagConstraints);
        cbx_lookfeel.setMinimumSize(new java.awt.Dimension(200, 27));
        cbx_lookfeel.setPreferredSize(new java.awt.Dimension(200, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 20.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 0);
        p_lookfeel.add(cbx_lookfeel, gridBagConstraints);
        p_ui.add(p_lookfeel, java.awt.BorderLayout.NORTH);
        p_colors.setBorder(javax.swing.BorderFactory.createTitledBorder("Node coloring"));
        p_colors.setLayout(new java.awt.GridBagLayout());
        l_default.setText("Default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        p_colors.add(l_default, gridBagConstraints);
        b_defaultColor.setBackground(new java.awt.Color(255, 255, 102));
        b_defaultColor.setText("          ");
        b_defaultColor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        p_colors.add(b_defaultColor, gridBagConstraints);
        l_selected.setText("Selected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        p_colors.add(l_selected, gridBagConstraints);
        b_selectedColor.setBackground(new java.awt.Color(255, 153, 0));
        b_selectedColor.setText("          ");
        b_selectedColor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        p_colors.add(b_selectedColor, gridBagConstraints);
        l_using.setText("Using");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        p_colors.add(l_using, gridBagConstraints);
        b_usingColor.setBackground(new java.awt.Color(102, 102, 255));
        b_usingColor.setText("          ");
        b_usingColor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        p_colors.add(b_usingColor, gridBagConstraints);
        l_used.setText("Used");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        p_colors.add(l_used, gridBagConstraints);
        b_usedColor.setBackground(new java.awt.Color(0, 153, 51));
        b_usedColor.setText("          ");
        b_usedColor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        p_colors.add(b_usedColor, gridBagConstraints);
        l_coupled.setText("Coupled");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        p_colors.add(l_coupled, gridBagConstraints);
        b_coupledColor.setBackground(new java.awt.Color(204, 0, 153));
        b_coupledColor.setText("          ");
        b_coupledColor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        p_colors.add(b_coupledColor, gridBagConstraints);
        p_ui.add(p_colors, java.awt.BorderLayout.CENTER);
        tp_prefs.addTab("UI", p_ui);
        getContentPane().add(tp_prefs, java.awt.BorderLayout.LINE_END);
        pack();
    }

    private void buttonCancelClicked(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void buttonOKClicked(java.awt.event.ActionEvent evt) {
        myConfirmed = true;
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PrefsDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.JButton b_cancel;

    private javax.swing.JButton b_coupledColor;

    private javax.swing.JButton b_defaultColor;

    private javax.swing.JButton b_ok;

    private javax.swing.JButton b_selectedColor;

    private javax.swing.JButton b_usedColor;

    private javax.swing.JButton b_usingColor;

    private javax.swing.JCheckBox cb_load_on_start;

    private javax.swing.JCheckBox cb_save_on_exit;

    private javax.swing.JComboBox cbx_lookfeel;

    private javax.swing.JLabel l_coupled;

    private javax.swing.JLabel l_default;

    private javax.swing.JLabel l_lookfeel;

    private javax.swing.JLabel l_selected;

    private javax.swing.JLabel l_used;

    private javax.swing.JLabel l_using;

    private javax.swing.JPanel p_buttons;

    private javax.swing.JPanel p_colors;

    private javax.swing.JPanel p_lookfeel;

    private javax.swing.JPanel p_project;

    private javax.swing.JPanel p_ui;

    private javax.swing.JTabbedPane tp_prefs;
}
