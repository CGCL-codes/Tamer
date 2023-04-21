package com.apelon.beans.apeledit;

import com.apelon.common.log4j.Categories;

/**
 * The bean information class for com.apelon.beans.apeledit.ApelTreeEditor.
 */
public class ApelTreeEditorBeanInfo extends java.beans.SimpleBeanInfo {

    /**
 * Gets the apelTree1HasNodePopups property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor apelTree1HasNodePopupsPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getApelTree1HasNodePopups", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getApelTree1HasNodePopups", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setApelTree1HasNodePopups", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setApelTree1HasNodePopups", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("apelTree1HasNodePopups", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("apelTree1HasNodePopups", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("treeHasNodePopups");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the apelTree1RootVisible property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor apelTree1RootVisiblePropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getApelTree1RootVisible", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getApelTree1RootVisible", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setApelTree1RootVisible", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setApelTree1RootVisible", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("apelTree1RootVisible", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("apelTree1RootVisible", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("treeRootVisible");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the apelTree1ShowRootsCombo property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor apelTree1ShowRootsComboPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getApelTree1ShowRootsCombo", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getApelTree1ShowRootsCombo", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setApelTree1ShowRootsCombo", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setApelTree1ShowRootsCombo", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("apelTree1ShowRootsCombo", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("apelTree1ShowRootsCombo", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("treeShowRootsCombo");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the apelTreeEditorMgr property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor apelTreeEditorMgrPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { com.apelon.beans.apeledit.ApelTreeEditorMgr.class };
                    aSetMethod = getBeanClass().getMethod("setApelTreeEditorMgr", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setApelTreeEditorMgr", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("apelTreeEditorMgr", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("apelTreeEditorMgr", getBeanClass());
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the componentOrientation property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor componentOrientationPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getComponentOrientation", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getComponentOrientation", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.awt.ComponentOrientation.class };
                    aSetMethod = getBeanClass().getMethod("setComponentOrientation", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setComponentOrientation", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("componentOrientation", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("componentOrientation", getBeanClass());
            }
            ;
            aDescriptor.setValue("enumerationValues", new Object[] { "UNKNOWN", java.awt.ComponentOrientation.UNKNOWN, "java.awt.ComponentOrientation.UNKNOWN", "LEFT_TO_RIGHT", java.awt.ComponentOrientation.LEFT_TO_RIGHT, "java.awt.ComponentOrientation.LEFT_TO_RIGHT", "RIGHT_TO_LEFT", java.awt.ComponentOrientation.RIGHT_TO_LEFT, "java.awt.ComponentOrientation.RIGHT_TO_LEFT" });
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Find the method by comparing (name & parameter size) against the methods in the class.
 * @return java.lang.reflect.Method
 * @param aClass java.lang.Class
 * @param methodName java.lang.String
 * @param parameterCount int
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
 * @return java.lang.Class
 */
    public static java.lang.Class getBeanClass() {
        return com.apelon.beans.apeledit.ApelTreeEditor.class;
    }

    /**
 * Gets the bean class name.
 * @return java.lang.String
 */
    public static java.lang.String getBeanClassName() {
        return "com.apelon.beans.apeledit.ApelTreeEditor";
    }

    public java.beans.BeanDescriptor getBeanDescriptor() {
        java.beans.BeanDescriptor aDescriptor = null;
        try {
            aDescriptor = new java.beans.BeanDescriptor(com.apelon.beans.apeledit.ApelTreeEditor.class);
        } catch (Throwable exception) {
        }
        ;
        return aDescriptor;
    }

    /**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
    public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
        try {
            java.beans.EventSetDescriptor aDescriptorList[] = {};
            return aDescriptorList;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
    public java.beans.MethodDescriptor[] getMethodDescriptors() {
        try {
            java.beans.MethodDescriptor aDescriptorList[] = { setApelTreeEditorMgr_comapelonbeansapeleditApelTreeEditorMgrMethodDescriptor() };
            return aDescriptorList;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        try {
            java.beans.PropertyDescriptor aDescriptorList[] = { apelTree1HasNodePopupsPropertyDescriptor(), apelTree1RootVisiblePropertyDescriptor(), apelTree1ShowRootsComboPropertyDescriptor(), apelTreeEditorMgrPropertyDescriptor(), componentOrientationPropertyDescriptor(), hasWellsPropertyDescriptor() };
            return aDescriptorList;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
 * Called whenever the bean information class throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
        Categories.uiView().error("Caught Exception", exception);
    }

    /**
 * Gets the hasWells property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor hasWellsPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getHasWells", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getHasWells", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setHasWells", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setHasWells", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("hasWells", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("hasWells", getBeanClass());
            }
            ;
            aDescriptor.setShortDescription("True if this tree editor supports wells.");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the setApelTreeEditorMgr(com.apelon.beans.apeledit.ApelTreeEditorMgr) method descriptor.
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor setApelTreeEditorMgr_comapelonbeansapeleditApelTreeEditorMgrMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { com.apelon.beans.apeledit.ApelTreeEditorMgr.class };
                aMethod = getBeanClass().getMethod("setApelTreeEditorMgr", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "setApelTreeEditorMgr", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("mgr");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }
}
