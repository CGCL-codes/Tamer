package frontend.almacen;

import eventos.GrupoProductoEvent;
import eventos.IGrupoProductoListener;
import eventos.IItbisListener;
import eventos.IProductoListener;
import eventos.IProductoStock;
import eventos.ItbisEvent;
import eventos.ProductoEvent;
import eventos.ProductoStockEvent;
import frontend.facturacion.DgAdministrarItbis;
import frontend.facturacion.DgCrearItbis;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mensaje_objetos.almacen.GrupoProducto;
import mensaje_objetos.almacen.PrecioCiudad;
import mensaje_objetos.almacen.PrecioCliente;
import mensaje_objetos.almacen.PrecioProvincia;
import mensaje_objetos.almacen.PrecioRegion;
import mensaje_objetos.almacen.Producto;
import mensaje_objetos.almacen.ProductoStock;
import mensaje_objetos.cliente.Ciudad;
import mensaje_objetos.cliente.Cliente;
import mensaje_objetos.cliente.Provincia;
import mensaje_objetos.cliente.Region;
import mensaje_objetos.facturacion.Itbis;
import modelo_objetos.almacen.ModeloProducto;
import modelo_objetos.cliente.ModeloCiudad;
import modelo_objetos.cliente.ModeloCliente;
import modelo_objetos.cliente.ModeloProvincia;
import modelo_objetos.cliente.ModeloRegion;
import utilidades.IObserver;
import utilidades.Screen;
import utilidades.SubjectHelper;

/**
 *
 * @author  carlos
 */
public class DgArticulo extends javax.swing.JDialog implements IObserver, IProductoListener, IGrupoProductoListener, IItbisListener, IProductoStock {

    private ModeloProducto modeloProducto;

    private ModeloRegion modeloRegion;

    private ModeloCiudad modeloCiudad;

    private ModeloProvincia modeloProvincia;

    private ModeloCliente modeloCliente;

    private Producto productoSeleccionado;

    private Producto productoNuevo;

    private Producto productoEditar;

    private List<PrecioCiudad> listaPrecioCiudad;

    private List<PrecioProvincia> listaPrecioProvincia;

    private List<PrecioRegion> listaPrecioRegion;

    private List<PrecioCliente> listaPrecioCliente;

    private DgGrupoProducto dgGrupo;

    private GrupoProducto grupoSeleccionado;

    private int accion;

    private Cliente clienteSeleccionado;

    private List<Region> regiones;

    private List<Ciudad> ciudades;

    private List<Provincia> provincias;

    private Itbis itbisSeleccionado;

    private File imagen;

    private JFileChooser dgChose;

    /** Creates new form DgArticulo */
    public DgArticulo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        modeloProvincia = ModeloProvincia.getInstance();
        modeloRegion = ModeloRegion.getInstance();
        modeloProducto = ModeloProducto.getInstance();
        modeloCiudad = ModeloCiudad.getInstance();
        dgChose = new JFileChooser();
        dgChose.setDialogType(JFileChooser.OPEN_DIALOG);
        Screen sc = new Screen(this);
        sc.centrarPantalla();
        addListener();
        inicializarCombo();
    }

    private void inicializarCombo() {
        regiones = modeloRegion.getAllRegion();
        ciudades = modeloCiudad.getAllCiudad();
        provincias = modeloProvincia.getAllProvincia();
        cmbProvincia.addItem("Seleccionar");
        for (Provincia i : provincias) {
            cmbProvincia.addItem(i.getNombre());
        }
        cmbRegion.addItem("Seleccionar");
        for (Region i : regiones) {
            cmbRegion.addItem(i.getDescripcion());
        }
        cmbCiudad.addItem("Seleccionar");
        for (Ciudad i : ciudades) {
            cmbCiudad.addItem(i.getNombre());
        }
    }

    /**
     *Metodo para subscribirme a los observadores
     */
    private void addListener() {
        SubjectHelper subAdd = modeloProducto.getSubjectAdd();
        SubjectHelper subRemove = modeloProducto.getSubjectRemove();
        SubjectHelper subUpdate = modeloProducto.getSubjectUpdate();
        SubjectHelper subMensaje = modeloProducto.getSubjectMensaje();
        subAdd.addObserver(this);
        subRemove.addObserver(this);
        subUpdate.addObserver(this);
        subMensaje.addObserver(this);
        pnTablaProductoStock1.addObservador(this);
    }

    private Provincia getSelectedProvincia() {
        String item = (String) cmbProvincia.getSelectedItem();
        for (Provincia i : provincias) {
            if (item.equals(i.getNombre())) {
                return i;
            }
        }
        return null;
    }

    private Region getSelectedRegion() {
        String item = (String) cmbRegion.getSelectedItem();
        for (Region i : regiones) {
            if (item.equals(i.getDescripcion())) {
                return i;
            }
        }
        return null;
    }

    private Ciudad getSelectedCiudad() {
        String item = (String) cmbCiudad.getSelectedItem();
        for (Ciudad i : ciudades) {
            if (item.equals(i.getNombre())) {
                return i;
            }
        }
        return null;
    }

    private void execute() {
        if (accion == ProductoEvent.NUEVO) {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            String codigo = txtCodigo.getText();
            String precioGeneral = txtPrecioGeneral.getText();
            String precioGeneralItbis = txtPrecioItbis.getText();
            String stockMin = stock_min.getText();
            String stockMax = stock_max.getText();
            BigDecimal precio = null;
            BigDecimal precioItbis = null;
            BigDecimal min_stock = null;
            BigDecimal max_stock = null;
            boolean sin_stock = this.sin_stock.isSelected();
            if (precioGeneral.equals("")) {
                precio = new BigDecimal(0);
            } else {
                precio = new BigDecimal(precioGeneral);
            }
            if (precioGeneral.equals("")) {
                precioItbis = new BigDecimal(0);
            } else {
                precioItbis = new BigDecimal(precioGeneralItbis);
            }
            if (!stockMin.equals("")) {
                min_stock = new BigDecimal(stockMin);
            } else {
                min_stock = new BigDecimal("0");
            }
            if (!stockMax.equals("")) {
                max_stock = new BigDecimal(stockMax);
            } else {
                max_stock = new BigDecimal("0");
            }
            if (!nombre.equals("") && !codigo.equals("")) {
                productoNuevo.setNombre(nombre);
                productoNuevo.setDescripcion(descripcion);
                productoNuevo.setCodigo(codigo);
                productoNuevo.setGrupo(grupoSeleccionado);
                productoNuevo.setPrecio(precio);
                productoNuevo.setPrecioItbis(precioItbis);
                productoNuevo.setPreciosCiudad(listaPrecioCiudad);
                productoNuevo.setPreciosCliente(listaPrecioCliente);
                productoNuevo.setPreciosProvincia(listaPrecioProvincia);
                productoNuevo.setPreciosRegion(listaPrecioRegion);
                productoNuevo.setItbis(itbisSeleccionado);
                productoNuevo.setVentaSinStock(sin_stock);
                productoNuevo.setStockMaximo(max_stock);
                productoNuevo.setStockMinimo(min_stock);
                productoNuevo.setStockFisico(new BigDecimal("0"));
                modeloProducto.addProducto(productoNuevo);
            } else {
                setWarning("Debes completar los campos correctamente");
            }
        }
        if (accion == ProductoEvent.EDITAR) {
            if (productoEditar != null) {
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();
                String codigo = txtCodigo.getText();
                String precioGeneral = txtPrecioGeneral.getText();
                String precioGeneralItbis = txtPrecioItbis.getText();
                String stockMin = stock_min.getText();
                String stockMax = stock_max.getText();
                String stockFisico = stock_fisico.getText();
                BigDecimal precio = null;
                BigDecimal precioItbis = null;
                BigDecimal min_stock = null;
                BigDecimal max_stock = null;
                BigDecimal fisico_stock = null;
                boolean sin_stock = this.sin_stock.isSelected();
                if (precioGeneral.equals("")) {
                    precio = new BigDecimal(0);
                } else {
                    precio = new BigDecimal(precioGeneral);
                }
                if (!stockMin.equals("")) {
                    min_stock = new BigDecimal(stockMin);
                } else {
                    min_stock = new BigDecimal("0");
                }
                if (!stockMax.equals("")) {
                    max_stock = new BigDecimal(stockMax);
                } else {
                    max_stock = new BigDecimal("0");
                }
                if (!stockFisico.equals("")) {
                    fisico_stock = new BigDecimal(stockFisico);
                } else {
                    fisico_stock = new BigDecimal("0");
                }
                if (precioGeneral.equals("")) {
                    precioItbis = new BigDecimal(0);
                } else {
                    precioItbis = new BigDecimal(precioGeneralItbis);
                }
                for (int k = 0; k < listaPrecioCiudad.size(); k++) {
                    System.out.println("Ciudad " + listaPrecioCiudad.get(k).getCiudad().getCodigo());
                }
                productoEditar.setDescripcion(txtDescripcion.getText());
                productoEditar.setNombre(txtNombre.getText());
                productoEditar.setNombre(nombre);
                productoEditar.setDescripcion(descripcion);
                productoEditar.setCodigo(codigo);
                productoEditar.setGrupo(grupoSeleccionado);
                productoEditar.setPrecioItbis(precioItbis);
                productoEditar.setPrecio(precio);
                productoEditar.setPreciosCiudad(listaPrecioCiudad);
                productoEditar.setPreciosCliente(listaPrecioCliente);
                productoEditar.setPreciosProvincia(listaPrecioProvincia);
                productoEditar.setPreciosRegion(listaPrecioRegion);
                productoEditar.setItbis(itbisSeleccionado);
                productoEditar.setVentaSinStock(sin_stock);
                productoEditar.setStockMaximo(max_stock);
                productoEditar.setStockMinimo(min_stock);
                productoEditar.setStockFisico(fisico_stock);
                productoEditar.setListaStockProducto(pnTablaProductoStock1.getListaProductoStock());
                modeloProducto.updateProducto(productoEditar);
            }
        }
    }

    public void setWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    /**
     *metodo para limpiar la informacion.
     */
    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtGrupo.setText("");
        txtItbis.setText("");
        txtNombre.setText("");
        txtPrecioGeneral.setText("");
        txtPrecioItbis.setText("");
        stock_fisico.setText("");
        stock_max.setText("");
        stock_min.setText("");
    }

    private void habilitarCampos(boolean des) {
        txtCodigo.setEnabled(des);
        txtDescripcion.setEnabled(des);
        txtGrupo.setEnabled(des);
        txtItbis.setEnabled(des);
        txtNombre.setEnabled(des);
        txtPrecioGeneral.setEnabled(des);
        txtPrecioItbis.setEnabled(des);
        btnBuscar.setEnabled(des);
        btnCliente.setEnabled(des);
        btnImagen.setEnabled(des);
        btnInsertarPrecioCliente.setEnabled(des);
        btnInsertarPrecioICiudad.setEnabled(des);
        btnInsertarPrecioProvincia.setEnabled(des);
        btnInsertarPrecioRegion.setEnabled(des);
        btnItbis.setEnabled(des);
        btnNuevo.setEnabled(des);
        btnSalvar.setEnabled(des);
        btnSalvarCerrar.setEnabled(des);
        cmbCiudad.setEnabled(des);
        cmbProvincia.setEnabled(des);
        cmbRegion.setEnabled(des);
        stock_max.setEnabled(des);
        stock_min.setEnabled(des);
    }

    public void setProducto(Producto producto) {
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        if (producto.getGrupo() != null) {
            txtGrupo.setText(producto.getGrupo().getNombre());
        }
        txtDescripcion.setText(producto.getDescripcion());
        txtPrecioGeneral.setText(producto.getPrecio() + "");
        double itbis = 0;
        try {
            itbis = producto.getItbis().getEquivalencia().doubleValue();
        } catch (NullPointerException e) {
            itbis = 0.0;
        }
        double precioItbis = producto.getPrecio().doubleValue() + producto.getPrecio().doubleValue() * itbis;
        txtPrecioItbis.setText(precioItbis + "");
        txtItbis.setText(itbis + "");
        modeloProducto.getImage(productoSeleccionado);
        if (productoSeleccionado.getImagen() != null) {
            ImageIcon imagen = new ImageIcon(modeloProducto.getImage(producto));
            lblImagen.setIcon(imagen);
        } else {
            lblImagen.setIcon(null);
        }
        stock_min.setText("" + producto.getStockMinimo());
        stock_max.setText("" + producto.getStockMaximo());
        stock_fisico.setText("" + producto.getStockFisico());
        sin_stock.setSelected(producto.isVentaSinStock());
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        txtCodigo = new utilidades.EnhancedJTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new utilidades.EnhancedJTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtGrupo = new utilidades.EnhancedJTextField();
        jPanel3 = new javax.swing.JPanel();
        btnImagen = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblImagen = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        sin_stock = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtPrecioGeneral = new utilidades.EnhancedJTextField();
        btnItbis = new javax.swing.JButton();
        txtItbis = new utilidades.EnhancedJTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPrecioItbis = new utilidades.EnhancedJTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbProvincia = new javax.swing.JComboBox();
        cmbCiudad = new javax.swing.JComboBox();
        cmbRegion = new javax.swing.JComboBox();
        txtPrecioRegion = new utilidades.EnhancedJTextField();
        txtPrecioProvincia = new utilidades.EnhancedJTextField();
        txtPrecioCiudad = new utilidades.EnhancedJTextField();
        txtCliente = new utilidades.EnhancedJTextField();
        jLabel11 = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        btnCliente = new javax.swing.JButton();
        txtPrecioCliente = new utilidades.EnhancedJTextField();
        btnInsertarPrecioRegion = new javax.swing.JButton();
        btnInsertarPrecioProvincia = new javax.swing.JButton();
        btnInsertarPrecioICiudad = new javax.swing.JButton();
        btnInsertarPrecioCliente = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        stock_fisico = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        stock_min = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        stock_max = new javax.swing.JTextField();
        pnTablaProductoStock1 = new frontend.almacen.PnTablaProductoStock();
        btnCerrar = new javax.swing.JButton();
        btnSalvarCerrar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtDescripcion = new utilidades.EnhancedJTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear Productos");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText("Codigo");
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Nombre");
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("Grupo Producto");
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("ComboBox.selectionBackground")));
        btnImagen.setFont(new java.awt.Font("Dialog", 0, 12));
        btnImagen.setMnemonic('I');
        btnImagen.setText("Imagen");
        btnImagen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImagenActionPerformed(evt);
            }
        });
        btnBorrar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnBorrar.setMnemonic('B');
        btnBorrar.setText("Borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        jScrollPane1.setViewportView(lblImagen);
        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3Layout.createSequentialGroup().add(btnImagen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnBorrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnImagen).add(btnBorrar)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE).addContainerGap()));
        btnBuscar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnBuscar.setText("Buscar...");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        sin_stock.setFont(new java.awt.Font("Dialog", 0, 12));
        sin_stock.setSelected(true);
        sin_stock.setText("Permitir Ventas Sin Stock");
        sin_stock.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sin_stock.setMargin(new java.awt.Insets(0, 0, 0, 0));
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel1Layout.createSequentialGroup().add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtGrupo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnBuscar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(sin_stock, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(txtGrupo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnBuscar).add(sin_stock)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        jTabbedPane1.addTab("General", jPanel1);
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("ComboBox.selectionBackground")));
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText("Precio General");
        btnItbis.setFont(new java.awt.Font("Dialog", 0, 12));
        btnItbis.setMnemonic('I');
        btnItbis.setText("Itbis");
        btnItbis.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItbisActionPerformed(evt);
            }
        });
        txtItbis.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItbisActionPerformed(evt);
            }
        });
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setText("Precio Itbis");
        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().addContainerGap().add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(txtPrecioItbis, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtPrecioGeneral, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnItbis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(16, 16, 16).add(txtItbis, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().addContainerGap().add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(txtPrecioGeneral, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(txtItbis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnItbis)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(txtPrecioItbis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Precio Region");
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Precio Provincia");
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Precio Ciudad");
        cmbProvincia.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProvinciaItemStateChanged(evt);
            }
        });
        cmbCiudad.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCiudadItemStateChanged(evt);
            }
        });
        cmbRegion.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbRegionItemStateChanged(evt);
            }
        });
        cmbRegion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRegionActionPerformed(evt);
            }
        });
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setText("Fecha");
        btnCliente.setFont(new java.awt.Font("Dialog", 0, 12));
        btnCliente.setMnemonic('C');
        btnCliente.setText("Cliente");
        btnInsertarPrecioRegion.setFont(new java.awt.Font("Dialog", 0, 12));
        btnInsertarPrecioRegion.setText("Insertar +");
        btnInsertarPrecioRegion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarPrecioRegionActionPerformed(evt);
            }
        });
        btnInsertarPrecioProvincia.setText("Insertar +");
        btnInsertarPrecioProvincia.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarPrecioProvinciaActionPerformed(evt);
            }
        });
        btnInsertarPrecioICiudad.setFont(new java.awt.Font("Dialog", 0, 12));
        btnInsertarPrecioICiudad.setText("Insertar +");
        btnInsertarPrecioICiudad.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarPrecioICiudadActionPerformed(evt);
            }
        });
        btnInsertarPrecioCliente.setFont(new java.awt.Font("Dialog", 0, 12));
        btnInsertarPrecioCliente.setText("Insertar +");
        btnInsertarPrecioCliente.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarPrecioClienteActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel2Layout.createSequentialGroup().add(jLabel11).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lblFecha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 209, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(43, 43, 43).add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)).add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, btnCliente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(14, 14, 14).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(cmbProvincia, 0, 134, Short.MAX_VALUE).add(txtCliente, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, cmbCiudad, 0, 134, Short.MAX_VALUE).add(cmbRegion, 0, 134, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(txtPrecioCliente, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtPrecioCiudad, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtPrecioProvincia, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtPrecioRegion, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(btnInsertarPrecioICiudad, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(btnInsertarPrecioCliente, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, btnInsertarPrecioProvincia, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, btnInsertarPrecioRegion, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(4, 4, 4).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel11).add(lblFecha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(23, 23, 23).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel7).add(cmbRegion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(txtPrecioRegion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnInsertarPrecioRegion)).add(65, 65, 65)).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(cmbProvincia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(txtPrecioProvincia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnInsertarPrecioProvincia)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmbCiudad, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel9).add(txtPrecioCiudad, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnInsertarPrecioICiudad, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))).add(31, 31, 31).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(txtCliente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnCliente).add(txtPrecioCliente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnInsertarPrecioCliente)).addContainerGap(69, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Precios", jPanel2);
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setText("Stock Fisico:");
        stock_fisico.setEnabled(false);
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel12.setText("Stock Minino:");
        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel13.setText("Stock Maximo:");
        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel5Layout.createSequentialGroup().addContainerGap().add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(pnTablaProductoStock1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE).add(jPanel5Layout.createSequentialGroup().add(jLabel10).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(stock_fisico, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel12).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(stock_min, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel13).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(stock_max, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))).addContainerGap()));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel5Layout.createSequentialGroup().addContainerGap().add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel10).add(stock_fisico, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel12).add(stock_min, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel13).add(stock_max, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(pnTablaProductoStock1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Stock", jPanel5);
        btnCerrar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnCerrar.setMnemonic('C');
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        btnSalvarCerrar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnSalvarCerrar.setMnemonic('a');
        btnSalvarCerrar.setText("Salvar ->");
        btnSalvarCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarCerrarActionPerformed(evt);
            }
        });
        btnSalvar.setFont(new java.awt.Font("Dialog", 0, 12));
        btnSalvar.setMnemonic('S');
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        btnNuevo.setFont(new java.awt.Font("Dialog", 0, 12));
        btnNuevo.setMnemonic('N');
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setText("Descripcion");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE).add(layout.createSequentialGroup().add(btnNuevo).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnSalvar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnSalvarCerrar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnCerrar)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(txtDescripcion, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(txtCodigo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtNombre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(txtNombre, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1).add(txtCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(txtDescripcion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4)).add(10, 10, 10).add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnSalvarCerrar).add(btnSalvar).add(btnNuevo).add(btnCerrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        pack();
    }

    private void txtItbisActionPerformed(java.awt.event.ActionEvent evt) {
        if (itbisSeleccionado != null) {
            txtItbis.setText(itbisSeleccionado.getEquivalencia() + "");
            BigDecimal precio = null;
            try {
                String precio1 = txtPrecioGeneral.getText();
                precio = new BigDecimal(precio1);
            } catch (NumberFormatException ne) {
                precio = new BigDecimal(0.0);
            }
            double precioItbis = precio.doubleValue() + precio.doubleValue() * itbisSeleccionado.getEquivalencia().doubleValue();
            txtPrecioItbis.setText(precioItbis + "");
        }
    }

    private void cmbCiudadItemStateChanged(java.awt.event.ItemEvent evt) {
        String item = (String) cmbCiudad.getSelectedItem();
        if (cmbCiudad.getSelectedIndex() != 0) {
            Ciudad ciudad = getSelectedCiudad();
            if (productoSeleccionado != null && ciudad != null) {
                PrecioCiudad precio = modeloProducto.getPrecioCiudad(productoSeleccionado, ciudad);
                if (precio != null) {
                    if (precio.getPrecio() != null) {
                        txtPrecioCiudad.setText(precio.getPrecio() + "");
                    } else {
                        txtPrecioCiudad.setText("");
                    }
                }
            }
        } else {
            txtPrecioCiudad.setText("");
        }
    }

    private void cmbProvinciaItemStateChanged(java.awt.event.ItemEvent evt) {
        String item = (String) cmbProvincia.getSelectedItem();
        if (cmbProvincia.getSelectedIndex() != 0) {
            Provincia provincia = getSelectedProvincia();
            if (productoSeleccionado != null && provincia != null) {
                PrecioProvincia precio = modeloProducto.getPrecioProvincia(productoSeleccionado, provincia);
                if (precio != null) {
                    if (precio.getPrecio() != null) {
                        txtPrecioProvincia.setText(precio.getPrecio() + "");
                    } else {
                        txtPrecioProvincia.setText("");
                    }
                }
            }
        } else {
            txtPrecioProvincia.setText("");
        }
    }

    private void cmbRegionItemStateChanged(java.awt.event.ItemEvent evt) {
        String item = (String) cmbRegion.getSelectedItem();
        if (cmbRegion.getSelectedIndex() != 0) {
            Region region = getSelectedRegion();
            PrecioRegion precio = modeloProducto.getPrecioRegion(productoSeleccionado, region);
            if (precio != null) {
                if (precio.getPrecio() != null) {
                    txtPrecioRegion.setText(precio.getPrecio() + "");
                } else {
                    txtPrecioRegion.setText("");
                }
            }
        } else {
            txtPrecioRegion.setText("");
        }
    }

    private void cmbRegionActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        modeloProducto.getFechaDataBase();
        dgGrupo = new DgGrupoProducto();
        dgGrupo.getBtnSeleccionar().setVisible(true);
        dgGrupo.addGrupoProductoListener(this);
        dgGrupo.setVisible(true);
    }

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (accion == ProductoEvent.EDITAR) {
            modeloProducto.removeImage(productoEditar);
            lblImagen.setIcon(null);
        }
    }

    private void btnImagenActionPerformed(java.awt.event.ActionEvent evt) {
        dgChose.showOpenDialog(this);
        imagen = dgChose.getSelectedFile();
        if (accion == ProductoEvent.NUEVO) {
            productoNuevo.setImagen(imagen);
        }
        if (accion == ProductoEvent.EDITAR) {
            productoEditar.setImagen(imagen);
        }
        if (imagen != null) {
            ImageIcon image = new ImageIcon(imagen.getPath());
            lblImagen.setIcon(image);
        } else {
            lblImagen.setIcon(null);
        }
    }

    private void btnItbisActionPerformed(java.awt.event.ActionEvent evt) {
        DgAdministrarItbis dgItbis = new DgAdministrarItbis();
        dgItbis.addItbisListener(this);
        dgItbis.getBtnSeleccionar().setVisible(true);
        dgItbis.setVisible(true);
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
    }

    private void btnInsertarPrecioClienteActionPerformed(java.awt.event.ActionEvent evt) {
        String precioGeneral = txtPrecioGeneral.getText();
        BigDecimal precio = null;
        if (precioGeneral.equals("")) {
            precio = new BigDecimal(0);
        } else {
            precio = new BigDecimal(precioGeneral);
        }
        if (clienteSeleccionado != null) {
            String codigo = txtCodigo.getText();
            Producto product = null;
            if (accion == ProductoEvent.NUEVO) {
                product = productoNuevo;
                listaPrecioCliente = new ArrayList();
            }
            if (accion == ProductoEvent.EDITAR) {
                product = productoSeleccionado;
                listaPrecioCliente = new ArrayList();
            }
            product.setCodigo(codigo);
            if (!codigo.equals("")) {
                PrecioCliente pr = new PrecioCliente();
                pr.setActivo(1);
                pr.setCliente(clienteSeleccionado);
                pr.setFecha(new Date());
                pr.setPrecio(precio);
                pr.setProducto(product);
                Date fecha = modeloProducto.getFechaDataBase();
                Date hora = modeloProducto.getHoraDataBase();
                pr.setFecha(fecha);
                pr.setHora(hora);
                listaPrecioCliente.add(pr);
                txtPrecioGeneral.setText("");
                txtCliente.setText("");
            } else {
                setWarning("Debes digitar un codigo para el producto");
            }
        }
    }

    private void btnInsertarPrecioICiudadActionPerformed(java.awt.event.ActionEvent evt) {
        String precioCiudad = txtPrecioCiudad.getText();
        Ciudad prov = getSelectedCiudad();
        BigDecimal precio = null;
        if (precioCiudad.equals("")) {
            precio = new BigDecimal(0);
        } else {
            precio = new BigDecimal(precioCiudad);
        }
        if (prov != null) {
            String codigo = txtCodigo.getText();
            Producto product = null;
            if (accion == ProductoEvent.NUEVO) {
                product = productoNuevo;
            }
            if (accion == ProductoEvent.EDITAR) {
                product = productoSeleccionado;
            }
            product.setCodigo(codigo);
            if (!codigo.equals("")) {
                PrecioCiudad pr = new PrecioCiudad();
                pr.setActivo(1);
                pr.setCiudad(prov);
                pr.setFecha(new Date());
                pr.setPrecio(precio);
                pr.setProducto(product);
                Date fecha = modeloProducto.getFechaDataBase();
                Date hora = modeloProducto.getHoraDataBase();
                pr.setFecha(fecha);
                pr.setHora(hora);
                listaPrecioCiudad.add(pr);
                txtPrecioCiudad.setText("");
                cmbCiudad.setSelectedIndex(0);
            } else {
                setWarning("Debes digitar un codigo para el producto");
            }
        } else {
            setWarning("Debes Elejir una Ciudad");
        }
    }

    private void btnInsertarPrecioProvinciaActionPerformed(java.awt.event.ActionEvent evt) {
        String precioProvincia = txtPrecioProvincia.getText();
        Provincia prov = getSelectedProvincia();
        BigDecimal precio = null;
        if (precioProvincia.equals("")) {
            precio = new BigDecimal(0);
        } else {
            precio = new BigDecimal(precioProvincia);
        }
        if (prov != null) {
            String codigo = txtCodigo.getText();
            Producto product = null;
            if (accion == ProductoEvent.NUEVO) {
                product = productoNuevo;
            }
            if (accion == ProductoEvent.EDITAR) {
                product = productoSeleccionado;
            }
            product.setCodigo(codigo);
            if (!codigo.equals("")) {
                PrecioProvincia pr = new PrecioProvincia();
                pr.setActivo(1);
                pr.setProvincia(prov);
                pr.setFecha(new Date());
                pr.setPrecio(precio);
                pr.setProducto(product);
                Date fecha = modeloProducto.getFechaDataBase();
                Date hora = modeloProducto.getHoraDataBase();
                pr.setFecha(fecha);
                pr.setHora(hora);
                listaPrecioProvincia.add(pr);
                txtPrecioProvincia.setText("");
                cmbProvincia.setSelectedIndex(0);
            } else {
                setWarning("Debes digitar un codigo para el producto");
            }
        } else {
            setWarning("Debes Elejir una provincia");
        }
    }

    private void btnInsertarPrecioRegionActionPerformed(java.awt.event.ActionEvent evt) {
        String precioRegion = txtPrecioRegion.getText();
        Region reg = getSelectedRegion();
        BigDecimal precio = null;
        if (precioRegion.equals("")) {
            precio = new BigDecimal(0);
        } else {
            precio = new BigDecimal(precioRegion);
        }
        if (reg != null) {
            String codigo = txtCodigo.getText();
            Producto product = null;
            if (accion == ProductoEvent.NUEVO) {
                product = productoNuevo;
                listaPrecioRegion = new ArrayList();
            }
            if (accion == ProductoEvent.EDITAR) {
                product = productoSeleccionado;
                listaPrecioRegion = new ArrayList();
            }
            product.setCodigo(codigo);
            if (!codigo.equals("")) {
                PrecioRegion pr = new PrecioRegion();
                pr.setActivo(1);
                pr.setRegion(reg);
                pr.setFecha(new Date());
                pr.setPrecio(precio);
                pr.setProducto(product);
                Date fecha = modeloProducto.getFechaDataBase();
                Date hora = modeloProducto.getHoraDataBase();
                pr.setFecha(fecha);
                pr.setHora(hora);
                listaPrecioRegion.add(pr);
                txtPrecioRegion.setText("");
                cmbRegion.setSelectedIndex(0);
            } else {
                setWarning("Debes digitar un codigo para el producto");
            }
        } else {
            setWarning("Debes Elejir una Region");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DgArticulo(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    /**
     *Para los observadores.
     */
    public void update(Class clase, Object argumento, String mensaje) {
    }

    /**
     *Metodo para comunicarme.
     */
    public void fireProducto(ProductoEvent producto) {
        if (producto != null) {
            if (producto.getId() == ProductoEvent.NUEVO) {
                accion = ProductoEvent.NUEVO;
                limpiarCampos();
                productoNuevo = new Producto();
                listaPrecioCiudad = new ArrayList();
                listaPrecioCliente = new ArrayList();
                listaPrecioProvincia = new ArrayList();
                listaPrecioRegion = new ArrayList();
            } else if (producto.getId() == ProductoEvent.EDITAR) {
                accion = ProductoEvent.EDITAR;
                limpiarCampos();
                productoSeleccionado = producto.getProducto();
                listaPrecioRegion = new ArrayList();
                listaPrecioProvincia = new ArrayList();
                listaPrecioCiudad = new ArrayList();
                listaPrecioCliente = new ArrayList();
                productoEditar = productoSeleccionado;
                setProducto(productoSeleccionado);
                habilitarCampos(true);
            } else if (producto.getId() == ProductoEvent.VISUALIZAR) {
                accion = ProductoEvent.VISUALIZAR;
                productoSeleccionado = producto.getProducto();
                listaPrecioRegion = new ArrayList();
                listaPrecioProvincia = new ArrayList();
                listaPrecioCiudad = new ArrayList();
                listaPrecioCliente = new ArrayList();
                setProducto(productoSeleccionado);
                habilitarCampos(false);
            }
            pnTablaProductoStock1.fireProducto(producto);
        }
    }

    public void fireGrupo(GrupoProductoEvent grupo) {
        if (grupo != null) {
            grupoSeleccionado = grupo.getGrupo();
            txtGrupo.setText(grupoSeleccionado.getNombre());
        }
    }

    public void fireItbis(ItbisEvent event) {
        if (event != null) {
            if (event.getItbis() != null) {
                itbisSeleccionado = event.getItbis();
                txtItbis.setText(itbisSeleccionado.getEquivalencia() + "");
                BigDecimal precio = null;
                try {
                    String precio1 = txtPrecioGeneral.getText();
                    precio = new BigDecimal(precio1);
                } catch (NumberFormatException ne) {
                    precio = new BigDecimal(0.0);
                }
                double precioItbis = precio.doubleValue() + precio.doubleValue() * itbisSeleccionado.getEquivalencia().doubleValue();
                txtPrecioItbis.setText(precioItbis + "");
            }
        }
    }

    public void fireProductoStock(ProductoStockEvent e) {
        if (e != null) {
            List<ProductoStock> lista = pnTablaProductoStock1.getListaProductoStock();
            int totalStock = 0;
            for (ProductoStock tmp : lista) {
                totalStock += tmp.getCantidad();
            }
            stock_fisico.setText("" + totalStock);
        }
    }

    private javax.swing.JButton btnBorrar;

    private javax.swing.JButton btnBuscar;

    private javax.swing.JButton btnCerrar;

    private javax.swing.JButton btnCliente;

    private javax.swing.JButton btnImagen;

    private javax.swing.JButton btnInsertarPrecioCliente;

    private javax.swing.JButton btnInsertarPrecioICiudad;

    private javax.swing.JButton btnInsertarPrecioProvincia;

    private javax.swing.JButton btnInsertarPrecioRegion;

    private javax.swing.JButton btnItbis;

    private javax.swing.JButton btnNuevo;

    private javax.swing.JButton btnSalvar;

    private javax.swing.JButton btnSalvarCerrar;

    private javax.swing.JComboBox cmbCiudad;

    private javax.swing.JComboBox cmbProvincia;

    private javax.swing.JComboBox cmbRegion;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

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

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JLabel lblFecha;

    private javax.swing.JLabel lblImagen;

    private frontend.almacen.PnTablaProductoStock pnTablaProductoStock1;

    private javax.swing.JCheckBox sin_stock;

    private javax.swing.JTextField stock_fisico;

    private javax.swing.JTextField stock_max;

    private javax.swing.JTextField stock_min;

    private utilidades.EnhancedJTextField txtCliente;

    private utilidades.EnhancedJTextField txtCodigo;

    private utilidades.EnhancedJTextField txtDescripcion;

    private utilidades.EnhancedJTextField txtGrupo;

    private utilidades.EnhancedJTextField txtItbis;

    private utilidades.EnhancedJTextField txtNombre;

    private utilidades.EnhancedJTextField txtPrecioCiudad;

    private utilidades.EnhancedJTextField txtPrecioCliente;

    private utilidades.EnhancedJTextField txtPrecioGeneral;

    private utilidades.EnhancedJTextField txtPrecioItbis;

    private utilidades.EnhancedJTextField txtPrecioProvincia;

    private utilidades.EnhancedJTextField txtPrecioRegion;
}
