package jdbframework.tags;

import jdbframework.tags.property.TagLovparameterProperty;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.util.*;
import jdbframework.common.GeneralSettings;
import jdbframework.common.JavaScriptList;

public class LovTag extends BodyTagSupport {

    private String property = null;

    private String module = null;

    private String control = null;

    private String top = "0";

    private String left = "0";

    private String width = "500";

    private String height = "400";

    private Vector lovparamFieldList = null;

    public void setProperty(String property) {
        this.property = property;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void addLovparamItem(TagLovparameterProperty lovparamFields) {
        if (lovparamFieldList == null) {
            lovparamFieldList = new Vector();
        }
        lovparamFieldList.add(lovparamFields);
    }

    public int doStartTag() throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    public int doEndTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        JspWriter out = pageContext.getOut();
        TagLovparameterProperty lovParam;
        String urlParam = "?";
        if (lovparamFieldList != null) {
            for (int i = 0; i < lovparamFieldList.size(); i++) {
                lovParam = (TagLovparameterProperty) lovparamFieldList.get(i);
                urlParam = urlParam.concat(lovParam.getProperty() + "=\"" + "+document.getElementById(\"" + lovParam.getValue() + "\").value" + "+\"&");
            }
        }
        String HtmlLov = "";
        HtmlLov = HtmlLov.concat("<img id=\"" + this.property + "\" name=\"" + this.property + "\" src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_LOV + "\" ");
        HtmlLov = HtmlLov.concat("onMouseDown=\"this.src='" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_LOV_DOWN + "'\"");
        HtmlLov = HtmlLov.concat("onMouseUp=\"this.src='" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_LOV + "'\"");
        HtmlLov = HtmlLov.concat("onMouseOut=\"this.src='" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_LOV + "'\"");
        HtmlLov = HtmlLov.concat("onClick=\"showlov_" + GeneralSettings.replace(this.property, ".", "_") + "()\"/>\n");
        String javaScript = "";
        javaScript += "<script>";
        javaScript += "function showlov_" + GeneralSettings.replace(this.property, ".", "_") + "(){";
        javaScript += GeneralSettings.replace(this.property, ".", "_") + "_window = window.open(\"" + this.module + urlParam + "\", \"lov\", \"left=" + this.left + ",top=" + this.top + ",width=" + this.width + ",height=" + this.height + ",menu=no\");";
        javaScript += GeneralSettings.replace(this.property, ".", "_") + "_window.focus();";
        javaScript += "return false;}\n";
        javaScript += "document.getElementById('" + this.control + "').parentNode.parentNode.getElementsByTagName('TD')[1].appendChild(document.getElementById('" + this.property + "'));";
        javaScript += "document.getElementById('" + this.control + "').style.width = parseInt(document.getElementById('" + this.control + "').style.width) - 16;";
        javaScript += "</script>";
        JavaScriptList javaScriptList = (JavaScriptList) request.getAttribute(GeneralSettings.TMP_JAVA_SCRIPT_LIST);
        javaScriptList.addScript(javaScript);
        try {
            out.println(HtmlLov);
        } catch (java.io.IOException io) {
        } finally {
            lovparamFieldList = null;
        }
        return EVAL_PAGE;
    }
}
