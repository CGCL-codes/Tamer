package org.joda.beans.impl.flexi;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.joda.beans.MetaBean;
import org.joda.beans.Property;
import org.joda.beans.PropertyMap;
import org.joda.beans.impl.BasicProperty;

/**
 * A map of properties for a {@code FlexiBean}.
 * 
 * @author Stephen Colebourne
 */
final class FlexiPropertyMap extends AbstractMap<String, Property<Object>> implements PropertyMap {

    /** The bean. */
    private final FlexiBean bean;

    /**
     * Factory to create a property map avoiding duplicate generics.
     * 
     * @param bean  the bean
     */
    static FlexiPropertyMap of(FlexiBean bean) {
        return new FlexiPropertyMap(bean);
    }

    /**
     * Creates a property map.
     * 
     * @param bean  the bean that the property is bound to, not null
     */
    private FlexiPropertyMap(FlexiBean bean) {
        if (bean == null) {
            throw new NullPointerException("Bean must not be null");
        }
        this.bean = bean;
    }

    @Override
    public int size() {
        return bean.size();
    }

    @Override
    public boolean containsKey(Object obj) {
        return bean.data.containsKey(obj);
    }

    @Override
    public Property<Object> get(Object obj) {
        return containsKey(obj) ? bean.property(obj.toString()) : null;
    }

    @Override
    public Set<String> keySet() {
        return bean.data.keySet();
    }

    @Override
    public Set<Entry<String, Property<Object>>> entrySet() {
        if (size() == 0) {
            return Collections.emptySet();
        }
        final MetaBean metaBean = bean.metaBean();
        return new AbstractSet<Entry<String, Property<Object>>>() {

            @Override
            public int size() {
                return bean.size();
            }

            @Override
            public Iterator<Entry<String, Property<Object>>> iterator() {
                final Iterator<String> it = bean.data.keySet().iterator();
                return new Iterator<Entry<String, Property<Object>>>() {

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Entry<String, Property<Object>> next() {
                        String name = it.next();
                        Property<Object> prop = BasicProperty.of(bean, FlexiMetaProperty.of(metaBean, name));
                        return new SimpleImmutableEntry<String, Property<Object>>(name, prop);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Unmodifiable");
                    }
                };
            }
        };
    }

    @Override
    public Map<String, Object> flatten() {
        return bean.toMap();
    }
}
