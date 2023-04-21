package org.codehaus.groovy.grails.orm.hibernate.metaclass;

import groovy.lang.MissingMethodException;
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.hibernate.SessionFactory;
import org.springframework.beans.SimpleTypeConverter;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * A static method that checks whether an entity exists in the database for the specified id
 *
 * eg. Account.exsits(1)
 * 
 * @author Graeme Rocher
 * @since 17-Feb-2006
 */
public class ExistsPersistentMethod extends AbstractStaticPersistentMethod {

    private static final Pattern METHOD_PATTERN = Pattern.compile("^exists$");

    private static final String METHOD_SIGNATURE = "exists";

    private GrailsApplication application;

    private SimpleTypeConverter typeConverter = new SimpleTypeConverter();

    public ExistsPersistentMethod(GrailsApplication application, SessionFactory sessionFactory, ClassLoader classLoader) {
        super(sessionFactory, classLoader, METHOD_PATTERN);
        this.application = application;
    }

    protected Object doInvokeInternal(Class clazz, String methodName, Object[] arguments) {
        if (arguments.length == 0) throw new MissingMethodException(METHOD_SIGNATURE, clazz, arguments);
        Object arg = arguments[0];
        if (arg == null) return null;
        GrailsDomainClass domainClass = (GrailsDomainClass) this.application.getArtefact(DomainClassArtefactHandler.TYPE, clazz.getName());
        if (domainClass != null) {
            Class identityType = domainClass.getIdentifier().getType();
            if (!identityType.isAssignableFrom(arg.getClass())) {
                if (arg instanceof Number && Long.class.equals(identityType)) {
                    arg = DefaultGroovyMethods.toLong((Number) arg);
                } else {
                    arg = typeConverter.convertIfNecessary(arg, identityType);
                }
            }
        }
        return Boolean.valueOf(super.getHibernateTemplate().get(clazz, (Serializable) arg) != null);
    }
}
