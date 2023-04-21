package com.google.gdt.eclipse.designer.gxt.databinding.ui.property;

import com.google.gdt.eclipse.designer.gxt.databinding.ui.providers.BindingLabelProvider;
import org.eclipse.wb.internal.core.databinding.ui.property.AbstractBindingProperty;
import org.eclipse.wb.internal.core.databinding.ui.property.Context;

/**
 * Property for single binding.
 * 
 * @author lobas_av
 * 
 */
public class BindingProperty extends AbstractBindingProperty {

    public BindingProperty(Context context) {
        super(context);
    }

    @Override
    public String getText() throws Exception {
        int column = m_isTarget ? 2 : 1;
        return BindingLabelProvider.INSTANCE.getColumnText(m_binding, column);
    }
}
