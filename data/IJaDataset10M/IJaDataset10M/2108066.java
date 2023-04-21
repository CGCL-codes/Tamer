package org.hardtokenmgmt.admin.ui.panels;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.hardtokenmgmt.admin.model.DiskTableModel;

/**
 * 
 * 
 * 
 * @author Philip Vendil 20 feb 2009
 *
 * @version $Id$
 */
public class PartitionInfoPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JScrollPane diskSpace = null;

    private JTable diskSpaceTable = null;

    private String hostName;

    /**
	 * @return the hostName
	 */
    public String getHostName() {
        return hostName;
    }

    /**
	 * This method initializes diskSpace	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getDiskSpace() {
        if (diskSpace == null) {
            diskSpace = new JScrollPane(getDiskSpaceTable());
            final GroupLayout groupLayout = new GroupLayout((JComponent) this);
            groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addComponent(diskSpace, GroupLayout.PREFERRED_SIZE, 621, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(diskSpace, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            setLayout(groupLayout);
        }
        return diskSpace;
    }

    /**
	 * This method initializes diskSpaceTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getDiskSpaceTable() {
        if (diskSpaceTable == null) {
            DiskTableModel diskTableModel = new DiskTableModel(hostName);
            StatusCellRenderer statusCellRenderer = new StatusCellRenderer(diskTableModel);
            diskSpaceTable = new JTable();
            diskSpaceTable.setModel(diskTableModel);
            diskSpaceTable.setFillsViewportHeight(true);
            diskSpaceTable.setAutoCreateRowSorter(true);
            diskSpaceTable.setDefaultRenderer(Object.class, statusCellRenderer);
            diskSpaceTable.getColumnModel().getColumn(DiskTableModel.COLUMN_PARTITIONNAME).setPreferredWidth(100);
            diskSpaceTable.getColumnModel().getColumn(DiskTableModel.COLUMN_STATUS).setPreferredWidth(100);
            diskSpaceTable.getColumnModel().getColumn(DiskTableModel.COLUMN_TOTALSPACE).setPreferredWidth(50);
            diskSpaceTable.getColumnModel().getColumn(DiskTableModel.COLUMN_FREESPACE).setPreferredWidth(50);
            diskSpaceTable.getColumnModel().getColumn(DiskTableModel.COLUMN_USED).setPreferredWidth(50);
            diskSpaceTable.getColumnModel().getColumn(DiskTableModel.COLUMN_THRESHOLD).setPreferredWidth(50);
        }
        return diskSpaceTable;
    }

    private void initComponents() {
        setBackground(Color.white);
        add(getDiskSpace());
        setSize(609, 103);
    }

    /**
	 * Constructor used by the visual editor only
	 */
    public PartitionInfoPanel() {
        initComponents();
    }

    /**
	 * Default Constructor
	 * @param hostname
	 */
    public PartitionInfoPanel(String hostname) {
        this.hostName = hostname;
        initComponents();
    }
}
