package Sourse;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

/**
 *
 * @author wsdess
 */
public class Frm_Diagnostico extends javax.swing.JFrame {

    String id = "";

    int t = 0;

    public Frm_Diagnostico(String id_empleado, int tipo) {
        initComponents();
        id = id_empleado;
        t = tipo;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        activar();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        dataSource1 = new FuenteDeDatos.DataSource();
        panel1 = new org.edisoncor.gui.panel.Panel();
        Lbl_expedientes = new javax.swing.JLabel();
        tbn_salir1 = new org.edisoncor.gui.button.ButtonSeven();
        dtf_idpac = new FuenteDeDatos.DataTextField();
        dtxf_desc = new FuenteDeDatos.DataTextField();
        dtxf_med = new FuenteDeDatos.DataTextField();
        buttonSeven2 = new org.edisoncor.gui.button.ButtonSeven();
        buttonSeven3 = new org.edisoncor.gui.button.ButtonSeven();
        buttonSeven4 = new org.edisoncor.gui.button.ButtonSeven();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        dataSource1.setCodigosql("select * from tbl_expedientes");
        dataSource1.setDb("sacm");
        dataSource1.setIp("localhost");
        dataSource1.setPassword("");
        dataSource1.setUsuario("root");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        panel1.setColorPrimario(new java.awt.Color(255, 255, 255));
        panel1.setColorSecundario(new java.awt.Color(0, 153, 153));
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        Lbl_expedientes.setFont(new java.awt.Font("Tahoma", 1, 24));
        Lbl_expedientes.setForeground(new java.awt.Color(0, 57, 85));
        Lbl_expedientes.setText("Diagnosticar paciente");
        panel1.add(Lbl_expedientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, 43));
        tbn_salir1.setBackground(new java.awt.Color(255, 0, 0));
        tbn_salir1.setText("X");
        tbn_salir1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbn_salir1ActionPerformed(evt);
            }
        });
        panel1.add(tbn_salir1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 30, 20));
        dtf_idpac.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dtf_idpacActionPerformed(evt);
            }
        });
        dtf_idpac.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                dtf_idpacKeyTyped(evt);
            }
        });
        panel1.add(dtf_idpac, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 124, 203, -1));
        dtxf_desc.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dtxf_descActionPerformed(evt);
            }
        });
        panel1.add(dtxf_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 162, 203, -1));
        panel1.add(dtxf_med, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 203, -1));
        buttonSeven2.setBackground(new java.awt.Color(0, 57, 85));
        buttonSeven2.setText("Ingresar");
        buttonSeven2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSeven2ActionPerformed(evt);
            }
        });
        panel1.add(buttonSeven2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 330, 90, -1));
        buttonSeven3.setBackground(new java.awt.Color(0, 57, 85));
        buttonSeven3.setText("Guardar");
        buttonSeven3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSeven3ActionPerformed(evt);
            }
        });
        panel1.add(buttonSeven3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, 100, -1));
        buttonSeven4.setBackground(new java.awt.Color(0, 57, 85));
        buttonSeven4.setText("Cancelar");
        panel1.add(buttonSeven4, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 330, 93, -1));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SACM_SPLASH.png")));
        panel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 50, -1, -1));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel11.setForeground(new java.awt.Color(0, 57, 85));
        jLabel11.setText("ID_Paciente:");
        panel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 124, -1, -1));
        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel12.setForeground(new java.awt.Color(0, 57, 85));
        jLabel12.setText("Medicacion:");
        panel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel13.setForeground(new java.awt.Color(0, 57, 85));
        jLabel13.setText("Descripcion:");
        panel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 162, -1, -1));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE));
        pack();
    }

    private void tbn_salir1ActionPerformed(java.awt.event.ActionEvent evt) {
        Frm_menu log = new Frm_menu(id, t);
        log.setVisible(true);
        this.dispose();
    }

    private void buttonSeven2ActionPerformed(java.awt.event.ActionEvent evt) {
        desactivar();
        this.dtf_idpac.requestFocus();
    }

    private void dtf_idpacActionPerformed(java.awt.event.ActionEvent evt) {
        desactivar();
        this.dtxf_desc.setEnabled(true);
        this.dtxf_desc.requestFocus();
    }

    private void dtf_idpacKeyTyped(java.awt.event.KeyEvent evt) {
        char numero = evt.getKeyChar();
        if (!(numero >= '0' && numero <= '9')) {
            evt.consume();
        }
    }

    private void buttonSeven3ActionPerformed(java.awt.event.ActionEvent evt) {
        activar();
        this.dataSource1.consulta("insert into tbl_expedientes (id_paciente,Fecha,descripcion,medicacion ) values('" + this.dtf_idpac.getText() + "',CURDATE(),'" + this.dtxf_desc.getText() + "','" + this.dtxf_med.getText() + "');");
        JOptionPane.showMessageDialog(this, "Diagnostico registrado", "Validacion Campos", JOptionPane.ERROR_MESSAGE);
        limpiarcampos();
    }

    private void dtxf_descActionPerformed(java.awt.event.ActionEvent evt) {
        desactivar();
        this.dtxf_desc.setEnabled(true);
        this.dtxf_med.setEnabled(true);
        this.dtxf_med.requestFocus();
    }

    public void limpiarcampos() {
        this.dtf_idpac.setText(" ");
        this.dtxf_desc.setText(" ");
        this.dtxf_med.setText(" ");
    }

    public void desactivar() {
        this.dtf_idpac.setEnabled(true);
        this.buttonSeven3.setEnabled(true);
        this.buttonSeven4.setEnabled(true);
    }

    public void activar() {
        this.buttonSeven4.setEnabled(false);
        this.buttonSeven3.setEnabled(false);
        this.dtf_idpac.setEnabled(false);
        this.dtxf_desc.setEnabled(false);
        this.dtxf_med.setEnabled(false);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Frm_Diagnostico(null, 0).setVisible(true);
            }
        });
    }

    private javax.swing.JLabel Lbl_expedientes;

    private org.edisoncor.gui.button.ButtonSeven buttonSeven2;

    private org.edisoncor.gui.button.ButtonSeven buttonSeven3;

    private org.edisoncor.gui.button.ButtonSeven buttonSeven4;

    private FuenteDeDatos.DataSource dataSource1;

    private FuenteDeDatos.DataTextField dtf_idpac;

    private FuenteDeDatos.DataTextField dtxf_desc;

    private FuenteDeDatos.DataTextField dtxf_med;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private org.edisoncor.gui.panel.Panel panel1;

    private org.edisoncor.gui.button.ButtonSeven tbn_salir1;
}
