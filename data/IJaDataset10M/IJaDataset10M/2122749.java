package org.forzaframework.web.servlet.tags;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelItem;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 06-oct-2007
 * Time: 12:12:21
 * Description:
 */
public class WindowTag extends PanelTag implements Observable {

    private Boolean modal;

    private Boolean show = true;

    private List<Item> buttons = new ArrayList<Item>();

    private String onShow;

    public WindowTag() {
        setLayout("fit");
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public void setModal(Boolean modal) {
        this.modal = modal;
    }

    private Object topToolbar;

    public void setTopToolbar(Item item) {
        topToolbar = item.toJSON();
    }

    public void addButton(Item button) {
        buttons.add(button);
    }

    public String getOnShow() {
        return onShow;
    }

    public void setOnShow(String onShow) {
        this.onShow = onShow;
    }

    public void doInitBody() throws JspException {
        try {
            if (this.bodyContent != null) {
                setItems(new ArrayList<PanelItem>());
                setTools(new ArrayList<PanelItem>());
                buttons = new ArrayList<Item>();
                topToolbar = null;
                StringBuffer sb = new StringBuffer();
                sb.append("<div id=\"").append(id).append("\" class=\"x-hidden\">");
                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        try {
            if (this.bodyContent != null) {
                StringBuffer sb = new StringBuffer();
                sb.append("</div>");
                sb.append("<script type=\"text/javascript\">\n");
                sb.append("Ext.onReady(function(){\n");
                sb.append("if(Ext.ComponentMgr.get('").append(id).append("')){");
                sb.append("Ext.ComponentMgr.get('").append(id).append("').destroy();");
                sb.append("}");
                sb.append("var win = new Ext.Window(\n");
                JSONObject config = new JSONObject();
                config.put("id", id);
                config.put("el", id);
                config.put("layout", getLayout());
                config.put("title", getTitle() != null ? getTitle() : getText(getTitleKey()));
                config.elementOpt("modal", modal);
                config.put("width", Integer.valueOf(getWidth()));
                config.put("height", Integer.valueOf(getHeight()));
                config.put("shadow", false);
                config.put("plain", false);
                config.elementOpt("border", getBorder());
                config.elementOpt("bodyBorder", getBodyBorder());
                config.put("minWidth", 100);
                config.put("minHeight", 100);
                config.put("proxyDrag", true);
                if (buttons.size() > 0) {
                    config.put("defaultButton", 0);
                    config.put("keys", new JSONFunction("{key:13,fn:function(){ win.buttons[0].handler.call(); },scope:win}"));
                }
                config.elementOpt("tbar", topToolbar);
                if (getItems().size() > 0) {
                    JSONArray jsonItems = new JSONArray();
                    for (PanelItem item : getItems()) {
                        jsonItems.add(item.toJSON());
                    }
                    config.put("items", jsonItems);
                }
                if (getTools().size() > 0) {
                    JSONArray jsonItems = new JSONArray();
                    for (PanelItem item : getTools()) {
                        jsonItems.add(item.toJSON());
                    }
                    config.put("tools", jsonItems);
                }
                if (buttons.size() > 0) {
                    JSONArray jsonItems = new JSONArray();
                    for (Item button : buttons) {
                        jsonItems.add(button.toJSON());
                    }
                    config.put("buttons", jsonItems);
                }
                if (this.listeners.size() > 0 || StringUtils.isNotBlank(onShow)) {
                    JSONObject listeners = new JSONObject();
                    for (Listener listener : this.listeners) {
                        listeners.put(listener.getEventName(), new JSONFunction(listener.getHandler()));
                    }
                    if (StringUtils.isNotBlank(onShow)) {
                        listeners.put("show", new JSONFunction(onShow));
                    }
                    config.put("listeners", listeners);
                }
                sb.append(config.toString(2)).append(");");
                if (show) {
                    sb.append("win.show();\n");
                }
                sb.append("});");
                sb.append("</script>\n");
                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);
                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }
}
