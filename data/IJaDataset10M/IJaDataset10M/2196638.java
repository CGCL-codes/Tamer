package org.guiceyfruit.jpa.support;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.guiceyfruit.support.AnnotationMemberProviderSupport;

/**
 * Allows the JPA persistence context to be injected via {@link javax.persistence.PersistenceContext}
 *
 * @version $Revision: 1.1 $
 */
public class PersistenceMemberProvider extends AnnotationMemberProviderSupport<PersistenceContext> {

    private final Provider<EntityManager> defaultEntityManager;

    private Map<String, Provider<EntityManager>> namedEntityManagers;

    @Inject
    public PersistenceMemberProvider(Provider<EntityManager> defaultEntityManager) {
        this.defaultEntityManager = defaultEntityManager;
    }

    protected Object provide(PersistenceContext annotation, Member member, TypeLiteral<?> requiredType, Class<?> memberType, Annotation[] annotations) {
        Provider<EntityManager> provider = null;
        String name = annotation.name();
        if (namedEntityManagers != null && name != null && name.length() > 0) {
            provider = namedEntityManagers.get(name);
        }
        if (provider == null) {
            provider = defaultEntityManager;
        }
        return provider.get();
    }

    public boolean isNullParameterAllowed(PersistenceContext annotation, Method method, Class<?> parameterType, int parameterIndex) {
        return false;
    }

    public Map<String, Provider<EntityManager>> getNamedEntityManagers() {
        return namedEntityManagers;
    }

    @Inject(optional = true)
    public void setNamedEntityManagers(Map<String, Provider<EntityManager>> namedEntityManagers) {
        this.namedEntityManagers = namedEntityManagers;
    }
}
