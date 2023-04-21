package photorganizer.common.bean;

import java.util.Properties;
import photorganizer.common.exception.TechnicalException;

public abstract class LocatorAbstract implements Locator {

    private final Locator delegate;

    private final Properties properties;

    public LocatorAbstract(Locator delegate) {
        this(delegate, null);
    }

    public LocatorAbstract(Locator delegate, Properties properties) {
        this.delegate = delegate;
        this.properties = properties;
    }

    public <T> T get(Class<T> beanClass) {
        return delegate.get(beanClass);
    }

    protected <T> T get(Class<T> beanClass, String name) {
        try {
            Object bean = delegate.get(Class.forName(name));
            return beanClass.cast(bean);
        } catch (ClassNotFoundException e) {
            throw new TechnicalException(e);
        }
    }

    protected String getProperty(String key) {
        return properties == null ? null : properties.getProperty(key);
    }
}
