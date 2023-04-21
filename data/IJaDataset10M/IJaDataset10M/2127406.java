package org.dasein.cloud.identity;

import javax.annotation.Nullable;

/**
 * A null implementation of the Dasein Cloud Identity Services. Implementation classes override selected
 * methods to indicate support for specific parts of the Identity Services.
 * @author George Reese (george.reese@imaginary.com)
 * @since 2010.11
 * @version 2012.02 - added support for identity and access management
 */
public abstract class AbstractIdentityServices implements IdentityServices {

    @Override
    @Nullable
    public IdentityAndAccessSupport getIdentityAndAccessSupport() {
        return null;
    }

    @Override
    @Nullable
    public ShellKeySupport getShellKeySupport() {
        return null;
    }

    @Override
    public boolean hasIdentityAndAccessSupport() {
        return (getIdentityAndAccessSupport() != null);
    }

    @Override
    public boolean hasShellKeySupport() {
        return (getShellKeySupport() != null);
    }
}
