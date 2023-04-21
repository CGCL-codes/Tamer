package com.google.gdt.eclipse.designer.gxt.model.layout;

import com.google.gdt.eclipse.designer.gxt.model.widgets.LayoutContainerInfo;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.draw2d.geometry.Dimension;
import org.eclipse.wb.draw2d.geometry.Point;
import org.eclipse.wb.draw2d.geometry.Rectangle;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.model.description.ToolkitDescription;
import org.eclipse.wb.internal.core.model.layout.absolute.IPreferenceConstants;
import org.eclipse.wb.internal.core.model.property.Property;
import org.eclipse.wb.internal.core.model.util.AbsoluteLayoutCreationFlowSupport;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;
import org.eclipse.wb.internal.core.utils.check.Assert;

/**
 * Model for <code>com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout</code>.
 * 
 * @author scheglov_ke
 * @coverage ExtGWT.model.layout
 */
public final class AbsoluteLayoutInfo extends AnchorLayoutInfo {

    public AbsoluteLayoutInfo(AstEditor editor, ComponentDescription description, CreationSupport creationSupport) throws Exception {
        super(editor, description, creationSupport);
    }

    @Override
    public void onSet() throws Exception {
        super.onSet();
        for (WidgetInfo widget : getWidgets()) {
            Rectangle bounds = widget.getModelBounds();
            command_BOUNDS(widget, bounds.getLocation(), bounds.getSize());
        }
    }

    @Override
    protected void onDelete() throws Exception {
        super.onDelete();
        for (WidgetInfo widget : getWidgets()) {
            widget.getSizeSupport().setSize(null);
        }
    }

    @Override
    protected void applyDefaultAnchorSize(WidgetInfo widget) throws Exception {
    }

    /**
   * @return {@link AbsoluteDataInfo} associated with given {@link WidgetInfo}.
   */
    public static AbsoluteDataInfo getAbsoluteData(WidgetInfo widget) {
        return (AbsoluteDataInfo) getLayoutData(widget);
    }

    /**
   * Performs "move" or "resize" operation.
   * 
   * @param widget
   *          the {@link WidgetInfo} which modifications applies to.
   * @param location
   *          the {@link Point} of new location of widget. May be <code>null</code>.
   * @param size
   *          the {@link Dimension} of new size of widget. May be <code>null</code>.
   */
    public void command_BOUNDS(WidgetInfo widget, Point location, Dimension size) throws Exception {
        LayoutContainerInfo container = getContainer();
        Assert.isTrue(container.getChildren().contains(widget), "%s is not child of %s.", widget, this);
        AbsoluteDataInfo absoluteData = getAbsoluteData(widget);
        if (location != null) {
            absoluteData.materialize();
            absoluteData.getPropertyByTitle("left").setValue(location.x);
            absoluteData.getPropertyByTitle("top").setValue(location.y);
        }
        if (size != null) {
            absoluteData.setAnchor(Property.UNKNOWN_VALUE);
            widget.getSizeSupport().setSize(size);
        }
        if (location != null && useCreationFlow()) {
            AbsoluteLayoutCreationFlowSupport.apply(container, container.getChildren(WidgetInfo.class), widget, location, size);
        }
    }

    private boolean useCreationFlow() {
        return getToolkit().getPreferences().getBoolean(IPreferenceConstants.P_CREATION_FLOW);
    }

    private ToolkitDescription getToolkit() {
        return getDescription().getToolkit();
    }
}
