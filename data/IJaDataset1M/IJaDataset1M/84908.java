package org.thymeleaf.spring3.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.util.Validate;

/**
 * <p>
 *   Special object made available by {@link SpringWebContext} to templates
 *   in Spring MVC applications in order to access beans in the Application Context.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class Beans implements Map<String, Object> {

    private final ApplicationContext ctx;

    public Beans(final ApplicationContext ctx) {
        super();
        Validate.notNull(ctx, "Application Context cannot be null");
        this.ctx = ctx;
    }

    public boolean containsKey(final Object key) {
        Validate.notNull(key, "Key cannot be null");
        return this.ctx.containsBean(key.toString());
    }

    public Object get(final Object key) {
        Validate.notNull(key, "Key cannot be null");
        return this.ctx.getBean(key.toString());
    }

    public Set<String> keySet() {
        return new LinkedHashSet<String>(Arrays.asList(this.ctx.getBeanDefinitionNames()));
    }

    public int size() {
        throw new UnsupportedOperationException("Method \"size\" not supported in Beans object");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Method \"isEmpty\" not supported in Beans object");
    }

    public boolean containsValue(final Object value) {
        throw new UnsupportedOperationException("Method \"containsValue\" not supported in Beans object");
    }

    public Object put(final String key, final Object value) {
        throw new UnsupportedOperationException("Method \"put\" not supported in Beans object");
    }

    public void putAll(final Map<? extends String, ? extends Object> m) {
        throw new UnsupportedOperationException("Method \"putAll\" not supported in Beans object");
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException("Method \"remove\" not supported in Beans object");
    }

    public void clear() {
        throw new UnsupportedOperationException("Method \"clear\" not supported in Beans object");
    }

    public Collection<Object> values() {
        throw new UnsupportedOperationException("Method \"values\" not supported in Beans object");
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException("Method \"entrySet\" not supported in Beans object");
    }
}
