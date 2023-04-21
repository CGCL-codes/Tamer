package org.gvsig.remotesensing.calculator.gui;

import info.clearthought.layout.TableLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.remotesensing.calculator.gui.listener.CalculatorPanelListener;
import org.nfunk.jep.JEP;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Panel que implementa el comportamiento b�sico de una calculadora
 * cuyas variables pueden ser asociadas a las bandas de las capas raster
 * cargadas en una vista.
 * 
 * @author Alejandro Mu�oz Sanchez	(alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007 
 */
public class CalculatorPanel extends JPanel implements FocusListener {

    private static final long serialVersionUID = 1L;

    private JButton salvar;

    private JButton recuperar;

    private JPanel centerPanel;

    private JPanel expresionPanel;

    private JTextArea jTextExpresion;

    private JScrollPane scrollExpresion;

    private JScrollPane scrollTree;

    private JScrollPane scrollVariables;

    private JTree jtree;

    private JCheckBox jCheckExtent;

    private KeysPanel kp;

    private TableFormat jTableVariables;

    private MapContext m_MapContext = null;

    private View view = null;

    private HashMap m_Constants;

    private HashMap qWindowsHash = null;

    /**
	 * Elementos de la tabla de variablas que debe permanecer fijo
	 * independientemente la expresi�n.
	 */
    private HashMap persistentVarTable = null;

    private final String nombreBandas = "Band";

    private JEP parser = null;

    private CalculatorPanelListener listener = null;

    private boolean checkVisible = true;

    /**
	 * @param calculatorDialog
	 * @param view	vista de la aplicacion 
	 */
    public CalculatorPanel(View view) {
        super();
        this.view = view;
        if (view != null) m_MapContext = view.getModel().getMapContext();
        listener = new CalculatorPanelListener(this);
        Inicializar();
    }

    /**
	 * @param calculatorDialog
	 * @param view	vista de la aplicacion 
	 */
    public CalculatorPanel(View view, boolean checkVisible) {
        super();
        this.checkVisible = checkVisible;
        this.view = view;
        if (view != null) m_MapContext = view.getModel().getMapContext();
        listener = new CalculatorPanelListener(this);
        Inicializar();
    }

    /**
	 * Inicializar los elementos del Panel CalculatorPanel
	 */
    private void Inicializar() {
        JPanel principalPanel = new JPanel();
        BorderLayout bd = new BorderLayout();
        principalPanel.setLayout(bd);
        kp = new KeysPanel(getJTextExpression());
        principalPanel.add(getCenterPanel(), BorderLayout.CENTER);
        principalPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        MouseListener m = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                jTableVariables.setEnEspera(false);
            }
        };
        kp.getJButton0().addMouseListener(m);
        kp.getJButton1().addMouseListener(m);
        kp.getJButton2().addMouseListener(m);
        kp.getJButton3().addMouseListener(m);
        kp.getJButton4().addMouseListener(m);
        kp.getJButton5().addMouseListener(m);
        kp.getJButton6().addMouseListener(m);
        kp.getJButton7().addMouseListener(m);
        kp.getJButton8().addMouseListener(m);
        kp.getJButton9().addMouseListener(m);
        kp.getJButtonMinus().addMouseListener(m);
        kp.getJButtonPlus().addMouseListener(m);
        kp.getJButtonBrackets().addMouseListener(m);
        kp.getJButtonDivide().addMouseListener(m);
        kp.getJButtonMultiply().addMouseListener(m);
        kp.getJButtonDot().addMouseListener(m);
        this.setLayout(new BorderLayout());
        this.add(principalPanel, BorderLayout.CENTER);
    }

    /**
	 * Define los elementos del panel central: JTree, Tabla de Variables y calculadora
	 * @return  panel central
	 */
    public JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(2, 2, 0, 2);
            c.weightx = 0.75;
            c.gridx = 0;
            c.gridy = 0;
            getScrollTree().setSize(50, 70);
            centerPanel.add(getScrollTree(), c);
            c.insets = new Insets(2, 2, 0, 2);
            c.weightx = 1.0;
            c.gridx = 1;
            c.gridy = 0;
            getScrollVariables().setSize(50, 70);
            getJTableVariables().getTableFormat().getColumn(PluginServices.getText(this, "valor")).setPreferredWidth(180);
            centerPanel.add(getScrollVariables(), c);
            c.weightx = 0.0;
            c.gridx = 2;
            c.gridy = 0;
            centerPanel.add(kp, c);
            c.insets = new Insets(2, 2, 0, 2);
            c.weightx = 1.0;
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 1;
            if (checkVisible) centerPanel.add(getJCheckExtent(), c);
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(2, 2, 0, 2);
            c.weightx = 1.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 2;
            centerPanel.add(getExpresionPanel(), c);
        }
        return centerPanel;
    }

    /**
	 * Crea un panel en el que se agrega el area de texto donde se define la expresi�n.
	 * @return panel con el area de texto donde se define la expresi�n
	 */
    public JPanel getExpresionPanel() {
        if (expresionPanel == null) {
            expresionPanel = new JPanel();
        }
        expresionPanel.setLayout(new BorderLayout());
        expresionPanel.add(getScrollExpresion(), BorderLayout.CENTER);
        JPanel p = new JPanel();
        TableLayout thisLayout = new TableLayout(new double[][] { { 5.0, TableLayout.PREFERRED }, { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } });
        thisLayout.setHGap(3);
        thisLayout.setVGap(3);
        p.setLayout(thisLayout);
        TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this, "expression"));
        topBorder.setTitlePosition(TitledBorder.TOP);
        expresionPanel.setBorder(new CompoundBorder(topBorder, new EmptyBorder(0, 0, 0, 0)));
        p.add(getSalvar(), "1,0");
        p.add(getRecuperar(), "1,1");
        expresionPanel.add(p, BorderLayout.EAST);
        return expresionPanel;
    }

    /**
	 * @return checkbox permitir distinto extend
	 */
    public JCheckBox getJCheckExtent() {
        if (jCheckExtent == null) {
            jCheckExtent = new JCheckBox(PluginServices.getText(this, "distinto_extent"), false);
            jCheckExtent.addActionListener(listener);
        }
        return jCheckExtent;
    }

    /**
	 * @return arbol con capas, funciones, operadores y constantes
	 */
    public JTree getJtree() {
        if (jtree == null) {
            jtree = new JTree();
            jtree.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            if (m_MapContext != null) this.InicizlizaJTree(jtree);
        }
        return jtree;
    }

    /**
	 * @return area de texto donde se intoduce la expresi�n
	 */
    public JTextArea getJTextExpression() {
        if (jTextExpresion == null) {
            jTextExpresion = new JTextArea();
            jTextExpresion.setLineWrap(true);
            jTextExpresion.setWrapStyleWord(true);
            jTextExpresion.addFocusListener(this);
            jTextExpresion.addKeyListener(new CalculatorPanelListener(this));
        }
        return jTextExpresion;
    }

    /**
	 *  @return formato de la tabla
	 */
    public TableFormat getJTableVariables() {
        if (jTableVariables == null) {
            jTableVariables = new TableFormat(jTextExpresion);
        }
        return jTableVariables;
    }

    /**
	* @return  scroll con el arbol
	*/
    public JScrollPane getScrollTree() {
        if (scrollTree == null) {
            scrollTree = new JScrollPane();
            scrollTree.setViewportView(getJtree());
            scrollTree.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return scrollTree;
    }

    /**
	*  @return scroll con el area de expresi�n
	*/
    public JScrollPane getScrollExpresion() {
        if (scrollExpresion == null) {
            scrollExpresion = new JScrollPane();
            scrollExpresion.setViewportView(getJTextExpression());
            scrollExpresion.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return scrollExpresion;
    }

    /**
	 * @return  scroll con la tabla de variables
	 */
    public JScrollPane getScrollVariables() {
        if (scrollVariables == null) {
            scrollVariables = new JScrollPane();
        }
        scrollVariables.setViewportView(getJTableVariables().getTableFormat());
        scrollVariables.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollVariables;
    }

    /**
	 * @return  button cargar expresi�n
	 */
    public JButton getRecuperar() {
        if (recuperar == null) {
            recuperar = new JButton();
            recuperar.setText(PluginServices.getText(this, "cargar"));
            recuperar.addActionListener(listener);
        }
        return recuperar;
    }

    /**
	 * @return  button salvar expresi�n
	 */
    public JButton getSalvar() {
        if (salvar == null) {
            salvar = new JButton();
            salvar.setText(PluginServices.getText(this, "salvar"));
            salvar.addActionListener(listener);
        }
        return salvar;
    }

    /**
	 * Metodo que rellena el Jtree con las las capas cargadas en la vista y sus
	 * bandas correspondientes. Ademas con las funciones, operadores y constantes 
	 * estandar.
	 */
    void InicizlizaJTree(JTree jTree) {
        int i;
        double dCellsize;
        String sName;
        jTree.setModel(null);
        FLayers layers = m_MapContext.getLayers();
        DefaultMutableTreeNode main = new DefaultMutableTreeNode(PluginServices.getText(this, "elementos_jtree"));
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(PluginServices.getText(this, "capas_jtree"));
        DefaultMutableTreeNode child;
        DefaultMutableTreeNode bandas;
        for (i = 0; i < layers.getLayersCount(); i++) {
            if (layers.getLayer(i) instanceof FLyrRasterSE) {
                child = new DefaultMutableTreeNode(layers.getLayer(i).getName());
                for (int j = 0; j < ((FLyrRasterSE) layers.getLayer(i)).getBandCount(); j++) {
                    String s = nombreBandas + (j + 1);
                    bandas = new DefaultMutableTreeNode(s);
                    child.add(bandas);
                }
                node.add(child);
            }
        }
        main.add(node);
        String sFunctions[] = { "sin", "cos", "tan", "asin", "acos", "atan", "atan2", "sinh", "cosh", "tanh", "asinh", "acosh", "atanh", "ln", "log", "exp", "abs", "rand", "mod", "sqrt", "if" };
        node = new DefaultMutableTreeNode(PluginServices.getText(this, "funciones_jtree"));
        for (i = 0; i < sFunctions.length; i++) {
            child = new DefaultMutableTreeNode(" " + sFunctions[i] + "() ");
            node.add(child);
        }
        main.add(node);
        String sOperators[] = { "+", "-", "*", "/", "%", "!", "^", "&&", "||", "<", ">", "<=", ">=", "==", "!=" };
        node = new DefaultMutableTreeNode(PluginServices.getText(this, "operadores_jtree"));
        for (i = 0; i < sOperators.length; i++) {
            child = new DefaultMutableTreeNode(" " + sOperators[i] + " ");
            node.add(child);
        }
        main.add(node);
        node = new DefaultMutableTreeNode(PluginServices.getText(this, "constantes_jtree"));
        m_Constants = new HashMap();
        m_Constants.put("e", " " + Double.toString(Math.E) + " ");
        m_Constants.put("Pi", " " + Double.toString(Math.PI) + " ");
        for (i = 0; i < layers.getLayersCount(); i++) {
            if (layers.getLayer(i) instanceof FLyrRasterSE) {
                sName = "Tama�o celda [" + layers.getLayer(i).getName() + "]";
                dCellsize = ((FLyrRasterSE) layers.getLayer(i)).getCellSize();
                m_Constants.put(sName, " " + Double.toString(dCellsize) + " ");
            }
        }
        Set set = m_Constants.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            child = new DefaultMutableTreeNode((String) iter.next());
            node.add(child);
        }
        main.add(node);
        jTree.setModel(new DefaultTreeModel(main));
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                int iRow = jtree.getRowForLocation(e.getX(), e.getY());
                TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
                if (iRow != -1 && e.getClickCount() == 2) {
                    insertTextFromTree(path);
                }
            }
        };
        jTree.addMouseListener(ml);
    }

    /**
	 * Agrega el texto del �rbol sobre el que se ha hecho doble click en el cuadro de la expresion
	 */
    private void insertTextFromTree(TreePath path) {
        String var;
        boolean yaAsignada = false;
        TreePath parent = path.getParentPath();
        if (parent != null && !parent.toString().equals("[" + PluginServices.getText(this, "elementos_jtree") + "]") && !parent.toString().equals("[" + PluginServices.getText(this, "elementos_jtree") + ", " + PluginServices.getText(this, "capas_jtree") + "]")) {
            String sParentName = parent.toString();
            String s = null;
            if (parent.getParentPath().toString().equals("[" + PluginServices.getText(this, "elementos_jtree") + ", " + PluginServices.getText(this, "capas_jtree") + "]")) {
                s = path.getParentPath().getLastPathComponent().toString() + "[" + path.getLastPathComponent().toString() + "]";
                if (jTableVariables.isEnEspera()) {
                    int row = jTableVariables.getFilaSeleccionada();
                    if (checkVisible && !getJCheckExtent().isSelected()) {
                        String layerBand, layerName;
                        FLyrRasterSE rasterLayer1, rasterLayer2;
                        int filaAsignada = 0;
                        for (int i = 0; i < getJTableVariables().getTableFormat().getRowCount(); i++) {
                            layerBand = getJTableVariables().getTableFormat().getValueAt(i, 1).toString();
                            if (!layerBand.equals("")) {
                                filaAsignada = i;
                                break;
                            }
                            if (i == getJTableVariables().getTableFormat().getRowCount() - 1) {
                                jTableVariables.actualizarVariable(row, s);
                                jTableVariables.setEnEspera(false);
                                yaAsignada = true;
                                s = "";
                            }
                        }
                        if (!yaAsignada) {
                            layerBand = getJTableVariables().getTableFormat().getValueAt(filaAsignada, 1).toString();
                            layerName = layerBand.substring(0, layerBand.indexOf("["));
                            rasterLayer1 = (FLyrRasterSE) m_MapContext.getLayers().getLayer(layerName);
                            layerName = s.substring(0, s.indexOf("["));
                            rasterLayer2 = (FLyrRasterSE) m_MapContext.getLayers().getLayer(layerName);
                            if ((rasterLayer1.getFullRasterExtent().height() == rasterLayer2.getFullRasterExtent().height()) && (rasterLayer1.getFullRasterExtent().width() == rasterLayer2.getFullRasterExtent().width())) {
                                jTableVariables.actualizarVariable(row, s);
                                jTableVariables.setEnEspera(false);
                                s = "";
                            } else s = "";
                        }
                    } else {
                        jTableVariables.actualizarVariable(row, s);
                        jTableVariables.setEnEspera(false);
                        s = "";
                    }
                } else {
                    var = consultarVariable(s);
                    jTextExpresion.insert(var, jTextExpresion.getCaretPosition());
                    s = "";
                }
            } else s = path.getLastPathComponent().toString();
            if (sParentName.equals("[" + PluginServices.getText(this, "elementos_jtree") + ", " + PluginServices.getText(this, "constantes_jtree") + "]")) {
                if (m_Constants.containsKey(s)) {
                    s = (String) m_Constants.get(s);
                } else {
                    s = "";
                }
            }
            jTextExpresion.insert(s, jTextExpresion.getCaretPosition());
            if (sParentName.equals("[" + PluginServices.getText(this, "elementos_jtree") + ", " + PluginServices.getText(this, "funciones_jtree") + "]")) {
                jTextExpresion.setCaretPosition(jTextExpresion.getCaretPosition() - 2);
            }
        }
    }

    /**
	  * Agregar la variable y el valor a la tabla, haciendo la comprobacion del extent
	  * @param valor
	  * @return la variable asociada al valor-->capa[banda]
	  */
    private String insertVariableToJTable(String valor) {
        String layerBand, layerName;
        FLyrRasterSE rasterLayer1, rasterLayer2;
        String s;
        int filaAsignada = 0;
        boolean yaAsignada = false;
        if (checkVisible && !getJCheckExtent().isSelected()) {
            if (getJTableVariables().getTableFormat().getRowCount() > 0) {
                for (int i = 0; i < getJTableVariables().getTableFormat().getRowCount(); i++) {
                    layerBand = getJTableVariables().getTableFormat().getValueAt(i, 1).toString();
                    if (!layerBand.equals("")) {
                        filaAsignada = i;
                        break;
                    }
                    if (i == getJTableVariables().getTableFormat().getRowCount() - 1) {
                        s = jTableVariables.InsertRow(null, valor);
                        getQWindowsHash().put(s, null);
                        return s;
                    }
                }
                if (yaAsignada == false) {
                    layerBand = getJTableVariables().getTableFormat().getValueAt(filaAsignada, 1).toString();
                    layerName = layerBand.substring(0, layerBand.indexOf("["));
                    rasterLayer1 = (FLyrRasterSE) m_MapContext.getLayers().getLayer(layerName);
                    layerName = valor.substring(0, valor.indexOf("["));
                    rasterLayer2 = (FLyrRasterSE) m_MapContext.getLayers().getLayer(layerName);
                    if ((rasterLayer1.getFullRasterExtent().height() == rasterLayer2.getFullRasterExtent().height()) && (rasterLayer1.getFullRasterExtent().width() == rasterLayer2.getFullRasterExtent().width())) {
                        s = jTableVariables.InsertRow(null, valor);
                        getQWindowsHash().put(s, null);
                        return s;
                    } else return "";
                }
            } else {
                s = jTableVariables.InsertRow(null, valor);
                getQWindowsHash().put(s, null);
                return s;
            }
        } else {
            s = jTableVariables.InsertRow(null, valor);
            getQWindowsHash().put(s, null);
            return s;
        }
        return "";
    }

    /**
	 * Se utiliza para conocer si un determinado valor-->(capa[banda])
	 * tiene una variable asociada, si no es as� se introduce y 
	 * se le asigna una nueva variable
	 *  
	 * @param valor
	 * @return el nombre de la variable asignada a ese valor
	 */
    private String consultarVariable(String valor) {
        String s = jTableVariables.getVariableOf(valor);
        if (s.equals("")) {
            String aux;
            aux = insertVariableToJTable(valor);
            return aux;
        }
        return s;
    }

    public void focusLost(FocusEvent e) {
        if (e.getSource() == jTextExpresion) {
            jTableVariables.setEnEspera(false);
            System.out.println("Evento del textarea:   " + jTableVariables.isEnEspera());
        }
    }

    public void focusGained(FocusEvent e) {
    }

    /**
	 * inicializa el parser y lo devuelve
	 * @return parser
	 */
    public JEP initializeParser() {
        parser = null;
        parser = getParser();
        return parser;
    }

    /**
	 * @return parser 
	 */
    public JEP getParser() {
        if (parser == null) {
            parser = new JEP();
            parser.setAllowUndeclared(true);
            parser.addStandardFunctions();
        }
        return parser;
    }

    /**
	 * @return tabla hash con las variables definidas
	 */
    public HashMap getQWindowsHash() {
        if (qWindowsHash == null) qWindowsHash = new HashMap();
        return qWindowsHash;
    }

    /**
	 * @return vista actual de la aplicacion
	 */
    public View getView() {
        return view;
    }

    public void setPersistentVarTable(HashMap persistentVarTable) {
        this.persistentVarTable = persistentVarTable;
        loadVarTable();
    }

    /**
	 * Carga la tabla de variables a partir del HasMap de persistencia.
	 *
	 */
    private void loadVarTable() {
        TableFormat varTable = getJTableVariables();
        if (varTable.getCont() > 0) varTable.getTableFormat().removeRowSelectionInterval(0, varTable.getCont() - 1);
        for (Iterator iter = persistentVarTable.keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            varTable.InsertRow(key, (String) persistentVarTable.get(key));
            getQWindowsHash().put(key, persistentVarTable.get(key));
        }
    }

    /**
	  * Actualiza el HasMap de persistencia con los elementos que tiene la tabla de 
	  * variables.
	  * 
	  */
    public void updatePersistentVarTable() {
        for (int i = 0; i < getJTableVariables().getTableFormat().getRowCount(); i++) {
            String var = getJTableVariables().getTableFormat().getValueAt(i, 0).toString();
            String value = getJTableVariables().getTableFormat().getValueAt(i, 1).toString();
            getPersistentVarTable().put(var, value);
        }
    }

    public HashMap getPersistentVarTable() {
        return persistentVarTable;
    }
}
