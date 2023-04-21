package de.mindcrimeilab.xsanalyzer.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author:Michael Engelhardt $
 * @version $Revision:12 $
 * 
 */
public class SchemaElementsPanel extends JPanel {

    private JTree jtSchema;

    private JList jlNamespaces;

    public SchemaElementsPanel() {
        super();
        initializeGui();
    }

    public void setTreeModel(TreeModel model) {
        getJtSchema().setModel(model);
    }

    public void setNamespaceListModel(ListModel model) {
        getJlNamespaces().setModel(model);
    }

    private void initializeGui() {
        setLayout(new GridBagLayout());
        Insets defaultInsets = new Insets(2, 2, 2, 2);
        Insets sepInsets = new Insets(10, 2, 2, 2);
        final GridBagConstraints gbcLabelNamespaces = new GridBagConstraints();
        gbcLabelNamespaces.anchor = GridBagConstraints.WEST;
        gbcLabelNamespaces.fill = GridBagConstraints.HORIZONTAL;
        gbcLabelNamespaces.gridx = 0;
        gbcLabelNamespaces.gridy = 0;
        gbcLabelNamespaces.weightx = 0;
        gbcLabelNamespaces.weighty = 0;
        gbcLabelNamespaces.insets = sepInsets;
        final GridBagConstraints gbcJlNamespaces = new GridBagConstraints();
        gbcJlNamespaces.anchor = GridBagConstraints.WEST;
        gbcJlNamespaces.fill = GridBagConstraints.BOTH;
        gbcJlNamespaces.gridx = 0;
        gbcJlNamespaces.gridy = 1;
        gbcJlNamespaces.weightx = 1;
        gbcJlNamespaces.weighty = 1;
        gbcJlNamespaces.insets = defaultInsets;
        final GridBagConstraints gbcLabelOutline = new GridBagConstraints();
        gbcLabelOutline.anchor = GridBagConstraints.WEST;
        gbcLabelOutline.fill = GridBagConstraints.HORIZONTAL;
        gbcLabelOutline.gridx = 0;
        gbcLabelOutline.gridy = 2;
        gbcLabelOutline.weightx = 0;
        gbcLabelOutline.weighty = 0;
        gbcLabelOutline.insets = sepInsets;
        final GridBagConstraints gbcJtSchema = new GridBagConstraints();
        gbcJtSchema.anchor = GridBagConstraints.WEST;
        gbcJtSchema.fill = GridBagConstraints.BOTH;
        gbcJtSchema.gridx = 0;
        gbcJtSchema.gridy = 3;
        gbcJtSchema.weightx = 1;
        gbcJtSchema.weighty = 2;
        gbcJtSchema.insets = defaultInsets;
        this.add(new JLabel("Namespaces:"), gbcLabelNamespaces);
        this.add(new JScrollPane(getJtSchema(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), gbcJtSchema);
        this.add(new JLabel("Public elements:"), gbcLabelOutline);
        this.add(new JScrollPane(getJlNamespaces(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), gbcJlNamespaces);
    }

    private JTree getJtSchema() {
        if (null == jtSchema) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("No schema loaded.");
            jtSchema = new JTree(root);
            jtSchema.setCellRenderer(new SchemaElementsRenderer());
        }
        return jtSchema;
    }

    private JList getJlNamespaces() {
        if (null == jlNamespaces) {
            jlNamespaces = new JList();
            jlNamespaces.setCellRenderer(new NamespaceItemRenderer());
        }
        return jlNamespaces;
    }

    /**
	 * @param tsl
	 * @see javax.swing.JTree#addTreeSelectionListener(javax.swing.event.TreeSelectionListener)
	 */
    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        jtSchema.addTreeSelectionListener(tsl);
    }

    /**
	 * @param tsl
	 * @see javax.swing.JTree#removeTreeSelectionListener(javax.swing.event.TreeSelectionListener)
	 */
    public void removeTreeSelectionListener(TreeSelectionListener tsl) {
        jtSchema.removeTreeSelectionListener(tsl);
    }
}
