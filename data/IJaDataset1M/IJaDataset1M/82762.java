package net.sf.istcontract.wsimport.model;

import net.sf.istcontract.wsimport.api.model.CheckedException;
import net.sf.istcontract.wsimport.api.model.ExceptionType;
import net.sf.istcontract.wsimport.api.model.JavaMethod;
import net.sf.istcontract.wsimport.api.model.SEIModel;
import com.sun.xml.bind.api.TypeReference;
import com.sun.xml.bind.api.Bridge;

/**
 * CheckedException class. Holds the exception class - class that has public
 * constructor
 * 
 * <code>public WrapperException()String message, FaultBean){}</code>
 * 
 * and method
 * 
 * <code>public FaultBean getFaultInfo();</code>
 *
 * @author Vivek Pandey
 */
public final class CheckedExceptionImpl implements CheckedException {

    private final Class exceptionClass;

    private final TypeReference detail;

    private final ExceptionType exceptionType;

    private final JavaMethodImpl javaMethod;

    private String messageName;

    private String faultAction;

    /**
     * @param jm {@link JavaMethodImpl} that throws this exception
     * @param exceptionClass
     *            Userdefined or WSDL exception class that extends
     *            java.lang.Exception.
     * @param detail
     *            detail or exception bean's TypeReference
     * @param exceptionType
     *            either ExceptionType.UserDefined or
     */
    public CheckedExceptionImpl(JavaMethodImpl jm, Class exceptionClass, TypeReference detail, ExceptionType exceptionType) {
        this.detail = detail;
        this.exceptionType = exceptionType;
        this.exceptionClass = exceptionClass;
        this.javaMethod = jm;
    }

    public AbstractSEIModelImpl getOwner() {
        return javaMethod.owner;
    }

    public JavaMethod getParent() {
        return javaMethod;
    }

    /**
     * @return the <code>Class</clode> for this object
     * 
     */
    public Class getExceptionClass() {
        return exceptionClass;
    }

    public Class getDetailBean() {
        return (Class) detail.type;
    }

    public Bridge getBridge() {
        return getOwner().getBridge(detail);
    }

    public TypeReference getDetailType() {
        return detail;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getFaultAction() {
        return faultAction;
    }

    public void setFaultAction(String faultAction) {
        this.faultAction = faultAction;
    }
}
