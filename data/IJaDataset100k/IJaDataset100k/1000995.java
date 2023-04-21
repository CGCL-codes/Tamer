package gov.sns.apps.jeri.apps.systemselect;

import java.awt.*;
import java.awt.event.*;
import javax.sql.*;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import gov.sns.apps.jeri.data.EpicsSystem;
import gov.sns.apps.jeri.data.EpicsSubsystem;
import gov.sns.apps.jeri.application.JeriDialog;

/**
 * Provides an interface that allows the user to select an 
 * <CODE>EpicsSystem</CODE> and/or a <CODE>Subsystem</CODE>.
 * 
 * @author Chris Fowlkes
 */
public class SystemSelectDialog extends JeriDialog {

    private JPanel labelPanel = new JPanel();

    private JPanel fieldPanel = new JPanel();

    private JPanel outerButtonPanel = new JPanel();

    private JPanel innerButtonPanel = new JPanel();

    private GridLayout labelPanelLayout = new GridLayout();

    private JLabel systemLabel = new JLabel();

    private JLabel subsystemLabel = new JLabel();

    private GridLayout fieldPanelLayout = new GridLayout();

    private JComboBox systemCombo = new JComboBox();

    private JComboBox subsystemCombo = new JComboBox();

    private BorderLayout outerButtonPanelLayout = new BorderLayout();

    private GridLayout innerButtonPanelLayout = new GridLayout();

    private JButton okButton = new JButton();

    private JButton cancelButton = new JButton();

    /**
   * Constant that denotes the OK button was clicked.
   */
    public static final int OK = 1;

    /**
   * Constant that denotes the OK button was not clicked.
   */
    public static final int CANCEL = 0;

    /**
   * Holds and indicator to determine if the OK button was used to exit the 
   * dialog.
   */
    private int result = CANCEL;

    /**
   * Holds the <CODE>DataSource</CODE> for the window. This is used to make 
   * database connections.
   */
    private DataSource connectionPool;

    /**
   * Contains the functionality for the dialog.
   */
    private SystemSelect systemSelect = new SystemSelect();

    /**
   * Creates a new <CODE>SystemSelectDialog</CODE>.
   */
    public SystemSelectDialog() {
        this(null, "", false);
    }

    /**
   * Creates a new <CODE>SystemSelectDialog</CODE>.
   * 
   * @param parent The parent window for the dialog.
   * @param title The title to appear in the dialog.
   * @param modal Pass as <CODE>true</CODE> for a modal dialog.
   */
    public SystemSelectDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
            pack();
            setSize(300, getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Component initialization.
   * 
   * @throws java.lang.Exception Thrown on initialization error.
   */
    private void jbInit() throws Exception {
        labelPanel.setLayout(labelPanelLayout);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
        labelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        fieldPanel.setLayout(fieldPanelLayout);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
        outerButtonPanel.setLayout(outerButtonPanelLayout);
        outerButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        innerButtonPanel.setLayout(innerButtonPanelLayout);
        labelPanelLayout.setRows(2);
        labelPanelLayout.setHgap(5);
        labelPanelLayout.setVgap(5);
        systemLabel.setText("System:");
        systemLabel.setDisplayedMnemonic('S');
        systemLabel.setLabelFor(systemCombo);
        subsystemLabel.setText("Subsystem:");
        subsystemLabel.setDisplayedMnemonic('u');
        subsystemLabel.setLabelFor(subsystemCombo);
        fieldPanelLayout.setRows(2);
        fieldPanelLayout.setVgap(5);
        systemCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                combo_itemStateChanged(e);
            }
        });
        subsystemCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                combo_itemStateChanged(e);
            }
        });
        innerButtonPanelLayout.setHgap(5);
        okButton.setText("OK");
        okButton.setEnabled(false);
        okButton.setMnemonic('O');
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        labelPanel.add(systemLabel, null);
        labelPanel.add(subsystemLabel, null);
        this.getContentPane().add(labelPanel, BorderLayout.WEST);
        fieldPanel.add(systemCombo, null);
        fieldPanel.add(subsystemCombo, null);
        this.getContentPane().add(fieldPanel, BorderLayout.CENTER);
        innerButtonPanel.add(okButton, null);
        innerButtonPanel.add(cancelButton, null);
        outerButtonPanel.add(innerButtonPanel, BorderLayout.EAST);
        this.getContentPane().add(outerButtonPanel, BorderLayout.SOUTH);
    }

    /**
   * Called when the ok button is clicked. This method closes the dialog and 
   * sets the value of the result property to <CODE>OK</CODE>.
   * 
   * @param e The <CODE>ActionEvent</CODE> that caused the invocation of this method.
   */
    private void okButton_actionPerformed(ActionEvent e) {
        result = OK;
        setVisible(false);
    }

    /**
   * Called when the cancel button is clicked. This method closes the dialog and 
   * sets the value of the result property to <CODE>CANCEL</CODE>.
   * 
   * @param e The <CODE>ActionEvent</CODE> that caused the invocation of this method.
   */
    private void cancelButton_actionPerformed(ActionEvent e) {
        result = CANCEL;
        setVisible(false);
    }

    /**
   * Gets the <CODE>DataSource</CODE> used by the window to connect to the 
   * database.
   *
   * @return The <CODE>DataSource</CODE> used to connect to the database.
   */
    public DataSource getDataSource() {
        return connectionPool;
    }

    /**
   * Sets the <CODE>DataSource</CODE> used by the window to connect to the 
   * database.
   *
   * @param connectionPool The <CODE>DataSource</CODE> to use to connect to the database.
   */
    public void setDataSource(DataSource connectionPool) {
        this.connectionPool = connectionPool;
        systemSelect.setDataSource(connectionPool);
    }

    /**
   * Returns an <CODE>int</CODE> used to determine which button the dialog was
   * closed with.
   *
   * @return Returns <CODE>COMMIT</CODE> if the ok button was clicked, returns <CODE>CANCEL</CODE> otherwise.
   */
    public int getResult() {
        return result;
    }

    /**
   * Refreshes the system and subsystem pick lists.
   * 
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void refresh() throws java.sql.SQLException {
        systemCombo.removeAllItems();
        systemCombo.addItem("Any");
        EpicsSystem[] systems = systemSelect.loadSystems();
        for (int i = 0; i < systems.length; i++) systemCombo.addItem(systems[i]);
        subsystemCombo.removeAllItems();
        subsystemCombo.addItem("Any");
        EpicsSubsystem[] subsystems = systemSelect.loadSubsystems();
        for (int i = 0; i < subsystems.length; i++) subsystemCombo.addItem(subsystems[i]);
    }

    /**
   * Gets the <CODE>EpicsSystem</CODE> selected by the user. This method returns
   * <CODE>null</CODE> if no <CODE>EpicsSystem</CODE> was selected.
   * 
   * @return The <CODE>EpicsSystem</CODE> selected by the user.
   */
    public EpicsSystem getSystem() {
        Object selectedSystem = systemCombo.getSelectedItem();
        if (selectedSystem instanceof EpicsSystem) return (EpicsSystem) selectedSystem; else return null;
    }

    /**
   * Gets the <CODE>EpicsSubsystem</CODE> selected by the user. This method 
   * returns <CODE>null</CODE> if no <CODE>EpicsSubsystem</CODE> was selected.
   * 
   * @return The <CODE>EpicsSubsystem</CODE> selected by the user.
   */
    public EpicsSubsystem getSubsystem() {
        Object selectedSubsystem = subsystemCombo.getSelectedItem();
        if (selectedSubsystem instanceof EpicsSubsystem) return (EpicsSubsystem) selectedSubsystem; else return null;
    }

    /**
   * Sets the value of the result property to <CODE>false</CODE>.
   * 
   * @param e The <CODE>WindowEvent</CODE> that caused the invocation of this method.
   */
    private void this_windowClosing(WindowEvent e) {
        result = CANCEL;
    }

    /**
   * Called when the selected item in either of the combo boxes changes.
   * 
   * @param e The <CODE>ItemEvent</CODE> that caused the invocation of this method.
   */
    private void combo_itemStateChanged(ItemEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Object selectedSystem = systemCombo.getSelectedItem();
                Object selectedSubsystem = subsystemCombo.getSelectedItem();
                boolean enable = selectedSystem instanceof EpicsSystem || selectedSubsystem instanceof EpicsSubsystem;
                okButton.setEnabled(enable);
            }
        });
    }
}
