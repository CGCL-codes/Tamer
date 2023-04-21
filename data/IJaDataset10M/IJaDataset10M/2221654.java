package vaj2cvs;

/**
 * The bean information class for vaj2cvs.CvsTagDialog.
 */
public class CvsTagDialogBeanInfo extends java.beans.SimpleBeanInfo {

    /**
 * Gets the branchSelected property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor branchSelectedPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getBranchSelected", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getBranchSelected", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setBranchSelected", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setBranchSelected", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("branchSelected", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("branchSelected", getBeanClass());
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the cancelSelected property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor cancelSelectedPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getCancelSelected", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getCancelSelected", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setCancelSelected", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setCancelSelected", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("cancelSelected", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("cancelSelected", getBeanClass());
            }
            ;
            aDescriptor.setShortDescription("Cancel button has been pressed");
            aDescriptor.setValue("preferred", new Boolean(true));
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
 * Gets the currentSelected property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor currentSelectedPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getCurrentSelected", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getCurrentSelected", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setCurrentSelected", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setCurrentSelected", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("currentSelected", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("currentSelected", getBeanClass());
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the dateSelected property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor dateSelectedPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getDateSelected", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getDateSelected", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setDateSelected", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setDateSelected", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("dateSelected", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("dateSelected", getBeanClass());
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the dateText property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor dateTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getDateText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getDateText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setDateText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setDateText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("dateText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("dateText", getBeanClass());
            }
            ;
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
        return vaj2cvs.CvsTagDialog.class;
    }

    /**
 * Gets the bean class name.
 * @return java.lang.String
 */
    public static java.lang.String getBeanClassName() {
        return "vaj2cvs.CvsTagDialog";
    }

    public java.beans.BeanDescriptor getBeanDescriptor() {
        java.beans.BeanDescriptor aDescriptor = null;
        try {
            aDescriptor = new java.beans.BeanDescriptor(vaj2cvs.CvsTagDialog.class);
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
            java.beans.MethodDescriptor aDescriptorList[] = { getTagTextMethodDescriptor(), main_javalangString__MethodDescriptor(), setTagText_javalangStringMethodDescriptor() };
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
            java.beans.PropertyDescriptor aDescriptorList[] = { branchSelectedPropertyDescriptor(), cancelSelectedPropertyDescriptor(), componentOrientationPropertyDescriptor(), currentSelectedPropertyDescriptor(), dateSelectedPropertyDescriptor(), dateTextPropertyDescriptor(), revisionSelectedPropertyDescriptor(), revisionTextPropertyDescriptor(), tagTextPropertyDescriptor() };
            return aDescriptorList;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
 * Gets the getTagText() method descriptor.
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor getTagTextMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = {};
                aMethod = getBeanClass().getMethod("getTagText", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getTagText", 0);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptors[] = {};
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

    /**
 * Called whenever the bean information class throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
    }

    /**
 * Gets the main(java.lang.String[]) method descriptor.
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor main_javalangString__MethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String[].class };
                aMethod = getBeanClass().getMethod("main", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "main", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("args");
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

    /**
 * Gets the revisionSelected property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor revisionSelectedPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getRevisionSelected", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getRevisionSelected", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { boolean.class };
                    aSetMethod = getBeanClass().getMethod("setRevisionSelected", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setRevisionSelected", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("revisionSelected", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("revisionSelected", getBeanClass());
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the revisionText property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor revisionTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getRevisionText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getRevisionText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setRevisionText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setRevisionText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("revisionText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("revisionText", getBeanClass());
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Gets the setTagText(java.lang.String) method descriptor.
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor setTagText_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("setTagText", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "setTagText", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("arg1");
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

    /**
 * Gets the tagText property descriptor.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor tagTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getTagText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getTagText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setTagText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setTagText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("tagText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("tagText", getBeanClass());
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }
}
