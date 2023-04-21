package net.simpleframework.web.page.component.ui.propeditor;

import java.util.Collection;
import java.util.Map;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.EJavascriptEvent;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PropEditorRender extends AbstractComponentJavascriptRender {

    public PropEditorRender(final IComponentRegistry componentRegistry) {
        super(componentRegistry);
    }

    @Override
    public String getJavascriptCode(final ComponentParameter compParameter) {
        final PropEditorBean formEditor = (PropEditorBean) compParameter.componentBean;
        Collection<PropField> propFields = null;
        final IPropEditorHandle peHandle = (IPropEditorHandle) compParameter.getComponentHandle();
        if (peHandle != null) {
            propFields = peHandle.getFormFields(compParameter);
        }
        if (propFields == null) {
            propFields = formEditor.getFormFields();
        }
        final StringBuilder sb = new StringBuilder();
        final String actionFunc = formEditor.getActionFunction();
        sb.append(ComponentRenderUtils.initContainerVar(compParameter));
        sb.append(actionFunc).append(".formEditor = new $Comp.FormEditor(").append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
        int i = 0;
        for (final PropField field : propFields) {
            if (i++ > 0) {
                sb.append(",");
            }
            sb.append("{");
            final String label = field.getLabel();
            if (StringUtils.hasText(label)) {
                sb.append("label: \"").append(label).append("\",");
            }
            final String labelStyle = field.getLabelStyle();
            if (StringUtils.hasText(labelStyle)) {
                sb.append("labelStyle: \"").append(labelStyle).append("\",");
            }
            final String description = field.getDescription();
            if (StringUtils.hasText(description)) {
                sb.append("desc: \"").append(JavascriptUtils.escape(description)).append("\",");
            }
            sb.append("components: ").append(getComponents(field));
            sb.append("}");
        }
        sb.append("], {");
        final String title = formEditor.getTitle();
        if (StringUtils.hasText(title)) {
            sb.append("title: \"").append(JavascriptUtils.escape(title)).append("\",");
        }
        sb.append("titleToggle: ").append(formEditor.isTitleToggle()).append(",");
        sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
        sb.append("onLoaded: function() {");
        final String callback = formEditor.getJsLoadedCallback();
        if (StringUtils.hasText(callback)) {
            sb.append(callback);
        }
        sb.append("}");
        sb.append("});");
        return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
    }

    private String getComponents(final PropField field) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        for (final FieldComponent comp : field.getFieldComponents()) {
            if (i++ > 0) {
                sb.append(",");
            }
            sb.append("{");
            final String name = comp.getName();
            if (StringUtils.hasText(name)) {
                sb.append("name: \"").append(name).append("\",");
            }
            final String style = comp.getStyle();
            if (StringUtils.hasText(style)) {
                sb.append("style: \"").append(style).append("\",");
            }
            final String[] attributes = StringUtils.split(comp.getAttributes(), ";");
            if (attributes != null && attributes.length > 0) {
                sb.append("attributes: {");
                int j = 0;
                for (final String attribute : attributes) {
                    final String[] arr = StringUtils.split(attribute, ":");
                    if (arr != null && arr.length > 0) {
                        if (j++ > 0) {
                            sb.append(",");
                        }
                        sb.append(arr[0]).append(": \"");
                        sb.append(arr.length == 1 ? arr[0] : arr[1]);
                        sb.append("\"");
                    }
                }
                sb.append("},");
            }
            final String defaultValue = comp.getDefaultValue();
            if (StringUtils.hasText(defaultValue)) {
                sb.append("defaultValue: \"");
                sb.append(JavascriptUtils.escape(defaultValue)).append("\",");
            }
            sb.append("type: \"").append(comp.getType()).append("\",");
            sb.append("events: {");
            int j = 0;
            for (final Map.Entry<EJavascriptEvent, String> entry : comp.getEventCallback().entrySet()) {
                if (j++ > 0) {
                    sb.append(",");
                }
                sb.append(entry.getKey()).append(": \"");
                sb.append(JavascriptUtils.escape(entry.getValue()));
                sb.append("\"");
            }
            sb.append("}");
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }
}
