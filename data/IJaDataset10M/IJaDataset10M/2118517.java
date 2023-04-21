package org.dyndns.atessier.glgen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

/** 
 *  Copyright 1999-2002 Matthew Robinson and Pavel Vorobiev. 
 *  All Rights Reserved. 
 * 
 *  =================================================== 
 *  This program contains code from the book "Swing" 
 *  2nd Edition by Matthew Robinson and Pavel Vorobiev 
 *  http://www.spindoczine.com/sbe 
 *  =================================================== 
 * 
 *  The above paragraph must be included in full, unmodified 
 *  and completely intact in the beginning of any source code 
 *  file that references, copies or uses (in any way, shape 
 *  or form) code contained in this file. 
 */
public class GLGEN {

    private static boolean _eng;

    private static boolean _ms_os = false;

    private static String _javaVersion;

    private static double _taxRate;

    private static String[] _stores;

    private static final String REGISTRY_SUBKEY = "org.dyndns.atessier.glgen";

    private Preferences _prefs = null;

    private JFrame _jFrame = null;

    private static String[] _cat;

    private JTree _jTree = null;

    private DefaultTreeModel _defaultTreeModel = null;

    private DefaultMutableTreeNode _defaultMutableTreeNode = null;

    private Document _document;

    private DefaultTreeCellRenderer _defaultTreeCellRenderer = null;

    private JScrollPane _jScrollPaneLeft = null;

    private JScrollPane _jScrollPaneRight = null;

    private JTable _jTable = null;

    private AttributTableModel _attributTableModel = null;

    private JSplitPane _jSplitPane = null;

    private TreeSelectionListener _treeSelectionListener = null;

    private DefaultTreeCellEditor _defaultTreeCellEditor = null;

    private static Node _editingNode = null;

    private static boolean _xmlChanged = false;

    private static double _total = 0.00;

    private JLabel _jLabel1 = null;

    private static JLabel _jLabel2 = null;

    private JPanel _jPanelSouth = null;

    private static ArrayList _arrayList = new ArrayList();

    private ArrayList _arrayListXML;

    private JToolBar _jToolBar = null;

    private JButton _jButtonGenerate = null;

    private JButton _jButtonAbout = null;

    private JButton _jButtonAddItem = null;

    private JButton _jButtonRemoveItem = null;

    private JButton _jButtonRemoveItemFromTable = null;

    private JButton _jButtonEditItem = null;

    private String _fs = null;

    public GLGEN() {
        super();
        initialize();
    }

    public static void main(String[] args) {
        GLGEN glgen = new GLGEN();
    }

    private void initialize() {
        Splash splash = new Splash("images" + get_fs() + "splash.jpg", this.get_jFrame(), 5000);
        this.queryOS();
        this.queryJavaVersion();
        this.queryLanguage();
        this.queryTaxRate();
        this.queryStores();
        this.get_jFrame();
        PlasticLookAndFeel.setMyCurrentTheme(new ExperienceBlue());
        this.setLaf(new com.jgoodies.looks.plastic.PlasticLookAndFeel());
        this.get_jFrame().getContentPane().setLayout(new BorderLayout());
        this.get_jFrame().getContentPane().add(get_jToolBar(), BorderLayout.NORTH);
        this.get_jFrame().getContentPane().add(get_jSplitPane(), BorderLayout.CENTER);
        this.get_jFrame().getContentPane().add(get_jPanelSouth(), BorderLayout.SOUTH);
        this.get_jFrame().setExtendedState(Frame.MAXIMIZED_BOTH);
        this.get_jFrame().setVisible(true);
        openGLGENfile();
    }

    private void addItemToTable(Item item) {
        get_attributTableModel().insert(item);
        get_jTable().tableChanged(new TableModelEvent(get_attributTableModel()));
    }

    private void addNewAttribute(Node node, String name, String value) {
        try {
            ((Element) node).setAttribute(name, value);
        } catch (Exception e) {
            showError(e, GLGEN.is_eng() ? "Error adding attribute" : "Erreur lors de l'ajout de l'attribut");
        }
    }

    private void addNewNode(Item item) {
        if (get_document() == null) return;
        XmlViewerNode xmlViewerNode = getSelectedTreeNode();
        if (xmlViewerNode == null) return;
        Node node = xmlViewerNode.getXmlNode();
        if (node == null) return;
        try {
            TreePath treePath = get_jTree().getSelectionPath();
            Element element = get_document().createElement(item.get_item().replaceAll(" ", "_"));
            XmlViewerNode nodeElement = new XmlViewerNode(element);
            xmlViewerNode.addXmlNode(nodeElement);
            get_defaultTreeModel().nodeStructureChanged(xmlViewerNode);
            if (treePath != null) {
                treePath = treePath.pathByAddingChild(nodeElement);
                get_jTree().setSelectionPath(treePath);
                get_jTree().scrollPathToVisible(treePath);
            }
        } catch (Exception e) {
            showError(e, GLGEN.is_eng() ? "Error addind new node" : "Erreur lors de l'ajout du nouveau noeud");
        }
    }

    public static void calculateTotal() {
        double total = 0;
        for (Iterator iter = get_arrayList().iterator(); iter.hasNext(); ) {
            Item item = (Item) iter.next();
            total = total + item.computeTotal();
        }
        set_total(total);
        displayTotal();
    }

    /**
	 * @param node
	 * @return
	 */
    private boolean canDisplayNode(Node node) {
        switch(node.getNodeType()) {
            case Node.ELEMENT_NODE:
                return true;
            case Node.TEXT_NODE:
                String string = node.getNodeValue().trim();
                return !(string.equals("") || string.equals("\n") || string.equals("\r\n"));
        }
        return false;
    }

    private void createGLGENXMLFile() {
        AddItemDialog addItemDialog = AddItemDialog.getInstance();
        if (addItemDialog.is_FinishButtonPressed()) {
            _arrayListXML = addItemDialog.get_arrayList();
            Collections.sort(_arrayListXML);
            GLgenXMLWriteParser glgenXMLWriteParser = new GLgenXMLWriteParser(_arrayListXML);
            glgenXMLWriteParser.writeFile();
            openGLGENfile();
        }
    }

    private DefaultMutableTreeNode createTreeNode(Node root) {
        if (!canDisplayNode(root)) {
            return null;
        }
        XmlViewerNode xmlViewerNode = new XmlViewerNode(root);
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            DefaultMutableTreeNode child = createTreeNode(node);
            if (child != null) {
                xmlViewerNode.add(child);
            }
        }
        return xmlViewerNode;
    }

    private void deleteAttribute() {
        int row = get_jTable().getSelectedRow();
        if (row < 0) return;
        String price = get_attributTableModel().getValueAt(row, AttributTableModel.PRICE_COLUMN).toString();
        String qty = get_attributTableModel().getValueAt(row, AttributTableModel.QUANTITY_COLUMN).toString();
        String taxable = get_attributTableModel().getValueAt(row, AttributTableModel.TAXABLE_COLUMN).toString();
        calculateTotal();
        get_arrayList().remove(row);
        get_attributTableModel().delete(row);
        get_jTable().tableChanged(new TableModelEvent(get_attributTableModel()));
    }

    private void deleteNode() {
        TreePath treePath = get_jTree().getSelectionPath();
        XmlViewerNode xmlViewerNode = getSelectedTreeNode();
        if (xmlViewerNode == null) {
            return;
        }
        Node node = xmlViewerNode.getXmlNode();
        if (node == null) {
            return;
        }
        int result = JOptionPane.showConfirmDialog(null, (GLGEN.is_eng() ? "Are you sure you want to delete this item ?" : "�tes vous sur de vouloir supprimer cet item ?"), "GLGEN", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            TreeNode treeNode = xmlViewerNode.getParent();
            xmlViewerNode.remove();
            get_defaultTreeModel().nodeStructureChanged(treeNode);
        } catch (Exception e) {
            showError(e, (GLGEN.is_eng() ? "Error deleting item" : "Erreur supression de l'item"));
        }
    }

    static void displayTotal() {
        get_jLabel2().setText(Double2StringWith2decimals.convert(Double.valueOf(get_total())) + "$");
    }

    private void enableButton() {
        if (getSelectedNode() != null) {
            get_jButtonRemoveItem().setEnabled(getSelectedNode().hasAttributes());
            get_jButtonEditItem().setEnabled(getSelectedNode().hasAttributes());
            get_jButtonAddItem().setEnabled(!getSelectedNode().hasAttributes());
            if (getSelectedNode().getNodeName().equalsIgnoreCase("GLGEN")) {
                get_jButtonAddItem().setEnabled(false);
            }
        } else {
            get_jButtonRemoveItem().setEnabled(false);
            get_jButtonEditItem().setEnabled(false);
            get_jButtonAddItem().setEnabled(false);
        }
    }

    /**
	 * @param tree
	 */
    public static void expandTree(JTree tree) {
        TreeNode treeNode = (TreeNode) tree.getModel().getRoot();
        TreePath treePath = new TreePath(treeNode);
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            TreeNode child = (TreeNode) treeNode.getChildAt(i);
            expandTree(tree, treePath, child);
        }
    }

    /**
	 * @param tree
	 * @param path
	 * @param node
	 */
    public static void expandTree(JTree tree, TreePath path, TreeNode node) {
        if (path == null || node == null) {
            return;
        }
        tree.expandPath(path);
        TreePath newPath = path.pathByAddingChild(node);
        for (int i = 0; i < node.getChildCount(); i++) {
            TreeNode child = (TreeNode) node.getChildAt(i);
            if (child != null) {
                expandTree(tree, newPath, child);
            }
        }
    }

    private void generateList() {
        Collections.sort(get_arrayList());
        CreateHtmlList list = new CreateHtmlList(get_arrayList(), "glgenList.html");
        list.writeFile();
        if (is_ms_os()) {
            try {
                Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler glgenlist.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(get_jFrame(), GLGEN.is_eng() ? "The list is called glgenlist.html and is present in the application directory. Just open it in your favorite browser and print it." : "La liste se nomme glgenlist.html et est dans le r�pertoire de cette application. Vous n'avez qu'� l'ouvrir dans votre navigateur pr�f�r� puis l'imprimer");
        }
    }

    /**
	 * @return Returns the _arrayList.
	 */
    public static ArrayList get_arrayList() {
        return _arrayList;
    }

    /**
	 * @return Returns the _arrayListXML.
	 */
    public ArrayList get_arrayListXML() {
        return _arrayListXML;
    }

    /**
	 * @return Returns the attributTableModel.
	 */
    public AttributTableModel get_attributTableModel() {
        if (_attributTableModel == null) {
            _attributTableModel = new AttributTableModel();
        }
        return _attributTableModel;
    }

    /**
	 * @return Returns the _cat.
	 */
    public static String[] get_cat() {
        if (_cat == null) {
            _cat = new String[7];
            if (GLGEN.is_eng()) {
                _cat[0] = "Bakery";
                _cat[1] = "Frozen";
                _cat[2] = "Fruits_Vegetables";
                _cat[3] = "Grocery";
                _cat[4] = "Health_Beauty";
                _cat[5] = "Meat_Poultry_Fish";
                _cat[6] = "Refrigerated";
            } else {
                _cat[0] = "Boulangerie";
                _cat[1] = "Congel�";
                _cat[2] = "�picerie";
                _cat[3] = "Fruits_L�gumes";
                _cat[4] = "R�frig�r�";
                _cat[5] = "Sant�_Beaut�";
                _cat[6] = "Viande_Volaille_Poisson";
            }
        }
        return _cat;
    }

    /**
	 * @return Returns the defaultTreeCellEditor.
	 */
    public DefaultTreeCellEditor get_defaultTreeCellEditor() {
        if (_defaultTreeCellEditor == null) {
            _defaultTreeCellEditor = new DefaultTreeCellEditor(get_jTree(), get_defaultTreeCellRenderer()) {

                public boolean isCellEditable(EventObject event) {
                    Node node = getSelectedNode();
                    if (node != null && node.getNodeType() == Node.TEXT_NODE) {
                        return super.isCellEditable(event);
                    } else {
                        return false;
                    }
                }

                public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
                    if (value instanceof XmlViewerNode) {
                        set_editingNode(((XmlViewerNode) value).getXmlNode());
                    }
                    return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
                }
            };
            _defaultTreeCellEditor.addCellEditorListener(new XmlEditorListener(this));
        }
        return _defaultTreeCellEditor;
    }

    /**
	 * @return Returns the defaultTreeCellRenderer.
	 */
    public DefaultTreeCellRenderer get_defaultTreeCellRenderer() {
        if (_defaultTreeCellRenderer == null) {
            _defaultTreeCellRenderer = new DefaultTreeCellRenderer() {

                public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                    Component result = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                    _defaultTreeCellRenderer.setLeafIcon(new ImageIcon("images//dot.gif"));
                    if (value instanceof XmlViewerNode) {
                        Node node = ((XmlViewerNode) value).getXmlNode();
                        if (node instanceof Element) {
                            setIcon(expanded ? openIcon : closedIcon);
                            if (node.hasAttributes()) setIcon(leafIcon);
                        } else {
                            setIcon(leafIcon);
                        }
                    }
                    return result;
                }
            };
        }
        return _defaultTreeCellRenderer;
    }

    /**
	 * @return Returns the defaultMutableTreeNode.
	 */
    public DefaultMutableTreeNode get_defaultMutableTreeNode() {
        if (_defaultMutableTreeNode == null) {
            _defaultMutableTreeNode = new DefaultMutableTreeNode(GLGEN.is_eng() ? "No XML file loaded" : "Aucun fichier XML charg�");
        }
        return _defaultMutableTreeNode;
    }

    /**
	 * @return Returns the defaulTreeModel.
	 */
    public DefaultTreeModel get_defaultTreeModel() {
        if (_defaultTreeModel == null) {
            _defaultTreeModel = new DefaultTreeModel(get_defaultMutableTreeNode());
        }
        return _defaultTreeModel;
    }

    /**
	 * @return Returns the _document.
	 */
    public Document get_document() {
        return _document;
    }

    /**
	 * @return Returns the _editingNode.
	 */
    public static Node get_editingNode() {
        return _editingNode;
    }

    public String get_fs() {
        if (_fs == null) {
            _fs = System.getProperty("file.separator");
        }
        return _fs;
    }

    /**
	 * @return Returns the _javaVersion.
	 */
    public static String get_javaVersion() {
        return _javaVersion;
    }

    /**
	 * @return Returns the _jButtonAbout.
	 */
    public JButton get_jButtonAbout() {
        if (_jButtonAbout == null) {
            _jButtonAbout = new JButton();
            _jButtonAbout.setIcon(new ImageIcon("images" + get_fs() + "wi0063-16.gif"));
            _jButtonAbout.setToolTipText(GLGEN.is_eng() ? "About" : "A propos");
            _jButtonAbout.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    AboutDialog ad = AboutDialog.getInstance();
                }
            });
        }
        return _jButtonAbout;
    }

    /**
	 * @return Returns the _jButtonAddItem.
	 */
    public JButton get_jButtonAddItem() {
        if (_jButtonAddItem == null) {
            _jButtonAddItem = new JButton();
            _jButtonAddItem.setIcon(new ImageIcon("images" + get_fs() + "new_document16.gif"));
            _jButtonAddItem.setToolTipText(GLGEN.is_eng() ? "Add Item" : "Ajouter Item");
            _jButtonAddItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    AddOneItemDialog dialog = AddOneItemDialog.getInstance();
                    if (dialog.is_OkButtonPressed()) {
                        addNewNode(dialog.get_item());
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "p", Double2StringWith2decimals.convert(dialog.get_item().get_price().toString()));
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "q", dialog.get_item().get_quantity().toString());
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "s", dialog.get_item().get_store());
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "t", dialog.get_item().get_taxable().toString());
                        get_defaultTreeModel().nodeStructureChanged(getSelectedTreeNode());
                        set_xmlChanged(true);
                    }
                }
            });
        }
        return _jButtonAddItem;
    }

    /**
	 * @return Returns the _jButtonEditItem.
	 */
    public JButton get_jButtonEditItem() {
        if (_jButtonEditItem == null) {
            _jButtonEditItem = new JButton();
            _jButtonEditItem.setIcon(new ImageIcon("images" + get_fs() + "wi0062-16.gif"));
            _jButtonEditItem.setToolTipText(GLGEN.is_eng() ? "Edit Item" : "Modifier Item");
            _jButtonEditItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Node node = getSelectedNode();
                    Item item = new Item(node.getNodeName(), node.getAttributes().getNamedItem("s").toString().substring(3, node.getAttributes().getNamedItem("s").toString().length() - 1), node.getParentNode().getNodeName(), Integer.valueOf(node.getAttributes().getNamedItem("q").toString().substring(3, node.getAttributes().getNamedItem("q").toString().length() - 1)), Double.valueOf(node.getAttributes().getNamedItem("p").toString().substring(3, node.getAttributes().getNamedItem("p").toString().length() - 1)), Boolean.valueOf(node.getAttributes().getNamedItem("t").toString().substring(3, node.getAttributes().getNamedItem("t").toString().length() - 1)));
                    AddOneItemDialog dialog = AddOneItemDialog.getInstance(item);
                    if (dialog.is_OkButtonPressed()) {
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "p", Double2StringWith2decimals.convert(dialog.get_item().get_price().toString()));
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "q", dialog.get_item().get_quantity().toString());
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "s", dialog.get_item().get_store());
                        addNewAttribute(getSelectedTreeNode().getXmlNode(), "t", dialog.get_item().get_taxable().toString());
                        get_defaultTreeModel().nodeStructureChanged(getSelectedTreeNode());
                        set_xmlChanged(true);
                    }
                }
            });
        }
        return _jButtonEditItem;
    }

    /**
	 * @return Returns the _jButtonGenerate.
	 */
    public JButton get_jButtonGenerate() {
        if (_jButtonGenerate == null) {
            _jButtonGenerate = new JButton();
            _jButtonGenerate = new JButton();
            _jButtonGenerate.setIcon(new ImageIcon("images" + get_fs() + "NP.gif"));
            _jButtonGenerate.setToolTipText(GLGEN.is_eng() ? "Generate grocery list" : "G�n�r� liste d'�picerie");
            _jButtonGenerate.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    generateList();
                }
            });
        }
        return _jButtonGenerate;
    }

    /**
	 * @return Returns the _jButtonRemoveItem. FromTable
	 */
    public JButton get_jButtonRemoveItem() {
        if (_jButtonRemoveItem == null) {
            _jButtonRemoveItem = new JButton();
            _jButtonRemoveItem.setIcon(new ImageIcon("images" + get_fs() + "delete_x16.gif"));
            _jButtonRemoveItem.setToolTipText(GLGEN.is_eng() ? "Delete Item" : "Supprimer Item");
            _jButtonRemoveItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    deleteNode();
                    set_xmlChanged(true);
                }
            });
        }
        return _jButtonRemoveItem;
    }

    /**
	 * @return Returns the _jButtonRemoveItemFromTable. 
	 */
    public JButton get_jButtonRemoveItemFromTable() {
        if (_jButtonRemoveItemFromTable == null) {
            _jButtonRemoveItemFromTable = new JButton();
            _jButtonRemoveItemFromTable.setIcon(new ImageIcon("images" + get_fs() + "delete_x16.gif"));
            _jButtonRemoveItemFromTable.setToolTipText(GLGEN.is_eng() ? "Delete Item from right table" : "Supprimer Item de la liste de droite");
            _jButtonRemoveItemFromTable.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    deleteAttribute();
                    calculateTotal();
                }
            });
        }
        return _jButtonRemoveItemFromTable;
    }

    /**
	 * @return Returns the _jframe.
	 */
    public JFrame get_jFrame() {
        if (_jFrame == null) {
            _jFrame = new JFrame();
            _jFrame.setBounds(0, 0, 800, 800);
            _jFrame.setTitle("GLgen v1.0 " + (GLGEN.is_eng() ? "Grocery List GENerator" : "G�n�rateur de Liste d'�picerie"));
            _jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("images" + get_fs() + "icon.gif"));
            _jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            _jFrame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    if (is_xmlChanged()) {
                        sortNsaveDocument();
                    }
                    _jFrame.dispose();
                    System.exit(0);
                }
            });
        }
        return _jFrame;
    }

    public JLabel get_jLabel1() {
        if (_jLabel1 == null) {
            _jLabel1 = new JLabel();
            _jLabel1.setText("Total = ");
        }
        return _jLabel1;
    }

    public static JLabel get_jLabel2() {
        if (_jLabel2 == null) {
            _jLabel2 = new JLabel();
        }
        return _jLabel2;
    }

    public JPanel get_jPanelSouth() {
        if (_jPanelSouth == null) {
            _jPanelSouth = new JPanel();
            _jPanelSouth.setLayout(new FlowLayout());
            _jPanelSouth.add(get_jLabel1());
            _jPanelSouth.add(get_jLabel2());
        }
        return _jPanelSouth;
    }

    /**
	 * @return Returns the jScrollPaneLeft.
	 */
    public JScrollPane get_jScrollPaneLeft() {
        if (_jScrollPaneLeft == null) {
            _jScrollPaneLeft = new JScrollPane();
            _jScrollPaneLeft.getViewport().add(get_jTree());
        }
        return _jScrollPaneLeft;
    }

    /**
	 * @return Returns the jScrollPaneRight.
	 */
    public JScrollPane get_jScrollPaneRight() {
        if (_jScrollPaneRight == null) {
            _jScrollPaneRight = new JScrollPane();
            _jScrollPaneRight.getViewport().add(get_jTable());
            _jScrollPaneRight.getViewport().setBackground(get_jTable().getBackground());
        }
        return _jScrollPaneRight;
    }

    /**
	 * @return Returns the jSplitPane.
	 */
    public JSplitPane get_jSplitPane() {
        if (_jSplitPane == null) {
            _jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, get_jScrollPaneLeft(), get_jScrollPaneRight());
            _jSplitPane.setDividerLocation(315);
            _jSplitPane.setDividerSize(5);
        }
        return _jSplitPane;
    }

    /**
	 * @return Returns the jTable.
	 */
    public JTable get_jTable() {
        if (_jTable == null) {
            _jTable = new JTable();
            _jTable.setModel(get_attributTableModel());
        }
        return _jTable;
    }

    /**
	 * @return Returns the _jToolBar.
	 */
    public JToolBar get_jToolBar() {
        if (_jToolBar == null) {
            _jToolBar = new JToolBar();
            _jToolBar.setFloatable(false);
            _jToolBar.add(new JLabel(GLGEN.is_eng() ? " Left panel " : " Panneau gauche "));
            _jToolBar.add(get_jButtonAddItem());
            _jToolBar.add(get_jButtonEditItem());
            _jToolBar.add(get_jButtonRemoveItem());
            _jToolBar.addSeparator();
            _jToolBar.addSeparator();
            _jToolBar.addSeparator();
            _jToolBar.add(new JLabel(GLGEN.is_eng() ? " Right panel " : "Panneau droit "));
            _jToolBar.add(get_jButtonRemoveItemFromTable());
            _jToolBar.add(get_jButtonGenerate());
            _jToolBar.addSeparator();
            _jToolBar.addSeparator();
            _jToolBar.add(get_jButtonAbout());
        }
        return _jToolBar;
    }

    /**
	 * @return Returns the jTree.
	 */
    public JTree get_jTree() {
        if (_jTree == null) {
            _jTree = new JTree();
            _jTree.setModel(get_defaultTreeModel());
            _jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            _jTree.setShowsRootHandles(true);
            _jTree.setEditable(false);
            _jTree.setCellRenderer(get_defaultTreeCellRenderer());
            _jTree.addTreeSelectionListener(get_treeSelectionListener());
            _jTree.setCellEditor(get_defaultTreeCellEditor());
            _jTree.setEditable(true);
            _jTree.setInvokesStopCellEditing(true);
        }
        return _jTree;
    }

    /**
	 * @return Returns the prefs.
	 */
    public Preferences get_prefs() {
        if (_prefs == null) {
            _prefs = Preferences.systemRoot();
        }
        return _prefs;
    }

    /**
	 * @return Returns the _stores.
	 */
    public static String[] get_stores() {
        return _stores;
    }

    /**
	 * @return Returns the _taxRate.
	 */
    public static double get_taxRate() {
        return _taxRate;
    }

    public static double get_total() {
        return _total;
    }

    public TreeSelectionListener get_treeSelectionListener() {
        if (_treeSelectionListener == null) {
            _treeSelectionListener = new TreeSelectionListener() {

                public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                    Node node = getSelectedNode();
                    enableButton();
                    if (node.hasAttributes()) {
                        Item item = new Item(node.getNodeName(), node.getAttributes().getNamedItem("s").toString().substring(3, node.getAttributes().getNamedItem("s").toString().length() - 1), node.getParentNode().getNodeName(), Integer.valueOf(node.getAttributes().getNamedItem("q").toString().substring(3, node.getAttributes().getNamedItem("q").toString().length() - 1)), Double.valueOf(Double2StringWith2decimals.convert(node.getAttributes().getNamedItem("p").toString().substring(3, node.getAttributes().getNamedItem("p").toString().length() - 1))), Boolean.valueOf(node.getAttributes().getNamedItem("t").toString().substring(3, node.getAttributes().getNamedItem("t").toString().length() - 1)));
                        get_arrayList().add(item);
                        addItemToTable(item);
                        calculateTotal();
                    }
                }
            };
        }
        return _treeSelectionListener;
    }

    public Node getSelectedNode() {
        XmlViewerNode xmlViewerNode = getSelectedTreeNode();
        if (xmlViewerNode == null) {
            return null;
        }
        return xmlViewerNode.getXmlNode();
    }

    public XmlViewerNode getSelectedTreeNode() {
        TreePath treePath = get_jTree().getSelectionPath();
        if (treePath == null) {
            return null;
        }
        Object object = treePath.getLastPathComponent();
        if (!(object instanceof XmlViewerNode)) {
            return null;
        }
        return (XmlViewerNode) object;
    }

    /**
	 * @return Returns the _eng.
	 */
    public static boolean is_eng() {
        return _eng;
    }

    /**
	 * @return Returns the _ms_os.
	 */
    public static boolean is_ms_os() {
        return _ms_os;
    }

    /**
	 * @return Returns the _xmlChanged.
	 */
    public static boolean is_xmlChanged() {
        return _xmlChanged;
    }

    private void openGLGENfile() {
        Thread thread = new Thread() {

            public void run() {
                File file = new File("glgen.xml");
                if (file == null || !file.isFile()) {
                    createGLGENXMLFile();
                    return;
                }
                try {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    set_document(documentBuilder.parse(file));
                    Element root = _document.getDocumentElement();
                    root.normalize();
                    DefaultMutableTreeNode defaultMutableTreeNode = createTreeNode(root);
                    get_defaultTreeModel().setRoot(defaultMutableTreeNode);
                    get_jTree().treeDidChange();
                    expandTree(get_jTree());
                } catch (Exception ex) {
                    showError(ex, (GLGEN.is_eng() ? "Error reading glgen.xml file" : "Erreur de lecture du fichier glgen.xml"));
                } finally {
                }
            }
        };
        thread.start();
    }

    private void queryJavaVersion() {
        _javaVersion = System.getProperty("java.version");
        if (!get_javaVersion().substring(0, 3).equalsIgnoreCase("1.5")) {
            JOptionPane.showMessageDialog(get_jFrame(), GLGEN.is_eng() ? "Your java runtime environement needs to be updated before you can use GLgen. Please visit http://java.sun.com and install Java 5 version 1.5 or superior. " : "Votre environement java requiert une mise � jour pour permettre l'utilisation de GLgen. SVP visiter le site http://java.sun.com et installer java5 version 1.5 ou ult�rieure");
            System.exit(0);
        }
    }

    private void queryLanguage() {
        if (get_prefs().getBoolean(REGISTRY_SUBKEY + "_language_set", false)) {
            set_eng(get_prefs().getBoolean(REGISTRY_SUBKEY + "_is_english", true));
        } else {
            Object[] options = { "English", "Fran�ais" };
            int n = JOptionPane.showOptionDialog(get_jFrame(), "What language do you prefer ? / Quel langue pr�f�rez vous ?", "Language / Langue", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n != 0) {
                get_prefs().putBoolean(REGISTRY_SUBKEY + "_is_english", false);
                set_eng(false);
            } else {
                get_prefs().putBoolean(REGISTRY_SUBKEY + "_is_english", true);
                set_eng(true);
            }
            get_prefs().putBoolean(REGISTRY_SUBKEY + "_language_set", true);
        }
    }

    private void queryOS() {
        if (System.getProperty("os.name").substring(0, 3).equalsIgnoreCase("win")) _ms_os = true;
    }

    private void queryStores() {
        if (get_prefs().getBoolean(REGISTRY_SUBKEY + "_stores_set", false)) {
            int numberStores = 0;
            for (int i = 0; i < 10; i++) {
                if (!get_prefs().get(REGISTRY_SUBKEY + "_store" + String.valueOf(i), "").equalsIgnoreCase("")) numberStores++;
            }
            _stores = new String[numberStores];
            for (int i = 0; i < numberStores; i++) {
                _stores[i] = get_prefs().get(REGISTRY_SUBKEY + "_store" + String.valueOf(i), "");
            }
        } else {
            int j = 0;
            String store = "store";
            try {
                while (!store.equalsIgnoreCase("") && j < 10) {
                    store = (String) JOptionPane.showInputDialog(get_jFrame(), (GLGEN.is_eng() ? "Type the name of the stores you shop to. Press cancel when finished" : "Inscrire le nom des magasins que vous fr�quentez. Appuyer sur Cancel quand vous avez termin�."), (GLGEN.is_eng() ? "Shopping store" : "Magasins"), JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (!store.equalsIgnoreCase("")) {
                        get_prefs().put(REGISTRY_SUBKEY + "_store" + String.valueOf(j), store.toLowerCase().replaceAll(" ", "_").replaceAll("�", "e").replaceAll("�", "e").replaceAll("�", "e").replaceAll("�", "a").replaceAll("�", "a").replaceAll("�", "c"));
                        j++;
                    }
                }
            } catch (NullPointerException e) {
            }
            get_prefs().putBoolean(REGISTRY_SUBKEY + "_stores_set", true);
            queryStores();
        }
    }

    private void queryTaxRate() {
        if (get_prefs().getBoolean(REGISTRY_SUBKEY + "_taxrate_set", false)) {
            set_taxRate(get_prefs().getDouble(REGISTRY_SUBKEY + "_taxrate", 0));
        } else {
            String s = (String) JOptionPane.showInputDialog(get_jFrame(), (GLGEN.is_eng() ? "Type your total taxe rate for taxable items" : "Inscrire le taux de taxe total pour les �l�ments taxables"), (GLGEN.is_eng() ? "Tax rate" : "Taux de taxation"), JOptionPane.PLAIN_MESSAGE, null, null, null);
            get_prefs().putDouble(REGISTRY_SUBKEY + "_taxrate", Double.parseDouble(s));
            set_taxRate(Double.parseDouble(s));
            get_prefs().putBoolean(REGISTRY_SUBKEY + "_taxrate_set", true);
        }
    }

    /**
	 * @param _document The _document to set.
	 */
    public void set_document(Document _document) {
        this._document = _document;
    }

    /**
	 * @param node The _editingNode to set.
	 */
    public static void set_editingNode(Node node) {
        _editingNode = node;
    }

    /**
	 * @param _eng The _eng to set.
	 */
    public static void set_eng(boolean _eng) {
        GLGEN._eng = _eng;
    }

    /**
	 * @param rate The _taxRate to set.
	 */
    public static void set_taxRate(double rate) {
        _taxRate = rate;
    }

    public static void set_total(double total) {
        _total = total;
    }

    /**
	 * @param changed The _xmlChanged to set.
	 */
    public static void set_xmlChanged(boolean changed) {
        _xmlChanged = changed;
    }

    public void setLaf(LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(_jFrame);
        } catch (UnsupportedLookAndFeelException e) {
        }
    }

    /**
	 * @param prefs The prefs to set.
	 */
    public void setPrefs(Preferences prefs) {
        this._prefs = prefs;
    }

    /**
	 * @param ex
	 * @param string
	 */
    public void showError(Exception ex, String string) {
        ex.printStackTrace();
    }

    private void sortNsaveDocument() {
        ArrayList arrayList = new ArrayList();
        NodeList nodelist = get_document().getChildNodes();
        int docLength = nodelist.getLength();
        for (int i = 0; i < docLength; i++) {
            Node node = nodelist.item(i);
            if (node.hasChildNodes()) {
                NodeList childNodeList = node.getChildNodes();
                for (int j = 0; j < childNodeList.getLength(); j++) {
                    Node childNode = childNodeList.item(j);
                    if (childNode.hasChildNodes()) {
                        NodeList grandChildNodeList = childNode.getChildNodes();
                        for (int k = 0; k < grandChildNodeList.getLength(); k++) {
                            Node grandChildNode = grandChildNodeList.item(k);
                            if (grandChildNode.hasAttributes()) {
                                NamedNodeMap namedNodeMap = grandChildNode.getAttributes();
                                Node price = namedNodeMap.getNamedItem("p");
                                Node quantity = namedNodeMap.getNamedItem("q");
                                Node store = namedNodeMap.getNamedItem("s");
                                Node taxable = namedNodeMap.getNamedItem("t");
                                arrayList.add(new ItemXML(grandChildNode.getNodeName(), store.getNodeValue(), grandChildNode.getParentNode().getNodeName(), Integer.valueOf(quantity.getNodeValue()), Double.valueOf(Double2StringWith2decimals.convert(price.getNodeValue())), Boolean.valueOf(taxable.getNodeValue())));
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(arrayList);
        GLgenXMLWriteParser glgenXMLWriteParser = new GLgenXMLWriteParser(arrayList);
        glgenXMLWriteParser.writeFile();
    }
}
