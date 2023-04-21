package org.nexopenframework.signature.impl;

import java.security.Key;
import java.security.PublicKey;
import javax.xml.crypto.KeySelectorResult;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class PublicKeySelectorResult implements KeySelectorResult {

    private final PublicKey pk;

    PublicKeySelectorResult(PublicKey pk) {
        this.pk = pk;
    }

    public Key getKey() {
        return pk;
    }
}
