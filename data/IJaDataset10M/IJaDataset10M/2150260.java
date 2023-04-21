package se.trillian.goodies.test;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Used to create dummy implementations of interfaces or classes for unit tests. 
 * Any unimplemented getters and setters defined by the interface or class will
 * be implemented automatically. Also, the created dummy object will
 * implements the {@link Bean} interface which can be used to set read-only
 * properties on the dummy or get write-only properties from the dummy.
 * <p>
 * Use it like this:
 * </p>
 * <p>
 * <pre>
 *   // The interface we want to create a dummy for
 *   interface User {
 *     String getFirstName();
 *     String getLastName();
 *     String getDisplayName();
 *   }
 *   
 *   // The base class for the dummy objects used in the test
 *   abstract class DUser implements User, Bean&lt;DUser&gt; {
 *     public String getDisplay() {
 *       // This property is derived from firstName and lastName
 *       return getFirstName() + " " + getLastName();
 *     }
 *   }
 *   
 *   DUser user = new Dummy<DUser>() {{
 *     // Set the values of the firstName and lastName properties
 *     set("firstName", "John");
 *     set("lastName", "Smith");
 *   }}.getObject();
 *   
 *   // Prints out "John Smith"
 *   System.out.println(user.getDisplayName());
 *   
 *   // Now let's change the read-only firstName property
 *   user._set("firstName", "Adam");
 *   
 *   // Prints out "Adam Smith"
 *   System.out.println(user.getDisplayName());
 * </pre>
 * 
 * </p>
 *
 * @author Niklas Therning
 * @version $Id$
 */
public abstract class Dummy<T> {

    protected final T object;

    @SuppressWarnings("unchecked")
    public Dummy(Object... args) {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        object = (T) BeanInterceptor.newInstance((Class) type.getActualTypeArguments()[0], args);
    }

    public T getObject() {
        return object;
    }

    @SuppressWarnings("unchecked")
    protected T set(String name, Object value) {
        ((Bean<T>) object)._set(name, value);
        return object;
    }

    protected T set(NameValue<String, Object> first, NameValue<String, Object>... rest) {
        set(first.getName(), first.getValue());
        for (NameValue<String, Object> nv : rest) {
            set(nv.getName(), nv.getValue());
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    protected Object get(String name) {
        return ((Bean<T>) object)._get(name);
    }

    protected NameValue<String, Object> p(String name, Object value) {
        return new NameValue<String, Object>(name, value);
    }

    protected NameValue<Object, Object> entry(Object name, Object value) {
        return new NameValue<Object, Object>(name, value);
    }

    protected <U> List<U> list(U... elements) {
        return Arrays.asList(elements);
    }

    protected <U> U[] array(U... elements) {
        return elements;
    }

    protected Map<Object, Object> map(NameValue<Object, Object>... entries) {
        Map<Object, Object> map = new TreeMap<Object, Object>();
        for (NameValue<Object, Object> entry : entries) {
            map.put(entry.getName(), entry.getValue());
        }
        return map;
    }

    protected static class NameValue<N, V> {

        private final N name;

        private final V value;

        public NameValue(N name, V value) {
            this.name = name;
            this.value = value;
        }

        public N getName() {
            return name;
        }

        public V getValue() {
            return value;
        }
    }
}
