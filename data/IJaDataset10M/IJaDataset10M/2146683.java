package org.jowidgets.workbench.toolkit.impl;

import java.util.LinkedList;
import java.util.List;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.IViewLayout;

final class FolderLayout extends WorkbenchPart implements IFolderLayout {

    private final String id;

    private final String groupId;

    private final List<? extends IViewLayout> views;

    private final boolean isDetachable;

    private final boolean viewsCloseable;

    FolderLayout(final String id, final String groupId, final String label, final String tooltip, final IImageConstant icon, final List<? extends IViewLayout> views, final boolean isDetachable, final boolean viewsCloseable) {
        super(label, tooltip, icon);
        Assert.paramNotEmpty(id, "id");
        Assert.paramNotNull(views, "views");
        this.id = id;
        if (groupId != null) {
            this.groupId = groupId;
        } else {
            this.groupId = id;
        }
        this.views = new LinkedList<IViewLayout>(views);
        this.isDetachable = isDetachable;
        this.viewsCloseable = viewsCloseable;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public List<? extends IViewLayout> getViews() {
        return new LinkedList<IViewLayout>(views);
    }

    @Override
    public boolean isDetachable() {
        return isDetachable;
    }

    @Override
    public boolean getViewsCloseable() {
        return viewsCloseable;
    }
}
