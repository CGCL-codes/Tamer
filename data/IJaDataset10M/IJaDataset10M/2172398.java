package com.genia.toolbox.spring.factory;

import org.springframework.beans.factory.FactoryBean;

/**
 * {@link FactoryBean} that allows to set system properties.
 */
public class SystemPropertyInitializer implements FactoryBean {

    /**
   * the property to set.
   */
    private String property;

    /**
   * the new value of the property.
   */
    private String value;

    /**
   * Return an instance (possibly shared or independent) of the object managed
   * by this factory.
   * <p>
   * As with a {@link org.springframework.beans.factory.BeanFactory}, this
   * allows support for both the Singleton and Prototype design pattern.
   * <p>
   * If this FactoryBean is not fully initialized yet at the time of the call
   * (for example because it is involved in a circular reference), throw a
   * corresponding
   * {@link org.springframework.beans.factory.FactoryBeanNotInitializedException}.
   * <p>
   * As of Spring 2.0, FactoryBeans are allowed to return <code>null</code>
   * objects. The factory will consider this as normal value to be used; it will
   * not throw a
   * org.springframework.beans.factory.FactoryBeanNotInitializedException in
   * this case anymore. FactoryBean implementations are encouraged to throw
   * org.springframework.beans.factory.FactoryBeanNotInitializedException
   * themselves now, as appropriate.
   * 
   * @return an instance of the bean (can be <code>null</code>)
   * @see org.springframework.beans.factory.FactoryBeanNotInitializedException
   * @see org.springframework.beans.factory.ObjectFactory#getObject()
   */
    public Object getObject() {
        System.setProperty(getProperty(), getValue());
        return null;
    }

    /**
   * Return the type of object that this FactoryBean creates, or
   * <code>null</code> if not known in advance.
   * <p>
   * This allows one to check for specific types of beans without instantiating
   * objects, for example on autowiring.
   * <p>
   * In the case of implementations that are creating a singleton object, this
   * method should try to avoid singleton creation as far as possible; it should
   * rather estimate the type in advance. For prototypes, returning a meaningful
   * type here is advisable too.
   * <p>
   * This method can be called <i>before</i> this FactoryBean has been fully
   * initialized. It must not rely on state created during initialization; of
   * course, it can still use such state if available.
   * <p>
   * <b>NOTE:</b> Autowiring will simply ignore FactoryBeans that return
   * <code>null</code> here. Therefore it is highly recommended to implement
   * this method properly, using the current state of the FactoryBean.
   * 
   * @return the type of object that this FactoryBean creates, or
   *         <code>null</code> if not known at the time of the call
   * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType
   * @see org.springframework.beans.factory.FactoryBean#getObjectType()
   */
    public Class<String> getObjectType() {
        return String.class;
    }

    /**
   * Is the object managed by this factory a singleton? That is, will
   * {@link #getObject()} always return the same object (a reference that can be
   * cached)?
   * <p>
   * <b>NOTE:</b> If a FactoryBean indicates to hold a singleton object, the
   * object returned from <code>getObject()</code> might get cached by the
   * owning BeanFactory. Hence, do not return <code>true</code> unless the
   * FactoryBean always exposes the same reference.
   * <p>
   * The singleton status of the FactoryBean itself will generally be provided
   * by the owning BeanFactory; usually, it has to be defined as singleton
   * there.
   * <p>
   * <b>NOTE:</b> This method returning <code>false</code> does not
   * necessarily indicate that returned objects are independent instances. An
   * implementation of the extended
   * {@link org.springframework.beans.factory.SmartFactoryBean} interface may
   * explicitly indicate independent instances through its
   * {@link org.springframework.beans.factory.SmartFactoryBean#isPrototype()}
   * method. Plain {@link FactoryBean} implementations which do not implement
   * this extended interface are simply assumed to always return independent
   * instances if the <code>isSingleton()</code> implementation returns
   * <code>false</code>.
   * 
   * @return if the exposed object is a singleton
   * @see #getObject()
   * @see org.springframework.beans.factory.SmartFactoryBean#isPrototype()
   * @see org.springframework.beans.factory.FactoryBean#isSingleton()
   */
    public boolean isSingleton() {
        return true;
    }

    /**
   * getter for the property property.
   * 
   * @return the property
   */
    public String getProperty() {
        return property;
    }

    /**
   * setter for the property property.
   * 
   * @param property
   *          the property to set
   */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
   * getter for the value property.
   * 
   * @return the value
   */
    public String getValue() {
        return value;
    }

    /**
   * setter for the value property.
   * 
   * @param value
   *          the value to set
   */
    public void setValue(String value) {
        this.value = value;
    }
}
