package view;

import ucm.UCController;
import ucm.UCAdaptActionListener;
import javax.swing.*;
import java.awt.*;

/**
 * registerUserGUI
 * User: Josh
 * Version: 3final
 * Date: 25/03/2009
 * Time: 21:26:20.
 */
public class AirportInfoGUI extends JPanel {

    private GUIFactory gui;

    private JPanel airportInfoPanel;

    private JPanel sidebar;

    private JButton registerButton;

    private JButton refreshButton;

    private JButton deleteUserButton;

    private JOptionPane dialog;

    private JScrollPane airportTableScrollPane;

    private JTable airportTable;

    private final String airportCols[] = { "Airport Name", "Tax" };

    /**
     * Constructor for building the login manager GUI
     * @param gui The GUI Factory method
     */
    public AirportInfoGUI(GUIFactory gui) {
        this.gui = gui;
        setSize(300, 300);
        setLayout(new BorderLayout());
        add(userInfoPanel(), BorderLayout.WEST);
        add(sidebar(), BorderLayout.EAST);
    }

    private JPanel userInfoPanel() {
        if (airportInfoPanel == null) {
            airportInfoPanel = new JPanel();
            airportTable = new JTable();
            airportTable.setAutoCreateRowSorter(true);
            airportTable.enableInputMethods(false);
            airportTable.setDragEnabled(false);
            javax.swing.table.DefaultTableModel airportTableModel = new javax.swing.table.DefaultTableModel(new Object[][] { {} }, airportCols);
            airportTable.setModel(airportTableModel);
            airportTableScrollPane = new JScrollPane();
            airportTableScrollPane.setViewportView(airportTable);
            airportTable.setSize(500, 300);
            airportTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            airportTable.getColumnModel().getColumn(0).setMaxWidth(60);
            airportInfoPanel.add(airportTableScrollPane);
        }
        return airportInfoPanel;
    }

    private JPanel sidebar() {
        if (sidebar == null) {
            sidebar = new JPanel(new GridLayout(3, 1));
            registerButton = gui.createRegisterButton();
            sidebar.add(registerButton);
            refreshButton = gui.createGenericButton("Refresh");
            sidebar.add(refreshButton);
            deleteUserButton = gui.createDeleteButton();
            sidebar.add(deleteUserButton);
        }
        return sidebar;
    }

    /**
     * Sets the 2D array users to the view's JTable
     * @param users 2D Array of user data
     */
    public void setAirports(Object[][] users) {
        javax.swing.table.DefaultTableModel userTableModel = new javax.swing.table.DefaultTableModel(users, airportCols);
        airportTable.setModel(userTableModel);
    }

    /**
     * Gets the selected user from the table
     * @return the user id of the user
     */
    public String getSelectedAiport() {
        return airportTable.getValueAt(airportTable.getSelectedRow(), 0).toString();
    }

    /**
     * Adapts the UC Controllr to the view
     * @param uc The UC controller to be adapted
     */
    public void setRegisterListener(UCController uc) {
        registerButton.addActionListener(new UCAdaptActionListener(uc));
    }

    /**
     * Shows a message dialog
     * @param message the message to be displayed
     */
    public void showMessage(String message) {
        dialog.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sets the ActionListener to the delete user button
     * @param uc UCController for the button
     */
    public void setDeleteUserListener(UCController uc) {
        deleteUserButton.addActionListener(new UCAdaptActionListener(uc));
    }

    /**
     * Sets the ActionListener to the new user button
     * @param uc UCController for the button
     */
    public void setNewUserListener(UCController uc) {
    }

    public void setRefreshButtonListener(UCController uc) {
        refreshButton.addActionListener(new UCAdaptActionListener(uc));
    }
}
