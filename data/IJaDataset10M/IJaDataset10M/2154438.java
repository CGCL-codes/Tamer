package com.google.gdt.eclipse.designer.gef.policy.grid.header.actions;

import com.google.common.collect.Lists;
import com.google.gdt.eclipse.designer.gef.policy.grid.header.edit.ColumnHeaderEditPart;
import com.google.gdt.eclipse.designer.gef.policy.grid.header.edit.DimensionHeaderEditPart;
import com.google.gdt.eclipse.designer.model.widgets.panels.grid.DimensionInfo;
import com.google.gdt.eclipse.designer.model.widgets.panels.grid.HTMLTableInfo;
import org.eclipse.wb.gef.core.EditPart;
import org.eclipse.wb.gef.core.IEditPartViewer;
import org.eclipse.wb.internal.core.model.util.ObjectInfoAction;
import org.eclipse.jface.resource.ImageDescriptor;
import java.util.List;

/**
 * Abstract action for manipulating selected {@link DimensionInfo}'s.
 * 
 * @author scheglov_ke
 * @coverage gwt.gef.policy
 */
public abstract class DimensionHeaderAction<T extends DimensionInfo> extends ObjectInfoAction {

    private final boolean m_horizontal;

    private final IEditPartViewer m_viewer;

    private final HTMLTableInfo m_panel;

    public DimensionHeaderAction(DimensionHeaderEditPart<T> editPart, String text) {
        this(editPart, text, null);
    }

    public DimensionHeaderAction(DimensionHeaderEditPart<T> editPart, String text, ImageDescriptor imageDescriptor) {
        this(editPart, text, imageDescriptor, AS_PUSH_BUTTON);
    }

    public DimensionHeaderAction(DimensionHeaderEditPart<T> editPart, String text, ImageDescriptor imageDescriptor, int style) {
        super(editPart.getPanel(), text, style);
        m_horizontal = editPart instanceof ColumnHeaderEditPart;
        m_viewer = editPart.getViewer();
        m_panel = editPart.getPanel();
        setImageDescriptor(imageDescriptor);
    }

    @Override
    public final int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }

    @Override
    protected final void runEx() throws Exception {
        List<T> dimensions = Lists.newArrayList();
        {
            List<EditPart> editParts = m_viewer.getSelectedEditParts();
            for (EditPart editPart : editParts) {
                if (editPart instanceof DimensionHeaderEditPart) {
                    @SuppressWarnings("unchecked") DimensionHeaderEditPart<T> headerEditPart = (DimensionHeaderEditPart<T>) editPart;
                    dimensions.add(headerEditPart.getDimension());
                }
            }
        }
        run(dimensions);
    }

    /**
   * Does some operation on {@link List} of selected {@link DimensionInfo}'s.
   */
    protected void run(List<T> dimensions) throws Exception {
        List<?> allDimensions = m_horizontal ? m_panel.getColumns() : m_panel.getRows();
        for (T dimension : dimensions) {
            int index = allDimensions.indexOf(dimension);
            run(dimension, index);
        }
    }

    /**
   * Does some operation on selected {@link DimensionInfo}'s.
   */
    protected void run(T dimension, int index) throws Exception {
    }
}
