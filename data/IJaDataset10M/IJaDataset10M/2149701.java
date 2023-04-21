package com.ivata.mask.web.field.text;

import org.apache.log4j.Logger;
import com.ivata.mask.field.Field;
import com.ivata.mask.field.FieldValueConvertor;
import com.ivata.mask.valueobject.ValueObject;
import com.ivata.mask.web.format.HTMLFormatter;

/**
 * <p>
 * This writer is used to display fields as
 * <code>&lt;input type=&quot;hidden&quot;...&gt;</code>, when they are
 * hidden from display.
 * </p>
 *
 * @since ivata masks 0.5 (20005-01-14)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class PasswordFieldWriter extends TextFieldWriter {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(PasswordFieldWriter.class);

    /**
     * <p>
     * Construct a hidden field writer.
     * </p>
     *
     * @param fieldParam
     *            defines the field to be displayed.
     * @param convertorParam
     *            converts objects into strings for display.
     * @param formatterParam
     *            <copyDoc>Refer to {@link #getFormatter}.</copyDoc>
     */
    public PasswordFieldWriter(final Field fieldParam, final FieldValueConvertor convertorParam, final HTMLFormatter formatterParam) {
        super(fieldParam, convertorParam, formatterParam);
        getAttributesWriter().setAttribute("type", "password");
    }

    /**
     * Overriden to clear the value attribute - we don't want to send the
     * password value.
     *
     * @param valueObject The value object whose value should be written.
     * @return Always returns an empty string.
     */
    protected String setValue(final ValueObject valueObject) {
        if (logger.isDebugEnabled()) {
            logger.debug("setValue(ValueObject valueObject = " + valueObject + ") - start");
        }
        removeAttribute("value");
        if (logger.isDebugEnabled()) {
            logger.debug("setValue(ValueObject) - end - return value = ");
        }
        return "";
    }
}
