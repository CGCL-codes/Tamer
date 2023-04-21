package com.prolix.editor.main.workspace.export.resources;

import com.prolix.editor.resourcemanager.model.ResourceTreeItem;

/**
 * INterface when resource tree items are added, removed, or changed; to update the composite view
 * when a new category was selected
 * @author zander
 *
 */
public interface IItemModelListener {

    public void resourceItemAdded(ResourceTreeItem item);

    public void resourceItemChanged(ResourceTreeItem item);

    public void resourceItemRemoved(ResourceTreeItem item);

    public void resourceItemMoved(ResourceTreeItem item);
}
