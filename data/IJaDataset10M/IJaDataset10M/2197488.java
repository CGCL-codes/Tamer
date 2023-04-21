package mascotrin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.*;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author  Daniel-Lap
 */
public class RegistrarCliente extends javax.swing.JFrame {

    /** Creates new form RegistrarCliente */
    public RegistrarCliente() {
        initComponents();
        tamano();
        TextoCliente();
    }

    JFormattedTextField codigoPostal;

    JFormattedTextField nexterior;

    JFormattedTextField fecha;

    JFormattedTextField telefono;

    public void TextoCliente() {
        try {
            JLabel lblfecha = new JLabel();
            lblfecha.setText("Fecha Nacimiento");
            JLabel lblCp = new JLabel();
            lblCp.setText("Codigo Postal");
            JLabel lbltel = new JLabel();
            lbltel.setText("Telefono");
            MaskFormatter mask = new MaskFormatter("##/##/####");
            MaskFormatter maskt = new MaskFormatter("##-##-##-##");
            MaskFormatter maskex = new MaskFormatter("####");
            MaskFormatter maskcp = new MaskFormatter("#####");
            fecha = new JFormattedTextField(mask);
            telefono = new JFormattedTextField(maskt);
            codigoPostal = new JFormattedTextField(maskcp);
            nexterior = new JFormattedTextField(maskex);
            DatosCliente.add(nexterior);
            DatosCliente.add(lblCp);
            DatosCliente.add(codigoPostal);
            DatosCliente.add(lbltel);
            DatosCliente.add(telefono);
            DatosCliente.add(lblfecha);
            DatosCliente.add(fecha);
        } catch (java.text.ParseException e) {
        }
    }

    public void tamano() {
        Dimension d = new Dimension(300, 300);
        this.setSize(d);
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        this.pack();
    }

    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        DatosCliente = new javax.swing.JPanel();
        idCliente = new javax.swing.JLabel();
        idclienteTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        nombreTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        apellidopTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
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
        jMenu6 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro de Clientes");
        jButton1.setText("Registrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        DatosCliente.setLayout(new java.awt.GridLayout(10, 2, -40, 4));
        idCliente.setText("IdCliente");
        DatosCliente.add(idCliente);
        DatosCliente.add(idclienteTxt);
        jLabel1.setText("Nombre");
        DatosCliente.add(jLabel1);
        DatosCliente.add(nombreTxt);
        jLabel2.setText("Apellido Paterno");
        DatosCliente.add(jLabel2);
        DatosCliente.add(apellidopTxt);
        jLabel3.setText("Apellido Materno");
        DatosCliente.add(jLabel3);
        DatosCliente.add(jTextField3);
        jLabel4.setText("Calle");
        DatosCliente.add(jLabel4);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        DatosCliente.add(jTextField4);
        jLabel5.setText("Colonia");
        DatosCliente.add(jLabel5);
        DatosCliente.add(jTextField5);
        jLabel6.setText("N.Exterior");
        DatosCliente.add(jLabel6);
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
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
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
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);
        jMenuItem6.setText("Consultar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);
        jMenuBar1.add(jMenu3);
        jMenu4.setText("Venta");
        jMenuItem7.setText("Registrar");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);
        jMenuItem8.setText("Consultar");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);
        jMenuBar1.add(jMenu4);
        jMenu6.setText("Avisos");
        jMenuItem11.setText("Ver Avisos");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem11);
        jMenuBar1.add(jMenu6);
        jMenu5.setText("BaseDatos");
        jMenuItem10.setText("Instalar");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);
        jMenuBar1.add(jMenu5);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1).addComponent(DatosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(DatosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jButton1)));
        pack();
    }

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String idcliente = idclienteTxt.getText();
        String nombre = nombreTxt.getText();
        String apellidop = apellidopTxt.getText();
        String apellidom = jTextField3.getText();
        String calle = jTextField4.getText();
        String colonia = jTextField5.getText();
        String nexterio = nexterior.getText();
        String cp = codigoPostal.getText();
        String FechaNacimiento = fecha.getText();
        String telefon = telefono.getText();
        String SQL = "INSERT INTO Cliente VALUES('" + idcliente + "','" + nombre + "','" + apellidop + "','" + apellidom + "','" + calle + "','" + colonia + "','" + nexterio + "','" + cp + "','" + FechaNacimiento + "','" + telefon + "')";
        CambiosSQL sqlRC = new CambiosSQL(SQL);
        idclienteTxt.setText("");
        nombreTxt.setText("");
        apellidopTxt.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        nexterior.setText("");
        codigoPostal.setText("");
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistraC rc = new RegistraC();
        this.remove(getContentPane());
        getContentPane().add(rc, BorderLayout.CENTER);
        setVisible(true);
        this.repaint();
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarCliente cc = new ConsultarCliente();
        cc.setEnabled(true);
        cc.setVisible(true);
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

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarProducto rp = new RegistrarProducto();
        rp.setEnabled(true);
        rp.setVisible(true);
        this.dispose();
    }

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarProducto cp = new ConsultarProducto();
        cp.setEnabled(true);
        cp.setVisible(true);
        this.dispose();
    }

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarVenta rv = new RegistrarVenta();
        rv.setEnabled(true);
        rv.setVisible(true);
        this.dispose();
    }

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {
        ConsultarVenta cv = new ConsultarVenta();
        cv.setEnabled(true);
        cv.setVisible(true);
        this.dispose();
    }

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {
        Avisos aviso = new Avisos();
        aviso.setEnabled(true);
        aviso.setVisible(true);
    }

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {
        Wizard w = new Wizard();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RegistrarCliente().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel DatosCliente;

    private javax.swing.JTextField apellidopTxt;

    private javax.swing.JLabel idCliente;

    private javax.swing.JTextField idclienteTxt;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenu jMenu4;

    private javax.swing.JMenu jMenu5;

    private javax.swing.JMenu jMenu6;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem10;

    private javax.swing.JMenuItem jMenuItem11;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JMenuItem jMenuItem5;

    private javax.swing.JMenuItem jMenuItem6;

    private javax.swing.JMenuItem jMenuItem7;

    private javax.swing.JMenuItem jMenuItem8;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField4;

    private javax.swing.JTextField jTextField5;

    private javax.swing.JTextField nombreTxt;
}
