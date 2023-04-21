package com.ibm.wala.j2ee;

import org.eclipse.jem.java.Method;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.MethodElement;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;

/**
 * Represents a declarative transaction attribute in a deployment descriptor
 */
public class DeploymentDeclaredTransaction extends AbstractDeclaredTransaction {

    /**
   * The method whose call is declared to be a transaction
   */
    private final Method method;

    /**
   * The MethodElement that gave birth to this transaction declaration
   */
    private final MethodElement methodElement;

    /**
   * Reference to the governing class loader.
   */
    private final ClassLoaderReference loader;

    /**
   * @param method
   */
    public DeploymentDeclaredTransaction(EnterpriseBean B, Method method, MethodElement methodElement, ClassLoaderReference loader, int kind, int transactionType) {
        super(B, kind, transactionType);
        this.method = method;
        this.methodElement = methodElement;
        this.loader = loader;
    }

    /**
   * TODO: cache this?
   */
    public MethodReference getMethodReference() {
        return J2EEUtil.createMethodReference(method, loader);
    }

    public MethodElement getMethodElement() {
        return methodElement;
    }
}
