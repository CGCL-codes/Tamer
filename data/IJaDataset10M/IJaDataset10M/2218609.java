package org.iptc.nar.core.datatype;

import java.net.URI;

/**
 * Object representation of a NamespaceEntry 
 */
public class SchemaDeclarationType {

    /**
	 * A short string used by the provider as a replacement for 
	 * a scheme URI.
	 */
    public String m_schemeAlias;

    /**
	 * The URI which identifies the scheme.
	 */
    public URI m_schemeURI;

    public String getSchemeAlias() {
        return m_schemeAlias;
    }

    public void setSchemeAlias(String schemeAlias) {
        this.m_schemeAlias = schemeAlias;
    }

    public URI getSchemeURI() {
        return m_schemeURI;
    }

    public void setSchemeURI(URI uri) {
        m_schemeURI = uri;
    }
}
