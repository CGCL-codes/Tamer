package frontend.cxp;

import eventos.IOrdenCompraListener;
import eventos.IProductoListener;
import eventos.IProveedorListener;
import eventos.OrdenCompraEvent;
import eventos.ProductoEvent;
import eventos.ProveedorEvent;
import frontend.proveedor.DgInsertarProducto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;
import mensaje_objetos.almacen.Producto;
import mensaje_objetos.almacen.ProductosDelProveedor;
import mensaje_objetos.proveedor.Proveedor;

/**
 *
 * @author  raymi
 */
public class DgCrearOrden extends javax.swing.JDialog implements IOrdenCompraListener, IProveedorListener, IProductoListener {

    private DgInsertarProducto dgInsertarProducto;

    private DgInsertarProveedor dgInsertarProveedor;

    private Proveedor proveedorSeleccionado;

    private ProductosDelProveedor productoSeleccionado;

    private Producto producto;

    private boolean buttonInsertar;

    private AbstractTableModel modeloTablaProducto;

    private String[] headers = { "Producto", "Descripcion", "Precio", "Cantidad" };

    private int accion;

    private List<ProductosDelProveedor> productoTable;

    private List<ProductosDelProveedor> listaProductos;

    /** Creates new form DgCrearOrden */
    public DgCrearOrden(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicializarComponentes();
    }

    public void inicializarComponentes() {
        dgInsertarProducto = new DgInsertarProducto(new JFrame(), true);
        dgInsertarProducto.addProductoListener(this);
        dgInsertarProveedor = new DgInsertarProveedor(new JFrame(), true);
        dgInsertarProveedor.addProveedorListener(this);
        inicializarTabla();
    }

    private void inicializarTabla() {
        modeloTablaProducto = new AbstractTableModel() {

            public int getColumnCount() {
                return headers.length;
            }

            public int getRowCount() {
                return productoTable.size();
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (productoTable.size() > 0) {
                    ProductosDelProveedor producto = productoTable.get(rowIndex);
                    if (producto != null) {
                        if (columnIndex == 0) {
                            return producto.getNombre();
                        }
                        if (columnIndex == 1) {
                            return producto.getDescripcion();
                        }
                        if (columnIndex == 2) {
                            return producto.getPrecio();
                        }
                        if (columnIndex == 3) {
                        }
                    }
                }
                return "";
            }

            public String getColumnName(int col) {
                return headers[col];
            }
        };
    }

    public void fireOrdenDeCompra(OrdenCompraEvent ov) {
        if (ov != null) {
            accion = ov.getId();
            if (ov.getId() == OrdenCompraEvent.NUEVO) {
                limpiarCamposProveedor();
                limpiarCamposProducto();
                inicializarTabla();
                listaProductos = new ArrayList<ProductosDelProveedor>();
                productoTable = new ArrayList<ProductosDelProveedor>();
            }
        }
    }

    public void limpiarCamposProveedor() {
        lblNombre.setText("");
        lblPorcMora.setText("");
        lblLimiteDeCredito.setText("");
        lblLimiteDeDias.setText("");
        lblRncCedula.setText("");
    }

    public void limpiarCamposProducto() {
        lblProducto.setText("");
        lblDescripcion.setText("");
        lblPrecio.setText("");
        txtCantidad.setText("");
    }

    public void fireProducto(ProductoEvent producto) {
        this.producto = producto.getProducto();
        productoSeleccionado = getSelectedProducto();
        if (productoSeleccionado != null) {
            if (producto != null) {
                if (!buttonInsertar) {
                    if (productoSeleccionado.getNombre() != null) {
                        lblNombre.setText(productoSeleccionado.getNombre());
                    }
                    if (productoSeleccionado.getDescripcion() != null) {
                        lblDescripcion.setText(productoSeleccionado.getDescripcion());
                    }
                    if (productoSeleccionado.getPrecio() != null) {
                        lblPrecio.setText("" + productoSeleccionado.getPrecio());
                    }
                } else {
                    List<ProductosDelProveedor> productos = proveedorSeleccionado.getProductos();
                    if (productos == null) {
                        productos = new ArrayList<ProductosDelProveedor>();
                        proveedorSeleccionado.setProductos(productos);
                    }
                    if (!productos.contains(productoSeleccionado)) productos.add(productoSeleccionado); else productos.set(productos.indexOf(productoSeleccionado), productoSeleccionado);
                    inicializarTabla();
                    limpiarCamposProducto();
                }
            }
        }
    }

    public ProductosDelProveedor getSelectedProducto() {
        ProductosDelProveedor producto = null;
        int arow = tableProductosSeleccionados.getSelectedRow();
        if (arow != -1) {
            int row = tableProductosSeleccionados.convertRowIndexToModel(arow);
            producto = productoTable.get(row);
        }
        return producto;
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnBuscarProveedor = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblRncCedula = new javax.swing.JLabel();
        lblLimiteDeCredito = new javax.swing.JLabel();
        lblLimiteDeDias = new javax.swing.JLabel();
        lblPorcMora = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnBuscarProducto = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblProducto = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        btnInsertarProducto = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        tableProductosSeleccionados = new org.jdesktop.swingx.JXTable();
        btnCerrar = new javax.swing.JButton();
        btnSalvarCerrar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        btnBuscarProveedor.setText("Buscar");
        btnBuscarProveedor.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProveedorActionPerformed(evt);
            }
        });
        jLabel3.setText("Tipo de pago:");
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Credito");
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Contado");
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos principales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(102, 153, 255)));
        jLabel1.setText("Nombre:");
        jLabel2.setText("Rnc/Cedula:");
        jLabel4.setText("Limite de Credito:");
        jLabel5.setText("Limite de Dias:");
        jLabel6.setText("Porcentaje de Mora:");
        lblNombre.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblNombre.setOpaque(true);
        lblRncCedula.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblRncCedula.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblRncCedula.setOpaque(true);
        lblLimiteDeCredito.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblLimiteDeCredito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblLimiteDeCredito.setOpaque(true);
        lblLimiteDeDias.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblLimiteDeDias.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblLimiteDeDias.setOpaque(true);
        lblPorcMora.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblPorcMora.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPorcMora.setOpaque(true);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblRncCedula)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblLimiteDeCredito)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblLimiteDeDias)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblPorcMora))).addContainerGap()));
        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jLabel1, jLabel2, jLabel4, jLabel5, jLabel6 });
        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { lblLimiteDeCredito, lblLimiteDeDias, lblNombre, lblPorcMora, lblRncCedula });
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(39, 39, 39).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblNombre)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(lblRncCedula)).addGap(6, 6, 6).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(lblLimiteDeCredito)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(lblLimiteDeDias)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(lblPorcMora)).addContainerGap(53, Short.MAX_VALUE)));
        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { jLabel1, jLabel2, jLabel4, jLabel5, jLabel6, lblLimiteDeCredito, lblLimiteDeDias, lblNombre, lblPorcMora, lblRncCedula });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnBuscarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel3).addGap(86, 86, 86).addComponent(jRadioButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton2))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(27, 27, 27).addComponent(btnBuscarProveedor).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jRadioButton1).addComponent(jRadioButton2)).addGap(73, 73, 73)));
        jTabbedPane1.addTab("Proveedor", jPanel1);
        btnBuscarProducto.setText("Buscar");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos principales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(102, 102, 255)));
        jLabel13.setText("Producto: ");
        jLabel14.setText("Descripcion:");
        jLabel15.setText("Precio:");
        lblProducto.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblProducto.setOpaque(true);
        lblDescripcion.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblDescripcion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblDescripcion.setOpaque(true);
        lblPrecio.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        lblPrecio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPrecio.setOpaque(true);
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel14).addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblDescripcion).addComponent(lblProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))).addGroup(jPanel4Layout.createSequentialGroup().addComponent(jLabel15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { lblDescripcion, lblPrecio, lblProducto });
        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jLabel13, jLabel14, jLabel15 });
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel13).addComponent(lblProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel14).addComponent(lblDescripcion)).addGap(8, 8, 8).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(lblPrecio)).addContainerGap(29, Short.MAX_VALUE)));
        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { lblDescripcion, lblPrecio, lblProducto });
        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { jLabel13, jLabel14, jLabel15 });
        jLabel19.setText("Cantidad:");
        btnInsertarProducto.setText("Insertar");
        btnInsertarProducto.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarProductoActionPerformed(evt);
            }
        });
        tableProductosSeleccionados.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Producto", "Descripcion", "Precio", "Cantidad" }) {

            boolean[] canEdit = new boolean[] { false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane.setViewportView(tableProductosSeleccionados);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnInsertarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(44, 44, 44).addComponent(btnBuscarProducto).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel19).addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnInsertarProducto)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE).addContainerGap()));
        jTabbedPane1.addTab("Productos", jPanel2);
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        btnSalvarCerrar.setText("Salvar ->");
        btnSalvar.setText("Salvar");
        btnNuevo.setText("Nuevo");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(btnNuevo).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSalvar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSalvarCerrar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCerrar).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE).addGap(12, 12, 12)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnCerrar).addComponent(btnSalvarCerrar).addComponent(btnSalvar).addComponent(btnNuevo)).addContainerGap()));
        pack();
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void btnInsertarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        if (producto != null) {
            buttonInsertar = true;
            ProductoEvent pv = new ProductoEvent();
            pv.setId(ProductoEvent.NUEVO);
            pv.setProducto(producto);
            fireProducto(pv);
        }
    }

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        buttonInsertar = false;
        ProductoEvent pv = new ProductoEvent();
        pv.setId(ProductoEvent.PROVEEDOR);
        pv.setProveedor(proveedorSeleccionado);
        dgInsertarProducto.fireProducto(pv);
        dgInsertarProducto.setVisible(true);
    }

    private void btnBuscarProveedorActionPerformed(java.awt.event.ActionEvent evt) {
        ProveedorEvent pv = new ProveedorEvent();
        pv.setId(ProveedorEvent.NUEVO);
        dgInsertarProveedor.fireProveedor(pv);
        dgInsertarProveedor.setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DgCrearOrden(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    public void fireProveedor(ProveedorEvent proveedor) {
        proveedorSeleccionado = proveedor.getProveedor();
        lblNombre.setText(proveedorSeleccionado.getNombre());
        if (proveedorSeleccionado.getRnc() != null && !proveedorSeleccionado.getRnc().equals("")) lblRncCedula.setText(proveedorSeleccionado.getRnc()); else lblRncCedula.setText(proveedorSeleccionado.getCedula());
        lblLimiteDeCredito.setText("" + proveedorSeleccionado.getLimiteDeCredito());
        lblLimiteDeDias.setText("" + proveedorSeleccionado.getLimiteDias());
        lblPorcMora.setText("" + proveedorSeleccionado.getPorcMora());
    }

    private javax.swing.JButton btnBuscarProducto;

    private javax.swing.JButton btnBuscarProveedor;

    private javax.swing.JButton btnCerrar;

    private javax.swing.JButton btnInsertarProducto;

    private javax.swing.JButton btnNuevo;

    private javax.swing.JButton btnSalvar;

    private javax.swing.JButton btnSalvarCerrar;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JScrollPane jScrollPane;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JLabel lblDescripcion;

    private javax.swing.JLabel lblLimiteDeCredito;

    private javax.swing.JLabel lblLimiteDeDias;

    private javax.swing.JLabel lblNombre;

    private javax.swing.JLabel lblPorcMora;

    private javax.swing.JLabel lblPrecio;

    private javax.swing.JLabel lblProducto;

    private javax.swing.JLabel lblRncCedula;

    private org.jdesktop.swingx.JXTable tableProductosSeleccionados;

    private javax.swing.JTextField txtCantidad;
}
