package vista;

import controlador.Proyecto;
import controlador.persistencia.BuilderMotor;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import modelo.repositorio.RepositorioMDR;
import org.netbeans.api.mdr.events.MDRChangeEvent;
import org.netbeans.api.mdr.events.MDRPreChangeListener;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.modelmanagement.UmlPackage;
import org.uml.diagrammanagement.Diagram;
import org.uml.diagrammanagement.GraphNode;
import org.uml.diagrammanagement.Property;
import org.uml.diagrammanagement.Uml1SemanticModelBridge;
import util.UMLUtil;
import util.Util;
import vista.explorador.Explorador;
import vista.explorador.NodoExplorador;
import vista.graficador.BarraTareas;
import vista.graficador.BarraTareasClase;
import vista.graficador.GrDiagrama;

/**
 *
 * @author  Juan Timoteo Ponce Ortiz
 */
public class FrmPrincipal extends javax.swing.JFrame implements ContenedorGraficador {

    private Proyecto proyecto;

    private Explorador arbol;

    private static FrmPrincipal INSTANCE;

    /** Creates new form FrmPrincipal */
    public FrmPrincipal() {
        proyecto = Proyecto.getInstancia();
        initComponents();
        init();
        initEventos();
        RepositorioMDR.getInstancia().startRepository(proyecto);
        INSTANCE = this;
    }

    public static FrmPrincipal getInstancia() {
        return INSTANCE;
    }

    public void init() {
        tabCenter.removeAll();
        if (arbol == null) {
            arbol = new Explorador(this);
            JScrollPane cont = new JScrollPane(arbol);
            tabLeft.add(arbol.getTitulo(), cont);
        } else {
            arbol.setModelo(proyecto.getModelo());
        }
        proyecto.addVista(arbol);
        repaint();
    }

    public void initEventos() {
        addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                salir();
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        FrmPrincipalListener listener = new FrmPrincipalListener();
        mNuevo.addActionListener(listener);
        mAbrir.addActionListener(listener);
        mGuardar.addActionListener(listener);
        mGuardarAs.addActionListener(listener);
        mAddElem.addActionListener(listener);
        mEditElem.addActionListener(listener);
        mDelElem.addActionListener(listener);
        mAddDiag.addActionListener(listener);
        mDelDiag.addActionListener(listener);
        mEXMI.addActionListener(listener);
        mIXMI.addActionListener(listener);
        mSalir.addActionListener(listener);
        btnDirectoNuevo.addActionListener(listener);
        btnDirectoAbrir.addActionListener(listener);
        btnDirectoGuardar.addActionListener(listener);
        btnDirectoAddDiagrama.addActionListener(listener);
        btnDirectoDelDiagrama.addActionListener(listener);
        btnCerrarDiagrama.addActionListener(listener);
    }

    private void initComponents() {
        barraFichero = new javax.swing.JToolBar();
        btnDirectoNuevo = new javax.swing.JButton();
        btnDirectoAbrir = new javax.swing.JButton();
        btnDirectoGuardar = new javax.swing.JButton();
        barraModelo = new javax.swing.JToolBar();
        btnDirectoAddDiagrama = new javax.swing.JButton();
        btnDirectoDelDiagrama = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        btnCerrarDiagrama = new javax.swing.JButton();
        panelPrincipal = new javax.swing.JSplitPane();
        tabLeft = new javax.swing.JTabbedPane();
        tabCenter = new javax.swing.JTabbedPane();
        txtMensaje = new javax.swing.JTextField();
        barraMenu = new javax.swing.JMenuBar();
        mArchivo = new javax.swing.JMenu();
        mNuevo = new javax.swing.JMenuItem();
        mAbrir = new javax.swing.JMenuItem();
        mGuardar = new javax.swing.JMenuItem();
        mGuardarAs = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mCerrar = new javax.swing.JMenuItem();
        mExportar = new javax.swing.JMenu();
        mEXMI = new javax.swing.JMenuItem();
        mImportar = new javax.swing.JMenu();
        mIXMI = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        mSalir = new javax.swing.JMenuItem();
        mEditar = new javax.swing.JMenu();
        mModelo = new javax.swing.JMenu();
        mAddElem = new javax.swing.JMenuItem();
        mEditElem = new javax.swing.JMenuItem();
        mDelElem = new javax.swing.JMenuItem();
        mDiagrama = new javax.swing.JMenu();
        mAddDiag = new javax.swing.JMenuItem();
        mDelDiag = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Case3CAPAS");
        btnDirectoNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/document-new.png")));
        btnDirectoNuevo.setToolTipText("Generar nuevo proyecto");
        barraFichero.add(btnDirectoNuevo);
        btnDirectoAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/document-open.png")));
        btnDirectoAbrir.setToolTipText("Abrir proyecto");
        barraFichero.add(btnDirectoAbrir);
        btnDirectoGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/document-save.png")));
        btnDirectoGuardar.setToolTipText("Guardar proyecto");
        barraFichero.add(btnDirectoGuardar);
        btnDirectoAddDiagrama.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/list-add.png")));
        btnDirectoAddDiagrama.setToolTipText("Agregar diagrama");
        barraModelo.add(btnDirectoAddDiagrama);
        btnDirectoDelDiagrama.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/list-remove.png")));
        btnDirectoDelDiagrama.setToolTipText("Eliminar diagrama");
        barraModelo.add(btnDirectoDelDiagrama);
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        barraModelo.add(jSeparator3);
        btnCerrarDiagrama.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/emblem-unreadable.png")));
        btnCerrarDiagrama.setToolTipText("Cerrar pestaña actual");
        barraModelo.add(btnCerrarDiagrama);
        panelPrincipal.setDividerLocation(200);
        panelPrincipal.setDividerSize(10);
        panelPrincipal.setOneTouchExpandable(true);
        panelPrincipal.setLeftComponent(tabLeft);
        panelPrincipal.setRightComponent(tabCenter);
        txtMensaje.setEditable(false);
        mArchivo.setText("Archivo");
        mNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/document-new.png")));
        mNuevo.setText("Nuevo proyecto");
        mArchivo.add(mNuevo);
        mAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/document-open.png")));
        mAbrir.setText("Abrir proyecto");
        mArchivo.add(mAbrir);
        mGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/document-save.png")));
        mGuardar.setText("Guardar proyecto");
        mArchivo.add(mGuardar);
        mGuardarAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/document-save-as.png")));
        mGuardarAs.setText("Guardar como");
        mArchivo.add(mGuardarAs);
        mArchivo.add(jSeparator1);
        mCerrar.setText("Cerrar proyecto");
        mCerrar.setEnabled(false);
        mArchivo.add(mCerrar);
        mExportar.setText("Exportar");
        mEXMI.setText("XMI");
        mExportar.add(mEXMI);
        mArchivo.add(mExportar);
        mImportar.setText("Importar");
        mIXMI.setText("XMI");
        mImportar.add(mIXMI);
        mArchivo.add(mImportar);
        mArchivo.add(jSeparator2);
        mSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        mSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/system-log-out.png")));
        mSalir.setText("Salir");
        mArchivo.add(mSalir);
        barraMenu.add(mArchivo);
        mEditar.setText("Editar");
        barraMenu.add(mEditar);
        mModelo.setText("Modelo");
        mAddElem.setText("Agregar elemento");
        mModelo.add(mAddElem);
        mEditElem.setText("Editar elemento");
        mModelo.add(mEditElem);
        mDelElem.setText("Eliminar elemento");
        mModelo.add(mDelElem);
        barraMenu.add(mModelo);
        mDiagrama.setText("Diagrama");
        mAddDiag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/list-add.png")));
        mAddDiag.setText("Agregar diagrama");
        mDiagrama.add(mAddDiag);
        mDelDiag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/list-remove.png")));
        mDelDiag.setText("Eliminar diagrama");
        mDiagrama.add(mDelDiag);
        barraMenu.add(mDiagrama);
        setJMenuBar(barraMenu);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(barraFichero, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(barraModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(338, 338, 338)).addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE).addComponent(txtMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(barraFichero, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(barraModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (args.length > 0 && args[0].equals("splash")) {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    SplashFrm spl = new SplashFrm();
                    spl.run();
                }
            });
        } else {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new FrmPrincipal().setVisible(true);
                }
            });
        }
    }

    public int indexOf(Diagram diagrama) {
        for (int i = 0; i < tabCenter.getComponentCount(); i++) {
            PanelGrDiagrama aux = (PanelGrDiagrama) tabCenter.getComponentAt(i);
            GrDiagrama diag = (GrDiagrama) aux.getDiagrama();
            if (diag.getDiagram().equals(diagrama)) return i;
        }
        return -1;
    }

    public GrDiagrama getDiagrama(int index) {
        if (index <= tabCenter.getComponentCount()) {
            PanelGrDiagrama aux = (PanelGrDiagrama) tabCenter.getComponentAt(index);
            return aux.getDiagrama();
        }
        return null;
    }

    public boolean contiene(Diagram diagrama) {
        return (indexOf(diagrama) != -1);
    }

    public boolean addGraficador(Diagram diagrama) {
        if (!contiene(diagrama)) {
            GrDiagrama graf = new GrDiagrama(diagrama);
            BarraTareasClase barra = new BarraTareasClase(graf);
            PanelGrDiagrama panel = new PanelGrDiagrama(barra, graf);
            tabCenter.add(graf.getTitulo(), panel);
            tabCenter.setSelectedComponent(panel);
            proyecto.addVista(graf);
            proyecto.updateVistas();
            return true;
        }
        return false;
    }

    public boolean removeGraficador(Diagram diagrama) {
        int index = indexOf(diagrama);
        if (index != -1) {
            proyecto.removeVista(getDiagrama(index));
            tabCenter.remove(index);
            return true;
        }
        return false;
    }

    public boolean isShowed(Diagram diagrama) {
        int index = tabCenter.getSelectedIndex();
        return (indexOf(diagrama) == index);
    }

    public void setShow(Diagram diagrama) {
        int index = indexOf(diagrama);
        if (index != -1) {
            tabCenter.setSelectedIndex(index);
        } else addGraficador(diagrama);
        setMensaje("Cambiando la vista de diagrama");
    }

    public void addElemento(ModelElement elemento) {
        PanelGrDiagrama aux = (PanelGrDiagrama) tabCenter.getSelectedComponent();
        GrDiagrama diag = aux.getDiagrama();
        diag.cancelCreacionLineaVisual();
        diag.cancelCreacionNodoVisual();
        GraphNode graphNode = UMLUtil.crearGraphNode();
        String claseGrafica = "GrClase";
        if (elemento instanceof UmlPackage) claseGrafica = "GrPaquete"; else if (elemento instanceof Interface) claseGrafica = "GrInterfaz";
        Property prop = UMLUtil.crearPropiedad("NodeType", claseGrafica);
        graphNode.getProperty().add(prop);
        Uml1SemanticModelBridge bridge = UMLUtil.crearPuenteSemantico(elemento);
        graphNode.setSemanticModel(bridge);
        diag.setCrearNodoVisual(graphNode);
        setMensaje("Presione sobre el panel para agregar");
    }

    private javax.swing.JToolBar barraFichero;

    private javax.swing.JMenuBar barraMenu;

    private javax.swing.JToolBar barraModelo;

    private javax.swing.JButton btnCerrarDiagrama;

    private javax.swing.JButton btnDirectoAbrir;

    private javax.swing.JButton btnDirectoAddDiagrama;

    private javax.swing.JButton btnDirectoDelDiagrama;

    private javax.swing.JButton btnDirectoGuardar;

    private javax.swing.JButton btnDirectoNuevo;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator3;

    private javax.swing.JMenuItem mAbrir;

    private javax.swing.JMenuItem mAddDiag;

    private javax.swing.JMenuItem mAddElem;

    private javax.swing.JMenu mArchivo;

    private javax.swing.JMenuItem mCerrar;

    private javax.swing.JMenuItem mDelDiag;

    private javax.swing.JMenuItem mDelElem;

    private javax.swing.JMenu mDiagrama;

    private javax.swing.JMenuItem mEXMI;

    private javax.swing.JMenuItem mEditElem;

    private javax.swing.JMenu mEditar;

    private javax.swing.JMenu mExportar;

    private javax.swing.JMenuItem mGuardar;

    private javax.swing.JMenuItem mGuardarAs;

    private javax.swing.JMenuItem mIXMI;

    private javax.swing.JMenu mImportar;

    private javax.swing.JMenu mModelo;

    private javax.swing.JMenuItem mNuevo;

    private javax.swing.JMenuItem mSalir;

    private javax.swing.JSplitPane panelPrincipal;

    private javax.swing.JTabbedPane tabCenter;

    private javax.swing.JTabbedPane tabLeft;

    private javax.swing.JTextField txtMensaje;

    class FrmPrincipalListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(mNuevo) || e.getSource().equals(btnDirectoNuevo)) {
                nuevo();
                return;
            }
            if (e.getSource().equals(mAbrir) || e.getSource().equals(btnDirectoAbrir)) {
                open();
                return;
            }
            if (e.getSource().equals(mGuardar) || e.getSource().equals(btnDirectoGuardar)) {
                save();
                return;
            }
            if (e.getSource().equals(mGuardarAs)) {
                saveComo();
                return;
            }
            if (e.getSource().equals(mAddElem)) {
                addElemento();
                return;
            }
            if (e.getSource().equals(mEditElem)) {
                editElemento();
                return;
            }
            if (e.getSource().equals(mDelElem)) {
                removeElemento();
                return;
            }
            if (e.getSource().equals(mAddDiag) || e.getSource().equals(btnDirectoAddDiagrama)) {
                addDiagrama();
                return;
            }
            if (e.getSource().equals(mDelDiag) || e.getSource().equals(btnDirectoDelDiagrama)) {
                removeDiagrama();
                return;
            }
            if (e.getSource().equals(mEXMI)) {
                exportXMI();
                return;
            }
            if (e.getSource().equals(mIXMI)) {
                importXMI();
                return;
            }
            if (e.getSource().equals(mSalir)) {
                salir();
                return;
            }
            if (e.getSource().equals(btnCerrarDiagrama)) {
                if (tabCenter.getComponentCount() > 0) {
                    PanelGrDiagrama temp = (PanelGrDiagrama) tabCenter.getSelectedComponent();
                    GrDiagrama diag = temp.getDiagrama();
                    proyecto.removeVista(diag);
                    tabCenter.remove(tabCenter.getSelectedIndex());
                }
                return;
            }
        }
    }

    private void nuevo() {
        proyecto.newProyecto();
        init();
        proyecto.updateVistas();
        setMensaje("Nuevo proyecto generado");
    }

    private void open() {
        proyecto.closeProyecto();
        if (proyecto.openProyecto(Util.showAbrir(this, "Abrir proyecto", "xmi"))) {
            init();
            arbol.setModelo(proyecto.getModelo());
            proyecto.addVista(arbol);
            proyecto.updateVistas();
            setMensaje("Proyecto cargado");
        } else setMensaje("Ocurrio un problema al cargar");
    }

    private void save() {
        if (proyecto.isModificado()) {
            if (proyecto.saveProyecto()) setMensaje("Proyecto almacenado");
        }
    }

    private void saveComo() {
        if (proyecto.saveComo(Util.showGuardar(this, "Guardar proyecto", "xmi"))) setMensaje("Proyecto almacenado");
    }

    private void exportXMI() {
        File f = Util.showGuardar(this, "Exportar", "xmi");
        if (f != null) {
            if (proyecto.exportar(f, BuilderMotor.PERSISTENCIA_XMI_ID)) setMensaje("Modelo exportado ");
        }
    }

    private void importXMI() {
        if (proyecto.closeProyecto()) {
            File f = Util.showAbrir(this, "Importar", "xmi");
            if (f != null) {
                if (proyecto.importar(f, BuilderMotor.PERSISTENCIA_XMI_ID)) setMensaje("Modelo importado");
                init();
                arbol.setModelo(proyecto.getModelo());
                proyecto.addVista(arbol);
                proyecto.updateVistas();
            }
        }
    }

    private void addElemento() {
        if (arbol.getSelectionPath() != null) {
            NodoExplorador nodo = (NodoExplorador) arbol.getSelectionPath().getLastPathComponent();
            if (nodo.getElemento() != null && nodo.getElemento() instanceof UmlPackage) {
                Object[] mensaje = new Object[2];
                mensaje[0] = "Seleccione el elemento a agregar";
                JComboBox combo = new JComboBox();
                combo.setEditable(true);
                combo.addItem("Clase");
                combo.addItem("Paquete");
                combo.addItem("Interfaz");
                mensaje[1] = combo;
                String[] opciones = new String[] { "Aceptar", "Cancelar" };
                int result = JOptionPane.showOptionDialog(this, mensaje, "Agregar elemento", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
                if (result == JOptionPane.OK_OPTION) {
                    ModelElement ele = null;
                    switch(combo.getSelectedIndex()) {
                        case 0:
                            ele = UMLUtil.crearClase("Nueva clase");
                            break;
                        case 1:
                            ele = UMLUtil.crearPaquete("Nueva paquete");
                            break;
                        case 2:
                            ele = UMLUtil.crearInterfaz("Nueva interfaz");
                            break;
                    }
                    ele.setNamespace((Namespace) nodo.getElemento());
                    actualizarArbol();
                    setMensaje("Elemento agregado");
                }
            } else Util.showAdvertencia(this, "Debe seleccionar un paquete");
        } else Util.showAdvertencia(this, "Debe seleccionar un paquete en el arbol primero");
    }

    private void editElemento() {
        if (arbol.getSelectionPath() != null) {
            NodoExplorador nodo = (NodoExplorador) arbol.getSelectionPath().getLastPathComponent();
            if (nodo != null) nodo.editElemento();
        } else Util.showAdvertencia(this, "Debe seleccionar un elemento en el arbol");
    }

    private void removeElemento() {
        if (arbol.getSelectionPath() != null) {
            NodoExplorador nodo = (NodoExplorador) arbol.getSelectionPath().getLastPathComponent();
            if (nodo != null) {
                nodo.remove();
                setMensaje("Elemento eliminado");
            }
        } else Util.showAdvertencia(this, "Debe seleccionar un elemento en el arbol");
    }

    private void addDiagrama() {
        if (arbol.getSelectionPath() != null) {
            NodoExplorador nodo = (NodoExplorador) arbol.getSelectionPath().getLastPathComponent();
            if (nodo != null) nodo.addDiagrama();
        } else Util.showAdvertencia(this, "Debe seleccionar un lugar en el arbol primero");
    }

    private void removeDiagrama() {
        if (arbol.getSelectionPath() != null) {
            NodoExplorador nodo = (NodoExplorador) arbol.getSelectionPath().getLastPathComponent();
            if (nodo != null && nodo.getDiagrama() != null) {
                nodo.remove();
                setMensaje("Diagrama eliminado");
            } else Util.showAdvertencia(this, "No es un diagrama");
        } else Util.showAdvertencia(this, "Debe seleccionar un diagrama");
    }

    private void salir() {
        if (proyecto.closeProyecto()) System.exit(0);
    }

    public void actualizarArbol() {
        arbol.updateVista();
    }

    private void setMensaje(String str) {
        txtMensaje.setText(str);
    }

    class PanelGrDiagrama extends JPanel {

        protected BarraTareas barra;

        protected GrDiagrama diagrama;

        public PanelGrDiagrama(BarraTareas barra, GrDiagrama diagrama) {
            super();
            this.barra = barra;
            this.diagrama = diagrama;
            setLayout(new BorderLayout());
            init();
        }

        void init() {
            barra.setOrientation(JToolBar.HORIZONTAL);
            add(barra, BorderLayout.NORTH);
            add(new JScrollPane(diagrama), BorderLayout.CENTER);
        }

        public GrDiagrama getDiagrama() {
            return diagrama;
        }
    }

    static class SplashFrm implements Runnable {

        FrmPrincipal frm = new FrmPrincipal();

        public void run() {
            final SplashScreen splash = SplashScreen.getSplashScreen();
            if (splash == null) {
                System.out.println("SplashScreen.getSplashScreen() returned null");
                return;
            }
            Graphics2D g = splash.createGraphics();
            if (g == null) {
                System.out.println("g is null");
                return;
            }
            for (int i = 0; i < 50; i++) {
                renderSplashFrame(g, i);
                splash.update();
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                }
            }
            splash.close();
            frm.setVisible(true);
            frm.toFront();
        }
    }

    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = { "repositorio", "elementos", "inicializaci�n", "configuraci�n" };
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120, 140, 200, 40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Cargando " + comps[(frame / 5) % 4] + "...", 120, 150);
    }
}
