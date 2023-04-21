package org.codehaus.groovy.grails.orm.hibernate.metaclass;

import groovy.lang.MissingMethodException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import java.util.regex.Pattern;

/**
 * Method that allows you to count the number of instances in the database
 *
 * eg. Account.count() // returns how many in total
 *
 *
 * @author Graeme Rocher
 * @since 17-Feb-2006
 */
public class CountPersistentMethod extends AbstractStaticPersistentMethod {

    private static final Pattern METHOD_PATTERN = Pattern.compile("^count$");

    private static final String METHOD_SIGNATURE = "count";

    public CountPersistentMethod(SessionFactory sessionFactory, ClassLoader classLoader) {
        super(sessionFactory, classLoader, METHOD_PATTERN);
    }

    protected Object doInvokeInternal(final Class clazz, String methodName, Object[] arguments) {
        HibernateTemplate t = getHibernateTemplate();
        if (arguments.length == 0) {
            return t.execute(new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException {
                    Criteria c = session.createCriteria(clazz);
                    c.setProjection(Projections.rowCount());
                    return c.uniqueResult();
                }
            });
        }
        throw new MissingMethodException(METHOD_SIGNATURE, clazz, arguments);
    }
}
