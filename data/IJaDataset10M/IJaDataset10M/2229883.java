package com.google.gdt.eclipse.designer.uibinder.gef.part;

import com.google.common.collect.Lists;
import com.google.gdt.eclipse.designer.gef.policy.TabLikePanelHandleLayoutEditPolicy;
import com.google.gdt.eclipse.designer.gef.policy.TabLikePanelWidgetLayoutEditPolicy;
import com.google.gdt.eclipse.designer.uibinder.model.widgets.TabLayoutPanelInfo;
import org.eclipse.wb.gef.core.EditPart;
import java.util.List;

/**
 * {@link EditPart} for {@link TabLayoutPanelInfo}.
 * 
 * @author scheglov_ke
 * @coverage GWT.UiBinder.gef
 */
public final class TabLayoutPanelEditPart extends PanelEditPart {

    private final TabLayoutPanelInfo m_panel;

    public TabLayoutPanelEditPart(TabLayoutPanelInfo panel) {
        super(panel);
        m_panel = panel;
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(TabLikePanelWidgetLayoutEditPolicy.create(m_panel, true));
        installEditPolicy(TabLikePanelHandleLayoutEditPolicy.create(m_panel, true));
    }

    @Override
    protected List<?> getModelChildren() {
        List<Object> children = Lists.newArrayList();
        children.add(m_panel.getActiveWidget());
        children.addAll(m_panel.getWidgetHandles());
        return children;
    }
}
