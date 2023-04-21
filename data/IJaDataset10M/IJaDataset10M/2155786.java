package org.opennms.core.xml;

import org.opennms.core.utils.LogUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

public class SimpleNamespaceFilter extends XMLFilterImpl {

    private String m_namespaceUri;

    private boolean m_addNamespace = false;

    private boolean m_addedNamespace = false;

    public SimpleNamespaceFilter(final String namespaceUri, final boolean addNamespace) {
        super();
        LogUtils.debugf(this, "SimpleNamespaceFilter initalized with namespace %s (%s)", namespaceUri, Boolean.valueOf(addNamespace));
        if (addNamespace) {
            this.m_namespaceUri = namespaceUri.intern();
        } else {
            this.m_namespaceUri = "".intern();
        }
        this.m_addNamespace = addNamespace;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        if (m_addNamespace) {
            startControlledPrefixMapping();
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        if (m_addNamespace) {
            if (LogUtils.isTraceEnabled(this)) LogUtils.tracef(this, "start: uri = %s, new uri = %s, localName = %s, qName = %s, attributes = %s", uri, m_namespaceUri, localName, qName, attributes);
            super.startElement(m_namespaceUri, localName, qName, attributes);
        } else {
            if (LogUtils.isTraceEnabled(this)) LogUtils.tracef(this, "start: uri = %s, new uri = %s, localName = %s, qName = %s, attributes = %s", uri, uri, localName, qName, attributes);
            super.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (m_addNamespace) {
            if (LogUtils.isTraceEnabled(this)) LogUtils.tracef(this, "end:   uri = %s, new uri = %s, localName = %s, qName = %s", uri, m_namespaceUri, localName, qName);
            super.endElement(m_namespaceUri, localName, qName);
        } else {
            if (LogUtils.isTraceEnabled(this)) LogUtils.tracef(this, "end:   uri = %s, new uri = %s, localName = %s, qName = %s", uri, uri, localName, qName);
            super.endElement(uri, localName, qName);
        }
    }

    @Override
    public void startPrefixMapping(final String prefix, final String url) throws SAXException {
        if (LogUtils.isTraceEnabled(this)) LogUtils.tracef(this, "startPrefixMapping: prefix = %s, url = %s", prefix, url);
        if (m_addNamespace) {
            this.startControlledPrefixMapping();
        } else {
            super.startPrefixMapping(prefix, url);
        }
    }

    private void startControlledPrefixMapping() throws SAXException {
        if (LogUtils.isTraceEnabled(this)) LogUtils.tracef(this, "startControlledPrefixMapping");
        if (m_addNamespace && !m_addedNamespace) {
            super.startPrefixMapping("".intern(), m_namespaceUri);
            m_addedNamespace = true;
        }
    }
}
