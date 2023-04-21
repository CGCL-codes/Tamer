package org.sss.presentation.faces.custom.ajaxinput;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import org.sss.presentation.faces.FacesUtils;
import org.sss.presentation.faces.common.spi.AbstractAjaxTag;

/**
 * AjaxInput的标签处理类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 126 $ $Date: 2008-03-02 09:21:01 -0500 (Sun, 02 Mar 2008) $
 */
public class AjaxInputTag extends AbstractAjaxTag {

    public static final String COMPONENT_TYPE = "org.sss.faces.AjaxInput";

    public static final String TAG_TYPE = "type";

    public static final String TAG_PATTERN = "pattern";

    public static final String TAG_CODENAME = "codeName";

    public static final String TAG_REQUIRED = "required";

    private String _type = null;

    private String _pattern = null;

    private ValueExpression _codeName = null;

    private ValueExpression _required = null;

    public void setType(String type) {
        this._type = type;
    }

    public void setPattern(String pattern) {
        this._pattern = pattern;
    }

    public void setCodeName(ValueExpression codeName) {
        this._codeName = codeName;
    }

    public void setRequired(ValueExpression required) {
        this._required = required;
    }

    @Override
    public String getComponentType() {
        return AjaxInputTag.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return FacesUtils.getRendererType(AjaxInputTag.COMPONENT_TYPE);
    }

    @Override
    public void setProperties(UIComponent component) {
        super.setProperties(component);
        ((AjaxInput) component).setType(_type);
        ((AjaxInput) component).setPattern(_pattern);
        component.setValueExpression(TAG_CODENAME, _codeName);
        component.setValueExpression(TAG_REQUIRED, _required);
    }

    @Override
    public void release() {
        super.release();
        _type = null;
        _pattern = null;
        _codeName = null;
        _required = null;
    }
}
