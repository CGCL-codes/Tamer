package org.jowidgets.workbench.toolkit.impl;

import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.ILayoutContainer;
import org.jowidgets.workbench.api.LayoutScope;
import org.jowidgets.workbench.toolkit.api.ILayoutBuilder;
import org.jowidgets.workbench.toolkit.api.ILayoutContainerBuilder;

class LayoutBuilder extends WorkbenchPartBuilder<ILayoutBuilder> implements ILayoutBuilder {

    private String id;

    private LayoutScope scope;

    private ILayoutContainer layoutContainer;

    LayoutBuilder() {
        super();
        this.scope = LayoutScope.COMPONENT;
    }

    @Override
    public ILayoutBuilder setId(final String id) {
        Assert.paramNotEmpty(id, "id");
        this.id = id;
        return this;
    }

    @Override
    public ILayoutBuilder setScope(final LayoutScope scope) {
        Assert.paramNotNull(scope, "scope");
        this.scope = scope;
        return this;
    }

    @Override
    public ILayoutBuilder setLayoutContainer(final ILayoutContainer layoutContainer) {
        Assert.paramNotNull(layoutContainer, "layoutContainer");
        this.layoutContainer = layoutContainer;
        return this;
    }

    @Override
    public ILayoutBuilder setLayoutContainer(final ILayoutContainerBuilder layoutContainerBuilder) {
        return setLayoutContainer(layoutContainerBuilder.build());
    }

    @Override
    public ILayout build() {
        return new Layout(id, scope, getLabel(), getTooltip(), getIcon(), layoutContainer);
    }
}
