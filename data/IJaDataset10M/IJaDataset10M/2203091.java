package org.apache.harmony.lang.management;

import java.lang.reflect.Method;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import org.apache.harmony.lang.management.internal.nls.Messages;

/**
 * Abstract implementation of the {@link DynamicMBean} interface that provides
 * behaviour required by a dynamic MBean. This class is subclassed by all of the
 * concrete MXBean types in this package.
 */
public abstract class DynamicMXBeanImpl implements DynamicMBean {

    protected MBeanInfo info;

    /**
     * @param info
     */
    protected void setMBeanInfo(MBeanInfo info) {
        this.info = info;
    }

    public AttributeList getAttributes(String[] attributes) {
        AttributeList result = new AttributeList();
        for (int i = 0; i < attributes.length; i++) {
            try {
                Object value = getAttribute(attributes[i]);
                result.add(new Attribute(attributes[i], value));
            } catch (Exception e) {
                if (ManagementUtils.VERBOSE_MODE) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return result;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        AttributeList result = new AttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attrib = (Attribute) attributes.get(i);
            String attribName = null;
            Object attribVal = null;
            try {
                this.setAttribute(attrib);
                attribName = attrib.getName();
                attribVal = getAttribute(attribName);
                result.add(new Attribute(attribName, attribVal));
            } catch (Exception e) {
                if (ManagementUtils.VERBOSE_MODE) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return result;
    }

    public MBeanInfo getMBeanInfo() {
        return info;
    }

    /**
     * Simple enumeration of the different kinds of access that may be required
     * of a dynamic MBean attribute.
     */
    enum AttributeAccessType {

        READING, WRITING
    }

    ;

    /**
     * Tests to see if this <code>DynamicMXBean</code> has an attribute with
     * the name <code>attributeName</code>. If the test is passed, the
     * {@link MBeanAttributeInfo}representing the attribute is returned.
     * 
     * @param attributeName
     *            the name of the attribute being queried
     * @param access
     *            an {@link AttributeAccessType}indication of whether the
     *            caller is looking for a readable or writable attribute.
     * @return if the named attribute exists and is readable or writable
     *         (depending on what was specified in <code>access</code>, an
     *         instance of <code>MBeanAttributeInfo</code> that describes the
     *         attribute, otherwise <code>null</code>.
     */
    protected MBeanAttributeInfo getPresentAttribute(String attributeName, AttributeAccessType access) {
        MBeanAttributeInfo[] attribs = info.getAttributes();
        MBeanAttributeInfo result = null;
        for (int i = 0; i < attribs.length; i++) {
            MBeanAttributeInfo attribInfo = attribs[i];
            if (attribInfo.getName().equals(attributeName)) {
                if (access.equals(AttributeAccessType.READING)) {
                    if (attribInfo.isReadable()) {
                        result = attribInfo;
                        break;
                    }
                } else {
                    if (attribInfo.isWritable()) {
                        result = attribInfo;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        Object result = null;
        Method getterMethod = null;
        MBeanAttributeInfo attribInfo = getPresentAttribute(attribute, AttributeAccessType.READING);
        if (attribInfo == null) {
            throw new AttributeNotFoundException(Messages.getString("lm.0A", attribute));
        }
        try {
            String getterPrefix = attribInfo.isIs() ? "is" : "get";
            getterMethod = this.getClass().getMethod(getterPrefix + attribute, (Class[]) null);
        } catch (Exception e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            throw new ReflectionException(e);
        }
        String realReturnType = getterMethod.getReturnType().getName();
        String openReturnType = attribInfo.getType();
        result = invokeMethod(getterMethod, (Object[]) null);
        try {
            if (!realReturnType.equals(openReturnType)) {
                result = ManagementUtils.convertToOpenType(result, Class.forName(openReturnType), Class.forName(realReturnType));
            }
        } catch (ClassNotFoundException e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            throw new MBeanException(e);
        }
        return result;
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        Class<?> argType = null;
        MBeanAttributeInfo attribInfo = getPresentAttribute(attribute.getName(), AttributeAccessType.WRITING);
        if (attribInfo == null) {
            throw new AttributeNotFoundException(Messages.getString("lm.0A", attribute));
        }
        try {
            argType = ManagementUtils.getClassMaybePrimitive(attribInfo.getType());
        } catch (ClassNotFoundException e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            throw new ReflectionException(e);
        }
        if (argType.isPrimitive()) {
            if (!ManagementUtils.isWrapperClass(attribute.getValue().getClass(), argType)) {
                throw new InvalidAttributeValueException(Messages.getString("lm.0B", attribInfo.getName(), attribInfo.getType()));
            }
        } else if (!argType.equals(attribute.getValue().getClass())) {
            throw new InvalidAttributeValueException(Messages.getString("lm.0B", attribInfo.getName(), attribInfo.getType()));
        }
        Method setterMethod = null;
        try {
            setterMethod = this.getClass().getMethod("set" + attribute.getName(), new Class[] { argType });
        } catch (Exception e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            throw new ReflectionException(e);
        }
        invokeMethod(setterMethod, attribute.getValue());
        try {
            setterMethod.invoke(this, attribute.getValue());
        } catch (Exception e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            Throwable root = e.getCause();
            if (root instanceof RuntimeException) {
                throw (RuntimeException) root;
            } else {
                throw new MBeanException((Exception) root);
            }
        }
    }

    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        Object result = null;
        String[] localSignature = signature;
        if (localSignature == null) {
            localSignature = new String[0];
        }
        MBeanOperationInfo opInfo = getPresentOperation(actionName, localSignature);
        if (opInfo == null) {
            throw new ReflectionException(new NoSuchMethodException(actionName), Messages.getString("lm.0C", actionName));
        }
        Method operationMethod = null;
        try {
            Class<?>[] argTypes = new Class[localSignature.length];
            for (int i = 0; i < localSignature.length; i++) {
                argTypes[i] = ManagementUtils.getClassMaybePrimitive(localSignature[i]);
            }
            operationMethod = this.getClass().getMethod(actionName, argTypes);
        } catch (Exception e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            throw new ReflectionException(e);
        }
        String realReturnType = operationMethod.getReturnType().getName();
        String openReturnType = opInfo.getReturnType();
        result = invokeMethod(operationMethod, params);
        try {
            if (!realReturnType.equals(openReturnType)) {
                result = ManagementUtils.convertToOpenType(result, Class.forName(openReturnType), Class.forName(realReturnType));
            }
        } catch (ClassNotFoundException e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            throw new MBeanException(e);
        }
        return result;
    }

    /**
     * Tests to see if this <code>DynamicMXBean</code> has an operation with
     * the name <code>actionName</code>. If the test is passed, the
     * {@link MBeanOperationInfo}representing the operation is returned to the
     * caller.
     * 
     * @param actionName
     *            the name of a possible method on this
     *            <code>DynamicMXBean</code>
     * @param signature
     *            the list of parameter types for the named operation in the
     *            correct order
     * @return if the named operation exists, an instance of
     *         <code>MBeanOperationInfo</code> that describes the operation,
     *         otherwise <code>null</code>.
     */
    protected MBeanOperationInfo getPresentOperation(String actionName, String[] signature) {
        MBeanOperationInfo[] operations = info.getOperations();
        MBeanOperationInfo result = null;
        for (int i = 0; i < operations.length; i++) {
            MBeanOperationInfo opInfo = operations[i];
            if (opInfo.getName().equals(actionName)) {
                if (signature.length == opInfo.getSignature().length) {
                    boolean match = true;
                    MBeanParameterInfo[] parameters = opInfo.getSignature();
                    for (int j = 0; j < parameters.length; j++) {
                        MBeanParameterInfo paramInfo = parameters[j];
                        if (!paramInfo.getType().equals(signature[j])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        result = opInfo;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param params
     * @param operationMethod
     * @return the result of the reflective method invocation
     * @throws MBeanException
     */
    private Object invokeMethod(Method operationMethod, Object... params) throws MBeanException {
        Object result = null;
        try {
            result = operationMethod.invoke(this, params);
        } catch (Exception e) {
            if (ManagementUtils.VERBOSE_MODE) {
                e.printStackTrace(System.err);
            }
            Throwable root = e.getCause();
            if (root instanceof RuntimeException) {
                throw (RuntimeException) root;
            } else {
                throw new MBeanException((Exception) root);
            }
        }
        return result;
    }
}
