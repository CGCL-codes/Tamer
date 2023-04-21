package net.woodstock.rockapi.loader.impl;

import java.util.Map;
import net.woodstock.rockapi.loader.Loader;
import net.woodstock.rockapi.message.ServiceMessage;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

@Deprecated
public class SpringWebLoader implements Loader {

    private static SpringWebLoader loader;

    private WebApplicationContext context;

    private SpringWebLoader() {
        super();
        this.context = ContextLoader.getCurrentWebApplicationContext();
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(Class<T> clazz) {
        Map<String, Object> map = this.context.getBeansOfType(clazz);
        if (map.size() == 0) {
            throw new RuntimeException(ServiceMessage.getMessage(SpringLoader.MESSAGE_ERROR_NOT_FOUND, clazz.getCanonicalName()));
        }
        if (map.size() > 1) {
            throw new RuntimeException(ServiceMessage.getMessage(SpringLoader.MESSAGE_ERROR_MANY_FOUND, clazz.getCanonicalName()));
        }
        return (T) map.values().iterator().next();
    }

    public Object getObject(String name) {
        return this.context.getBean(name);
    }

    public static SpringWebLoader getInstance() {
        if (SpringWebLoader.loader == null) {
            synchronized (SpringWebLoader.class) {
                if (SpringWebLoader.loader == null) {
                    SpringWebLoader.loader = new SpringWebLoader();
                }
            }
        }
        return SpringWebLoader.loader;
    }
}
