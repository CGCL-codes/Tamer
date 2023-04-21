package org.cantaloop.cgimlet.lang;

public class PrefixIdentifierFactory implements IdentifierFactory {

    private String m_prefix;

    public PrefixIdentifierFactory(String prefix) {
        m_prefix = prefix;
    }

    public String getIdentifier(String baseName) {
        return m_prefix + baseName;
    }
}
