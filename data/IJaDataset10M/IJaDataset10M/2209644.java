package com.google.gdt.eclipse.designer.model.widgets;

import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;

/**
 * Model for <code>Image</code> widget.
 * 
 * @author scheglov_ke
 * @coverage gwt.model
 */
public final class ImageInfo extends WidgetInfo {

    public ImageInfo(AstEditor editor, ComponentDescription description, CreationSupport creationSupport) throws Exception {
        super(editor, description, creationSupport);
    }
}
