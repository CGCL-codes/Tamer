package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidadinternal.convert.ConverterUtils;

public abstract class ValueRenderer extends XhtmlRenderer {

    protected ValueRenderer(FacesBean.Type type) {
        super(type);
    }

    @Override
    protected void findTypeConstants(FacesBean.Type type) {
        super.findTypeConstants(type);
        _converterKey = type.findKey("converter");
        _valueKey = type.findKey("value");
    }

    protected String getConvertedString(FacesContext context, UIComponent component, FacesBean bean) {
        Object value = getValue(bean);
        Converter converter = getConverter(bean);
        if ((converter == null) && (value != null) && !(value instanceof String)) converter = getDefaultConverter(context, bean);
        if (converter != null) {
            return converter.getAsString(context, component, value);
        }
        return toString(value);
    }

    protected Converter getDefaultConverter(FacesContext context, FacesBean bean) {
        ValueExpression expression = getValueExpression(bean);
        if (expression == null) return null;
        Class<?> type = expression.getType(context.getELContext());
        return ConverterUtils.createConverter(context, type);
    }

    protected Object getValue(FacesBean bean) {
        return bean.getProperty(_valueKey);
    }

    /**
   * Returns the ValueExpression for the "value" property.
   */
    protected ValueExpression getValueExpression(FacesBean bean) {
        return bean.getValueExpression(_valueKey);
    }

    protected Converter getConverter(FacesBean bean) {
        return (Converter) bean.getProperty(_converterKey);
    }

    private PropertyKey _valueKey;

    private PropertyKey _converterKey;
}
