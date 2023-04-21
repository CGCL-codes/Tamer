package com.agimatec.commons.config.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.math.BigDecimal;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 *
 * @author Roman Stumm
 */
class BigDecimalHandler extends PrimNodeHandler {

    public BigDecimalHandler(final String aTag) {
        super(aTag);
    }

    protected Class getInstanceClass() {
        return BigDecimal.class;
    }

    protected Object startNode(final Attributes attr) throws SAXException {
        try {
            return new BigDecimal(getValue(attr));
        } catch (NumberFormatException ex) {
            throw new SAXException("not a BigDecimal: " + getValue(attr), ex);
        }
    }
}
