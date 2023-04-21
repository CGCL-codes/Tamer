package com.prolix.editor.main.workspace.export.resources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import com.prolix.editor.main.navi.bar.IMainNavigationListener;
import com.prolix.editor.main.navi.bar.id.GLMNavigationKey;
import com.prolix.editor.main.workspace.export.ExportWorkspace;
import com.prolix.editor.resourcemanager.model.ResourceTreeCategory;
import com.prolix.editor.resourcemanager.model.ResourceTreeItem;
import com.prolix.editor.resourcemanager.treeviewer.ResourceItemsTreeViewer;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import com.prolix.editor.resourcemanager.zip.RessourceManager;

public class ResourceItemCompositeManager extends SashForm implements IItemModelListener, IMainNavigationListener {

    private ExportWorkspace export_workspace;

    private Composite item_composite;

    private ResourceTreeCategory root_element;

    private ScrolledComposite scroll_composite;

    private ResourceItemsTreeViewer tree_viewer;

    private ResourceTreeCategory category;

    public ResourceItemCompositeManager(ExportWorkspace parent) {
        super(parent, SWT.NONE);
        this.export_workspace = parent;
        setupView();
        parent.addNavigationListener(this);
    }

    public RessourceManager getRessourceManager() {
        return export_workspace.getContainer().getRessourceManager();
    }

    public LearningDesignDataModel getContainer() {
        return export_workspace.getContainer();
    }

    private void setupView() {
        tree_viewer = new ResourceItemsTreeViewer(this, SWT.NONE);
        export_workspace.getContainer().getResourceRootCategory().setViewer(tree_viewer);
        this.root_element = export_workspace.getContainer().getResourceRootCategory();
        tree_viewer.setInput(this.root_element);
        this.root_element.addListener(this);
        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 0;
        layout.marginHeight = 1;
        layout.marginWidth = 1;
        scroll_composite = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        item_composite = new Composite(scroll_composite, SWT.NONE);
        item_composite.setLayout(layout);
        item_composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        scroll_composite.setContent(item_composite);
        scroll_composite.setExpandVertical(true);
        scroll_composite.setExpandHorizontal(true);
        scroll_composite.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                scroll_composite.setMinSize(item_composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                item_composite.layout();
            }
        });
        setWeights(new int[] { 3, 7 });
    }

    public ResourceTreeCategory getRootElement() {
        return this.root_element;
    }

    public void setCategory(ResourceTreeCategory category) {
        processCategory(category);
    }

    /**
	 * FIXME here is a problem: when the category (parent) has changed, the composite of the new parent is displayed instead of the old category; the tree also
	 * doesn't change accordingly
	 * 
	 * @param category
	 */
    public void processCategory(ResourceTreeCategory category) {
        this.processCategory(category, false);
    }

    public void processCategory(ResourceTreeCategory category, boolean force) {
        if (category == null || (category.equals(this.category) && !force)) return;
        this.category = category;
        Control[] controls = item_composite.getChildren();
        if (controls != null) {
            for (int i = 0; i < controls.length; i++) controls[i].dispose();
        }
        if (category.getResourceTreeItems() == null) return;
        Object[] children = category.getResourceTreeItems();
        if (children == null) {
            item_composite.redraw();
            return;
        }
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof ResourceTreeItem) {
                addResourceItemComposite((ResourceTreeItem) children[i]);
            }
        }
    }

    /**
	 * creates a new composite for the given {@link ResourceTreeItem} and adds it to the item_composite when an item was moved, the the target (new) category is
	 * displayed (not the old one as intended)
	 * 
	 * @param item
	 */
    private void addResourceItemComposite(ResourceTreeItem item) {
        if (item.getParent().equals(this.category)) {
            new ResourceTreeItemComposite(item, this.item_composite);
            item_composite.layout();
            scroll_composite.setMinSize(item_composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }
    }

    /**
	 * called when an {@link ResourceTreeItem} has changed
	 * 
	 * @param res_item
	 */
    private void updateComposite(ResourceTreeItem res_item) {
        Control[] composites = this.item_composite.getChildren();
        ResourceTreeItemComposite composite;
        for (int i = 0; i < composites.length; i++) {
            composite = (ResourceTreeItemComposite) composites[i];
            if (composite.getResourceTreeItem().equals(res_item)) {
                System.out.println("\nResourceItemCompositeManager::(private)updating composite ---> " + composite + "; item: " + res_item.getLabel());
                composite.updateComposite();
            }
        }
        item_composite.layout();
    }

    /**
	 * disposes the composite {@link ResourceTreeItemComposite} which represents the given {@link ResourceTreeItem}
	 * 
	 * @param item
	 */
    private void disposeComposite(ResourceTreeItem item) {
        if (!item.getParent().equals(this.category)) return;
        Control[] composites = this.item_composite.getChildren();
        ResourceTreeItemComposite composite;
        for (int i = 0; i < composites.length; i++) {
            composite = (ResourceTreeItemComposite) composites[i];
            if (composite.getResourceTreeItem().equals(item)) {
                composite.setVisible(false);
                composite.dispose();
                System.out.println("ResourceItemCompositeManager ---> composite disposed: " + composite.getResourceTreeItem().getLabel() + " - item_composites.children: " + this.item_composite.getChildren().length);
            }
        }
        item_composite.layout();
        item_composite.update();
        System.out.println("ResourceItemCompositeManager ---> active composite (Category): " + this.category.getLabel() + " - item_composites.children (after dispose): " + this.item_composite.getChildren().length);
    }

    /**
	 * ==============================================================================================
	 * 
	 * Listener / Interface methods
	 * 
	 * ==============================================================================================
	 */
    public void resourceItemAdded(ResourceTreeItem item) {
        addResourceItemComposite(item);
    }

    public void resourceItemChanged(ResourceTreeItem item) {
        updateComposite(item);
    }

    public void resourceItemMoved(ResourceTreeItem item) {
        processCategory(item.getParent());
    }

    public void resourceItemRemoved(ResourceTreeItem item) {
        disposeComposite(item);
    }

    public void update(GLMNavigationKey key) {
        if (key.isType(GLMNavigationKey.key_resources)) {
            this.processCategory(this.category, true);
        }
    }
}
