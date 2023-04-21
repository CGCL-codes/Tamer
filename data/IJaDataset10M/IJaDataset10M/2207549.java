package frontend.recibodecaja;

import eventos.ClienteEvent;
import eventos.FormaPagoEvent;
import eventos.IClienteListener;
import eventos.IFormaPagoListener;
import eventos.ProductoEvent;
import eventos.ReciboCajaEvent;
import eventos.ReciboCajaEvent;
import frontend.cliente.DgAdministrarCliente;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import mensaje_objetos.almacen.Producto;
import mensaje_objetos.cliente.Cliente;
import mensaje_objetos.cliente.Direccion;
import mensaje_objetos.facturacion.Factura;
import mensaje_objetos.notas.NotaDebito;
import mensaje_objetos.recibocaja.FormaPago;
import mensaje_objetos.recibocaja.ReciboCaja;
import modelo_objetos.recibocaja.ModeloReciboCaja;
import modelo_objetos.serie.ModeloDocumentoSerie;
import utilidades.IObserver;
import utilidades.Screen;
import utilidades.SubjectHelper;
import utilidades.n2t;

/**
 *
 * @author  carlos
 */
public class DgCrearReciboDeCaja extends javax.swing.JDialog implements IObserver, IClienteListener, IFormaPagoListener {

    private ModeloReciboCaja modelo;

    private ModeloDocumentoSerie modeloDocSerie;

    private SubjectHelper subAdd;

    private SubjectHelper subUpdate;

    private ReciboCaja ReciboCajaSeleccionado;

    private ReciboCaja ReciboCajaNuevo;

    private ReciboCaja ReciboCajaActualizar;

    private List<Factura> listaFacturas = new ArrayList();

    private List<Factura> facturaTable = new ArrayList();

    private List<NotaDebito> listaNotas = new ArrayList();

    private List<NotaDebito> notaTable = new ArrayList();

    private List<ReciboCaja> listaReciboCaja = new ArrayList();

    private List<ReciboCaja> reciboCajaTable = new ArrayList();

    private List<FormaPago> listaFormaPago = new ArrayList();

    private String[] headersFactura = { "Codigo", "Total", "Pendiente", "Pagado", "Fecha", "Fecha Vencimiento" };

    private String[] headersNota = { "Codigo", "Concepto", "Fecha", "Total" };

    private AbstractTableModel modeloTablaFactura;

    private AbstractTableModel modeloTablaNotaDebito;

    private AbstractTableModel modeloTablaRecibo;

    private ReciboCaja reciboCajaNuevo;

    private ReciboCaja reciboCajaActualizar;

    private Cliente clienteSeleccionado;

    private FormaPago formaPagoSeleccionada;

    private DgAdministrarCliente dgAdministrarCliente;

    private DgFormaPagoRecibo dgFormaPago;

    private int accion;

    private double montoTotal = 0.0;

    /** Creates new form DgCrearReciboDeCaja */
    public DgCrearReciboDeCaja(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        modelo = ModeloReciboCaja.getInstance();
        modeloDocSerie = ModeloDocumentoSerie.getInstance();
        Screen sc = new Screen(this);
        sc.centrarPantalla();
        addListener();
        inicializarTableFactura();
        inicializarTableNota();
        inicializarCombo();
    }

    private void addListener() {
        subAdd = modelo.getSubjectAdd();
        subAdd.addObserver(this);
        subUpdate = modelo.getSubjectUpdate();
        subUpdate.addObserver(this);
    }

    private void inicializarCombo() {
        cmbBuscarFacturas.addItem("Seleccionar");
        cmbBuscarFacturas.addItem("Automoatico Por Fecha");
        cmbBuscarFacturas.addItem("Manual");
    }

    private void inicializarTableFactura() {
        if (facturaTable == null) {
            facturaTable = new ArrayList();
        }
        modeloTablaFactura = new AbstractTableModel() {

            public int getColumnCount() {
                return headersFactura.length;
            }

            public int getRowCount() {
                return facturaTable.size();
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Factura factura = facturaTable.get(rowIndex);
                if (factura != null) {
                    if (columnIndex == 0) {
                        return factura.getCodigo();
                    }
                    if (columnIndex == 1) {
                        return factura.getTotalNeto() + "";
                    }
                    if (columnIndex == 2) {
                        return factura.getTotalPendiente() + "";
                    }
                    if (columnIndex == 3) {
                        return factura.getTotalPagado() + "";
                    }
                    if (columnIndex == 4) {
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        return format.format(factura.getFechaFactura());
                    }
                    if (columnIndex == 5) {
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        return format.format(factura.getFechaVencimiento());
                    }
                }
                return "";
            }

            public String getColumnName(int col) {
                return headersFactura[col];
            }
        };
        tableFacturas.setModel(modeloTablaFactura);
        modeloTablaFactura.fireTableDataChanged();
    }

    private void inicializarTableNota() {
        modeloTablaNotaDebito = new AbstractTableModel() {

            public int getColumnCount() {
                return headersNota.length;
            }

            public int getRowCount() {
                return notaTable.size();
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                NotaDebito nota = notaTable.get(rowIndex);
                if (nota != null) {
                    if (columnIndex == 0) {
                        return "";
                    }
                    if (columnIndex == 1) {
                        return "";
                    }
                    if (columnIndex == 2) {
                        return "";
                    }
                    if (columnIndex == 3) {
                        return "";
                    }
                }
                return "";
            }

            public String getColumnName(int col) {
                return headersNota[col];
            }
        };
        tableNotaDebito.setModel(modeloTablaNotaDebito);
        modeloTablaNotaDebito.fireTableDataChanged();
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCodigo = new utilidades.EnhancedJTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        btnCliente = new javax.swing.JButton();
        txtCliente = new utilidades.EnhancedJTextField();
        jLabel2 = new javax.swing.JLabel();
        txtRNC = new utilidades.EnhancedJTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDireccion = new utilidades.EnhancedJTextField();
        jLabel5 = new javax.swing.JLabel();
        txtProvincia = new utilidades.EnhancedJTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCiudad = new utilidades.EnhancedJTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPais = new utilidades.EnhancedJTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCedula = new utilidades.EnhancedJTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFacturas = new org.jdesktop.swingx.JXTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnInsertar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnVisualizar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cmbBuscarFacturas = new javax.swing.JComboBox();
        btnBuscarFacturas = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnInsertarnota = new javax.swing.JButton();
        btnRemoverNota = new javax.swing.JButton();
        btnVisualizarNota = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableNotaDebito = new org.jdesktop.swingx.JXTable();
        btnFormaPago = new javax.swing.JButton();
        txtMonto = new utilidades.EnhancedJTextField();
        btnCerrar = new javax.swing.JButton();
        btnSalvarCerrar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        areaMonto = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        txtConcepto = new utilidades.EnhancedJTextField();
        checkAdelantado = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        dateFecha = new org.jdesktop.swingx.JXDatePicker();
        jLabel10 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("ComboBox.selectionBackground")));
        jLabel1.setText("Codigo");
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(416, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(15, Short.MAX_VALUE).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(txtCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        btnCliente.setText("Cliente");
        btnCliente.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteActionPerformed(evt);
            }
        });
        txtCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCliente.setEditable(false);
        jLabel2.setText("RNC");
        txtRNC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtRNC.setEditable(false);
        jLabel4.setText("Direccion");
        txtDireccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtDireccion.setEditable(false);
        jLabel5.setText("Provincia");
        txtProvincia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtProvincia.setEditable(false);
        jLabel6.setText("Ciudad");
        txtCiudad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCiudad.setEditable(false);
        jLabel7.setText("Pais");
        txtPais.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtPais.setEditable(false);
        jLabel3.setText("Cedula");
        txtCedula.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCedula.setEditable(false);
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(btnCliente).add(jLabel4).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel6).add(jLabel5).add(jLabel7))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(txtCliente, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtProvincia, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtCiudad, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtDireccion, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE).add(txtPais, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(jLabel3)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(txtCedula, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE).add(txtRNC, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnCliente).add(txtCliente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2).add(txtRNC, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(txtDireccion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3).add(txtCedula, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(txtProvincia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(txtCiudad, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel7).add(txtPais, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(86, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Datos Cliente", jPanel2);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Facturas a Pagar"));
        tableFacturas.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null } }, new String[] { "Codigo", "Total", "Pendiente", "Pagado", "Fecha", "A Pagar" }));
        jScrollPane1.setViewportView(tableFacturas);
        jToolBar1.setFloatable(false);
        jToolBar1.setBorderPainted(false);
        btnInsertar.setText("Insertar +");
        jToolBar1.add(btnInsertar);
        btnRemover.setText("Remover -");
        jToolBar1.add(btnRemover);
        btnVisualizar.setText("Visualizar");
        jToolBar1.add(btnVisualizar);
        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().addContainerGap().add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jLabel8.setText("Modo de Pago");
        cmbBuscarFacturas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Manual", "Automatico por Fecha" }));
        btnBuscarFacturas.setText("Buscar Facturas a Pagar");
        btnBuscarFacturas.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFacturasActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmbBuscarFacturas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 182, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnBuscarFacturas)).add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel8).add(cmbBuscarFacturas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnBuscarFacturas)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Facturas", jPanel3);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Notas de Debito"));
        jToolBar2.setFloatable(false);
        jToolBar2.setBorderPainted(false);
        btnInsertarnota.setText("Insertar +");
        btnInsertarnota.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarnotaActionPerformed(evt);
            }
        });
        jToolBar2.add(btnInsertarnota);
        btnRemoverNota.setText("Remover -");
        btnRemoverNota.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverNotaActionPerformed(evt);
            }
        });
        jToolBar2.add(btnRemoverNota);
        btnVisualizarNota.setText("Visualizar");
        btnVisualizarNota.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarNotaActionPerformed(evt);
            }
        });
        jToolBar2.add(btnVisualizarNota);
        tableNotaDebito.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Codigo", "Fecha", "Descripcion", "Monto" }));
        jScrollPane2.setViewportView(tableNotaDebito);
        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup().addContainerGap().add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jToolBar2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)).addContainerGap()));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel6Layout.createSequentialGroup().add(jToolBar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel5Layout.createSequentialGroup().addContainerGap().add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel5Layout.createSequentialGroup().addContainerGap().add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(58, 58, 58)));
        jTabbedPane1.addTab("Nota de Debito", jPanel5);
        btnFormaPago.setText("Forma de Pago");
        btnFormaPago.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFormaPagoActionPerformed(evt);
            }
        });
        txtMonto.setEditable(false);
        txtMonto.setForeground(new java.awt.Color(153, 153, 153));
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        btnSalvarCerrar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnSalvarCerrar.setText("Salvar ->");
        btnSalvarCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarCerrarActionPerformed(evt);
            }
        });
        btnSalvar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        btnNuevo.setFont(new java.awt.Font("Dialog", 0, 12));
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jLabel11.setText("Monto");
        areaMonto.setBackground(javax.swing.UIManager.getDefaults().getColor("ComboBox.background"));
        areaMonto.setColumns(20);
        areaMonto.setForeground(javax.swing.UIManager.getDefaults().getColor("Nb.ScrollPane.Border.color"));
        areaMonto.setRows(5);
        jScrollPane3.setViewportView(areaMonto);
        jLabel12.setText("Concepto");
        checkAdelantado.setText("Adelantado");
        checkAdelantado.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkAdelantado.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkAdelantado.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkAdelantadoItemStateChanged(evt);
            }
        });
        jLabel14.setText("Tipo de Recibo");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Fecha");
        jLabel10.setBackground(new java.awt.Color(204, 204, 204));
        jLabel10.setForeground(javax.swing.UIManager.getDefaults().getColor("Nb.ScrollPane.Border.color"));
        jLabel10.setText("Monto Total");
        jLabel10.setOpaque(true);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().addContainerGap().add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(btnNuevo).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnSalvar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnSalvarCerrar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnCerrar)))).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(10, 10, 10).add(jLabel12).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtConcepto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 299, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE).add(btnFormaPago, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(7, 7, 7).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtMonto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 201, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(checkAdelantado, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)).add(6, 6, 6).add(jLabel11).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 328, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().addContainerGap().add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().addContainerGap().add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(dateFecha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jLabel9).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel14).add(checkAdelantado)).add(16, 16, 16)).add(layout.createSequentialGroup().add(dateFecha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(39, 39, 39).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel11).add(txtMonto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel10))).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createSequentialGroup().add(btnFormaPago).add(48, 48, 48)).add(layout.createSequentialGroup().add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 242, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(4, 4, 4).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnCerrar).add(btnSalvarCerrar).add(btnSalvar).add(btnNuevo)).add(layout.createSequentialGroup().add(20, 20, 20).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(txtConcepto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel12)))))).addContainerGap()));
        pack();
    }

    private void btnFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {
        dgFormaPago = new DgFormaPagoRecibo(new JFrame(), true);
        FormaPagoEvent ev = new FormaPagoEvent();
        dgFormaPago.fireFormaPago(listaFormaPago);
        dgFormaPago.addFormaPagoListener(this);
        dgFormaPago.setVisible(true);
    }

    private void checkAdelantadoItemStateChanged(java.awt.event.ItemEvent evt) {
        if (checkAdelantado.isSelected()) {
            jTabbedPane1.setEnabled(false);
        } else {
            jTabbedPane1.setEnabled(true);
        }
    }

    private void btnVisualizarNotaActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnRemoverNotaActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnInsertarnotaActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnBuscarFacturasActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnClienteActionPerformed(java.awt.event.ActionEvent evt) {
        dgAdministrarCliente = new DgAdministrarCliente();
        dgAdministrarCliente.addClienteListener(this);
        dgAdministrarCliente.setVisible(true);
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void btnSalvarCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        execute();
        dispose();
    }

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        execute();
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarCampos();
        txtCodigo.requestFocus();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DgCrearReciboDeCaja(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private void execute() {
        if (accion == ReciboCajaEvent.NUEVO) {
            reciboCajaNuevo = new ReciboCaja();
            String codigo = txtCodigo.getText();
            String serie = modeloDocSerie.getValueSerie("Recibo Caja");
            String monto = txtMonto.getText();
            String descripcionTotal = areaMonto.getText();
            String concepto = txtConcepto.getText();
            if (!codigo.equals("") && !monto.equals("")) {
                if (checkAdelantado.isSelected()) {
                    reciboCajaNuevo.setAdelantado(1);
                } else {
                    reciboCajaNuevo.setAdelantado(0);
                }
                reciboCajaNuevo.setCliente(clienteSeleccionado);
                reciboCajaNuevo.setCodigo(codigo);
                reciboCajaNuevo.setConcepto(concepto);
                reciboCajaNuevo.setDescripcionTotal(descripcionTotal);
                reciboCajaNuevo.setFecha(dateFecha.getDate());
                reciboCajaNuevo.setListaFacturas(facturaTable);
                reciboCajaNuevo.setSerie(serie);
                reciboCajaNuevo.setFormasPago(listaFormaPago);
                reciboCajaNuevo.setTotal(new BigDecimal(montoTotal));
                reciboCajaNuevo.setRebajado(0);
                modelo.addReciboCaja(reciboCajaNuevo);
            } else {
                setWarning("Debes completar los campos");
            }
        }
        if (accion == ReciboCajaEvent.EDITAR) {
            if (reciboCajaActualizar != null) {
                String codigo = txtCodigo.getText();
                String monto = txtMonto.getText();
                String descripcionTotal = areaMonto.getText();
                String concepto = txtConcepto.getText();
                reciboCajaActualizar.setCodigo(codigo);
                reciboCajaActualizar.setCliente(clienteSeleccionado);
                reciboCajaActualizar.setConcepto(concepto);
                reciboCajaActualizar.setDescripcionTotal(descripcionTotal);
                modelo.updateReciboCaja(reciboCajaActualizar);
            }
        }
    }

    public void update(Class clase, Object argumento, String mensaje) {
        if (clase == ModeloReciboCaja.class) {
            if (mensaje.equals("addReciboCaja")) {
                limpiarCampos();
                txtCodigo.requestFocus();
            }
            if (mensaje.equals("updateReciboCaja")) {
                limpiarCampos();
            }
        }
    }

    private Factura getSelectedFactura() {
        Factura factura = null;
        int row = tableFacturas.getSelectedRow();
        if (row != -1) {
            int arow = tableFacturas.convertRowIndexToModel(row);
            factura = facturaTable.get(arow);
        }
        return factura;
    }

    private NotaDebito getSelectedNota() {
        NotaDebito nota = null;
        int row = tableNotaDebito.getSelectedRow();
        if (row != -1) {
            int arow = tableNotaDebito.convertRowIndexToModel(row);
            nota = notaTable.get(arow);
        }
        return nota;
    }

    private void calcularMonto(List<FormaPago> lista) {
        montoTotal = 0.0;
        for (FormaPago i : lista) {
            montoTotal += i.getMontoPagar().doubleValue();
        }
        String montoText = getCurrency(montoTotal);
        txtMonto.setText(montoText);
        n2t conv = new n2t();
        String montoLetras = conv.convertirLetras((int) montoTotal);
        montoLetras += " PESOS";
        areaMonto.setText(fraccionarCantidad(montoLetras));
    }

    private String fraccionarCantidad(String cant) {
        String cantNew = cant;
        if (cant.length() > 40) {
            String t = cant.substring(0, 40);
            String t1 = cant.substring(40, cant.length());
            cantNew = t + "\n" + t1;
        }
        return cantNew.toUpperCase();
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtCedula.setText("");
        txtCliente.setText("");
        txtConcepto.setText("");
        txtDireccion.setText("");
        txtMonto.setText("");
        txtPais.setText("");
        txtProvincia.setText("");
        txtRNC.setText("");
        areaMonto.setText("");
    }

    public void fireReciboCaja(ReciboCajaEvent reciboCaja) {
        if (reciboCaja.getId() == ReciboCajaEvent.VISUALIZAR) {
            accion = ReciboCajaEvent.VISUALIZAR;
            ReciboCaja reciboCaja1 = reciboCaja.getReciboCaja();
            if (reciboCaja1 != null) {
                setReciboCaja(reciboCaja1);
                setEnabled(false);
            }
        }
        if (reciboCaja.getId() == ReciboCajaEvent.NUEVO) {
            accion = ReciboCajaEvent.NUEVO;
            setEnabled(true);
            txtCodigo.requestFocus();
        }
        if (reciboCaja.getId() == ReciboCajaEvent.EDITAR) {
            accion = ReciboCajaEvent.EDITAR;
            ReciboCaja reciboCaja1 = reciboCaja.getReciboCaja();
            setReciboCaja(reciboCaja1);
            setEnabled(true);
        }
    }

    private void setReciboCaja(ReciboCaja reciboCaja) {
        clienteSeleccionado = reciboCaja.getCliente();
        listaFacturas = reciboCaja.getListaFacturas();
        listaNotas = reciboCaja.getListaNotaDebito();
        listaFormaPago = reciboCaja.getFormasPago();
        if (listaFacturas == null) {
            listaFacturas = new ArrayList();
        }
        if (listaNotas == null) {
            listaNotas = new ArrayList();
        }
        facturaTable = listaFacturas;
        notaTable = listaNotas;
        modeloTablaFactura.fireTableDataChanged();
        modeloTablaNotaDebito.fireTableDataChanged();
        txtCedula.setText(clienteSeleccionado.getCedula());
        if (clienteSeleccionado.getDirecciones() != null) {
            if (clienteSeleccionado.getDirecciones().size() > 0) {
                Direccion direccion = clienteSeleccionado.getDirecciones().get(0);
                if (direccion.getCiudad() != null) {
                    txtCiudad.setText(direccion.getCiudad().getNombre());
                }
                if (direccion.getPais() != null) {
                    txtPais.setText(direccion.getPais().getNombre());
                }
                if (direccion.getProvincia() != null) {
                    txtProvincia.setText(direccion.getProvincia().getNombre());
                }
                txtDireccion.setText(direccion.getDescripcion());
            }
        }
        txtCliente.setText(clienteSeleccionado.getNombre());
        txtCodigo.setText(reciboCaja.getCodigo());
        txtConcepto.setText(reciboCaja.getConcepto());
        txtMonto.setText(reciboCaja.getTotal() + "");
        txtRNC.setText(clienteSeleccionado.getRnc());
        areaMonto.setText(reciboCaja.getDescripcionTotal());
        if (reciboCaja.getAdelantado() == 1) {
            checkAdelantado.setSelected(true);
        } else {
            checkAdelantado.setSelected(false);
        }
    }

    public void setEnabled(boolean des) {
        btnBuscarFacturas.setEnabled(des);
        btnCliente.setEnabled(des);
        btnFormaPago.setEnabled(des);
        btnInsertar.setEnabled(des);
        btnInsertarnota.setEnabled(des);
        btnNuevo.setEnabled(des);
        btnRemover.setEnabled(des);
        btnRemoverNota.setEnabled(des);
        btnSalvar.setEnabled(des);
        btnSalvarCerrar.setEnabled(des);
        btnVisualizar.setEnabled(des);
        btnVisualizarNota.setEnabled(des);
    }

    private String getCurrency(double total) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        String tot = format.format(total);
        return tot;
    }

    private String getTipoComa(double cantidad) {
        NumberFormat format = NumberFormat.getNumberInstance();
        String tot = format.format(cantidad);
        return tot;
    }

    public void setWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void fireCliente(ClienteEvent ev) {
        if (ev != null) {
            clienteSeleccionado = ev.getCliente();
            if (clienteSeleccionado != null) {
                txtCliente.setText(clienteSeleccionado.getNombre());
                txtRNC.setText(clienteSeleccionado.getRnc());
                txtCedula.setText(clienteSeleccionado.getCedula());
                if (clienteSeleccionado.getDirecciones() != null) {
                    if (clienteSeleccionado.getDirecciones().size() > 0) {
                        Direccion direccion = clienteSeleccionado.getDirecciones().get(0);
                        txtDireccion.setText(direccion.getDescripcion());
                        if (direccion.getCiudad() != null) {
                            txtCiudad.setText(direccion.getCiudad().getNombre());
                        }
                        if (direccion.getProvincia() != null) {
                            txtProvincia.setText(direccion.getProvincia().getNombre());
                        }
                        if (direccion.getPais() != null) {
                            txtPais.setText(direccion.getPais().getNombre());
                        }
                    }
                }
            }
        }
    }

    public void fireFormaPago(Object forma) {
        listaFormaPago = (List<FormaPago>) forma;
        calcularMonto(listaFormaPago);
    }

    private javax.swing.JTextArea areaMonto;

    private javax.swing.JButton btnBuscarFacturas;

    private javax.swing.JButton btnCerrar;

    private javax.swing.JButton btnCliente;

    private javax.swing.JButton btnFormaPago;

    private javax.swing.JButton btnInsertar;

    private javax.swing.JButton btnInsertarnota;

    private javax.swing.JButton btnNuevo;

    private javax.swing.JButton btnRemover;

    private javax.swing.JButton btnRemoverNota;

    private javax.swing.JButton btnSalvar;

    private javax.swing.JButton btnSalvarCerrar;

    private javax.swing.JButton btnVisualizar;

    private javax.swing.JButton btnVisualizarNota;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JCheckBox checkAdelantado;

    private javax.swing.JComboBox cmbBuscarFacturas;

    private org.jdesktop.swingx.JXDatePicker dateFecha;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JToolBar jToolBar2;

    private org.jdesktop.swingx.JXTable tableFacturas;

    private org.jdesktop.swingx.JXTable tableNotaDebito;

    private utilidades.EnhancedJTextField txtCedula;

    private utilidades.EnhancedJTextField txtCiudad;

    private utilidades.EnhancedJTextField txtCliente;

    private utilidades.EnhancedJTextField txtCodigo;

    private utilidades.EnhancedJTextField txtConcepto;

    private utilidades.EnhancedJTextField txtDireccion;

    private utilidades.EnhancedJTextField txtMonto;

    private utilidades.EnhancedJTextField txtPais;

    private utilidades.EnhancedJTextField txtProvincia;

    private utilidades.EnhancedJTextField txtRNC;
}
