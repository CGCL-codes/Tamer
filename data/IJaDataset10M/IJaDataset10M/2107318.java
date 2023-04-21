package com.jay.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.print.PrinterException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * @author Jay
 * @date 2006. 6. 19.
 */
public class CellBean extends JPanel {

    private static final long serialVersionUID = 1L;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    /**
        * This is the default constructor
        */
    public CellBean() {
        super();
        initialize();
    }

    /**
        * @param tableModel
        */
    public void setModel(DefaultTableModel tableModel) {
        this.clear();
        this.getJTable().setModel(tableModel);
        this.getJTable().setAutoResizeMode(WIDTH);
    }

    /**
        * This method initializes this
        * 
        * @return void
        */
    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.add(getJScrollPane(), gridBagConstraints);
    }

    /**
        * This method initializes jScrollPane	
        * 	
        * @return javax.swing.JScrollPane	
        */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
        * This method initializes jTable	
        * 	
        * @return javax.swing.JTable	
        */
    public JTable getJTable() {
        if (jTable == null) {
            jTable = new JTable();
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            jTable.setCellSelectionEnabled(true);
        }
        return jTable;
    }

    protected boolean printTable() throws PrinterException {
        Boolean bFlag = false;
        bFlag = this.getJTable().print();
        return bFlag;
    }

    void clear() {
        DefaultTableModel tableModel = new DefaultTableModel();
        if (this.getJTable().getRowCount() > 0) this.getJTable().setModel(tableModel);
    }
}
