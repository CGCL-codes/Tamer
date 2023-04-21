package org.cis.jproyinv.comun.vistas;

import java.util.Date;
import javax.swing.JOptionPane;
import org.cis.jproyinv.comun.modelo.Direccion;
import org.cis.jproyinv.comun.modelo.Docente;
import org.cis.jproyinv.comun.modelo.EstadoCivil;
import org.cis.jproyinv.comun.modelo.EstadoLaboral;
import org.cis.jproyinv.comun.modelo.TipoDocumentoIdentificacion;
import org.cis.jproyinv.comun.modelo.TipoGenero;
import org.cis.jproyinv.comun.negocio.EditarDocenteUCController;

public class EditarDocente extends javax.swing.JDialog {

    public EditarDocenteUCController docenteUCC = new EditarDocenteUCController();

    public boolean bandera = false;

    public EditarDocente(boolean bandera, Docente docente) {
        super(new javax.swing.JFrame(), true);
        initComponents();
        this.setLocationRelativeTo(this);
        this.dtcFechaNacimiento.setDate(new Date());
        this.bandera = bandera;
        if (bandera == true) {
            docenteUCC.setDocente(docente);
            reemplazarCampos(docenteUCC.getDocente());
        } else {
            txtDNI.setEditable(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        tabbedPaneRound1 = new org.edisoncor.gui.tabbedPane.TabbedPaneRound();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        tabbedPaneVertical1 = new org.edisoncor.gui.tabbedPane.TabbedPaneVertical();
        panelImage2 = new org.edisoncor.gui.panel.PanelImage();
        panelRound3 = new org.edisoncor.gui.panel.PanelRound();
        txtNombres = new org.edisoncor.gui.textField.TextFieldRound();
        cbxTipoDNI = new javax.swing.JComboBox();
        txtDNI = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbxGenero = new javax.swing.JComboBox();
        txtApellidos = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtEmail = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel20 = new javax.swing.JLabel();
        cbxEstadoCivil = new javax.swing.JComboBox();
        dtcFechaNacimiento = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        panelRound1 = new org.edisoncor.gui.panel.PanelRound();
        jLabel21 = new javax.swing.JLabel();
        txtTituloAcademico = new org.edisoncor.gui.textField.TextFieldRound();
        cbxEstadoLaboral = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        panelImage3 = new org.edisoncor.gui.panel.PanelImage();
        panelRound4 = new org.edisoncor.gui.panel.PanelRound();
        txtPais = new org.edisoncor.gui.textField.TextFieldRound();
        txtCiudad = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtProvincia = new org.edisoncor.gui.textField.TextFieldRound();
        txtCelular = new org.edisoncor.gui.textField.TextFieldRound();
        txtTelefono = new org.edisoncor.gui.textField.TextFieldRound();
        txtNumeroConstruccion = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        txtCalles = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel16 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jproyinv.JproyinvApp.class).getContext().getResourceMap(EditarDocente.class);
        setTitle(resourceMap.getString("Form.title"));
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(640, 578));
        setName("Form");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tabbedPaneRound1.setName("tabbedPaneRound1");
        getContentPane().add(tabbedPaneRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 493));
        btnGuardar.setText(resourceMap.getString("btnGuardar.text"));
        btnGuardar.setName("btnGuardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 143, -1));
        btnCancelar.setText(resourceMap.getString("btnCancelar.text"));
        btnCancelar.setName("btnCancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 500, 143, -1));
        tabbedPaneVertical1.setName("tabbedPaneVertical1");
        panelImage2.setIcon(resourceMap.getIcon("panelImage2.icon"));
        panelImage2.setName("panelImage2");
        panelRound3.setName("panelRound3");
        txtNombres.setName("txtNombres");
        txtNombres.setOpaque(true);
        cbxTipoDNI.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DOCUMENTO_NACIONAL_IDENTIFICACION", "PASAPORTE" }));
        cbxTipoDNI.setName("cbxTipoDNI");
        cbxTipoDNI.setOpaque(false);
        cbxTipoDNI.setRequestFocusEnabled(false);
        txtDNI.setName("txtDNI");
        txtDNI.setOpaque(true);
        jLabel4.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel4.setForeground(resourceMap.getColor("jLabel4.foreground"));
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        jLabel5.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel5.setForeground(resourceMap.getColor("jLabel5.foreground"));
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        jLabel6.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel6.setForeground(resourceMap.getColor("jLabel6.foreground"));
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        jLabel7.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel7.setForeground(resourceMap.getColor("jLabel7.foreground"));
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        cbxGenero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MASCULINO", "FEMENINO" }));
        cbxGenero.setName("cbxGenero");
        cbxGenero.setOpaque(false);
        cbxGenero.setRequestFocusEnabled(false);
        txtApellidos.setName("txtApellidos");
        txtApellidos.setOpaque(true);
        jLabel17.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel17.setForeground(resourceMap.getColor("jLabel17.foreground"));
        jLabel17.setText(resourceMap.getString("jLabel17.text"));
        jLabel17.setName("jLabel17");
        jLabel18.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel18.setForeground(resourceMap.getColor("jLabel18.foreground"));
        jLabel18.setText(resourceMap.getString("jLabel18.text"));
        jLabel18.setName("jLabel18");
        jLabel19.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel19.setForeground(resourceMap.getColor("jLabel19.foreground"));
        jLabel19.setText(resourceMap.getString("jLabel19.text"));
        jLabel19.setName("jLabel19");
        txtEmail.setName("txtEmail");
        txtEmail.setOpaque(true);
        jLabel20.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel20.setForeground(resourceMap.getColor("jLabel20.foreground"));
        jLabel20.setText(resourceMap.getString("jLabel20.text"));
        jLabel20.setName("jLabel20");
        cbxEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SOLTERO", "CASADO", "DIVORCIADO", "VIUDO", "UNION_LIBRE" }));
        cbxEstadoCivil.setName("cbxEstadoCivil");
        cbxEstadoCivil.setOpaque(false);
        cbxEstadoCivil.setRequestFocusEnabled(false);
        dtcFechaNacimiento.setName("dtcFechaNacimiento");
        dtcFechaNacimiento.setRequestFocusEnabled(false);
        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRound3Layout.createSequentialGroup().addContainerGap().addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel6).addComponent(jLabel5).addComponent(jLabel4).addComponent(jLabel7).addComponent(jLabel17).addComponent(jLabel20).addComponent(jLabel19).addComponent(jLabel18)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cbxEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(cbxTipoDNI, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE).addComponent(txtApellidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(dtcFechaNacimiento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(cbxGenero, javax.swing.GroupLayout.Alignment.LEADING, 0, 145, Short.MAX_VALUE).addComponent(txtDNI, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))).addGap(826, 826, 826)));
        panelRound3Layout.setVerticalGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRound3Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cbxTipoDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cbxGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel17)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel18).addComponent(dtcFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel19)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cbxEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel20)).addContainerGap(22, Short.MAX_VALUE)));
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        panelRound1.setName("panelRound1");
        jLabel21.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel21.setForeground(resourceMap.getColor("jLabel21.foreground"));
        jLabel21.setText(resourceMap.getString("jLabel21.text"));
        jLabel21.setName("jLabel21");
        txtTituloAcademico.setName("txtTituloAcademico");
        txtTituloAcademico.setOpaque(true);
        cbxEstadoLaboral.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NOMBRAMIENTO", "CONTRATO_TIEMPO_COMPLETO", "CONTRATO_MEDIO_TIEMPO" }));
        cbxEstadoLaboral.setName("cbxEstadoLaboral");
        cbxEstadoLaboral.setOpaque(false);
        cbxEstadoLaboral.setRequestFocusEnabled(false);
        jLabel22.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel22.setForeground(resourceMap.getColor("jLabel22.foreground"));
        jLabel22.setText(resourceMap.getString("jLabel22.text"));
        jLabel22.setName("jLabel22");
        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRound1Layout.createSequentialGroup().addGap(29, 29, 29).addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel22).addComponent(jLabel21)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(cbxEstadoLaboral, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtTituloAcademico, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)).addGap(64, 64, 64)));
        panelRound1Layout.setVerticalGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createSequentialGroup().addContainerGap(28, Short.MAX_VALUE).addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel21).addComponent(txtTituloAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cbxEstadoLaboral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel22)).addGap(26, 26, 26)));
        javax.swing.GroupLayout panelImage2Layout = new javax.swing.GroupLayout(panelImage2);
        panelImage2.setLayout(panelImage2Layout);
        panelImage2Layout.setHorizontalGroup(panelImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelImage2Layout.createSequentialGroup().addContainerGap().addGroup(panelImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1).addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(13, Short.MAX_VALUE)));
        panelImage2Layout.setVerticalGroup(panelImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelImage2Layout.createSequentialGroup().addContainerGap().addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(36, Short.MAX_VALUE)));
        tabbedPaneVertical1.addTab(resourceMap.getString("panelImage2.TabConstraints.tabTitle"), panelImage2);
        panelImage3.setIcon(resourceMap.getIcon("panelImage3.icon"));
        panelImage3.setName("panelImage3");
        panelRound4.setName("panelRound4");
        txtPais.setName("txtPais");
        txtPais.setOpaque(true);
        txtCiudad.setName("txtCiudad");
        txtCiudad.setOpaque(true);
        jLabel8.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel8.setForeground(resourceMap.getColor("jLabel8.foreground"));
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        jLabel9.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel9.setForeground(resourceMap.getColor("jLabel9.foreground"));
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        jLabel10.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel10.setForeground(resourceMap.getColor("jLabel10.foreground"));
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        txtProvincia.setName("txtProvincia");
        txtProvincia.setOpaque(true);
        txtCelular.setName("txtCelular");
        txtCelular.setOpaque(true);
        txtTelefono.setName("txtTelefono");
        txtTelefono.setOpaque(true);
        txtNumeroConstruccion.setText(resourceMap.getString("txtNumeroConstruccion.text"));
        txtNumeroConstruccion.setName("txtNumeroConstruccion");
        txtNumeroConstruccion.setOpaque(true);
        jLabel11.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel11.setForeground(resourceMap.getColor("jLabel11.foreground"));
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        jLabel13.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel13.setForeground(resourceMap.getColor("jLabel13.foreground"));
        jLabel13.setText(resourceMap.getString("jLabel13.text"));
        jLabel13.setName("jLabel13");
        jLabel14.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel14.setForeground(resourceMap.getColor("jLabel14.foreground"));
        jLabel14.setText(resourceMap.getString("jLabel14.text"));
        jLabel14.setName("jLabel14");
        jLabel15.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel15.setForeground(resourceMap.getColor("jLabel15.foreground"));
        jLabel15.setText(resourceMap.getString("jLabel15.text"));
        jLabel15.setName("jLabel15");
        jScrollPane2.setName("jScrollPane2");
        txtObservaciones.setColumns(20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setRows(5);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setName("txtObservaciones");
        jScrollPane2.setViewportView(txtObservaciones);
        txtCalles.setText(resourceMap.getString("txtCalles.text"));
        txtCalles.setName("txtCalles");
        txtCalles.setOpaque(true);
        txtCalles.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCallesActionPerformed(evt);
            }
        });
        jLabel16.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel16.setForeground(resourceMap.getColor("jLabel16.foreground"));
        jLabel16.setText(resourceMap.getString("jLabel16.text"));
        jLabel16.setName("jLabel16");
        javax.swing.GroupLayout panelRound4Layout = new javax.swing.GroupLayout(panelRound4);
        panelRound4.setLayout(panelRound4Layout);
        panelRound4Layout.setHorizontalGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRound4Layout.createSequentialGroup().addGap(28, 28, 28).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel8).addComponent(jLabel9).addComponent(jLabel13).addComponent(jLabel10).addComponent(jLabel14).addComponent(jLabel15).addComponent(jLabel11).addComponent(jLabel16)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(panelRound4Layout.createSequentialGroup().addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtProvincia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtCiudad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtPais, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE).addComponent(txtCalles, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(txtCelular, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtTelefono, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtNumeroConstruccion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(90, 90, 90)));
        panelRound4Layout.setVerticalGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRound4Layout.createSequentialGroup().addGap(26, 26, 26).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(txtPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(8, 8, 8).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9).addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(8, 8, 8).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(txtProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtCalles, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtNumeroConstruccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11)).addGap(8, 8, 8).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRound4Layout.createSequentialGroup().addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(8, 8, 8).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel14))).addComponent(jLabel13)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel15)).addContainerGap(32, Short.MAX_VALUE)));
        javax.swing.GroupLayout panelImage3Layout = new javax.swing.GroupLayout(panelImage3);
        panelImage3.setLayout(panelImage3Layout);
        panelImage3Layout.setHorizontalGroup(panelImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelImage3Layout.createSequentialGroup().addContainerGap(44, Short.MAX_VALUE).addComponent(panelRound4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        panelImage3Layout.setVerticalGroup(panelImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelImage3Layout.createSequentialGroup().addGap(44, 44, 44).addComponent(panelRound4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(46, Short.MAX_VALUE)));
        tabbedPaneVertical1.addTab(resourceMap.getString("panelImage3.TabConstraints.tabTitle"), panelImage3);
        getContentPane().add(tabbedPaneVertical1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 493));
        pack();
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        if (camposObligatoriosVacios() == false) {
            if (validarDNI() == true) {
                guardarDatos();
                if (bandera == false) {
                    if (docenteUCC.existe(docenteUCC.getDocente()) == false) {
                        docenteUCC.guardar(docenteUCC.getDocente());
                        this.dispose();
                        JOptionPane.showMessageDialog(this, "Docente guardado");
                        BuscarDocente buscar = new BuscarDocente(false);
                        buscar.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Docente ya existe");
                    }
                } else {
                    docenteUCC.actualizar(docenteUCC.getDocente());
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Docente guardado");
                    BuscarDocente buscar = new BuscarDocente(false);
                    buscar.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "DNI Incorrecto");
            }
        } else {
            this.setVisible(false);
            JOptionPane.showMessageDialog(this, "Campos Oblibatorios Vacíos");
            this.setVisible(true);
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        BuscarDocente buscar = new BuscarDocente(false);
        this.dispose();
        buscar.setVisible(true);
    }

    private void txtCallesActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JButton btnCancelar;

    private javax.swing.JButton btnGuardar;

    private javax.swing.JComboBox cbxEstadoCivil;

    private javax.swing.JComboBox cbxEstadoLaboral;

    private javax.swing.JComboBox cbxGenero;

    private javax.swing.JComboBox cbxTipoDNI;

    private com.toedter.calendar.JDateChooser dtcFechaNacimiento;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JScrollPane jScrollPane2;

    private org.edisoncor.gui.panel.PanelImage panelImage2;

    private org.edisoncor.gui.panel.PanelImage panelImage3;

    private org.edisoncor.gui.panel.PanelRound panelRound1;

    private org.edisoncor.gui.panel.PanelRound panelRound3;

    private org.edisoncor.gui.panel.PanelRound panelRound4;

    private org.edisoncor.gui.tabbedPane.TabbedPaneRound tabbedPaneRound1;

    private org.edisoncor.gui.tabbedPane.TabbedPaneVertical tabbedPaneVertical1;

    private org.edisoncor.gui.textField.TextFieldRound txtApellidos;

    private org.edisoncor.gui.textField.TextFieldRound txtCalles;

    private org.edisoncor.gui.textField.TextFieldRound txtCelular;

    private org.edisoncor.gui.textField.TextFieldRound txtCiudad;

    private org.edisoncor.gui.textField.TextFieldRound txtDNI;

    private org.edisoncor.gui.textField.TextFieldRound txtEmail;

    private org.edisoncor.gui.textField.TextFieldRound txtNombres;

    private org.edisoncor.gui.textField.TextFieldRound txtNumeroConstruccion;

    private javax.swing.JTextArea txtObservaciones;

    private org.edisoncor.gui.textField.TextFieldRound txtPais;

    private org.edisoncor.gui.textField.TextFieldRound txtProvincia;

    private org.edisoncor.gui.textField.TextFieldRound txtTelefono;

    private org.edisoncor.gui.textField.TextFieldRound txtTituloAcademico;

    private void reemplazarCampos(Docente docente) {
        txtNombres.setText(docente.getNombres());
        txtApellidos.setText(docente.getApellidos());
        cbxTipoDNI.setSelectedItem(docente.getM_TipoDocumentoIdentificacion().toString());
        txtDNI.setText(docente.getNumeroDocumentoIdentificacion());
        cbxGenero.setSelectedItem(docente.getM_TipoGenero().toString());
        dtcFechaNacimiento.setDate(docente.getFechaNacimiento());
        txtEmail.setText(docente.getE_mail());
        cbxEstadoCivil.setSelectedItem(docente.getM_EstadoCivil().toString());
        txtTituloAcademico.setText(docente.getTituloAcademico());
        cbxEstadoLaboral.setSelectedItem(docente.getM_EstadoLaboral().toString());
        txtPais.setText(docente.getDireccion().getPais());
        txtCiudad.setText(docente.getDireccion().getCiudad());
        txtProvincia.setText(docente.getDireccion().getProvincia());
        txtNumeroConstruccion.setText(docente.getDireccion().getNumeroConstruccion() + "");
        txtTelefono.setText(docente.getDireccion().getTelefono());
        txtCelular.setText(docente.getDireccion().getCelular());
        txtObservaciones.setText(docente.getDireccion().getObservacion());
        txtCalles.setText(docente.getDireccion().getCalles());
        txtDNI.setEditable(false);
    }

    private void guardarDatos() {
        docenteUCC.getDocente().setNombres(txtNombres.getText().toUpperCase());
        docenteUCC.getDocente().setApellidos(txtApellidos.getText().toUpperCase());
        docenteUCC.getDocente().setM_TipoDocumentoIdentificacion(TipoDocumentoIdentificacion.valueOf(cbxTipoDNI.getSelectedItem().toString()));
        docenteUCC.getDocente().setNumeroDocumentoIdentificacion(txtDNI.getText());
        docenteUCC.getDocente().setM_TipoGenero(TipoGenero.valueOf(cbxGenero.getSelectedItem().toString()));
        docenteUCC.getDocente().setFechaNacimiento(dtcFechaNacimiento.getDate());
        docenteUCC.getDocente().setE_mail(txtEmail.getText().toUpperCase());
        docenteUCC.getDocente().setM_EstadoCivil(EstadoCivil.valueOf(cbxEstadoCivil.getSelectedItem().toString()));
        docenteUCC.getDocente().setTituloAcademico(txtTituloAcademico.getText().toUpperCase());
        docenteUCC.getDocente().setM_EstadoLaboral(EstadoLaboral.valueOf(cbxEstadoLaboral.getSelectedItem().toString()));
        if (bandera == false) {
            docenteUCC.getDocente().setDireccion(new Direccion());
        }
        docenteUCC.getDocente().getDireccion().setPais(txtPais.getText().toUpperCase());
        docenteUCC.getDocente().getDireccion().setCiudad(txtCiudad.getText().toUpperCase());
        docenteUCC.getDocente().getDireccion().setProvincia(txtProvincia.getText().toUpperCase());
        docenteUCC.getDocente().getDireccion().setNumeroConstruccion(Integer.parseInt(txtNumeroConstruccion.getText()));
        docenteUCC.getDocente().getDireccion().setTelefono(txtTelefono.getText());
        docenteUCC.getDocente().getDireccion().setCelular(txtCelular.getText());
        docenteUCC.getDocente().getDireccion().setObservacion(txtObservaciones.getText().toUpperCase());
        docenteUCC.getDocente().getDireccion().setCalles(txtCalles.getText().toUpperCase());
    }

    private boolean camposObligatoriosVacios() {
        boolean campos = false;
        try {
            Integer.parseInt(txtNumeroConstruccion.getText());
            if (txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtDNI.getText().isEmpty() || txtTituloAcademico.getText().isEmpty()) {
                campos = true;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El número de casa debe tener el siguiente formato: 0516");
            campos = true;
        } catch (StringIndexOutOfBoundsException e) {
            campos = true;
        }
        return campos;
    }

    private boolean validarDNI() {
        boolean validar = true;
        if (cbxTipoDNI.getSelectedIndex() == 0) {
            docenteUCC.getDocente().setNumeroDocumentoIdentificacion(txtDNI.getText());
            if (docenteUCC.getDocente().validarDNI() == false) {
                validar = false;
            }
        }
        return validar;
    }

    public EditarDocenteUCController getDocenteUCC() {
        return docenteUCC;
    }

    public void setDocenteUCC(EditarDocenteUCController docenteUCC) {
        this.docenteUCC = docenteUCC;
    }

    public boolean isBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }
}
