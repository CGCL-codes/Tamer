package org.xaware.server.engine.instruction.bizcomps.copybookconfig;

import org.jdom.Element;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.context.AbstractConfigTranslator;
import org.xaware.server.engine.exceptions.XAwareConfigMissingException;
import org.xaware.server.engine.exceptions.XAwareValidationException;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.XAwareSubstitutionException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 *This class holds the configuration information of child elements of column_map element in copybook BizComponent.
 *It holds the responibility of parsing and processing the information from the config element.
 * @author satish
 *
 */
public class ColumnMapSegment extends AbstractConfigTranslator {

    /**String constant to hold the class name.*/
    private static final String CLASS_LOCATION = "ColumnMapSegment";

    /**variable to hold the config element name.*/
    private String m_columnName;

    /**variable to hold the field_length attribute value of this element.*/
    private int m_field_length;

    /**variable to hold the pad_left attribute value of this element.*/
    private char m_pad_left = ' ';

    /**variable to hold the pad_right attribute value of this element.*/
    private char m_pad_right = ' ';

    /**boolean indicating if the pad_left attribute value is present for this element.*/
    private boolean bpadleft = false;

    /**boolean indicating if the pad_right attribute value is present for this element.*/
    private boolean bpadright = false;

    /**boolean indicating if the field_length attribute value is present for this element.*/
    private boolean fieldLengthIsSet = false;

    /**
     * Constructor.
     * 
     * 
     * @param p_context
     *            Context necessary for substitution.
     * @param p_configElement
     *            configuration element held by this class object.
     * @param p_logger
     *            {@link XAwareLogger} needed for logging.
     * @throws XAwareSubstitutionException
     *             if there is a failure during substitution.
     * @throws XAwareException
     *             thrown probably during processing of a functoid.
     */
    public ColumnMapSegment(IBizViewContext p_context, XAwareLogger p_logger, Element p_configElement) throws XAwareException {
        super(p_context, p_logger, CLASS_LOCATION, p_configElement);
        parseConfigInfo();
    }

    /**
	 * parses the configuration information from the configuration element it holds, into the 
	 * member variables.
	 * @throws XAwareException exception thrown while parsing invalid or missing information
	 *  
	 */
    private void parseConfigInfo() throws XAwareException {
        final String methodName = "parseConfigInfo";
        m_columnName = m_configElement.getName();
        String m_field_length_value = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_FIELDLENGTH);
        if (m_field_length_value != null) {
            try {
                m_field_length = Integer.parseInt(m_field_length_value);
                fieldLengthIsSet = true;
            } catch (NumberFormatException ex) {
                throw new XAwareValidationException("field length attribute value should be an int value");
            }
        }
        String m_pad_left_string = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_PADLEFT);
        if (m_pad_left_string != null) {
            bpadleft = true;
            if (m_pad_left_string.equalsIgnoreCase(XAwareConstants.BIZCOMPONENT_ATTR_VALUE_PADSPACE)) {
                m_pad_left = ' ';
            } else if (m_pad_left_string.length() > 0) {
                m_pad_left = m_pad_left_string.charAt(0);
            } else {
                bpadleft = false;
                m_logger.info("Left padding with zero-length character will be ignored.");
            }
        }
        String m_pad_right_string = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_PADRIGHT);
        if (m_pad_right_string != null) {
            bpadright = true;
            if (m_pad_right_string.equalsIgnoreCase(XAwareConstants.BIZCOMPONENT_ATTR_VALUE_PADSPACE)) {
                m_pad_right = ' ';
            } else if (m_pad_right_string.length() > 0) {
                m_pad_right = m_pad_right_string.charAt(0);
            } else {
                bpadright = false;
                m_logger.info("Right padding with zero-length character specified.");
                if (!bpadleft) {
                    m_logger.info("Field will be padded with spaces if necessary");
                }
            }
        }
        if ((bpadleft || bpadright) && fieldLengthIsSet == false) {
            String message = "If pad_left or pad_right attributes are set, field_length attribute must be set, as well.";
            m_logger.severe(message, m_classLocation, methodName);
            throw new XAwareConfigMissingException(message);
        }
        if (bpadleft && bpadright) {
            String message = "Either pad_left attribute or pad_right attribute should be set, not both.";
            m_logger.severe(message, m_classLocation, methodName);
            throw new XAwareConfigMissingException(message);
        }
    }

    @Override
    protected String getElementNameConstant() {
        return m_columnName;
    }

    /**
	 * processes this columnMapSegment and returns the stringbuffer holding the result.
	 * @return StringBuffer buffer containing the result of processing this segment.
	 */
    public StringBuffer processColumnMapSegment() {
        final String methodName = "processColumnMapSegment";
        m_logger.entering(CLASS_LOCATION, methodName);
        StringBuffer outputBuffer = new StringBuffer(1024);
        String text = m_configElement.getText();
        if (text.startsWith("%")) outputBuffer.append(""); else {
            text = formatFixedFieldData(text);
            if (text == null) return null;
            outputBuffer.append(text);
        }
        m_logger.exiting(CLASS_LOCATION, methodName);
        return outputBuffer;
    }

    /**
	 * creates a fixed length string. The method used optional pad right and pad left attributes to specify fillers
	 * in the string. If the length is not specified, the default is the size of the string to the formatted.
	 * 
	 * @param inValue String The string that has to be formatted to a fixed length with optional padding
	 * @return String the formatted string
	 */
    private String formatFixedFieldData(String inValue) {
        final String methodName = "formatFixedFieldData";
        m_logger.entering(m_classLocation, methodName);
        StringBuffer result = null;
        result = new StringBuffer(this.m_field_length);
        if (inValue != null && inValue.length() > this.m_field_length) {
            inValue = inValue.substring(0, this.m_field_length);
        }
        final int invalLength = (inValue != null ? inValue.length() : 0);
        if (invalLength < this.m_field_length) {
            if (bpadleft) {
                for (int c = 0; c < this.m_field_length - invalLength; ++c) {
                    result.append(m_pad_left);
                }
                result.append(inValue);
            } else if (bpadright) {
                result.append(inValue);
                for (int c = invalLength; c < this.m_field_length; ++c) {
                    result.append(m_pad_right);
                }
            } else {
                result.append(inValue);
                for (int c = invalLength; c < this.m_field_length; ++c) {
                    result.append(m_pad_right);
                }
            }
        } else {
            result.append(inValue);
        }
        return result.toString();
    }

    /**
     * Accessor for the element name.
     * 
     * @return m_columnName column segment name.
     */
    public final String getColumnSegmentName() {
        return m_columnName;
    }

    /**
     * returns the field_length value for this segment.
     * @return m_field_length field length value.
     */
    public final int getFieldLength() {
        return m_field_length;
    }
}
