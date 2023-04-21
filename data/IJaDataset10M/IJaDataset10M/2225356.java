package com.agimatec.commons.config.sax;

import com.agimatec.commons.config.DecimalNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.math.BigDecimal;

/**
 * Title:
 * Description:
 * Company:
 *
 * @author Roman Stumm
 */
class DecimalNodeHandler extends NodeHandler {

    public DecimalNodeHandler(final String aTag) {
        super(aTag);
    }

    protected Class getInstanceClass() {
        return DecimalNode.class;
    }

    protected void acceptCharacters(final ConfigContentHandler docHandler, final char[] chars, final int offset, final int length) throws SAXException {
        final DecimalNode node = (DecimalNode) docHandler.getCurrentNode();
        setValue(node, new String(chars, offset, length));
    }

    protected Object startNode(final Attributes attr) throws SAXException {
        final DecimalNode node = new DecimalNode();
        setName(node, attr);
        final String value = getValue(attr);
        if (value != null) {
            try {
                setValue(node, value);
            } catch (NumberFormatException ex) {
                throw new SAXException("not a BigDecimal: " + value, ex);
            }
        }
        return node;
    }

    private void setValue(final DecimalNode node, final String stringValue) {
        node.setValue(new BigDecimal(stringValue));
    }
}
