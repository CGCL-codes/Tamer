package org.nakedobjects.runtime.authentication.standard;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;
import org.nakedobjects.runtime.installers.InstallerAbstract;

public abstract class StandardAuthenticationManagerInstallerAbstract extends InstallerAbstract implements AuthenticationManagerInstaller {

    private final String name;

    public StandardAuthenticationManagerInstallerAbstract(final String name) {
        this.name = name;
    }

    public final AuthenticationManager createAuthenticationManager() {
        final AuthenticationManagerStandard authenticationManager = new AuthenticationManagerStandard(getConfiguration());
        final Authenticator authenticator = createAuthenticator(getConfiguration());
        authenticationManager.addAuthenticator(authenticator);
        return authenticationManager;
    }

    /**
     * Hook method
     * @return
     */
    protected abstract Authenticator createAuthenticator(final NakedObjectConfiguration configuration);

    public final String getName() {
        return name;
    }
}
