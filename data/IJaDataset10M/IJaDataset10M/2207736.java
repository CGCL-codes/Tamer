package org.codehaus.groovy.grails.orm.hibernate.metaclass;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.metaclass.AbstractDynamicMethods;
import org.hibernate.SessionFactory;
import java.beans.IntrospectionException;

/**
 * A class responsible for dealing with matching and dispatching to dynamic finder methods
 *
 * @author Graeme Rocher
 * @author Steven Devijver
 *
 * @since 0.1
 *
 * Created: Aug 7, 2005
 */
public class DomainClassMethods extends AbstractDynamicMethods {

    public static final String ERRORS_PROPERTY = "errors";

    public DomainClassMethods(GrailsApplication application, Class theClass, SessionFactory sessionFactory, ClassLoader classLoader) throws IntrospectionException {
        super(theClass);
        addStaticMethodInvocation(new FindAllByPersistentMethod(application, sessionFactory, classLoader));
        addStaticMethodInvocation(new FindByPersistentMethod(application, sessionFactory, classLoader));
        addStaticMethodInvocation(new CountByPersistentMethod(application, sessionFactory, classLoader));
        addStaticMethodInvocation(new ListOrderByPersistentMethod(sessionFactory, classLoader));
    }
}
