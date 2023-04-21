package mascotrin;

import java.sql.*;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.MaskFormatter;
import java.awt.BorderLayout;

/**
 *
 * @author  Daniel-Lap
 */
public class RegistrarMascota extends javax.swing.JFrame {

    /** Creates new form RegistrarMascota */
    public RegistrarMascota() {
        initComponents();
        TextoMascara();
    }

    JFormattedTextField fecha;

    JFormattedTextField peso;

    public void TextoMascara() {
        try {
            JLabel lblFecha = new JLabel();
            lblFecha.setText("Fecha Nacimiento");
            MaskFormatter mask = new MaskFormatter("##/##/####");
            fecha = new JFormattedTextField(mask);
            peso = new JFormattedTextField(new Double(0));
            DatosMascota.add(peso);
            DatosMascota.add(lblFecha);
            DatosMascota.add(fecha);
        } catch (java.text.ParseException e) {
        }
    }

    private void initComponents() {
        jTextField7 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        DatosMascota = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem26 = new javax.swing.JMenuItem();
        jTextField7.setText("jTextField7");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RegistrarMascota");
        jButton1.setText("Registrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jMenu1.setText("Cliente");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });
        jMenuItem1.setText("Registrar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenuItem2.setText("Consultar");
        jMenu1.add(jMenuItem2);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Mascota");
        jMenuItem3.setText("Registrar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);
        jMenuItem4.setText("Consultar");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);
        jMenuBar1.add(jMenu2);
        jMenu3.setText("Producto");
        jMenuItem5.setText("Registrar");
        jMenu3.add(jMenuItem5);
        jMenuItem6.setText("Consultar");
        jMenu3.add(jMenuItem6);
        jMenuBar1.add(jMenu3);
        jMenu4.setText("Venta");
        jMenuItem7.setText("Registrar");
        jMenu4.add(jMenuItem7);
        jMenuItem8.setText("Consultar");
        jMenu4.add(jMenuItem8);
        jMenuBar1.add(jMenu4);
        DatosMascota.setLayout(new java.awt.GridLayout(9, 2, 0, 7));
        jLabel1.setText("IDCliente");
        DatosMascota.add(jLabel1);
        DatosMascota.add(jTextField1);
        jLabel2.setText("Nombre");
        DatosMascota.add(jLabel2);
        DatosMascota.add(jTextField2);
        jLabel3.setText("Raza");
        DatosMascota.add(jLabel3);
        DatosMascota.add(jTextField3);
        jLabel4.setText("Tamaño");
        DatosMascota.add(jLabel4);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "chico", "mediano", "grande" }));
        DatosMascota.add(jComboBox2);
        jLabel5.setText("Color");
        DatosMascota.add(jLabel5);
        DatosMascota.add(jTextField5);
        jLabel7.setText("Pelo");
        DatosMascota.add(jLabel7);
        DatosMascota.add(jTextField8);
        jLabel8.setText("Sexo");
        DatosMascota.add(jLabel8);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Macho", "Hembra" }));
        DatosMascota.add(jComboBox1);
        jLabel6.setText("Peso");
        DatosMascota.add(jLabel6);
        jMenu5.setText("Cliente");
        jMenuItem9.setText("Registrar");
        jMenu5.add(jMenuItem9);
        jMenuItem10.setText("Consultar");
        jMenu5.add(jMenuItem10);
        jMenuBar2.add(jMenu5);
        jMenu6.setText("Mascota");
        jMenuItem11.setText("Registrar");
        jMenu6.add(jMenuItem11);
        jMenuItem12.setText("Consultar");
        jMenu6.add(jMenuItem12);
        jMenuBar2.add(jMenu6);
        jMenu7.setText("Producto");
        jMenuItem13.setText("Registrar");
        jMenu7.add(jMenuItem13);
        jMenuItem14.setText("Consultar");
        jMenu7.add(jMenuItem14);
        jMenuBar2.add(jMenu7);
        jMenu8.setText("Venta");
        jMenuItem15.setText("Registrar");
        jMenu8.add(jMenuItem15);
        jMenuItem16.setText("Consultar");
        jMenu8.add(jMenuItem16);
        jMenuBar2.add(jMenu8);
        jMenu9.setText("Cliente");
        jMenu9.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu9ActionPerformed(evt);
            }
        });
        jMenuItem17.setText("Registrar");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem17);
        jMenuItem18.setText("Consultar");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem18);
        jMenuBar3.add(jMenu9);
        jMenu10.setText("Mascota");
        jMenuItem19.setText("Registrar");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem19);
        jMenuItem20.setText("Consultar");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem20);
        jMenuBar3.add(jMenu10);
        jMenu11.setText("Producto");
        jMenuItem21.setText("Registrar");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem21);
        jMenuItem22.setText("Consultar");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem22);
        jMenuBar3.add(jMenu11);
        jMenu12.setText("Venta");
        jMenuItem23.setText("Registrar");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem23);
        jMenuItem24.setText("Consultar");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem24);
        jMenuBar3.add(jMenu12);
        jMenu13.setText("Avisos");
        jMenuItem25.setText("Ver Avisos");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem25);
        jMenuBar3.add(jMenu13);
        jMenu14.setText("BaseDatos");
        jMenuItem26.setText("Instalar");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem26);
        jMenuBar3.add(jMenu14);
        setJMenuBar(jMenuBar3);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton1).addComponent(DatosMascota, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(19, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(DatosMascota, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jButton1).addContainerGap()));
        pack();
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarCliente rc = new RegistrarCliente();
        rc.setEnabled(true);
        rc.setVisible(true);
        this.dispose();
    }

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarMascota rm = new RegistrarMascota();
        rm.setEnabled(true);
        rm.setVisible(true);
        this.dispose();
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarMascota cm = new ConsultarMascota();
        cm.setEnabled(true);
        cm.setVisible(true);
        this.dispose();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String idCliente = jTextField1.getText();
        String nombre = jTextField2.getText();
        String raza = jTextField3.getText();
        String tamaño = jComboBox1.getSelectedItem().toString();
        String color = jTextField5.getText();
        double pesop = Double.parseDouble(peso.getText());
        String pelo = jTextField8.getText();
        String sexo = jComboBox2.getSelectedItem().toString();
        String fechanacimiento = fecha.getText();
        String SQL = "INSERT INTO Mascota VALUES('" + idCliente + "','" + nombre + "','" + raza + "','" + tamaño + "','" + color + "','" + pesop + "','" + pelo + "','" + sexo + "','" + fechanacimiento + "')";
        CambiosSQL sqlRM = new CambiosSQL(SQL);
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField5.setText("");
        peso.setText("");
        jTextField8.setText("");
    }

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistraC rc = new RegistraC();
        this.remove(getContentPane());
        getContentPane().add(rc, BorderLayout.CENTER);
        setVisible(true);
        this.repaint();
    }

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarCliente cc = new ConsultarCliente();
        cc.setEnabled(true);
        cc.setVisible(true);
        this.dispose();
    }

    private void jMenu9ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarMascota rm = new RegistrarMascota();
        rm.setEnabled(true);
        rm.setVisible(true);
        this.dispose();
    }

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarMascota cm = new ConsultarMascota();
        cm.setEnabled(true);
        cm.setVisible(true);
        this.dispose();
    }

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarProducto rp = new RegistrarProducto();
        rp.setEnabled(true);
        rp.setVisible(true);
        this.dispose();
    }

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarProducto cp = new ConsultarProducto();
        cp.setEnabled(true);
        cp.setVisible(true);
        this.dispose();
    }

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarVenta rv = new RegistrarVenta();
        rv.setEnabled(true);
        rv.setVisible(true);
        this.dispose();
    }

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarVenta cv = new ConsultarVenta();
        cv.setEnabled(true);
        cv.setVisible(true);
        this.dispose();
    }

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {
        Avisos aviso = new Avisos();
        aviso.setEnabled(true);
        aviso.setVisible(true);
    }

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {
        Wizard w = new Wizard();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RegistrarMascota().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel DatosMascota;

    private javax.swing.JButton jButton1;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JComboBox jComboBox2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu10;

    private javax.swing.JMenu jMenu11;

    private javax.swing.JMenu jMenu12;

    private javax.swing.JMenu jMenu13;

    private javax.swing.JMenu jMenu14;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenu jMenu4;

    private javax.swing.JMenu jMenu5;

    private javax.swing.JMenu jMenu6;

    private javax.swing.JMenu jMenu7;

    private javax.swing.JMenu jMenu8;

    private javax.swing.JMenu jMenu9;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuBar jMenuBar2;

    private javax.swing.JMenuBar jMenuBar3;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem10;

    private javax.swing.JMenuItem jMenuItem11;

    private javax.swing.JMenuItem jMenuItem12;

    private javax.swing.JMenuItem jMenuItem13;

    private javax.swing.JMenuItem jMenuItem14;

    private javax.swing.JMenuItem jMenuItem15;

    private javax.swing.JMenuItem jMenuItem16;

    private javax.swing.JMenuItem jMenuItem17;

    private javax.swing.JMenuItem jMenuItem18;

    private javax.swing.JMenuItem jMenuItem19;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem20;

    private javax.swing.JMenuItem jMenuItem21;

    private javax.swing.JMenuItem jMenuItem22;

    private javax.swing.JMenuItem jMenuItem23;

    private javax.swing.JMenuItem jMenuItem24;

    private javax.swing.JMenuItem jMenuItem25;

    private javax.swing.JMenuItem jMenuItem26;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JMenuItem jMenuItem5;

    private javax.swing.JMenuItem jMenuItem6;

    private javax.swing.JMenuItem jMenuItem7;

    private javax.swing.JMenuItem jMenuItem8;

    private javax.swing.JMenuItem jMenuItem9;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField5;

    private javax.swing.JTextField jTextField7;

    private javax.swing.JTextField jTextField8;
}
