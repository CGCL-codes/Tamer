package cx.ath.contribs.internal.xerces.impl;

import java.io.IOException;
import cx.ath.contribs.internal.xerces.xni.XNIException;
import cx.ath.contribs.internal.xerces.xni.grammars.XMLDTDDescription;
import cx.ath.contribs.internal.xerces.xni.parser.XMLEntityResolver;
import cx.ath.contribs.internal.xerces.xni.parser.XMLInputSource;

/**
 * <p>This interface extends <code>XMLEntityResolver</code> providing
 * a method to resolve external subsets for documents which do not 
 * explicitly provide one. The application can register an object that 
 * implements this interface with the parser configuration. If registered, 
 * it will be queried to locate an external subset when none is provided, 
 * even for documents that do not contain DOCTYPE declarations. If the 
 * registered external subset resolver does not provide an external subset
 * for a given document, it should return <code>null</code>.</p>
 *
 * @xerces.internal
 * 
 * @author Michael Glavassevich, IBM
 *
 * @version $Id: ExternalSubsetResolver.java,v 1.2 2007/07/13 07:23:27 paul Exp $
 */
public interface ExternalSubsetResolver extends XMLEntityResolver {

    /**
     * <p>Locates an external subset for documents which do not explicitly
     * provide one. If no external subset is provided, this method should
     * return <code>null</code>.</p>
     *
     * @param grammarDescription a description of the DTD
     *
     * @throws XNIException Thrown on general error.
     * @throws IOException  Thrown if resolved entity stream cannot be
     *                      opened or some other i/o error occurs.
     */
    public XMLInputSource getExternalSubset(XMLDTDDescription grammarDescription) throws XNIException, IOException;
}
