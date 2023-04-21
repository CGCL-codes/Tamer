package com.interworldtransport.clados;

/**   com.interworldtransport.clados.BadSignatureException  Bad signature handler for the ProductTable.
 * <p>
 * This class is designed to be the handler of signature definition exceptions.
 * Product tables can be built from simple signatures, but the string must be
 * well formed.
 * <p>
 * @version 0.80, $Date: 2005/09/29 08:36:20 $
 * @author Dr Alfred W Differ
 */
public class BadSignatureException extends CladosException {

    private static final long serialVersionUID = 3443626909650145934L;

    /**
 * Construct this exception.  This exception must have the source monad and a
 * message complaining about the expectations of the source.
 */
    BadSignatureException(CladosObject pSource, String pMessage) {
        super(pSource, pMessage);
    }
}
