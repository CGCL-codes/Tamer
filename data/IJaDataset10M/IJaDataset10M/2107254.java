package org.nextframework.view;

import java.util.HashMap;
import java.util.Map;
import org.nextframework.util.Util;

/**
 * @author rogelgarcia
 * @since 30/01/2006
 * @version 1.1
 */
public class PanelTag extends BaseTag {

    protected Integer colspan;

    protected String title;

    protected Boolean propertyRenderAsDouble;

    protected String onSelectTab;

    public String getOnSelectTab() {
        return onSelectTab;
    }

    public void setOnSelectTab(String onSelectTab) {
        this.onSelectTab = onSelectTab;
    }

    public Boolean getPropertyRenderAsDouble() {
        return propertyRenderAsDouble;
    }

    public void setPropertyRenderAsDouble(Boolean propertyRenderAsDouble) {
        this.propertyRenderAsDouble = propertyRenderAsDouble;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void doComponent() throws Exception {
        BaseTag findFirst2 = findFirst2(AcceptPanelRenderedBlock.class, PanelTag.class, ColumnTag.class);
        if (findFirst2 != null && findFirst2 instanceof AcceptPanelRenderedBlock) {
            String body;
            if (getJspBody() != null) {
                body = getBody();
            } else {
                body = "";
            }
            Map<String, Object> attrs = new HashMap<String, Object>();
            attrs.putAll(getDynamicAttributesMap());
            if ("".equals(attrs.get("style"))) {
                attrs.remove("style");
            }
            if ("".equals(attrs.get("class"))) {
                attrs.remove("class");
            }
            if (colspan != null) {
                attrs.put("colspan", colspan);
            }
            if (title != null) {
                attrs.put("title", title);
            }
            if (onSelectTab != null) {
                attrs.put("onselecttab", onSelectTab);
            }
            if (id != null) {
                attrs.put("id", id);
            }
            PanelRenderedBlock renderedBlock = new PanelRenderedBlock();
            renderedBlock.setBody(body);
            renderedBlock.setProperties(attrs);
            AcceptPanelRenderedBlock acceptPanel = findParent2(AcceptPanelRenderedBlock.class, true);
            acceptPanel.addBlock(renderedBlock);
        } else {
            Object style = getDynamicAttributesMap().get("style");
            Object clazz = getDynamicAttributesMap().get("class");
            if (Util.objects.isNotEmpty(style) || Util.objects.isNotEmpty(clazz)) {
                getOut().print("<span");
                if (Util.objects.isNotEmpty(style)) {
                    getOut().print(" style=\"" + style + "\"");
                }
                if (Util.objects.isNotEmpty(clazz)) {
                    getOut().print(" class=\"" + clazz + "\"");
                }
                getOut().print(">");
            }
            doBody();
            if (Util.objects.isNotEmpty(style) || Util.objects.isNotEmpty(clazz)) {
                getOut().print("</span>");
            }
        }
    }
}
