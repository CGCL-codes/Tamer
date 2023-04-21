package org.cis.jproyinv.proyecto.vista;

import java.util.ArrayList;
import java.util.List;
import org.cis.jproyinv.proyecto.modelo.ProyectoInvestigacion;
import org.cis.jproyinv.proyecto.negocio.AdministrarProyectoInvestigacionUCController;
import org.jdesktop.observablecollections.ObservableList;

public class ConsultarProyectoInvestigacion extends javax.swing.JDialog {

    public AdministrarProyectoInvestigacionUCController proyectoUCC = new AdministrarProyectoInvestigacionUCController();

    public ConsultarProyectoInvestigacion() {
        super(new javax.swing.JFrame(), true);
        initComponents();
        this.setLocationRelativeTo(this);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        panelImage1 = new org.edisoncor.gui.panel.PanelImage();
        panelRoundTranslucidoComplete1 = new org.edisoncor.gui.panel.PanelRoundTranslucidoComplete();
        txtCriterio = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        panelRect1 = new org.edisoncor.gui.panel.PanelRect();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResultadoBusqueda = new javax.swing.JTable();
        btnDetalle = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jproyinv.JproyinvApp.class).getContext().getResourceMap(ConsultarProyectoInvestigacion.class);
        setTitle(resourceMap.getString("Form.title"));
        setAlwaysOnTop(true);
        setName("Form");
        setResizable(false);
        panelImage1.setToolTipText(resourceMap.getString("panelImage1.toolTipText"));
        panelImage1.setIcon(resourceMap.getIcon("panelImage1.icon"));
        panelImage1.setName("panelImage1");
        panelRoundTranslucidoComplete1.setName("panelRoundTranslucidoComplete1");
        txtCriterio.setName("txtCriterio");
        txtCriterio.setOpaque(true);
        jLabel3.setFont(resourceMap.getFont("jLabel3.font"));
        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground"));
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        javax.swing.GroupLayout panelRoundTranslucidoComplete1Layout = new javax.swing.GroupLayout(panelRoundTranslucidoComplete1);
        panelRoundTranslucidoComplete1.setLayout(panelRoundTranslucidoComplete1Layout);
        panelRoundTranslucidoComplete1Layout.setHorizontalGroup(panelRoundTranslucidoComplete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRoundTranslucidoComplete1Layout.createSequentialGroup().addGap(30, 30, 30).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(39, Short.MAX_VALUE)));
        panelRoundTranslucidoComplete1Layout.setVerticalGroup(panelRoundTranslucidoComplete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRoundTranslucidoComplete1Layout.createSequentialGroup().addGap(26, 26, 26).addGroup(panelRoundTranslucidoComplete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(txtCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(30, Short.MAX_VALUE)));
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        btnBuscar.setText(resourceMap.getString("btnBuscar.text"));
        btnBuscar.setName("btnBuscar");
        btnBuscar.setOpaque(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        btnLimpiar.setText(resourceMap.getString("btnLimpiar.text"));
        btnLimpiar.setName("btnLimpiar");
        btnLimpiar.setOpaque(false);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        panelRect1.setName("panelRect1");
        jScrollPane1.setName("jScrollPane1");
        tblResultadoBusqueda.setName("tblResultadoBusqueda");
        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listObservable}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tblResultadoBusqueda);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${tema}"));
        columnBinding.setColumnName("Tema");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${tags}"));
        columnBinding.setColumnName("Tags");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fechaCulminacion}"));
        columnBinding.setColumnName("Fecha Culminacion");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane1.setViewportView(tblResultadoBusqueda);
        tblResultadoBusqueda.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tblResultadoBusqueda.columnModel.title0"));
        tblResultadoBusqueda.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tblResultadoBusqueda.columnModel.title1"));
        tblResultadoBusqueda.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tblResultadoBusqueda.columnModel.title2"));
        btnDetalle.setText(resourceMap.getString("btnDetalle.text"));
        btnDetalle.setName("btnDetalle");
        btnDetalle.setOpaque(false);
        btnDetalle.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalleActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelRect1Layout = new javax.swing.GroupLayout(panelRect1);
        panelRect1.setLayout(panelRect1Layout);
        panelRect1Layout.setHorizontalGroup(panelRect1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRect1Layout.createSequentialGroup().addGap(24, 24, 24).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(btnDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(27, 27, 27)));
        panelRect1Layout.setVerticalGroup(panelRect1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRect1Layout.createSequentialGroup().addGap(26, 26, 26).addGroup(panelRect1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnDetalle)).addContainerGap(30, Short.MAX_VALUE)));
        btnCerrar.setText(resourceMap.getString("btnCerrar.text"));
        btnCerrar.setName("btnCerrar");
        btnCerrar.setOpaque(false);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelImage1);
        panelImage1.setLayout(panelImage1Layout);
        panelImage1Layout.setHorizontalGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelImage1Layout.createSequentialGroup().addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelImage1Layout.createSequentialGroup().addContainerGap().addComponent(panelRect1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(panelImage1Layout.createSequentialGroup().addGap(20, 20, 20).addComponent(jLabel1)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelImage1Layout.createSequentialGroup().addContainerGap().addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(panelImage1Layout.createSequentialGroup().addGap(21, 21, 21).addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelRoundTranslucidoComplete1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel2))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelImage1Layout.createSequentialGroup().addContainerGap(627, Short.MAX_VALUE).addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelImage1Layout.setVerticalGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelImage1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelRoundTranslucidoComplete1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(1, 1, 1).addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnBuscar).addComponent(btnLimpiar)).addGap(18, 18, 18).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelRect1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCerrar).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelImage1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        bindingGroup.bind();
        pack();
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        listObservable.clear();
        try {
            listObservable.addAll(proyectoUCC.buscar(txtCriterio.getText().toUpperCase()));
        } catch (IllegalArgumentException e) {
        }
        tblResultadoBusqueda.repaint();
    }

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        txtCriterio.setText("");
        list.clear();
        listObservable.clear();
        tblResultadoBusqueda.repaint();
    }

    private void btnDetalleActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            proyectoUCC.setProyecto(proyectoUCC.editar(proyectoUCC.buscar(tblResultadoBusqueda.getValueAt(tblResultadoBusqueda.getSelectedRow(), 0) + "").get(0).getId()));
            DetalleProyectoInvestigacion detalle = new DetalleProyectoInvestigacion(proyectoUCC.getProyecto());
            this.setVisible(false);
            detalle.setVisible(true);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private javax.swing.JButton btnBuscar;

    private javax.swing.JButton btnCerrar;

    private javax.swing.JButton btnDetalle;

    private javax.swing.JButton btnLimpiar;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPane1;

    private org.edisoncor.gui.panel.PanelImage panelImage1;

    private org.edisoncor.gui.panel.PanelRect panelRect1;

    private org.edisoncor.gui.panel.PanelRoundTranslucidoComplete panelRoundTranslucidoComplete1;

    private javax.swing.JTable tblResultadoBusqueda;

    private org.edisoncor.gui.textField.TextFieldRound txtCriterio;

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    private List<ProyectoInvestigacion> list = new ArrayList();

    private ObservableList<ProyectoInvestigacion> listObservable = org.jdesktop.observablecollections.ObservableCollections.observableList(list);

    public List<ProyectoInvestigacion> getList() {
        return list;
    }

    public void setList(List<ProyectoInvestigacion> list) {
        this.list = list;
    }

    public ObservableList<ProyectoInvestigacion> getListObservable() {
        return listObservable;
    }

    public void setListObservable(ObservableList<ProyectoInvestigacion> listObservable) {
        this.listObservable = listObservable;
    }
}
