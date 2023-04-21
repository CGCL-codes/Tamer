package net.sourceforge.mords.client.display;

/**
 *
 * @author  david
 */
public class AppointmentPanel extends javax.swing.JPanel {

    /** Creates new form AppointmentPanel */
    public AppointmentPanel() {
        initComponents();
    }

    private void initComponents() {
        detailPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        timeField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        modField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        durationField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        priorityField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        facilityComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        setLayout(new java.awt.GridLayout(0, 1));
        detailPanel.setLayout(new java.awt.GridLayout(0, 2));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("ID:  ");
        detailPanel.add(jLabel1);
        detailPanel.add(idField);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Date:  ");
        detailPanel.add(jLabel2);
        dateField.setEditable(false);
        detailPanel.add(dateField);
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Time:  ");
        detailPanel.add(jLabel3);
        timeField.setEditable(false);
        detailPanel.add(timeField);
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Modified:  ");
        detailPanel.add(jLabel4);
        modField.setEditable(false);
        detailPanel.add(modField);
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Duration (minutes):  ");
        detailPanel.add(jLabel6);
        detailPanel.add(durationField);
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Priority:  ");
        detailPanel.add(jLabel7);
        detailPanel.add(priorityField);
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Facility:  ");
        detailPanel.add(jLabel9);
        detailPanel.add(facilityComboBox);
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Status:  ");
        detailPanel.add(jLabel10);
        detailPanel.add(statusComboBox);
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Name:  ");
        detailPanel.add(jLabel8);
        detailPanel.add(nameField);
        add(detailPanel);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));
        jScrollPane1.setViewportView(jTextArea1);
        add(jScrollPane1);
    }

    private javax.swing.JTextField dateField;

    private javax.swing.JPanel detailPanel;

    private javax.swing.JTextField durationField;

    private javax.swing.JComboBox facilityComboBox;

    private javax.swing.JTextField idField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextField modField;

    private javax.swing.JTextField nameField;

    private javax.swing.JTextField priorityField;

    private javax.swing.JComboBox statusComboBox;

    private javax.swing.JTextField timeField;
}
