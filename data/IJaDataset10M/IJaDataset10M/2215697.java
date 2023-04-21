package creator.components;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 *
 * @author  Creator
 */
public class CheckBoxList extends javax.swing.JPanel {

    /** Creates new form BeanForm */
    public CheckBoxList() {
        initComponents();
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        internalItems = new Vector<InstallData>();
        items = new Vector<String>();
        jList1 = new javax.swing.JList();
        this.jList1.setListData(internalItems);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        jList1.setCellRenderer(renderer);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this);
        jList1.addMouseListener(lst);
        jList1.addKeyListener(lst);
        setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(jList1);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }

    javax.swing.JList jList1;

    private javax.swing.JScrollPane jScrollPane1;

    /**
     * Holds value of property items.
     */
    private Vector<InstallData> internalItems;

    private Vector<String> items;

    /**
     * Getter for property items.
     * @return Value of property items.
     */
    public Vector<String> getItems() {
        items.clear();
        for (InstallData data : internalItems) {
            items.add(data.m_name);
        }
        return items;
    }

    public void add(String name, Boolean value) {
        internalItems.add(new InstallData(name, value));
        this.jList1.setListData(internalItems);
        this.jList1.updateUI();
    }

    /**
     * Setter for property items.
     * @param items New value of property items.
     */
    public void setItems(Vector<String> items) {
        this.items = items;
        internalItems.clear();
        for (String s : items) {
            internalItems.add(new InstallData(s));
        }
        this.jList1.setListData(internalItems);
        this.jList1.updateUI();
    }

    public Vector<String> SelectedItems() {
        Vector<String> v = new Vector<String>();
        for (InstallData data : internalItems) {
            if (data.isSelected()) v.add(data.m_name);
        }
        return v;
    }
}
