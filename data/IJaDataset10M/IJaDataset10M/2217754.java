package com.google.gdt.eclipse.designer.gef.part.panels;

import com.google.common.collect.Lists;
import com.google.gdt.eclipse.designer.gef.part.CompositeEditPart;
import com.google.gdt.eclipse.designer.gef.policy.TabLikePanelHandleLayoutEditPolicy;
import com.google.gdt.eclipse.designer.gef.policy.TabLikePanelWidgetLayoutEditPolicy;
import com.google.gdt.eclipse.designer.model.widgets.panels.TabPanelInfo;
import org.eclipse.wb.gef.core.EditPart;
import java.util.List;

/**
 * {@link EditPart} for {@link TabPanelInfo}.
 * 
 * @author scheglov_ke
 * @coverage gwt.gef.part
 */
public final class TabPanelEditPart extends CompositeEditPart {

    private final TabPanelInfo m_panel;

    public TabPanelEditPart(TabPanelInfo panel) {
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
