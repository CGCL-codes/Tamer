package com.google.gdt.eclipse.designer.smart.model;

import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.core.model.association.Association;
import org.eclipse.wb.core.model.association.InvocationAssociation;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;
import org.eclipse.wb.internal.core.utils.ast.AstNodeUtils;
import org.eclipse.wb.internal.core.utils.execution.ExecutionUtils;
import org.eclipse.wb.internal.core.utils.reflect.ReflectionUtils;

/**
 * Model for <code>com.smartgwt.client.widgets.tile.TileLayout</code>.
 * 
 * @author scheglov_ke
 * @coverage SmartGWT.model
 */
public class TileLayoutInfo extends CanvasInfo {

    public TileLayoutInfo(AstEditor editor, ComponentDescription description, CreationSupport creationSupport) throws Exception {
        super(editor, description, creationSupport);
    }

    @Override
    public void setObject(Object object) throws Exception {
        super.setObject(object);
        if (!isPlaceholder()) {
            ReflectionUtils.invokeMethod(object, "setAnimateTileChange(java.lang.Boolean)", false);
        }
    }

    @Override
    protected void refresh_dispose_detach(WidgetInfo child) throws Exception {
        if (child instanceof CanvasInfo) {
            CanvasInfo childCanvas = (CanvasInfo) child;
            Association association = childCanvas.getAssociation();
            if (association instanceof InvocationAssociation) {
                InvocationAssociation invocationAssociation = (InvocationAssociation) association;
                if (AstNodeUtils.getMethodSignature(invocationAssociation.getInvocation()).startsWith("addTile(com.smartgwt.client.widgets.Canvas")) {
                    if (childCanvas.isCreated()) {
                        ReflectionUtils.invokeMethod(getObject(), "removeTile(com.smartgwt.client.widgets.Canvas)", childCanvas.getObject());
                        return;
                    }
                }
            }
        }
        super.refresh_dispose_detach(child);
    }

    @Override
    protected void destroyObject(Object object) throws Exception {
        if (object != null) {
            ExecutionUtils.waitEventLoop(150);
        }
        super.destroyObject(object);
    }
}
