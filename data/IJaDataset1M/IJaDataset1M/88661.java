package com.idna.gav.exceptions.handler;

import org.dom4j.Document;
import com.idna.gav.exceptions.BaseGavException;

public interface GavErrorHandler {

    /**
	 * Log the gav exception in error log database. 
	 * 
	 * @param gavException
	 */
    void persistError(BaseGavException gavException);

    /**
	 * Reuse the passed in {@link Document} object to create an error doc. 
	 * 
	 * @param doc A {@link Document} object whose contents will be removed and re-populated with error messages. 
	 * @param e Exception from which information e.g. <code>errorCode</code> is obtained. 
	 * @return The same {@link Document} object but containing exception presented messages. 
	 */
    Document createErrorDoc(Document doc, Exception e);
}
