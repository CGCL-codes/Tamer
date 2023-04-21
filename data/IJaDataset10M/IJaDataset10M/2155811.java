package org.mitre.bio.phylo;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.lang.reflect.Constructor;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.event.*;
import org.mitre.bio.phylo.dom.*;
import org.mitre.bio.phylo.dom.gui.*;
import org.mitre.bio.phylo.dom.gui.event.Tree2DPaneChangeEvent;
import org.mitre.bio.phylo.dom.gui.event.Tree2DPaneChangeListener;
import org.mitre.bio.phylo.dom.gui.treepainter.*;
import org.mitre.bio.phylo.dom.io.PhyloStringReader;
import org.mitre.ui.ColorFontChooserDialog;
import org.w3c.dom.*;

/**
 * Main GUI class for TreeViewJApplet
 * 
 * This provides an applet that many (but not all) of the features of the 
 * TreeViewJ application in a form that is embeddable in HTML pages.  
 * The applet can be configured as �headless� meaning that none of its own 
 * UI controls are presented but the underlying functionality is.  
 * This way the HTML/Javascript can supply look and feel consistent with the 
 * rest of the containing web application.
 * 
 * See examples/TreeViewJApplet.html for usage.
 * 
 * @author Tim J. Brown
 * @author Marc E. Colosimo
 * @version 1.1
 * 
 */
public class TreeViewJApplet extends JApplet implements TreeSelectionListener, ClipboardOwner, Tree2DPaneChangeListener {

    /**
     * List Elements
     */
    protected PhyloTreeModel treeModel;

    /** Our JTree that implements AutoScroll. */
    protected ListTree listTree;

    protected ListSelectionModel selectionModel;

    /** Map that stores the TreeViewPane for each Phylogeny. */
    protected Map<Phylogeny, Tree2DScrollPane> treeMap = new HashMap<Phylogeny, Tree2DScrollPane>();

    /**
     * GUI Elements
     */
    protected JPanel treeScrollPanel;

    protected Tree2DScrollPane treeScrollPane;

    protected XMLTree dtf;

    /**
     * Actions
     */
    protected Action slantAction, rectAction, phyloAction, circleAction;

    protected Action colorAction, timeBarAction;

    protected Action zoomInAction, zoomOutAction;

    /**
     * Menus, MenuItems and Buttons
     * 
     * Need to use JToggleButton for the different
     * types of trees, to make sure only one is selected.
     */
    protected JMenuBar menuBar;

    protected JToolBar toolBar;

    protected JMenu editMenu, viewMenu, toolsMenu;

    protected JToggleButton slantButton, phyloButton, rectButton, circleButton;

    protected JRadioButtonMenuItem slantItem, phyloItem, rectItem, circleItem;

    protected JCheckBoxMenuItem timeBarItem, internalLabelsItem, leafLabelsItem;

    protected JMenuItem findMenuItem, getInfoMenuItem;

    protected JMenuItem labelFontMenuItem, branchColorMenuItem;

    protected JMenuItem defaultLabelFontMenuItem, defaultBranchColorMenuItem;

    protected JMenu defaultsMenu, sortMenu;

    protected JMenuItem sortDateMenuItem;

    /**
     * Display Constants
     */
    protected static final String SLANTVIEW = "slant";

    protected static final String RECTVIEW = "rectangle";

    protected static final String PHYLOVIEW = "phylo";

    protected static final String CIRCLEVIEW = "circle";

    protected static final String ZOOMEDINVIEW = "zoom";

    protected static final String ZOOMEDOUTVIEW = "unzoom";

    protected int count = 1;

    /**
     * Basic Setup - Name, size, etc
     */
    @Override
    public void init() {
        treeModel = new PhyloTreeModel(new DefaultMutableTreeNode("Open Files"));
        listTree = new ListTree(treeModel);
        listTree.expandPath(new TreePath(treeModel.getRoot()));
        listTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        listTree.setEditable(true);
        listTree.setRootVisible(false);
        DefaultTreeCellEditor tce = new DefaultTreeCellEditor(listTree, (DefaultTreeCellRenderer) listTree.getCellRenderer());
        listTree.setCellEditor(tce);
        ToolTipManager.sharedInstance().registerComponent(listTree);
        TreeListCellRenderer render = new TreeListCellRenderer();
        listTree.setCellRenderer(render);
        createActions();
        toolBar = makeToolBar();
        menuBar = makeMenuBar();
        this.add(menuBar);
        this.setJMenuBar(menuBar);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(toolBar, BorderLayout.NORTH);
        listTree.addTreeSelectionListener(this);
        this.treeScrollPanel = new JPanel(new BorderLayout());
        this.treeScrollPanel.setBorder(new EtchedBorder());
        this.treeScrollPanel.add(new JScrollPane());
        boolean bShowToolbar = true;
        try {
            bShowToolbar = getParameter("withToolbar").equals("true");
        } catch (NullPointerException e) {
        }
        boolean bShowMenu = true;
        try {
            bShowMenu = getParameter("withMenu").equals("true");
        } catch (NullPointerException e) {
        }
        if (!bShowToolbar) {
            remove(toolBar);
        }
        if (!bShowMenu) {
            setJMenuBar(null);
            remove(menuBar);
        }
        try {
            String xml = getParameter("treeData");
            if (xml != null) {
                this.loadPhyloXML(xml);
            }
            String select = getParameter("selectNodes");
            if (select != null) {
                this.selectNodes(select);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        contentPane.add(this.treeScrollPanel);
        this.setContentPane(contentPane);
        this.updateMenu();
        this.centerFrame();
        this.setVisible(true);
    }

    /**
     * Load and display the given PhyloXML tree.
     * <br>
     * This currently will only show the last phylogeny in the last forest.
     * 
     * @param xml the PhyloXML tree
     * @return if it was loaded 
     */
    public boolean loadPhyloXML(String xml) {
        try {
            PhyloStringReader psr = new PhyloStringReader();
            LinkedList fl = psr.readTreeString(xml);
            DefaultMutableTreeNode forestNode;
            if (!fl.isEmpty()) {
                for (Iterator j = fl.iterator(); j.hasNext(); ) {
                    Forest f = (Forest) j.next();
                    forestNode = new DefaultMutableTreeNode(f);
                    for (Iterator i = f.getPhylogenies(); i.hasNext(); ) {
                        Phylogeny p = (Phylogeny) i.next();
                        Tree2DScrollPane tvp = this.newTree2DScrollPane(p);
                        tvp.setMinimumSize(new Dimension(300, 200));
                        this.treeScrollPanel.removeAll();
                        this.treeScrollPanel.add(tvp);
                        treeModel.insertPhylogenyInto(p, forestNode, forestNode.getChildCount());
                        if (p.getMaxPathLength() == 0) {
                            tvp.setTree2DPainter(new RectangleTree2DPainter(p, tvp.getTree2DPanel()));
                        } else {
                            tvp.setTree2DPainter(new PhyloTree2DPainter(p, tvp.getTree2DPanel()));
                        }
                        this.treeScrollPane = tvp;
                    }
                    treeModel.insertNodeInto(forestNode, (DefaultMutableTreeNode) treeModel.getRoot(), 0);
                    TreePath tp = new TreePath(treeModel.getPathToRoot(forestNode));
                    listTree.expandPath(new TreePath(treeModel.getPathToRoot(forestNode)));
                    int row = listTree.getRowForPath(new TreePath(treeModel.getPathToRoot(forestNode.getChildAt(0))));
                    listTree.setSelectionRow(row);
                }
            }
            this.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns an XML string containing the PhyloXML version of the displayed 
     * tree.
     */
    public String exportPhyloXML() {
        String emptyXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<phylogenies><\\phylogenies>";
        if (this.listTree.getSelectionCount() == 0) {
            return emptyXML;
        }
        DefaultMutableTreeNode tn = (DefaultMutableTreeNode) listTree.getLastSelectedPathComponent();
        if (tn.getParent() != treeModel.getRoot()) {
            return emptyXML;
        } else {
            Forest f = (Forest) tn.getUserObject();
            Document d = f.getOwnerDocument();
            return d.toString();
        }
    }

    /**
     * Highlight the given node
     * 
     * @param nodeName the name of the node to highlight.
     */
    public void selectNodes(String nodeName) {
        Iterator iter = treeMap.keySet().iterator();
        if (iter.hasNext()) {
            Phylogeny phylo = (Phylogeny) iter.next();
            Tree2DPanel treePanel = treeScrollPane.getTree2DPanel();
            Boolean useRegex = false;
            Boolean ignoreCase = true;
            Boolean contains = true;
            Set<Element> nodeSet = new HashSet<Element>();
            ArrayList<Element> nodes = new ArrayList<Element>();
            nodes.addAll(phylo.findCladesWithName(nodeName, useRegex, ignoreCase, contains));
            if (!nodes.isEmpty()) {
                treePanel.selectClades(nodes);
            }
        }
    }

    /**
     * Change the type of tree being shown or zoom in/out.
     * 
     * @param viewType one of the display constants
     */
    public void showView(String viewType) {
        DefaultMutableTreeNode tn = (DefaultMutableTreeNode) listTree.getLastSelectedPathComponent();
        if (tn == null) {
            return;
        }
        if (tn.isLeaf() && !tn.isRoot()) {
            Phylogeny p = (Phylogeny) tn.getUserObject();
            Tree2DScrollPane tvp = treeScrollPane;
            if (viewType.equals(SLANTVIEW)) {
                tvp.setTree2DPainter(new SlantedTree2DPainter(p, tvp.getTree2DPanel()));
            } else if (viewType.equals(RECTVIEW)) {
                tvp.setTree2DPainter(new RectangleTree2DPainter(p, tvp.getTree2DPanel()));
            } else if (viewType.equals(PHYLOVIEW) && tvp.getPhylogeny().getMaxPathLength() != 0) {
                tvp.setTree2DPainter(new PhyloTree2DPainter(p, tvp.getTree2DPanel()));
            } else if (viewType.equals(CIRCLEVIEW)) {
                tvp.setTree2DPainter(new CircleTree2DPainter(p, tvp.getTree2DPanel()));
            } else if (viewType.equals(ZOOMEDINVIEW)) {
                tvp.zoomIn(null);
            } else if (viewType.equals(ZOOMEDOUTVIEW)) {
                tvp.zoomOut(null);
            }
        }
    }

    /**
     * Centers this frame on screen when opened
     */
    public void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        this.setLocation(x, y);
    }

    /**
     * Create the tool bar.
     * 
     * @return the tool bar with all the buttons added
     */
    public JToolBar makeToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setBorder(new EtchedBorder());
        ButtonGroup typeGroup = new ButtonGroup();
        slantButton = new JToggleButton(slantAction);
        slantButton.setToolTipText("Slanted Cladogram");
        rectButton = new JToggleButton(rectAction);
        rectButton.setToolTipText("Rectangular Cladogram");
        phyloButton = new JToggleButton(phyloAction);
        phyloButton.setToolTipText("Phylogram");
        circleButton = new JToggleButton(circleAction);
        circleButton.setToolTipText("Circular Cladogram");
        typeGroup.add(slantButton);
        typeGroup.add(rectButton);
        typeGroup.add(phyloButton);
        typeGroup.add(circleButton);
        toolbar.add(slantButton);
        toolbar.add(rectButton);
        toolbar.add(phyloButton);
        toolbar.add(circleButton);
        toolbar.addSeparator();
        toolbar.add(new JButton(zoomInAction));
        toolbar.add(new JButton(zoomOutAction));
        return toolbar;
    }

    public JMenuBar makeMenuBar() {
        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        JMenuBar mb = new JMenuBar();
        editMenu = new JMenu("Edit");
        findMenuItem = new JMenuItem("Find");
        findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, shortcutKeyMask));
        findMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                findMenuAction(evt);
            }
        });
        editMenu.add(findMenuItem);
        getInfoMenuItem = new JMenuItem("Get Info");
        getInfoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, shortcutKeyMask));
        getInfoMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                getInfoMenuAction(evt);
            }
        });
        editMenu.add(getInfoMenuItem);
        mb.add(editMenu);
        viewMenu = new JMenu("View");
        slantItem = new JRadioButtonMenuItem(slantAction);
        rectItem = new JRadioButtonMenuItem(rectAction);
        phyloItem = new JRadioButtonMenuItem(phyloAction);
        circleItem = new JRadioButtonMenuItem(circleAction);
        timeBarItem = new JCheckBoxMenuItem(timeBarAction);
        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(slantItem);
        viewGroup.add(rectItem);
        viewGroup.add(phyloItem);
        viewGroup.add(circleItem);
        viewMenu.add(slantItem);
        viewMenu.add(rectItem);
        viewMenu.add(phyloItem);
        viewMenu.add(circleItem);
        viewMenu.addSeparator();
        leafLabelsItem = new JCheckBoxMenuItem("Show Leaf Labels");
        leafLabelsItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showLeafLabelsMenuAction(evt);
            }
        });
        leafLabelsItem.setSelected(true);
        viewMenu.add(leafLabelsItem);
        internalLabelsItem = new JCheckBoxMenuItem("Show Internal Labels");
        internalLabelsItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showInternalLabelsMenuAction(evt);
            }
        });
        viewMenu.add(internalLabelsItem);
        viewMenu.add(timeBarItem);
        mb.add(viewMenu);
        toolsMenu = new JMenu("Tools");
        mb.add(toolsMenu);
        sortMenu = new JMenu("Sort by...");
        sortDateMenuItem = new JMenuItem("Date");
        sortDateMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                sortDateMenuAction(evt);
            }
        });
        sortMenu.add(sortDateMenuItem);
        toolsMenu.add(sortMenu);
        toolsMenu.addSeparator();
        labelFontMenuItem = new JMenuItem("Label Font");
        labelFontMenuItem.setToolTipText("Set the default label font for the tree.");
        labelFontMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                labelFontMenuAction(evt);
            }
        });
        toolsMenu.add(labelFontMenuItem);
        branchColorMenuItem = new JMenuItem("Branch Color");
        branchColorMenuItem.setToolTipText("Set the default color for the branchs of the tree.");
        branchColorMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                branchColorMenuAction(evt);
            }
        });
        toolsMenu.add(branchColorMenuItem);
        toolsMenu.addSeparator();
        defaultsMenu = new JMenu("Defaults");
        defaultLabelFontMenuItem = new JMenuItem("Label Font");
        defaultLabelFontMenuItem.setToolTipText("Set the 'global' default label font for the tree.");
        defaultLabelFontMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                defaultLabelFontMenuAction(evt);
            }
        });
        defaultsMenu.add(defaultLabelFontMenuItem);
        defaultBranchColorMenuItem = new JMenuItem("Branch Color");
        defaultBranchColorMenuItem.setToolTipText("Set the 'global' default color for the branchs of the tree.");
        defaultBranchColorMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                defaultBranchColorMenuAction(evt);
            }
        });
        defaultsMenu.add(defaultBranchColorMenuItem);
        toolsMenu.add(defaultsMenu);
        return mb;
    }

    public void createActions() {
        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        slantAction = new SlantActionClass("Slanted", null);
        rectAction = new RectActionClass("Rectangular", null);
        phyloAction = new PhyloActionClass("Phylogram", null);
        circleAction = new CircleActionClass("Circular", null);
        timeBarAction = new TimeBarActionClass("Time Bar", null);
        colorAction = new ColorActionClass("Color", null);
        zoomInAction = new ZoomInActionClass("Zoom In", null);
        zoomOutAction = new ZoomOutActionClass("Zoom Out", null);
    }

    /**
     * Handles updating the menu bar and items to reflect the current state.
     */
    public void updateMenu() {
        if (!(treeScrollPane instanceof Tree2DScrollPane)) {
            editMenu.setEnabled(false);
            viewMenu.setEnabled(false);
            toolsMenu.setEnabled(false);
            slantButton.setEnabled(false);
            phyloButton.setEnabled(false);
            rectButton.setEnabled(false);
            phyloButton.setEnabled(false);
            circleButton.setEnabled(false);
            timeBarItem.setSelected(false);
            return;
        }
        editMenu.setEnabled(true);
        viewMenu.setEnabled(true);
        toolsMenu.setEnabled(true);
        Tree2DPainter treePainter = treeScrollPane.getTree2DPainter();
        Tree2DPanel treePanel = treeScrollPane.getTree2DPanel();
        leafLabelsItem.setSelected(treePanel.isShownLeafLabels());
        internalLabelsItem.setSelected(treePanel.isShownInternalLabels());
        slantButton.setEnabled(true);
        rectButton.setEnabled(true);
        if (treePanel.getPhylogeny().getMaxPathLength() == 0) {
            phyloAction.setEnabled(false);
            phyloButton.setToolTipText("Phylogram - branch lengths not given");
            phyloButton.setEnabled(false);
        } else {
            phyloAction.setEnabled(true);
            phyloButton.setToolTipText("Phylogram");
            phyloButton.setEnabled(true);
        }
        if (treePanel instanceof TimeAxisTree2DPanel) {
            circleButton.setEnabled(false);
            circleItem.setEnabled(false);
            timeBarItem.setSelected(true);
        } else {
            circleButton.setEnabled(true);
            circleItem.setEnabled(true);
            timeBarItem.setSelected(false);
        }
        if (treePainter instanceof CircleTree2DPainter) {
            circleButton.setSelected(true);
            circleItem.setSelected(true);
            timeBarItem.setEnabled(false);
        } else if (treePainter instanceof PhyloTree2DPainter) {
            phyloButton.setSelected(true);
            phyloItem.setSelected(true);
            timeBarItem.setEnabled(true);
        } else if (treePainter instanceof RectangleTree2DPainter) {
            rectButton.setSelected(true);
            rectItem.setSelected(true);
            timeBarItem.setEnabled(true);
        } else {
            slantButton.setSelected(true);
            slantItem.setSelected(true);
            timeBarItem.setEnabled(true);
        }
        if (treePanel.getZoomFactor() == Tree2DPanelPreferences.getDefaultZoomFactor()) {
            zoomOutAction.setEnabled(false);
            zoomInAction.setEnabled(true);
        } else {
            zoomOutAction.setEnabled(true);
            zoomInAction.setEnabled(true);
        }
        findMenuItem.setEnabled(true);
    }

    /**
     * Add the given Tree ScrollPane
     */
    public void addTree2DScrollPane(Tree2DScrollPane scrollPane) {
        scrollPane.addTree2DPaneChangeListener(this);
        treeMap.put(scrollPane.getPhylogeny(), scrollPane);
    }

    /**
     * Make a new ScrollPane with the given Phylogeny
     */
    public Tree2DScrollPane newTree2DScrollPane(Phylogeny phylogeny) {
        Tree2DScrollPane pane = new Tree2DScrollPane(phylogeny);
        this.addTree2DScrollPane(pane);
        return pane;
    }

    /**
     * Event handler.
     * 
     * @param evt the change event
     */
    public void tree2DPaneChanged(Tree2DPaneChangeEvent evt) {
        updateMenu();
    }

    /**
     * TreeSelectionListener - Handle tree selection events.
     * <P>
     * If the node selected is a tree (ie, leaf and not root), the split pane
     * updates to show the selected tree.
     * 
     * @param evt the tree selection event.
     */
    public void valueChanged(TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.listTree.getLastSelectedPathComponent();
        if (node == null || node.isRoot()) {
            return;
        }
        Object nodeInfo = node.getUserObject();
        if (node.getParent() != this.treeModel.getRoot()) {
            Phylogeny p = (Phylogeny) nodeInfo;
            if (!treeMap.containsKey(p)) {
                this.treeScrollPane = this.newTree2DScrollPane(p);
            } else {
                this.treeScrollPane = treeMap.get(p);
            }
            this.treeScrollPanel.removeAll();
            this.treeScrollPanel.add(this.treeScrollPane);
            updateMenu();
        } else {
            this.treeScrollPane = null;
            this.treeScrollPane.removeAll();
            updateMenu();
        }
    }

    /**
     * lostOwnership method for clipboard.
     */
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
    }

    /**
     * Handle "Find" menu events.
     * 
     * @param evt
     */
    public void findMenuAction(ActionEvent evt) {
        Phylogeny phylo = this.treeScrollPane.getTree2DPanel().getPhylogeny();
        List<Element> cladeL = FindCladeDialog.showDialog(getContentPane(), this.treeScrollPane.getTree2DPanel());
        if (cladeL == null) {
            return;
        }
        selectNodes("A");
        this.treeScrollPane.getTree2DPanel().selectClades(cladeL);
        this.treeScrollPane.requestFocusInWindow();
    }

    /**
     * Handle "Get Info" menu events. 
     * 
     * @param evt
     */
    public void getInfoMenuAction(ActionEvent evt) {
        if (this.treeScrollPane == null) {
            return;
        }
        Tree2DPanel treePanel = this.treeScrollPane.getTree2DPanel();
        PhylogenyInspectorDialog.showDialog(treePanel.getPhylogeny());
    }

    /** 
     * Handle "Show Leaf Labels" menu events.
     */
    public void showLeafLabelsMenuAction(ActionEvent evt) {
        if (this.treeScrollPane == null) {
            return;
        }
        Tree2DPanel tp = this.treeScrollPane.getTree2DPanel();
        if (tp.isShownLeafLabels()) {
            tp.showLeafLabels(false);
            leafLabelsItem.setSelected(false);
        } else {
            tp.showLeafLabels(true);
            leafLabelsItem.setSelected(true);
        }
        this.treeScrollPane.repaint();
    }

    /** 
     * Handle "Show Internal Labels" menu events.
     *
     * @param evt the event
     */
    public void showInternalLabelsMenuAction(ActionEvent evt) {
        if (this.treeScrollPane == null) {
            return;
        }
        Tree2DPanel tp = this.treeScrollPane.getTree2DPanel();
        if (tp.isShownInternalLabels()) {
            tp.showInternalLabels(false);
            internalLabelsItem.setSelected(false);
        } else {
            tp.showInternalLabels(true);
            internalLabelsItem.setSelected(true);
        }
        this.treeScrollPane.repaint();
    }

    /**
     * Handle "Sort -> Date" menu events.
     * 
     * @param evt the event
     */
    public void sortDateMenuAction(ActionEvent evt) {
        DefaultMutableTreeNode lastSelected = (DefaultMutableTreeNode) listTree.getLastSelectedPathComponent();
        if (lastSelected == null) {
            return;
        }
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) lastSelected.getParent();
        if (parent.equals(treeModel.getRoot())) {
            System.err.println("SortDateTreeACtionClass: Cannot perform sort for this node.");
            return;
        }
        Tree2DPanel tp = this.treeScrollPane.getTree2DPanel();
        Phylogeny sortedPhylo = TaxonDateSorterDialog.showDialog(getContentPane(), tp.getPhylogeny());
        if (sortedPhylo == null) {
            return;
        }
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(sortedPhylo);
        treeModel.insertNodeInto(newNode, parent, parent.getChildCount());
        Tree2DScrollPane scrollPane = this.newTree2DScrollPane(sortedPhylo);
        if (tp instanceof TimeAxisTree2DPanel) {
            scrollPane.setTree2DPanel(new TimeAxisTree2DPanel(scrollPane.getPhylogeny()));
        }
        Tree2DPainter painter = null;
        Class[] intArgsClass = new Class[] { Phylogeny.class, Tree2DPanel.class };
        Object[] intArgs = new Object[] { sortedPhylo, scrollPane.getTree2DPanel() };
        try {
            Class painterClass = tp.getTree2DPainter().getClass();
            Constructor constructor = painterClass.getConstructor(intArgsClass);
            painter = (Tree2DPainter) constructor.newInstance(intArgs);
        } catch (Exception e) {
            System.err.println(e);
        }
        if (painter != null) {
            scrollPane.setTree2DPainter(painter);
        }
        listTree.setSelectionPath(new TreePath(newNode.getPath()));
    }

    /**
     * Handle "Label Font" menu events by putting up a font dialog box.
     *
     * @param evt the event
     */
    public void labelFontMenuAction(ActionEvent evt) {
        Tree2DPanel treePanel = treeScrollPane.getTree2DPanel();
        Tree2DPanelPreferences prefs = treePanel.getPreferences();
        final Frame frame = JOptionPane.getFrameForComponent(this);
        final ColorFontChooserDialog dialog = new ColorFontChooserDialog(frame, prefs.getCladeLabelFont(), prefs.getCladeLabelColor(), treePanel.getPhylogeny().toString());
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        if (!dialog.isCancelled()) {
            Font newFont = dialog.getSelectedFont();
            prefs.setCladeLabelFont(newFont);
            Color newColor = dialog.getSelectedColor();
            prefs.setCladeLabelColor(newColor);
        }
    }

    /**
     * Handle "Branch Color" menu events by putting up a color chooser dialog box.
     *
     * @param evt the event
     */
    public void branchColorMenuAction(ActionEvent evt) {
        Tree2DPanelPreferences pref = treeScrollPane.getTree2DPanel().getPreferences();
        Color color = pref.getCladeBranchColor();
        Color newColor = JColorChooser.showDialog(this, "", color);
        if (newColor != null) {
            pref.setCladeBranchColor(newColor);
        }
    }

    /**
     * Handle "Default->Label Font" menu events by putting up a font dialog box.
     *
     * @param evt the event
     */
    public void defaultLabelFontMenuAction(ActionEvent evt) {
        Tree2DPanel treePanel = treeScrollPane.getTree2DPanel();
        Tree2DPanelPreferences prefs = treePanel.getPreferences();
        final Frame frame = JOptionPane.getFrameForComponent(this);
        final ColorFontChooserDialog dialog = new ColorFontChooserDialog(frame, prefs.getCladeLabelFont(), (Color) prefs.getCladeLabelColor(), treePanel.getPhylogeny().toString());
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        if (!dialog.isCancelled()) {
            Font newFont = dialog.getSelectedFont();
            Tree2DPanelPreferences.setDefaultCladeLabelFont(newFont);
            Color newColor = dialog.getSelectedColor();
            Tree2DPanelPreferences.setDefaultCladeLabelColor(newColor);
        }
    }

    /**
     * Handle "Default->Branch Color" menu events by putting up a color chooser dialog box.
     *
     * @param evt the event
     */
    public void defaultBranchColorMenuAction(ActionEvent evt) {
        Tree2DPanelPreferences pref = treeScrollPane.getTree2DPanel().getPreferences();
        Color color = (Color) pref.getCladeBranchColor();
        Color newColor = JColorChooser.showDialog(this, "", color);
        if (newColor != null) {
            Tree2DPanelPreferences.setDefaultCladeBranchColor(newColor);
        }
    }

    /**
     * View menu item - Slanted Tree View Action
     */
    public class SlantActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SlantActionClass(String text, KeyStroke shortCut) {
            super(text);
            putValue(ACCELERATOR_KEY, shortCut);
        }

        public void actionPerformed(ActionEvent e) {
            showView(SLANTVIEW);
        }
    }

    /** 
     * View menu item - Rectangle Tree View Action
     */
    public class RectActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public RectActionClass(String text, KeyStroke shortCut) {
            super(text);
            putValue(ACCELERATOR_KEY, shortCut);
        }

        public void actionPerformed(ActionEvent e) {
            showView(RECTVIEW);
        }
    }

    /**
     * View menu item - Phylogram Tree View Action
     */
    public class PhyloActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public PhyloActionClass(String text, KeyStroke shortCut) {
            super(text);
            putValue(ACCELERATOR_KEY, shortCut);
        }

        public void actionPerformed(ActionEvent e) {
            showView(PHYLOVIEW);
        }
    }

    /**
     * View menu item - Circular Tree View Action
     */
    public class CircleActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public CircleActionClass(String text, KeyStroke shortCut) {
            super(text);
            putValue(ACCELERATOR_KEY, shortCut);
        }

        public void actionPerformed(ActionEvent e) {
            showView(CIRCLEVIEW);
        }
    }

    /**
     * View menu item - Put the current Tree2DPainter into a TimeBarPanel.
     */
    public class TimeBarActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public TimeBarActionClass(String text, KeyStroke shortCut) {
            super(text);
        }

        public void actionPerformed(ActionEvent e) {
            DefaultMutableTreeNode tn = (DefaultMutableTreeNode) listTree.getLastSelectedPathComponent();
            if (tn == null) {
                return;
            }
            if (tn.isLeaf() && !tn.isRoot()) {
                Phylogeny p = (Phylogeny) tn.getUserObject();
                Tree2DScrollPane tvp = treeScrollPane;
                Tree2DPanel treePanel = tvp.getTree2DPanel();
                if (treePanel instanceof TimeAxisTree2DPanel) {
                    Tree2DPanel tp = new BasicTree2DPanel(treePanel.getPhylogeny());
                    tp.selectClades(treePanel.getSelectedClades());
                    Tree2DPainter treePainter = treePanel.getTree2DPainter();
                    treePainter.setTree2DPanel(tp);
                    tp.setTree2DPainter(treePainter);
                    tvp.setTree2DPanel(tp);
                } else {
                    TimeAxisTree2DPanel tbp = new TimeAxisTree2DPanel(p);
                    tbp.selectClades(treePanel.getSelectedClades());
                    Tree2DPainter treePainter = treePanel.getTree2DPainter();
                    treePainter.setTree2DPanel(tbp);
                    tbp.setTree2DPainter(treePainter);
                    tvp.setTree2DPanel(tbp);
                }
                updateMenu();
            }
        }
    }

    public class ZoomInActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ZoomInActionClass(String text, KeyStroke shortCut) {
            super(text);
            putValue(ACCELERATOR_KEY, shortCut);
        }

        public void actionPerformed(ActionEvent e) {
            showView(ZOOMEDINVIEW);
        }
    }

    public class ZoomOutActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ZoomOutActionClass(String text, KeyStroke shortCut) {
            super(text);
            putValue(ACCELERATOR_KEY, shortCut);
        }

        public void actionPerformed(ActionEvent e) {
            showView(ZOOMEDOUTVIEW);
        }
    }

    /**
     * This only sets the default clade branch color for the selected phylogeny,
     * not the text label colors.
     */
    public class ColorActionClass extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ColorActionClass(String text, KeyStroke shortCut) {
            super(text);
        }

        public void actionPerformed(ActionEvent e) {
            DefaultMutableTreeNode tn = (DefaultMutableTreeNode) listTree.getLastSelectedPathComponent();
            if (tn == null || tn.getParent() == treeModel.getRoot()) {
                return;
            }
            Color c = JColorChooser.showDialog(TreeViewJApplet.this, "Color", Color.BLACK);
            if (c == null) {
                c = Color.BLACK;
            }
            Tree2DPanel treePanel = treeScrollPane.getTree2DPanel();
            if (treePanel == null) {
                return;
            }
            treePanel.setCladeBranchColor(c);
        }
    }
}
