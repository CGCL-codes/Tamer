package org.jsecurity.authc.credential;

import org.jsecurity.crypto.hash.AbstractHash;
import org.jsecurity.crypto.hash.Sha384Hash;

/**
 * @author Les Hazlewood
 * @since Jun 10, 2008 5:02:27 PM
 */
public class Sha384CredentialsMatcherTest extends HashedCredentialsMatcherTest {

    public Class<? extends HashedCredentialsMatcher> getMatcherClass() {
        return Sha384CredentialsMatcher.class;
    }

    public AbstractHash hash(Object credentials) {
        return new Sha384Hash(credentials);
    }
}
