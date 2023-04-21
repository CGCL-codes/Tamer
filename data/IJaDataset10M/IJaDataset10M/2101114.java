package resultado;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import respuesta.DatosRespuesta;
import respuesta.RespuestaParametros;
import respuesta.RespuestaUtil;
import respuesta.RespuestaVO;
import utilidad.MiExcepcion;
import utilidad.Util;
import utilidad.clasesBase.BaseVO;
import utilidad.componentes.MiTable;
import utilidad.componentes.MiTableCellRendererFecha;
import utilidad.componentes.MiTableModel;
import utilidad.vo.ColumnaTablaVO;
import encuesta.EncuestaParametros;
import encuesta.EncuestaUtil;
import encuesta.EncuestaVO;
import java.util.Date;

public class DatosResultado extends javax.swing.JFrame {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("resultado/Bundle");

    public DatosResultado(ResultadoVO resultado) {
        initComponents();
        this.resultado = resultado;
        inicializarForm();
    }

    public DatosResultado(ResultadoVO resultado, MiTable tablaOrigen) {
        initComponents();
        this.resultado = resultado;
        this.tablaOrigen = tablaOrigen;
        inicializarForm();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLTitulo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jBEliminar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        jLError = new javax.swing.JLabel();
        jTPDatos = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jDPFecha = new org.jdesktop.swingx.JXDatePicker();
        jCBEncuesta = new utilidad.componentes.MiJComboBox();
        jSHora = new javax.swing.JSpinner();
        jPNotas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTANotas = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTRespuestas = new utilidad.componentes.MiTable();
        jBNuevaRespuesta = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMConfiguracion = new javax.swing.JMenu();
        jMICreacionModificacion = new javax.swing.JMenuItem();
        jMAyuda = new javax.swing.JMenu();
        jMIPaginaInicio = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(null);
        jLTitulo.setFont(new java.awt.Font("Arial", 1, 24));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resultado/Bundle");
        jLTitulo.setText(bundle.getString("DatosResultado.jLTitulo.text"));
        jLTitulo.setToolTipText(bundle.getString("DatosResultado.jLTitulo.toolTipText"));
        jLTitulo.setName("jLTitulo");
        getContentPane().add(jLTitulo);
        jLTitulo.setBounds(20, 10, 480, 29);
        jSeparator1.setName("jSeparator1");
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(0, 50, 580, 10);
        jBEliminar.setText(bundle.getString("DatosResultado.jBEliminar.text"));
        jBEliminar.setName("jBEliminar");
        jBEliminar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(jBEliminar);
        jBEliminar.setBounds(20, 460, 90, 23);
        jBCancelar.setText(bundle.getString("DatosResultado.jBCancelar.text"));
        jBCancelar.setName("jBCancelar");
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jBCancelar);
        jBCancelar.setBounds(350, 460, 90, 23);
        jBGuardar.setText(bundle.getString("DatosResultado.jBGuardar.text"));
        jBGuardar.setName("jBGuardar");
        jBGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(jBGuardar);
        jBGuardar.setBounds(440, 460, 90, 23);
        jLError.setFont(new java.awt.Font("Arial", 1, 11));
        jLError.setForeground(java.awt.Color.red);
        jLError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLError.setText(bundle.getString("DatosResultado.jLError.text"));
        jLError.setName("jLError");
        jLError.setText("");
        getContentPane().add(jLError);
        jLError.setBounds(10, 490, 640, 20);
        jTPDatos.setToolTipText(bundle.getString("DatosResultado.jTPDatos.toolTipText"));
        jTPDatos.setFont(new java.awt.Font("Arial", 0, 11));
        jTPDatos.setName("jTPDatos");
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setName("jPanel1");
        jPanel1.setLayout(null);
        jLabel3.setText(bundle.getString("DatosResultado.jLabel3.text"));
        jLabel3.setName("jLabel3");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 50, 70, 20);
        jLabel9.setText(bundle.getString("DatosResultado.jLabel9.text"));
        jLabel9.setName("jLabel9");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(20, 20, 70, 20);
        jDPFecha.setFormats("dd/MM/yyyy");
        jDPFecha.setName("jDPFecha");
        jPanel1.add(jDPFecha);
        jDPFecha.setBounds(90, 20, 110, 20);
        jCBEncuesta.setName("jCBEncuesta");
        jCBEncuesta.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBEncuestaItemStateChanged(evt);
            }
        });
        jPanel1.add(jCBEncuesta);
        jCBEncuesta.setBounds(20, 70, 460, 20);
        jSHora.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR_OF_DAY));
        jSHora.setEditor(new javax.swing.JSpinner.DateEditor(jSHora, "H:mm"));
        jSHora.setName("jSHora");
        jPanel1.add(jSHora);
        jSHora.setBounds(200, 20, 60, 20);
        jTPDatos.addTab(bundle.getString("DatosResultado.jPanel1.TabConstraints.tabTitle"), jPanel1);
        jPNotas.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPNotas.setToolTipText(bundle.getString("DatosResultado.jPNotas.toolTipText"));
        jPNotas.setName("jPNotas");
        jPNotas.setLayout(null);
        jScrollPane1.setName("jScrollPane1");
        jTANotas.setColumns(20);
        jTANotas.setRows(5);
        jTANotas.setName("jTANotas");
        jScrollPane1.setViewportView(jTANotas);
        jPNotas.add(jScrollPane1);
        jScrollPane1.setBounds(10, 30, 460, 80);
        jLabel6.setText(bundle.getString("DatosResultado.jLabel6.text"));
        jLabel6.setName("jLabel6");
        jPNotas.add(jLabel6);
        jLabel6.setBounds(10, 10, 80, 14);
        jTPDatos.addTab(bundle.getString("DatosResultado.jPNotas.TabConstraints.tabTitle"), jPNotas);
        getContentPane().add(jTPDatos);
        jTPDatos.setBounds(20, 60, 510, 160);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("cliente/Bundle");
        jTPDatos.getAccessibleContext().setAccessibleName(bundle1.getString("DatosCliente.jTPDatos.AccessibleContext.accessibleName"));
        jScrollPane7.setName("jScrollPane7");
        jTRespuestas.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jTRespuestas.setName("jTRespuestas");
        jTRespuestas.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTRespuestasMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTRespuestas);
        getContentPane().add(jScrollPane7);
        jScrollPane7.setBounds(20, 260, 510, 190);
        jBNuevaRespuesta.setText(bundle.getString("DatosResultado.jBNuevaRespuesta.text"));
        jBNuevaRespuesta.setName("jBNuevaRespuesta");
        jBNuevaRespuesta.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNuevaRespuestaActionPerformed(evt);
            }
        });
        getContentPane().add(jBNuevaRespuesta);
        jBNuevaRespuesta.setBounds(360, 230, 170, 23);
        jLabel23.setText(bundle.getString("DatosResultado.jLabel23.text"));
        jLabel23.setName("jLabel23");
        getContentPane().add(jLabel23);
        jLabel23.setBounds(20, 240, 80, 20);
        jMenuBar1.setName("jMenuBar1");
        jMConfiguracion.setText(bundle.getString("DatosResultado.jMConfiguracion.text"));
        jMConfiguracion.setName("jMConfiguracion");
        jMICreacionModificacion.setText(bundle.getString("DatosResultado.jMICreacionModificacion.text"));
        jMICreacionModificacion.setName("jMICreacionModificacion");
        jMICreacionModificacion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICreacionModificacionActionPerformed(evt);
            }
        });
        jMConfiguracion.add(jMICreacionModificacion);
        jMenuBar1.add(jMConfiguracion);
        jMAyuda.setText(bundle.getString("DatosResultado.jMAyuda.text"));
        jMAyuda.setName("jMAyuda");
        jMIPaginaInicio.setText(bundle.getString("DatosResultado.jMIPaginaInicio.text"));
        jMIPaginaInicio.setName("jMIPaginaInicio");
        jMIPaginaInicio.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIPaginaInicioActionPerformed(evt);
            }
        });
        jMAyuda.add(jMIPaginaInicio);
        jMenuBar1.add(jMAyuda);
        setJMenuBar(jMenuBar1);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 561) / 2, (screenSize.height - 580) / 2, 561, 580);
    }

    private void jTRespuestasMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1) {
            modificarRespuesta();
        }
    }

    private void jBNuevaRespuestaActionPerformed(java.awt.event.ActionEvent evt) {
        nuevaRespuesta();
    }

    private void jCBEncuestaItemStateChanged(java.awt.event.ItemEvent evt) {
    }

    private void jBGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        guardar(true);
    }

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        cancelar();
    }

    private void jBEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        eliminar();
    }

    private void jMICreacionModificacionActionPerformed(java.awt.event.ActionEvent evt) {
        verCreacionModificacion();
    }

    private void jMIPaginaInicioActionPerformed(java.awt.event.ActionEvent evt) {
        irPaginaInicio();
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        Util.eliminarReferenciaLista(this);
    }

    private boolean guardar(boolean cerrarVentana) {
        try {
            validarObjeto();
            cargarObjeto();
            int numRegs = 0;
            if (resultado.getId() == -1) {
                numRegs = ResultadoUtil.insertar(resultado);
                if ((numRegs > 0) && (tablaOrigen != null)) {
                    tablaOrigen.añadirObjeto(resultado);
                }
            } else {
                numRegs = ResultadoUtil.actualizar(resultado);
                if ((numRegs > 0) && (tablaOrigen != null)) {
                    tablaOrigen.modificarObjeto(resultado);
                }
            }
            if (cerrarVentana && (numRegs > 0)) {
                this.dispose();
            }
            return true;
        } catch (MiExcepcion ex) {
            if (!ex.getDescripcion().isEmpty()) {
                jLError.setText(ex.getDescripcion());
            }
            javax.swing.JComponent componente = ex.getComponente();
            if (componente != null) {
                componente.requestFocus();
                componente.setBackground(java.awt.Color.LIGHT_GRAY);
            }
            return false;
        } catch (Exception ex) {
            jLError.setText(ex.getMessage());
            return false;
        }
    }

    private void cancelar() {
        this.dispose();
    }

    private void eliminar() {
        int resp = JOptionPane.showConfirmDialog(this, bundle.getString("¿Desea_Eliminar_Este_Resultado?"), "", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            int numRegs = ResultadoUtil.eliminar(this.resultado.getId());
            if ((numRegs > 0) && (tablaOrigen != null)) {
                tablaOrigen.eliminarObjeto(this.resultado);
            }
            this.dispose();
        }
    }

    private void irPaginaInicio() {
        inicio.InicioApp.cerrarVentanasAbiertas();
        this.dispose();
    }

    private void verCreacionModificacion() {
        operador.DatosCreacionModificacion frmDatos = new operador.DatosCreacionModificacion(this, resultado);
        Util.mostrarVentana(frmDatos);
    }

    private void inicializarForm() {
        Util.cambiarIcono(this);
        ajustarVisibilidades();
        cargarCombos();
        this.setTitle(bundle.getString("Datos_Resultado_Encuesta"));
        jLTitulo.setText(bundle.getString("Datos_Resultado_Encuesta"));
        mostrarObjeto();
        cargarModeloRespuestas();
    }

    private void ajustarVisibilidades() {
    }

    private void cargarCombos() {
        cargarComboEncuestas();
    }

    private void cargarComboEncuestas() {
        EncuestaParametros param = new EncuestaParametros();
        ArrayList lista = EncuestaUtil.listado(param);
        Util.añadirVO(lista, EncuestaUtil.buscarEncuesta(resultado.getIdEncuesta()));
        jCBEncuesta.cargarLista(lista, "encuesta.EncuestaVO", "getNombre");
    }

    private void cargarObjeto() {
        Date fechaHora = Util.crearFecha(jDPFecha.getDate(), (Date) jSHora.getValue());
        resultado.setFecha(fechaHora);
        resultado.setEncuesta((EncuestaVO) jCBEncuesta.getSelectedObj());
        resultado.setNotas(jTANotas.getText());
    }

    private void mostrarObjeto() {
        jCBEncuesta.setSelectedId(resultado.getIdEncuesta());
        jDPFecha.setDate(resultado.getFecha());
        jSHora.setValue(resultado.getFecha());
        jTANotas.setText(resultado.getNotas());
    }

    private void validarObjeto() throws MiExcepcion {
        if (jCBEncuesta.getSelectedId() < 1) {
            throw new MiExcepcion(jCBEncuesta, bundle.getString("Debe_Seleccionar_Una_Encuesta"));
        }
    }

    protected void cargarModeloRespuestas() {
        crearModeloRespuestas();
        cargarListaColumnasRespuestas();
        cargarListaObjetosRespuestas();
        calcularTotalesRespuestas();
        asignarModeloRespuestas();
    }

    protected void asignarModeloRespuestas() {
        jTRespuestas.setModel(modeloRespuestas);
        jTRespuestas.getModel().addTableModelListener(new OyenteModeloTabla());
        jTRespuestas.inicializarColumnas(modeloRespuestas.getListaColumnas());
    }

    public void crearModeloRespuestas() {
        this.modeloRespuestas = new MiTableModel("respuesta.RespuestaVO");
    }

    public void calcularTotalesRespuestas() {
        jLError.setText(bundle.getString("Existen_") + modeloRespuestas.getRowCount() + bundle.getString("_Respuesta(s)"));
    }

    public void cargarListaObjetosRespuestas() {
        RespuestaParametros param = new RespuestaParametros();
        param.idResultado = resultado.getId();
        ArrayList listadoRespuestas = RespuestaUtil.listado(param);
        modeloRespuestas.setListaObjetos(listadoRespuestas);
    }

    public void cargarListaColumnasRespuestas() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        ColumnaTablaVO col = null;
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Pregunta"), "getNombrePregunta", 0, SwingConstants.LEFT, 300));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Respuesta"), "getValor", 1, SwingConstants.CENTER));
        modeloRespuestas.setListaColumnas(listaColumnas);
    }

    protected BaseVO getObjSeleccionadoTablaRespuestas() {
        BaseVO obj = null;
        int filaModeloSel = jTRespuestas.getFilaSeleccionadaModelo();
        if (filaModeloSel != -1) {
            obj = (BaseVO) this.modeloRespuestas.getObjDeLista(filaModeloSel);
        }
        return obj;
    }

    public void nuevaRespuesta() {
        if ((resultado.getId() != -1) || (guardar(false))) {
            RespuestaVO respuesta = new RespuestaVO();
            respuesta.setIdResultado(resultado.getId());
            respuesta.setIdEncuesta(resultado.getIdEncuesta());
            respuesta.setNombreEncuesta(resultado.getNombreEncuesta());
            DatosRespuesta frm = new DatosRespuesta(respuesta, jTRespuestas);
            Util.mostrarVentana(frm);
        }
    }

    public void modificarRespuesta() {
        RespuestaVO respuesta = (RespuestaVO) getObjSeleccionadoTablaRespuestas();
        DatosRespuesta frm = new DatosRespuesta(respuesta, jTRespuestas);
        Util.mostrarVentana(frm);
    }

    private ResultadoVO resultado = new ResultadoVO();

    private MiTable tablaOrigen = null;

    private MiTableModel modeloRespuestas = null;

    class OyenteModeloTabla implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            calcularTotalesRespuestas();
        }
    }

    ;

    @Override
    protected JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        Action actionEscape = new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        };
        Action actionEnter = new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                guardar(true);
            }
        };
        KeyStroke strokeEscape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        KeyStroke strokeEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        rootPane.registerKeyboardAction(actionEscape, strokeEscape, JComponent.WHEN_IN_FOCUSED_WINDOW);
        rootPane.registerKeyboardAction(actionEnter, strokeEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }

    private javax.swing.JButton jBCancelar;

    private javax.swing.JButton jBEliminar;

    private javax.swing.JButton jBGuardar;

    private javax.swing.JButton jBNuevaRespuesta;

    private utilidad.componentes.MiJComboBox jCBEncuesta;

    private org.jdesktop.swingx.JXDatePicker jDPFecha;

    private javax.swing.JLabel jLError;

    private javax.swing.JLabel jLTitulo;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JMenu jMAyuda;

    private javax.swing.JMenu jMConfiguracion;

    private javax.swing.JMenuItem jMICreacionModificacion;

    private javax.swing.JMenuItem jMIPaginaInicio;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JPanel jPNotas;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSpinner jSHora;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane7;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JTextArea jTANotas;

    private javax.swing.JTabbedPane jTPDatos;

    private utilidad.componentes.MiTable jTRespuestas;
}