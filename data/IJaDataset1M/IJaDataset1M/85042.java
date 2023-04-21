package org.jowidgets.workbench.toolkit.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.toolkit.api.IFolderLayoutBuilder;
import org.jowidgets.workbench.toolkit.api.IViewLayoutBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

class FolderLayoutBuilder extends WorkbenchPartBuilder<IFolderLayoutBuilder> implements IFolderLayoutBuilder {

    private final List<IViewLayout> views;

    private String id;

    private String groupId;

    private boolean detachable;

    private boolean viewsCloseable;

    FolderLayoutBuilder() {
        super();
        this.detachable = true;
        this.viewsCloseable = true;
        this.views = new LinkedList<IViewLayout>();
    }

    @Override
    public IFolderLayoutBuilder setId(final String id) {
        Assert.paramNotEmpty(id, "id");
        this.id = id;
        return this;
    }

    @Override
    public IFolderLayoutBuilder setGroupId(final String groupId) {
        Assert.paramNotEmpty(groupId, "groupId");
        this.groupId = groupId;
        return this;
    }

    @Override
    public IFolderLayoutBuilder setDetachable(final boolean detachable) {
        this.detachable = detachable;
        return this;
    }

    @Override
    public IFolderLayoutBuilder setViewsCloseable(final boolean viewsCloseable) {
        this.viewsCloseable = viewsCloseable;
        return this;
    }

    @Override
    public IFolderLayoutBuilder setViews(final List<? extends IViewLayout> views) {
        Assert.paramNotNull(views, "views");
        this.views.clear();
        this.views.addAll(views);
        return this;
    }

    @Override
    public IFolderLayoutBuilder setViews(final IViewLayout... views) {
        Assert.paramNotNull(views, "views");
        return setViews(Arrays.asList(views));
    }

    @Override
    public IFolderLayoutBuilder addView(final IViewLayout view) {
        Assert.paramNotNull(view, "view");
        this.views.add(view);
        return this;
    }

    @Override
    public IFolderLayoutBuilder setViews(final IViewLayoutBuilder... viewsBuilder) {
        Assert.paramNotNull(viewsBuilder, "viewsBuilder");
        this.views.clear();
        for (final IViewLayoutBuilder viewLayoutBuilder : viewsBuilder) {
            this.views.add(viewLayoutBuilder.build());
        }
        return this;
    }

    @Override
    public IFolderLayoutBuilder addView(final IViewLayoutBuilder viewBuilder) {
        return addView(viewBuilder.build());
    }

    @Override
    public IFolderLayoutBuilder addView(final String id, final String label, final String tooltip, final IImageConstant icon) {
        return addView(view().setId(id).setLabel(label).setTooltip(tooltip).setIcon(icon));
    }

    @Override
    public IFolderLayoutBuilder addView(final String id, final String label, final IImageConstant icon) {
        return addView(view().setId(id).setLabel(label).setIcon(icon));
    }

    @Override
    public IFolderLayoutBuilder addView(final String id, final String label, final String tooltip) {
        return addView(view().setId(id).setLabel(label).setTooltip(tooltip));
    }

    @Override
    public IFolderLayoutBuilder addView(final String id, final String label) {
        return addView(view().setId(id).setLabel(label));
    }

    @Override
    public IFolderLayoutBuilder addView(final String id, final IImageConstant icon) {
        return addView(view().setId(id).setIcon(icon));
    }

    @Override
    public IFolderLayout build() {
        return new FolderLayout(id, groupId, getLabel(), getTooltip(), getIcon(), views, detachable, viewsCloseable);
    }

    private static IViewLayoutBuilder view() {
        return WorkbenchToolkit.getLayoutBuilderFactory().view();
    }
}
