package org.gvsig.gpe.exceptions;

import java.net.URI;
import java.util.Hashtable;
import java.util.Map;

public class ParserFileNotSupportedException extends ParserCreationException {

    private URI uri = null;

    public ParserFileNotSupportedException(URI uri) {
        this.uri = uri;
        initialize();
    }

    /**
	 * Initialize the properties
	 */
    private void initialize() {
        messageKey = "gpe_parser_creation_uri_not_found";
        formatString = "There is not a parser to parse the uri %(uri)";
    }

    protected Map values() {
        Hashtable params = new Hashtable();
        params.put("uri", uri);
        return params;
    }
}
