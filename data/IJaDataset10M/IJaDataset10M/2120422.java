package frontend.cliente;

import eventos.IClienteListener;
import eventos.ClienteEvent;
import eventos.IGrupoClienteListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import mensaje_objetos.cliente.Contacto;
import mensaje_objetos.cliente.Direccion;
import mensaje_objetos.cliente.GrupoCliente;
import mensaje_objetos.cliente.Cliente;
import modelo_objetos.cliente.ModeloGrupoCliente;
import modelo_objetos.cliente.ModeloCliente;
import utilidades.IObserver;
import utilidades.Screen;
import utilidades.SubjectHelper;

/**
 *
 * @author  carlos
 */
public class DgAdministrarCliente extends javax.swing.JDialog {

    private List<Cliente> clienteTable = new ArrayList();

    private List<Cliente> listaClienteTotal = new ArrayList();

    private ModeloCliente modelo;

    private Cliente clienteSeleccionado;

    private SubjectHelper subAdd;

    private SubjectHelper subRemove;

    private SubjectHelper subMensaje;

    private SubjectHelper subUpdate;

    private String[] headers = { "Codigo", "Nombre", "RNC", "Razon Social", "Contacto Principal", "Cedula", "Grupo", "Fecha", "Direccion", "Provincia", "Ciudad", "Pais", "Telefono" };

    private AbstractTableModel tableModel;

    private DefaultTableCellRenderer tableRender;

    private IClienteListener listener;

    private IInsertarCliente dgCrearCliente;

    /** Creates new form IAdministrarCliente */
    public DgAdministrarCliente() {
        super(new JFrame(), true);
        initComponents();
        inicializarComponentes();
        addListener();
    }

    private void inicializarComponentes() {
        dgCrearCliente = new IInsertarCliente();
        modelo = ModeloCliente.getInstance();
        inicializarTable();
        Screen sc = new Screen(this);
        sc.centrarPantalla();
        inicializarCombo();
    }

    public void addClienteListener(IClienteListener listener) {
        this.listener = listener;
    }

    private void inicializarCombo() {
        cmbTipoBusqueda.addItem("Seleccionar");
        cmbTipoBusqueda.addItem("Codigo");
        cmbTipoBusqueda.addItem("Nombre");
        cmbTipoBusqueda.addItem("RNC");
        cmbTipoBusqueda.addItem("Razon Social");
        cmbTipoBusqueda.addItem("Contacto Principal");
        cmbTipoBusqueda.addItem("Cedula");
        cmbTipoBusqueda.addItem("Grupo");
        cmbTipoBusqueda.addItem("Fecha");
        cmbTipoBusqueda.addItem("Direccion");
        cmbTipoBusqueda.addItem("Ciudad");
        cmbTipoBusqueda.addItem("Provincia");
        cmbTipoBusqueda.addItem("Pais");
        cmbTipoBusqueda.addItem("Telefono");
    }

    private void inicializarTable() {
        listaClienteTotal = modelo.getAllClientes();
        clienteTable = modelo.getAllClientes();
        tableModel = new AbstractTableModel() {

            public int getColumnCount() {
                return headers.length;
            }

            public int getRowCount() {
                return clienteTable.size();
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Cliente cliente = clienteTable.get(rowIndex);
                if (cliente != null) {
                    if (columnIndex == 0) {
                        return cliente.getCodigo();
                    }
                    if (columnIndex == 1) {
                        return cliente.getNombre();
                    }
                    if (columnIndex == 2) {
                        return cliente.getRnc();
                    }
                    if (columnIndex == 3) {
                        return cliente.getRazonSocial();
                    }
                    if (columnIndex == 4) {
                        if (cliente.getContactos().size() > 0) {
                            Contacto cont = cliente.getContactos().get(0);
                            if (cont != null) {
                                return cont.getNombre();
                            }
                        }
                        return "";
                    }
                    if (columnIndex == 5) {
                        return cliente.getCedula();
                    }
                    if (columnIndex == 6) {
                        if (cliente.getListaGrupos().size() > 0) {
                            GrupoCliente grupo = cliente.getListaGrupos().get(0);
                            if (grupo != null) {
                                return grupo.getNombre();
                            }
                        }
                        return "";
                    }
                    if (columnIndex == 7) {
                        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                        return f.format(cliente.getFechaIngreso());
                    }
                    if (columnIndex == 8) {
                        if (cliente.getDirecciones().size() > 0) {
                            Direccion dir = cliente.getDirecciones().get(0);
                            if (dir != null) {
                                return dir.getDescripcion();
                            }
                        }
                        return "";
                    }
                    if (columnIndex == 9) {
                        if (cliente.getDirecciones().size() > 0) {
                            Direccion dir = cliente.getDirecciones().get(0);
                            if (dir != null) {
                                if (dir.getCiudad() != null) {
                                    return dir.getCiudad().getNombre();
                                }
                            }
                        }
                        return "";
                    }
                    if (columnIndex == 10) {
                        if (cliente.getDirecciones().size() > 0) {
                            Direccion dir = cliente.getDirecciones().get(0);
                            if (dir != null) {
                                if (dir.getProvincia() != null) {
                                    return dir.getProvincia().getNombre();
                                }
                            }
                        }
                        return "";
                    }
                    if (columnIndex == 11) {
                        if (cliente.getDirecciones().size() > 0) {
                            Direccion dir = cliente.getDirecciones().get(0);
                            if (dir != null) {
                                if (dir.getPais() != null) {
                                    return dir.getPais().getNombre();
                                }
                            }
                        }
                        return "";
                    }
                    if (columnIndex == 12) {
                        if (cliente.getContactos().size() > 0) {
                            Contacto cont = cliente.getContactos().get(0);
                            if (cont != null) {
                                return cont.getTelefono1();
                            }
                        }
                        return "";
                    }
                }
                return "";
            }

            public String getColumnName(int col) {
                return headers[col];
            }
        };
        tableCliente.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    private void addListener() {
        DgClienteListener listener = new DgClienteListener();
        subAdd = modelo.getSubjectAdd();
        subAdd.addObserver(listener);
        subRemove = modelo.getSubjectRemove();
        subRemove.addObserver(listener);
        subUpdate = modelo.getSubjectUpdate();
        subUpdate.addObserver(listener);
        tableCliente.getSelectionModel().addListSelectionListener(listener);
    }

    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnVisualizar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new utilidades.EnhancedJTextField();
        jLabel2 = new javax.swing.JLabel();
        cmbTipoBusqueda = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCliente = new org.jdesktop.swingx.JXTable();
        dateFechaIngreso = new org.jdesktop.swingx.JXDatePicker();
        btnCerrar = new javax.swing.JButton();
        btnSeleccionar = new javax.swing.JButton();
        setIconifiable(true);
        setTitle("Administrar Clientes");
        jToolBar1.setFloatable(false);
        jToolBar1.setBorderPainted(false);
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
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Tipo de busqueda");
        cmbTipoBusqueda.setFont(new java.awt.Font("Dialog", 0, 12));
        tableCliente.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null, null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null, null, null, null, null } }, new String[] { "Codigo", "Nombre", "RNC", "Razon Social", "Contacto Principal", "Cedula", "Grupo", "Fecha", "Direccion", "Ciudad", "Pais", "Telefono" }));
        jScrollPane1.setViewportView(tableCliente);
        jScrollPane2.setViewportView(jScrollPane1);
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
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE).add(layout.createSequentialGroup().add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtBuscar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 306, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE).add(dateFechaIngreso, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmbTipoBusqueda, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 186, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(btnSeleccionar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnCerrar))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(cmbTipoBusqueda, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2).add(txtBuscar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(dateFechaIngreso, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 343, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnCerrar).add(btnSeleccionar)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {
        if (listener != null) {
            Cliente cliente = getSelectedCliente();
            if (listener != null) {
                ClienteEvent ev = new ClienteEvent();
                ev.setCliente(cliente);
                ev.setId(ClienteEvent.SELECCIONAR_CLIENTE);
                listener.fireCliente(ev);
            }
        }
        dispose();
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        String patron = txtBuscar.getText();
        String itemSelected = (String) cmbTipoBusqueda.getSelectedItem();
        if (itemSelected.equals("Codigo")) {
            clienteTable = modelo.getClientePorCodigo(listaClienteTotal, patron);
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Nombre")) {
            clienteTable = modelo.getClientePorNombre(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("RNC")) {
            clienteTable = modelo.getClientePorRNC(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Razon Social")) {
            clienteTable = modelo.getClientePorRazonSocial(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Contacto Principal")) {
            clienteTable = modelo.getClientePorContactoPrincipal(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Cedula")) {
            clienteTable = modelo.getClientePorCedula(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Grupo")) {
            clienteTable = modelo.getClientePorGrupo(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Fecha")) {
            clienteTable = modelo.getClienteFecha(listaClienteTotal, dateFechaIngreso.getDate());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Direccion")) {
            clienteTable = modelo.getClientePorDireccion(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Ciudad")) {
            clienteTable = modelo.getClientePorCiudad(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Provincia")) {
            clienteTable = modelo.getClientePorPais(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Pais")) {
            clienteTable = modelo.getClientePorPais(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
        if (itemSelected.equals("Telefono")) {
            clienteTable = modelo.getClientePorTelefono(listaClienteTotal, patron.trim());
            tableModel.fireTableDataChanged();
        }
    }

    private void btnVisualizarActionPerformed(java.awt.event.ActionEvent evt) {
        dgCrearCliente = new IInsertarCliente();
        clienteSeleccionado = getSelectedCliente();
        ClienteEvent ev = new ClienteEvent();
        ev.setId(ClienteEvent.VISUALIZAR);
        ev.setCliente(clienteSeleccionado);
        dgCrearCliente.fireCliente(ev);
        dgCrearCliente.setVisible(true);
    }

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        clienteSeleccionado = getSelectedCliente();
        dgCrearCliente = new IInsertarCliente();
        ClienteEvent ev = new ClienteEvent();
        ev.setId(ClienteEvent.EDITAR);
        ev.setCliente(clienteSeleccionado);
        dgCrearCliente.fireCliente(ev);
        dgCrearCliente.setVisible(true);
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        ClienteEvent ev = new ClienteEvent();
        ev.setId(ClienteEvent.NUEVO);
        dgCrearCliente.fireCliente(ev);
        dgCrearCliente.setVisible(true);
    }

    /**Metodo para obtener la fila seleccionada*/
    public Cliente getSelectedCliente() {
        Cliente cliente = null;
        int arow = tableCliente.getSelectedRow();
        if (arow != -1) {
            int row = tableCliente.convertRowIndexToModel(arow);
            cliente = clienteTable.get(row);
        }
        return cliente;
    }

    public void setWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**Clase para manejar eventos alternos*/
    class DgClienteListener implements ListSelectionListener, IObserver {

        private JDialog padre;

        public void setPadre(JDialog padre) {
            this.padre = padre;
        }

        public void valueChanged(ListSelectionEvent e) {
            clienteSeleccionado = getSelectedCliente();
        }

        public void update(Class clase, Object argumento, String mensaje) {
            if (clase == ModeloCliente.class) {
                if (argumento != null) {
                    if (mensaje.equals("addCliente")) {
                        Cliente cliente = (Cliente) argumento;
                        clienteTable.add(cliente);
                        listaClienteTotal.add(cliente);
                        tableModel.fireTableDataChanged();
                    }
                    if (mensaje.equals("removeCliente")) {
                        Cliente cliente = (Cliente) argumento;
                        clienteTable.remove(cliente);
                        listaClienteTotal.remove(cliente);
                        tableModel.fireTableDataChanged();
                    }
                    if (mensaje.equals("updateCliente")) {
                        Cliente cliente = (Cliente) argumento;
                        listaClienteTotal.remove(clienteSeleccionado);
                        listaClienteTotal.add(cliente);
                        clienteTable.remove(clienteSeleccionado);
                        clienteTable.add(cliente);
                        tableModel.fireTableDataChanged();
                    }
                    if (mensaje.equals("mensaje")) {
                        setWarning(argumento + "");
                    }
                }
            }
        }
    }

    public void fireCliente(ClienteEvent desc) {
    }

    private void setIconifiable(boolean b) {
    }

    private javax.swing.JButton btnBorrar;

    private javax.swing.JButton btnCerrar;

    private javax.swing.JButton btnEditar;

    private javax.swing.JButton btnNuevo;

    private javax.swing.JButton btnSeleccionar;

    private javax.swing.JButton btnVisualizar;

    private javax.swing.JComboBox cmbTipoBusqueda;

    private org.jdesktop.swingx.JXDatePicker dateFechaIngreso;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JToolBar jToolBar1;

    private org.jdesktop.swingx.JXTable tableCliente;

    private utilidades.EnhancedJTextField txtBuscar;
}
