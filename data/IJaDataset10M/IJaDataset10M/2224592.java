package openfield.explotacion;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.RollbackException;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import openfield.persistence.business.RollBackExceptionParser;
import openfield.persistence.business.BusinessFactory;
import openfield.persistence.business.Explotacion;
import openfield.persistence.entities.explotacion.CosteFijoInstalacion;
import openfield.persistence.entities.explotacion.Instalacion;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//openfield.explotacion//Instalaciones//EN", autostore = false)
public final class InstalacionesTopComponent extends TopComponent {

    private static InstalacionesTopComponent instance;

    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "openfield/explotacion/resources/icons/pipe16.png";

    private static final String PREFERRED_ID = "InstalacionesTopComponent";

    private List<Instalacion> instalaciones = new LinkedList<Instalacion>();

    public static final String PROP_INSTALACIONES = "instalaciones";

    private List<CosteFijoInstalacion> costesFijos = new LinkedList<CosteFijoInstalacion>();

    public static final String PROP_COSTESFIJOS = "costesFijos";

    private InstalacionesPanel panel = null;

    private CosteFijoInstalacionesPanel cpanel = null;

    private DialogDescriptor dd = null;

    private BusinessFactory bf;

    private Explotacion ex;

    private TableRowSorter sorter, sorterCf;

    private final TableModel model, modelCf;

    public InstalacionesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(InstalacionesTopComponent.class, "CTL_InstalacionesTopComponent"));
        setToolTipText(NbBundle.getMessage(InstalacionesTopComponent.class, "HINT_InstalacionesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        panelFiltro.setVisible(false);
        bf = new BusinessFactory();
        ex = bf.getExplotacion();
        setInstalaciones(ex.getInstalaciones());
        setCostesFijos(new LinkedList<CosteFijoInstalacion>());
        model = tablaInstalaciones.getModel();
        modelCf = tablaCostesFijos.getModel();
        sorter = new TableRowSorter(model);
        sorterCf = new TableRowSorter(modelCf);
        tablaInstalaciones.setRowSorter(sorter);
        tablaCostesFijos.setRowSorter(sorterCf);
        tablaInstalaciones.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (tablaInstalaciones.getSelectedRowCount() > 0) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    newCosteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    newCosteButton.setEnabled(false);
                }
                refreshCostesFijos();
            }
        });
        tablaCostesFijos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (tablaCostesFijos.getSelectedRowCount() > 0) {
                    editCosteButton.setEnabled(true);
                    deleteCosteButton.setEnabled(true);
                } else {
                    editCosteButton.setEnabled(false);
                    deleteCosteButton.setEnabled(false);
                }
            }
        });
        filterText.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }

            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }
        });
        filterTextCf.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                newFilterCf();
            }

            public void insertUpdate(DocumentEvent e) {
                newFilterCf();
            }

            public void removeUpdate(DocumentEvent e) {
                newFilterCf();
            }
        });
    }

    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaInstalaciones = new javax.swing.JTable();
        panelDetalles = new javax.swing.JTabbedPane();
        panelCostesFijos = new javax.swing.JPanel();
        newCosteButton = new javax.swing.JButton();
        editCosteButton = new javax.swing.JButton();
        deleteCosteButton = new javax.swing.JButton();
        filterTextCf = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCostesFijos = new javax.swing.JTable();
        filterCleanCfButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        filterTButton = new javax.swing.JToggleButton();
        panelFiltro = new javax.swing.JPanel();
        filterText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        filterCleanButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        tablaInstalaciones.setFillsViewportHeight(true);
        tablaInstalaciones.setName("tablaInstalaciones");
        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${instalaciones}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tablaInstalaciones);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${descripcion}"));
        columnBinding.setColumnName("Descripcion");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ud}"));
        columnBinding.setColumnName("Ud");
        columnBinding.setColumnClass(Double.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${valorCompra}"));
        columnBinding.setColumnName("Valor Compra");
        columnBinding.setColumnClass(Double.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fecAlta}"));
        columnBinding.setColumnName("Fec Alta");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${valorResidual}"));
        columnBinding.setColumnName("Valor Residual");
        columnBinding.setColumnClass(Double.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fecBaja}"));
        columnBinding.setColumnName("Fec Baja");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane1.setViewportView(tablaInstalaciones);
        tablaInstalaciones.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaInstalaciones.columnModel.title0"));
        tablaInstalaciones.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaInstalaciones.columnModel.title6"));
        tablaInstalaciones.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaInstalaciones.columnModel.title3_1"));
        tablaInstalaciones.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaInstalaciones.columnModel.title7"));
        tablaInstalaciones.getColumnModel().getColumn(4).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaInstalaciones.columnModel.title4"));
        tablaInstalaciones.getColumnModel().getColumn(5).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaInstalaciones.columnModel.title1"));
        newCosteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/coins-add.png")));
        org.openide.awt.Mnemonics.setLocalizedText(newCosteButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.newCosteButton.text"));
        newCosteButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.newCosteButton.toolTipText"));
        newCosteButton.setEnabled(false);
        newCosteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCosteButtonActionPerformed(evt);
            }
        });
        editCosteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/coins.png")));
        org.openide.awt.Mnemonics.setLocalizedText(editCosteButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.editCosteButton.text"));
        editCosteButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.editCosteButton.toolTipText"));
        editCosteButton.setEnabled(false);
        editCosteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCosteButtonActionPerformed(evt);
            }
        });
        deleteCosteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/coins-delete.png")));
        org.openide.awt.Mnemonics.setLocalizedText(deleteCosteButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.deleteCosteButton.text"));
        deleteCosteButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.deleteCosteButton.toolTipText"));
        deleteCosteButton.setEnabled(false);
        deleteCosteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCosteButtonActionPerformed(evt);
            }
        });
        filterTextCf.setText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterTextCf.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.jLabel2.text"));
        tablaCostesFijos.setName("tablaCostesFijos");
        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${costesFijos}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tablaCostesFijos);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fecha}"));
        columnBinding.setColumnName("Fecha");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${descripcion}"));
        columnBinding.setColumnName("Descripcion");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${tipoCosteFijo.nombre}"));
        columnBinding.setColumnName("Tipo Coste Fijo.nombre");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${cantidad}"));
        columnBinding.setColumnName("Cantidad");
        columnBinding.setColumnClass(Double.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${NFactura}"));
        columnBinding.setColumnName("NFactura");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${notas}"));
        columnBinding.setColumnName("Notas");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane3.setViewportView(tablaCostesFijos);
        tablaCostesFijos.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaCostesFijos.columnModel.title4_1"));
        tablaCostesFijos.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaCostesFijos.columnModel.title0"));
        tablaCostesFijos.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaCostesFijos.columnModel.title5"));
        tablaCostesFijos.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaCostesFijos.columnModel.title2"));
        tablaCostesFijos.getColumnModel().getColumn(4).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaCostesFijos.columnModel.title3"));
        tablaCostesFijos.getColumnModel().getColumn(5).setHeaderValue(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.tablaCostesFijos.columnModel.title4"));
        filterCleanCfButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/clean16.png")));
        org.openide.awt.Mnemonics.setLocalizedText(filterCleanCfButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterCleanCfButton.text"));
        filterCleanCfButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterCleanCfButton.toolTipText"));
        filterCleanCfButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterCleanCfButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout panelCostesFijosLayout = new org.jdesktop.layout.GroupLayout(panelCostesFijos);
        panelCostesFijos.setLayout(panelCostesFijosLayout);
        panelCostesFijosLayout.setHorizontalGroup(panelCostesFijosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelCostesFijosLayout.createSequentialGroup().addContainerGap().add(newCosteButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(editCosteButton).add(18, 18, 18).add(deleteCosteButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(filterTextCf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(filterCleanCfButton).addContainerGap()).add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE));
        panelCostesFijosLayout.setVerticalGroup(panelCostesFijosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelCostesFijosLayout.createSequentialGroup().addContainerGap().add(panelCostesFijosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(editCosteButton).add(newCosteButton).add(deleteCosteButton).add(panelCostesFijosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(filterTextCf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2)).add(filterCleanCfButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)));
        panelDetalles.addTab(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.panelCostesFijos.TabConstraints.tabTitle"), panelCostesFijos);
        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/add.png")));
        org.openide.awt.Mnemonics.setLocalizedText(newButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.newButton.text"));
        newButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.newButton.toolTipText"));
        newButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/edit.png")));
        org.openide.awt.Mnemonics.setLocalizedText(editButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.editButton.text"));
        editButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.editButton.toolTipText"));
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/refresh.png")));
        org.openide.awt.Mnemonics.setLocalizedText(refreshButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.refreshButton.text"));
        refreshButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.refreshButton.toolTipText"));
        refreshButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/delete.png")));
        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.deleteButton.text"));
        deleteButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.deleteButton.toolTipText"));
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        filterTButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/view-filter.png")));
        org.openide.awt.Mnemonics.setLocalizedText(filterTButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterTButton.text"));
        filterTButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterTButton.toolTipText"));
        filterTButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterTButtonActionPerformed(evt);
            }
        });
        panelFiltro.setOpaque(false);
        filterText.setText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterText.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.jLabel1.text"));
        filterCleanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/clean16.png")));
        org.openide.awt.Mnemonics.setLocalizedText(filterCleanButton, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterCleanButton.text"));
        filterCleanButton.setToolTipText(org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.filterCleanButton.toolTipText"));
        filterCleanButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterCleanButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout panelFiltroLayout = new org.jdesktop.layout.GroupLayout(panelFiltro);
        panelFiltro.setLayout(panelFiltroLayout);
        panelFiltroLayout.setHorizontalGroup(panelFiltroLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelFiltroLayout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(filterText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 245, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(filterCleanButton).addContainerGap(86, Short.MAX_VALUE)));
        panelFiltroLayout.setVerticalGroup(panelFiltroLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelFiltroLayout.createSequentialGroup().addContainerGap().add(panelFiltroLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(filterCleanButton).add(panelFiltroLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(filterText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap(20, Short.MAX_VALUE)));
        jPanel2.setOpaque(false);
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tablaInstalaciones, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.notas}"), jTextArea1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        jScrollPane2.setViewportView(jTextArea1);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfield/explotacion/resources/icons/view-pim-notes.png")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(InstalacionesTopComponent.class, "InstalacionesTopComponent.jLabel3.text"));
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(20, 20, 20).add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(newButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(editButton).add(18, 18, 18).add(refreshButton).add(18, 18, 18).add(deleteButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 152, Short.MAX_VALUE).add(filterTButton).addContainerGap()).add(panelFiltro, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(panelDetalles, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(newButton).add(editButton).add(refreshButton).add(deleteButton).add(filterTButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelFiltro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(panelDetalles, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 245, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        bindingGroup.bind();
    }

    private void newCosteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (tablaInstalaciones.getSelectedRow() == -1) {
            return;
        }
        CosteFijoInstalacion cfi = new CosteFijoInstalacion();
        cpanel = new CosteFijoInstalacionesPanel();
        dd = new DialogDescriptor(cpanel, "Añadir Coste Fijo", true, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == DialogDescriptor.OK_OPTION) {
                    Date fechaCoste = cpanel.getCosteFijoInstalacion().getFecha();
                    if ((ex.getEjerciciosByFecha(fechaCoste).size() > 0) || (fechaCoste == null)) {
                        try {
                            ex.addCosteFijoInstalacion(cpanel.getCosteFijoInstalacion());
                        } catch (RollbackException rex) {
                            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(RollBackExceptionParser.getParsedMessage(rex)));
                            return;
                        }
                        refreshCostesFijos();
                        dd.setClosingOptions(null);
                    } else {
                        SimpleDateFormat fechaSimple = new SimpleDateFormat("dd/MM/yyyy");
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("No se ha encontrado ningún Ejercicio disponible para la fecha " + fechaSimple.format(fechaCoste) + "\nSi desea asignar un coste a esta fecha debe crear el Ejercicio primero."));
                    }
                }
            }
        });
        dd.setClosingOptions(new Object[] { DialogDescriptor.CANCEL_OPTION, DialogDescriptor.CLOSED_OPTION });
        cfi.setInstalacion(getInstalaciones().get(tablaInstalaciones.convertRowIndexToModel(tablaInstalaciones.getSelectedRow())));
        cpanel.setCosteFijoInstalacion(cfi);
        Dialog editDialog = DialogDisplayer.getDefault().createDialog(dd);
        editDialog.setResizable(false);
        editDialog.setVisible(true);
    }

    private void editCosteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (tablaCostesFijos.getSelectedRow() == -1) {
            return;
        }
        CosteFijoInstalacion cfi = getCostesFijos().get(tablaCostesFijos.convertRowIndexToModel(tablaCostesFijos.getSelectedRow()));
        cpanel = new CosteFijoInstalacionesPanel();
        dd = new DialogDescriptor(cpanel, "Editar Coste Fijo", true, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == DialogDescriptor.OK_OPTION) {
                    Date fechaCoste = cpanel.getCosteFijoInstalacion().getFecha();
                    if ((ex.getEjerciciosByFecha(fechaCoste).size() > 0) || (fechaCoste == null)) {
                        try {
                            ex.updateCosteFijoInstalacion(cpanel.getCosteFijoInstalacion());
                        } catch (RollbackException rex) {
                            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(RollBackExceptionParser.getParsedMessage(rex)));
                            return;
                        }
                        refreshCostesFijos();
                        dd.setClosingOptions(null);
                    } else {
                        SimpleDateFormat fechaSimple = new SimpleDateFormat("dd/MM/yyyy");
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("No se ha encontrado ningún Ejercicio disponible para la fecha " + fechaSimple.format(fechaCoste) + "\nSi desea asignar un coste a esta fecha debe crear el Ejercicio primero."));
                    }
                }
            }
        });
        dd.setClosingOptions(new Object[] { DialogDescriptor.CANCEL_OPTION, DialogDescriptor.CLOSED_OPTION });
        cpanel.setCosteFijoInstalacion(costesFijos.get(tablaCostesFijos.getSelectedRow()));
        Dialog editDialog = DialogDisplayer.getDefault().createDialog(dd);
        editDialog.setResizable(false);
        editDialog.setVisible(true);
    }

    private void deleteCosteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (tablaCostesFijos.getSelectedRow() == -1) return;
        int[] selected = tablaCostesFijos.getSelectedRows();
        String msg = (selected.length > 1) ? selected.length + " filas seran borradas." : getCostesFijos().get(tablaCostesFijos.convertRowIndexToModel(tablaCostesFijos.getSelectedRow())).getDescripcion() + " será borrado.";
        NotifyDescriptor.Confirmation conf = new NotifyDescriptor.Confirmation(msg + " ¿Está seguro de continuar?", "Confirmación", NotifyDescriptor.OK_CANCEL_OPTION);
        DialogDisplayer.getDefault().notify(conf);
        if (conf.getValue() != NotifyDescriptor.OK_OPTION) return;
        tablaCostesFijos.getSelectionModel().clearSelection();
        List<CosteFijoInstalacion> toRemove = new ArrayList<CosteFijoInstalacion>(selected.length);
        for (int idx = 0; idx < selected.length; idx++) {
            CosteFijoInstalacion c = costesFijos.get(tablaCostesFijos.convertRowIndexToModel(selected[idx]));
            toRemove.add(c);
            try {
                ex.removeCosteFijoInstalacion(c);
            } catch (RollbackException rex) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(RollBackExceptionParser.getParsedMessage(rex)));
                return;
            }
        }
        refreshCostesFijos();
    }

    private void filterCleanCfButtonActionPerformed(java.awt.event.ActionEvent evt) {
        filterTextCf.setText("");
    }

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {
        panel = new InstalacionesPanel();
        dd = new DialogDescriptor(panel, "Nueva Instalación", true, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == DialogDescriptor.OK_OPTION) {
                    try {
                        ex.addInstalacion(panel.getInstalacion());
                    } catch (RollbackException rex) {
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(RollBackExceptionParser.getParsedMessage(rex)));
                        return;
                    }
                    refreshInstalaciones();
                    dd.setClosingOptions(null);
                }
            }
        });
        dd.setClosingOptions(new Object[] { DialogDescriptor.CANCEL_OPTION, DialogDescriptor.CLOSED_OPTION });
        panel.setInstalacion(new Instalacion());
        Dialog editDialog = DialogDisplayer.getDefault().createDialog(dd);
        editDialog.setResizable(false);
        editDialog.setVisible(true);
    }

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (tablaInstalaciones.getSelectedRow() == -1) return;
        panel = new InstalacionesPanel();
        dd = new DialogDescriptor(panel, "Editar Instalación", true, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == DialogDescriptor.OK_OPTION) {
                    try {
                        ex.updateInstalacion(panel.getInstalacion());
                    } catch (RollbackException rex) {
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(RollBackExceptionParser.getParsedMessage(rex)));
                        return;
                    }
                    refreshInstalaciones();
                    dd.setClosingOptions(null);
                }
            }
        });
        dd.setClosingOptions(new Object[] { DialogDescriptor.CANCEL_OPTION, DialogDescriptor.CLOSED_OPTION });
        panel.setInstalacion(getInstalaciones().get(tablaInstalaciones.convertRowIndexToModel(tablaInstalaciones.getSelectedRow())));
        Dialog editDialog = DialogDisplayer.getDefault().createDialog(dd);
        editDialog.setResizable(false);
        editDialog.setVisible(true);
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        refreshInstalaciones();
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (tablaInstalaciones.getSelectedRow() == -1) return;
        int[] selected = tablaInstalaciones.getSelectedRows();
        String msg = (selected.length > 1) ? selected.length + " filas seran borradas." : getInstalaciones().get(tablaInstalaciones.convertRowIndexToModel(tablaInstalaciones.getSelectedRow())).getDescripcion() + " será borrado.";
        NotifyDescriptor.Confirmation conf = new NotifyDescriptor.Confirmation(msg + " ¿Está seguro de continuar?", "Confirmación", NotifyDescriptor.OK_CANCEL_OPTION);
        DialogDisplayer.getDefault().notify(conf);
        if (conf.getValue() != NotifyDescriptor.OK_OPTION) return;
        tablaInstalaciones.getSelectionModel().clearSelection();
        List<Instalacion> toRemove = new ArrayList<Instalacion>(selected.length);
        for (int idx = 0; idx < selected.length; idx++) {
            Instalacion e = getInstalaciones().get(tablaInstalaciones.convertRowIndexToModel(selected[idx]));
            toRemove.add(e);
            try {
                ex.removeInstalacion(e);
            } catch (RollbackException rex) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(RollBackExceptionParser.getParsedMessage(rex)));
                return;
            }
        }
        refreshInstalaciones();
    }

    private void filterTButtonActionPerformed(java.awt.event.ActionEvent evt) {
        panelFiltro.setVisible(filterTButton.isSelected());
    }

    private void filterCleanButtonActionPerformed(java.awt.event.ActionEvent evt) {
        filterText.setText("");
    }

    private javax.swing.JButton deleteButton;

    private javax.swing.JButton deleteCosteButton;

    private javax.swing.JButton editButton;

    private javax.swing.JButton editCosteButton;

    private javax.swing.JButton filterCleanButton;

    private javax.swing.JButton filterCleanCfButton;

    private javax.swing.JToggleButton filterTButton;

    private javax.swing.JTextField filterText;

    private javax.swing.JTextField filterTextCf;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JButton newButton;

    private javax.swing.JButton newCosteButton;

    private javax.swing.JPanel panelCostesFijos;

    private javax.swing.JTabbedPane panelDetalles;

    private javax.swing.JPanel panelFiltro;

    private javax.swing.JButton refreshButton;

    private javax.swing.JTable tablaCostesFijos;

    private javax.swing.JTable tablaInstalaciones;

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized InstalacionesTopComponent getDefault() {
        if (instance == null) {
            instance = new InstalacionesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the InstalacionesTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized InstalacionesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(InstalacionesTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof InstalacionesTopComponent) {
            return (InstalacionesTopComponent) win;
        }
        Logger.getLogger(InstalacionesTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /**
     * Get the value of instalaciones
     *
     * @return the value of instalaciones
     */
    public List<Instalacion> getInstalaciones() {
        return instalaciones;
    }

    /**
     * Set the value of instalaciones
     *
     * @param instalaciones new value of instalaciones
     */
    public void setInstalaciones(List<Instalacion> instalaciones) {
        List<Instalacion> oldInstalaciones = this.instalaciones;
        this.instalaciones = instalaciones;
        propertyChangeSupport.firePropertyChange(PROP_INSTALACIONES, oldInstalaciones, instalaciones);
    }

    /**
     * Get the value of costesFijos
     *
     * @return the value of costesFijos
     */
    public List<CosteFijoInstalacion> getCostesFijos() {
        return costesFijos;
    }

    /**
     * Set the value of costesFijos
     *
     * @param costesFijos new value of costesFijos
     */
    public void setCostesFijos(List<CosteFijoInstalacion> costesFijos) {
        List<CosteFijoInstalacion> oldCostesFijos = this.costesFijos;
        this.costesFijos = costesFijos;
        propertyChangeSupport.firePropertyChange(PROP_COSTESFIJOS, oldCostesFijos, costesFijos);
    }

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Actualiza la lista edificaciones y los componenetes bindeados.
     */
    private void refreshInstalaciones() {
        setInstalaciones(new LinkedList<Instalacion>());
        if (ex.getInstalaciones().size() > 0) setInstalaciones(ex.getInstalaciones());
    }

    private void refreshCostesFijos() {
        int selectedRows[] = tablaInstalaciones.getSelectedRows();
        int rows = ex.getCostesFijosInstalaciones().size();
        setCostesFijos(new LinkedList<CosteFijoInstalacion>());
        if (selectedRows.length > 0 && rows > 0) {
            Instalacion instalacion = instalaciones.get(tablaInstalaciones.convertRowIndexToModel(selectedRows[0]));
            setCostesFijos(ex.getCostesFijosInstalaciones(instalacion));
        }
    }

    private void newFilter() {
        RowFilter<TableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filterText.getText());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void newFilterCf() {
        RowFilter<TableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filterTextCf.getText());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorterCf.setRowFilter(rf);
    }
}
