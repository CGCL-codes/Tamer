package com.darwinit.xmleditor;

import com.darwinit.xmleditor.xmltree.XMLTree;
import com.darwinit.xmleditor.xmltree.XMLTreeModel;
import com.darwinit.xmleditor.xmltree.XMLTreeNode;
import com.darwinit.xmlfiles.XMLFile;
import com.darwinit.xmlfiles.xml.NodeSelected;
import com.darwinit.xmlfiles.xml.NodeSelection;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XMLEditorPanel: Class to provide the XMLEditor functionality.
 *
 * @author Martien van den Akker
 * @author Darwin IT Professionals
 *
 */
public class XMLEditorPanel extends JPanel {

    private IXMLEditorFrame parentFrame = null;

    private JToolBar buttonBar = new JToolBar();

    private JSplitPane sptCenterPane = new JSplitPane();

    private JScrollPane scpTreePane = new JScrollPane();

    private GridBagLayout gridBagLayout = new GridBagLayout();

    private GridBagConstraints gridBagConstraints = new GridBagConstraints();

    private JSplitPane sptPaneRight = new JSplitPane();

    private JPanel parentPane = new JPanel();

    private JPanel childPane = new JPanel();

    private JLabel lblNodeName = new JLabel("Node: ");

    private JTextField tfNodeName = new JTextField();

    private JButton btnAddNode = new JButton();

    private JButton btnCopyNode = new JButton();

    private JLabel lblAttrName = new JLabel("Attribute: ");

    private JTextField tfAttrName = new JTextField();

    private JButton btnAddAttr = new JButton();

    private JButton btnDelNode = new JButton();

    private JButton btnAddTextNode = new JButton();

    private JButton btnMoveUp = new JButton();

    private JButton btnMoveDwn = new JButton();

    private AttributeTable parentTable = new AttributeTable();

    private AttributeTable childTable = new AttributeTable();

    private String labelNodeName = "Node";

    private String labelNodeValue = "Value";

    private TableModelListener parentTableModelListener;

    private TableModelListener childTableModelListener;

    private JCheckBox cbShowTextNodes = new JCheckBox("Show TextNodes", true);

    private XMLTree xmlTree = null;

    private NodeSelection childNodeSelection = null;

    private final String NODE_SEL_ERROR = "Select a node first!";

    /**
     * Constructor
     * @param parentFrame
     */
    public XMLEditorPanel(IXMLEditorFrame parentFrame) {
        try {
            this.parentFrame = parentFrame;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a  component with gridBag constraints to the panel
     * @param panel
     * @param component
     * @param x
     * @param y
     * @param gridWidth
     * @param gridHeight
     */
    void addGB(JPanel panel, Component component, int x, int y, int gridWidth, int gridHeight) {
        int is = 2;
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.gridwidth = gridWidth;
        gridBagConstraints.gridheight = gridHeight;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(is, is, is, is);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        panel.add(component, gridBagConstraints);
    }

    /**
     * Add a label-component to the panel with gridbag constraints
     * @param panel
     * @param component
     * @param row
     */
    void addGBLabel(JPanel panel, Component component, int row) {
        int is = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = row;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(is, is, is, is);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        panel.add(component, gridBagConstraints);
    }

    /**
     * Add a field-component to the panel with GridBagConstraints.
     * @param panel
     * @param component
     * @param row
     */
    void addGBField(JPanel panel, Component component, int row) {
        int is = 2;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = row;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(is, is, is, is);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        panel.add(component, gridBagConstraints);
    }

    /**
     * Add a Button to the panel with GridBagConstraints
     * @param panel
     * @param component
     * @param row
     */
    void addGBButton(JPanel panel, Component component, int row) {
        int is = 2;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = row;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(is, is, is, is);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        panel.add(component, gridBagConstraints);
    }

    /**
     * Add a ButtonBar to the panel with GridBagConstraints
     * @param panel
     * @param component
     * @param row
     */
    void addGBButtonBar(JPanel panel, Component component, int row) {
        int is = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = row;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(is, is, is, is);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        panel.add(component, gridBagConstraints);
    }

    /**
     * Initialize the panel
     * @throws Exception
     */
    private void jbInit() throws Exception {
        scpTreePane.setSize(new Dimension(300, 800));
        initTree();
        XMLTree xmlTree = getXmlTree();
        scpTreePane.getViewport().add(xmlTree);
        int row = 0;
        parentPane.setLayout(gridBagLayout);
        addGBLabel(parentPane, lblNodeName, row);
        addGBField(parentPane, tfNodeName, row);
        btnAddNode.setText("Add Node");
        btnAddNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                addNode(tfNodeName.getText());
            }
        });
        addGBButton(parentPane, btnAddNode, row++);
        btnCopyNode.setText("Copy Node");
        btnCopyNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                copyNode();
            }
        });
        buttonBar.add(btnCopyNode);
        addGBLabel(parentPane, lblAttrName, row);
        addGBField(parentPane, tfAttrName, row);
        btnAddAttr.setText("Add Attribute");
        btnAddAttr.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                addAttribute(tfAttrName.getText());
            }
        });
        addGBButton(parentPane, btnAddAttr, row++);
        btnAddTextNode.setText("Add TextNode");
        btnAddTextNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                addTextNode("Node Text");
            }
        });
        buttonBar.add(btnAddTextNode);
        cbShowTextNodes.setMnemonic(KeyEvent.VK_S);
        cbShowTextNodes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                refresh();
            }
        });
        addGBField(parentPane, cbShowTextNodes, row++);
        btnDelNode.setText("Delete Node");
        btnDelNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                deleteNode();
            }
        });
        buttonBar.add(btnDelNode);
        btnMoveUp.setText("Move Up");
        btnMoveUp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                moveNodeUp();
            }
        });
        buttonBar.add(btnMoveUp);
        btnMoveDwn.setText("Move Down");
        btnMoveDwn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                moveNodeDown();
            }
        });
        buttonBar.add(btnMoveDwn);
        buttonBar.setFloatable(false);
        addGBButtonBar(parentPane, buttonBar, row++);
        initParentTable();
        addGB(parentPane, new JScrollPane(parentTable), 0, row++, 2, 1);
        childPane.setLayout(gridBagLayout);
        initChildTable();
        addGB(childPane, new JScrollPane(childTable), 0, 1, 1, 1);
        sptPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
        sptPaneRight.setDividerLocation(200);
        sptPaneRight.add(parentPane, JSplitPane.TOP);
        sptPaneRight.add(childPane, JSplitPane.BOTTOM);
        sptCenterPane.setDividerLocation(250);
        sptCenterPane.add(scpTreePane, JSplitPane.LEFT);
        sptCenterPane.add(sptPaneRight, JSplitPane.RIGHT);
        this.setLayout(gridBagLayout);
        addGB(this, sptCenterPane, 0, 0, 1, 1);
        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent componentEvent) {
            }

            public void componentMoved(ComponentEvent componentEvent) {
            }

            public void componentShown(ComponentEvent componentEvent) {
                panelShown(componentEvent);
            }

            public void componentHidden(ComponentEvent componentEvent) {
            }
        });
    }

    /**
     * Refresh the panel
     */
    public void refresh() {
        scpTreePane.getViewport().remove(xmlTree);
        initTree();
        scpTreePane.getViewport().add(xmlTree);
    }

    /**
     * Perform a refresh on the panelShown event.
     * @param componentEvent
     */
    private void panelShown(ComponentEvent componentEvent) {
        refresh();
    }

    /**
     * Change panel content for a node.
     * @param node
     */
    private void handleCurrentNode(XMLTreeNode node) {
        NamedNodeMap attributes = node.getAttributes();
        parentTable.removeAll();
        if (attributes != null && attributes.getLength() > 0) {
            int found = attributes.getLength();
            for (int i2 = 0; i2 < found; i2++) {
                Node attribute = attributes.item(i2);
                String attrText = attribute.getNodeValue();
                String attrName = attribute.getNodeName();
                parentTable.addColumn(attrName, new Object[] { attrText });
                attribute = attribute.getNextSibling();
            }
        }
    }

    /**
     * Change Panel content for childs of a node.
     * @param node
     */
    private void handeChildNodes(XMLTreeNode node) {
        NodeList nl = node.getChildNodes();
        childTable.removeAll();
        childTable.addColumn(labelNodeName);
        childTable.addColumn(labelNodeValue);
        childNodeSelection = new NodeSelection();
        if (nl != null && nl.getLength() > 0) {
            Node childNode = nl.item(0);
            int row = 0;
            while (childNode != null) {
                if (childNode.getNodeType() != Node.TEXT_NODE || includeTextNodes()) {
                    String nodeName = childNode.getNodeName();
                    String nodeValue = childNode.getNodeValue();
                    childTable.addRow();
                    row = childTable.getRowCount() - 1;
                    childTable.setAttribute(labelNodeName, row, nodeName);
                    childTable.setAttribute(labelNodeValue, row, nodeValue);
                    NamedNodeMap attributes = childNode.getAttributes();
                    if (attributes != null && attributes.getLength() > 0) {
                        int found = attributes.getLength();
                        for (int i2 = 0; i2 < found; i2++) {
                            Node attribute = attributes.item(i2);
                            String attrText = attribute.getNodeValue();
                            String attrName = attribute.getNodeName();
                            childTable.addColumn(attrName);
                            childTable.setAttribute(attrName, row, attrText);
                        }
                    }
                    childNodeSelection.addNode(childNode);
                }
                childNode = childNode.getNextSibling();
            }
        }
    }

    /**
     * Change the panel content for a node.
     * @param node
     */
    private void handleNode(XMLTreeNode node) {
        parentTable.model.removeTableModelListener(parentTableModelListener);
        childTable.model.removeTableModelListener(childTableModelListener);
        switch(node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                handeChildNodes(node);
                break;
            case Node.ELEMENT_NODE:
                handleCurrentNode(node);
                handeChildNodes(node);
                break;
            case Node.TEXT_NODE:
                handleCurrentNode(node);
                break;
            default:
                break;
        }
        parentTable.model.addTableModelListener(parentTableModelListener);
        childTable.model.addTableModelListener(childTableModelListener);
    }

    /**
     * Handler for the TreePath Selected event.
     * @param tp
     */
    private void treePathSelected(TreePath tp) {
        if (tp != null) {
            XMLTreeNode treeNode = XMLTreeNode.getXMLTreeNode(tp);
            handleNode(treeNode);
        }
    }

    /**
     * Handler for the TreePath Expanded event.
     * @param tp
     */
    private void treePathExpanded(TreePath tp) {
        setStatus("Expanded: " + tp.toString());
        pl("Expanded: " + tp.toString());
    }

    /**
     * Handler for the TreePath Collapsed event.
     * @param tp
     */
    private void treePathCollapsed(TreePath tp) {
        setStatus("Collapsed: " + tp.toString());
        pl("Collapsed: " + tp.toString());
    }

    /**
     * Handler for the TreePath Will Expand event.
     * @param tp
     */
    private void treePathWillExpand(TreePath tp) {
        setStatus("Will Expand: " + tp.toString());
        pl("Will Expand: " + tp.toString());
    }

    /**
     * Handler for the TreePath Will Collapse event.
     * @param tp
     */
    private void treePathWillCollapse(TreePath tp) {
        setStatus("Will Collapse: " + tp.toString());
        pl("Will Collapse: " + tp.toString());
    }

    /**
     * Initialize the JTree
     */
    private void initTree() {
        XMLTreeNode treeRoot = getRootXMLTreeNode(includeTextNodes());
        XMLTree xmlTree = new XMLTree(treeRoot);
        xmlTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                treePathSelected(treeSelectionEvent.getNewLeadSelectionPath());
            }
        });
        setXmlTree(xmlTree);
    }

    /**
     *Get the root  XMLTreeNode
     * @return
     */
    private XMLTreeNode getRootXMLTreeNode(boolean includeTextNodes) {
        Node treeRoot = getRootNode();
        XMLTreeNode xmlTreeNode = new XMLTreeNode(treeRoot, includeTextNodes);
        return xmlTreeNode;
    }

    /**
     *Get the root Node
     * @return
     */
    private Node getRootNode() {
        Node treeRoot = null;
        NodeList nodeList = getRootNodeList();
        if (nodeList != null) {
            treeRoot = nodeList.item(0);
        }
        return treeRoot;
    }

    /**
     * Get NodeList from XMLFile
     */
    private NodeList getRootNodeList() {
        XMLFile xmlFile = parentFrame.getXmlFile();
        NodeList nl = null;
        if (xmlFile != null && xmlFile.isParsed()) {
            try {
                nl = xmlFile.selectNodes("/");
            } catch (XPathExpressionException e) {
                showError("Xpath error: " + e.getMessage());
            }
        }
        return nl;
    }

    /**
     * Handle the Table Changed event for the parent Table.
     * @param tableModelEvent
     */
    private void parentTableChanged(TableModelEvent tableModelEvent) {
        int col = tableModelEvent.getColumn();
        int row1 = tableModelEvent.getFirstRow();
        String value = (String) parentTable.getValueAt(row1, col);
        XMLTree xmlTree = getXmlTree();
        XMLTreeNode treeNode = xmlTree.getSelectedNode();
        NodeSelected nodeEdited = treeNode.getNodeSelected();
        nodeEdited.setAttrValue(col, value);
    }

    /**
     * Handle the Table Changed event for the child table.
     * @param tableModelEvent
     */
    private void childTableChanged(TableModelEvent tableModelEvent) {
        int col = tableModelEvent.getColumn();
        int row = tableModelEvent.getFirstRow();
        String attributeName = childTable.getColumnName(col);
        String value = (String) childTable.getValueAt(row, col);
        NodeSelected nodeEdited = childNodeSelection.getNodeSelectedAt(row);
        if (col == 1) {
            nodeEdited.setNodeValue(value);
        }
        if (col > 1) {
            nodeEdited.setAttrValue(attributeName, value);
        }
    }

    /**
     * Initialize the parent table
     */
    private void initParentTable() {
        parentTableModelListener = new TableModelListener() {

            public void tableChanged(TableModelEvent tableModelEvent) {
                parentTableChanged(tableModelEvent);
            }
        };
        parentTable.model.addTableModelListener(parentTableModelListener);
    }

    /**
     * Initialize the child table
     */
    private void initChildTable() {
        childTableModelListener = new TableModelListener() {

            public void tableChanged(TableModelEvent tableModelEvent) {
                childTableChanged(tableModelEvent);
            }
        };
        childTable.model.addTableModelListener(childTableModelListener);
    }

    /**
     * Copy a node
     */
    private void copyNode() {
        XMLTree xmlTree = getXmlTree();
        if (!xmlTree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else {
            XMLTreeNode selectedNode = xmlTree.getSelectedNode();
            XMLTreeNode parentNode = selectedNode.getParent();
            if (parentNode.getNodeType() == Node.DOCUMENT_NODE) {
                showError("Please select a child node!");
            } else {
                XMLTreeModel model = xmlTree.getModel();
                model.copyNode(selectedNode);
                showMessage("Node copied!");
            }
        }
    }

    /**
     * delete a Node
     */
    private void deleteNode() {
        XMLTree tree = getXmlTree();
        if (!tree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else {
            tree.deleteSelectedNode();
            setStatus("Node Deleted!");
        }
    }

    /**
     * Add an attribute to the selected node.
     * @param attrName
     */
    private void addAttribute(String attrName) {
        XMLTree tree = getXmlTree();
        if (!tree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else if (!"".equals(attrName)) {
            try {
                XMLTreeNode selectedNode = tree.getSelectedNode();
                selectedNode.addAttribute(attrName, attrName);
            } catch (Exception e) {
                showError(e);
            }
            setStatus("Attribute added!");
        } else {
            setStatus("Fill in an attribute name!");
        }
    }

    /**
     * Add a Node to the tree
     * @param nodeName
     */
    private void addNode(String nodeName) {
        XMLTree tree = getXmlTree();
        if (!tree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else if (!"".equals(nodeName)) {
            tree.addNode(nodeName);
            handleNode(tree.getSelectedNode());
            setStatus("Node added!");
        } else {
            showError("Fill in an Node name!");
        }
    }

    /**
     * Add a Text Node to the Tree
     * @param nodeText
     */
    private void addTextNode(String nodeText) {
        XMLTree tree = getXmlTree();
        if (!tree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else if (!"".equals(nodeText)) {
            tree.addTextNode(nodeText);
            handleNode(tree.getSelectedNode());
            showMessage("Node added!");
        } else {
            showError("Fill in a node text!");
        }
    }

    /**
     * Move Tree Node one position up in the XML Tree and in the model-tree.
     */
    private void moveNodeUp() {
        XMLTree tree = getXmlTree();
        if (!tree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else {
            int pathCount = tree.getSelectedPathCount();
            if (pathCount > 1) {
                if (!tree.selectedNodeIsFirstSibling()) {
                    tree.MoveSelectedNodeUp();
                    setStatus("Node moved!");
                } else {
                    setStatus("Could not move node: first Node!");
                }
            } else {
                showError("Could not move node: top node selected!");
            }
        }
    }

    /**
     * Move Tree Node one position down in the XML Tree and in the model-tree.
     */
    private void moveNodeDown() {
        XMLTree tree = getXmlTree();
        if (!tree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else {
            int pathCount = tree.getSelectedPathCount();
            if (pathCount > 1) {
                if (!tree.selectedNodeIsLastSibling()) {
                    tree.MoveSelectedNodeDown();
                    setStatus("Node moved!");
                } else {
                    setStatus("Could not move node: last Node!");
                }
            } else {
                showError("Could not move node: top node selected!");
            }
        }
    }

    /**
     * Perform the Copy action (copying a node to cache).
     */
    public void performClipboardCopyAction() {
        XMLTree xmlTree = getXmlTree();
        if (!xmlTree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else {
            xmlTree.copyToClipBoard();
        }
    }

    /**
     * Perform the Cut action (copying a node to cache and delete it from the tree).
     */
    public void performClipboardCutAction() {
        XMLTree xmlTree = getXmlTree();
        if (!xmlTree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else {
            xmlTree.copyToClipBoard();
            deleteNode();
        }
    }

    /**
     * Perform the Paste action (copying a node to cache).
     */
    public void performClipBoardPasteAction() {
        XMLTree xmlTree = getXmlTree();
        if (!xmlTree.isTreeNodeSelected()) {
            showError(NODE_SEL_ERROR);
        } else {
            if (xmlTree.getClipboardXMLTreeNode() == null) {
                showError("No node copied yet!");
            } else {
                xmlTree.pasteFromClipBoard();
                handleNode(xmlTree.getSelectedNode());
            }
        }
    }

    /**
     *Get the content of the NodeNameField
     * @return
     */
    public String getNodeNameField() {
        return tfNodeName.getText();
    }

    /**
     * Set the Status
     * @param text
     */
    public void setStatus(String text) {
        parentFrame.setStatus(text);
    }

    /**
     * Print to System out
     * @param text
     */
    private void pl(String text) {
        System.out.println(text);
    }

    /**
     * Show an informational message
     * @param text
     */
    public void showMessage(String text) {
        setStatus(text);
    }

    /**
     * Show an error message
     * @param text
     */
    public void showError(String text) {
        parentFrame.showError(text);
    }

    /**
     * Show an error message
     * @param e
     */
    public void showError(Exception e) {
        parentFrame.showError(e.getMessage());
    }

    /**
     *Set the XMLTree
     * @param xmlTree
     */
    public void setXmlTree(XMLTree xmlTree) {
        this.xmlTree = xmlTree;
    }

    /**
     *Get the XMLTree
     * @return
     */
    public XMLTree getXmlTree() {
        return xmlTree;
    }

    private XMLFile getXmlFile() {
        XMLFile xmlFile = parentFrame.getXmlFile();
        return xmlFile;
    }

    /**
     * Check if xmlFile is loaded and parsed.
     * @return
     */
    private boolean isXmlLoadedAndParsed() {
        boolean result = false;
        XMLFile xmlFile = getXmlFile();
        if (xmlFile != null && xmlFile.isParsed()) {
            result = true;
        }
        return result;
    }

    /**
     * Check Show TextNodes checkbox
     * @return
     */
    private boolean includeTextNodes() {
        return cbShowTextNodes.isSelected();
    }
}
