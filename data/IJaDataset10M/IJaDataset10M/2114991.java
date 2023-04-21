package net.sf.doolin.gui.action.support;

import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.util.Utils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Delegates the call to a service.
 * 
 * @author Damien Coraboeuf
 * 
 * @param <E>
 *            Type of the parameter
 */
public class ServiceActionDelegate<E> implements ActionDelegate<E> {

    private Object target;

    private String methodName;

    private boolean useActionContext;

    @Override
    public void call(ActionContext actionContext, E parameter) {
        if (this.useActionContext) {
            Utils.callMethod(this.target, this.methodName, actionContext, parameter);
        } else {
            Utils.callMethod(this.target, this.methodName, parameter);
        }
    }

    public boolean isUseActionContext() {
        return this.useActionContext;
    }

    /**
	 * Sets the target method to call
	 * 
	 * @param methodName
	 *            Method name
	 */
    @Required
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
	 * Sets the target service to call
	 * 
	 * @param target
	 *            Object to call
	 */
    @Required
    public void setTarget(Object target) {
        this.target = target;
    }

    public void setUseActionContext(boolean useActionContext) {
        this.useActionContext = useActionContext;
    }
}
