package org.apache.myfaces.taglib.core;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.ConverterELTag;
import javax.servlet.jsp.JspException;

/**
 * Implementation of ConverterELTag
 * 
 * @author Bruno Aranda (latest modification by $Author: jakobk $)
 * @version $Revision: 922543 $ $Date: 2010-03-13 06:55:01 -0500 (Sat, 13 Mar 2010) $
 */
public class ConverterTag extends ConverterELTag {

    private static final long serialVersionUID = -4506829108081L;

    private ValueExpression _converterId;

    private ValueExpression _binding;

    private String _converterIdString = null;

    public ConverterTag() {
        super();
    }

    public void setConverterId(ValueExpression converterId) {
        _converterId = converterId;
    }

    public void setBinding(ValueExpression binding) {
        _binding = binding;
    }

    /**
     * Use this method to specify the converterId programmatically.
     * 
     * @param converterIdString
     */
    public void setConverterIdString(String converterIdString) {
        _converterIdString = converterIdString;
    }

    @Override
    public void release() {
        super.release();
        _converterId = null;
        _binding = null;
        _converterIdString = null;
    }

    @Override
    protected Converter createConverter() throws JspException {
        Converter converter = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        if (_binding != null) {
            try {
                converter = (Converter) _binding.getValue(elContext);
                if (converter != null) {
                    return converter;
                }
            } catch (Exception e) {
                throw new JspException("Exception creating converter using binding", e);
            }
        }
        if ((_converterId != null) || (_converterIdString != null)) {
            try {
                if (null != _converterIdString) {
                    converter = facesContext.getApplication().createConverter(_converterIdString);
                } else {
                    String converterId = (String) _converterId.getValue(elContext);
                    converter = facesContext.getApplication().createConverter(converterId);
                }
                if (converter != null && _binding != null) {
                    _binding.setValue(elContext, converter);
                }
            } catch (Exception e) {
                throw new JspException("Exception creating converter with converterId: " + _converterId, e);
            }
        }
        if (converter == null) {
            throw new JspException("Could not create converter. Please specify a valid converterId" + " or a non-null binding.");
        }
        return converter;
    }
}
