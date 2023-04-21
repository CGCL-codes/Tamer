package com.google.gdt.eclipse.designer.gef.part.panels;

import com.google.gdt.eclipse.designer.gef.policy.VerticalSplitPanelLayoutEditPolicy;
import com.google.gdt.eclipse.designer.model.widgets.panels.VerticalSplitPanelInfo;
import org.eclipse.wb.gef.core.EditPart;
import org.eclipse.wb.gef.core.policies.EditPolicy;

/**
 * {@link EditPart} for {@link VerticalSplitPanelInfo}.
 * 
 * @author scheglov_ke
 * @coverage gwt.gef.part
 */
public final class VerticalSplitPanelEditPart extends PanelEditPart {

    private final VerticalSplitPanelInfo m_panel;

    public VerticalSplitPanelEditPart(VerticalSplitPanelInfo panel) {
        super(panel);
        m_panel = panel;
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, VerticalSplitPanelLayoutEditPolicy.create(m_panel));
    }
}
