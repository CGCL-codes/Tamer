package org.photovault.replication;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;

/**
 Factory for constructing resolvers that can convert DTO into an object persisted 
 by Hibernate.
 
 @author Harri Kaimio
 @since 0.6.0
 @see DTOResolverFactory
 @see HibernateDTOResolver
 */
public class HibernateDtoResolverFactory implements DTOResolverFactory {

    /**
     Session used to look up or persist objects.
     */
    private Session session;

    /**
     Already created resolvers that are associated with this session.
     */
    private Map<Class, HibernateDTOResolver> resolvers = new HashMap<Class, HibernateDTOResolver>();

    /**
     Default resolver
     */
    private DTOResolver defaultResolver = new DefaultDtoResolver();

    public HibernateDtoResolverFactory(Session sess) {
        this.session = sess;
    }

    /**
     Get an instance of given resolver class
     @param clazz Class of the resolver
     @return Instance of clazz
     @throws IllegalArgumentException if clazz is not subclass of {@link 
     HibernateDTOResolver} or cannot be constructed by reflection.
     */
    public DTOResolver getResolver(Class<? extends DTOResolver> clazz) {
        if (clazz == DefaultDtoResolver.class) {
            return defaultResolver;
        }
        if (resolvers.containsKey(clazz)) {
            return resolvers.get(clazz);
        }
        HibernateDTOResolver resolver = null;
        try {
            resolver = (HibernateDTOResolver) clazz.newInstance();
            resolver.setSession(session);
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException("Cannot instantiate " + clazz.getName(), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Cannot access constructor of " + clazz.getName(), ex);
        }
        return resolver;
    }
}
