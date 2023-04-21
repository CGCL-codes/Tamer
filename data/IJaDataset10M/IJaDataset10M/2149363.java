package frontend.facturacion;

import eventos.IItbisListener;
import eventos.ItbisEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import mensaje_objetos.facturacion.Itbis;
import modelo_objetos.facturacion.ModeloItbis;
import utilidades.IObserver;
import utilidades.Screen;
import utilidades.SubjectHelper;

/**
 *
 * @author  carlos
 */
public class DgAdministrarItbis extends javax.swing.JDialog {

    private List<Itbis> itbisTable = new ArrayList();

    private List<Itbis> listaItbisTotal = new ArrayList();

    private ModeloItbis modelo;

    private Itbis itbisSeleccionado;

    private SubjectHelper subAdd;

    private SubjectHelper subRemove;

    private SubjectHelper subUpdate;

    private SubjectHelper subMensaje;

    private String[] headers = { "Codigo", "Descripcion", "Equivalencia" };

    private AbstractTableModel tableModel;

    private DefaultTableCellRenderer tableRender;

    private IItbisListener listener;

    private DgCrearItbis dgCrearItbis;

    /** Creates new form DgItbis */
    public DgAdministrarItbis() {
        super(new JFrame(), true);
        initComponents();
        setTitle("Administrador de Itbis");
        Screen sc = new Screen(this);
        sc.centrarPantalla();
        inicializarComponentes();
        addListener();
    }

    public void addItbisListener(IItbisListener listener) {
        this.listener = listener;
    }

    private void inicializarComponentes() {
        dgCrearItbis = new DgCrearItbis(new JFrame(), true);
        modelo = ModeloItbis.getInstance();
        btnSeleccionar.setVisible(false);
        inicializarTable();
        inicializarCombo();
    }

    /**
     *Mostrando toda la informacion del grupo.
     */
    public void setVisible(boolean flag) {
        if (flag) {
            itbisTable = modelo.getItbisPorCodigo(listaItbisTotal, "");
            tableModel.fireTableDataChanged();
        }
        super.setVisible(flag);
    }

    private void inicializarTable() {
        listaItbisTotal = modelo.getAllItbiss();
        itbisTable = modelo.getAllItbiss();
        tableModel = new AbstractTableModel() {

            public int getColumnCount() {
                return headers.length;
            }

            public int getRowCount() {
                return itbisTable.size();
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Itbis itbis = itbisTable.get(rowIndex);
                if (itbis != null) {
                    if (columnIndex == 0) {
                        return itbis.getCodigo();
                    }
                    if (columnIndex == 1) {
                        return itbis.getDescripcion();
                    }
                    if (columnIndex == 2) {
                        return itbis.getEquivalencia();
                    }
                }
                return "";
            }

            public String getColumnName(int col) {
                return headers[col];
            }
        };
        tableItbis.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    private void inicializarCombo() {
        cmbBusqueda.addItem("Seleccionar");
        cmbBusqueda.addItem("Codigo");
        cmbBusqueda.addItem("Descripcion");
        cmbBusqueda.addItem("Equivalencia");
    }

    private void addListener() {
        DgItbisListener listener = new DgItbisListener();
        subAdd = modelo.getSubjectAdd();
        subAdd.addObserver(listener);
        subRemove = modelo.getSubjectRemove();
        subRemove.addObserver(listener);
        subMensaje = modelo.getSubjectMensaje();
        subMensaje.addObserver(listener);
        subUpdate = modelo.getSubjectUpdate();
        subUpdate.addObserver(listener);
        tableItbis.getSelectionModel().addListSelectionListener(listener);
    }

    /**Metodo para obtener la fila seleccionada*/
    public Itbis getSelectedItbis() {
        Itbis itbis = null;
        int arow = tableItbis.getSelectedRow();
        if (arow != -1) {
            int row = tableItbis.convertRowIndexToModel(arow);
            itbis = itbisTable.get(row);
        }
        return itbis;
    }

    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnVisualizar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new utilidades.EnhancedJTextField();
        cmbBusqueda = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableItbis = new org.jdesktop.swingx.JXTable();
        btnCerrar = new javax.swing.JButton();
        btnSeleccionar = new javax.swing.JButton();
        jToolBar1.setFloatable(false);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setFont(new java.awt.Font("Dialog", 0, 12));
        btnNuevo.setFont(new java.awt.Font("Dialog", 0, 12));
        btnNuevo.setMnemonic('N');
        btnNuevo.setText("Nuevo");
        btnNuevo.setBorderPainted(false);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);
        btnEditar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnEditar.setMnemonic('E');
        btnEditar.setText("Editar");
        btnEditar.setBorderPainted(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditar);
        btnBorrar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnBorrar.setMnemonic('B');
        btnBorrar.setText("Borrar");
        btnBorrar.setBorderPainted(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBorrar);
        btnVisualizar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnVisualizar.setMnemonic('V');
        btnVisualizar.setText("Visualizar");
        btnVisualizar.setBorderPainted(false);
        btnVisualizar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnVisualizar);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText("Buscar");
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        cmbBusqueda.setFont(new java.awt.Font("Dialog", 0, 12));
        cmbBusqueda.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBusquedaItemStateChanged(evt);
            }
        });
        tableItbis.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane1.setViewportView(tableItbis);
        btnCerrar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnCerrar.setMnemonic('C');
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        btnSeleccionar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnSeleccionar.setMnemonic('S');
        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE).add(layout.createSequentialGroup().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtBuscar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmbBusqueda, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(btnSeleccionar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnCerrar))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(17, 17, 17).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(cmbBusqueda, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(txtBuscar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 186, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnCerrar).add(btnSeleccionar)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        String patron = txtBuscar.getText();
        String itemSelected = (String) cmbBusqueda.getSelectedItem();
        if (itemSelected.equals("Codigo")) {
            itbisTable = modelo.getItbisPorCodigo(listaItbisTotal, patron);
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Descripcion")) {
            itbisTable = modelo.getItbisPorDescripcion(listaItbisTotal, patron);
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Equivalencia")) {
            itbisTable = modelo.getItbisPorEquivalencia(listaItbisTotal, patron);
            tableModel.fireTableDataChanged();
        }
    }

    private void cmbBusquedaItemStateChanged(java.awt.event.ItemEvent evt) {
        String itemSelected = (String) cmbBusqueda.getSelectedItem();
        if (itemSelected.equals("Seleccionar")) {
            itbisTable.clear();
            txtBuscar.setEnabled(false);
        } else {
            txtBuscar.setEnabled(true);
            txtBuscar.requestFocus();
        }
    }

    private void btnVisualizarActionPerformed(java.awt.event.ActionEvent evt) {
        if (itbisSeleccionado != null) {
            ItbisEvent ev = new ItbisEvent();
            ev.setItbis(itbisSeleccionado);
            ev.setId(ItbisEvent.VISUALIZAR);
            dgCrearItbis.fireItbis(ev);
            dgCrearItbis.setVisible(true);
        }
    }

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (itbisSeleccionado != null) {
            modelo.removeItbis(itbisSeleccionado);
        }
    }

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        if (itbisSeleccionado != null) {
            ItbisEvent ev = new ItbisEvent();
            ev.setItbis(itbisSeleccionado.clone());
            ev.setId(ItbisEvent.EDITAR);
            dgCrearItbis.fireItbis(ev);
            dgCrearItbis.setVisible(true);
        }
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        ItbisEvent ev = new ItbisEvent();
        ev.setItbis(null);
        ev.setId(ItbisEvent.NUEVO);
        dgCrearItbis.fireItbis(ev);
        dgCrearItbis.setVisible(true);
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {
        Itbis itbis = getSelectedItbis();
        ItbisEvent ev = new ItbisEvent();
        ev.setItbis(itbis);
        ev.setId(ItbisEvent.SELECCIONAR_ITBIS);
        listener.fireItbis(ev);
        dispose();
    }

    /**Clase para manejar eventos alternos*/
    class DgItbisListener implements ListSelectionListener, IObserver {

        private JDialog padre;

        public void setPadre(JDialog padre) {
            this.padre = padre;
        }

        public void valueChanged(ListSelectionEvent e) {
            itbisSeleccionado = getSelectedItbis();
        }

        public void update(Class clase, Object argumento, String mensaje) {
            if (clase == ModeloItbis.class) {
                if (argumento != null) {
                    if (mensaje.equals("addItbis")) {
                        Itbis itbis = (Itbis) argumento;
                        itbisTable.add(itbis);
                        listaItbisTotal.add(itbis);
                        tableModel.fireTableDataChanged();
                    }
                    if (mensaje.equals("removeItbis")) {
                        Itbis itbis = (Itbis) argumento;
                        itbisTable.remove(itbis);
                        listaItbisTotal.remove(itbis);
                        tableModel.fireTableDataChanged();
                    }
                    if (mensaje.equals("updateItbis")) {
                        Itbis itbis = (Itbis) argumento;
                        listaItbisTotal.remove(itbisSeleccionado);
                        listaItbisTotal.add(itbis);
                        itbisTable.remove(itbisSeleccionado);
                        itbisTable.add(itbis);
                        tableModel.fireTableDataChanged();
                    }
                    if (mensaje.equals("mensaje")) {
                        setWarning(argumento + "");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        DgAdministrarItbis dgItbis = new DgAdministrarItbis();
        dgItbis.setVisible(true);
    }

    public void setWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getBtnSeleccionar() {
        return btnSeleccionar;
    }

    private javax.swing.JButton btnBorrar;

    private javax.swing.JButton btnCerrar;

    private javax.swing.JButton btnEditar;

    private javax.swing.JButton btnNuevo;

    private javax.swing.JButton btnSeleccionar;

    private javax.swing.JButton btnVisualizar;

    private javax.swing.JComboBox cmbBusqueda;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JToolBar jToolBar1;

    private org.jdesktop.swingx.JXTable tableItbis;

    private utilidades.EnhancedJTextField txtBuscar;
}
