package org.soda.dpws.service;

import org.soda.dpws.wsdl.OperationInfo;

/**
 * Represents the description of a service operation fault. <p/> Faults are
 * created using the {@link OperationInfo#addFault(String)} method.
 * 
 * @author <a href="mailto:poutsma@mac.com">Arjen Poutsma</a>
 */
public class FaultInfo extends MessagePartContainer {

    private String name;

    private Class<?> exceptionClass;

    /**
   * Initializes a new instance of the <code>FaultInfo</code> class with the
   * given name and operation
   * 
   * @param name
   *          the name.
   * @param operation
   */
    public FaultInfo(String name, OperationInfo operation) {
        super(operation);
        this.name = name;
    }

    /**
   * Returns the name of the fault.
   * 
   * @return the name.
   */
    public String getName() {
        return name;
    }

    /**
   * Sets the name of the fault.
   * 
   * @param name
   *          the name.
   */
    public void setName(String name) {
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException("Invalid name [" + name + "]");
        }
        getOperation().removeFault(getName());
        this.name = name;
        getOperation().addFault(this);
    }

    /**
   * @return the exception class
   */
    public Class<?> getExceptionClass() {
        return exceptionClass;
    }

    /**
   * @param exceptionClass
   */
    public void setExceptionClass(Class<?> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }
}
