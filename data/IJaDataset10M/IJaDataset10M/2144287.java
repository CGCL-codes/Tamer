package org.codehaus.groovy.grails.orm.hibernate.metaclass;

import org.hibernate.SessionFactory;
import java.util.regex.Pattern;

/**
 * A method that allows you to discard changes to a domain class 
 * 
 * @author Graeme Rocher
 * @since 0.2
 */
public class DiscardPersistentMethod extends AbstractDynamicPersistentMethod {

    public static final String METHOD_SIGNATURE = "discard";

    public static final Pattern METHOD_PATTERN = Pattern.compile('^' + METHOD_SIGNATURE + '$');

    public DiscardPersistentMethod(SessionFactory sessionFactory, ClassLoader classLoader) {
        super(METHOD_PATTERN, sessionFactory, classLoader);
    }

    protected Object doInvokeInternal(Object target, Object[] arguments) {
        if (getHibernateTemplate().contains(target)) {
            getHibernateTemplate().evict(target);
        }
        return target;
    }
}
