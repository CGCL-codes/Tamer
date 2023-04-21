package org.dbunit.dataset.datatype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * String data type that ignore case when comparing String values.
 * 
 * @author gommma (gommma AT users.sourceforge.net)
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 909 $ $Date: 2008-12-04 15:20:00 -0500 (Thu, 04 Dec 2008) $
 * @since 2.4.0
 */
public class StringIgnoreCaseDataType extends StringDataType {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(StringIgnoreCaseDataType.class);

    public StringIgnoreCaseDataType(String name, int sqlType) {
        super(name, sqlType);
    }

    protected int compareNonNulls(Object value1, Object value2) throws TypeCastException {
        logger.debug("compareNonNulls(value1={}, value2={}) - start", value1, value2);
        String value1cast = (String) value1;
        String value2cast = (String) value2;
        return value1cast.compareToIgnoreCase(value2cast);
    }
}
