package net.sourceforge.poi.xml.hssf;

import net.sourceforge.poi.cocoon.serialization.util.BooleanConverter;
import net.sourceforge.poi.cocoon.serialization.util.BooleanResult;
import java.io.IOException;

/**
 * No-op implementation of ElementProcessor to handle the
 * "even_if_only_styles" tag
 *
 * This element has a single attribute, value, which is boolean.
 *
 * This element is not used in HSSFSerializer 1.0
 *
 * @author Marc Johnson (marc_johnson27591@hotmail.com)
 */
public class EP_EvenIfOnlyStyles extends BaseElementProcessor {

    private static final String _value_attribute = "value";

    private BooleanResult _value;

    /**
     * constructor
     */
    public EP_EvenIfOnlyStyles() {
        super(null);
        _value = null;
    }

    /**
     * @return value
     *
     * @exception IOException if the value is malformed or missing
     */
    public boolean getValue() throws IOException {
        if (_value == null) {
            _value = BooleanConverter.extractBoolean(getValue(_value_attribute));
        }
        return _value.booleanValue();
    }
}
