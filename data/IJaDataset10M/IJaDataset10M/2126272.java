package org.openexi.fujitsu.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

final class LocatorOnSAXParseException implements Locator {

    private final SAXParseException m_spe;

    LocatorOnSAXParseException(SAXParseException spe) {
        m_spe = spe;
    }

    public int getColumnNumber() {
        return m_spe.getColumnNumber();
    }

    public int getLineNumber() {
        return m_spe.getLineNumber();
    }

    public String getPublicId() {
        return m_spe.getPublicId();
    }

    public String getSystemId() {
        return m_spe.getSystemId();
    }
}
