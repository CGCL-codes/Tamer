package com.nexirius.util.resource;

/**
 * THIS CLASS IS NOT USED. IT SHOULD BE REMOVED FROM CLEAR CASE!!!!!!!!
 * <p/>
 * The bean information class for com.nexirius.util.resource.ClientResourceImpl.
 */
public class ClientResourceImplBeanInfo extends java.beans.SimpleBeanInfo {

    /**
     * Find the method by comparing (name & parameter size) against the methods in the class.
     *
     * @param aClass         java.lang.Class
     * @param methodName     java.lang.String
     * @param parameterCount int
     * @return java.lang.reflect.Method
     */
    public static java.lang.reflect.Method findMethod(java.lang.Class aClass, java.lang.String methodName, int parameterCount) {
        try {
            java.lang.reflect.Method methods[] = aClass.getMethods();
            for (int index = 0; index < methods.length; index++) {
                java.lang.reflect.Method method = methods[index];
                if ((method.getParameterTypes().length == parameterCount) && (method.getName().equals(methodName))) {
                    return method;
                }
            }
        } catch (java.lang.Throwable exception) {
            return null;
        }
        return null;
    }

    /**
     * Returns the BeanInfo of the superclass of this bean to inherit its features.
     *
     * @return java.beans.BeanInfo[]
     */
    public java.beans.BeanInfo[] getAdditionalBeanInfo() {
        java.lang.Class superClass;
        java.beans.BeanInfo superBeanInfo = null;
        try {
            superClass = getBeanDescriptor().getBeanClass().getSuperclass();
        } catch (java.lang.Throwable exception) {
            return null;
        }
        try {
            superBeanInfo = java.beans.Introspector.getBeanInfo(superClass);
        } catch (java.beans.IntrospectionException ie) {
        }
        if (superBeanInfo != null) {
            java.beans.BeanInfo[] ret = new java.beans.BeanInfo[1];
            ret[0] = superBeanInfo;
            return ret;
        }
        return null;
    }

    /**
     * Gets the bean class.
     *
     * @return java.lang.Class
     */
    public static java.lang.Class getBeanClass() {
        return com.nexirius.util.resource.ClientResourceImpl.class;
    }

    /**
     * Gets the bean class name.
     *
     * @return java.lang.String
     */
    public static java.lang.String getBeanClassName() {
        return "com.nexirius.util.resource.ClientResourceImpl";
    }

    public java.beans.BeanDescriptor getBeanDescriptor() {
        java.beans.BeanDescriptor aDescriptor = null;
        try {
            aDescriptor = new java.beans.BeanDescriptor(com.nexirius.util.resource.ClientResourceImpl.class);
        } catch (Throwable exception) {
        }
        ;
        return aDescriptor;
    }

    /**
     * Return the event set descriptors for this bean.
     *
     * @return java.beans.EventSetDescriptor[]
     */
    public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
        try {
            java.beans.EventSetDescriptor aDescriptorList[] = { resourceChangeEventSetDescriptor() };
            return aDescriptorList;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
     * Gets the getHelpURL(java.lang.String) method descriptor.
     *
     * @return java.beans.MethodDescriptor
     */
    public java.beans.MethodDescriptor getHelpURL_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("getHelpURL", aParameterTypes);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getHelpURL", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("resourceKey");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
     * Gets the getIcon(java.lang.String) method descriptor.
     *
     * @return java.beans.MethodDescriptor
     */
    public java.beans.MethodDescriptor getIcon_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("getIcon", aParameterTypes);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getIcon", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("resourceKey");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
     * Gets the getKeyAcceleratorText(java.lang.String) method descriptor.
     *
     * @return java.beans.MethodDescriptor
     */
    public java.beans.MethodDescriptor getKeyAcceleratorText_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("getKeyAcceleratorText", aParameterTypes);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getKeyAcceleratorText", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("resourceKey");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
     * Return the method descriptors for this bean.
     *
     * @return java.beans.MethodDescriptor[]
     */
    public java.beans.MethodDescriptor[] getMethodDescriptors() {
        try {
            java.beans.MethodDescriptor aDescriptorList[] = { getHelpURL_javalangStringMethodDescriptor(), getIcon_javalangStringMethodDescriptor(), getKeyAcceleratorText_javalangStringMethodDescriptor(), getMnemonic_javalangStringMethodDescriptor(), getToolTipText_javalangStringMethodDescriptor() };
            return aDescriptorList;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
     * Gets the getMnemonic(java.lang.String) method descriptor.
     *
     * @return java.beans.MethodDescriptor
     */
    public java.beans.MethodDescriptor getMnemonic_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("getMnemonic", aParameterTypes);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getMnemonic", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("resourceKey");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
     * Return the property descriptors for this bean.
     *
     * @return java.beans.PropertyDescriptor[]
     */
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        try {
            java.beans.PropertyDescriptor aDescriptorList[] = {};
            return aDescriptorList;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
     * Gets the getToolTipText(java.lang.String) method descriptor.
     *
     * @return java.beans.MethodDescriptor
     */
    public java.beans.MethodDescriptor getToolTipText_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("getToolTipText", aParameterTypes);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getToolTipText", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("resourceKey");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
     * Called whenever the bean information class throws an exception.
     *
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {
    }

    /**
     * Gets the resourceChange event set descriptor.
     *
     * @return java.beans.EventSetDescriptor
     */
    public java.beans.EventSetDescriptor resourceChangeEventSetDescriptor() {
        java.beans.EventSetDescriptor aDescriptor = null;
        try {
            try {
                java.beans.MethodDescriptor eventMethodDescriptors[] = { resourceChangeresourceChange_javalangObjectMethodEventDescriptor() };
                java.lang.reflect.Method anAddMethod = null;
                try {
                    java.lang.Class anAddMethodParameterTypes[] = { com.nexirius.util.resource.ResourceChangeListener.class };
                    anAddMethod = getBeanClass().getMethod("addResourceChangeListener", anAddMethodParameterTypes);
                } catch (java.lang.Throwable exception) {
                    handleException(exception);
                    anAddMethod = findMethod(getBeanClass(), "addResourceChangeListener", 1);
                }
                ;
                java.lang.reflect.Method aRemoveMethod = null;
                try {
                    java.lang.Class aRemoveMethodParameterTypes[] = { com.nexirius.util.resource.ResourceChangeListener.class };
                    aRemoveMethod = getBeanClass().getMethod("removeResourceChangeListener", aRemoveMethodParameterTypes);
                } catch (java.lang.Throwable exception) {
                    handleException(exception);
                    aRemoveMethod = findMethod(getBeanClass(), "removeResourceChangeListener", 1);
                }
                ;
                aDescriptor = new java.beans.EventSetDescriptor("resourceChange", com.nexirius.util.resource.ResourceChangeListener.class, eventMethodDescriptors, anAddMethod, aRemoveMethod);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                java.lang.String eventMethodNames[] = { "resourceChange" };
                aDescriptor = new java.beans.EventSetDescriptor(getBeanClass(), "resourceChange", com.nexirius.util.resource.ResourceChangeListener.class, eventMethodNames, "addResourceChangeListener", "removeResourceChangeListener");
            }
            ;
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
     * Gets the resourceChange.resourceChange(java.lang.Object) method descriptor.
     *
     * @return java.beans.MethodDescriptor
     */
    public java.beans.MethodDescriptor resourceChangeresourceChange_javalangObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { com.nexirius.util.resource.ResourceChangeEvent.class };
                aMethod = (com.nexirius.util.resource.ResourceChangeListener.class).getMethod("resourceChange", aParameterTypes);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aMethod = findMethod((com.nexirius.util.resource.ResourceChangeListener.class), "resourceChange", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("event");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
            aDescriptor.setDisplayName("resourceChange.resourceChange(java.lang.Object)");
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }
}
