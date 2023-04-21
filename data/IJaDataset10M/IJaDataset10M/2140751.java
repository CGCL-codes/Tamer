package com.bbn.vessel.author.header;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;
import org.apache.log4j.Logger;
import com.bbn.vessel.author.models.Category;
import com.bbn.vessel.author.models.NodeSpec;
import com.bbn.vessel.author.models.NodeSpecTable;

/**
 * <Enter the description of this type here>
 * 
 * @author RTomlinson
 */
public class NodeSpecEditor extends DetailEditor {

    private static Logger logger = Logger.getLogger(NodeSpecEditor.class);

    private final JTextField nodeTypeField = new JTextField(25);

    private final JTextField classNameField = new JTextField(25);

    private final DefaultComboBoxModel model = new DefaultComboBoxModel();

    private final JComboBox categoryBox = new JComboBox(model);

    private NodeSpec nodeSpec;

    private NodeSpecNode nodeSpecNode;

    private boolean modified;

    /**
     * @param panel
     * @param nodeTree
     * @param nodeSpecTable
     */
    public NodeSpecEditor(JPanel panel, JTree nodeTree, NodeSpecTable nodeSpecTable) {
        super(panel, nodeTree, nodeSpecTable);
        categoryBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        saveChanges();
                    }
                });
            }
        });
    }

    /**
     * @see com.bbn.vessel.author.header.DetailEditor#setTreeNode(javax.swing.tree.TreeNode)
     */
    @Override
    public void setTreeNode(TreeNode treeNode) {
        nodeSpecNode = (NodeSpecNode) treeNode;
        nodeSpec = nodeSpecNode.getNodeSpec();
        resetPanel();
        addTitle("Type and Implementation Class");
        nodeTypeField.setText(nodeSpec.getType());
        addLabelledComponent(nodeTypeField, "Node Type: ");
        classNameField.setText(nodeSpec.getImplementationClass());
        addLabelledComponent(classNameField, "Implementation Class: ");
        updateCategoryBoxModel();
        categoryBox.setSelectedItem(nodeSpecNode.getParent());
        addLabelledComponent(categoryBox, "Category");
        List<TreeNode> refNodes = new ArrayList<TreeNode>(3);
        refNodes.add(nodeSpecNode.getArgumentsNode());
        refNodes.add(nodeSpecNode.getInputTerminalsNode());
        refNodes.add(nodeSpecNode.getOutputTerminalsNode());
        addRefList(refNodes, true);
        descriptionTextArea.setText(nodeSpec.getDescription());
        addDescription();
        modified = false;
    }

    /**
   *
   */
    private void updateCategoryBoxModel() {
        model.removeAllElements();
        MyRootNode rootNode = (MyRootNode) treeModel.getRoot();
        for (Enumeration<?> e = rootNode.children(); e.hasMoreElements(); ) {
            model.addElement(e.nextElement());
        }
    }

    /**
     * @see com.bbn.vessel.author.header.DetailEditor#saveChanges()
     */
    @Override
    public boolean saveChanges() {
        String type = nodeTypeField.getText();
        if (!nodeSpec.getType().equals(type)) {
            modified = true;
            nodeSpec.setType(type);
        }
        String className = classNameField.getText();
        if (!nodeSpec.getImplementationClass().equals(className)) {
            modified = true;
            nodeSpec.setImplementationClass(className);
        }
        String description = descriptionTextArea.getText();
        if (!nodeSpec.getDescription().equals(description)) {
            modified = true;
            nodeSpec.setDescription(description);
        }
        CategoryNode newCategoryNode = (CategoryNode) categoryBox.getSelectedItem();
        Category newCategory = newCategoryNode.getCategory();
        Category currentCategory = nodeSpec.getCategory();
        if (newCategory != null && newCategory != currentCategory) {
            CategoryNode oldCategoryNode = nodeSpecNode.getParent();
            currentCategory.removeNodeSpec(nodeSpec);
            newCategory.addNodeSpec(nodeSpec);
            nodeSpec.setCategory(newCategory);
            int[] removedIndexes = { oldCategoryNode.removeNodeSpecNode(nodeSpecNode) };
            TreeNode[] removedChildren = { nodeSpecNode };
            treeModel.nodesWereRemoved(oldCategoryNode, removedIndexes, removedChildren);
            NodeSpecNode newNodeSpecNode = new NodeSpecNode(newCategoryNode, nodeSpec);
            int[] insertedIndexes = { newCategoryNode.addNodeSpecNode(newNodeSpecNode) };
            treeModel.nodesWereInserted(newCategoryNode, insertedIndexes);
            modified = true;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Finished saveChanges");
        }
        return modified;
    }
}
