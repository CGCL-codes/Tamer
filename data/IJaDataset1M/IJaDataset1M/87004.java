package org.rapla.gui.internal.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.rapla.components.calendar.RaplaArrowButton;
import org.rapla.components.layout.TableLayout;
import org.rapla.components.util.Tools;
import org.rapla.entities.Category;
import org.rapla.entities.CategoryAnnotations;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.gui.EditComponent;
import org.rapla.gui.RaplaGUIComponent;
import org.rapla.gui.TreeFactory;
import org.rapla.gui.toolkit.RaplaButton;
import org.rapla.gui.toolkit.RaplaColorList;
import org.rapla.gui.toolkit.RaplaTree;
import org.rapla.gui.toolkit.RaplaWidget;
import org.rapla.gui.toolkit.RecursiveNode;

/**
 *  @author Christopher Kohlhaas
 */
public class CategoryEditUI extends RaplaGUIComponent implements EditComponent, RaplaWidget {

    JPanel panel = new JPanel();

    JPanel toolbar = new JPanel();

    RaplaButton newButton = new RaplaButton();

    RaplaButton newSubButton = new RaplaButton();

    RaplaButton removeButton = new RaplaButton();

    RaplaArrowButton moveUpButton = new RaplaArrowButton('^', 25);

    RaplaArrowButton moveDownButton = new RaplaArrowButton('v', 25);

    JCheckBox editAdvanced = new JCheckBox();

    CategoryNode rootNode;

    Category rootCategory;

    CategoryDetail detailPanel;

    RaplaTreeEdit treeEdit;

    DefaultTreeModel model;

    boolean editKeys;

    Listener listener = new Listener();

    TreeCellRenderer iconRenderer;

    public CategoryEditUI(RaplaContext sm) throws RaplaException {
        super(sm);
        detailPanel = new CategoryDetail(sm);
        panel.setPreferredSize(new Dimension(690, 350));
        treeEdit = new RaplaTreeEdit(getI18n(), detailPanel.getComponent(), listener);
        treeEdit.setListDimension(new Dimension(250, 100));
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(newButton);
        toolbar.add(newSubButton);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(removeButton);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(moveUpButton);
        toolbar.add(moveDownButton);
        toolbar.add(editAdvanced);
        panel.setLayout(new BorderLayout());
        toolbar.add(editAdvanced);
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(treeEdit.getComponent(), BorderLayout.CENTER);
        editAdvanced.setOpaque(false);
        newButton.addActionListener(listener);
        newSubButton.addActionListener(listener);
        removeButton.addActionListener(listener);
        editAdvanced.addActionListener(listener);
        moveUpButton.addActionListener(listener);
        moveDownButton.addActionListener(listener);
        iconRenderer = getTreeFactory().createRenderer();
        treeEdit.getTree().setCellRenderer(new TreeCellRenderer() {

            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (value instanceof CategoryNode) {
                    Category c = (Category) ((CategoryNode) value).getCategory();
                    value = c.getName(getRaplaLocale().getLocale());
                    if (editKeys) {
                        value = "{" + c.getKey() + "} " + value;
                    }
                }
                return iconRenderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }
        });
        newButton.setText(getString("new_category"));
        newButton.setIcon(getIcon("icon.new"));
        newSubButton.setText(getString("new_sub-category"));
        newSubButton.setIcon(getIcon("icon.new"));
        removeButton.setText(getString("delete"));
        editAdvanced.setText(getString("edit_advanced"));
        removeButton.setIcon(getIcon("icon.delete"));
        detailPanel.addChangeListener(listener);
        detailPanel.setEditKeys(false);
        editAdvanced.setText(getString("edit_advanced"));
    }

    private final TreeFactory getTreeFactory() {
        return getService(TreeFactory.class);
    }

    class Listener implements ActionListener, ChangeListener {

        public void actionPerformed(ActionEvent evt) {
            try {
                if (evt.getSource() == newButton) {
                    createCategory(false);
                } else if (evt.getSource() == newSubButton) {
                    createCategory(true);
                } else if (evt.getSource() == removeButton) {
                    removeCategory();
                } else if (evt.getSource() == moveUpButton) {
                    moveCategory(-1);
                } else if (evt.getSource() == moveDownButton) {
                    moveCategory(1);
                } else if (evt.getSource() == editAdvanced) {
                    editKeys = editAdvanced.isSelected();
                    detailPanel.setEditKeys(editKeys);
                    updateModel();
                } else if (evt.getActionCommand().equals("edit")) {
                    detailPanel.mapFrom(treeEdit.getSelectedValue());
                }
            } catch (RaplaException ex) {
                showException(ex, getComponent());
            }
        }

        public void stateChanged(ChangeEvent e) {
            try {
                confirmEdits();
            } catch (RaplaException ex) {
                showException(ex, getComponent());
            }
        }
    }

    public JComponent getComponent() {
        return panel;
    }

    public int getSelectedIndex() {
        return treeEdit.getSelectedIndex();
    }

    public void setObject(Object o) throws RaplaException {
        if (!(o instanceof Category)) throw new RaplaException("Only category objects are accepted: " + o.getClass());
        this.rootCategory = (Category) o;
        updateModel();
    }

    private void createCategory(boolean bCreateSubCategory) throws RaplaException {
        confirmEdits();
        Category newCategory;
        RecursiveNode parentNode;
        TreePath path = treeEdit.getTree().getSelectionPath();
        if (path == null) {
            parentNode = rootNode;
        } else {
            RecursiveNode selectedNode = (RecursiveNode) path.getLastPathComponent();
            if (selectedNode.getParent() == null || bCreateSubCategory) parentNode = selectedNode; else parentNode = (RecursiveNode) selectedNode.getParent();
        }
        newCategory = createNewNodeAt(parentNode);
        updateModel();
        RecursiveNode newNode = rootNode.findNodeFor(newCategory);
        TreePath selectionPath = new TreePath(newNode.getPath());
        treeEdit.getTree().setSelectionPath(selectionPath);
        detailPanel.requestFocus();
    }

    private String createNewKey(Category[] subCategories) {
        int max = 1;
        for (int i = 0; i < subCategories.length; i++) {
            String key = subCategories[i].getKey();
            if (key.length() > 1 && key.charAt(0) == 'c' && Character.isDigit(key.charAt(1))) {
                try {
                    int value = Integer.valueOf(key.substring(1)).intValue();
                    if (value >= max) max = value + 1;
                } catch (NumberFormatException ex) {
                }
            }
        }
        return "c" + (max);
    }

    private Category createNewNodeAt(RecursiveNode parentNode) throws RaplaException {
        Category newCategory = getModification().newCategory();
        Category parent = ((CategoryNode) parentNode).getCategory();
        newCategory.setKey(createNewKey(parent.getCategories()));
        newCategory.getName().setName(getI18n().getLang(), getString("new_category"));
        parent.addCategory(newCategory);
        getLogger().debug(" new category " + newCategory + " added to " + parent);
        int index = parentNode.getIndexOfUserObject(newCategory);
        if (index < 0) throw new RaplaException("Can't insert new Category");
        return newCategory;
    }

    private void removeCategory() throws RaplaException {
        TreePath[] paths = treeEdit.getTree().getSelectionPaths();
        if (paths == null) return;
        CategoryNode[] categoryNodes = new CategoryNode[paths.length];
        for (int i = 0; i < paths.length; i++) {
            categoryNodes[i] = (CategoryNode) paths[i].getLastPathComponent();
        }
        removeNodes(categoryNodes);
        updateModel();
    }

    private void moveCategory(int direction) throws RaplaException {
        TreePath[] paths = treeEdit.getTree().getSelectionPaths();
        if (paths == null || paths.length == 0) return;
        CategoryNode categoryNode = (CategoryNode) paths[0].getLastPathComponent();
        if (categoryNode == null) {
            return;
        }
        Category selectedCategory = categoryNode.getCategory();
        Category parent = selectedCategory.getParent();
        if (parent == null || selectedCategory.equals(rootCategory)) return;
        Category[] childs = parent.getCategories();
        for (int i = 0; i < childs.length; i++) {
            parent.removeCategory(childs[i]);
        }
        if (direction == -1) {
            Category last = null;
            for (int i = 0; i < childs.length; i++) {
                Category current = childs[i];
                if (current.equals(selectedCategory)) {
                    parent.addCategory(current);
                }
                if (last != null && !last.equals(selectedCategory)) {
                    parent.addCategory(last);
                }
                last = current;
            }
            if (last != null) {
                parent.addCategory(last);
            }
        } else {
            boolean insertNow = false;
            for (int i = 0; i < childs.length; i++) {
                Category current = childs[i];
                if (!current.equals(selectedCategory)) {
                    parent.addCategory(current);
                } else {
                    insertNow = true;
                    continue;
                }
                if (insertNow) {
                    insertNow = false;
                    parent.addCategory(selectedCategory);
                }
            }
            if (insertNow) {
                parent.addCategory(selectedCategory);
            }
        }
        updateModel();
    }

    public void removeNodes(CategoryNode[] nodes) {
        ArrayList childList = new ArrayList();
        TreeNode[] path = null;
        CategoryNode parentNode = null;
        for (int i = 0; i < nodes.length; i++) {
            if (parentNode == null) {
                path = nodes[i].getPath();
                parentNode = (CategoryNode) nodes[i].getParent();
            }
            if (parentNode == null) continue;
            int index = parentNode.getIndexOfUserObject(nodes[i].getUserObject());
            if (index >= 0) {
                childList.add(nodes[i]);
                if (getLogger().isDebugEnabled()) getLogger().debug("Removing CategoryNode " + nodes[i].getCategory());
            }
        }
        if (path != null) {
            int size = childList.size();
            Object[] childs = new Object[size];
            for (int i = 0; i < size; i++) {
                childs[i] = childList.get(i);
            }
            for (int i = 0; i < size; i++) {
                Category subCategory = ((CategoryNode) childs[i]).getCategory();
                subCategory.getParent().removeCategory(subCategory);
                getLogger().debug("category removed " + subCategory);
            }
        }
    }

    public void mapTo(Object object) throws RaplaException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void mapToObject() throws RaplaException {
        validate(this.rootCategory);
        confirmEdits();
    }

    public Object getObject() {
        return this.rootCategory;
    }

    private void updateModel() throws RaplaException {
        this.rootNode = new CategoryNode(null, rootCategory);
        model = new DefaultTreeModel(this.rootNode);
        RaplaTree.exchangeTreeModel(model, treeEdit.getTree());
    }

    public void confirmEdits() throws RaplaException {
        if (getSelectedIndex() < 0) return;
        Object category = treeEdit.getSelectedValue();
        detailPanel.mapTo(category);
        TreePath path = treeEdit.getTree().getSelectionPath();
        if (path != null) model.nodeChanged((TreeNode) path.getLastPathComponent());
    }

    private void validate(Category category) throws RaplaException {
        checkKey(category.getKey());
        Category[] categories = category.getCategories();
        for (int i = 0; i < categories.length; i++) {
            validate(categories[i]);
        }
    }

    private void checkKey(String key) throws RaplaException {
        if (key.length() == 0) throw new RaplaException(getString("error.no_key"));
        if (!Tools.isKey(key) || key.length() > 50) {
            Object[] param = new Object[3];
            param[0] = key;
            param[1] = "'-', '_'";
            param[2] = "'_'";
            throw new RaplaException(getI18n().format("error.invalid_key", param));
        }
    }

    public void setEditKeys(boolean editKeys) {
        detailPanel.setEditKeys(editKeys);
        this.editKeys = editKeys;
    }

    class CategoryNode extends RecursiveNode {

        public CategoryNode(TreeNode parent, Category category) {
            super(parent, category);
        }

        protected Category getCategory() {
            return (Category) getUserObject();
        }

        protected Object[] getChildObjects() {
            return getCategory().getCategories();
        }

        protected RecursiveNode createChildNode(Object userObject) {
            return new CategoryNode(this, (Category) userObject);
        }

        public String toString() {
            return getName(getCategory());
        }
    }
}

class CategoryDetail extends AbstractEditField implements ChangeListener {

    JPanel panel = new JPanel();

    JLabel nameLabel = new JLabel();

    JLabel keyLabel = new JLabel();

    JLabel colorLabel = new JLabel();

    MultiLanguageField name;

    TextField key;

    TextField colorTextField;

    JPanel colorPanel = new JPanel();

    public CategoryDetail(RaplaContext sm) throws RaplaException {
        super(sm);
        name = new MultiLanguageField(sm, "name");
        key = new TextField(sm, "key");
        colorTextField = new TextField(sm, "color");
        double fill = TableLayout.FILL;
        double pre = TableLayout.PREFERRED;
        panel.setLayout(new TableLayout(new double[][] { { 5, pre, 5, fill }, { 5, pre, 5, pre, 5, pre, 5 } }));
        panel.add("1,1,l,f", nameLabel);
        panel.add("3,1,f,f", name.getComponent());
        panel.add("1,3,l,f", keyLabel);
        panel.add("3,3,f,f", key.getComponent());
        panel.add("1,5,l,f", colorLabel);
        panel.add("3,5,f,f", colorPanel);
        colorPanel.setLayout(new BorderLayout());
        colorPanel.add(colorTextField.getComponent(), BorderLayout.CENTER);
        nameLabel.setText(getString("name") + ":");
        keyLabel.setText(getString("key") + ":");
        colorLabel.setText(getString("color") + ":");
        name.addChangeListener(this);
        key.addChangeListener(this);
        colorTextField.addChangeListener(this);
    }

    public void requestFocus() {
        name.requestFocus();
    }

    public void setEditKeys(boolean editKeys) {
        keyLabel.setVisible(editKeys);
        key.getComponent().setVisible(editKeys);
        colorLabel.setVisible(editKeys);
        colorTextField.getComponent().setVisible(editKeys);
    }

    public JComponent getComponent() {
        return panel;
    }

    public Object getValue() {
        return null;
    }

    public void setValue(Object object) {
    }

    public void mapFrom(Object object) throws RaplaException {
        Category category = (Category) object;
        name.mapFrom(category);
        key.mapFrom(category);
    }

    public void mapTo(Object object) throws RaplaException {
        Category category = (Category) object;
        name.mapTo(category);
        key.mapTo(category);
        String colorValue = colorTextField.getValue().toString().trim();
        if (colorValue.length() > 0) {
            category.setAnnotation(CategoryAnnotations.KEY_NAME_COLOR, colorValue);
        } else {
            category.setAnnotation(CategoryAnnotations.KEY_NAME_COLOR, null);
        }
    }

    public void stateChanged(ChangeEvent e) {
        fireContentChanged();
    }
}
