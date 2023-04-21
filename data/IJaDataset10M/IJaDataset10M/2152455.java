package org.scub.foundation.framework.base.securite;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.SecurityConfig;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.rmi.ContextPropagatingRemoteInvocation;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.runas.RunAsManagerImpl;

/**
 * Permet de réaliser une athentification locale puis un changement de rôle en Run_As<br>
 * Créé un contexte de sécurité avant un appel RMI à un service sécurisé.
 * @author Goumard Stéphane (stephane.goumard@scub.net)
 */
public final class RunAsCustomAuthRemoteInvocationFactory implements RemoteInvocationFactory {

    /** Logger. */
    private Logger logger = Logger.getLogger(RunAsCustomAuthRemoteInvocationFactory.class);

    /** Fournisseur d'authentification. */
    private AuthenticationManager authenticationManager;

    /** Gestionnaire de changement d'identité. */
    private RunAsManagerImpl runAsManager;

    /** Login interne de l'application. */
    private String login;

    /** Mot de passe interne de l'application. */
    private String motDePasse;

    /** Role demandé pour le changement d'identité. */
    private String runAsRole;

    /** {@inheritDoc} */
    public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
        if (logger.isDebugEnabled()) {
            logger.debug("Appel d'un service sécurisé (" + methodInvocation + "), Authentification...");
        }
        try {
            final Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, motDePasse));
            final ConfigAttributeDefinition def = new ConfigAttributeDefinition(new SecurityConfig(runAsRole));
            final Authentication runAsAuth = runAsManager.buildRunAs(auth, new Object(), def);
            Boolean roleExiste = false;
            for (int it = 0; it < auth.getAuthorities().length; it++) {
                if (runAsAuth.getAuthorities()[0].equals(auth.getAuthorities()[it])) {
                    roleExiste = true;
                    break;
                }
            }
            if (!roleExiste) {
                SecurityContextHolder.getContext().setAuthentication(runAsAuth);
            }
        } catch (BadCredentialsException e) {
            logger.error(e, e);
        }
        return new ContextPropagatingRemoteInvocation(methodInvocation);
    }

    /**
     * Set the authenticationManager value.
     * @param authenticationManager the authenticationManager to set
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Fixe la valeur du paramétre login.
     * @param login le login a fixer.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Fixe la valeur du paramétre motDePasse.
     * @param motDePasse le motDePasse a fixer.
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    /**
     * Fixe la valeur du paramétre runAsManager.
     * @param runAsManager le runAsManager a fixer.
     */
    public void setRunAsManager(RunAsManagerImpl runAsManager) {
        this.runAsManager = runAsManager;
    }

    /**
     * Fixe la valeur du paramétre runAsRole.
     * @param runAsRole le runAsRole a fixer.
     */
    public void setRunAsRole(String runAsRole) {
        this.runAsRole = runAsRole;
    }
}
