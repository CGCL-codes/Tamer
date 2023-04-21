package vista;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import javax.help.HelpBroker;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import modelo.Proyecto;
import modelo.repositorio.Repositorio;
import modelo.repositorio.RepositorioImpl;
import modelo.types.PropertyType;
import modelo.types.TokenMakerType;
import org.apache.log4j.Logger;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.modelmanagement.UmlPackage;
import org.uml.diagrammanagement.Diagram;
import org.uml.diagrammanagement.GraphNode;
import util.AppUtil;
import util.Configuracion;
import util.Constantes;
import util.HelpUtil;
import util.ImageSelection;
import util.PropertyTableModel;
import util.gui.CloseableTabbedPane;
import util.gui.JAbout;
import util.gui.PanelGrDiagrama;
import util.gui.WaitDialog;
import util.uml.ElementoVisualUtil;
import util.uml.UMLUtil;
import vista.editores.EditorProyecto;
import vista.explorador.Explorador;
import vista.explorador.NodoExplorador;
import vista.graficador.GrDiagrama;
import vista.graficador.GrNodoVisual;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif_lite.panel.SimpleInternalFrame;
import controlador.ControllerProyecto;
import controlador.persistencia.MotorPersistencia;
import controlador.persistencia.MotorPersistenciaImpl;

/**
 * The Class UIPrincipal.
 * 
 * @author Juan Timoteo Ponce Ortiz
 */
public final class UIPrincipal extends JFrame implements Vista {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** The log. */
    private static Logger log = Logger.getLogger(UIPrincipal.class);

    /** The proyecto. */
    private final Proyecto proyecto;

    /** The arbol. */
    private final Explorador arbol;

    /** The controller proyecto. */
    private final ControllerProyecto controllerProyecto = ControllerProyecto.newInstance();

    /** The property viewer. */
    private final Vista propertyViewer = new UIPropertyViewer(new Hashtable<String, String>());

    /** The tabbed pane. */
    private final CloseableTabbedPane tabbedPane = new CloseableTabbedPane();

    /** The container. */
    private final DiagramContainer container;

    private final Repositorio repository;

    /**
	 * Instantiates a new uI principal.
	 */
    public UIPrincipal() {
        initComponents();
        tabbedPane.setVisible(true);
        panelRightContainer.add(tabbedPane, BorderLayout.CENTER);
        repository = RepositorioImpl.getInstance();
        repository.start();
        final MotorPersistencia persistenceManager = new MotorPersistenciaImpl(repository);
        proyecto = new Proyecto(repository, persistenceManager);
        controllerProyecto.addModel(proyecto);
        container = new DiagramContainerImpl(repository, proyecto, tabbedPane, controllerProyecto, propertyViewer);
        arbol = new Explorador(repository, controllerProyecto, container);
        init();
        initEventos();
        getPanelExplorer().setViewportView(arbol);
        setVisible(true);
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public Explorador getArbol() {
        return arbol;
    }

    public ControllerProyecto getControllerProyecto() {
        return controllerProyecto;
    }

    public Vista getPropertyViewer() {
        return propertyViewer;
    }

    public CloseableTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public DiagramContainer getContainer() {
        return container;
    }

    /**
	 * Btn new action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnNewActionPerformed() {
        nuevo();
    }

    /**
	 * Btn open action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnOpenActionPerformed() {
        open();
    }

    /**
	 * Btn save action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnSaveActionPerformed() {
        save();
    }

    /**
	 * Btn properties action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnPropertiesActionPerformed() {
        editProject();
    }

    /**
	 * Btn add diagram action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnAddDiagramActionPerformed() {
        addDiagrama();
    }

    /**
	 * Btn remove diagram action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnRemoveDiagramActionPerformed() {
        removeDiagrama();
    }

    /**
	 * Btn generate action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnGenerateActionPerformed() {
        generarCodigo();
    }

    /**
	 * Btn run generated action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void btnRunGeneratedActionPerformed() {
        runGenerated();
    }

    /**
	 * Find paths menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void findPathsMenuItemActionPerformed() {
        findPaths();
    }

    /**
	 * View code menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void viewCodeMenuItemActionPerformed() {
        viewCode();
    }

    /**
	 * View code.
	 */
    private void viewCode() {
        final Diagram diagram = getCurrentDiagram();
        if (diagram == null) {
            AppUtil.showMensaje(this, "No existe un diagrama seleccionado");
            return;
        }
        final UICodeViewer codeViewer = UICodeViewer.newInstance();
        codeViewer.setType(TokenMakerType.JAVA);
        codeViewer.setRootFolder(proyecto.getGenPath() + "/" + diagram.getName().toLowerCase().replace(" ", "_"));
        codeViewer.loadFiles();
        codeViewer.setVisible(true);
    }

    /**
	 * New menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void newMenuItemActionPerformed() {
        nuevo();
    }

    /**
	 * Open menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void openMenuItemActionPerformed() {
        open();
    }

    /**
	 * Save menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void saveMenuItemActionPerformed() {
        save();
    }

    /**
	 * Save as menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void saveAsMenuItemActionPerformed() {
        saveComo();
    }

    /**
	 * Export xmi menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void exportXMIMenuItemActionPerformed() {
        exportXMI();
    }

    /**
	 * Import xmi menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void importXMIMenuItemActionPerformed() {
        importXMI();
    }

    /**
	 * Properties menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void propertiesMenuItemActionPerformed() {
        editProject();
    }

    /**
	 * Exit menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void exitMenuItemActionPerformed() {
        salir();
    }

    /**
	 * Copy image menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void copyImageMenuItemActionPerformed() {
        exportToImage(true);
    }

    /**
	 * Image to file menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void imageToFileMenuItemActionPerformed() {
        exportToImage(false);
    }

    /**
	 * Prints the menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void printMenuItemActionPerformed() {
        print();
    }

    /**
	 * Adds the diagram menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void addDiagramMenuItemActionPerformed() {
        addDiagrama();
    }

    /**
	 * Removes the diagram menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void removeDiagramMenuItemActionPerformed() {
        removeDiagrama();
    }

    /**
	 * Adds the elem menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void addElemMenuItemActionPerformed() {
        addElemento();
    }

    /**
	 * Edits the elem menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void editElemMenuItemActionPerformed() {
        editElemento();
    }

    /**
	 * Removes the elem menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void removeElemMenuItemActionPerformed() {
        removeElemento();
    }

    /**
	 * Edits the generation menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void editGenerationMenuItemActionPerformed() {
        editGeneracion();
    }

    /**
	 * Generate menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void generateMenuItemActionPerformed() {
        generarCodigo();
    }

    /**
	 * Run generated menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void runGeneratedMenuItemActionPerformed() {
        runGenerated();
    }

    /**
	 * This window closing.
	 * 
	 * @param e
	 *            the e
	 */
    private void thisWindowClosing(final WindowEvent e) {
        salir();
    }

    /**
	 * Help menu item action performed.
	 * 
	 * @param e
	 *            the e
	 */
    private void helpMenuItemActionPerformed() {
        help();
    }

    private void aboutMenuItemActionPerformed() {
        showInfo();
    }

    /**
	 * Inits the components.
	 */
    private void initComponents() {
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        newMenuItem = new JMenuItem();
        openMenuItem = new JMenuItem();
        saveMenuItem = new JMenuItem();
        saveAsMenuItem = new JMenuItem();
        exportXMIMenuItem = new JMenuItem();
        importXMIMenuItem = new JMenuItem();
        propertiesMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        editMenu = new JMenu();
        copyImageMenuItem = new JMenuItem();
        imageToFileMenuItem = new JMenuItem();
        printMenuItem = new JMenuItem();
        modelMenu = new JMenu();
        addDiagramMenuItem = new JMenuItem();
        removeDiagramMenuItem = new JMenuItem();
        addElemMenuItem = new JMenuItem();
        editElemMenuItem = new JMenuItem();
        removeElemMenuItem = new JMenuItem();
        findPathsMenuItem = new JMenuItem();
        separator7 = new JSeparator();
        generationMenu = new JMenu();
        editGenerationMenuItem = new JMenuItem();
        generateMenuItem = new JMenuItem();
        runGeneratedMenuItem = new JMenuItem();
        viewCodeMenuItem = new JMenuItem();
        helpMenu = new JMenu();
        helpMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
        toolBar = new JToolBar();
        btnNew = new JButton();
        btnOpen = new JButton();
        btnSave = new JButton();
        btnProperties = new JButton();
        btnAddDiagram = new JButton();
        btnRemoveDiagram = new JButton();
        btnGenerate = new JButton();
        btnRunGenerated = new JButton();
        simpleInternalFrame1 = new SimpleInternalFrame();
        panelExplorer = new JScrollPane();
        separator1 = compFactory.createSeparator("Proyecto");
        panelProject = new JScrollPane();
        tableProject = new JTable();
        separator2 = compFactory.createSeparator("Elemento");
        panelElement = new JScrollPane();
        tableElement = new JTable();
        panelRight = new SimpleInternalFrame();
        panelRightContainer = new JPanel();
        panelConsole = new SimpleInternalFrame();
        statusText = new JLabel();
        CellConstraints cc = new CellConstraints();
        setTitle("Tiergen");
        setIconImage(new ImageIcon(getClass().getResource("/iconos/mail_send.png")).getImage());
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout("139dlu, $lcgap, [323dlu,default]:grow", "default, $lgap, 267dlu:grow, $lgap, 19dlu, $lgap, default"));
        {
            {
                fileMenu.setText("Archivo");
                newMenuItem.setText("Nuevo");
                newMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/document-new.png")));
                newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
                newMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        newMenuItemActionPerformed();
                    }
                });
                fileMenu.add(newMenuItem);
                openMenuItem.setText("Abrir");
                openMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/document-open.png")));
                openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
                openMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        openMenuItemActionPerformed();
                    }
                });
                fileMenu.add(openMenuItem);
                saveMenuItem.setText("Guardar");
                saveMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/document-save.png")));
                saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
                saveMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        saveMenuItemActionPerformed();
                    }
                });
                fileMenu.add(saveMenuItem);
                saveAsMenuItem.setText("Guardar como");
                saveAsMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/document-save-as.png")));
                saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
                saveAsMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        saveAsMenuItemActionPerformed();
                    }
                });
                fileMenu.add(saveAsMenuItem);
                fileMenu.addSeparator();
                exportXMIMenuItem.setText("Exportar a XMI");
                exportXMIMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
                exportXMIMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        exportXMIMenuItemActionPerformed();
                    }
                });
                fileMenu.add(exportXMIMenuItem);
                importXMIMenuItem.setText("Importar desde XMI");
                importXMIMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
                importXMIMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        importXMIMenuItemActionPerformed();
                    }
                });
                fileMenu.add(importXMIMenuItem);
                fileMenu.addSeparator();
                propertiesMenuItem.setText("Propiedades");
                propertiesMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/format-justify-fill.png")));
                propertiesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.ALT_MASK));
                propertiesMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        propertiesMenuItemActionPerformed();
                    }
                });
                fileMenu.add(propertiesMenuItem);
                fileMenu.addSeparator();
                exitMenuItem.setText("Salir");
                exitMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/system-shutdown.png")));
                exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
                exitMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        exitMenuItemActionPerformed();
                    }
                });
                fileMenu.add(exitMenuItem);
            }
            menuBar.add(fileMenu);
            {
                editMenu.setText("Edicion");
                copyImageMenuItem.setText("Copiar a imagen");
                copyImageMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/edit-copy.png")));
                copyImageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
                copyImageMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        copyImageMenuItemActionPerformed();
                    }
                });
                editMenu.add(copyImageMenuItem);
                imageToFileMenuItem.setText("Guardar como imagen");
                imageToFileMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/document-save-as.png")));
                imageToFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK | KeyEvent.ALT_MASK));
                imageToFileMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        imageToFileMenuItemActionPerformed();
                    }
                });
                editMenu.add(imageToFileMenuItem);
                printMenuItem.setText("Imprimir diagrama");
                printMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/document-print.png")));
                printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
                printMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        printMenuItemActionPerformed();
                    }
                });
                editMenu.add(printMenuItem);
            }
            menuBar.add(editMenu);
            {
                modelMenu.setText("Diagrama");
                addDiagramMenuItem.setText("Agregar diagrama");
                addDiagramMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/list-add.png")));
                addDiagramMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
                addDiagramMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        addDiagramMenuItemActionPerformed();
                    }
                });
                modelMenu.add(addDiagramMenuItem);
                removeDiagramMenuItem.setText("Remover diagrama");
                removeDiagramMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/list-remove.png")));
                removeDiagramMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
                removeDiagramMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        removeDiagramMenuItemActionPerformed();
                    }
                });
                modelMenu.add(removeDiagramMenuItem);
                modelMenu.addSeparator();
                addElemMenuItem.setText("Agregar elemento");
                addElemMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/list-add.png")));
                addElemMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_MASK));
                addElemMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        addElemMenuItemActionPerformed();
                    }
                });
                modelMenu.add(addElemMenuItem);
                editElemMenuItem.setText("Editar elemento");
                editElemMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/format-justify-fill.png")));
                editElemMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.ALT_MASK));
                editElemMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        editElemMenuItemActionPerformed();
                    }
                });
                modelMenu.add(editElemMenuItem);
                removeElemMenuItem.setText("Remover elemento");
                removeElemMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/list-remove.png")));
                removeElemMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.ALT_MASK));
                removeElemMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        removeElemMenuItemActionPerformed();
                    }
                });
                modelMenu.add(removeElemMenuItem);
                findPathsMenuItem.setText("Encontrar relaciones");
                findPathsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.ALT_MASK));
                findPathsMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        findPathsMenuItemActionPerformed();
                    }
                });
                modelMenu.add(findPathsMenuItem);
                modelMenu.add(separator7);
            }
            menuBar.add(modelMenu);
            {
                generationMenu.setText("Generacion");
                editGenerationMenuItem.setText("Configurar generación");
                editGenerationMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/format-justify-fill.png")));
                editGenerationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));
                editGenerationMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        editGenerationMenuItemActionPerformed();
                    }
                });
                generationMenu.add(editGenerationMenuItem);
                generateMenuItem.setText("Generar código");
                generateMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/media-record.png")));
                generateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.ALT_MASK));
                generateMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        generateMenuItemActionPerformed();
                    }
                });
                generationMenu.add(generateMenuItem);
                runGeneratedMenuItem.setText("Ejecutar código generado");
                runGeneratedMenuItem.setIcon(new ImageIcon(getClass().getResource("/iconos/media-playback-start.png")));
                runGeneratedMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.ALT_MASK));
                runGeneratedMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        runGeneratedMenuItemActionPerformed();
                    }
                });
                generationMenu.add(runGeneratedMenuItem);
                viewCodeMenuItem.setText("Visualizar codigo generado");
                viewCodeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.ALT_MASK));
                viewCodeMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        viewCodeMenuItemActionPerformed();
                    }
                });
                generationMenu.add(viewCodeMenuItem);
            }
            menuBar.add(generationMenu);
            {
                helpMenu.setText("Ayuda");
                helpMenuItem.setText("Ayuda");
                helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
                helpMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        helpMenuItemActionPerformed();
                    }
                });
                helpMenu.add(helpMenuItem);
                aboutMenuItem.setText("Acerca de");
                aboutMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        aboutMenuItemActionPerformed();
                    }
                });
                helpMenu.add(aboutMenuItem);
            }
            menuBar.add(helpMenu);
        }
        setJMenuBar(menuBar);
        {
            btnNew.setIcon(new ImageIcon(getClass().getResource("/iconos/document-new.png")));
            btnNew.setToolTipText("Genera un nuevo proyecto");
            btnNew.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnNewActionPerformed();
                }
            });
            toolBar.add(btnNew);
            btnOpen.setIcon(new ImageIcon(getClass().getResource("/iconos/document-open.png")));
            btnOpen.setToolTipText("Permite abrir un proyecto guardado");
            btnOpen.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnOpenActionPerformed();
                }
            });
            toolBar.add(btnOpen);
            btnSave.setIcon(new ImageIcon(getClass().getResource("/iconos/document-save.png")));
            btnSave.setToolTipText("Guardar el proyecto actual");
            btnSave.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnSaveActionPerformed();
                }
            });
            toolBar.add(btnSave);
            btnProperties.setIcon(new ImageIcon(getClass().getResource("/iconos/format-justify-fill.png")));
            btnProperties.setToolTipText("Editar las propiedades del proyecto");
            btnProperties.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnPropertiesActionPerformed();
                }
            });
            toolBar.add(btnProperties);
            btnAddDiagram.setIcon(new ImageIcon(getClass().getResource("/iconos/list-add.png")));
            btnAddDiagram.setToolTipText("Agregar un diagrama");
            btnAddDiagram.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnAddDiagramActionPerformed();
                }
            });
            toolBar.add(btnAddDiagram);
            btnRemoveDiagram.setIcon(new ImageIcon(getClass().getResource("/iconos/list-remove.png")));
            btnRemoveDiagram.setToolTipText("Eliminar el diagrama actual");
            btnRemoveDiagram.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnRemoveDiagramActionPerformed();
                }
            });
            toolBar.add(btnRemoveDiagram);
            btnGenerate.setIcon(new ImageIcon(getClass().getResource("/iconos/media-record.png")));
            btnGenerate.setToolTipText("Generar código");
            btnGenerate.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnGenerateActionPerformed();
                }
            });
            toolBar.add(btnGenerate);
            btnRunGenerated.setIcon(new ImageIcon(getClass().getResource("/iconos/media-playback-start.png")));
            btnRunGenerated.setToolTipText("Ejecutar el proyecto generado");
            btnRunGenerated.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    btnRunGeneratedActionPerformed();
                }
            });
            toolBar.add(btnRunGenerated);
        }
        contentPane.add(toolBar, cc.xywh(1, 1, 3, 1));
        {
            simpleInternalFrame1.setTitle("Explorador");
            Container simpleInternalFrame1ContentPane = simpleInternalFrame1.getContentPane();
            simpleInternalFrame1ContentPane.setLayout(new FormLayout("135dlu", "145dlu:grow, $lgap, default, $lgap, 48dlu:grow, $lgap, default, $lgap, 78dlu:grow"));
            simpleInternalFrame1ContentPane.add(panelExplorer, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
            simpleInternalFrame1ContentPane.add(separator1, cc.xy(1, 3));
            {
                panelProject.setViewportView(tableProject);
            }
            simpleInternalFrame1ContentPane.add(panelProject, cc.xywh(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
            simpleInternalFrame1ContentPane.add(separator2, cc.xy(1, 7));
            {
                panelElement.setViewportView(tableElement);
            }
            simpleInternalFrame1ContentPane.add(panelElement, cc.xywh(1, 9, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
        }
        contentPane.add(simpleInternalFrame1, cc.xywh(1, 3, 1, 3));
        {
            panelRight.setTitle("Diseñador");
            Container panelRightContentPane = panelRight.getContentPane();
            panelRightContentPane.setLayout(new FormLayout("default:grow", "fill:default:grow"));
            {
                panelRightContainer.setLayout(new BorderLayout());
            }
            panelRightContentPane.add(panelRightContainer, cc.xy(1, 1));
        }
        contentPane.add(panelRight, cc.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
        {
            panelConsole.setTitle("Salida");
            Container panelConsoleContentPane = panelConsole.getContentPane();
            panelConsoleContentPane.setLayout(new BorderLayout());
        }
        contentPane.add(panelConsole, cc.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
        statusText.setIcon(new ImageIcon(getClass().getResource("/iconos/gtk-info.png")));
        statusText.setLabelFor(panelRight);
        statusText.setToolTipText("Mensajes de estado de la aplicación");
        contentPane.add(statusText, cc.xywh(1, 7, 3, 1));
        pack();
        setLocationRelativeTo(getOwner());
    }

    private JMenuBar menuBar;

    private JMenu fileMenu;

    private JMenuItem newMenuItem;

    private JMenuItem openMenuItem;

    private JMenuItem saveMenuItem;

    private JMenuItem saveAsMenuItem;

    private JMenuItem exportXMIMenuItem;

    private JMenuItem importXMIMenuItem;

    private JMenuItem propertiesMenuItem;

    private JMenuItem exitMenuItem;

    private JMenu editMenu;

    private JMenuItem copyImageMenuItem;

    private JMenuItem imageToFileMenuItem;

    private JMenuItem printMenuItem;

    private JMenu modelMenu;

    private JMenuItem addDiagramMenuItem;

    private JMenuItem removeDiagramMenuItem;

    private JMenuItem addElemMenuItem;

    private JMenuItem editElemMenuItem;

    private JMenuItem removeElemMenuItem;

    private JMenuItem findPathsMenuItem;

    private JSeparator separator7;

    private JMenu generationMenu;

    private JMenuItem editGenerationMenuItem;

    private JMenuItem generateMenuItem;

    private JMenuItem runGeneratedMenuItem;

    private JMenuItem viewCodeMenuItem;

    private JMenu helpMenu;

    private JMenuItem helpMenuItem;

    private JMenuItem aboutMenuItem;

    private JToolBar toolBar;

    private JButton btnNew;

    private JButton btnOpen;

    private JButton btnSave;

    private JButton btnProperties;

    private JButton btnAddDiagram;

    private JButton btnRemoveDiagram;

    private JButton btnGenerate;

    private JButton btnRunGenerated;

    private SimpleInternalFrame simpleInternalFrame1;

    private JScrollPane panelExplorer;

    private JComponent separator1;

    private JScrollPane panelProject;

    private JTable tableProject;

    private JComponent separator2;

    private JScrollPane panelElement;

    private JTable tableElement;

    private SimpleInternalFrame panelRight;

    private JPanel panelRightContainer;

    private SimpleInternalFrame panelConsole;

    private JLabel statusText;

    /**
	 * Gets the panel explorer.
	 * 
	 * @return the panel explorer
	 */
    public JScrollPane getPanelExplorer() {
        return panelExplorer;
    }

    /**
	 * Gets the tab center.
	 * 
	 * @return the tab center
	 */
    public CloseableTabbedPane getTabCenter() {
        return tabbedPane;
    }

    /**
	 * Gets the table project.
	 * 
	 * @return the table project
	 */
    public JTable getTableProject() {
        return tableProject;
    }

    /**
	 * Gets the panel element.
	 * 
	 * @return the panel element
	 */
    public JScrollPane getPanelElement() {
        return panelElement;
    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PropertyType.PROJECT_MODIFIED.toString())) {
            if (!evt.getOldValue().equals(this)) {
                updateVista();
            }
        }
    }

    @Override
    public void setModelo(final Object modelo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateVista() {
        log.error("Not implemented yet");
    }

    @Override
    public String getTitulo() {
        return "Tiergen : " + getProyecto().getNombre();
    }

    /**
	 * Inits the.
	 */
    private void init() {
        getTabCenter().clearAllTabs();
        controllerProyecto.clear();
        controllerProyecto.addModel(proyecto);
        controllerProyecto.addView(arbol);
        arbol.setModelo(proyecto.getModelo());
        getArbol().updateVista();
        getPanelExplorer().updateUI();
        addProjectView();
        getPanelElement().setViewportView((JTable) propertyViewer);
    }

    /**
	 * Inits the eventos.
	 */
    private void initEventos() {
        getTabbedPane().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("removing_tab")) {
                    final PanelGrDiagrama panel = (PanelGrDiagrama) evt.getOldValue();
                    controllerProyecto.removeView(panel.getDiagrama());
                }
            }
        });
    }

    /**
	 * Adds the project view.
	 */
    private void addProjectView() {
        final Hashtable properties = proyecto.getProperties();
        getTableProject().setModel(new PropertyTableModel(properties));
        controllerProyecto.addView(new Vista() {

            @Override
            public void setModelo(final Object modelo) {
            }

            @Override
            public void updateVista() {
            }

            @Override
            public String getTitulo() {
                return "";
            }

            @Override
            public void modelPropertyChange(final PropertyChangeEvent evt) {
                final PropertyTableModel tableModel = (PropertyTableModel) getTableProject().getModel();
                tableModel.fireTableDataChanged();
            }
        });
    }

    /**
	 * Nuevo.
	 */
    private void nuevo() {
        try {
            saveConfirmando();
            proyecto.newProyecto();
            init();
            controllerProyecto.changeModelo(this);
            setMensaje("Nuevo proyecto generado: " + proyecto.getNombre());
            setTitle("Tiergen " + proyecto.getNombre());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
	 * TODO.
	 */
    private void open() {
        final Runnable thread = new Runnable() {

            @Override
            public void run() {
                saveConfirmando();
                final File file = AppUtil.showAbrirCarpeta(null, "Abrir proyecto", Configuracion.getParam("last_path"));
                if (file != null) {
                    WaitDialog.showDialog();
                    try {
                        proyecto.closeProyecto();
                        proyecto.openProyecto(file);
                        init();
                        controllerProyecto.changeModelo(this);
                        Configuracion.setParam("last_path", proyecto.getPath());
                        setTitle("Tiergen " + getProyecto().getNombre());
                        setMensaje("Proyecto cargado: " + proyecto.getNombre());
                    } catch (NullPointerException e) {
                        log.error(e.getMessage(), e);
                        setMensaje("Fichero invalido: " + e.getMessage());
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                        setMensaje("Fichero invalido: " + e.getMessage());
                    }
                    WaitDialog.hideDialog();
                } else {
                    setMensaje("Directorio invalido");
                }
            }
        };
        thread.run();
    }

    /**
	 * Save.
	 */
    private void save() {
        try {
            WaitDialog.showDialog();
            if (proyecto.isActive() && proyecto.getPath().isEmpty()) {
                saveComo();
            } else {
                getProyecto().saveProyecto();
                setMensaje("Proyecto almacenado: " + proyecto.getPath());
            }
        } catch (IOException e) {
            log.error(e);
        } catch (NullPointerException e) {
            log.error(e.getMessage(), e);
        }
        WaitDialog.hideDialog();
    }

    /**
	 * Save como.
	 */
    private void saveComo() {
        try {
            final File file = AppUtil.showGuardarCarpeta(this, "Guardar proyecto", "");
            if (file != null) {
                WaitDialog.showDialog();
                proyecto.saveComo(file);
                Configuracion.setParam("last_path", file.getPath());
                setMensaje("Proyecto almacenado: " + proyecto.getPath());
            }
        } catch (NullPointerException e) {
            log.error(e.getMessage(), e);
            setMensaje("Fichero invalido: " + e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            setMensaje("Fichero invalido: " + e.getMessage());
        }
        WaitDialog.hideDialog();
    }

    /**
	 * Save confirmando.
	 */
    private void saveConfirmando() {
        if (proyecto.isModified()) {
            if (AppUtil.showConfirmar(this, "Cerrar proyecto", "Desea guardar el proyecto actual?")) {
                save();
            }
        }
    }

    /**
	 * Edits the project.
	 */
    private void editProject() {
        if (proyecto.isActive()) {
            final EditorProyecto dialog = new EditorProyecto(getControllerProyecto(), this, true);
            dialog.setVisible(true);
            setTitle("Tiergen " + getProyecto().getNombre());
        }
    }

    /**
	 * Export xmi.
	 */
    private void exportXMI() {
        try {
            if (!proyecto.isActive()) {
                return;
            }
            final File f = AppUtil.showGuardar(this, "Exportar", "xmi");
            if (f == null) {
                AppUtil.showAdvertencia(this, "Fichero invalido");
            } else {
                WaitDialog.showDialog();
                getProyecto().exportar(f);
                setMensaje("Modelo exportado: " + f.getPath());
            }
        } catch (NullPointerException e) {
            setMensaje("Fichero invalido: " + e.getMessage());
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            setMensaje("Fichero invalido: " + e.getMessage());
        }
        WaitDialog.hideDialog();
    }

    /**
	 * Import xmi.
	 */
    private void importXMI() {
        saveConfirmando();
        try {
            final File importFile = AppUtil.showAbrir(this, "Importar", "xmi", Configuracion.getParam(Constantes.LAST_PATH));
            if (importFile != null) {
                WaitDialog.showDialog();
                proyecto.closeProyecto();
                proyecto.importar(importFile);
                init();
                controllerProyecto.changeModelo(this);
                setMensaje("Modelo importado: " + importFile.getPath());
                setTitle("Tiergen " + getProyecto().getNombre());
            }
        } catch (NullPointerException e) {
            setMensaje("Fichero invalido: " + e.getMessage());
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            AppUtil.showError(this, e.getMessage());
        } catch (Exception e) {
            AppUtil.showError(this, e.getMessage());
        }
        WaitDialog.hideDialog();
    }

    /**
	 * Adds the elemento.
	 */
    private void addElemento() {
        if (getArbol().getSelectionPath() != null) {
            final NodoExplorador nodo = (NodoExplorador) getArbol().getSelectionPath().getLastPathComponent();
            if (nodo.getElemento() != null && nodo.getElemento() instanceof UmlPackage) {
                final Object[] mensaje = new Object[2];
                mensaje[0] = "Seleccione el elemento a agregar";
                final JComboBox combo = new JComboBox();
                combo.setEditable(true);
                combo.addItem("Clase");
                combo.addItem("Paquete");
                combo.addItem("Interfaz");
                mensaje[1] = combo;
                final String[] opciones = new String[] { "Aceptar", "Cancelar" };
                final int result = JOptionPane.showOptionDialog(this, mensaje, "Agregar elemento", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
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
                        default:
                    }
                    ele.setNamespace((Namespace) nodo.getElemento());
                    getArbol().updateVista();
                    setMensaje("Elemento agregado:" + ele.getName());
                }
            } else {
                AppUtil.showAdvertencia(this, "Debe seleccionar un paquete");
            }
        } else {
            AppUtil.showAdvertencia(this, "Debe seleccionar un paquete en el arbol primero");
        }
    }

    /**
	 * Edits the elemento.
	 */
    private void editElemento() {
        final int index = getTabCenter().getSelectedIndexTab();
        if (index != -1) {
            final List<GrNodoVisual> selection = getContainer().getGraficador(index).getSelectedNodos();
            if (!selection.isEmpty()) {
                selection.get(0).edit();
            }
        } else if (getArbol().getSelectionPath() != null) {
            final NodoExplorador nodo = (NodoExplorador) getArbol().getSelectionPath().getLastPathComponent();
            if (nodo != null) {
                try {
                    nodo.editElemento();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    AppUtil.showAdvertencia(this, e.getMessage());
                }
            }
        } else {
            AppUtil.showAdvertencia(this, "Debe seleccionar un elemento en el arbol");
        }
    }

    /**
	 * Removes the elemento.
	 */
    private void removeElemento() {
        if (getArbol().getSelectionPath() != null) {
            final NodoExplorador nodo = (NodoExplorador) getArbol().getSelectionPath().getLastPathComponent();
            if (nodo != null) {
                nodo.remove();
                setMensaje("Elemento eliminado ");
            }
        } else {
            AppUtil.showAdvertencia(this, "Debe seleccionar un elemento en el arbol");
        }
    }

    /**
	 * Adds the diagrama.
	 */
    private void addDiagrama() {
        if (getArbol().getSelectionPath() != null) {
            final NodoExplorador nodo = (NodoExplorador) getArbol().getSelectionPath().getLastPathComponent();
            if (nodo != null) {
                nodo.addDiagrama();
            }
        } else {
            AppUtil.showAdvertencia(this, "Debe seleccionar un lugar en el arbol primero");
        }
    }

    /**
	 * Removes the diagrama.
	 */
    private void removeDiagrama() {
        if (getArbol().getSelectionPath() != null) {
            final NodoExplorador nodo = (NodoExplorador) getArbol().getSelectionPath().getLastPathComponent();
            if (nodo != null && nodo.getDiagrama() != null) {
                nodo.remove();
                setMensaje("Diagrama eliminado");
            } else {
                AppUtil.showAdvertencia(this, "No es un diagrama");
            }
        } else {
            AppUtil.showAdvertencia(this, "Debe seleccionar un diagrama");
        }
    }

    /**
	 * Salir.
	 */
    private void salir() {
        if (proyecto.isModified()) {
            if (AppUtil.showConfirmar(this, "Salir de la aplicacion", "Desea guardar el proyecto actual?")) {
                save();
            }
        }
        log.info("Shutting down repository");
        getProyecto().closeProyecto();
        repository.shutdown();
        System.exit(0);
    }

    /**
	 * Help.
	 */
    private void help() {
        final HelpBroker broker = HelpUtil.getInstance().getBroker();
        broker.setCurrentID("installation");
        broker.setDisplayed(true);
    }

    private void showInfo() {
        JAbout infoDialog = new JAbout(this);
        infoDialog.setModal(true);
        infoDialog.setTitle(Constantes.APPLICATION_SHORT_NAME);
        infoDialog.setComments("CASE application to generate EJB Persistence entities");
        infoDialog.setVersion("0.1");
        infoDialog.setAuthor("Juan Timoteo Ponce Ortiz");
        infoDialog.setWeb("http://timoponce.blogspot.com", "Timoteo Ponce's Blog");
        infoDialog.setVisible(true);
    }

    /**
	 * Sets the mensaje.
	 * 
	 * @param str
	 *            the str
	 * @param busy
	 *            the busy
	 */
    private void setMensaje(final String str) {
        log.info(str);
        statusText.setText(str);
    }

    /**
	 * Generar codigo.
	 */
    private void generarCodigo() {
        final Diagram currentDiagram = getCurrentDiagram();
        if (currentDiagram != null) {
            final UIGenerador dialog = new UIGenerador(false, this, true, currentDiagram, proyecto);
            dialog.setVisible(true);
        } else {
            AppUtil.showAdvertencia(this, "No existe un diagrama seleccionado");
        }
    }

    /**
	 * Edits the generacion.
	 */
    private void editGeneracion() {
        final Diagram currentDiagram = getCurrentDiagram();
        if (currentDiagram != null) {
            final UIGenerador dialog = new UIGenerador(true, this, true, currentDiagram, proyecto);
            dialog.setVisible(true);
        } else {
            AppUtil.showAdvertencia(this, "No existe un diagrama seleccionado");
        }
    }

    /**
	 * Run generated.
	 */
    private void runGenerated() {
        final Diagram diagram = getCurrentDiagram();
        if (diagram == null) {
            AppUtil.showMensaje(this, "No existe un diagrama seleccionado");
            return;
        }
        final UIRunAplicacion dialog = new UIRunAplicacion(this);
        dialog.setAntProjectPath(proyecto.getGenPath() + "/" + diagram.getName().toLowerCase().replace(" ", "_"));
        dialog.init();
        dialog.loadProperties();
        dialog.setVisible(true);
    }

    /**
	 * Find paths.
	 */
    private void findPaths() {
        final int index = getTabCenter().getSelectedIndexTab();
        if (index != -1) {
            final List<GrNodoVisual> selection = getContainer().getGraficador(index).getSelectedNodos();
            if (selection.isEmpty()) {
                return;
            }
            final GraphNode node = selection.get(0).getNodoLogico();
            final ModelElement element = ElementoVisualUtil.getElementoLogico(node);
            try {
                getContainer().findRelaciones(element);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            AppUtil.showAdvertencia(this, "Debe seleccionar un elemento en el diagrama");
        }
    }

    /**
	 * Export to image.
	 * 
	 * @param copyToClipboard
	 *            the copy to clipboard
	 */
    private void exportToImage(final boolean copyToClipboard) {
        final PanelGrDiagrama panel = (PanelGrDiagrama) getTabCenter().getSelectedTab();
        if (panel == null) {
            AppUtil.showAdvertencia(this, "Debe seleccionar un diagrama");
            return;
        }
        final Image image = panelToImage(panel.getDiagrama());
        if (copyToClipboard) {
            copyImageToClipboard(image);
            setMensaje("Diagrama exportado a imagen");
        } else {
            File file = AppUtil.showGuardar(this, "Guardar imagen", "png");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                ImageIO.write((RenderedImage) image, "png", file);
                setMensaje("Saving image to: " + file.getPath());
            } catch (IOException e) {
                log.error("Can't save image: " + e);
            }
        }
    }

    /**
	 * Panel to image.
	 * 
	 * @param panel
	 *            the panel
	 * 
	 * @return the image
	 */
    public static Image panelToImage(final JPanel panel) {
        final BufferedImage image = (BufferedImage) panel.createImage(panel.getWidth(), panel.getHeight());
        final Graphics graphs = image.getGraphics();
        panel.paint(graphs);
        return image;
    }

    /**
	 * Copy image to clipboard.
	 * 
	 * @param image
	 *            the image
	 */
    public void copyImageToClipboard(final Image image) {
        final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        final ImageSelection dh = new ImageSelection(image);
        cb.setContents(dh, null);
    }

    /**
	 * Prints the.
	 */
    private void print() {
        final PanelGrDiagrama panel = (PanelGrDiagrama) getTabCenter().getSelectedTab();
        if (panel == null) {
            AppUtil.showAdvertencia(this, "Debe seleccionar un diagrama");
            return;
        }
        final Image image = panelToImage(panel.getDiagrama());
        try {
            final File tempFile = new File("img.tmp");
            ImageIO.write((RenderedImage) image, "png", tempFile);
            log.info("Saving temporal image to: " + tempFile.getPath());
            final PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new Copies(1));
            final PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
            if (pss.length == 0) {
                throw new RuntimeException("No printer services available.");
            }
            final PrintService ps = pss[0];
            System.out.println("Printing to " + ps);
            DocPrintJob job = ps.createPrintJob();
            final FileInputStream fin = new FileInputStream(tempFile);
            final Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.GIF, null);
            job.print(doc, pras);
            fin.close();
        } catch (IOException ie) {
            log.error(ie);
        } catch (PrintException pe) {
            log.error(pe);
        }
    }

    /**
	 * Gets the current diagram.
	 * 
	 * @return the current diagram
	 */
    public Diagram getCurrentDiagram() {
        if (getTabCenter().getSelectedIndexTab() != -1) {
            final GrDiagrama grDiagrama = container.getGraficador(getTabCenter().getSelectedIndexTab());
            if (grDiagrama != null) {
                return grDiagrama.getDiagram();
            }
        }
        return null;
    }
}
