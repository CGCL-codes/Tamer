package org.apache.myfaces.shared_impl.taglib.html;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author: cagatay $)
 * @author Martin Marinschek
 * @version $Revision: 606793 $ $Date: 2007-12-25 10:20:46 -0500 (Tue, 25 Dec 2007) $
 */
public abstract class HtmlSelectBooleanCheckboxELTagBase extends org.apache.myfaces.shared_impl.taglib.html.HtmlInputELTagBase {

    private ValueExpression _accesskey;

    private ValueExpression _alt;

    private ValueExpression _checked;

    private ValueExpression _datafld;

    private ValueExpression _datasrc;

    private ValueExpression _dataformatas;

    private ValueExpression _disabled;

    private ValueExpression _onblur;

    private ValueExpression _onchange;

    private ValueExpression _onfocus;

    private ValueExpression _onselect;

    private ValueExpression _readonly;

    private ValueExpression _tabindex;

    public void release() {
        super.release();
        _accesskey = null;
        _alt = null;
        _checked = null;
        _datafld = null;
        _datasrc = null;
        _dataformatas = null;
        _disabled = null;
        _onblur = null;
        _onchange = null;
        _onfocus = null;
        _onselect = null;
        _readonly = null;
        _tabindex = null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.ACCESSKEY_ATTR, _accesskey);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.ALT_ATTR, _alt);
        setBooleanProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.CHECKED_ATTR, _checked);
        setBooleanProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.DISABLED_ATTR, _disabled);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.ONCHANGE_ATTR, _onchange);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.ONFOCUS_ATTR, _onfocus);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.ONSELECT_ATTR, _onselect);
        setBooleanProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.READONLY_ATTR, _readonly);
        setStringProperty(component, org.apache.myfaces.shared_impl.renderkit.html.HTML.TABINDEX_ATTR, _tabindex);
    }

    public void setAccesskey(ValueExpression accesskey) {
        _accesskey = accesskey;
    }

    public void setAlt(ValueExpression alt) {
        _alt = alt;
    }

    public void setChecked(ValueExpression checked) {
        _checked = checked;
    }

    public void setDatafld(ValueExpression datafld) {
        _datafld = datafld;
    }

    public void setDatasrc(ValueExpression datasrc) {
        _datasrc = datasrc;
    }

    public void setDataformatas(ValueExpression dataformatas) {
        _dataformatas = dataformatas;
    }

    public void setDisabled(ValueExpression disabled) {
        _disabled = disabled;
    }

    public void setOnblur(ValueExpression onblur) {
        _onblur = onblur;
    }

    public void setOnchange(ValueExpression onchange) {
        _onchange = onchange;
    }

    public void setOnfocus(ValueExpression onfocus) {
        _onfocus = onfocus;
    }

    public void setOnselect(ValueExpression onselect) {
        _onselect = onselect;
    }

    public void setReadonly(ValueExpression readonly) {
        _readonly = readonly;
    }

    public void setTabindex(ValueExpression tabindex) {
        _tabindex = tabindex;
    }
}
