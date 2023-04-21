package org.t2framework.lucy.exception;

/**
 * <#if locale="en">
 * <p>
 * ArgumentMismatchException.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 */
public class ArgumentMismatchException extends LucyBaseRuntimeException {

    private static final long serialVersionUID = -604816650314018789L;

    public ArgumentMismatchException(Class<?> clazz, String methodName, int lineNo, int columnNo) {
        super("ELucyCore0007", clazz.getName(), methodName, lineNo, columnNo);
    }
}
