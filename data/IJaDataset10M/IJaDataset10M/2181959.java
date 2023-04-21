package org.mgkFramework.bean;

import java.lang.reflect.Method;
import org.mgkFramework.exceptions.MgkExternalExceptionWrapper;

public class FactMethodBeanFactory implements IMgkBeanFactory {

    private Method factMethod;

    /**
	 * @param factMethod Must be a static method that takes no args
	 */
    public FactMethodBeanFactory(Method factMethod) {
        this.factMethod = factMethod;
    }

    /**
	 * 
	 * @param clz
	 * @param methodName 
	 */
    public FactMethodBeanFactory(Class<?> factClass, String methodName) {
        try {
            this.factMethod = factClass.getMethod(methodName);
        } catch (Exception e) {
            throw new MgkExternalExceptionWrapper(e);
        }
    }

    @Override
    public Object newInstance() {
        try {
            return factMethod.invoke(null);
        } catch (Exception e) {
            throw new MgkExternalExceptionWrapper(e);
        }
    }
}
