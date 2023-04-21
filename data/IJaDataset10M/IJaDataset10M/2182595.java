package mascotrin;

import java.awt.Dimension;

/**
 *
 * @author  PcAbajo
 */
public class ConsultaClie extends javax.swing.JPanel {

    /** Creates new form ConsultaClie */
    public ConsultaClie() {
        initComponents();
        tamano();
    }

    public void tamano() {
        Dimension d = new Dimension(400, 400);
        this.setSize(d);
        this.setMaximumSize(d);
        this.setMinimumSize(d);
    }

    private void initComponents() {
        jComboBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "IDCliente", "Nombre", "ApellidoPaterno", "ApellidoMaterno", "Telefono", "Calle", "Colonia", "Todos" }));
        add(jComboBox1);
        jTextField1.setText("                    ");
        add(jTextField1);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "IDCliente", "Nombre", "ApellidoPaterno", "ApellidoMaterno", "Telefono", "Calle", "Colonia" }));
        add(jComboBox2);
        jTextField2.setText("                          ");
        add(jTextField2);
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {} }, new String[] {}));
        jScrollPane1.setViewportView(jTable1);
        add(jScrollPane1);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String Busqueda1 = jComboBox1.getSelectedItem().toString();
        String Busqueda2 = jComboBox2.getSelectedItem().toString();
        String dato1 = jTextField1.getText();
        String dato2 = jTextField2.getText();
        String SQL;
        if (Busqueda1.equalsIgnoreCase("Todos") && Busqueda1.equalsIgnoreCase(Busqueda2) == false) {
            SQL = "Select * from Cliente where " + Busqueda2 + " ='" + dato2 + "'";
        } else if (Busqueda2.equalsIgnoreCase("Todos") && Busqueda1.equalsIgnoreCase(Busqueda2) == false) {
            SQL = "Select * from Cliente where " + Busqueda1 + " ='" + dato1 + "'";
        } else if (Busqueda1.equalsIgnoreCase("Todos") && Busqueda2.equalsIgnoreCase("Todos")) {
            SQL = "Select * from Cliente";
        } else {
            SQL = "Select * from Cliente where " + Busqueda1 + " ='" + dato1 + "' and " + Busqueda2 + " = '" + dato2 + "'";
        }
        ConsultasSQL sqlCC = new ConsultasSQL(SQL, jTable1);
        jTable1.setEnabled(false);
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JComboBox jComboBox2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;
}
