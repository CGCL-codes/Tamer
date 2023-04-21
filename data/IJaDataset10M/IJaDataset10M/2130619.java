package dash.obtain.provider.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import dash.obtain.initialize.Config;
import dash.obtain.provider.ObtainLookup;
import dash.obtain.provider.Provider;

public class SpringProvider implements Provider {

    /**
	 * The default lookup pattern for Spring Bean configuration files.
	 */
    public static final String DEFAULT_LOCATION_PATTERN = "classpath*:/**/beans.xml";

    /**
	 * The configuration key for a custom Location Pattern
	 */
    public static final String SPRING_PROVIDER_LOCATION = "spring.provider.location";

    BeanFactory beanFactory;

    public SpringProvider() {
        this(getLocationPattern());
    }

    public SpringProvider(String locationPattern) {
        this(buildBeanFactory(locationPattern));
    }

    public SpringProvider(BeanFactory beanFactory) {
        if (beanFactory == null) throw new NullPointerException("Can't allow null BeanFactory.");
        this.beanFactory = beanFactory;
    }

    public Object lookup(ObtainLookup lookup) {
        Object result = null;
        if (lookup.fieldKey != null && !"".equals(lookup.fieldKey)) {
            result = beanFactory.getBean(lookup.fieldKey, lookup.fieldClass);
        }
        return result;
    }

    static ClassPathXmlApplicationContext buildBeanFactory(String locationPattern) {
        return new ClassPathXmlApplicationContext(StringUtils.commaDelimitedListToStringArray(locationPattern));
    }

    static String getLocationPattern() {
        return Config.getProperty(SPRING_PROVIDER_LOCATION, DEFAULT_LOCATION_PATTERN);
    }
}
