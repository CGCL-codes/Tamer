package gnu.beanfactory.taglib;

import gnu.beanfactory.*;
import gnu.beanfactory.servlet.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.util.*;
import gnu.beanfactory.taglib.format.*;

public abstract class BaseTag extends TagSupport {

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(BaseTag.class);

    java.text.Format myFormatBean = new ToStringFormat();

    java.text.Format getFormatBean() {
        return myFormatBean;
    }

    public String myFormatUrl;

    String getFormatUrl() {
        return myFormatUrl;
    }

    public void setFormatUrl(String u) {
        try {
            myFormatBean = (java.text.Format) Container.getBeanContext().lookup(expand(u));
            if (myFormatBean == null) {
                myFormatBean = new ToStringFormat();
            }
        } catch (Exception e) {
            log.warn("formatUrl not valid: " + u);
            myFormatBean = new ToStringFormat();
        }
    }

    String format(Object input) {
        try {
            return getFormatBean().format(input);
        } catch (NullPointerException e) {
            return null;
        }
    }

    String myWidth;

    public String getWidth() {
        return myWidth;
    }

    public void setWidth(String s) {
        myWidth = s;
    }

    String myHeight;

    public String getHeight() {
        return myHeight;
    }

    public void setHeight(String s) {
        myHeight = s;
    }

    String myProperty;

    public String getProperty() {
        return myProperty;
    }

    public void setProperty(String t) {
        myProperty = t;
    }

    String myMethod;

    public final String getMethod() {
        return myMethod;
    }

    public final void setMethod(String t) {
        myMethod = expand(t);
    }

    String myValue;

    public final void setValue(String s) {
        myValue = expand(s);
    }

    public final String getValue() {
        return myValue;
    }

    final String getDereferencedUrl() throws BeanFactoryException {
        return dereference(getUrl());
    }

    final String getDereferencedValue() throws BeanFactoryException {
        return dereference(getValue());
    }

    public static final String dereference(String input) throws BeanFactoryException {
        try {
            Object target = null;
            BeanContext ctx = Container.getBeanContext();
            if (isReference(input)) {
                target = ctx.resolve(input);
            } else {
                target = input;
            }
            if (target != null) {
                return target.toString();
            } else {
                return "";
            }
        } catch (NestedNullPointerException e) {
            return "";
        }
    }

    String myUrlx;

    public String getUrl() {
        return myUrlx;
    }

    public void setUrl(String urn) {
        myUrlx = expand(urn);
    }

    public String expand(String urn) {
        if (urn == null) {
            return null;
        }
        String rval = urn;
        String beanPart = null;
        String propPart = null;
        String realUrl = null;
        if (urn != null && urn.startsWith("bean:") && (urn.indexOf("/") == -1)) {
            int idx = urn.indexOf(".");
            if (idx != -1) {
                beanPart = urn.substring(0, idx);
                propPart = urn.substring(idx + 1);
                realUrl = (String) getImportMap().get(beanPart);
            } else {
                realUrl = (String) getImportMap().get(urn);
            }
            if (realUrl != null) {
                rval = realUrl;
                if (propPart != null) {
                    rval = rval + "." + propPart;
                }
            }
        }
        return rval;
    }

    String priority = "0";

    public String getPriority() {
        return priority;
    }

    public void setPriority(String s) {
        priority = s;
    }

    public final boolean isNotEmpty(String s) {
        return (s != null && s.length() > 0);
    }

    public static final boolean isReference(String input) {
        if (input != null && input.startsWith("bean:")) {
            return true;
        }
        return false;
    }

    /**
     * @deprecated Use getMangledMethodString() instead
     **/
    final String getMethodString() {
        return getMangledMethodString();
    }

    final String getMangledMethodString() {
        return "!" + getPriority() + ":" + getMethod();
    }

    /**
     * @deprecated Use getMangledPropertyString() instead
     **/
    final String getPropertyString() {
        return getMangledPropertyString();
    }

    final String getMangledPropertyString() {
        return "$" + getPriority() + ":" + getUrl();
    }

    String myClass;

    public String getStyleClass() {
        return myClass;
    }

    public void setStyleClass(String s) {
        myClass = s;
    }

    String myBorder;

    public String getBorder() {
        return myBorder;
    }

    public void setBorder(String b) {
        myBorder = b;
    }

    String mySize;

    public String getSize() {
        return mySize;
    }

    public void setSize(String size) {
        mySize = size;
    }

    String myMaxlength;

    public String getMaxlength() {
        return myMaxlength;
    }

    public void setMaxlength(String maxlength) {
        myMaxlength = maxlength;
    }

    String myMultiple;

    public String getMultiple() {
        return myMultiple;
    }

    public void setMultiple(String multiple) {
        myMultiple = multiple;
    }

    String myCols;

    public void setCols(String c) {
        myCols = c;
    }

    public String getCols() {
        return myCols;
    }

    String myRows;

    public void setRows(String r) {
        myRows = r;
    }

    public String getRows() {
        return myRows;
    }

    String myAlt;

    public void setAlt(String alt) {
        myAlt = alt;
    }

    public String getAlt() {
        return myAlt;
    }

    String myTabindex;

    public String getTabindex() {
        return myTabindex;
    }

    public void setTabindex(String ti) {
        myTabindex = ti;
    }

    String myOnmousedown;

    public String getOnmousedown() {
        return myOnmousedown;
    }

    public void setOnmousedown(String s) {
        myOnmousedown = s;
    }

    String myOnmouseup;

    public String getOnmouseup() {
        return myOnmouseup;
    }

    public void setOnmouseup(String s) {
        myOnmouseup = s;
    }

    String myOnmouseover;

    public String getOnmouseover() {
        return myOnmouseover;
    }

    public void setOnmouseover(String s) {
        myOnmouseover = s;
    }

    String myOnmousemove;

    public String getOnmousemove() {
        return myOnmousemove;
    }

    public void setOnmousemove(String s) {
        myOnmousemove = s;
    }

    String myOnmouseout;

    public String getOnmouseout() {
        return myOnmouseout;
    }

    public void setOnmouseout(String s) {
        myOnmouseout = s;
    }

    String myOnclick;

    public String getOnclick() {
        return myOnclick;
    }

    public void setOnclick(String s) {
        myOnclick = s;
    }

    String myOndblclick;

    public String getOndblclick() {
        return myOndblclick;
    }

    public void setOndblclick(String s) {
        myOndblclick = s;
    }

    public String myOnkeypress;

    public String getOnkeypress() {
        return myOnkeypress;
    }

    public void setOnkeypress(String s) {
        myOnkeypress = s;
    }

    public String myOnkeydown;

    public String getOnkeydown() {
        return myOnkeydown;
    }

    public void setOnkeydown(String s) {
        myOnkeydown = s;
    }

    public String myOnkeyup;

    public String getOnkeyup() {
        return myOnkeyup;
    }

    public void setOnkeyup(String s) {
        myOnkeyup = s;
    }

    String myOnblur;

    public String getOnblur() {
        return myOnblur;
    }

    public void setOnblur(String k) {
        myOnblur = k;
    }

    String myAccesskey;

    public String getAccesskey() {
        return myAccesskey;
    }

    public void setAccesskey(String k) {
        myAccesskey = k;
    }

    String myOnchange;

    public String getOnchange() {
        return myOnchange;
    }

    public void setOnchange(String k) {
        myOnchange = k;
    }

    String myOnfocus;

    public String getOnfocus() {
        return myOnfocus;
    }

    public void setOnfocus(String k) {
        myOnfocus = k;
    }

    String myOnselect;

    public String getOnselect() {
        return myOnselect;
    }

    public void setOnselect(String k) {
        myOnselect = k;
    }

    String myId;

    public String getId() {
        return myId;
    }

    public void setId(String id) {
        myId = id;
    }

    String myStyle;

    public String getStyle() {
        return myStyle;
    }

    public void setStyle(String s) {
        myStyle = s;
    }

    public void writeDescriptiveAttributes() throws java.io.IOException {
        if (getWidth() != null) writeAttribute("width", getWidth());
        if (getHeight() != null) writeAttribute("height", getHeight());
        if (getStyleClass() != null) writeAttribute("class", getStyleClass());
        if (getBorder() != null) {
            writeAttribute("border", getBorder());
        }
        if (getSize() != null) {
            writeAttribute("size", getSize());
        }
        if (getMaxlength() != null) {
            writeAttribute("maxlength", getMaxlength());
        }
        if (getMultiple() != null) {
            writeAttribute("multiple", getMultiple());
        }
        if (getRows() != null) {
            writeAttribute("rows", getRows());
        }
        if (getCols() != null) {
            writeAttribute("cols", getCols());
        }
        if (getAlt() != null) {
            writeAttribute("alt", getAlt());
        }
        if (getTabindex() != null) {
            writeAttribute("tabindex", getTabindex());
        }
        if (getAccesskey() != null) {
            writeAttribute("accesskey", getAccesskey());
        }
        if (getOnfocus() != null) {
            writeAttribute("onfocus", getOnfocus());
        }
        if (getOnblur() != null) {
            writeAttribute("onblur", getOnblur());
        }
        if (getOnselect() != null) {
            writeAttribute("onselect", getOnselect());
        }
        if (getOnchange() != null) {
            writeAttribute("onchange", getOnchange());
        }
        if (getOnclick() != null) {
            writeAttribute("onclick", getOnclick());
        }
        if (getOndblclick() != null) {
            writeAttribute("ondblclick", getOndblclick());
        }
        if (getOnkeyup() != null) {
            writeAttribute("onkeyup", getOnkeyup());
        }
        if (getOnkeydown() != null) {
            writeAttribute("onkeydown", getOnkeydown());
        }
        if (getOnkeypress() != null) {
            writeAttribute("onkeypress", getOnkeypress());
        }
        if (getOnclick() != null) {
            writeAttribute("ondblclick", getOndblclick());
        }
        if (getOnmousedown() != null) {
            writeAttribute("onmousedown", getOnmousedown());
        }
        if (getOnmouseup() != null) {
            writeAttribute("onmouseup", getOnmouseup());
        }
        if (getOnmouseover() != null) {
            writeAttribute("onmouseover", getOnmouseover());
        }
        if (getOnmousemove() != null) {
            writeAttribute("onmousemove", getOnmousemove());
        }
        if (getOnmouseout() != null) {
            writeAttribute("onmouseout", getOnmouseout());
        }
        if (getId() != null) {
            writeAttribute("id", getId());
        }
        if (getStyle() != null) {
            writeAttribute("style", getStyle());
        }
    }

    public void writeAttribute(String key, String val) throws java.io.IOException {
        writeAttribute(key, val, false);
    }

    public void writeAttribute(String key, String val, boolean escape) throws java.io.IOException {
        JspWriter out = pageContext.getOut();
        out.print(" ");
        out.print(key);
        out.print("=\"");
        if (escape == true) {
            out.print(gnu.beanfactory.util.Regex.escapeHtmlAttribute(val));
        } else {
            out.print(val);
        }
        out.print("\" ");
    }

    public void writeFingerprint(DigestSecurity ds) throws java.io.IOException {
        JspWriter out = pageContext.getOut();
        out.print("<input type=\"hidden\" name=\"");
        out.print(DigestSecurity.FINGERPRINT_KEY);
        out.print("\" value=\"");
        out.print(ds.getDigest());
        out.print("\">");
    }

    public synchronized Map getImportMap() {
        Map map = (Map) pageContext.getAttribute(IMPORT_MAP_KEY);
        if (map == null) {
            map = new HashMap();
            pageContext.setAttribute(IMPORT_MAP_KEY, map);
        }
        return map;
    }

    public static String IMPORT_MAP_KEY = "__IMPORT_MAP";
}
