package org.jenia.faces.template.component;

import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jenia.faces.template.handler.ViewHandler;
import org.jenia.faces.util.Util;

public class UIMainViewContent extends HtmlForm {

    private String realId;

    private String onAfterLoading;

    private String onBeforeLoading;

    public static final String COMPONENT_TYPE = "org.jenia.faces.template.MainViewContent";

    public UIMainViewContent() {
        super();
    }

    public static String getCOMPONENT_TYPE() {
        return COMPONENT_TYPE;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[4];
        _values[0] = super.saveState(_context);
        _values[1] = realId;
        _values[2] = onBeforeLoading;
        _values[3] = onAfterLoading;
        return _values;
    }

    @Override
    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        realId = (String) _values[1];
        onBeforeLoading = (String) _values[2];
        onAfterLoading = (String) _values[3];
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) return;
        UIIncludeMainView imv = (UIIncludeMainView) getParent();
        String onSubmit = getOnsubmit();
        if (imv.getAjaxed()) {
            String obl = onBeforeLoading != null ? Util.escapeJSText(onBeforeLoading) : "";
            String oal = onAfterLoading != null ? Util.escapeJSText(onAfterLoading) : "";
            String suf = "return performj4fajaxrequest('" + realId + "','" + realId + "_form','" + obl + "','" + oal + "');";
            if (onSubmit == null || !onSubmit.endsWith(suf)) setOnsubmit((onSubmit != null ? onSubmit : "") + suf);
        }
        super.encodeBegin(context);
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        if (!isRendered()) return;
        UIIncludeMainView imv = (UIIncludeMainView) getParent();
        ResponseWriter rw = Util.getFacesContext().getResponseWriter();
        UIComponent cb = imv.getFacet("beforeInclude");
        if (cb != null) {
            if (!((UIComponent) getChildren().get(0)).getId().equals(cb.getId())) getChildren().add(0, cb);
        }
        UIComponent cb2 = imv.getFacet("afterInclude");
        if (cb2 != null) {
            if (!((UIComponent) getChildren().get(getChildren().size() - 1)).getId().equals(cb2.getId())) getChildren().add(cb2);
        }
        super.encodeChildren(context);
        rw.startElement("input", null);
        rw.writeAttribute("type", "hidden", null);
        rw.writeAttribute("name", ViewHandler._J4FAJAX, null);
        if (imv.getAjaxed()) rw.writeAttribute("value", realId, null); else rw.writeAttribute("value", realId + ViewHandler._OUTSIDE_AJAX, null);
        rw.endElement("input");
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        if (!isRendered()) return;
        if (!context.getExternalContext().getRequestServletPath().replaceAll("\\.jsf", ".jsp").equals(context.getViewRoot().getViewId().replaceAll("\\.jsf", ".jsp")) || Util.getRequestAttribute(ViewHandler._INSIDE_AJAX) != null) {
            ResponseWriter rw = Util.getFacesContext().getResponseWriter();
            rw.startElement("script", this);
            rw.writeAttribute("language", "JavaScript", null);
            rw.writeAttribute("type", "text/javascript", null);
            rw.write("jeniaTemplateOnLoad" + realId + "=window.onload;\n");
            rw.write("window.onload=function() {\n");
            rw.write("Element.scrollTo(document.body);\n");
            rw.write("if (jeniaTemplateOnLoad" + realId + "!=null)");
            rw.write("jeniaTemplateOnLoad" + realId + "();\n");
            rw.write("window.onload=jeniaTemplateOnLoad" + realId + ";\n");
            rw.write("}\n");
            rw.endElement("script");
        }
        super.encodeEnd(context);
    }

    @Override
    public void setId(String id) {
        if (id.endsWith("_form")) realId = id.substring(0, id.indexOf("_form")); else realId = id;
        super.setId(realId + "_form");
    }

    public String getOnAfterLoading() {
        if (null != this.onAfterLoading) {
            return this.onAfterLoading;
        }
        ValueExpression _vb = getValueExpression("onAfterLoading");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setOnAfterLoading(String onAfterLoading) {
        this.onAfterLoading = onAfterLoading;
    }

    public String getOnBeforeLoading() {
        if (null != this.onBeforeLoading) {
            return this.onBeforeLoading;
        }
        ValueExpression _vb = getValueExpression("onBeforeLoading");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setOnBeforeLoading(String onBeforeLoading) {
        this.onBeforeLoading = onBeforeLoading;
    }
}
