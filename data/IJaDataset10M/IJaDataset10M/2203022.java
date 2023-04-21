package siouxsie.app.ui.base;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Action scope resolver for classpath scanning instanciation.
 * Action must be prototype, so scope = noo proxy mode and prototype.
 * @author Arnaud Cogoluegnes
 * @version $Id: ActionScopeResolver.java 135 2008-05-24 17:03:23Z acogo $
 */
public class ActionScopeResolver implements ScopeMetadataResolver {

    private static final ScopeMetadata SCOPE = new ScopeMetadata();

    static {
        SCOPE.setScopedProxyMode(ScopedProxyMode.NO);
        SCOPE.setScopeName("prototype");
    }

    public ScopeMetadata resolveScopeMetadata(BeanDefinition beanDefinition) {
        return SCOPE;
    }
}
