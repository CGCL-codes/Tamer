package com.google.gdt.eclipse.designer.gwtext.gef.policy;

import com.google.common.collect.Lists;
import com.google.gdt.eclipse.designer.gef.policy.AbstractResizeSelectionEditPolicy;
import com.google.gdt.eclipse.designer.gwtext.model.layout.RowLayoutInfo;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.core.gef.command.EditCommand;
import org.eclipse.wb.draw2d.IColorConstants;
import org.eclipse.wb.draw2d.IPositionConstants;
import org.eclipse.wb.gef.graphical.handles.Handle;
import java.util.List;

/**
 * {@link SelectionLayoutEditPolicy} for {@link WidgetInfo} on {@link RowLayoutInfo}.
 * 
 * @author scheglov_ke
 * @coverage GWTExt.gef.policy
 */
public final class RowSelectionEditPolicy extends AbstractResizeSelectionEditPolicy {

    private final WidgetInfo m_widget;

    public RowSelectionEditPolicy(WidgetInfo widget) {
        m_widget = widget;
    }

    @Override
    protected List<Handle> createSelectionHandles() {
        List<Handle> handles = Lists.newArrayList();
        handles.add(createResizeHandle(IPositionConstants.SOUTH, 0.25, IColorConstants.green));
        return handles;
    }

    @Override
    protected void updateTooltipCommand() {
        if (m_ctrlPressed) {
            final String height = m_newSize.height * 100 / getParentHeight() + "%";
            m_tooltip = "height: " + height + "\nRelease Ctrl to set height in pixels";
            m_command = new EditCommand(m_widget) {

                @Override
                protected void executeEdit() throws Exception {
                    RowLayoutInfo.getRowData(m_widget).setHeight(height);
                }
            };
        } else {
            final int height = m_newSize.height;
            m_tooltip = "height: " + height + "px" + "\nPress Ctrl to set height in percents";
            m_command = new EditCommand(m_widget) {

                @Override
                protected void executeEdit() throws Exception {
                    RowLayoutInfo.getRowData(m_widget).setHeight(height);
                }
            };
        }
    }
}
