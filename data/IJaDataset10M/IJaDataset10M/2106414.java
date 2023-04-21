package com.cube42.tools.ssscripter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.gui.DialogUtils;
import com.cube42.util.gui.GUIFileUtils;
import com.cube42.util.system.MachineID;
import com.cube42.util.system.SubSystemID;

/**
 * Panel that allows a user to run, create, load, and save SSScripts
 * <p>
 * SSScripts are used to specify the order and machine that subsystems
 * are to be started and shutdown
 *
 * @author  Matt Paulin
 * @version $Id: StartupShutdownScripter.java,v 1.9 2003/11/15 00:38:06 ajcantu Exp $
 */
public class StartupShutdownScripter extends JPanel {

    /**
     * The path that the images are stored at
     */
    static final String IMAGE_PATH = "icons/";

    /**
     * File extension to use on the shutdown startup scripts
     */
    private static final String FILE_EXTENSION = "ssscript";

    /**
     * Icon to use in the tree if none other was provided
     */
    private static final ImageIcon DEFAULT_ICON = new ImageIcon(ClassLoader.getSystemResource(IMAGE_PATH + "dot.gif"));

    /**
     * Popup menu used with the scripter node
     */
    private JPopupMenu scriptPopupMenu;

    /**
     * Popup menu used by the ActionBlock node
     */
    private JPopupMenu actionBlockPopupMenu;

    /**
     * Popup menu used by the ActionNode
     */
    private JPopupMenu actionPopupMenu;

    /**
     * Button used to run the script
     */
    private JButton runButton;

    /**
     * Button used to reset the script
     */
    private JButton resetButton;

    /**
     * Button used to stop the script
     */
    private JButton stopButton;

    /**
     * Button used to save the script
     */
    private JButton saveButton;

    /**
     * Button used to load the script
     */
    private JButton loadButton;

    /**
     * Button used to clear the script
     */
    private JButton clearButton;

    /**
     * The tree model to contain all of the data
     */
    private DefaultTreeModel model;

    /**
     * Dialog used to create new startup nodes
     */
    private SubSystemMachineSelectionDialog smsDialog;

    /**
     * Tree used to show the whole system
     */
    private JTree systemTree;

    /**
     * The root of the tree
     */
    private ScriptNode root;

    /**
     * The node that was last selected
     */
    private Object selectedObject;

    /**
     * Target used to start all the starters
     */
    private SSScriptTarget target;

    /**
     * Action Block Editor for editing the action block
     */
    private ActionBlockEditorDialog actionBlockEditor;

    /**
     * Used to process the script
     */
    private ScriptProcessor processor;

    /**
	 * Used to create DOM structures (from files and from scratch)
	 */
    private static DocumentBuilderFactory documentBuilderFactory = null;

    /**
	 * Lock for Document Builder.
	 */
    private static final Object documentBuilderFactoryLock = new Object();

    /**
	 * Used to write DOM structures (to streams)
	 */
    private static TransformerFactory transformerFactory = null;

    /**
	 * Lock for TransformerFactory.
	 */
    private static final Object transformerFactoryLock = new Object();

    /**
     * Constructs the SubSystemLifeCyclePanel
     *
     * @param   target  The SSScriptTarget used to start the subsystems
     */
    public StartupShutdownScripter(SSScriptTarget target) {
        super();
        this.target = target;
        this.smsDialog = new SubSystemMachineSelectionDialog();
        this.actionBlockEditor = new ActionBlockEditorDialog();
        JMenuItem createActionBlockMenuItem;
        createActionBlockMenuItem = new JMenuItem("Create Action Block");
        createActionBlockMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createActionBlockAction();
            }
        });
        this.scriptPopupMenu = new JPopupMenu("Script Options");
        this.scriptPopupMenu.add(createActionBlockMenuItem);
        JMenuItem createCoreStartupMenuItem;
        createCoreStartupMenuItem = new JMenuItem("Core Startup");
        createCoreStartupMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createCoreStartupAction();
            }
        });
        JMenuItem createCoreShutdownMenuItem;
        createCoreShutdownMenuItem = new JMenuItem("Core Shutdown");
        createCoreShutdownMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createCoreShutdownAction();
            }
        });
        JMenuItem createFragmentStartupMenuItem;
        createFragmentStartupMenuItem = new JMenuItem("Fragment Startup");
        createFragmentStartupMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createFragmentStartupAction();
            }
        });
        JMenuItem createFragmentShutdownMenuItem;
        createFragmentShutdownMenuItem = new JMenuItem("Fragment Shutdown");
        createFragmentShutdownMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createFragmentShutdownAction();
            }
        });
        JMenu createActionMenu = new JMenu("Create Action");
        createActionMenu.add(createCoreStartupMenuItem);
        createActionMenu.add(createCoreShutdownMenuItem);
        createActionMenu.add(createFragmentStartupMenuItem);
        createActionMenu.add(createFragmentShutdownMenuItem);
        JMenuItem deleteActionBlockMenuItem;
        deleteActionBlockMenuItem = new JMenuItem("Delete");
        deleteActionBlockMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteActionBlockAction();
            }
        });
        this.actionBlockPopupMenu = new JPopupMenu();
        this.actionBlockPopupMenu.add(createActionMenu);
        this.actionBlockPopupMenu.add(deleteActionBlockMenuItem);
        JMenuItem deleteActionMenuItem;
        deleteActionMenuItem = new JMenuItem("Delete");
        deleteActionMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteActionAction();
            }
        });
        this.actionPopupMenu = new JPopupMenu();
        this.actionPopupMenu.add(deleteActionMenuItem);
        this.runButton = new JButton("Run");
        this.runButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                runScriptAction();
            }
        });
        this.resetButton = new JButton("Reset");
        this.resetButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resetScriptAction();
            }
        });
        this.stopButton = new JButton("Stop");
        this.stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopScriptAction();
            }
        });
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(Box.createHorizontalStrut(10));
        northPanel.add(this.runButton);
        northPanel.add(Box.createHorizontalStrut(10));
        northPanel.add(this.resetButton);
        northPanel.add(Box.createHorizontalStrut(10));
        northPanel.add(this.stopButton);
        northPanel.add(Box.createHorizontalStrut(10));
        this.saveButton = new JButton("Save");
        this.saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveScriptAction();
            }
        });
        this.loadButton = new JButton("Load");
        this.loadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loadScriptAction();
            }
        });
        this.clearButton = new JButton("Clear");
        this.clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clearScriptAction();
            }
        });
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        southPanel.add(Box.createHorizontalStrut(10));
        southPanel.add(this.saveButton);
        southPanel.add(Box.createHorizontalStrut(10));
        southPanel.add(this.loadButton);
        southPanel.add(Box.createHorizontalStrut(10));
        southPanel.add(this.clearButton);
        southPanel.add(Box.createHorizontalStrut(10));
        root = new ScriptNode();
        this.model = new DefaultTreeModel(this.root);
        this.systemTree = new JTree(this.model);
        this.systemTree.putClientProperty("JTree.lineStyle", "Angled");
        this.systemTree.setShowsRootHandles(false);
        this.systemTree.setToggleClickCount(20);
        this.systemTree.setCellRenderer(new ScriptTreeRenderer());
        this.systemTree.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    showSelectedNode(e);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    showPopupMenu(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    showPopupMenu(e);
                }
            }
        });
        this.setLayout(new BorderLayout());
        this.add(northPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(this.systemTree), BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Updates the tree to the user with all the new information
     */
    public void updateTree() {
        this.model.reload();
        this.expandAll(this.systemTree, new TreePath(this.root));
    }

    /**
     * Called when the mouse is double clicked
     *
     * @param   e   MouseEvent
     */
    void showSelectedNode(MouseEvent e) {
        TreePath path = this.systemTree.getPathForLocation(e.getX(), e.getY());
        if (path != null) {
            this.systemTree.setSelectionPath(path);
            this.selectedObject = path.getLastPathComponent();
            if (selectedObject instanceof ActionBlockNode) {
                ActionBlockNode abNode = (ActionBlockNode) this.selectedObject;
                this.actionBlockEditor.editActionBlock(abNode);
                this.updateTree();
            }
            if (selectedObject instanceof ActionNode) {
                ActionNode aNode = (ActionNode) this.selectedObject;
                if (aNode.getState() == ActionNode.ERROR_STATE) {
                    ActionNodeErrorDialog errorDialog = new ActionNodeErrorDialog(aNode);
                }
            }
        }
    }

    /**
     * Called when the mouse is clicked
     *
     * @param   e   MouseEvent
     */
    void showPopupMenu(MouseEvent e) {
        TreePath path = this.systemTree.getPathForLocation(e.getX(), e.getY());
        if (path != null) {
            this.systemTree.setSelectionPath(path);
            this.selectedObject = path.getLastPathComponent();
            if (selectedObject instanceof ScriptNode) {
                this.scriptPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
            if (selectedObject instanceof ActionBlockNode) {
                this.actionBlockPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
            if (selectedObject instanceof ActionNode) {
                this.actionPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Creates a new Action Block
     */
    void createActionBlockAction() {
        ActionBlockNode tempNode = new ActionBlockNode(this.root);
        this.actionBlockEditor.editActionBlock(tempNode);
        if (!this.actionBlockEditor.isCancelled()) {
            this.root.addActionBlockNode(tempNode);
            this.updateTree();
        }
    }

    /**
     * Creates a new core startup action
     */
    void createCoreStartupAction() {
        if (this.selectedObject != null) {
            if (selectedObject instanceof ActionBlockNode) {
                this.smsDialog.selectCore(this.target.getAvaiableCores(), this.target.getAvailableMachines());
                if (!this.smsDialog.isCancelled()) {
                    ActionBlockNode abnode = (ActionBlockNode) this.selectedObject;
                    SubSystemID core = smsDialog.getSelectedSubSystemID();
                    if (core == null) {
                        DialogUtils.createErrorDialog("No Core was selected");
                        return;
                    }
                    MachineID machine = smsDialog.getSelectedMachineID();
                    if (machine == null) {
                        DialogUtils.createErrorDialog("No Machine was selected");
                        return;
                    }
                    abnode.addActionNode(new CoreStartupNode(abnode, core, machine));
                    this.updateTree();
                }
            }
        }
    }

    /**
     * Creates a new core shutdown action
     */
    void createCoreShutdownAction() {
        if (this.selectedObject != null) {
            if (selectedObject instanceof ActionBlockNode) {
                this.smsDialog.selectCore(this.target.getAvaiableCores(), this.target.getAvailableMachines());
                if (!this.smsDialog.isCancelled()) {
                    ActionBlockNode abnode = (ActionBlockNode) this.selectedObject;
                    SubSystemID core = smsDialog.getSelectedSubSystemID();
                    if (core == null) {
                        DialogUtils.createErrorDialog("No Core was selected");
                        return;
                    }
                    MachineID machine = smsDialog.getSelectedMachineID();
                    if (machine == null) {
                        DialogUtils.createErrorDialog("No Machine was selected");
                        return;
                    }
                    abnode.addActionNode(new CoreShutdownNode(abnode, core, machine));
                    this.updateTree();
                }
            }
        }
    }

    /**
     * Creates a new fragment startup action
     */
    void createFragmentStartupAction() {
        if (this.selectedObject != null) {
            if (selectedObject instanceof ActionBlockNode) {
                this.smsDialog.selectFragment(this.target.getAvailableFragments(), this.target.getAvailableMachines());
                if (!this.smsDialog.isCancelled()) {
                    ActionBlockNode abnode = (ActionBlockNode) this.selectedObject;
                    SubSystemID fragment = smsDialog.getSelectedSubSystemID();
                    if (fragment == null) {
                        DialogUtils.createErrorDialog("No Fragment was selected");
                        return;
                    }
                    MachineID machine = smsDialog.getSelectedMachineID();
                    if (machine == null) {
                        DialogUtils.createErrorDialog("No Machine was selected");
                        return;
                    }
                    abnode.addActionNode(new FragmentStartupNode(abnode, fragment, machine));
                    this.updateTree();
                }
            }
        }
    }

    /**
     * Creates a new fragment shutdown action
     */
    void createFragmentShutdownAction() {
        if (this.selectedObject != null) {
            if (selectedObject instanceof ActionBlockNode) {
                this.smsDialog.selectFragment(this.target.getAvailableFragments(), this.target.getAvailableMachines());
                if (!this.smsDialog.isCancelled()) {
                    ActionBlockNode abnode = (ActionBlockNode) this.selectedObject;
                    SubSystemID fragment = smsDialog.getSelectedSubSystemID();
                    if (fragment == null) {
                        DialogUtils.createErrorDialog("No Fragment was selected");
                        return;
                    }
                    MachineID machine = smsDialog.getSelectedMachineID();
                    if (machine == null) {
                        DialogUtils.createErrorDialog("No Machine was selected");
                        return;
                    }
                    abnode.addActionNode(new FragmentShutdownNode(abnode, fragment, machine));
                    this.updateTree();
                }
            }
        }
    }

    /**
     * Called when the selected action block is to be deleted
     */
    void deleteActionBlockAction() {
        if (this.selectedObject != null) {
            if (selectedObject instanceof ActionBlockNode) {
                this.root.remove(selectedObject);
                this.updateTree();
            }
        }
    }

    /**
     * Called when the selected action is to be deleted
     */
    void deleteActionAction() {
        if (this.selectedObject != null) {
            if (selectedObject instanceof ActionNode) {
                ActionNode anode = (ActionNode) this.selectedObject;
                anode.getActionBlockNode().remove(anode);
                this.updateTree();
            }
        }
    }

    /**
     * Action that runs the loaded script
     */
    void runScriptAction() {
        if (processor == null) {
            this.processor = new ScriptProcessor(this.root, this.target, this);
        } else {
            if (this.processor.getState() == ScriptProcessor.FINISHED) {
                this.resetScriptAction();
                this.processor = new ScriptProcessor(this.root, this.target, this);
            }
        }
        if (this.processor.getState() == ScriptProcessor.STOPPED) {
            this.processor.start();
        } else {
            DialogUtils.createErrorDialog("Script is already running");
        }
    }

    /**
     * Action that resets the script that is being run
     */
    void resetScriptAction() {
        this.root.reset();
        this.updateTree();
    }

    /**
     * Action that stops the script being run
     */
    void stopScriptAction() {
        if (this.processor != null) {
            if (this.processor.getState() == ScriptProcessor.RUNNING) {
                this.processor.stop();
            } else {
                DialogUtils.createErrorDialog("No Script is running");
            }
        } else {
            DialogUtils.createErrorDialog("No Script is running");
        }
    }

    /**
     * Action that saves the created script
     */
    void saveScriptAction() {
        try {
            GUIFileUtils.saveString(convertToXML(this.root), FILE_EXTENSION, "Startup Shutdown Script");
        } catch (Cube42Exception e) {
            DialogUtils.createErrorDialog(e);
        }
    }

    /**
     * Action that loads a script
     */
    void loadScriptAction() {
        try {
            File file = GUIFileUtils.selectFile(FILE_EXTENSION, "Startup Shutdown Script");
            Document doc;
            Element tempElement = null;
            if (file != null) {
                try {
                    doc = getDocumentBuilder().parse(file);
                    tempElement = doc.getDocumentElement();
                    ScriptNode sNode = new ScriptNode(tempElement);
                    for (int i = 0; i < sNode.size(); i++) {
                        this.root.add(sNode.elementAt(i));
                    }
                    this.updateTree();
                } catch (SAXException e) {
                    DialogUtils.createErrorDialog("Corrupt Script File " + e.getMessage());
                } catch (IOException e) {
                    DialogUtils.createErrorDialog("Corrupt Script File " + e.getMessage());
                }
            }
        } catch (Cube42Exception e) {
            DialogUtils.createErrorDialog(e);
        }
    }

    /**
     * Action that clears a script
     */
    void clearScriptAction() {
        if (DialogUtils.createWarningConfirmDialog("This will delete your current script.\nContinue?")) {
            this.root.clear();
            this.updateTree();
        }
    }

    /**
     * Converts the Script Node to an XML element
     *
     * @param   scriptNode  The script node to convert
     * @return  The XML Element that is used to store the XML
     * @throws  Cube42Exception if the node could not be converted
     */
    private static String convertToXML(ScriptNode scriptNode) throws Cube42Exception {
        ByteArrayOutputStream out;
        try {
            Element xml = scriptNode.toXML(getDocumentBuilder().newDocument());
            out = new ByteArrayOutputStream();
            writeElementToOutputStream(xml, out);
            return out.toString();
        } catch (IOException e) {
            throw new Cube42Exception(SSScriptSystemCodes.IOEXCEPTION_CONVERTING_TO_XML, new Object[] { e.getMessage() });
        } catch (TransformerException e) {
            throw new Cube42Exception(SSScriptSystemCodes.IOEXCEPTION_CONVERTING_TO_XML, new Object[] { e.getMessage() });
        }
    }

    /**
     * Method that recursivily expands all of the tree
     *
     * @param   tree    The JTree to expand
     * @param   parent  The parent TreePath
     */
    private void expandAll(JTree tree, TreePath parent) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (!node.isLeaf()) {
            if (node.getChildCount() >= 0) {
                for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode) e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    expandAll(tree, path);
                }
            }
        }
        tree.expandPath(parent);
    }

    /**
     * Static utility method for an attribute from the provided element
     *
     * @param   element     The element to extract the machine name from
     * @param   attribute   The attribute to extract
     * @param   type        The type of node extracting the machine name
     * @return  The machine name in this element
     * @throws  Cube42Exception if this attribute is not found
     */
    protected static String extractAttribute(Element element, String attribute, String type) throws Cube42Exception {
        if (element == null) {
            throw new Cube42Exception(SSScriptSystemCodes.NULL_ELEMENT_ATTRIBUTE, new Object[] { attribute, type });
        }
        String tempAttribute = element.getAttribute(attribute);
        if (tempAttribute == null) {
            throw new Cube42Exception(SSScriptSystemCodes.MISSING_ATTRIBUTE, new Object[] { attribute, type });
        }
        return tempAttribute;
    }

    /**
     * Used to render everything on the tree
     */
    protected class ScriptTreeRenderer extends DefaultTreeCellRenderer {

        /**
         * Sets the render component of the specified tree cell value
         */
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof ActionNode) {
                ActionNode aNode = (ActionNode) value;
                this.setIcon(aNode.getIcon());
            } else {
                this.setIcon(StartupShutdownScripter.DEFAULT_ICON);
            }
            return this;
        }
    }

    /**
	 * Writes a DOM Element to an OutputStream.
	 * 
	 * @param e The DOM Element
	 * @param os The OutputStream
	 * @throws TransformerException If the transformation fails
	 * @throws IOException If the stream cannot be flushed after transformation
	 */
    private static void writeElementToOutputStream(Element e, OutputStream os) throws TransformerException, IOException {
        Transformer transformer = null;
        transformer = getTransformer();
        DOMSource source = new DOMSource(e);
        StreamResult result = new StreamResult(os);
        transformer.transform(source, result);
        os.flush();
    }

    /**
	 * Gets a new DocumentBuilder, which can be safely used by a single thread.
	 * This method may be called by multiple threads, but the returned DocumentBuilder may not.
	 *
	 * @returns a new DocumentBuilder, which can be safely used by a single thread.
	 */
    private static DocumentBuilder getDocumentBuilder() {
        DocumentBuilder documentBuilder;
        synchronized (documentBuilderFactoryLock) {
            if (documentBuilderFactory == null) documentBuilderFactory = DocumentBuilderFactory.newInstance();
            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return documentBuilder;
    }

    /**
	 * Gets a new Transformer, which can be safely used by a single thread.
	 * This method may be called by multiple threads, but the returned Transformer may not.
	 *
	 * @returns a new Transformer, which can be safely used by a single thread.
	 */
    private static Transformer getTransformer() {
        Transformer transformer;
        synchronized (transformerFactoryLock) {
            if (transformerFactory == null) transformerFactory = TransformerFactory.newInstance();
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return transformer;
    }
}
