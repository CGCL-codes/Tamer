package org.mobicents.xcap.client.uri;

import org.mobicents.xcap.client.uri.encoding.UriComponentEncoder;

/**
 * @author martins
 *
 */
public class AttributeSelectorBuilder {

    private static final String PREFIX = "@";

    private final String name;

    /**
	 * 
	 */
    public AttributeSelectorBuilder(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringBuilder(PREFIX).append(name).toString();
    }

    /**
	 * 
	 * @return
	 */
    public String toPercentEncodedString() {
        return new StringBuilder(PREFIX).append(UriComponentEncoder.encodePath(name)).toString();
    }
}
