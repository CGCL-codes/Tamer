package frontend.almacen;

import eventos.AlmacenEvent;
import javax.swing.JOptionPane;
import mensaje_objetos.almacen.Almacen;
import modelo_objetos.almacen.ModeloAlmacen;
import utilidades.IObserver;
import utilidades.PreguntaUsuario;

/**
 *
 * @author  vacax
 */
public class DgCrearAlmacen extends javax.swing.JDialog implements IObserver, eventos.IAlmacen {

    /**
     *Propiedades
     */
    private ModeloAlmacen modelo;

    private int modoOperacion = -1;

    /** Creates new form DgCrearAlmacen */
    public DgCrearAlmacen(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        instanciarObjetos();
    }

    public DgCrearAlmacen() {
        initComponents();
        instanciarObjetos();
    }

    /**
     *Metodo para instanciar los objetos.
     */
    private void instanciarObjetos() {
        modelo = ModeloAlmacen.getInstancia();
        addListener();
    }

    /**
     *Metodo para indentificar los listener.
     */
    private void addListener() {
    }

    /**
     *Metodo para encapsular la informacion del nuevo almacen.
     */
    private Almacen getDatos() {
        Almacen tmp = new Almacen();
        tmp.setCodigo(codigo.getText());
        tmp.setNombre(nombre.getText());
        tmp.setDireccion(direccion.getText());
        tmp.setTelefono(telefono.getText());
        tmp.setObservacion(observacion.getText());
        return tmp;
    }

    /**
     */
    private void setDatos(Almacen tmp) {
        codigo.setText(tmp.getCodigo());
        nombre.setText(tmp.getNombre());
        direccion.setText(tmp.getDireccion());
        telefono.setText(tmp.getTelefono());
        observacion.setText(tmp.getObservacion());
    }

    /**
     *Metodo para validar la informacion.
     */
    private boolean validandoDatos(Almacen tmp) {
        if (tmp.getCodigo().length() == 0) {
            PreguntaUsuario.mensajeIncompleto("Codigo");
            codigo.requestFocus();
            return false;
        } else if (tmp.getNombre().length() == 0) {
            PreguntaUsuario.mensajeIncompleto("Nombre");
            nombre.requestFocus();
            return false;
        }
        return true;
    }

    /**
     *Metodo para limpiar los campos.
     */
    private void limpiarCampos() {
        codigo.setText("");
        nombre.setText("");
        direccion.setText("");
        telefono.setText("");
        observacion.setText("");
    }

    /**
     * Metodo para habilitar los campos de la tabla.
     */
    private void habilitarCampos(boolean valor) {
        codigo.setEnabled(valor);
        nombre.setEnabled(valor);
        direccion.setEnabled(valor);
        telefono.setEnabled(valor);
        observacion.setEnabled(valor);
    }

    /**
     *Metodo para limpiar todas las variables.
     */
    private void limpiandoVariables() {
        habilitarCampos(true);
        limpiarCampos();
        modoOperacion = -1;
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        direccion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        observacion = new javax.swing.JTextArea();
        aceptar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear Almacen");
        setAlwaysOnTop(true);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion Basica"));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText("Codigo:");
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Nombre:");
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1).add(jLabel2)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(nombre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE).add(codigo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(codigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(nombre, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(16, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion Adicional"));
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("Direccion:");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setText("Telefono:");
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText("Observacion:");
        observacion.setColumns(20);
        observacion.setRows(5);
        jScrollPane1.setViewportView(observacion);
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(jLabel4)).add(3, 3, 3).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(telefono, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE).add(direccion, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))).add(jPanel2Layout.createSequentialGroup().add(jLabel5).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(direccion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(telefono, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel5).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(13, Short.MAX_VALUE)));
        aceptar.setFont(new java.awt.Font("Dialog", 0, 12));
        aceptar.setText("Aceptar");
        aceptar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap(258, Short.MAX_VALUE).add(aceptar).addContainerGap()).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 12, Short.MAX_VALUE).add(aceptar).addContainerGap()));
        pack();
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        limpiandoVariables();
    }

    private void aceptarActionPerformed(java.awt.event.ActionEvent evt) {
        aceptar();
    }

    /**
     * Metodo para indicar lo que estoy indicando.
     */
    private void aceptar() {
        if (modoOperacion == AlmacenEvent.NUEVO) {
            Almacen tmp = getDatos();
            if (validandoDatos(tmp)) {
                modelo.addAlmancen(tmp);
            }
        } else if (modoOperacion == AlmacenEvent.EDITAR) {
            Almacen tmp = getDatos();
            if (validandoDatos(tmp)) {
                modelo.saveOruUpdateAlmacen(tmp);
            }
        } else if (modoOperacion == AlmacenEvent.VISUALIZAR) {
            habilitarCampos(true);
        }
        setVisible(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DgCrearAlmacen(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    public void fireAlmacen(AlmacenEvent almacen) {
        limpiarCampos();
        if (almacen.getId() == AlmacenEvent.NUEVO) {
            codigo.setEnabled(true);
        } else if (almacen.getId() == AlmacenEvent.EDITAR) {
            setDatos(almacen.getAlmacen());
            codigo.setEnabled(false);
        } else if (almacen.getId() == AlmacenEvent.VISUALIZAR) {
            habilitarCampos(false);
            setDatos(almacen.getAlmacen());
        }
        modoOperacion = almacen.getId();
    }

    public void update(Class clase, Object argumento, String mensaje) {
    }

    private javax.swing.JButton aceptar;

    private javax.swing.JTextField codigo;

    private javax.swing.JTextField direccion;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField nombre;

    private javax.swing.JTextArea observacion;

    private javax.swing.JTextField telefono;
}
