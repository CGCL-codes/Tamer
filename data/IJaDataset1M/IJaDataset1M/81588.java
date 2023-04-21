package net.sf.rcpforms.experimenting.rcp.BACK;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import net.sf.rcpforms.modeladapter.configuration.EnumRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.IntegerRangeAdapter;
import net.sf.rcpforms.modeladapter.configuration.IntegerRangeAdapter.IntRange;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.BeanConverterRegistry;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;
import net.sf.rcpforms.modeladapter.tables.ObservableListBeanContentProvider;
import net.sf.rcpforms.modeladapter.util.Validate;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.IStructuredContentProvider;

public class BeanAdapter extends ModelAdapter {

    protected static final Logger LOG = Logger.getLogger(BeanAdapter.class.getName());

    protected static class PropertyChain implements IPropertyChain {

        protected String[] properties;

        protected Class<?> beanMeta;

        @SuppressWarnings("null")
        public PropertyChain(final Object beanMeta, final Object... properties) {
            Validate.isTrue(beanMeta instanceof Class);
            Validate.isTrue(properties != null && properties.length > 0);
            this.beanMeta = (Class<?>) beanMeta;
            this.properties = new String[properties.length];
            System.arraycopy(properties, 0, this.properties, 0, properties.length);
        }

        @Override
        public Object getModelMeta() {
            return beanMeta;
        }

        @Override
        public boolean equals(final Object obj) {
            boolean result = false;
            if (obj instanceof PropertyChain) {
                final PropertyChain propChain = (PropertyChain) obj;
                if (propChain.getModelMeta() == getModelMeta() && propChain.properties.length == properties.length) {
                    result = true;
                    for (int i = 0; i < properties.length; i++) {
                        if (!properties[i].equals(propChain.properties[i])) {
                            result = false;
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public Class<?> getType() {
            return getPropertyType(getPropertyDescriptor());
        }

        protected PropertyDescriptor getUnnestedPropertyDescriptor(final Class<?> beanClazz, final String property) throws IntrospectionException {
            final PropertyDescriptor descriptor = null;
            final BeanInfo beanInfo = Introspector.getBeanInfo(beanClazz);
            final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (final PropertyDescriptor propertyDescriptor : descriptors) {
                if (property.equals(propertyDescriptor.getName())) {
                    return propertyDescriptor;
                }
            }
            return descriptor;
        }

        /**
         * gets the property descriptor for the given path of properties starting from beanClass,
         * e.g. Address.class, "work","phone" will get the property descriptor of property
         * Address.getWork().getPhone()
         */
        protected PropertyDescriptor getPropertyDescriptor() {
            PropertyDescriptor desc = null;
            String path = beanMeta.getName();
            try {
                String property = null;
                Class<?> modelObjectClass = beanMeta;
                for (final Object prop : properties) {
                    property = (String) prop;
                    path += "." + property;
                    desc = getUnnestedPropertyDescriptor(modelObjectClass, property);
                    modelObjectClass = getPropertyType(desc);
                }
            } catch (final Exception ex) {
                throw new IllegalArgumentException("BeanAdapter: Exception getting property '" + path + "'");
            }
            return desc;
        }

        protected Class<?> getPropertyType(final PropertyDescriptor desc) {
            return desc.getPropertyType();
        }

        /**
         * retrieves the value of the property in the given model
         * 
         * @param model
         */
        @Override
        public Object getValue(final Object model) {
            Validate.isTrue(ModelAdapter.getAdapterForInstance(model).getMetaClass(model) == beanMeta, "Model Object has not the same metaclass which was passed for property descriptor construction");
            PropertyDescriptor descriptor = null;
            String path = beanMeta.getName();
            Object result = model;
            try {
                String property = null;
                Class<?> modelObjectClass = beanMeta;
                for (final Object prop : properties) {
                    property = (String) prop;
                    path += "." + property;
                    descriptor = getUnnestedPropertyDescriptor(modelObjectClass, property);
                    modelObjectClass = getPropertyType(descriptor);
                    try {
                        final Method method = descriptor.getReadMethod();
                        result = method.invoke(result, new Object[] {});
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                        final String message = "Error in Provider: " + getClass().getName() + " accessing property " + property + ": " + ex.getMessage();
                        LOG.severe(message);
                        throw new IllegalArgumentException(message);
                    }
                }
            } catch (final Exception ex) {
                throw new IllegalArgumentException("BeanAdapter: Exception getting property '" + path + "'");
            }
            return result;
        }

        /**
         * sets the value of the property in the given model
         * 
         * @param model
         */
        @Override
        public void setValue(final Object model, final Object value) {
            Validate.isTrue(ModelAdapter.getAdapterForInstance(model).getMetaClass(model) == beanMeta, "Model Object has not the same metaclass which was passed for property descriptor construction");
            PropertyDescriptor descriptor = null;
            String path = beanMeta.getName();
            Object result = model;
            boolean wasSet = false;
            try {
                String property = null;
                Class<?> modelObjectClass = beanMeta;
                for (final Object prop : properties) {
                    property = (String) prop;
                    path += "." + property;
                    descriptor = getUnnestedPropertyDescriptor(modelObjectClass, property);
                    modelObjectClass = getPropertyType(descriptor);
                    if (prop == properties[properties.length - 1]) {
                        try {
                            final Method method = descriptor.getWriteMethod();
                            method.invoke(result, new Object[] { value });
                            wasSet = true;
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                            final String message = "Exception in BeanAdapter: " + getClass().getName() + " setting property " + property + ": " + ex.getMessage();
                            LOG.severe(message);
                            throw new IllegalArgumentException(message);
                        }
                    } else {
                        try {
                            final Method method = descriptor.getReadMethod();
                            result = method.invoke(result, new Object[] {});
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                            final String message = "Exception in BeanAdapter: " + getClass().getName() + " accessing property " + property + ": " + ex.getMessage();
                            LOG.severe(message);
                            throw new IllegalArgumentException(message);
                        }
                    }
                }
            } catch (final Exception ex) {
                throw new IllegalArgumentException("BeanAdapter: Exception setting property '" + path + "'");
            }
            if (!wasSet) {
                throw new IllegalArgumentException("BeanAdapter: Exception setting property " + path);
            }
        }

        @Override
        public String toString() {
            String result = "PropChain(" + beanMeta.getName();
            for (final String prop : properties) {
                result += "." + prop;
            }
            return result + ")";
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }
    }

    private static BeanAdapter instance = new BeanAdapter();

    protected BeanAdapter() {
        super(new BeanConverterRegistry());
    }

    public static BeanAdapter getInstance() {
        return instance;
    }

    /**
     * a bean adapter can adapt any class
     */
    @Override
    public boolean canAdaptClass(final Object metaClassToAdapt) {
        return metaClassToAdapt instanceof Class;
    }

    @Override
    public IObservableValue getObservableValue(final Object bean, final IPropertyChain propertyChain) {
        return getObservableValue(Realm.getDefault(), bean, propertyChain);
    }

    /**
     * gets an observable value for the given property from the given data model bean
     */
    public IObservableValue getObservableValue(final Realm realm, Object bean, final IPropertyChain propertyChain) {
        final PropertyChain chain = (PropertyChain) propertyChain;
        String property = "";
        if (chain.properties.length > 1) {
            bean = this.getNestedProperty(bean, chain);
            property = chain.properties[chain.properties.length - 1];
        } else {
            property = chain.properties[0];
        }
        return BeansObservables.observeValue(realm, bean, property);
    }

    /**
     * gets a detail observable value for the given property from the given data model bean
     */
    @Override
    public IObservableValue getObservableDetailValue(final IObservableValue masterBeanObservableValue, final IPropertyChain propertyChain) {
        final PropertyChain chain = (PropertyChain) propertyChain;
        String property = "";
        Validate.isTrue(chain.properties.length == 1, "NestedProperties for master detail binding are not supported!");
        property = chain.properties[0];
        Validate.notNull(Realm.getDefault(), "Make sure a Databinding Realm is set; you should wrap your main method into Realm.runWithDefault() to provide one");
        return BeansObservables.observeDetailValue(masterBeanObservableValue.getRealm(), masterBeanObservableValue, property, chain.getType());
    }

    private Object getNestedProperty(final Object bean, final PropertyChain chain) {
        Object nestedBean = bean;
        Class<?> metaClass = chain.beanMeta;
        for (int c = 0; c < chain.properties.length - 1; ++c) {
            try {
                final PropertyDescriptor pd = chain.getUnnestedPropertyDescriptor(metaClass, chain.properties[c]);
                final Method method = pd.getReadMethod();
                method.setAccessible(true);
                nestedBean = method.invoke(nestedBean, new Object[] {});
                if (nestedBean == null) {
                    return null;
                }
                metaClass = nestedBean.getClass();
            } catch (final Exception ex) {
                ex.printStackTrace();
                final String message = "Error in Adapter: " + getClass().getName() + ". Error occurred in Object: \"" + nestedBean.toString() + "\" accessing property " + chain.properties[c] + "; check if this property is really a nested property!\n ErrorMessage is: " + ex.getMessage();
                LOG.severe(message);
                throw new IllegalArgumentException(message);
            }
        }
        return nestedBean;
    }

    @Override
    public Class<?> getMetaClass(final Object modelObjectToAdapt) {
        Validate.isTrue(!(modelObjectToAdapt instanceof Class), "Please pass an instance of the class, not the class itself");
        return modelObjectToAdapt.getClass();
    }

    /**
     * get a range adapter for a property which is used to fill combo items when combo box binding
     * is used. very convenient for enums and enum-like data types with a limited range of values.
     * <p>
     * This can even be used for choosing references to other objects via combo.
     */
    @SuppressWarnings("unchecked")
    @Override
    public IRangeAdapter getRangeAdapter(final IPropertyChain propertyChain) {
        Validate.isTrue(propertyChain instanceof PropertyChain);
        final PropertyChain chain = (PropertyChain) propertyChain;
        if (chain.getType().isEnum()) {
            return new EnumRangeAdapter((Class<? extends Enum<?>>) chain.getType());
        }
        if (chain.getType().equals(Integer.TYPE)) {
            if (chain.properties.length == 1) {
                try {
                    final Field[] fields = chain.beanMeta.getFields();
                    for (final Field f : fields) {
                        if (f.get(chain.getModelMeta()).equals(chain.properties[0])) {
                            for (final Annotation a : f.getAnnotations()) {
                                if (a instanceof IntRange) return new IntegerRangeAdapter(((IntRange) a).minValue(), ((IntRange) a).maxValue(), ((IntRange) a).step());
                            }
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    LOG.severe("Exception in BeanAdapter.getRangeAdapter() for IntRange annotation: " + e.getLocalizedMessage());
                }
            }
        }
        return null;
    }

    public IRangeAdapter getRangeAdapter(final Class<?> modelType, final String propertyName) {
        Validate.notNull(modelType, "modelType can't be null");
        final IPropertyChain propertyChain = getPropertyChain(modelType, propertyName);
        final Class<?> propertyType = (Class<?>) propertyChain.getType();
        return proGetRangeAdapter(modelType, propertyName, propertyType);
    }

    protected IRangeAdapter proGetRangeAdapter(final Class<?> modelType, final String propertyName, final Class<?> propertyType) {
        if (propertyType.isEnum()) {
            return new EnumRangeAdapter((Class<? extends Enum<?>>) propertyType);
        }
        final String[] properties = split(propertyName);
        if (propertyType.equals(Integer.TYPE)) {
            if (properties.length == 1) {
                try {
                    final Field[] fields = modelType.getFields();
                    for (final Field f : fields) {
                        if (f.get(modelType).equals(properties[0])) {
                            for (final Annotation a : f.getAnnotations()) {
                                if (a instanceof IntRange) return new IntegerRangeAdapter(((IntRange) a).minValue(), ((IntRange) a).maxValue(), ((IntRange) a).step());
                            }
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    LOG.severe("Exception in BeanAdapter.getRangeAdapter() for IntRange annotation: " + e.getLocalizedMessage());
                }
            }
        }
        return null;
    }

    @Override
    public IPropertyChain getPropertyChain(final Object beanMeta, final Object... properties) {
        return new PropertyChain(beanMeta, properties);
    }

    /**
     * checks if the given propertyPath is valid, throws an exception if not
     * 
     * @param metaClass metaclass
     * @param propertyPath path
     * @param writable if true, validation is done to ensure property is writable
     */
    @Override
    public void validatePropertyPath(final Object metaClass, final String propertyPath, final boolean writable) {
        String message = "";
        final String msgHead = "PropertyPath " + propertyPath + ": ";
        try {
            final PropertyChain propertyChain = (PropertyChain) getPropertyChain(metaClass, propertyPath);
            final PropertyDescriptor desc = propertyChain.getPropertyDescriptor();
            if (desc.getReadMethod() == null) {
                message += msgHead + " read method not available\n";
            }
            if (writable && desc.getWriteMethod() == null) {
                message += msgHead + " write method not available for editable column\n";
            }
        } catch (final IllegalArgumentException ex) {
            message += msgHead + " exception accessing property " + ex.getMessage() + "\n";
        }
    }

    /**
     * create a list content provider which reacts on changes of contained beans too
     */
    @Override
    public IStructuredContentProvider createDefaultContentProvider() {
        return new ObservableListBeanContentProvider();
    }
}
