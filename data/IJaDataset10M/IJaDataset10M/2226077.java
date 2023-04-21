package prototipo;

import javax.swing.*;
import BL.torneoBL;
import BE.torneo;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  Billy
 */
public class frmEditarTorneo extends javax.swing.JInternalFrame {

    String nombreTorneo, ciudad, organizador;

    torneo admtorneo;

    DefaultTableModel mdt;

    frmAdmTorneo fat;

    /** Creates new form frmNuevoTorneo2 */
    public frmEditarTorneo() {
        initComponents();
        admtorneo = new torneo();
    }

    public frmEditarTorneo(torneo elTorneo, frmAdmTorneo fat) {
        initComponents();
        this.admtorneo = elTorneo;
        this.txtNombreTorneo.setText(elTorneo.getnombreTorneo());
        this.txtCiudad.setText(elTorneo.getCiudad());
        this.txtOrganizador.setText(elTorneo.getOrganizador());
        this.jdcFechaInicio.setDate(elTorneo.getFechaInicio().getTime());
        this.jdcFechaFinal.setDate(elTorneo.getFechaFin().getTime());
        this.fat = fat;
    }

    public void LimpiarDatos() {
        this.txtNombreTorneo.setText("");
        this.txtCiudad.setText("");
        this.txtOrganizador.setText("");
        this.jdcFechaInicio.setDate(null);
        this.jdcFechaFinal.setDate(null);
    }

    public void ValidarDatos() {
        nombreTorneo = txtNombreTorneo.getText();
        ciudad = this.txtCiudad.getText();
        organizador = this.txtOrganizador.getText();
    }

    private void initComponents() {
        pnlNuevoTorneo = new javax.swing.JPanel();
        lblNombreTorneo = new javax.swing.JLabel();
        lblCiudad = new javax.swing.JLabel();
        lblFechaInicio = new javax.swing.JLabel();
        lblFechaFinal = new javax.swing.JLabel();
        lblOrganizador = new javax.swing.JLabel();
        txtCiudad = new javax.swing.JTextField();
        txtNombreTorneo = new javax.swing.JTextField();
        txtOrganizador = new javax.swing.JTextField();
        jdcFechaInicio = new com.toedter.calendar.JDateChooser();
        jdcFechaFinal = new com.toedter.calendar.JDateChooser();
        btnLimpiarDatos = new javax.swing.JButton();
        btnCrearTorneo = new javax.swing.JButton();
        pnlFiguraNuevoTorneo = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setTitle("Editar Torneo");
        pnlNuevoTorneo.setBorder(javax.swing.BorderFactory.createTitledBorder("Editar Torneo"));
        lblNombreTorneo.setText("Nombre de torneo:");
        lblCiudad.setText("Ciudad: ");
        lblFechaInicio.setText("Fecha de Inicio:");
        lblFechaFinal.setText("Fecha Final:");
        lblOrganizador.setText("Organizador:");
        btnLimpiarDatos.setText("Limpiar Datos");
        btnLimpiarDatos.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarDatosActionPerformed(evt);
            }
        });
        btnCrearTorneo.setText("Editar Torneo");
        btnCrearTorneo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearTorneoActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout pnlNuevoTorneoLayout = new javax.swing.GroupLayout(pnlNuevoTorneo);
        pnlNuevoTorneo.setLayout(pnlNuevoTorneoLayout);
        pnlNuevoTorneoLayout.setHorizontalGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlNuevoTorneoLayout.createSequentialGroup().addContainerGap().addGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlNuevoTorneoLayout.createSequentialGroup().addGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblFechaInicio).addComponent(jdcFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(16, 16, 16).addGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(pnlNuevoTorneoLayout.createSequentialGroup().addComponent(lblFechaFinal).addGap(52, 52, 52)).addComponent(jdcFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(lblNombreTorneo).addComponent(txtNombreTorneo, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE).addComponent(lblCiudad).addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblOrganizador).addComponent(txtOrganizador, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE).addGroup(pnlNuevoTorneoLayout.createSequentialGroup().addComponent(btnLimpiarDatos).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE).addComponent(btnCrearTorneo))).addContainerGap()));
        pnlNuevoTorneoLayout.setVerticalGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlNuevoTorneoLayout.createSequentialGroup().addComponent(lblNombreTorneo).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtNombreTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblCiudad).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblFechaInicio).addComponent(lblFechaFinal)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jdcFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jdcFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblOrganizador).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtOrganizador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(20, 20, 20).addGroup(pnlNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnLimpiarDatos).addComponent(btnCrearTorneo))));
        pnlFiguraNuevoTorneo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/prototipo/torneoImagen2.jpg")));
        javax.swing.GroupLayout pnlFiguraNuevoTorneoLayout = new javax.swing.GroupLayout(pnlFiguraNuevoTorneo);
        pnlFiguraNuevoTorneo.setLayout(pnlFiguraNuevoTorneoLayout);
        pnlFiguraNuevoTorneoLayout.setHorizontalGroup(pnlFiguraNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6));
        pnlFiguraNuevoTorneoLayout.setVerticalGroup(pnlFiguraNuevoTorneoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(pnlNuevoTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pnlFiguraNuevoTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(pnlNuevoTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(20, 20, 20).addComponent(pnlFiguraNuevoTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSalir).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void btnCrearTorneoActionPerformed(java.awt.event.ActionEvent evt) {
        boolean resultado;
        admtorneo.setnombreTorneo(txtNombreTorneo.getText());
        admtorneo.setCiudad(txtCiudad.getText());
        admtorneo.setOrganizador(txtOrganizador.getText());
        admtorneo.setFechaInicio(this.jdcFechaInicio.getCalendar());
        admtorneo.setFechaFin(this.jdcFechaFinal.getCalendar());
        admtorneo.setNumeroDivisiones(0);
        admtorneo.setTerminoTorneo(false);
        if (this.txtNombreTorneo.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Ponga el nombre del Torneo, Por favor", "Informacion", JOptionPane.WARNING_MESSAGE);
        } else {
            torneoBL admTorneoBL = new torneoBL();
            resultado = admTorneoBL.actualizarTorneo(admtorneo.getIdTorneo(), admtorneo);
            if (resultado == true) {
                JOptionPane.showMessageDialog(this, "Se ha actualizado correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                this.fat.actualizardatosTorneo();
            } else {
                JOptionPane.showMessageDialog(this, "Ha Ocurrido un error. No se han grabado los datos", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void btnLimpiarDatosActionPerformed(java.awt.event.ActionEvent evt) {
        LimpiarDatos();
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private javax.swing.JButton btnCrearTorneo;

    private javax.swing.JButton btnLimpiarDatos;

    private javax.swing.JButton btnSalir;

    private javax.swing.JLabel jLabel6;

    private com.toedter.calendar.JDateChooser jdcFechaFinal;

    private com.toedter.calendar.JDateChooser jdcFechaInicio;

    private javax.swing.JLabel lblCiudad;

    private javax.swing.JLabel lblFechaFinal;

    private javax.swing.JLabel lblFechaInicio;

    private javax.swing.JLabel lblNombreTorneo;

    private javax.swing.JLabel lblOrganizador;

    private javax.swing.JPanel pnlFiguraNuevoTorneo;

    private javax.swing.JPanel pnlNuevoTorneo;

    private javax.swing.JTextField txtCiudad;

    private javax.swing.JTextField txtNombreTorneo;

    private javax.swing.JTextField txtOrganizador;
}
