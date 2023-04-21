package com.samyem.datamanager.ui;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import com.samyem.datamanager.commons.DataManager;
import com.samyem.datamanager.model.Item;
import com.samyem.datamanager.model.ItemContainer;
import com.samyem.datamanager.model.filesystem.FileItem;

public class ContainerExplorer extends Window {

    private static final long serialVersionUID = 1L;

    private DataManager manager;

    private ItemContainer rootContainer;

    public ContainerExplorer(DataManager manager, ItemContainer container) {
        this.manager = manager;
        this.rootContainer = container;
        setupUI();
    }

    private void setupUI() {
        this.appendChild(new Caption("Exploring " + rootContainer.getName()));
        final Tree tree = new Tree();
        Treecols cols = new Treecols();
        tree.appendChild(cols);
        Treecol nameCol = new Treecol("Name");
        nameCol.setWidth("400px");
        cols.appendChild(nameCol);
        cols.appendChild(new Treecol("Description"));
        Treechildren children = new Treechildren();
        tree.appendChild(children);
        addChildren(children, rootContainer);
        this.appendChild(tree);
        tree.addEventListener("onSelect", new EventListener() {

            public boolean isAsap() {
                return true;
            }

            public void onEvent(Event event) {
                doSelectItem(tree);
            }
        });
    }

    private void addChildren(Treechildren children, ItemContainer container) {
        for (Item item : container.getChildren()) {
            Treeitem treeItem = new Treeitem();
            children.appendChild(treeItem);
            Treerow row = new Treerow();
            treeItem.appendChild(row);
            Treecell nameCell = new Treecell(item.getName());
            row.appendChild(nameCell);
            Treecell descriptionCell = new Treecell(item.getDescription() == null ? "" : item.getDescription());
            row.appendChild(descriptionCell);
            if (item instanceof ItemContainer) {
                setChildable(treeItem, (ItemContainer) item);
            } else {
                treeItem.setValue(item);
            }
        }
    }

    private void doSelectItem(Tree tree) {
        Object value = tree.getSelectedItem().getValue();
        if (value == null) {
            return;
        }
        Item item = (Item) value;
        if (item instanceof FileItem) {
            onItemOpened(item);
        }
    }

    /**
     * Set all containers as potential tree node that can be expanded as needed
     * @param treeItem
     */
    private void setChildable(final Treeitem treeItem, final ItemContainer container) {
        final Treechildren children = new Treechildren();
        treeItem.appendChild(children);
        final Treeitem childTreeItem = new Treeitem();
        children.appendChild(childTreeItem);
        Treerow row = new Treerow();
        childTreeItem.appendChild(row);
        Treecell nameCell = new Treecell("Loading...");
        row.appendChild(nameCell);
        treeItem.setOpen(false);
        treeItem.addEventListener("onOpen", new EventListener() {

            public boolean isAsap() {
                return true;
            }

            public void onEvent(Event event) {
                treeItem.removeEventListener("onOpen", this);
                children.removeChild(childTreeItem);
                addChildren(children, container);
            }
        });
    }

    public void onItemOpened(Item item) {
    }
}
