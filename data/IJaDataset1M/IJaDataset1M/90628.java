package org.acegisecurity;

import org.acegisecurity.providers.AbstractAuthenticationToken;

/**
 * Simple holder that indicates the {@link MockRunAsManager} returned a
 * different <Code>Authentication</code> object.
 *
 * @author Ben Alex
 * @version $Id: MockRunAsAuthenticationToken.java,v 1.4 2006/02/09 04:16:50 benalex Exp $
 */
public class MockRunAsAuthenticationToken extends AbstractAuthenticationToken {

    public MockRunAsAuthenticationToken() {
        super(null);
    }

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return null;
    }
}
