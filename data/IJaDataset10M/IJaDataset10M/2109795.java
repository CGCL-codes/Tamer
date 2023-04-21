package us.wthr.jdem846.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.model.annotations.Order;
import us.wthr.jdem846.model.annotations.ProcessOption;
import us.wthr.jdem846.model.exceptions.InvalidProcessOptionException;
import us.wthr.jdem846.model.exceptions.MethodContainerInvokeException;

public class OptionModelMethodContainer {

    @SuppressWarnings("unused")
    private static Log log = Logging.getLog(OptionModelMethodContainer.class);

    private Object declaringObject;

    private Method method;

    private ProcessOption annotation;

    private String propertyName;

    private boolean isSetter;

    private boolean isGetter;

    private Order orderAnnotation;

    public OptionModelMethodContainer(Object declaringObject, Method method) throws InvalidProcessOptionException {
        this.declaringObject = declaringObject;
        this.method = method;
        if (method.isAnnotationPresent(ProcessOption.class)) {
            annotation = method.getAnnotation(ProcessOption.class);
        } else {
            annotation = null;
        }
        if (method.isAnnotationPresent(Order.class)) {
            orderAnnotation = method.getAnnotation(Order.class);
        } else {
            orderAnnotation = null;
        }
        String name = method.getName();
        propertyName = determinePropertyName(name);
        isSetter = false;
        isGetter = false;
        if (name.startsWith("get")) {
            isGetter = true;
            validateAsGetter(method);
        } else if (name.startsWith("is")) {
            isGetter = true;
            validateAsGetter(method);
        } else if (name.startsWith("set")) {
            isSetter = true;
            validateAsSetter(method);
        } else if (name.startsWith("put")) {
            isSetter = true;
            validateAsSetter(method);
        } else {
            throw new InvalidProcessOptionException("Method '" + name + "' does not meet naming standards", method);
        }
    }

    public String getId() {
        return annotation.id();
    }

    public String getLabel() {
        return annotation.label();
    }

    public String getTooltip() {
        return annotation.tooltip();
    }

    public String getOptionGroup() {
        return annotation.optionGroup();
    }

    public Class<?> getListModelClass() {
        return annotation.listModel();
    }

    public Class<?> getValidatorClass() {
        return annotation.validator();
    }

    public boolean isEnabled() {
        return annotation.enabled();
    }

    public String getMethodName() {
        return method.getName();
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public boolean isSetter() {
        return isSetter;
    }

    public boolean hasAnnotation() {
        return (annotation != null);
    }

    public boolean hasOrderAnnotation() {
        return (orderAnnotation != null);
    }

    public int getOrder() {
        if (hasOrderAnnotation()) {
            return orderAnnotation.value();
        } else {
            return Order.NOT_SET;
        }
    }

    public Object getValue() throws MethodContainerInvokeException {
        if (isGetter()) {
            try {
                return method.invoke(declaringObject);
            } catch (Exception ex) {
                throw new MethodContainerInvokeException("Error invoking method: " + ex.getMessage(), ex);
            }
        } else {
            throw new MethodContainerInvokeException("Method is not a getter");
        }
    }

    public void setValue(Object value) throws MethodContainerInvokeException {
        if (isSetter()) {
            try {
                method.invoke(declaringObject, value);
            } catch (Exception ex) {
                throw new MethodContainerInvokeException("Error invoking method: " + ex.getMessage(), ex);
            }
        } else {
            throw new MethodContainerInvokeException("Method is not a setter");
        }
    }

    public Class<?> getType() {
        if (isGetter()) {
            return method.getReturnType();
        } else {
            Class<?>[] parameterTypes = method.getParameterTypes();
            return parameterTypes[0];
        }
    }

    public static boolean validateAsGetterOrSetter(Method method) throws InvalidProcessOptionException {
        boolean isGetter = false;
        boolean isSetter = false;
        try {
            if (validateAsGetter(method)) {
                isGetter = true;
            }
            if (validateAsSetter(method)) {
                isSetter = true;
            }
        } catch (InvalidProcessOptionException ex) {
        }
        if (!isGetter && !isSetter) {
            throw new InvalidProcessOptionException("Method '" + method.getName() + "' does not meet getter/setter requirements", method);
        } else {
            return true;
        }
    }

    public static boolean validateAsGetter(Method method) throws InvalidProcessOptionException {
        if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
            throw new InvalidProcessOptionException("Getter method '" + method.getName() + "' goes not meet naming standards", method);
        }
        if (method.isVarArgs()) {
            throw new InvalidProcessOptionException("Getter method '" + method.getName() + "' cannot require parameters", method);
        }
        if (method.getReturnType().equals(Void.TYPE)) {
            throw new InvalidProcessOptionException("Getter method '" + method.getName() + "' must have a return type.", method);
        }
        return true;
    }

    public static boolean validateAsSetter(Method method) throws InvalidProcessOptionException {
        if (!method.getName().startsWith("set") && !method.getName().startsWith("put")) {
            throw new InvalidProcessOptionException("Setter method '" + method.getName() + "' goes not meet naming standards", method);
        }
        if (method.getParameterTypes().length != 1) {
            throw new InvalidProcessOptionException("Setter method '" + method.getName() + "' must have a single parameter", method);
        }
        if (!method.getReturnType().equals(Void.TYPE)) {
            throw new InvalidProcessOptionException("Setter method '" + method.getName() + "' cannot have a return type.", method);
        }
        return true;
    }

    public static String determinePropertyName(String methodName) {
        String propertyName = null;
        if (methodName.startsWith("get")) {
            propertyName = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            propertyName = methodName.substring(2);
        } else if (methodName.startsWith("set")) {
            propertyName = methodName.substring(3);
        } else if (methodName.startsWith("put")) {
            propertyName = methodName.substring(3);
        }
        return propertyName;
    }
}
