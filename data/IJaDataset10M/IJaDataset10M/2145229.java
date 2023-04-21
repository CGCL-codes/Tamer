package org.apache.myfaces.shared_impl.taglib.html;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author: cagatay $)
 * @version $Revision: 606793 $ $Date: 2007-12-25 10:20:46 -0500 (Tue, 25 Dec 2007) $
 * @deprecated use {@link HtmlMessagesELTagBase} instead
 */
public abstract class HtmlMessagesTagBase extends org.apache.myfaces.shared_impl.taglib.html.HtmlComponentTagBase {

    private String _showSummary;

    private String _showDetail;

    private String _globalOnly;

    private String _infoClass;

    private String _infoStyle;

    private String _warnClass;

    private String _warnStyle;

    private String _errorClass;

    private String _errorStyle;

    private String _fatalClass;

    private String _fatalStyle;

    private String _layout;

    private String _tooltip;

    public void release() {
        super.release();
        _showSummary = null;
        _showDetail = null;
        _globalOnly = null;
        _infoClass = null;
        _infoStyle = null;
        _warnClass = null;
        _warnStyle = null;
        _errorClass = null;
        _errorStyle = null;
        _fatalClass = null;
        _fatalStyle = null;
        _layout = null;
        _tooltip = null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setBooleanProperty(component, org.apache.myfaces.shared_impl.renderkit.JSFAttr.SHOW_SUMMARY_ATTR, _showSummary);
        setBooleanProperty(component, JSFAttr.SHOW_DETAIL_ATTR, _showDetail);
        setBooleanProperty(component, JSFAttr.GLOBAL_ONLY_ATTR, _globalOnly);
        setStringProperty(component, JSFAttr.INFO_CLASS_ATTR, _infoClass);
        setStringProperty(component, JSFAttr.INFO_STYLE_ATTR, _infoStyle);
        setStringProperty(component, JSFAttr.WARN_CLASS_ATTR, _warnClass);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.JSFAttr.WARN_STYLE_ATTR, _warnStyle);
        setStringProperty(component, JSFAttr.ERROR_CLASS_ATTR, _errorClass);
        setStringProperty(component, JSFAttr.ERROR_STYLE_ATTR, _errorStyle);
        setStringProperty(component, JSFAttr.FATAL_CLASS_ATTR, _fatalClass);
        setStringProperty(component, JSFAttr.FATAL_STYLE_ATTR, _fatalStyle);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.JSFAttr.LAYOUT_ATTR, _layout);
        setBooleanProperty(component, org.apache.myfaces.shared_impl.renderkit.JSFAttr.TOOLTIP_ATTR, _tooltip);
    }

    public void setShowSummary(String showSummary) {
        _showSummary = showSummary;
    }

    public void setShowDetail(String showDetail) {
        _showDetail = showDetail;
    }

    public void setGlobalOnly(String globalOnly) {
        _globalOnly = globalOnly;
    }

    public void setErrorClass(String errorClass) {
        _errorClass = errorClass;
    }

    public void setErrorStyle(String errorStyle) {
        _errorStyle = errorStyle;
    }

    public void setFatalClass(String fatalClass) {
        _fatalClass = fatalClass;
    }

    public void setFatalStyle(String fatalStyle) {
        _fatalStyle = fatalStyle;
    }

    public void setInfoClass(String infoClass) {
        _infoClass = infoClass;
    }

    public void setInfoStyle(String infoStyle) {
        _infoStyle = infoStyle;
    }

    public void setWarnClass(String warnClass) {
        _warnClass = warnClass;
    }

    public void setWarnStyle(String warnStyle) {
        _warnStyle = warnStyle;
    }

    public void setLayout(String layout) {
        _layout = layout;
    }

    public void setTooltip(String tooltip) {
        _tooltip = tooltip;
    }
}
