package net.sf.twip.jmock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.sf.twip.TwipExtension;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A {@link TwipExtension} that creates a
 * {@link org.jmock.integration.junit4.JMock}-{@link org.jmock.Mockery} and
 * mocks all member variables of the test class that are annotated by
 * {@link Mock}. If there is already a Mockery defined, use that. If it is just
 * declared, but not initialized, do that.
 */
public class JMockExtension implements TwipExtension {

    private static final ThreadLocal<Mockery> MOCKERY = new ThreadLocal<Mockery>();

    private List<Field> findMocks(final Object testInstance) {
        final List<Field> mocks = new ArrayList<Field>();
        for (final Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Mock.class) == null) continue;
            field.setAccessible(true);
            mocks.add(field);
        }
        return mocks;
    }

    private Mockery findOrCreateMockery(Object testIntance) {
        for (Class<?> c = testIntance.getClass(); c != null; c = c.getSuperclass()) {
            for (final Field f : c.getDeclaredFields()) {
                if (Mockery.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    try {
                        Mockery mockery = (Mockery) f.get(testIntance);
                        if (mockery == null) {
                            mockery = new JUnit4Mockery();
                            f.set(testIntance, mockery);
                        }
                        return mockery;
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }

    public Statement wrap(boolean beforeSetup, FrameworkMethod method, final Object testInstance, final Statement wrappedStatement) {
        if (!beforeSetup) return wrappedStatement;
        final Mockery mockery = findOrCreateMockery(testInstance);
        if (mockery == null) return wrappedStatement;
        MOCKERY.set(mockery);
        for (final Field field : findMocks(testInstance)) {
            final String name = field.getName();
            final Class<?> type = field.getType();
            try {
                field.set(testInstance, mockery.mock(type, name));
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return new JMockStatement(wrappedStatement, mockery);
    }
}
