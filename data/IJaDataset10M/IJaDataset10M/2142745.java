package org.jservicerules.support.annotations.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;
import org.jsemantic.core.session.factory.SemanticSessionCreationException;
import org.jsemantic.core.session.factory.SemanticSessionFactory;
import org.jsemantic.servicerules.drools.session.factory.SemanticSessionFactoryImpl;
import org.jservicerules.support.annotations.SemanticFactory;
import org.jservicerules.support.annotations.SemanticService;
import org.jservicerules.support.annotations.SessionVariables;
import org.jservicerules.support.annotations.SpringExternalContext;
import org.jservicerules.support.annotations.Variable;
import org.jservicerules.support.spring.SpringExternalContextImpl;

/**
 * <p>
 * This class is used as a helper by the interceptor classes in order to process
 * the annotations.
 */
public class InterceptorHelper {

    /** The Constant log. */
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InterceptorHelper.class);

    private static String SEMANTIC_SESSION_FACTORY_METHOD = "setSemanticSessionFactory";

    /**
	 * Inject the SemanticSessionFactory in the service class.
	 * 
	 * @param ann
	 *            the ann
	 * @param service
	 *            the service
	 * @param source
	 *            the source
	 * 
	 * @return the semantic session factory
	 * 
	 * @throws SemanticSessionCreationException
	 *             the semantic session creation exception
	 */
    public SemanticSessionFactory injectSemanticServiceFactory(Annotation ann, Object service, Object source) throws SemanticSessionCreationException {
        String rulesFile = null;
        boolean stateless = false;
        SemanticSessionFactory factory = null;
        if (ann instanceof SemanticService) {
            rulesFile = ((SemanticService) ann).rulesFile();
            stateless = ((SemanticService) ann).stateless();
            factory = new SemanticSessionFactoryImpl(rulesFile, stateless);
            injectSemanticServiceFactory(factory, service);
        } else if (ann instanceof SemanticFactory) {
            rulesFile = ((SemanticFactory) ann).rulesFile();
            stateless = ((SemanticFactory) ann).stateless();
            factory = new SemanticSessionFactoryImpl(rulesFile, stateless);
            Field field = (Field) source;
            injectSemanticFactory(field, factory, service);
        } else {
            log.error("Error . Correct Annotations haven't been found.");
            throw new SemanticSessionCreationException("Error . Correct Annotations haven't been found.");
        }
        return factory;
    }

    /**
	 * Inject the SemanticSessionFactory in the service class, using the
	 * SemanticFactory annotation.
	 * 
	 * @param annotatedField
	 *            the annotated field
	 * @param factory
	 *            the factory
	 * @param service
	 *            the service
	 * 
	 * @throws SemanticSessionCreationException
	 *             the semantic session creation exception
	 */
    public void injectSemanticFactory(Field annotatedField, SemanticSessionFactory factory, Object service) throws SemanticSessionCreationException {
        try {
            annotatedField.setAccessible(true);
            annotatedField.set(service, factory);
        } catch (IllegalArgumentException e) {
            log.error(e);
            throw new SemanticSessionCreationException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e);
            throw new SemanticSessionCreationException(e.getMessage(), e);
        }
    }

    /**
	 * Inject the SemanticSessionFactory in the service class, using the
	 * SemanticService annotation.
	 * 
	 * @param factory
	 *            the factory
	 * @param service
	 *            the service
	 * 
	 * @throws SemanticSessionCreationException
	 *             the semantic session creation exception
	 */
    private void injectSemanticServiceFactory(SemanticSessionFactory factory, Object service) throws SemanticSessionCreationException {
        Method method = null;
        try {
            method = service.getClass().getMethod(SEMANTIC_SESSION_FACTORY_METHOD, new Class[] { SemanticSessionFactory.class });
            method.invoke(service, factory);
        } catch (SecurityException e) {
            log.error(e);
            throw new SemanticSessionCreationException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            log.error(e);
            throw new SemanticSessionCreationException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error(e);
            throw new SemanticSessionCreationException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e);
            throw new SemanticSessionCreationException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error(e);
            throw new SemanticSessionCreationException(e.getMessage(), e);
        }
    }

    /**
	 * Inject the spring external context in the service using the
	 * SpringExternalContext annotation.
	 * 
	 * @param factory
	 *            the factory
	 * @param ann
	 *            the ann
	 */
    public void injectSpringExternalContext(SemanticSessionFactory factory, SpringExternalContext ann) {
        String contextFile = ann.contextFile();
        SpringExternalContextImpl ctx = new SpringExternalContextImpl(contextFile);
        factory.setExternalContext(ctx);
    }

    /**
	 * Inject the Session Variables in the service using the
	 * SessionVariables annotation.
	 * 
	 * @param factory
	 *            the factory
	 * @param ann
	 *            the ann
	 */
    public void injectSessionVariables(SemanticSessionFactory factory, SessionVariables ann) {
        Variable[] variables = ann.variables();
        Map sessionVariables = new WeakHashMap();
        for (Variable variable : variables) {
            sessionVariables.put(variable.key(), variable.value());
        }
        factory.setSessionVariables(sessionVariables);
    }
}
