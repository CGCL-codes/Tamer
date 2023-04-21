package org.cloudlet.web.service.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import java.lang.reflect.Proxy;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

final class InjectionServiceLayerDecorator extends ServiceLayerDecorator {

    /**
   * JSR 303 validator used to validate requested entities.
   */
    private final Validator validator;

    private final Injector injector;

    private final Provider<EntityManager> em;

    private final AutoBeanFactory beanFactory;

    @Inject
    InjectionServiceLayerDecorator(final Injector injector, final Validator validator, final Provider<EntityManager> em, final AutoBeanFactory beanFactory) {
        super();
        this.injector = injector;
        this.validator = validator;
        this.em = em;
        this.beanFactory = beanFactory;
    }

    @Override
    public <T> T createDomainObject(final Class<T> clazz) {
        if (clazz.isInterface()) {
            return beanFactory.create(clazz).as();
        }
        T domain = injector.getInstance(clazz);
        return domain;
    }

    @Override
    public <T extends Locator<?, ?>> T createLocator(final Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    @Override
    public Object createServiceInstance(final Class<? extends RequestContext> requestContext) {
        Class<?> serviceClass = getTop().resolveServiceClass(requestContext);
        Object service = injector.getInstance(serviceClass);
        return service;
    }

    @Override
    public Object getId(final Object domainObject) {
        return getTop().getProperty(domainObject, "id");
    }

    @Override
    public Class<?> getIdType(final Class<?> domainType) {
        return getTop().getGetter(domainType, "id").getReturnType();
    }

    @Override
    public Object getVersion(final Object domainObject) {
        return getTop().getProperty(domainObject, "version");
    }

    @Override
    public boolean isLive(final Object domainObject) {
        return em.get().contains(domainObject);
    }

    @Override
    public <T> T loadDomainObject(final Class<T> clazz, final Object domainId) {
        return em.get().find(clazz, domainId);
    }

    @Override
    public <T> Class<? extends T> resolveClientType(final Class<?> domainClass, final Class<T> clientType, final boolean required) {
        if (Proxy.isProxyClass(domainClass)) {
            return clientType;
        }
        return super.resolveClientType(domainClass, clientType, required);
    }

    /**
   * Invokes JSR 303 validator on a given domain object.
   * 
   * @param domainObject the domain object to be validated
   * @param <T> the type of the entity being validated
   * @return the violations associated with the domain object
   */
    @Override
    public <T> Set<ConstraintViolation<T>> validate(final T domainObject) {
        return validator.validate(domainObject);
    }
}
