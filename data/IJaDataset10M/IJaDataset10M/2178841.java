package de.herberlin.wwwutil;

import java.io.InputStream;
import de.herberlin.wwwutil.httperror.HttpError;

/**
 * Parses the response from a proxy server.
 *   
 * @author hans joachim herbertz
 * created 28.12.2003
 */
public class ProxyResponse extends ResponseHeader {

    /**
	 * Constructor for ProxyResponse.
	 * @param in
	 * @throws HttpError
	 */
    public ProxyResponse(InputStream in) throws HttpError {
        headers = parseStream(in);
    }
}
