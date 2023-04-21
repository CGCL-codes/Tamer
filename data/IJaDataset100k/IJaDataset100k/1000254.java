package org.jenia.faces.datatools.component.html;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.jenia.faces.datatools.component.UIDataTools;

/**
 * @author Andrea Tessaro Porta
 */
public class HtmlMatrixController extends UIDataTools {

    public static final String COMPONENT_TYPE = "org.jenia.faces.datatools.HtmlMatrixController";

    public static final String RENDERER_TYPE = "org.jenia.faces.datatools.MatrixController";

    private String accesskey;

    private String dir;

    private String lang;

    private String tabindex;

    private String title;

    private String style;

    private String styleClass;

    private String forId;

    public HtmlMatrixController() {
        super();
        setRendererType(RENDERER_TYPE);
    }

    public java.lang.String getAccesskey() {
        if (null != this.accesskey) {
            return this.accesskey;
        }
        ValueExpression _vb = getValueExpression("accesskey");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setAccesskey(java.lang.String accesskey) {
        this.accesskey = accesskey;
    }

    public java.lang.String getDir() {
        if (null != this.dir) {
            return this.dir;
        }
        ValueExpression _vb = getValueExpression("dir");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setDir(java.lang.String dir) {
        this.dir = dir;
    }

    public java.lang.String getLang() {
        if (null != this.lang) {
            return this.lang;
        }
        ValueExpression _vb = getValueExpression("lang");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setLang(java.lang.String lang) {
        this.lang = lang;
    }

    public java.lang.String getTabindex() {
        if (null != this.tabindex) {
            return this.tabindex;
        }
        ValueExpression _vb = getValueExpression("tabindex");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setTabindex(java.lang.String tabindex) {
        this.tabindex = tabindex;
    }

    public java.lang.String getTitle() {
        if (null != this.title) {
            return this.title;
        }
        ValueExpression _vb = getValueExpression("title");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public String getStyle() {
        if (null != this.style) {
            return this.style;
        }
        ValueExpression _vb = getValueExpression("style");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        if (null != this.styleClass) {
            return this.styleClass;
        }
        ValueExpression _vb = getValueExpression("styleClass");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getFor() {
        if (null != this.forId) {
            return this.forId;
        }
        ValueExpression _vb = getValueExpression("for");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setFor(String forId) {
        this.forId = forId;
    }

    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[9];
        _values[0] = super.saveState(_context);
        _values[1] = accesskey;
        _values[2] = lang;
        _values[3] = dir;
        _values[4] = tabindex;
        _values[5] = title;
        _values[6] = style;
        _values[7] = styleClass;
        _values[8] = forId;
        return _values;
    }

    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        accesskey = (String) _values[1];
        lang = (String) _values[2];
        dir = (String) _values[3];
        tabindex = (String) _values[4];
        title = (String) _values[5];
        style = (String) _values[6];
        styleClass = (String) _values[7];
        forId = (String) _values[8];
    }
}
