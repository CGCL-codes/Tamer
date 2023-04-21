package org.doxla.spring.automock2.resolver;

import java.util.Collection;
import org.doxla.spring.automock2.AutoMockExposingPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Resolve a {@link Collection} of Classes to mock.
 * Typically passed to a {@link AutoMockExposingPostProcessor} to decide
 * which {@link Class}es to mock.
 *
 * @author danoxlade
 */
public interface MockClassResolver {

    Collection<Class<?>> resolveClassesToMock(ConfigurableListableBeanFactory beanFactory);
}
