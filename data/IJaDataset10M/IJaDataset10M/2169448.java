package dialogPackage;

/**
 * La classe bean info per dialogPackage.EditCopiaDocumento.
 */
public class EditCopiaDocumentoBeanInfo extends java.beans.SimpleBeanInfo {

    /**
 * Ottiene il descriptor del metodo addEditCopiaDocumentoListener(dialogPackage.EditCopiaDocumentoListener).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor addEditCopiaDocumentoListener_dialogPackageEditCopiaDocumentoListenerMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { dialogPackage.EditCopiaDocumentoListener.class };
                aMethod = getBeanClass().getMethod("addEditCopiaDocumentoListener", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "addEditCopiaDocumentoListener", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("newListener");
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
 * Ottiene il descriptor della proprieta componentOrientation.
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
 * Ottiene il descriptor del metodo editCopiaDocumento_Initialize().
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor editCopiaDocumento_InitializeMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = {};
                aMethod = getBeanClass().getMethod("editCopiaDocumento_Initialize", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "editCopiaDocumento_Initialize", 0);
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
 * Ottiene il descriptor dell'insieme di eventi editCopiaDocumento.
 * @return java.beans.EventSetDescriptor
 */
    public java.beans.EventSetDescriptor editCopiaDocumentoEventSetDescriptor() {
        java.beans.EventSetDescriptor aDescriptor = null;
        try {
            try {
                java.beans.MethodDescriptor eventMethodDescriptors[] = { editCopiaDocumentoproposteJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor(), parcelleJButtonAction_actionPerformed_javaawteventActionEventMethodEventDescriptor() };
                java.lang.reflect.Method anAddMethod = null;
                try {
                    java.lang.Class anAddMethodParameterTypes[] = { dialogPackage.EditCopiaDocumentoListener.class };
                    anAddMethod = getBeanClass().getMethod("addEditCopiaDocumentoListener", anAddMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    anAddMethod = findMethod(getBeanClass(), "addEditCopiaDocumentoListener", 1);
                }
                ;
                java.lang.reflect.Method aRemoveMethod = null;
                try {
                    java.lang.Class aRemoveMethodParameterTypes[] = { dialogPackage.EditCopiaDocumentoListener.class };
                    aRemoveMethod = getBeanClass().getMethod("removeEditCopiaDocumentoListener", aRemoveMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aRemoveMethod = findMethod(getBeanClass(), "removeEditCopiaDocumentoListener", 1);
                }
                ;
                aDescriptor = new java.beans.EventSetDescriptor("editCopiaDocumento", dialogPackage.EditCopiaDocumentoListener.class, eventMethodDescriptors, anAddMethod, aRemoveMethod);
            } catch (Throwable exception) {
                handleException(exception);
                java.lang.String eventMethodNames[] = { "parcelleJButtonAction_actionPerformed", "proposteJButtonAction_actionPerformed" };
                aDescriptor = new java.beans.EventSetDescriptor(getBeanClass(), "editCopiaDocumento", dialogPackage.EditCopiaDocumentoListener.class, eventMethodNames, "addEditCopiaDocumentoListener", "removeEditCopiaDocumentoListener");
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo editCopiaDocumento.proposteJButtonAction_actionPerformed(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor editCopiaDocumentoproposteJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.EditCopiaDocumentoListener.class).getMethod("proposteJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.EditCopiaDocumentoListener.class), "proposteJButtonAction_actionPerformed", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("newEvent");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
            aDescriptor.setDisplayName("proposteJButtonAction_actionPerformed(java.awt.event.ActionEvent)");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Trovare il metodo confrontando (dimensione nome e parametro) e i metodi contenuti nella classe.
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
 * Restituisce il BeanInfo della superclasse del bean per ereditarne le relative funzioni.
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
 * Ottiene la classe bean.
 * @return java.lang.Class
 */
    public static java.lang.Class getBeanClass() {
        return dialogPackage.EditCopiaDocumento.class;
    }

    /**
 * Ottiene il nome della classe bean.
 * @return java.lang.String
 */
    public static java.lang.String getBeanClassName() {
        return "dialogPackage.EditCopiaDocumento";
    }

    public java.beans.BeanDescriptor getBeanDescriptor() {
        java.beans.BeanDescriptor aDescriptor = null;
        try {
            aDescriptor = new java.beans.BeanDescriptor(dialogPackage.EditCopiaDocumento.class);
        } catch (Throwable exception) {
        }
        ;
        return aDescriptor;
    }

    /**
 * Restituire i descriptor del set di eventi per questo bean.
 * @return java.beans.EventSetDescriptor[]
 */
    public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
        try {
            java.beans.EventSetDescriptor aDescriptorList[] = { editCopiaDocumentoEventSetDescriptor() };
            return aDescriptorList;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
 * Restituire i descriptor dei metodi per questo bean.
 * @return java.beans.MethodDescriptor[]
 */
    public java.beans.MethodDescriptor[] getMethodDescriptors() {
        try {
            java.beans.MethodDescriptor aDescriptorList[] = { addEditCopiaDocumentoListener_dialogPackageEditCopiaDocumentoListenerMethodDescriptor(), editCopiaDocumento_InitializeMethodDescriptor(), main_javalangString__MethodDescriptor(), removeEditCopiaDocumentoListener_dialogPackageEditCopiaDocumentoListenerMethodDescriptor() };
            return aDescriptorList;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
 * Restituire i descriptor delle proprieta per questo bean.
 * @return java.beans.PropertyDescriptor[]
 */
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        try {
            java.beans.PropertyDescriptor aDescriptorList[] = { componentOrientationPropertyDescriptor() };
            return aDescriptorList;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return null;
    }

    /**
 * Richiamato ogni volta che la classe bean info lancia un'eccezione.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
    }

    /**
 * Ottiene il descriptor del metodo main(java.lang.String[]).
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
 * Ottiene il descriptor del metodo parcelleJButtonAction_actionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor parcelleJButtonAction_actionPerformed_javaawteventActionEventMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.EditCopiaDocumentoListener.class).getMethod("parcelleJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.EditCopiaDocumentoListener.class), "parcelleJButtonAction_actionPerformed", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("newEvent");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
            aDescriptor.setDisplayName("parcelleJButtonAction_actionPerformed(java.awt.event.ActionEvent)");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo proposteJButtonAction_actionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor proposteJButtonAction_actionPerformed_javaawteventActionEventMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.EditCopiaDocumentoListener.class).getMethod("proposteJButtonAction_actionPerformed", aParameterTypes);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.EditCopiaDocumentoListener.class), "proposteJButtonAction_actionPerformed", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("newEvent");
                java.beans.ParameterDescriptor aParameterDescriptors[] = { aParameterDescriptor1 };
                aDescriptor = new java.beans.MethodDescriptor(aMethod, aParameterDescriptors);
            } catch (java.lang.Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.MethodDescriptor(aMethod);
            }
            ;
            aDescriptor.setDisplayName("proposteJButtonAction_actionPerformed(java.awt.event.ActionEvent)");
        } catch (java.lang.Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo removeEditCopiaDocumentoListener(dialogPackage.EditCopiaDocumentoListener).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor removeEditCopiaDocumentoListener_dialogPackageEditCopiaDocumentoListenerMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { dialogPackage.EditCopiaDocumentoListener.class };
                aMethod = getBeanClass().getMethod("removeEditCopiaDocumentoListener", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "removeEditCopiaDocumentoListener", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("newListener");
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
