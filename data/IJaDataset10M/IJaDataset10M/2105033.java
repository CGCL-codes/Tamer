package org.jcp.xml.dsig.internal.dom;

import java.security.InvalidAlgorithmParameterException;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

/**
 * DOM-based implementation of Enveloped Signature Transform.
 * (Uses Apache XML-Sec Transform implementation)
 *
 * @author Sean Mullan
 */
public final class DOMEnvelopedTransform extends ApacheTransform {

    public void init(TransformParameterSpec params) throws InvalidAlgorithmParameterException {
        if (params != null) {
            throw new InvalidAlgorithmParameterException("params must be null");
        }
    }
}
