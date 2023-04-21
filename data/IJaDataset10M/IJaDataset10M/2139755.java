package com.syrtsov.ddao.factory;

import com.syrtsov.ddao.UseStatementFactory;
import java.lang.reflect.Method;

/**
 * StatementFactoryManager maintains lazy loaded cache of mappings of method objects
 * to StatementFactory implementations.
 * <p/>
 * Created by Pavel Syrtsov
 * Date: Nov 3, 2007
 * Time: 5:11:13 PM
 */
public class StatementFactoryManager {

    /**
     * default implementation class
     */
    public static Class<? extends StatementFactory> defaultStatementFactory = DefaultStatementFactory.class;

    public static StatementFactory createStatementFactory(Method method, String sql) throws StatementFactoryException {
        Class<? extends StatementFactory> statementFactoryClass = getFactoryClass(method);
        StatementFactory sf;
        try {
            sf = statementFactoryClass.newInstance();
        } catch (Exception e) {
            throw new StatementFactoryException("Failed to create statement factory for " + method);
        }
        sf.init(sql, method);
        return sf;
    }

    private static Class<? extends StatementFactory> getFactoryClass(Method method) {
        UseStatementFactory useStatementFactory = method.getAnnotation(UseStatementFactory.class);
        if (useStatementFactory != null) {
            return useStatementFactory.value();
        }
        Class<?> declaringClass = method.getDeclaringClass();
        useStatementFactory = declaringClass.getAnnotation(UseStatementFactory.class);
        if (useStatementFactory != null) {
            return useStatementFactory.value();
        }
        return defaultStatementFactory;
    }
}
