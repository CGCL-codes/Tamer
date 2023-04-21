package org.ultracalendar.standaloneclient.views;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.ultracalendar.appointment_management.Appointment;
import org.ultracalendar.appointment_management.AppointmentType;
import org.ultracalendar.login_system.LoginException;
import org.ultracalendar.standaloneclient.ejb.ServiceLocatorException;
import org.ultracalendar.standaloneclient.models.CalendarModel;
import org.ultracalendar.standaloneclient.models.LoginModel;
import org.ultracalendar.user_management.User;

/**
 * The calendar dialog.
 * 
 * @author Hannes John
 * @since 01.06.2009 13:58:11
 */
public class CalendarInternalFrame extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = -663898206413356887L;

    /** The model */
    private final CalendarModel model;

    /** The login model */
    private final LoginModel loginModel;

    /** The table model for the table */
    private final TableModel tableModel;

    /** The add appointment dialog */
    private final AddAppointmentInternalFrame addAppointmentInternalFrame;

    /** Creates new form CalendarInternalFrame */
    public CalendarInternalFrame(final CalendarModel model, final LoginModel loginModel) {
        this.model = model;
        this.loginModel = loginModel;
        tableModel = buildTableModel();
        observeLoginModel();
        addAppointmentInternalFrame = new AddAppointmentInternalFrame(model, loginModel);
        initComponents();
    }

    /**
	 * Builds the table model.
	 * 
	 * @return The table model
	 */
    private AbstractTableModel buildTableModel() {
        return new AbstractTableModel() {

            private static final long serialVersionUID = -8587361670174882582L;

            /** The appointments of this table model */
            private List<Appointment> appointments;

            /** The column names */
            private final String[] columnNames = new String[] { "Title", "Start", "End", "Note", "Private", "Type" };

            /** The column classes */
            private final Class<?>[] columnClasses = new Class[] { String.class, String.class, String.class, String.class, Boolean.class, AppointmentType.class };

            {
                model.addObserver(new Observer() {

                    @Override
                    public void update(Observable o, Object arg) {
                        doUpdate();
                    }
                });
                model.notifyObservers();
            }

            @Override
            public int getColumnCount() {
                return 6;
            }

            @Override
            public int getRowCount() {
                return appointments.size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                final Appointment appointment = appointments.get(rowIndex);
                switch(columnIndex) {
                    case 0:
                        return appointment.getTitle();
                    case 1:
                        return new SimpleDateFormat("dd.MM.yyyy H:m").format(appointment.getStart().getTime());
                    case 2:
                        return new SimpleDateFormat("dd.MM.yyyy H:m").format(appointment.getEnd().getTime());
                    case 3:
                        return appointment.getTextualNote();
                    case 4:
                        return appointment.isPrivateAppointment();
                    default:
                        return appointment.getAppointmentType();
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClasses[columnIndex];
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            /** Updates this table model's data */
            private void doUpdate() {
                try {
                    if (!isLoggedIn()) {
                        clearTable();
                        return;
                    }
                    final User loggedInUser = loginModel.getLoggedInUser();
                    appointments = model.getTodaysAppointments(loggedInUser);
                    fireTableDataChanged();
                } catch (LoginException e) {
                    showErrorDialog("Invalid login.", "Invalid login");
                    clearTable();
                } catch (ServiceLocatorException e) {
                    showErrorDialog("The server seems to be down.", "No connection");
                    clearTable();
                }
            }

            /**
			 * Clears the table.
			 */
            private void clearTable() {
                appointments = Collections.emptyList();
                fireTableDataChanged();
            }
        };
    }

    /**
	 * Indicates whether one is logged in.
	 * 
	 * @return true, if logged in, else false
	 */
    private boolean isLoggedIn() {
        return loginModel.isLoggedIn();
    }

    /**
	 * Observes the login model.
	 */
    private void observeLoginModel() {
        loginModel.addObserver(new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                loginStateChanged();
            }
        });
    }

    /**
	 * Indicates that the login state has changed.
	 */
    private void loginStateChanged() {
        invalidateModel();
        if (loginModel.isLoggedIn()) {
            addMenuItem.setEnabled(true);
        } else {
            addMenuItem.setEnabled(false);
        }
    }

    /**
	 * Invalidates the model.
	 */
    private void invalidateModel() {
        model.notifyObservers();
    }

    /**
	 * Shows an error dialog.
	 * 
	 * @param message
	 *            The message
	 * @param title
	 *            The title
	 */
    private void showErrorDialog(final String message, final String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        addMenuItem = new javax.swing.JMenuItem();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Calendar");
        jTable1.setModel(tableModel);
        jScrollPane1.setViewportView(jTable1);
        jMenu1.setText("Appointments");
        addMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        addMenuItem.setText("Add");
        addMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAddAppointmentInternalFrame(evt);
            }
        });
        jMenu1.add(addMenuItem);
        jMenuBar1.add(jMenu1);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void showAddAppointmentInternalFrame(java.awt.event.ActionEvent evt) {
        MainFrame.getInstance().showInternalFrame(addAppointmentInternalFrame);
    }

    private javax.swing.JMenuItem addMenuItem;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable1;
}
