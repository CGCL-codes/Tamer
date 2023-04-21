package com.tdcs.lords.client.display;

import com.tdcs.lords.client.util.LastNameSearcher;
import com.tdcs.lords.client.util.PhoneSearcher;
import com.tdcs.lords.client.util.RecordSearcher;
import com.tdcs.lords.client.util.SSNSearcher;
import com.tdcs.lords.client.util.Searchable;
import com.tdcs.lords.obj.Patient;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  david
 */
public class RecordsSelect extends javax.swing.JPanel implements Searchable {

    private Collection<Patient> patients;

    private Date time;

    private Vector<String> headers = null;

    private Patient selPatient;

    private Vector<Patient> selPatients;

    /** Creates new form RecordsSelect */
    public RecordsSelect() {
        initComponents();
        time = new Date();
        recordField.requestFocusInWindow();
        selPatients = new Vector<Patient>();
        list.setListData(selPatients);
    }

    private void matchRecord(String record) {
        Iterator<Patient> it = patients.iterator();
        while (it.hasNext()) {
            Patient pt = it.next();
            if (pt.getRecord().equals(record)) {
                selPatient = pt;
                break;
            }
        }
    }

    public void setData(Collection<Patient> patients) {
        this.patients = patients;
        setSearched(patients);
    }

    public Vector<Patient> getSelectedPatients() {
        return selPatients;
    }

    private void initComponents() {
        searchPanel = new javax.swing.JPanel();
        optionPanel = new javax.swing.JPanel();
        recordRadioButton = new javax.swing.JRadioButton();
        recordField = new javax.swing.JTextField();
        lastNameRadioButton = new javax.swing.JRadioButton();
        lastNameField = new javax.swing.JTextField();
        ssnRadioButton = new javax.swing.JRadioButton();
        ssnField = new javax.swing.JTextField();
        phoneRadioButton = new javax.swing.JRadioButton();
        phoneField = new javax.swing.JFormattedTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        remButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        selectedPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        setLayout(new java.awt.GridLayout(0, 1));
        searchPanel.setLayout(new java.awt.BorderLayout());
        optionPanel.setLayout(new java.awt.GridLayout(0, 2));
        recordRadioButton.setSelected(true);
        recordRadioButton.setText("Record #:");
        recordRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        recordRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        optionPanel.add(recordRadioButton);
        recordField.setFocusCycleRoot(true);
        recordField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                recordFieldKeyReleased(evt);
            }
        });
        optionPanel.add(recordField);
        lastNameRadioButton.setText("Last Name:");
        lastNameRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lastNameRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        optionPanel.add(lastNameRadioButton);
        lastNameField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                lastNameFieldKeyReleased(evt);
            }
        });
        optionPanel.add(lastNameField);
        ssnRadioButton.setText("SSN:");
        ssnRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ssnRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        optionPanel.add(ssnRadioButton);
        ssnField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                ssnFieldKeyReleased(evt);
            }
        });
        optionPanel.add(ssnField);
        phoneRadioButton.setText("Phone Number:");
        phoneRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        phoneRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        optionPanel.add(phoneRadioButton);
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                phoneFieldKeyReleased(evt);
            }
        });
        optionPanel.add(phoneField);
        searchPanel.add(optionPanel, java.awt.BorderLayout.NORTH);
        table.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        table.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table);
        searchPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        addButton.setText("Add V");
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addButton);
        remButton.setText("Remove ^");
        remButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remButtonActionPerformed(evt);
            }
        });
        jPanel1.add(remButton);
        clearButton.setText("Clear List");
        clearButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        jPanel1.add(clearButton);
        searchPanel.add(jPanel1, java.awt.BorderLayout.PAGE_END);
        add(searchPanel);
        selectedPanel.setLayout(new java.awt.BorderLayout());
        list.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(list);
        selectedPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jLabel1.setText("<html> <body> <p>Select the records that are going to be moved/combined.  These are the records that are redundant (multiple entries for the same patient), erroneous(need to change the SSN field, which changes the record ID), or not current (patient's name has changed.)</p> <p>If the records are going to be merged with a single existing record, <b>DO NOT SELECT THE DESTINATION RECORD!!!</b>  You will select the destination in the next step.</p> </body> </html>");
        selectedPanel.add(jLabel1, java.awt.BorderLayout.PAGE_START);
        add(selectedPanel);
    }

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selPatients = new Vector<Patient>();
        list.setListData(selPatients);
    }

    private void remButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.getSelectedIndex() != -1) {
            int remIndex = list.getSelectedIndex();
            selPatients.remove(remIndex);
            list.setListData(selPatients);
        }
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (selPatient != null) {
            selPatients.add(selPatient);
            selPatient = null;
            list.setListData(selPatients);
        }
    }

    private void phoneFieldKeyReleased(java.awt.event.KeyEvent evt) {
        if (phoneRadioButton.isSelected()) {
            new PhoneSearcher(patients, this, phoneField.getText()).start();
        }
    }

    private void ssnFieldKeyReleased(java.awt.event.KeyEvent evt) {
        if (ssnRadioButton.isSelected()) {
            new SSNSearcher(patients, this, ssnField.getText()).start();
        }
    }

    private void lastNameFieldKeyReleased(java.awt.event.KeyEvent evt) {
        if (lastNameRadioButton.isSelected()) {
            new LastNameSearcher(patients, this, lastNameField.getText()).start();
        }
    }

    private void recordFieldKeyReleased(java.awt.event.KeyEvent evt) {
        if (recordRadioButton.isSelected()) {
            new RecordSearcher(patients, this, recordField.getText()).start();
        }
    }

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {
        int row = table.getSelectedRow();
        if (row != -1) {
            String record = (String) table.getValueAt(row, 0);
            matchRecord(record);
        }
    }

    public synchronized void setSearched(Collection<Patient> listed, Date now) {
        if (time.before(now)) {
            time = now;
            setSearched(listed);
        }
    }

    private void setSearched(Collection<Patient> listed) {
        makeHeaders();
        Vector<Vector> data = new Vector<Vector>();
        Iterator<Patient> it = listed.iterator();
        while (it.hasNext()) {
            Patient pt = it.next();
            Vector<String> row = new Vector<String>();
            row.add(pt.getRecord());
            row.add(pt.getLastName());
            row.add(pt.getFirstName());
            row.add(pt.getSsn());
            row.add(pt.getPhone());
            row.add(pt.getAddress());
            data.add(row);
        }
        table.setModel(new DefaultTableModel(data, headers));
    }

    private void makeHeaders() {
        if (headers != null) {
            return;
        } else {
            headers = new Vector<String>();
            headers.add("Record");
            headers.add("Last Name");
            headers.add("First Name");
            headers.add("SSN");
            headers.add("Phone");
            headers.add("Address");
        }
    }

    public void setFound(Collection<String> list, Date now) {
    }

    private javax.swing.JButton addButton;

    private javax.swing.JButton clearButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextField lastNameField;

    private javax.swing.JRadioButton lastNameRadioButton;

    private javax.swing.JList list;

    private javax.swing.JPanel optionPanel;

    private javax.swing.JFormattedTextField phoneField;

    private javax.swing.JRadioButton phoneRadioButton;

    private javax.swing.JTextField recordField;

    private javax.swing.JRadioButton recordRadioButton;

    private javax.swing.JButton remButton;

    private javax.swing.JPanel searchPanel;

    private javax.swing.JPanel selectedPanel;

    private javax.swing.JTextField ssnField;

    private javax.swing.JRadioButton ssnRadioButton;

    private javax.swing.JTable table;
}
