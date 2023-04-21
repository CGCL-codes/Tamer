package com.google.gdt.eclipse.designer.gwtext.model.widgets;

import com.google.common.collect.Maps;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.core.model.JavaInfo;
import org.eclipse.wb.core.model.broadcast.JavaEventListener;
import org.eclipse.wb.internal.core.model.JavaInfoUtils;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.model.util.ScriptUtils;
import org.eclipse.wb.internal.core.model.util.StackContainerSupport;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;
import org.eclipse.wb.internal.core.utils.jdt.core.CodeUtils;
import org.eclipse.wb.internal.core.utils.reflect.ReflectionUtils;
import java.util.List;
import java.util.Map;

/**
 * Model for <code>TabPanel</code>.
 * 
 * @author scheglov_ke
 * @coverage GWTExt.model
 */
public final class TabPanelInfo extends ContainerInfo {

    private final StackContainerSupport<WidgetInfo> m_stackContainer = new StackContainerSupport<WidgetInfo>(this) {

        @Override
        protected List<WidgetInfo> getChildren() {
            return getChildrenWidgets();
        }
    };

    public TabPanelInfo(AstEditor editor, ComponentDescription description, CreationSupport creationSupport) throws Exception {
        super(editor, description, creationSupport);
        addBroadcastListener(new JavaEventListener() {

            @Override
            public void bindComponents(List<JavaInfo> components) throws Exception {
                removeBroadcastListener(this);
                ensureTabsRendered();
            }
        });
    }

    @Override
    protected void refresh_afterCreate() throws Exception {
        super.refresh_afterCreate();
        ReflectionUtils.invokeMethod(getObject(), "setActiveTab(int)", getActiveIndex());
    }

    /**
   * We ask elements for all components, but <code>TabPanel</code> renders component only when its
   * tab becomes visible. So, we should force rendering.
   */
    private void ensureTabsRendered() throws Exception {
        ClassLoader classLoader = JavaInfoUtils.getClassLoader(this);
        String script = CodeUtils.getSource("count = tabPanel.getItems().length;", "for (i = 0; i < count; i++) {", "  tabPanel.setActiveTab(i);", "}");
        Map<String, Object> variables = Maps.newTreeMap();
        variables.put("tabPanel", getObject());
        ScriptUtils.evaluate(classLoader, script, variables);
    }

    public int getActiveIndex() {
        WidgetInfo activeWidget = m_stackContainer.getActive();
        return activeWidget != null ? getChildrenWidgets().indexOf(activeWidget) : 0;
    }
}
