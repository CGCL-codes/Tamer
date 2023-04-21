package org.restlet.client.engine.http.header;

import java.util.Collection;
import org.restlet.client.data.Dimension;

/**
 * Dimension header writer.
 * 
 * @author Thierry Boileau
 */
public class DimensionWriter extends HeaderWriter<Dimension> {

    /**
     * Creates a vary header from the given dimensions.
     * 
     * @param dimensions
     *            The dimensions to copy to the response.
     * @return Returns the Vary header or null, if dimensions is null or empty.
     */
    public static String write(Collection<Dimension> dimensions) {
        return new DimensionWriter().append(dimensions).toString();
    }

    /**
     * Appends a collection of dimensions as a header.
     * 
     * @param dimensions
     *            The dimensions to format.
     * @return This writer.
     */
    public DimensionWriter append(Collection<Dimension> dimensions) {
        if ((dimensions != null) && !dimensions.isEmpty()) {
            if (dimensions.contains(Dimension.CLIENT_ADDRESS) || dimensions.contains(Dimension.TIME) || dimensions.contains(Dimension.UNSPECIFIED)) {
                append("*");
            } else {
                boolean first = true;
                for (Dimension dimension : dimensions) {
                    if (first) {
                        first = false;
                    } else {
                        append(", ");
                    }
                    append(dimension);
                }
            }
        }
        return this;
    }

    @Override
    public HeaderWriter<Dimension> append(Dimension dimension) {
        if (dimension == Dimension.CHARACTER_SET) {
            append(HeaderConstants.HEADER_ACCEPT_CHARSET);
        } else if (dimension == Dimension.CLIENT_AGENT) {
            append(HeaderConstants.HEADER_USER_AGENT);
        } else if (dimension == Dimension.ENCODING) {
            append(HeaderConstants.HEADER_ACCEPT_ENCODING);
        } else if (dimension == Dimension.LANGUAGE) {
            append(HeaderConstants.HEADER_ACCEPT_LANGUAGE);
        } else if (dimension == Dimension.MEDIA_TYPE) {
            append(HeaderConstants.HEADER_ACCEPT);
        } else if (dimension == Dimension.AUTHORIZATION) {
            append(HeaderConstants.HEADER_AUTHORIZATION);
        }
        return this;
    }
}
