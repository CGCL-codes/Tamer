package com.google.gdt.eclipse.designer.gwtext.model.layout;

import com.google.gdt.eclipse.designer.gwtext.model.layout.assistant.FormLayoutAssistant;
import com.google.gdt.eclipse.designer.gwtext.model.widgets.FieldInfo;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.internal.core.model.JavaInfoUtils;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;
import org.eclipse.wb.internal.core.utils.check.Assert;

/**
 * Model for <code>com.gwtext.client.widgets.layout.FormLayout</code>.
 * 
 * @author scheglov_ke
 * @coverage GWTExt.model.layout
 */
public final class FormLayoutInfo extends AnchorLayoutInfo {

    public FormLayoutInfo(AstEditor editor, ComponentDescription description, CreationSupport creationSupport) throws Exception {
        super(editor, description, creationSupport);
    }

    @Override
    protected void initializeLayoutAssistant() {
        new FormLayoutAssistant(this);
    }

    private Class<?> m_formLayoutDataClass;

    @Override
    protected Object getDefaultVirtualDataObject(WidgetInfo widget) throws Exception {
        if (widget instanceof FieldInfo) {
            return getFormLayoutDataClass().newInstance();
        }
        return super.getDefaultVirtualDataObject(widget);
    }

    @Override
    protected Class<?> getLayoutDataClass(WidgetInfo widget) throws Exception {
        if (widget instanceof FieldInfo) {
            return getFormLayoutDataClass();
        }
        return super.getLayoutDataClass(widget);
    }

    private Class<?> getFormLayoutDataClass() throws Exception {
        if (m_formLayoutDataClass == null) {
            String layoutDataClassName = "com.gwtext.client.widgets.layout.FormLayoutData";
            Assert.isNotNull(layoutDataClassName);
            Assert.isTrue(layoutDataClassName.length() != 0);
            m_formLayoutDataClass = JavaInfoUtils.getClassLoader(this).loadClass(layoutDataClassName);
        }
        return m_formLayoutDataClass;
    }

    /**
   * @return {@link FormLayoutDataInfo} association with given {@link WidgetInfo}.
   */
    public static FormLayoutDataInfo getFormData(WidgetInfo widget) {
        return (FormLayoutDataInfo) getLayoutData(widget);
    }
}
