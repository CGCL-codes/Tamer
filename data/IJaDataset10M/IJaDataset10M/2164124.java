package es.uma.ama.maudeWorkstationGUI.ui.views;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.internal.Workbench;
import es.uma.ama.maudeWorkstationGUI.MaudeWorkstationGUIPlugin;
import es.uma.ama.maudeWorkstationGUI.Messages;
import es.uma.ama.maudeWorkstationGUI.ui.states.MaudeSesion;

/**
 * It creates a dialog where you can select an apropiate option.
 * 
 * @author Alfredo
 *
 */
public class MaudeDialogoEleccion {

    private Shell sShell = null;

    private Composite composite = null;

    private Composite compositeBuscar = null;

    private Button buttonCerrar = null;

    private Tree tree = null;

    private Label labelBuscar = null;

    private Text textBuscar = null;

    private String nombre, comando, comando2;

    private List<TreeItem> listaHojas;

    private List<String> opsIntroducidos;

    public static final String TRACE = "trace";

    public static final String SELECT = "select";

    public static final String DESELECT = "deselect";

    public static final String EXCLUDE = "exclude";

    public static final String INCLUDE = "include";

    public static final String BREAK = "break";

    public Shell getShell() {
        return sShell;
    }

    public MaudeDialogoEleccion(String title, String nomb, String command, String command2) {
        comando = command;
        comando2 = command2;
        nombre = nomb;
        listaHojas = new LinkedList<TreeItem>();
        opsIntroducidos = new LinkedList<String>();
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.marginWidth = 5;
        gridLayout2.numColumns = 1;
        gridLayout2.horizontalSpacing = 5;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.CENTER;
        gridData3.verticalAlignment = GridData.CENTER;
        Display d = UIPlugin.getDefault().getWorkbench().getDisplay();
        sShell = new Shell(d, SWT.CLOSE | SWT.TITLE | SWT.TOOL | SWT.MODELESS);
        sShell.setText(title);
        createCompositeBuscar();
        createComposite();
        sShell.setLayout(gridLayout2);
        sShell.setSize(new Point(216, 416));
        Point p = d.getActiveShell().getLocation();
        sShell.setLocation(p.x + 400, p.y + 100);
        buttonCerrar = new Button(sShell, SWT.NONE);
        buttonCerrar.setText("Cerrar");
        buttonCerrar.setLayoutData(gridData3);
        buttonCerrar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                sShell.close();
            }
        });
    }

    /**
	 * This method initializes composite	
	 *
	 */
    private void createComposite() {
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 1;
        gridLayout1.verticalSpacing = 5;
        gridLayout1.marginWidth = 16;
        gridLayout1.marginHeight = 16;
        gridLayout1.horizontalSpacing = 5;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.heightHint = 100;
        gridData1.widthHint = 100;
        gridData1.horizontalIndent = 0;
        gridData1.verticalAlignment = GridData.FILL;
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        composite = new Composite(sShell, SWT.NONE);
        composite.setLayoutData(gridData);
        composite.setLayout(gridLayout1);
        tree = new Tree(composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
        tree.setLayoutData(gridData1);
        tree.addTreeListener(new TreeListener() {

            public void treeExpanded(TreeEvent e) {
                TreeItem ti = (TreeItem) e.item;
                ti.setImage(MaudeWorkstationGUIPlugin.getImage(Messages.getString("MaudeWorkstationGUIPlugin.DIALOGO_ELECCION_PATH_FOLDER_OPEN")));
            }

            public void treeCollapsed(TreeEvent e) {
                TreeItem ti = (TreeItem) e.item;
                ti.setImage(MaudeWorkstationGUIPlugin.getImage(Messages.getString("MaudeWorkstationGUIPlugin.DIALOGO_ELECCION_PATH_FOLDER")));
            }
        });
        tree.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                TreeItem ti = (TreeItem) e.item;
                sendCommandToMaude(ti);
            }
        });
    }

    private void sendCommandToMaude(TreeItem ti) {
        if (ti.getItemCount() > 0 || ti.getData() == null) {
            ti.setChecked(false);
            return;
        }
        boolean b = (Boolean) ti.getData();
        if (b) {
            MaudeWorkstationGUIPlugin.getDefault().sendToMaude(nombre + " " + comando2 + " " + ti.getText() + " .");
            ti.setChecked(false);
            ti.setData(false);
            if (nombre.equals(TRACE) && comando.equals(SELECT)) {
                MaudeSesion.newInstance().getTrazaState().removeOpersSelected(ti.getText());
            } else if (nombre.equals(BREAK) && comando.equals(SELECT)) {
                MaudeSesion.newInstance().getDepuracionState().removeOpersBreakpoint(ti.getText());
            } else if (nombre.equals(TRACE) && comando.equals(EXCLUDE)) {
                MaudeSesion.newInstance().getTrazaState().removeModuloExcluido(ti.getText());
            }
            seleccionarOperadoresConNombre(ti.getText(), false);
        } else {
            MaudeWorkstationGUIPlugin.getDefault().sendToMaude(nombre + " " + comando + " " + ti.getText() + " .");
            ti.setData(true);
            ti.setChecked(true);
            if (nombre.equals(TRACE) && comando.equals(SELECT)) {
                MaudeSesion.newInstance().getTrazaState().addOpersSelected(ti.getText());
            } else if (nombre.equals(BREAK) && comando.equals(SELECT)) {
                MaudeSesion.newInstance().getDepuracionState().addOpersBreakpoint(ti.getText());
            } else if (nombre.equals(TRACE) && comando.equals(EXCLUDE)) {
                MaudeSesion.newInstance().getTrazaState().addModuloExcluido(ti.getText());
            }
            seleccionarOperadoresConNombre(ti.getText(), true);
        }
    }

    /**
	 * This method initializes compositeBuscar	
	 *
	 */
    private void createCompositeBuscar() {
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.verticalAlignment = GridData.CENTER;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.CENTER;
        gridData2.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        compositeBuscar = new Composite(sShell, SWT.NONE);
        compositeBuscar.setLayout(gridLayout);
        compositeBuscar.setLayoutData(gridData2);
        labelBuscar = new Label(compositeBuscar, SWT.NONE);
        labelBuscar.setText("Select/Deselect");
        textBuscar = new Text(compositeBuscar, SWT.BORDER);
        textBuscar.setLayoutData(gridData1);
        textBuscar.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.keyCode == 13) {
                    String elemento = textBuscar.getText();
                    final TreeItem item = buscarOperadoresConNombre(elemento);
                    if (item == null) MessageDialog.openWarning(Workbench.getInstance().getDisplay().getActiveShell(), "Warning", "The operator " + elemento + " isn't declared in this module."); else {
                        sendCommandToMaude(item);
                        Thread hebra = new Thread() {

                            public void run() {
                                Workbench.getInstance().getDisplay().syncExec(new Runnable() {

                                    public void run() {
                                        labelBuscar.setText(((Boolean) item.getData()) ? "Selected" : "Deselected");
                                    }
                                });
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Workbench.getInstance().getDisplay().syncExec(new Runnable() {

                                    public void run() {
                                        labelBuscar.setText("Select/Deselect");
                                    }
                                });
                            }
                        };
                        hebra.start();
                    }
                }
            }
        });
    }

    private TreeItem addHijo(Tree padre, String contenido) {
        TreeItem it = new TreeItem(padre, SWT.NONE);
        it.setText(contenido);
        it.setImage(MaudeWorkstationGUIPlugin.getImage(Messages.getString("MaudeWorkstationGUIPlugin.DIALOGO_ELECCION_PATH_FOLDER")));
        return it;
    }

    private TreeItem addHijo(TreeItem padre, String contenido) {
        TreeItem it = new TreeItem(padre, SWT.NONE);
        it.setText(contenido);
        it.setImage(MaudeWorkstationGUIPlugin.getImage(Messages.getString("MaudeWorkstationGUIPlugin.DIALOGO_ELECCION_PATH_FOLDER")));
        return it;
    }

    private TreeItem addHijo(TreeItem padre, String contenido, boolean selected) {
        opsIntroducidos.add(contenido);
        TreeItem it = new TreeItem(padre, SWT.NONE);
        it.setText(contenido);
        it.setChecked(selected);
        it.setData(selected);
        listaHojas.add(it);
        return it;
    }

    private TreeItem addHijo(Tree padre, String contenido, boolean selected) {
        if (!opsIntroducidos.contains(contenido)) {
            opsIntroducidos.add(contenido);
            TreeItem it = new TreeItem(padre, SWT.NONE);
            it.setText(contenido);
            it.setChecked(selected);
            it.setData(selected);
            listaHojas.add(it);
            return it;
        } else return null;
    }

    public void show() {
        sShell.setVisible(true);
        sShell.setFocus();
    }

    /** * a�ade un modulo a este dialogo
		 * Nota:suponemos que se a�aden los modulos sin seleccionar por lo que no se envia msje a maude */
    private void anadirModulo(String elem, TreeItem padre) {
        addHijo(padre, elem, MaudeSesion.newInstance().getTrazaState().containsModuloExcluido(elem));
    }

    /** * a�ade un operador a este dialogo
		 * Nota: suponemos que se a�aden sin seleccionar por lo que no se envia msje a maude
		 * @param cabeza la cabeza del operador op <cabeza> : <cola>
		 * @param cola la cola del operador
		 * @param modulo el modulo al que pertenece el operador
		 **/
    private void anadirOperador(String cabeza, String cola, String modulo, Tree padre) {
        if (nombre.equals(TRACE) && comando.equals(SELECT)) {
            addHijo(padre, cabeza, MaudeSesion.newInstance().getTrazaState().containsOpersSelected(cabeza));
        } else if (nombre.equals(BREAK) && comando.equals(SELECT)) {
            addHijo(padre, cabeza, MaudeSesion.newInstance().getDepuracionState().containsOpersBreakpoint(cabeza));
        }
    }

    /** A�ade una etiqueta a este dialogo 
		 * Nota: suponemos que se a�aden sin seleccionar por lo que no se envia msje a maude
		 */
    private void anadirEtiqueta(String etiqueta, String modulo, TreeItem padre) {
        if (nombre.equals(TRACE) && comando.equals(SELECT)) {
            addHijo(padre, etiqueta, MaudeSesion.newInstance().getTrazaState().containsOpersSelected(etiqueta));
        } else if (nombre.equals(BREAK) && comando.equals(SELECT)) {
            addHijo(padre, etiqueta, MaudeSesion.newInstance().getDepuracionState().containsOpersBreakpoint(etiqueta));
        }
    }

    /** * selecciona o deselecciona todos los operadores con el mismo nombre 
		 * @param nombre el nombre del operador
		 * @param select si esta activo selecciona los operadores */
    private void seleccionarOperadoresConNombre(String nombre, boolean selected) {
        Iterator<TreeItem> iter = listaHojas.iterator();
        while (iter.hasNext()) {
            TreeItem it = iter.next();
            if (it.getText().equals(nombre)) {
                it.setChecked(selected);
                it.setData(selected);
            }
        }
    }

    /** * selecciona o deselecciona todos los operadores con el mismo nombre 
		 * @param nombre el nombre del operador
		 * @param select si esta activo selecciona los operadores */
    private TreeItem buscarOperadoresConNombre(String nombre) {
        Iterator<TreeItem> iter = listaHojas.iterator();
        while (iter.hasNext()) {
            TreeItem it = iter.next();
            if (it.getText().equals(nombre)) {
                return it;
            }
        }
        return null;
    }

    /** * a�ade las etiquetas del modulo actual */
    public void addDatosEtiquetasModulos() {
        Set cjtoEtiquetas = MaudeWorkstationGUIPlugin.getDefault().getDatabase().getCjtoEtiquetasModulo(MaudeSesion.newInstance().getRewritingState().getActiveModule());
        TreeItem padre = addHijo(tree, Messages.getString("MaudeWorkstationGUIPlugin.DIALOGO_ELECCION_LABELS"));
        if (MaudeWorkstationGUIPlugin.DEBUG) System.out.println("Cjto Etiquetas: " + cjtoEtiquetas.toString());
        addEtiquetas(MaudeSesion.newInstance().getRewritingState().getActiveModule(), cjtoEtiquetas, padre);
        cjtoModulosEtiquetasVisitados = new HashSet<String>();
        cjtoModulosEtiquetasVisitados.add(MaudeSesion.newInstance().getRewritingState().getActiveModule());
        addDatosEtiquetasModulosRecursivo(MaudeSesion.newInstance().getRewritingState().getActiveModule(), padre);
    }

    private Set<String> cjtoModulosEtiquetasVisitados;

    private void addDatosEtiquetasModulosRecursivo(String nomModulo, TreeItem padre) {
        Set cjtoModulos = MaudeWorkstationGUIPlugin.getDefault().getDatabase().getCjtoIEPModulo(nomModulo);
        if (cjtoModulos == null) {
        } else {
            Iterator itCjto = cjtoModulos.iterator();
            while (itCjto.hasNext()) {
                String nomHere = (String) itCjto.next();
                if (!cjtoModulosEtiquetasVisitados.contains(nomHere)) {
                    Set cjtoEtiquetas = MaudeWorkstationGUIPlugin.getDefault().getDatabase().getCjtoEtiquetasModulo(nomHere);
                    addEtiquetas(nomHere, cjtoEtiquetas, padre);
                    cjtoModulosEtiquetasVisitados.add(nomHere);
                    addDatosEtiquetasModulosRecursivo(nomHere, padre);
                }
            }
        }
    }

    private void addEtiquetas(String nomMod, Set cjtoEtiquetas, TreeItem padre) {
        if (cjtoEtiquetas == null || cjtoEtiquetas.isEmpty()) {
        } else {
            TreeItem padreMod = this.addHijo(padre, nomMod);
            Iterator it = cjtoEtiquetas.iterator();
            while (it.hasNext()) {
                this.anadirEtiqueta((String) it.next(), nomMod, padreMod);
            }
        }
    }

    /** * a�ade los operadores del modulo actual */
    public void addDatosOperadoresModulos() {
        Set cjtoOpers = MaudeWorkstationGUIPlugin.getDefault().getDatabase().getCjtoOpersModulo(MaudeSesion.newInstance().getRewritingState().getActiveModule());
        addOperadores(MaudeSesion.newInstance().getRewritingState().getActiveModule(), cjtoOpers, tree);
    }

    private void addOperadores(String nomMod, Set cjtoOpers, Tree padre) {
        if (cjtoOpers == null) {
        } else {
            Iterator it = cjtoOpers.iterator();
            while (it.hasNext()) {
                String operador = (String) it.next();
                StringTokenizer stok = new StringTokenizer(operador, "�");
                this.anadirOperador(stok.nextToken(), stok.nextToken(), nomMod, padre);
            }
        }
    }

    /** * Adds the modules clasified for the database */
    public void addDatosModulos() {
        List listaModulosPredefCore = MaudeWorkstationGUIPlugin.getDefault().getDatabase().getListaModulosPredefinidosCore();
        if (!listaModulosPredefCore.isEmpty()) {
            TreeItem padre = addHijo(tree, Messages.getString("MaudeWorkstationGUIPlugin.DIALOGO_ELECCION_PREDEFINED_CORE_MAUDE"));
            Iterator it = listaModulosPredefCore.iterator();
            while (it.hasNext()) {
                String nomMod = (String) it.next();
                this.anadirModulo(nomMod, padre);
            }
        }
        List listaModulosPredefFull = MaudeWorkstationGUIPlugin.getDefault().getDatabase().getListaModulosPredefinidosFull();
        if (!listaModulosPredefFull.isEmpty()) {
            TreeItem padre = addHijo(tree, Messages.getString("MaudeWorkstationGUIPlugin.DIALOGO_ELECCION_PREDEFINED_FULL_MAUDE"));
            Iterator it = listaModulosPredefFull.iterator();
            while (it.hasNext()) {
                String nomMod = (String) it.next();
                this.anadirModulo(nomMod, padre);
            }
        }
    }
}
