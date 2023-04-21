package com.mojang.joxsi.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.mojang.joxsi.Material;

/**
 * Displays a table of loaded materials and handles modifying them.
 *
 * @author Ross Bearman
 */
public class MaterialChangerFrame extends JFrame implements ActionListener {

    Map<String, String> materialOverrides;

    MaterialTableModel tableModel;

    ModelDisplayerFrame parent;

    /**
     * Creates a MaterialChangerFrame, converting a material HashMap into a String array.
     *
     * @param parent
     *        the ModelDisplayerFrame that instantiated this object
     * @param materials
     *        holds the name and object of each material loaded in the current scene
     * @param materialOverrides
     *        holds the override details for the current textures
     */
    public MaterialChangerFrame(ModelDisplayerFrame parent, Map<String, Material> materials, Map<String, String> materialOverrides) {
        this.setSize(600, 400);
        this.setTitle("Modify Materials");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        tableModel = new MaterialTableModel(materials);
        this.parent = parent;
        this.materialOverrides = materialOverrides;
        JTable tblMaterials = new JTable(tableModel);
        tblMaterials.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        JButton btnOkay = new JButton("Okay");
        JButton btnCancel = new JButton("Cancel");
        btnOkay.setActionCommand("Okay");
        btnCancel.setActionCommand("Cancel");
        btnOkay.addActionListener(this);
        btnCancel.addActionListener(this);
        JScrollPane scrollPane = new JScrollPane(tblMaterials);
        JPanel panel = new JPanel();
        panel.add(btnOkay);
        panel.add(btnCancel);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(panel, BorderLayout.PAGE_END);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Okay")) {
            boolean success = parent.getModelDisplayer().modifyMaterials(this, tableModel.getMaterialData());
            if (success) this.dispose();
        } else if (command.equals("Cancel")) this.dispose();
    }
}
