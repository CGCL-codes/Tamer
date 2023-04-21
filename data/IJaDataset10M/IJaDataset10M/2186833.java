package org.objectwiz.plugin.uibuilder.model.reference;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import org.objectwiz.core.Application;
import org.objectwiz.plugin.methodinvocation.ObjectMethod;
import org.objectwiz.plugin.methodinvocation.MethodInvocationHelper;

/**
 * Reference to a method by its name.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
@Embeddable
public class MethodRef implements Serializable {

    private String methodName;

    public MethodRef() {
    }

    public MethodRef(String methodName) {
        this.methodName = methodName;
    }

    @NotNull
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ObjectMethod resolveMethod(Application application) {
        MethodInvocationHelper helper = MethodInvocationHelper.require(application);
        return helper.requireMethodByFullName(methodName);
    }
}
