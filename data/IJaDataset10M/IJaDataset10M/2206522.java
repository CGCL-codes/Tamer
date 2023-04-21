package ui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import ui.Messages;
import ui.command.CommandExecutor;
import ui.command.creation.ShowNewNAddressDialogCommand;
import ui.command.deletion.DeleteNAddressCommand;
import ui.command.information.InfoOnNAddressCommand;
import ui.table.NAddressTable;
import ui.table.TableSorter;
import ui.table.model.NAddressTableModel;
import base.user.User;

@SuppressWarnings("serial")
public class TabledNAddressPanel extends JPanel {

    private static final transient Logger logger = Logger.getLogger(TabledNAddressPanel.class.getName());

    private JTable nAddressTable;

    private TableSorter nAddressTableSorter;

    private JScrollPane nAddressTableScrollPane;

    private NAddressTableModel nAddressTableModel;

    private JPanel nAddressButtonPanel;

    private JButton newNAddressButton;

    private JButton infoOnNAddressButton;

    private JButton deleteNAddressButton;

    private final User user;

    public TabledNAddressPanel(User user) {
        this.user = user;
        initialize();
    }

    protected void initialize() {
        this.setBorder(new TitledBorder(Messages.getString("common.normalmailaddresses")));
        this.setLayout(new BorderLayout());
        this.add(getNAddressTableScrollPane(), BorderLayout.CENTER);
        this.add(getNAddressButtonPanel(), BorderLayout.EAST);
    }

    /**
	 * @return Returns the nAddressTable.
	 */
    protected JTable getNAddressTable() {
        if (nAddressTable == null) {
            nAddressTable = new NAddressTable(getNAddressTableSorter());
            nAddressTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
            nAddressTableSorter.setTableHeader(nAddressTable.getTableHeader());
            nAddressTable.getTableHeader().setToolTipText(Messages.getString("tablesorter.header.tooltip"));
        }
        return nAddressTable;
    }

    /**
	 * @return Returns the nAddressTableScrollPane.
	 */
    protected JScrollPane getNAddressTableScrollPane() {
        if (nAddressTableScrollPane == null) {
            nAddressTableScrollPane = new JScrollPane(getNAddressTable());
        }
        return nAddressTableScrollPane;
    }

    /**
	 * @return Returns the nAddressTableSorter.
	 */
    protected TableSorter getNAddressTableSorter() {
        if (nAddressTableSorter == null) {
            nAddressTableSorter = new TableSorter(getNAddressTableModel());
        }
        return nAddressTableSorter;
    }

    /**
	 * @return Returns the nAddressTableModel.
	 */
    protected NAddressTableModel getNAddressTableModel() {
        if (nAddressTableModel == null) {
            nAddressTableModel = new NAddressTableModel(getUser());
        }
        return nAddressTableModel;
    }

    /**
	 * @return Returns the nAddressButtonPanel.
	 */
    protected JPanel getNAddressButtonPanel() {
        if (nAddressButtonPanel == null) {
            nAddressButtonPanel = new JPanel();
            nAddressButtonPanel.setLayout(new GridLayout(3, 1));
            nAddressButtonPanel.add(getNewNAddressButton());
            nAddressButtonPanel.add(getDeleteNAddressButton());
            nAddressButtonPanel.add(getInfoOnNAddressButton());
        }
        return nAddressButtonPanel;
    }

    /**
	 * @return Returns the deleteNAddressButton.
	 */
    protected JButton getDeleteNAddressButton() {
        if (deleteNAddressButton == null) {
            deleteNAddressButton = new JButton();
            deleteNAddressButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/actions/list-remove.png")));
            deleteNAddressButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed deleteNAddressButton");
                    if (getNAddressTable().getSelectedRow() != -1) {
                        int nAddressId = Integer.parseInt(((TableSorter) getNAddressTable().getModel()).getValueAt(getNAddressTable().getSelectedRow(), NAddressTableModel.NADDRESS_ID_COLUMN).toString());
                        CommandExecutor.getInstance().executeCommand(new DeleteNAddressCommand(getUser(), nAddressId, getNAddressTableModel()), false);
                    } else JOptionPane.showMessageDialog(null, Messages.getString("panel.tablednaddresspanel.message1"), Messages.getString("common.warning"), JOptionPane.WARNING_MESSAGE);
                }
            });
        }
        return deleteNAddressButton;
    }

    /**
	 * @return Returns the infoOnNAddressButton.
	 */
    protected JButton getInfoOnNAddressButton() {
        if (infoOnNAddressButton == null) {
            infoOnNAddressButton = new JButton();
            infoOnNAddressButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/status/dialog-information.png")));
            infoOnNAddressButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed infoOnNAddressButton");
                    if (getNAddressTable().getSelectedRow() != -1) {
                        int nAddressId = Integer.parseInt(((TableSorter) getNAddressTable().getModel()).getValueAt(getNAddressTable().getSelectedRow(), NAddressTableModel.NADDRESS_ID_COLUMN).toString());
                        CommandExecutor.getInstance().executeCommand(new InfoOnNAddressCommand(getUser(), nAddressId), false);
                    } else JOptionPane.showMessageDialog(null, Messages.getString("panel.tablednaddresspanel.message1"), Messages.getString("common.warning"), JOptionPane.WARNING_MESSAGE);
                }
            });
        }
        return infoOnNAddressButton;
    }

    /**
	 * @return Returns the newNAddressButton.
	 */
    protected JButton getNewNAddressButton() {
        if (newNAddressButton == null) {
            newNAddressButton = new JButton();
            newNAddressButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/actions/list-add.png")));
            newNAddressButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed newNAddressButton");
                    ShowNewNAddressDialogCommand showNewNAddressDialogCommand = new ShowNewNAddressDialogCommand(getUser(), getNAddressTableModel());
                    CommandExecutor.getInstance().executeCommand(showNewNAddressDialogCommand, false);
                }
            });
        }
        return newNAddressButton;
    }

    /**
	 * @return Returns the user.
	 */
    protected User getUser() {
        return user;
    }
}
