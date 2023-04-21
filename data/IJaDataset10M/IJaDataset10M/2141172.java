package cz.cvut.felk.cig.configuration;

import cz.cvut.felk.cig.jcool.core.annotation.Configurable;
import cz.cvut.felk.cig.jcool.core.annotation.Option;
import cz.cvut.felk.cig.jcool.core.annotation.Range;
import cz.cvut.felk.cig.jcool.core.annotation.Switch;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

/**
 * Annotation configuration is a representation of configurable fields declared 
 * on an object.
 *
 * @author ytoh
 */
public class AnnotationConfiguration {

    static final Logger logger = Logger.getLogger(AnnotationConfiguration.class);

    private ConfigurableFieldFactory factory;

    private Object configuredObject = null;

    private List<ConfigurableField> configurableFields;

    /**
     * Constructs an empty AnnotationConfiguration.
     *
     * @param o
     * @param factory
     */
    private AnnotationConfiguration(Object o, ConfigurableFieldFactory factory) {
        this.configuredObject = o;
        this.factory = factory;
        configurableFields = new ArrayList<ConfigurableField>();
    }

    /**
     * Constructs an AnnotationConfiguration for a suppied object and analyzes
     * the configuration candidate fields.
     *
     * @param o
     * object annotated with annotations marking configuration points
     * @param provider
     *
     * @return an instance of AnnotationConfiguration holding references
     * to configurable fields declared in <code>o</code>
     */
    public static AnnotationConfiguration forObject(Object o, ConfigurationProvider provider) {
        ConfigurableFieldFactory factory = new ConfigurableFieldFactory(provider);
        factory.register(Integer.TYPE, Range.class, new RangeField());
        factory.register(Integer.TYPE, Option.class, new OptionField());
        factory.register(Double.TYPE, Range.class, new RangeField());
        factory.register(String.class, Option.class, new OptionField());
        factory.register(Boolean.TYPE, Switch.class, new SwitchField());
        AnnotationConfiguration c = new AnnotationConfiguration(o, factory);
        c.analyzeFields(o);
        return c;
    }

    /**
     * Filters the object field to retrieve the right fields annotated
     * with the right configuration annotations.
     * Setters have to be present for these fields to be configured.
     *
     * @param o object to search
     */
    private void analyzeFields(final Object o) {
        for (Field f : o.getClass().getDeclaredFields()) {
            List<Annotation> present = (List<Annotation>) CollectionUtils.select(Arrays.asList(f.getAnnotations()), new Predicate() {

                public boolean evaluate(Object o) {
                    return ((Annotation) o).annotationType().isAnnotationPresent(Configurable.class);
                }
            });
            if (present.size() > 0 && PropertyUtils.isWriteable(o, f.getName())) {
                ConfigurableField cf = factory.from(configuredObject, f, present.get(0));
                if (cf != null) {
                    configurableFields.add(cf);
                }
            }
        }
    }

    /**
     * @return returns the underlying object
     */
    public Object getConfiguredObject() {
        return configuredObject;
    }

    /**
     * @return a list of ConfigurableFields representing the correctly annotated
     * writeable fields of the underlying object
     */
    public List<ConfigurableField> getConfigurableFields() {
        return configurableFields;
    }
}
