package org.formaria.editor.builder;

import org.formaria.swing.TreeTable;
import org.formaria.swing.table.CheckBoxTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;
import org.formaria.editor.project.EditorProject;
import org.formaria.aria.validation.Validator;

/**
 * A page generator wizard for creating form elements within the editor
 * <p> Copyright (c) Formaria Ltd., 2002-2007</p>
 * <p> $Revision: 1.77 $</p>
 * <p> License: see License.txt</p>
 */
public class FormGeneratorWizard extends JDialog implements ActionListener {

    private static String[] componentItems = { "<none>", "Label", "Edit", "Combo", "Audio", "Calendar", "CheckBox", "CheckedCombo", "CheckList", "Date", "Image", "ImageMap", "List", "Money", "Table", "TextArea", "Tree", "Password", "ProgressBar", "RadioButton", "Shape", "Spinner", "ReflectedImage", "Video" };

    private static String[] validationItems = { "<none>", "MinMax", "Date", "Name", "Password", "RegEx", "Set" };

    private FormGeneratorInfo formGeneratorInfo;

    private JButton nextBtn, prevBtn, upBtn, downBtn;

    private Object lastBtn;

    private JScrollPane jScrollPane1;

    private TreeTable table;

    private JPanel fieldPanel, layoutPanel;

    private CardLayout cardManager;

    private JSpinner colWidthSpinner;

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel jLabel6;

    private JLabel jLabel7, jLabel8;

    private JLabel alphaWarning;

    private JSpinner numColsSpinner;

    private JSpinner paddingSpinner;

    private JSpinner panelPaddingSpinner;

    private JTextField panelTitle;

    private JSpinner rowHeightSpinner;

    private JSpinner suffixWidthSpinner;

    private JSpinner panelArcSpinner;

    private JCheckBox panelOpaque;

    private int cardIdx;

    private JPanel cardPanel, buttonPanel;

    private EditorProject currentProject;

    /** 
   * Creates a new instance of FormGeneratorWizard
   */
    public FormGeneratorWizard(EditorProject project, FormGeneratorInfo model) {
        super((JFrame) null, true);
        currentProject = project;
        formGeneratorInfo = model;
        initComponents();
        setTitle("Form Generation");
        cardIdx = 0;
    }

    public boolean showDialog() {
        setupProperties();
        setVisible(true);
        return (lastBtn == nextBtn);
    }

    /**
   * Expand the table's nodes stored in the specified TreeTableNode object.   
   * @param rootNode the root of the tree containing nodes to be expanded.
   */
    private void expandNodes(TreeTableNode rootNode) {
        for (TreeTableNode childNode : rootNode.children) expandNodes(childNode, 0);
    }

    /**
   * Expands the table's nodes stored in the specified subtree.
   * @param node the root of a subtree containing nodes to be expanded.
   * @param level distance between the root node and the specified node.
   */
    private void expandNodes(TreeTableNode node, int level) {
        if (node.isExpanded()) {
            table.setSelection(node.rowNr + level);
            for (TreeTableNode childNode : node.children) expandNodes(childNode, node.rowNr + level);
        }
    }

    /**
   * Stores the information about the currently expanded nodes
   * in the given ExpandedNode object   
   */
    private void storeExpandedNodes(TreeTableNode rootNode) {
        storeExpandedNodes(rootNode, 0, formGeneratorInfo);
    }

    private int storeExpandedNodes(TreeTableNode currentNode, int tableIndex, FormGeneratorInfo formInfo) {
        while (sameLevel(tableIndex, formInfo)) {
            FormGeneratorInfo childInfo = getChildFormInfo(tableIndex, formInfo);
            TreeTableNode childNode = currentNode.addChild(tableIndex);
            if (sameLevel(tableIndex + 1, childInfo)) tableIndex = storeExpandedNodes(childNode, tableIndex + 1, childInfo); else tableIndex++;
        }
        return tableIndex;
    }

    /**
   * Gets the field's name at the specified table's node.
   * @param idx the number of the table's row.
   * @return the name of the field.
   */
    private String getTableFieldName(int idx) {
        return (String) table.getValue(idx, 0);
    }

    /**
   * Indicates whether the specified table row is at the same level
   * as the given FormGeneratorInfo object.
   */
    private boolean sameLevel(int tableIndex, FormGeneratorInfo formInfo) {
        if (formInfo == null) return false;
        if (tableIndex >= table.getRowCount()) return false;
        String tableFieldName = (String) table.getValue(tableIndex, 0);
        return (formInfo.getProperty(tableFieldName, "name") != null);
    }

    private FormGeneratorInfo getChildFormInfo(int rowIdx, FormGeneratorInfo formInfo) {
        String tableFieldName = (String) table.getValue(rowIdx, 0);
        return (FormGeneratorInfo) formInfo.getProperty(tableFieldName, "childPanel");
    }

    /**
   * Moves the specified field in the tree table one position up or down.
   * @param selIdx the position of the field in the tree table.
   * @param moveUp true - move up, false - move down.
   */
    private void moveField(int selIdx, boolean moveUp) {
        final TreeTableNode rootNode = TreeTableNode.createRoot();
        storeExpandedNodes(rootNode);
        String fieldName = (String) table.getValue(selIdx, 0);
        if (formGeneratorInfo.moveField(fieldName, moveUp)) {
            final int newIdx = rootNode.moveField(selIdx, moveUp);
            table.tableChanged(new TableModelEvent(formGeneratorInfo));
            table.reload();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    expandNodes(rootNode);
                    JTree tree = table.getTree();
                    TreePath selectedPath = tree.getPathForRow(newIdx);
                    tree.scrollPathToVisible(selectedPath);
                    Rectangle rect = table.getCellRect(newIdx, 0, true);
                    Container p = table.getParent();
                    if (p instanceof JViewport) ((JViewport) p).setViewPosition(new Point(rect.x, Math.max(0, rect.y - ((JScrollPane) p.getParent()).getHeight() / 2)));
                    table.setRowSelectionInterval(newIdx, newIdx);
                }
            });
        }
    }

    public void actionPerformed(ActionEvent ae) {
        lastBtn = ae.getSource();
        int selIdx = table.getSelectedRow();
        if (lastBtn == upBtn) {
            moveField(selIdx, true);
            return;
        } else if (lastBtn == downBtn) {
            moveField(selIdx, false);
            return;
        }
        if (lastBtn == nextBtn) {
            if (cardIdx == 1) {
                setVisible(false);
                saveProperties();
            } else {
                prevBtn.setText("Prev");
                nextBtn.setText("Finish");
                prevBtn.setIcon(new ImageIcon(getClass().getResource("resources/go-previous.png")));
                nextBtn.setIcon(new ImageIcon(getClass().getResource("resources/go-jump.png")));
                cardManager.next(cardPanel);
                cardIdx++;
                lastBtn = null;
            }
        } else {
            if (cardIdx == 0) setVisible(false); else {
                prevBtn.setText("Cancel");
                nextBtn.setText("Next");
                prevBtn.setIcon(new ImageIcon(getClass().getResource("resources/user-trash-full.png")));
                nextBtn.setIcon(new ImageIcon(getClass().getResource("resources/go-next.png")));
                cardManager.previous(cardPanel);
            }
            cardIdx--;
        }
    }

    private void saveProperties() {
        formGeneratorInfo.setGenerationParameter("rowHeight", rowHeightSpinner.getValue().toString());
        formGeneratorInfo.setGenerationParameter("colWidth", colWidthSpinner.getValue().toString());
        formGeneratorInfo.setGenerationParameter("padding", paddingSpinner.getValue().toString());
        formGeneratorInfo.setGenerationParameter("panelPadding", panelPaddingSpinner.getValue().toString());
        formGeneratorInfo.setGenerationParameter("numColumns", numColsSpinner.getValue().toString());
        formGeneratorInfo.setGenerationParameter("suffixWidth", suffixWidthSpinner.getValue().toString());
        formGeneratorInfo.setGenerationParameter("panelArc", panelArcSpinner.getValue().toString());
        formGeneratorInfo.setGenerationParameter("panelOpaque", panelOpaque.isSelected() ? "true" : "false");
    }

    private void setupProperties() {
        panelTitle.setText(formGeneratorInfo.getGenerationParameter("title"));
        rowHeightSpinner.setModel(new SpinnerNumberModel(FormGenerator.ROW_HEIGHT, 5, 50, 1));
        colWidthSpinner.setModel(new SpinnerNumberModel(FormGenerator.COL_WIDTH, 30, 2000, 2));
        paddingSpinner.setModel(new SpinnerNumberModel(FormGenerator.PADDING, 0, 50, 1));
        panelPaddingSpinner.setModel(new SpinnerNumberModel(FormGenerator.PANEL_PADDING, 0, 50, 1));
        numColsSpinner.setModel(new SpinnerNumberModel(1, 1, 50, 1));
        suffixWidthSpinner.setModel(new SpinnerNumberModel(0, 0, 50, 1));
        rowHeightSpinner.setValue(Integer.parseInt(formGeneratorInfo.getGenerationParameter("rowHeight")));
        colWidthSpinner.setValue(Integer.parseInt(formGeneratorInfo.getGenerationParameter("colWidth")));
        paddingSpinner.setValue(Integer.parseInt(formGeneratorInfo.getGenerationParameter("padding")));
        panelPaddingSpinner.setValue(Integer.parseInt(formGeneratorInfo.getGenerationParameter("panelPadding")));
        numColsSpinner.setValue(Integer.parseInt(formGeneratorInfo.getGenerationParameter("numColumns")));
        suffixWidthSpinner.setValue(Integer.parseInt(formGeneratorInfo.getGenerationParameter("suffixWidth")));
        panelArcSpinner.setValue(Integer.parseInt(formGeneratorInfo.getGenerationParameter("panelArc")));
        panelOpaque.setSelected("true".equals("panelOpaque"));
    }

    /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    private void initComponents() {
        setSize(864, 425);
        cardPanel = new JPanel();
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BorderLayout(2, 2));
        fieldPanel.setSize(750, 379);
        layoutPanel = new JPanel();
        layoutPanel.setLayout(null);
        layoutPanel.setSize(750, 379);
        cardManager = new CardLayout(10, 10);
        JPanel contentPanel = (JPanel) getContentPane();
        cardPanel.setLayout(cardManager);
        cardPanel.add(fieldPanel, "fields");
        cardPanel.add(layoutPanel, "layout");
        cardManager.show(cardPanel, "fields");
        jScrollPane1 = new JScrollPane();
        fieldPanel.add(jScrollPane1, BorderLayout.CENTER);
        nextBtn = new JButton();
        prevBtn = new JButton();
        upBtn = new JButton();
        downBtn = new JButton();
        upBtn.setIcon(new ImageIcon(getClass().getResource("resources/go-up.png")));
        downBtn.setIcon(new ImageIcon(getClass().getResource("resources/go-down.png")));
        contentPanel.add(cardPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(prevBtn);
        buttonPanel.add(nextBtn);
        prevBtn.setBounds(340, 390, 100, 30);
        nextBtn.setBounds(450, 390, 100, 30);
        JPanel navBtnPane1 = new JPanel();
        fieldPanel.add(navBtnPane1, BorderLayout.EAST);
        navBtnPane1.setLayout(new BoxLayout(navBtnPane1, BoxLayout.Y_AXIS));
        navBtnPane1.add(upBtn);
        navBtnPane1.add(downBtn);
        upBtn.setEnabled(false);
        downBtn.setEnabled(false);
        nextBtn.addActionListener(this);
        prevBtn.addActionListener(this);
        upBtn.addActionListener(this);
        downBtn.addActionListener(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        table = new TreeTable();
        CheckBoxTableCellRenderer booleanRenderer = new CheckBoxTableCellRenderer();
        CheckBoxTableCellRenderer booleanEditor = new CheckBoxTableCellRenderer();
        FormComboTableCellRenderer compRenderer = new FormComboTableCellRenderer(componentItems);
        String listItems[] = currentProject.getValidations();
        int len = listItems.length;
        String[] validations = new String[1 + len];
        System.arraycopy(listItems, 0, validations, 1, len);
        validations[0] = "[None]";
        FormComboTableCellRenderer validationRenderer = new FormComboTableCellRenderer(validations);
        table.setDefaultRenderer(Boolean.class, booleanRenderer);
        table.setDefaultEditor(Boolean.class, booleanEditor);
        table.setDefaultEditor(JComponent.class, compRenderer);
        table.setDefaultRenderer(Validator.class, new DefaultTableCellRenderer());
        table.setDefaultEditor(Validator.class, validationRenderer);
        List<String> propertyList = formGeneratorInfo.getPropertyList();
        Map<String, Object> fieldProperties = new HashMap();
        fieldProperties.put("childPanel", formGeneratorInfo);
        table.setDataModel(new FormInfoTreeTableAdapter(propertyList, fieldProperties), formGeneratorInfo);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(table);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int selIdx = table.getSelectedRow();
                int maxIdx = table.getRowCount() - 1;
                if (selIdx < 0) {
                    upBtn.setEnabled(false);
                    downBtn.setEnabled(false);
                } else {
                    upBtn.setEnabled(selIdx > 0);
                    downBtn.setEnabled(selIdx < maxIdx);
                }
            }
        });
        nextBtn.setIcon(new ImageIcon(getClass().getResource("resources/go-next.png")));
        nextBtn.setText("Next");
        prevBtn.setIcon(new ImageIcon(getClass().getResource("resources/user-trash-full.png")));
        prevBtn.setText("Cancel");
        panelTitle = new JTextField();
        jLabel1 = new JLabel("Title");
        layoutPanel.add(panelTitle);
        jLabel2 = new JLabel("Row Height");
        rowHeightSpinner = new JSpinner();
        jLabel3 = new JLabel("Column Width");
        colWidthSpinner = new JSpinner();
        jLabel4 = new JLabel("Padding");
        paddingSpinner = new JSpinner();
        jLabel5 = new JLabel("Number of Columns");
        numColsSpinner = new JSpinner();
        suffixWidthSpinner = new JSpinner();
        jLabel6 = new JLabel("Suffix Width");
        jLabel7 = new JLabel("Panel Padding");
        panelPaddingSpinner = new JSpinner();
        panelArcSpinner = new JSpinner();
        jLabel8 = new JLabel("Panel Rounding");
        panelOpaque = new JCheckBox("Panel Opaque");
        layoutPanel.add(panelTitle);
        layoutPanel.add(rowHeightSpinner);
        layoutPanel.add(colWidthSpinner);
        layoutPanel.add(paddingSpinner);
        layoutPanel.add(numColsSpinner);
        layoutPanel.add(suffixWidthSpinner);
        layoutPanel.add(panelPaddingSpinner);
        layoutPanel.add(panelArcSpinner);
        layoutPanel.add(panelOpaque);
        layoutPanel.add(jLabel1);
        layoutPanel.add(jLabel2);
        layoutPanel.add(jLabel3);
        layoutPanel.add(jLabel4);
        layoutPanel.add(jLabel5);
        layoutPanel.add(jLabel6);
        layoutPanel.add(jLabel7);
        layoutPanel.add(jLabel8);
        alphaWarning = new JLabel("Warning: This is an early access feature - use at your discretion");
        alphaWarning.setHorizontalAlignment(SwingConstants.RIGHT);
        alphaWarning.setForeground(Color.gray);
        alphaWarning.setBounds(400, 300, 400, 20);
        layoutPanel.add(alphaWarning);
        panelTitle.setBounds(120, 10, 100, 20);
        rowHeightSpinner.setBounds(120, 34, 100, 20);
        colWidthSpinner.setBounds(120, 58, 100, 20);
        paddingSpinner.setBounds(120, 82, 100, 20);
        numColsSpinner.setBounds(120, 106, 100, 20);
        suffixWidthSpinner.setBounds(120, 130, 100, 20);
        panelPaddingSpinner.setBounds(120, 154, 100, 20);
        panelArcSpinner.setBounds(120, 178, 100, 20);
        panelOpaque.setBounds(14, 202, 124, 20);
        panelOpaque.setHorizontalTextPosition(AbstractButton.LEADING);
        panelOpaque.setHorizontalAlignment(AbstractButton.RIGHT);
        jLabel1.setBounds(14, 10, 100, 20);
        jLabel2.setBounds(14, 34, 100, 20);
        jLabel3.setBounds(14, 58, 100, 20);
        jLabel4.setBounds(14, 82, 100, 20);
        jLabel5.setBounds(14, 106, 100, 20);
        jLabel6.setBounds(14, 130, 100, 20);
        jLabel7.setBounds(14, 154, 100, 20);
        jLabel8.setBounds(14, 178, 100, 20);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        layoutPanel.setBorder(BorderFactory.createEtchedBorder());
        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
    }
}

/** Stores informations about expanded TreeTable nodes */
class TreeTableNode {

    int rowNr;

    TreeTableNode parent;

    List<TreeTableNode> children;

    /**
     * Creates a new root.
     * @return the new root
     */
    static TreeTableNode createRoot() {
        return new TreeTableNode(0, null);
    }

    /**
     * Creates a new instance of TreeTableNode
     * @param aRowNr associated row in the tree table
     * @param aParent parent node
     */
    private TreeTableNode(int aRowNr, TreeTableNode aParent) {
        rowNr = aRowNr;
        parent = aParent;
        children = new ArrayList();
    }

    /**
     * Adds a new child to this node
     * @param aRowNr associated row number in the table
     * @return new child node
     */
    TreeTableNode addChild(int aRowNr) {
        int relRowNr = aRowNr - getAbsDeep();
        TreeTableNode childNode = new TreeTableNode(relRowNr, this);
        children.add(childNode);
        return childNode;
    }

    /**
     * Indicates whether this treeTable node is expanded.
     * @return true if expanded, false otherwise
     */
    public boolean isExpanded() {
        return (children.size() > 0);
    }

    /**
     * Moves the specified row up/down
     * @param aRowNr the number of the row to be moved
     * @param moveUp true - move up, false - move down
     * @return the new position in the table
     */
    int moveField(int aRowNr, boolean moveUp) {
        int numChildren = children.size();
        for (int i = 0; i < numChildren; i++) {
            TreeTableNode currentChild = children.get(i);
            if (currentChild.rowNr == aRowNr) return switchChildren(i, i + (moveUp ? -1 : 1));
            TreeTableNode nextChild = (i + 1 < numChildren ? children.get(i + 1) : null);
            if ((nextChild == null) || (currentChild.rowNr < aRowNr) && (nextChild.rowNr > aRowNr)) return (currentChild.rowNr + currentChild.moveField(aRowNr - currentChild.rowNr, moveUp));
        }
        return aRowNr;
    }

    /**
     * Swithes the child from the postion idx1 with the one
     * from the position idx2
     * @return the new row nr of the child that previously was
     * located at the idx1 position.
     */
    private int switchChildren(int idx1, int idx2) {
        if ((idx1 < 0) || (idx1 >= children.size())) return children.get(idx1).rowNr;
        if ((idx2 < 0) || (idx2 >= children.size())) return children.get(idx1).rowNr;
        TreeTableNode child1 = children.get(idx1);
        TreeTableNode child2 = children.get(idx2);
        children.set(idx1, child2);
        children.set(idx2, child1);
        if (idx1 < idx2) {
            child2.rowNr = child1.rowNr;
            child1.rowNr += child2.countRows();
        } else {
            child1.rowNr = child2.rowNr;
            child2.rowNr += child1.countRows();
        }
        return child1.rowNr;
    }

    /**
     * Gets the total number of rows rooted at this node
     * @return total number of rows
     */
    private int countRows() {
        int rowCount = 1;
        for (TreeTableNode child : children) rowCount += child.countRows();
        return rowCount;
    }

    /**
     * Gets the distance between root node and this node
     * @return the distance
     */
    private int getAbsDeep() {
        int dist = rowNr;
        TreeTableNode node = this;
        while ((node = node.parent) != null) dist += node.rowNr;
        return dist;
    }
}
