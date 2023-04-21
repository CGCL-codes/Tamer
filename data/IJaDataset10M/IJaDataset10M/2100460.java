package org.databene.commons.bean;

import org.databene.commons.BeanUtil;

/**
 * Default implementation of the {@link ClassProvider} interface.
 * It forwards the call to {@link BeanUtil}.<br/>
 * <br/>
 * Created at 16.11.2008 07:05:10
 * @since 0.4.6
 * @author Volker Bergmann
 */
public class DefaultClassProvider implements ClassProvider {

    private static DefaultClassProvider instance = new DefaultClassProvider();

    public static ClassProvider getInstance() {
        return instance;
    }

    public Class<?> forName(String className) {
        return BeanUtil.forName(className);
    }

    public static Class<?> resolveByObjectOrDefaultInstance(String className, Object context) {
        ClassProvider classProvider;
        if (context instanceof ClassProvider) classProvider = (ClassProvider) context; else classProvider = DefaultClassProvider.getInstance();
        return classProvider.forName(className);
    }
}
