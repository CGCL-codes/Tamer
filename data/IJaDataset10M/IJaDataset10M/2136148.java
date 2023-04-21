package com.xmultra.processor.xformer;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * Implementation of the ErrorListener interface.  This implementation
 * simply rethrows the exceptions.  The default implementation of this
 * iterface does not rethrow warnings, but rather writes them to STDERR.
 * The primary use of this error listener is to put checks in your
 * transformations and exit utilizing the xsl:message element.
 *
 * @author KRD, Last edited by: $Author: viji $
 * @version $Change: 34667 $ $Revision: #1 $ $DateTime: 2008/04/22 11:42:11 $
 */
public class XslErrorListener implements ErrorListener {

    /**
     * warning
     *
     * @param exception TransformerException
     */
    public void warning(TransformerException exception) throws TransformerException {
        throw exception;
    }

    /**
     * error
     *
     * @param exception TransformerException
     */
    public void error(TransformerException exception) throws TransformerException {
        throw exception;
    }

    /**
     * fatalError
     *
     * @param exception TransformerException
     */
    public void fatalError(TransformerException exception) throws TransformerException {
        throw exception;
    }
}
