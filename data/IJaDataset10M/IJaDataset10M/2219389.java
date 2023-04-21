package net.simpleframework.web.page.component.ui.listbox;

import java.util.Collection;
import java.util.Map;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.AbstractNodeUIBean;
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
public class ListboxRender extends AbstractComponentJavascriptRender {

    public static final String EVENT_CLICK = "__click";

    public static final String EVENT_DBLCLICK = "__dblclick";

    public ListboxRender(final IComponentRegistry componentRegistry) {
        super(componentRegistry);
    }

    @Override
    public String getJavascriptCode(final ComponentParameter compParameter) {
        final ListboxBean listboxBean = (ListboxBean) compParameter.componentBean;
        Collection<ListItem> listItems = null;
        final IListboxHandle lHandle = (IListboxHandle) compParameter.getComponentHandle();
        if (lHandle != null) {
            listItems = lHandle.getListItems(compParameter);
        }
        if (listItems == null) {
            listItems = listboxBean.getListItems();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ComponentRenderUtils.initContainerVar(compParameter));
        final String actionFunc = listboxBean.getActionFunction();
        sb.append(actionFunc).append(".").append(eventClick(EVENT_CLICK, listboxBean.getJsClickCallback(), actionFunc));
        sb.append(actionFunc).append(".").append(eventClick(EVENT_DBLCLICK, listboxBean.getJsDblclickCallback(), actionFunc));
        sb.append(actionFunc).append(".listbox = new $Comp.ListBox(").append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
        int i = 0;
        for (final ListItem listItem : listItems) {
            if (i++ > 0) {
                sb.append(",");
            }
            sb.append("{");
            final String text = listItem.getText();
            if (StringUtils.hasText(text)) {
                sb.append("text: \"").append(JavascriptUtils.escape(text)).append("\",");
            }
            final String tip = listItem.getTip();
            if (StringUtils.hasText(tip)) {
                sb.append("tip: \"").append(JavascriptUtils.escape(tip)).append("\",");
            }
            if (lHandle != null) {
                final Map<String, Object> attributes = lHandle.getListItemAttributes(compParameter, listItem);
                if (attributes != null && attributes.size() > 0) {
                    sb.append("\"attributes\": ").append(JSONUtils.toJSON(attributes)).append(",");
                }
            }
            sb.append(getUIEventData(listItem, actionFunc));
            sb.append("id: \"").append(listItem.getId()).append("\"");
            sb.append("}");
        }
        sb.append("], {");
        sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
        sb.append("checkbox: ").append(listboxBean.isCheckbox()).append(",");
        sb.append("tooltip: ").append(listboxBean.isTooltip());
        sb.append("});");
        return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
    }

    private String getUIEventData(final AbstractNodeUIBean bean, final String actionFunc) {
        final StringBuilder sb = new StringBuilder();
        sb.append("click: ").append(actionFunc).append(".");
        sb.append(EVENT_CLICK).append(",");
        sb.append("dblclick: ").append(actionFunc).append(".");
        sb.append(EVENT_DBLCLICK).append(",");
        final String click = bean.getJsClickCallback();
        if (StringUtils.hasText(click)) {
            sb.append(EVENT_CLICK).append(": \"");
            sb.append(JavascriptUtils.escape(click)).append("\",");
        }
        final String dblclick = bean.getJsDblclickCallback();
        if (StringUtils.hasText(dblclick)) {
            sb.append(EVENT_DBLCLICK).append(": \"");
            sb.append(JavascriptUtils.escape(click)).append("\",");
        }
        return sb.toString();
    }

    protected String eventClick(final String event, final String defaultBody, final String actionFunc) {
        final StringBuilder sb = new StringBuilder();
        sb.append(event).append("=").append("function(item, ev) {");
        sb.append("var cb = function(id, text, item, json, ev) {");
        sb.append("var func = item.data.").append(event).append(";");
        sb.append("if (func) { eval(func); } else {");
        sb.append(StringUtils.blank(defaultBody)).append("}};");
        sb.append("cb(item.getId(), item.getText(), item, ").append(actionFunc).append(".json, ev); };");
        return sb.toString();
    }
}
