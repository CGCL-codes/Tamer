package org.hardtokenmgmt.admin.ui.panels.hostinfo;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.hardtokenmgmt.admin.model.StatusRenderedCell;
import org.hardtokenmgmt.admin.model.tablemodels.CAInfoTableModel;
import org.hardtokenmgmt.admin.ui.panels.StatusCellRenderer;

/**
 * 
 * 
 * 
 * @author Philip Vendil 20 feb 2009
 *
 * @version $Id$
 */
public class CAInfoTablePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JScrollPane cAInfoSpace = null;

    private JTable cAInfoTable = null;

    private String hostName;

    /**
	 * @return the hostName
	 */
    public String getHostName() {
        return hostName;
    }

    /**
	 * This method initializes CAInfoSpace	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getCAInfoSpace() {
        if (cAInfoSpace == null) {
            cAInfoSpace = new JScrollPane(getTable());
            final GroupLayout groupLayout = new GroupLayout((JComponent) this);
            groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addComponent(cAInfoSpace, GroupLayout.PREFERRED_SIZE, 621, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(cAInfoSpace, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            setLayout(groupLayout);
        }
        return cAInfoSpace;
    }

    /**
	 * This method initializes Table	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getTable() {
        if (cAInfoTable == null) {
            CAInfoTableModel tableModel = new CAInfoTableModel(hostName);
            StatusCellRenderer statusCellRenderer = new StatusCellRenderer();
            cAInfoTable = new JTable();
            cAInfoTable.setModel(tableModel);
            cAInfoTable.setFillsViewportHeight(true);
            cAInfoTable.setAutoCreateRowSorter(true);
            cAInfoTable.setDefaultRenderer(StatusRenderedCell.class, statusCellRenderer);
            cAInfoTable.getColumnModel().getColumn(CAInfoTableModel.COLUMN_CANAME).setPreferredWidth(100);
            cAInfoTable.getColumnModel().getColumn(CAInfoTableModel.COLUMN_STATUS).setPreferredWidth(100);
            cAInfoTable.getColumnModel().getColumn(CAInfoTableModel.COLUMN_CATOKEN).setPreferredWidth(100);
            cAInfoTable.getColumnModel().getColumn(CAInfoTableModel.COLUMN_MONITORED).setPreferredWidth(50);
        }
        return cAInfoTable;
    }

    private void initComponents() {
        setBackground(Color.white);
        add(getCAInfoSpace());
    }

    /**
	 * Constructor used by the visual editor only
	 */
    public CAInfoTablePanel() {
        initComponents();
    }

    /**
	 * Default Constructor
	 * @param hostname
	 */
    public CAInfoTablePanel(String hostname) {
        this.hostName = hostname;
        initComponents();
    }
}
