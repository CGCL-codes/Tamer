package org.atricore.idbus.kernel.main.authn.scheme;

import org.atricore.idbus.kernel.main.authn.BaseCredential;

/**
 * This credential represents a username.
 *
 * @author <a href="mailto:sgonzalez@josso.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id: UsernameCredential.java 1040 2009-03-05 00:56:52Z gbrigand $
 */
public class UsernameCredential extends BaseCredential {

    public UsernameCredential() {
        super();
    }

    public UsernameCredential(Object credential) {
        super(credential);
    }
}
