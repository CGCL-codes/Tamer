package org.initialize4j.service.modify;

import org.initialize4j.provide.Provider;
import org.initialize4j.util.InitializationUtil;

/**
 * {@link Modifier} implementation using a {@link Provider} to change values.
 *
 * @author <a href="hillger.t@gmail.com">hillger.t</a>
 */
class ProviderModifier implements Modifier {

    private final Provider<?> provider;

    ProviderModifier(Provider<?> provider) {
        this.provider = provider;
    }

    /**
   * {@inheritDoc}
   */
    public void change(Object bean, String fieldName) {
        try {
            Object value = provider.provide();
            InitializationUtil.setProperty(bean, fieldName, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
