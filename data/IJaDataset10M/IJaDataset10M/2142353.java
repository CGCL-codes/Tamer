package br.gov.frameworkdemoiselle.shiro.security;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import org.apache.shiro.subject.Subject;
import br.gov.frameworkdemoiselle.security.Authorizer;

@Alternative
public class ShiroAuthorizer implements Authorizer {

    private static final long serialVersionUID = 1L;

    private Subject subject;

    @Inject
    public ShiroAuthorizer(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean hasRole(String role) {
        return subject.hasRole(role);
    }

    @Override
    public boolean hasPermission(String resource, String operation) {
        return subject.isPermitted(resource + ":" + operation);
    }
}
