package com.google.gdt.eclipse.designer.gxt.model.layout.assistant;

import com.google.gdt.eclipse.designer.gxt.model.layout.BorderLayoutInfo;
import org.eclipse.wb.core.editor.actions.assistant.AbstractAssistantPage;
import org.eclipse.wb.core.model.ObjectInfo;
import org.eclipse.swt.widgets.Composite;
import java.util.List;

/**
 * Assistant pages provider for {@link BorderLayoutInfo}.
 * 
 * @author sablin_aa
 * @coverage ExtGWT.model.layout.assistant
 */
public final class BorderLayoutAssistant extends LayoutAssistantSupport<BorderLayoutInfo> {

    public BorderLayoutAssistant(BorderLayoutInfo layout) {
        super(layout);
    }

    @Override
    protected AbstractAssistantPage createLayoutPage(Composite parent) {
        return new BorderLayoutAssistantPage(parent, m_layout);
    }

    @Override
    protected AbstractAssistantPage createConstraintsPage(Composite parent, List<ObjectInfo> objects) {
        return new BorderLayoutDataAssistantPage(parent, objects);
    }
}
