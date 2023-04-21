package dialogPackage;

/**
 * La classe bean info per dialogPackage.Cerca.
 */
public class CercaBeanInfo extends java.beans.SimpleBeanInfo {

    /**
 * Ottiene il descriptor del metodo annullaJButtonAction_actionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor annullaJButtonAction_actionPerformed_javaawteventActionEventMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.CercaListener.class).getMethod("annullaJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CercaListener.class), "annullaJButtonAction_actionPerformed", 1);
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
            aDescriptor.setDisplayName("annullaJButtonAction_actionPerformed(java.awt.event.ActionEvent)");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo cerca_WindowOpened(java.awt.event.WindowEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor cerca_WindowOpened_javaawteventWindowEventMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.awt.event.WindowEvent.class };
                aMethod = getBeanClass().getMethod("cerca_WindowOpened", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "cerca_WindowOpened", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("windowEvent");
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
 * Ottiene il descriptor del metodo cerca.annullaJButtonAction_actionPerformed(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor cercaannullaJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.CercaListener.class).getMethod("annullaJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CercaListener.class), "annullaJButtonAction_actionPerformed", 1);
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
            aDescriptor.setDisplayName("annullaJButtonAction");
            aDescriptor.setShortDescription("Premuto tasto Annulla");
            aDescriptor.setValue("preferred", new Boolean(true));
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor dell'insieme di eventi cerca.
 * @return java.beans.EventSetDescriptor
 */
    public java.beans.EventSetDescriptor cercaEventSetDescriptor() {
        java.beans.EventSetDescriptor aDescriptor = null;
        try {
            try {
                java.beans.MethodDescriptor eventMethodDescriptors[] = { cercaokJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor(), cercaannullaJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() };
                java.lang.reflect.Method anAddMethod = null;
                try {
                    java.lang.Class anAddMethodParameterTypes[] = { dialogPackage.CercaListener.class };
                    anAddMethod = getBeanClass().getMethod("addCercaListener", anAddMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    anAddMethod = findMethod(getBeanClass(), "addCercaListener", 1);
                }
                ;
                java.lang.reflect.Method aRemoveMethod = null;
                try {
                    java.lang.Class aRemoveMethodParameterTypes[] = { dialogPackage.CercaListener.class };
                    aRemoveMethod = getBeanClass().getMethod("removeCercaListener", aRemoveMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aRemoveMethod = findMethod(getBeanClass(), "removeCercaListener", 1);
                }
                ;
                aDescriptor = new java.beans.EventSetDescriptor("cerca", dialogPackage.CercaListener.class, eventMethodDescriptors, anAddMethod, aRemoveMethod);
            } catch (Throwable exception) {
                handleException(exception);
                java.lang.String eventMethodNames[] = { "okJButtonAction_actionPerformed", "annullaJButtonAction_actionPerformed" };
                aDescriptor = new java.beans.EventSetDescriptor(getBeanClass(), "cerca", dialogPackage.CercaListener.class, eventMethodNames, "addCercaListener", "removeCercaListener");
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo cerca.okJButtonAction_actionPerformed(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor cercaokJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.CercaListener.class).getMethod("okJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CercaListener.class), "okJButtonAction_actionPerformed", 1);
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
            aDescriptor.setDisplayName("okJButtonAction");
            aDescriptor.setShortDescription("Premuto tasto Ok");
            aDescriptor.setValue("preferred", new Boolean(true));
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
 * Ottiene il descriptor della proprieta descrizioneJTextFieldText.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor descrizioneJTextFieldTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getDescrizioneJTextFieldText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getDescrizioneJTextFieldText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setDescrizioneJTextFieldText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setDescrizioneJTextFieldText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("descrizioneJTextFieldText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("descrizioneJTextFieldText", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("descrizione");
            aDescriptor.setShortDescription("Descrizione");
            aDescriptor.setValue("preferred", new Boolean(true));
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
        return dialogPackage.Cerca.class;
    }

    /**
 * Ottiene il nome della classe bean.
 * @return java.lang.String
 */
    public static java.lang.String getBeanClassName() {
        return "dialogPackage.Cerca";
    }

    public java.beans.BeanDescriptor getBeanDescriptor() {
        java.beans.BeanDescriptor aDescriptor = null;
        try {
            aDescriptor = new java.beans.BeanDescriptor(dialogPackage.Cerca.class);
        } catch (Throwable exception) {
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo getDescrizioneJTextFieldText().
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor getDescrizioneJTextFieldTextMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = {};
                aMethod = getBeanClass().getMethod("getDescrizioneJTextFieldText", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getDescrizioneJTextFieldText", 0);
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
 * Restituire i descriptor del set di eventi per questo bean.
 * @return java.beans.EventSetDescriptor[]
 */
    public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
        try {
            java.beans.EventSetDescriptor aDescriptorList[] = { cercaEventSetDescriptor() };
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
            java.beans.MethodDescriptor aDescriptorList[] = { cerca_WindowOpened_javaawteventWindowEventMethodDescriptor(), getDescrizioneJTextFieldTextMethodDescriptor(), main_javalangString__MethodDescriptor(), setDescrizioneJTextFieldText_javalangStringMethodDescriptor() };
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
            java.beans.PropertyDescriptor aDescriptorList[] = { componentOrientationPropertyDescriptor(), descrizioneJTextFieldTextPropertyDescriptor() };
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
 * Ottiene il descriptor del metodo okJButtonAction_actionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor okJButtonAction_actionPerformed_javaawteventActionEventMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.CercaListener.class).getMethod("okJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CercaListener.class), "okJButtonAction_actionPerformed", 1);
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
            aDescriptor.setDisplayName("okJButtonAction_actionPerformed(java.awt.event.ActionEvent)");
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo setDescrizioneJTextFieldText(java.lang.String).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor setDescrizioneJTextFieldText_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("setDescrizioneJTextFieldText", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "setDescrizioneJTextFieldText", 1);
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
}
