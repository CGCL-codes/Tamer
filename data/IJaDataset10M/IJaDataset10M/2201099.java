package org.jbeans;

import java.util.*;

/**
 * This class is used to store information about and handle getting 
 * and setting of both nested and local properties. This class 
 * functions very similarly its parent, {@link DynamicNestedBeanProperty 
 * DynamicNestedBeanProperty}, except for one slight difference.
 * This is that when this class is constructed, it takes a property
 * name that contains the indexes to be used for the indexed properties
 * in the nesting. For example, if we had a nesting such as:<p>
 * <code>getIndexed(1).getProperty2().getValue()</code><p>
 * you would construct an instance of this class using the property
 * String:<p>
 * <code>indexed[1].property2.value</code><p>
 * This is an important disctinction and was added to the JBeans package
 * because of speed issues and also because many applications could
 * use this static concept for initialization and many other places.
 * Adding in this class reduces the amount of work needed to parse
 * the indices out of the property String and re-format the property
 * String.<p>
 * One important thing to remember is that after this class has been
 * constructed, you can still use the 
 * {@link DynamicNestedBeanProperty#getPropertyValue(Object,List) 
 * getPropertyValue}, {@link DynamicNestedBeanProperty#getPropertyValue(Object,int []) 
 * getPropertyValue}, {@link 
 * DynamicNestedBeanProperty#setPropertyValue(Object,List,Object,boolean)
 * setPropertyValue}, and {@link 
 * DynamicNestedBeanProperty#setPropertyValue(Object,int [],Object,boolean)
 * setPropertyValue} methods from {@link DynamicNestedBeanProperty 
 * DynamicNestedBeanProperty}.
 * @author  Brian Pontarelli
 */
public class NestedBeanProperty extends DynamicNestedBeanProperty {

    List indices;

    /** 
     * Default constructor that can be used by sub-classes that want to delay the 
     * initialization of the propertyName and beanClass or that do not use these
     * members. This constructor also calls the default constructor from the
     * BaseBeanProperty super-class. This means that using this constructor will
     * not make a template method call to {@link #initialize() #initialize()}.
     */
    protected NestedBeanProperty() {
        super();
        strict = false;
    }

    /**
     * Constructs a new NestedBeanProperty that can be used to describe nested or
     * local properties, depending on the value of the property string (see
     * the class comment for more information). It also uses the class given
     * to construct the method list.
     * @param   propertyName The name of the local or nested property
     * @param   beanClass The class to build the property for
     * @throws  BeanException If anything went wrong during the parsing of the 
     *          property string or building of the property list
     */
    public NestedBeanProperty(String propertyName, Class beanClass) throws BeanException {
        this(propertyName, beanClass, false);
    }

    /**
     * Constructs a new NestedBeanProperty that can be used to describe nested or
     * local properties, depending on the value of the property string (see
     * the class comment for more information). It also uses the given fully
     * qualified name of the bean class and the propertyName to construct the
     * method list.
     * @param   propertyName The name of the local or nested property
     * @param   beanClass The fully qualified name of the bean class to build the 
     *          property for
     * @throws  BeanException If anything went wrong during the parsing of the 
     *          property string or building of the property list
     */
    public NestedBeanProperty(String propertyName, String beanClass) throws BeanException {
        this(propertyName, beanClass, false);
    }

    /**
     * Constructs a new NestedBeanProperty that can be used to describe nested or
     * local properties, depending on the value of the property string (see
     * the class comment for more information). It also uses the class given
     * to construct the method list.
     * @param   propertyName The name of the property or deep property String
     * @param   beanClass The class to build the property for
     * @param   strict Determines how this class handles cases when setting and 
     *          retrieving property values where nested properties need to be
     *          auto generated because they are null
     * @throws  BeanException If anything went wrong during the parsing of
     *          the property string or building of the property list
     */
    public NestedBeanProperty(String propertyName, Class beanClass, boolean strict) throws BeanException {
        super(propertyName, beanClass, strict);
    }

    /**
     * Constructs a new NestedBeanProperty that can be used to describe nested or
     * local properties, depending on the value of the property string (see
     * the class comment for more information). It also uses the given fully
     * qualified name of the bean class and the propertyName to construct the
     * method list.
     * @param   propertyName The name of the property or deep property String
     * @param   beanClass The fully qualified name of the bean class to build the 
     *          property for
     * @param   strict Determines how this class handles cases when setting and 
     *          retrieving property values where nested properties need to be
     *          auto generated because they are null
     * @throws  BeanException If anything went wrong during the parsing of
     *          the property string or building of the property list
     */
    public NestedBeanProperty(String propertyName, String beanClass, boolean strict) throws BeanException {
        super(propertyName, beanClass, strict);
    }

    /**
     * Initializes all the members of this class by parsing out the propertyName String
     * and creating the necessary BeanProperty class and everything else that might
     * be needed
     * @throws  BeanException If there were any problems initializing this class
     */
    protected void initialize() throws BeanException {
        properties = new ArrayList();
        indices = new ArrayList();
        StringTokenizer ts = new StringTokenizer(propertyName, ".");
        JavaBeanTools.IndexHelper ih = new JavaBeanTools.IndexHelper();
        BeanProperty prop = null;
        Class childClass = beanClass;
        while (ts.hasMoreTokens()) {
            ih = JavaBeanTools.findIndex(ts.nextToken(), ih);
            if (ih.index != -1) {
                indices.add(new Integer(ih.index));
                prop = new IndexedBeanProperty(ih.propertyName, childClass);
            } else {
                prop = new BeanProperty(ih.propertyName, childClass);
            }
            childClass = prop.getPropertyType();
            properties.add(prop);
        }
        this.propertyType = prop.getPropertyType();
    }

    /**
     * Returns the value of the local or nested property described by this
     * instance.
     * @param   bean The bean to retrieve the property on
     * @return  The value of the property
     * @throws  BeanException If there was an error getting the JavaBean property or
     *          the getter/is method threw a checked Exception or if strict is true
     *          and a null non-leaf property was encountered
     */
    public Object getPropertyValue(final Object bean) throws BeanException {
        return super.getPropertyValue(bean, indices);
    }

    /**
     * Sets the local or nested property value described by this instance. This method
     * will optionally try to convert the value parameter to the correct type for the
     * property. 
     * @param   bean The bean instance to set the property on
     * @param   value The value to set on the bean
     * @param   convert Determines whether or not the value should be converted
     *          to the correct parameter type for the property set method
     * @throws  BeanException If there was an error setting the JavaBean property or
     *          the setter method threw a checked Exception or if strict is true
     *          an a null non-leaf property was encountered
     */
    public void setPropertyValue(final Object bean, Object value, final boolean convert) throws BeanException {
        super.setPropertyValue(bean, indices, value, convert);
    }
}
