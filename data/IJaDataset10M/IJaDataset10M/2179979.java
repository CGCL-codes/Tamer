package com.google.gdt.eclipse.designer.gwtext.model.layout;

import com.google.gdt.eclipse.designer.gwtext.model.layout.assistant.BorderLayoutAssistant;
import com.google.gdt.eclipse.designer.gwtext.model.widgets.ContainerInfo;
import com.google.gdt.eclipse.designer.model.widgets.WidgetAttachAfterConstructor;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.core.eval.EvaluationContext;
import org.eclipse.wb.core.model.JavaInfo;
import org.eclipse.wb.core.model.association.Association;
import org.eclipse.wb.core.model.broadcast.EvaluationEventListener;
import org.eclipse.wb.core.model.broadcast.JavaInfoSetAssociationBefore;
import org.eclipse.wb.internal.core.model.JavaInfoUtils;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;
import org.eclipse.wb.internal.core.utils.reflect.ReflectionUtils;
import org.eclipse.wb.internal.core.utils.state.GlobalState;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Model for <code>com.gwtext.client.widgets.layout.BorderLayout</code>.
 * 
 * @author scheglov_ke
 * @coverage GWTExt.model.layout
 */
public final class BorderLayoutInfo extends LayoutInfo {

    public BorderLayoutInfo(AstEditor editor, ComponentDescription description, CreationSupport creationSupport) throws Exception {
        super(editor, description, creationSupport);
        ensureCenterWidgetListeners();
    }

    @Override
    protected void initializeLayoutAssistant() {
        new BorderLayoutAssistant(this);
    }

    @Override
    public void onSet() throws Exception {
        super.onSet();
        for (WidgetInfo widget : getContainer().getChildrenWidgets()) {
            getBorderData(widget).materialize();
        }
    }

    @Override
    protected Object getDefaultVirtualDataObject(WidgetInfo widget) throws Exception {
        return new Object();
    }

    /**
   * @return {@link BorderLayoutDataInfo} association with given {@link WidgetInfo}.
   */
    public static BorderLayoutDataInfo getBorderData(WidgetInfo widget) {
        return (BorderLayoutDataInfo) getLayoutData(widget);
    }

    /**
   * @return the {@link WidgetInfo} that has given position, may be <code>null</code>.
   */
    public Object getWidget(String position) {
        for (WidgetInfo widget : getContainer().getChildrenWidgets()) {
            String widgetPosition = getBorderData(widget).getPosition();
            if (position.equals(widgetPosition)) {
                return widget;
            }
        }
        return null;
    }

    @Override
    protected void refresh_afterCreate0() throws Exception {
        ensureCenterWidget();
        super.refresh_afterCreate0();
    }

    private void ensureCenterWidgetListeners() {
        addBroadcastListener(new WidgetAttachAfterConstructor() {

            public void invoke() throws Exception {
                ensureCenterWidget();
            }
        });
        addBroadcastListener(new JavaInfoSetAssociationBefore() {

            public void invoke(JavaInfo javaInfo, Association association) throws Exception {
                ContainerInfo container = getContainer();
                if (isActive() && GlobalState.isParsing() && javaInfo == container) {
                    ensureCenterWidget();
                }
            }
        });
        addBroadcastListener(new EvaluationEventListener() {

            @Override
            public void evaluateBefore(EvaluationContext context, ASTNode node) throws Exception {
                if (isActive() && node instanceof Statement) {
                    Association association = getContainer().getAssociation();
                    if (association != null && association.getStatement() == node) {
                        ensureCenterWidget();
                    }
                }
            }
        });
    }

    private void ensureCenterWidget() throws Exception {
        if (getWidget("center") == null) {
            ClassLoader classLoader = JavaInfoUtils.getClassLoader(this);
            Object panel;
            {
                Class<?> panelClass = classLoader.loadClass("com.gwtext.client.widgets.Panel");
                panel = ReflectionUtils.getConstructor(panelClass, String.class, String.class).newInstance("Center", "No widget with CENTER position.");
            }
            Object data;
            {
                Class<?> dataClass = classLoader.loadClass("com.gwtext.client.widgets.layout.BorderLayoutData");
                Class<?> regionClass = classLoader.loadClass("com.gwtext.client.core.RegionPosition");
                Object centerRegion = ReflectionUtils.getFieldObject(regionClass, "CENTER");
                data = ReflectionUtils.getConstructor(dataClass, regionClass).newInstance(centerRegion);
            }
            ReflectionUtils.invokeMethod(getContainer().getObject(), "add(com.gwtext.client.widgets.Component,com.gwtext.client.widgets.layout.LayoutData)", panel, data);
        }
    }
}
