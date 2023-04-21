package org.apache.http.contrib.sip;

import org.apache.http.message.BasicHeader;

/**
 * Represents a SIP (or HTTP) header field with optional compact name.
 * 
 *
 * @version $Revision: 744512 $
 */
public class BasicCompactHeader extends BasicHeader implements CompactHeader {

    /** The compact name, if there is one. */
    private final String compact;

    /**
     * Constructor with names and value.
     *
     * @param fullname          the full header name
     * @param compactname       the compact header name, or <code>null</code>
     * @param value             the header value
     */
    public BasicCompactHeader(final String fullname, final String compactname, final String value) {
        super(fullname, value);
        if ((compactname != null) && (compactname.length() >= fullname.length())) {
            throw new IllegalArgumentException("Compact name must be shorter than full name. " + compactname + " -> " + fullname);
        }
        this.compact = compactname;
    }

    public String getCompactName() {
        return this.compact;
    }

    /**
     * Creates a compact header with automatic lookup.
     *
     * @param name      the header name, either full or compact
     * @param value     the header value
     * @param mapper    the header name mapper, or <code>null</code> for the
     *                  {@link BasicCompactHeaderMapper#DEFAULT default}
     */
    public static BasicCompactHeader newHeader(final String name, final String value, CompactHeaderMapper mapper) {
        if (name == null) {
            throw new IllegalArgumentException("The name must not be null.");
        }
        if (mapper == null) mapper = BasicCompactHeaderMapper.DEFAULT;
        final String altname = mapper.getAlternateName(name);
        String fname = name;
        String cname = altname;
        if ((altname != null) && (name.length() < altname.length())) {
            fname = altname;
            cname = name;
        }
        return new BasicCompactHeader(fname, cname, value);
    }
}
