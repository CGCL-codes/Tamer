package com.framedobjects.dashwell.utils.webservice;

import org.apache.log4j.Logger;
import wsl.fw.exception.MdnException;

/**
 * The <code>VCboolean</code> class represents the Java boolean type.
 * 
 */
public class WSboolean extends ParamView {

    private static final Logger LOGGER = Logger.getLogger(WSboolean.class);

    /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   */
    public WSboolean(ParamListItem paramListItem) throws MdnException {
        this(paramListItem, 0);
    }

    /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   * @param arrayIndex the index where this component must save the values.
   */
    public WSboolean(final ParamListItem paramListItem, int arrayIndex) throws MdnException {
        super(paramListItem, arrayIndex);
        setDefaultItemValue();
    }

    /**
   * Sets the value in the {@link ParamListItem} object.
   * 
   * @param value boolean value (true or false).
   */
    private void setParamListItemValue(boolean value) {
        m_paramListItem.getVectorData().set(m_arrayIndex, value);
    }

    /**
   * The default value of a {@link ParamListItem} object is
   * a string or already a value of the correct Java type.
   * The string has to be parsed into the the correct Java type
   * and stored in the {@link ParamListItem} object.
   */
    private void setDefaultItemValue() throws MdnException {
        boolean value;
        try {
            if (m_paramListItem.getVectorData().get(0) instanceof String) {
                value = (Boolean) Boolean.parseBoolean((String) m_paramListItem.getVectorData().get(0));
                m_paramListItem.getVectorData().set(0, value);
            } else if (m_paramListItem.getVectorData().get(m_arrayIndex) instanceof Boolean) value = (Boolean) m_paramListItem.getVectorData().get(m_arrayIndex); else {
                value = (Boolean) m_paramListItem.getVectorData().get(0);
                m_paramListItem.getVectorData().set(m_arrayIndex, value);
            }
        } catch (NumberFormatException e) {
            throw new MdnException("error.NumberFormatException" + "error.VCMappingDefaultValueError" + m_paramListItem.getDatatype(), e);
        } catch (ClassCastException e) {
            throw new MdnException("error.ClassCastException" + "error.VCMappingDefaultValueError" + m_paramListItem.getDatatype(), e);
        }
    }

    public boolean validate() {
        return true;
    }
}
