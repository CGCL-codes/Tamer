package org.dozer.vo.jaxb.employee;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * net.sf.dozer.util.mapping.vo.jaxb.employee package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 * 
 */
public class ObjectFactory extends org.dozer.vo.jaxb.employee.impl.runtime.DefaultJAXBContextImpl {

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(16, 0.75F);

    private static java.util.HashMap rootTagMap = new java.util.HashMap();

    public static final org.dozer.vo.jaxb.employee.impl.runtime.GrammarInfo grammarInfo = new org.dozer.vo.jaxb.employee.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (org.dozer.vo.jaxb.employee.ObjectFactory.class));

    public static final java.lang.Class version = (org.dozer.vo.jaxb.employee.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((org.dozer.vo.jaxb.employee.EmployeeWithInnerClassType.class), "net.sf.dozer.util.mapping.vo.jaxb.employee.impl.EmployeeWithInnerClassTypeImpl");
        defaultImplementations.put((org.dozer.vo.jaxb.employee.EmployeeWithInnerClass.class), "net.sf.dozer.util.mapping.vo.jaxb.employee.impl.EmployeeWithInnerClassImpl");
        defaultImplementations.put((org.dozer.vo.jaxb.employee.EmployeeType.class), "net.sf.dozer.util.mapping.vo.jaxb.employee.impl.EmployeeTypeImpl");
        defaultImplementations.put((org.dozer.vo.jaxb.employee.Employee.class), "net.sf.dozer.util.mapping.vo.jaxb.employee.impl.EmployeeImpl");
        defaultImplementations.put((org.dozer.vo.jaxb.employee.EmployeeWithInnerClassType.AddressType.class), "net.sf.dozer.util.mapping.vo.jaxb.employee.impl.EmployeeWithInnerClassTypeImpl.AddressTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://jaxb.vo..dozer.sf.net/Employee", "EmployeeWithInnerClass"), (org.dozer.vo.jaxb.employee.EmployeeWithInnerClass.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://jaxb.vo..dozer.sf.net/Employee", "Employee"), (org.dozer.vo.jaxb.employee.Employee.class));
    }

    /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
   * net.sf.dozer.util.mapping.vo.jaxb.employee
   * 
   */
    public ObjectFactory() {
        super(grammarInfo);
    }

    /**
   * Create an instance of the specified Java content interface.
   * 
   * @param javaContentInterface
   *          the Class object of the javacontent interface to instantiate
   * @return a new instance
   * @throws JAXBException
   *           if an error occurs
   */
    @Override
    public java.lang.Object newInstance(java.lang.Class javaContentInterface) throws javax.xml.bind.JAXBException {
        return super.newInstance(javaContentInterface);
    }

    /**
   * Get the specified property. This method can only be used to get provider specific properties. Attempting to get an
   * undefined property will result in a PropertyException being thrown.
   * 
   * @param name
   *          the name of the property to retrieve
   * @return the value of the requested property
   * @throws PropertyException
   *           when there is an error retrieving the given property or value
   */
    @Override
    public java.lang.Object getProperty(java.lang.String name) throws javax.xml.bind.PropertyException {
        return super.getProperty(name);
    }

    /**
   * Set the specified property. This method can only be used to set provider specific properties. Attempting to set an
   * undefined property will result in a PropertyException being thrown.
   * 
   * @param value
   *          the value of the property to be set
   * @param name
   *          the name of the property to retrieve
   * @throws PropertyException
   *           when there is an error processing the given property or value
   */
    @Override
    public void setProperty(java.lang.String name, java.lang.Object value) throws javax.xml.bind.PropertyException {
        super.setProperty(name, value);
    }

    /**
   * Create an instance of EmployeeWithInnerClassType
   * 
   * @throws JAXBException
   *           if an error occurs
   */
    public org.dozer.vo.jaxb.employee.EmployeeWithInnerClassType createEmployeeWithInnerClassType() throws javax.xml.bind.JAXBException {
        return new org.dozer.vo.jaxb.employee.impl.EmployeeWithInnerClassTypeImpl();
    }

    /**
   * Create an instance of EmployeeWithInnerClass
   * 
   * @throws JAXBException
   *           if an error occurs
   */
    public org.dozer.vo.jaxb.employee.EmployeeWithInnerClass createEmployeeWithInnerClass() throws javax.xml.bind.JAXBException {
        return new org.dozer.vo.jaxb.employee.impl.EmployeeWithInnerClassImpl();
    }

    /**
   * Create an instance of EmployeeType
   * 
   * @throws JAXBException
   *           if an error occurs
   */
    public org.dozer.vo.jaxb.employee.EmployeeType createEmployeeType() throws javax.xml.bind.JAXBException {
        return new org.dozer.vo.jaxb.employee.impl.EmployeeTypeImpl();
    }

    /**
   * Create an instance of Employee
   * 
   * @throws JAXBException
   *           if an error occurs
   */
    public org.dozer.vo.jaxb.employee.Employee createEmployee() throws javax.xml.bind.JAXBException {
        return new org.dozer.vo.jaxb.employee.impl.EmployeeImpl();
    }

    /**
   * Create an instance of EmployeeWithInnerClassTypeAddressType
   * 
   * @throws JAXBException
   *           if an error occurs
   */
    public org.dozer.vo.jaxb.employee.EmployeeWithInnerClassType.AddressType createEmployeeWithInnerClassTypeAddressType() throws javax.xml.bind.JAXBException {
        return new org.dozer.vo.jaxb.employee.impl.EmployeeWithInnerClassTypeImpl.AddressTypeImpl();
    }
}
