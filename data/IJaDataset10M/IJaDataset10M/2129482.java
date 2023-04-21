package org.jcp.xml.dsig.internal.dom;

import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.Provider;
import org.w3c.dom.Element;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;

/**
 * DOM-based abstract implementation of CanonicalizationMethod.
 *
 * @author Sean Mullan
 */
public class DOMCanonicalizationMethod extends DOMTransform implements CanonicalizationMethod {

    /**
     * Creates a <code>DOMCanonicalizationMethod</code>.
     *
     * @param spi TransformService
     */
    public DOMCanonicalizationMethod(TransformService spi) throws InvalidAlgorithmParameterException {
        super(spi);
    }

    /**
     * Creates a <code>DOMCanonicalizationMethod</code> from an element. This
     * ctor invokes the abstract {@link #unmarshalParams unmarshalParams}
     * method to unmarshal any algorithm-specific input parameters.
     *
     * @param cmElem a CanonicalizationMethod element
     */
    public DOMCanonicalizationMethod(Element cmElem, XMLCryptoContext context, Provider provider) throws MarshalException {
        super(cmElem, context, provider);
    }

    /**
     * Canonicalizes the specified data using the underlying canonicalization
     * algorithm. This is a convenience method that is equivalent to invoking
     * the {@link #transform transform} method.
     *
     * @param data the data to be canonicalized
     * @param xc the <code>XMLCryptoContext</code> containing
     *     additional context (may be <code>null</code> if not applicable)
     * @return the canonicalized data
     * @throws NullPointerException if <code>data</code> is <code>null</code>
     * @throws TransformException if an unexpected error occurs while
     *    canonicalizing the data
     */
    public Data canonicalize(Data data, XMLCryptoContext xc) throws TransformException {
        return transform(data, xc);
    }

    public Data canonicalize(Data data, XMLCryptoContext xc, OutputStream os) throws TransformException {
        return transform(data, xc, os);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CanonicalizationMethod)) {
            return false;
        }
        CanonicalizationMethod ocm = (CanonicalizationMethod) o;
        return (getAlgorithm().equals(ocm.getAlgorithm()) && DOMUtils.paramsEqual(getParameterSpec(), ocm.getParameterSpec()));
    }
}
