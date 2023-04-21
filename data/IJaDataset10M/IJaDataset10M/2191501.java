package org.eiichiro.jazzmaster.kernel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.eiichiro.reverb.lang.UncheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Assembler} assembles an instance from the specified {@code Class} and 
 * {@code Property}. This class is used by {@code Kernel} for component 
 * creation with dependency injection.
 * If you want to use this class for component creation, you should control this 
 * class via {@code Kernel} with the 
 * <a href="http://www.martinfowler.com/bliki/FluentInterface.html">
 * fluent interface</a>. For example: 
 * <pre>
 * package jazzmaster.hello;
 * 
 * import org.eiichiro.jazzmaster.kernel.Injected;
 * import org.eiichiro.jazzmaster.kernel.Kernel;
 * import org.eiichiro.jazzmaster.kernel.Property;
 * import org.eiichiro.jazzmaster.service.Implementation;
 * 
 * {@code @Implementation}
 * public class HelloImpl implements Hello {
 * 
 *     {@code @Injected} private Kernel kernel;
 * 
 *     public Message sayHello() {
 *         // If you want to use kernel independently from Jazzmaster framework, 
 *         // you need to instantiate Kernel directly.
 *         return kernel.assemble(Message.class).with(new Property&lt;String&gt;("Hello, Jazzmaster!")).getInstance();
 *     }
 * 
 *     public void setKernel(Kernel kernel) {
 *         this.kernel = kernel;
 *     }
 * 
 * }
 * </pre>
 * {@code Message} class is: 
 * <pre>
 * package jazzmaster.hello;
 * 
 * public class Message {
 * 
 *     private String message;
 * 
 *     public String getMessage() {
 *         return message;
 *     }
 * 
 *     public void setMessage(String message) {
 *         this.message = message;
 *     }
 * 
 * }
 * </pre>
 * Jazzmaster dependency injection is usually <b>typesafe</b>. And it also 
 * supports name-based binding. The property binding processes the following 
 * order: 
 * <ol>
 * <li>
 * Assembler gets all of the declared field that can be visible from every 
 * instance hierarchy and iterates each of them.
 * </li>
 * <li>
 * For each field, assembler iterates each of the specified properties and binds 
 * the immediate property.
 * </li>
 * <li>
 * If the property has the name and the name doesn't match the field's one, 
 * binding process for the current property is skipped and the next property 
 * will be processed. When the property has its name, it never matches the field 
 * that has a different name.
 * </li>
 * <li>
 * Assembler gets property's type by calling {@code Property#getType()}. When 
 * this method returns {@code null}, assembler gets all of the class hierarchy 
 * of property's value types, every superclasses, every implementing interfaces. 
 * If field's declaring type matches to any one of these classes, the binding 
 * process is continued.
 * Thus, for example, when a field is declared as {@code java.lang.Object} and a 
 * property is declared as {@code java.lang.String}, this property 
 * <b>MATCHES</b> this field. Because {@code java.lang.String} is 
 * {@code java.lang.Object} ({@code java.lang.Object} is superclass of 
 * {@code String}).
 * </li>
 * <li>
 * If the property type and field type match each other, assembler gets binding 
 * annotation from the property and indicates each binding annotation is 
 * declared on the field. When all binding annotations are declared on the field, 
 * assembler considers that the property matches the field and bind property 
 * value into field value.
 * </li>
 * </ol>
 * When one property matches several fields, assembler binds the property into 
 * all of the matched fields. If you want to qualify the binding, you should use 
 * property name of {@code Binding} annotations.
 *
 * @see Kernel
 * @see Property
 * @see Binding
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Assembler<T> {

    private Logger logger = LoggerFactory.getLogger(Assembler.class);

    private Class<T> clazz;

    private T instance;

    private Set<Property<?>> properties = new HashSet<Property<?>>();

    private boolean assembled = false;

    /**
	 * Constructs a new {@code Assembler} instance with the specified 
	 * {@code Class}.
	 * 
	 * @param clazz The class to be instantiated.
	 */
    public Assembler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("'clazz' must not be [" + clazz + "]");
        }
        this.clazz = clazz;
    }

    /**
	 * Constructos a new {@code Assembler} instance with the specified instance.
	 * 
	 * @param instance The instance to be assembled.
	 */
    @SuppressWarnings("unchecked")
    public Assembler(T instance) {
        if (instance == null) {
            throw new IllegalArgumentException("'instance' must not be [" + instance + "]");
        }
        clazz = (Class<T>) instance.getClass();
        this.instance = instance;
    }

    /**
	 * Assembles instance from the specified class or instance with the 
	 * specified properties. If instance is still specified (set on its field), 
	 * this method DOESN'T instantiate again but reassembles still specified one. 
	 * The other side, if class is still specified, this method instantiate the 
	 * class instance newly. Once this method invoked, the assembled instance is 
	 * cached, so that {@code Assembler#getInstance()} always returns the same 
	 * instance. 
	 * This method uses <a href="http://commons.apache.org/beanutils/">
	 * Commons BeanUtils</a> for property binding, so you need to declare the 
	 * setter method for the field that a property need to be bound. If setter 
	 * method for the field has not declared, this method attempts to set the 
	 * property value into the field directly. If any exceptions are thrown from 
	 * BeanUtils, this method DOESN'T rethrow it and logs it to warning or debug 
	 * message.
	 * 
	 * @see PropertyUtils#setProperty(Object, String, Object)
	 */
    @SuppressWarnings("unchecked")
    public void assemble() {
        T instance = null;
        if (this.instance != null) {
            instance = this.instance;
        } else {
            try {
                instance = clazz.newInstance();
            } catch (IllegalAccessException e) {
                throw new UncheckedException(e);
            } catch (InstantiationException e) {
                throw new UncheckedException(e);
            }
        }
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(clazz);
        for (Class<?> superclass : superclasses) {
            fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
        }
        for (Field field : fields) {
            for (Property<?> property : properties) {
                if (matches(field, property)) {
                    try {
                        PropertyUtils.setProperty(instance, field.getName(), property.getValue());
                    } catch (IllegalAccessException e) {
                        logger.warn(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        logger.warn(e.getMessage(), e);
                    } catch (NoSuchMethodException e) {
                        logger.debug("Cannot find setter method for field [" + field + "]: " + e.getMessage());
                        field.setAccessible(true);
                        try {
                            field.set(instance, property.getValue());
                        } catch (IllegalArgumentException e1) {
                            logger.warn(e.getMessage(), e);
                        } catch (IllegalAccessException e1) {
                            logger.warn(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        this.instance = instance;
        assembled = true;
    }

    /**
	 * Sets one property which to assemble with.
	 * This method returns the current {@code Assembler} instance ({@code this} 
	 * pointer) for fluent interface programming.
	 * 
	 * @param property The property to assemble with.
	 * @return The current {@code Assembler} instance.
	 */
    public Assembler<T> with(Property<?> property) {
        Set<Property<?>> properties = new HashSet<Property<?>>(1);
        properties.add(property);
        return with(properties);
    }

    /**
	 * Sets the {@code Set} view of {@code Property} which to assemble with.
	 * This method returns the current {@code Assembler} instance for fluent 
	 * interface programming.
	 * 
	 * @param properties The properties to assemble with.
	 * @return The current {@code Assembler} instance.
	 */
    public Assembler<T> with(Set<Property<?>> properties) {
        this.properties.addAll(properties);
        assembled = false;
        return this;
    }

    /**
	 * Returns the assembled instance.
	 * If {@code #assemble()} has not called yet, this method calls it then 
	 * returns the assembled instance. This method is often invoked at end of 
	 * fluent interface invocation flow. For example: 
	 * <pre></pre>
	 * 
	 * @return The instance assembled by this {@code Assembler}.
	 */
    public T getInstance() {
        if (!assembled) {
            assemble();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private boolean matches(Field field, Property<?> property) {
        if (property.getName() != null && !field.getName().equals(property.getName())) {
            return false;
        }
        List<Class<?>> types = new ArrayList<Class<?>>();
        if (property.getType() != null) {
            types.add(property.getType());
        } else {
            Class<?> clazz = property.getValue().getClass();
            types.add(clazz);
            types.addAll(ClassUtils.getAllSuperclasses(clazz));
            types.addAll(ClassUtils.getAllInterfaces(clazz));
        }
        for (Class<?> type : types) {
            if (field.getType().equals(type)) {
                Set<Annotation> bindings = getBindings(property.getAnnotations());
                for (Annotation binding : bindings) {
                    Annotation[] annotations = field.getAnnotations();
                    boolean matches = false;
                    for (Annotation annotation : annotations) {
                        if (binding.equals(annotation)) {
                            matches = true;
                        }
                    }
                    if (!matches) {
                        return false;
                    }
                }
                logger.debug("Field [" + field + "] matches property [" + property + "]");
                return true;
            }
        }
        return false;
    }

    private Set<Annotation> getBindings(Set<Annotation> annotations) {
        Set<Annotation> bindings = new HashSet<Annotation>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getAnnotation(Binding.class) != null) {
                bindings.add(annotation);
            }
        }
        return bindings;
    }
}
