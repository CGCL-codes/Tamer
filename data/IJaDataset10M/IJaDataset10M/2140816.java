package org.jowidgets.workbench.tools;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.ILayoutContainer;
import org.jowidgets.workbench.api.LayoutScope;
import org.jowidgets.workbench.toolkit.api.ILayoutBuilder;
import org.jowidgets.workbench.toolkit.api.ILayoutContainerBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class Layout implements ILayout {

    private final ILayout layout;

    public Layout(final String id, final ILayoutContainer layoutContainer) {
        this(builder(id, layoutContainer));
    }

    public Layout(final String id, final String label, final ILayoutContainer layoutContainer) {
        this(builder(id, label, layoutContainer));
    }

    public Layout(final String id, final String label, final String tooltip, final ILayoutContainer layoutContainer) {
        this(builder(id, label, tooltip, layoutContainer));
    }

    public Layout(final String id, final String label, final String tooltip, final IImageConstant icon, final ILayoutContainer layoutContainer) {
        this(builder(id, label, tooltip, icon, layoutContainer));
    }

    public Layout(final String id, final ILayoutContainerBuilder layoutContainerBuilder) {
        this(builder(id, layoutContainerBuilder));
    }

    public Layout(final String id, final String label, final ILayoutContainerBuilder layoutContainerBuilder) {
        this(builder(id, label, layoutContainerBuilder));
    }

    public Layout(final String id, final String label, final String tooltip, final ILayoutContainerBuilder layoutContainerBuilder) {
        this(builder(id, label, tooltip, layoutContainerBuilder));
    }

    public Layout(final String id, final String label, final String tooltip, final IImageConstant icon, final ILayoutContainerBuilder layoutContainerBuilder) {
        this(builder(id, label, tooltip, icon, layoutContainerBuilder));
    }

    public Layout(final ILayoutBuilder builder) {
        Assert.paramNotNull(builder, "builder");
        this.layout = builder.build();
    }

    @Override
    public final String getLabel() {
        return layout.getLabel();
    }

    @Override
    public final String getTooltip() {
        return layout.getTooltip();
    }

    @Override
    public final IImageConstant getIcon() {
        return layout.getIcon();
    }

    @Override
    public final String getId() {
        return layout.getId();
    }

    @Override
    public final LayoutScope getScope() {
        return layout.getScope();
    }

    @Override
    public final ILayoutContainer getLayoutContainer() {
        return layout.getLayoutContainer();
    }

    public static ILayoutBuilder builder(final String id, final String label, final String tooltip, final IImageConstant icon, final ILayoutContainer layoutContainer) {
        return builder().setId(id).setLabel(label).setTooltip(tooltip).setIcon(icon).setLayoutContainer(layoutContainer);
    }

    public static ILayoutBuilder builder(final String id, final String label, final String tooltip, final ILayoutContainer layoutContainer) {
        return builder().setId(id).setLabel(label).setTooltip(tooltip).setLayoutContainer(layoutContainer);
    }

    public static ILayoutBuilder builder(final String id, final String label, final ILayoutContainer layoutContainer) {
        return builder().setId(id).setLabel(label).setLayoutContainer(layoutContainer);
    }

    public static ILayoutBuilder builder(final String id, final ILayoutContainer layoutContainer) {
        return builder().setId(id).setLayoutContainer(layoutContainer);
    }

    public static ILayoutBuilder builder(final String id, final String label, final String tooltip, final IImageConstant icon, final ILayoutContainerBuilder layoutContainerBuilder) {
        return builder().setId(id).setLabel(label).setTooltip(tooltip).setIcon(icon).setLayoutContainer(layoutContainerBuilder);
    }

    public static ILayoutBuilder builder(final String id, final String label, final String tooltip, final ILayoutContainerBuilder layoutContainerBuilder) {
        return builder().setId(id).setLabel(label).setTooltip(tooltip).setLayoutContainer(layoutContainerBuilder);
    }

    public static ILayoutBuilder builder(final String id, final String label, final ILayoutContainerBuilder layoutContainerBuilder) {
        return builder().setId(id).setLabel(label).setLayoutContainer(layoutContainerBuilder);
    }

    public static ILayoutBuilder builder(final String id, final ILayoutContainerBuilder layoutContainerBuilder) {
        return builder().setId(id).setLayoutContainer(layoutContainerBuilder);
    }

    private static ILayoutBuilder builder() {
        return WorkbenchToolkit.getLayoutBuilderFactory().layout();
    }
}
