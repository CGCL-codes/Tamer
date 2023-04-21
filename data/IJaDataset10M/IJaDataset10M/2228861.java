package org.genxdm.processor.w3c.xs.impl;

import java.util.ArrayList;
import org.genxdm.xs.constraints.RestrictedXPath;

final class DefaultRestrictedXPathImpl implements RestrictedXPath {

    private final ArrayList<String> m_namespaces = new ArrayList<String>();

    private final ArrayList<String> m_localNames = new ArrayList<String>();

    private final ArrayList<Boolean> m_contextNode = new ArrayList<Boolean>();

    boolean m_isAttribute;

    boolean m_relocatable;

    DefaultRestrictedXPathImpl m_alternate;

    public boolean isContextNode(final int index) {
        return m_contextNode.get(index);
    }

    public String getStepNamespace(final int index) {
        return m_namespaces.get(index);
    }

    public String getStepLocalName(final int index) {
        return m_localNames.get(index);
    }

    public boolean isWildcardNamespace(final int index) {
        return null == m_namespaces.get(index);
    }

    public boolean isWildcardLocalName(final int index) {
        return null == m_localNames.get(index);
    }

    public boolean isRelocatable() {
        return m_relocatable;
    }

    public void setRelocatableFlag(final boolean relocatableFlag) {
        m_relocatable = relocatableFlag;
    }

    public boolean isAttribute() {
        return m_isAttribute;
    }

    public void setAttributeFlag(final boolean attributeFlag) {
        m_isAttribute = attributeFlag;
    }

    public int getStepLength() {
        return m_localNames.size();
    }

    public int getLBoundStep() {
        return 0;
    }

    public int getUBoundStep() {
        return m_localNames.size() - 1;
    }

    public DefaultRestrictedXPathImpl getAlternate() {
        return m_alternate;
    }

    public void setAlternate(final DefaultRestrictedXPathImpl alternate) {
        m_alternate = alternate;
    }

    public void addContextNodeStep() {
        m_namespaces.add(null);
        m_localNames.add(null);
        m_contextNode.add(Boolean.TRUE);
    }

    public void addNameStep(final String namespaceURI, final String localName) {
        m_namespaces.add(namespaceURI);
        m_localNames.add(localName);
        m_contextNode.add(Boolean.FALSE);
    }
}
