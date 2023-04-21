package com.controltier.shared.resources;

/**
 * ResourceXMLParserException is thrown if an error occurs in the {@link com.controltier.shared.resources.ResourceXMLParser}.
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision$
 */
public class ResourceXMLParserException extends Exception {

    public ResourceXMLParserException() {
        super();
    }

    public ResourceXMLParserException(final String msg) {
        super(msg);
    }

    public ResourceXMLParserException(final Exception cause) {
        super(cause);
    }

    public ResourceXMLParserException(final String msg, final Exception cause) {
        super(msg, cause);
    }
}
