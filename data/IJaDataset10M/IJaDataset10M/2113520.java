package TablasJavaSep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  SQL
 */
public class Principal extends javax.swing.JFrame {

    private Connection conn;

    /** Creates new form Principal */
    public Principal() {
        try {
            initComponents();
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "littium");
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel model = new DefaultTableModel();
        jTable1 = new javax.swing.JTable();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jTextField1.setText("jTextField1");
        jPanel3.add(jTextField1);
        jTextField2.setText("jTextField2");
        jPanel3.add(jTextField2);
        jTextField3.setText("jTextField3");
        jPanel3.add(jTextField3);
        jPanel1.add(jPanel3);
        jButton1.setText("add");
        jPanel4.add(jButton1);
        jButton2.setText("delete");
        jPanel4.add(jButton2);
        jButton3.setText("modify");
        jPanel4.add(jButton3);
        jButton4.setText("load");
        jPanel4.add(jButton4);
        jPanel1.add(jPanel4);
        getContentPane().add(jPanel1);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jTable1.setModel(model);
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Domicilio");
        jScrollPane1.setViewportView(jTable1);
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanel2);
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;
}
