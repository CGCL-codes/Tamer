package org.brandao.jbrgates.converters;

import java.net.URL;
import org.brandao.jbrgates.JSONConverter;
import org.brandao.jbrgates.JSONException;

/**
 *
 * @author Brandao
 * @version 1.1
 */
public class URLConverter implements JSONConverter {

    public StringBuffer getJsonObject(Object value) throws JSONException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getObject(Object value) throws JSONException {
        try {
            return new URL(String.valueOf(value));
        } catch (Exception ex) {
            throw new JSONException(ex);
        }
    }
}
