package com.google.gdt.eclipse.designer.smart.gef.part.form;

import com.google.gdt.eclipse.designer.smart.model.form.FormItemInfo;
import org.eclipse.wb.core.gef.part.AbstractComponentEditPart;
import org.eclipse.wb.gef.core.EditPart;

/**
 * {@link EditPart} for {@link FormItemInfo}.
 * 
 * @author sablin_aa
 * @coverage SmartGWT.gef.part
 */
public final class FormItemEditPart extends AbstractComponentEditPart {

    public FormItemEditPart(FormItemInfo item) {
        super(item);
    }
}
