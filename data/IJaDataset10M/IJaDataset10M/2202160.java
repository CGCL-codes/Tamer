package dialogPackage;

/**
 * La classe bean info per dialogPackage.CompletaControParti.
 */
public class CompletaControPartiBeanInfo extends java.beans.SimpleBeanInfo {

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
                aMethod = (dialogPackage.CompletaControPartiListener.class).getMethod("annullaJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CompletaControPartiListener.class), "annullaJButtonAction_actionPerformed", 1);
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
 * Ottiene il descriptor della proprieta avvocatoJTextFieldText.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor avvocatoJTextFieldTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getAvvocatoJTextFieldText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getAvvocatoJTextFieldText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setAvvocatoJTextFieldText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setAvvocatoJTextFieldText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("avvocatoJTextFieldText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("avvocatoJTextFieldText", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("avvocatoJTextField");
            aDescriptor.setShortDescription("Avvocato della Controparte");
            aDescriptor.setValue("preferred", new Boolean(true));
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor della proprieta buttonFlagThis.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor buttonFlagThisPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getButtonFlagThis", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getButtonFlagThis", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.Boolean.class };
                    aSetMethod = getBeanClass().getMethod("setButtonFlagThis", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setButtonFlagThis", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("buttonFlagThis", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("buttonFlagThis", getBeanClass());
            }
            ;
            aDescriptor.setBound(true);
            aDescriptor.setDisplayName("buttonFlag");
            aDescriptor.setShortDescription("buttonFlagThis");
            aDescriptor.setValue("preferred", new Boolean(true));
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor della proprieta cognomeJTextFieldText.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor cognomeJTextFieldTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getCognomeJTextFieldText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getCognomeJTextFieldText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setCognomeJTextFieldText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setCognomeJTextFieldText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("cognomeJTextFieldText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("cognomeJTextFieldText", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("cognome");
            aDescriptor.setShortDescription("Cognome");
            aDescriptor.setValue("preferred", new Boolean(true));
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo completaControParti_WindowOpened(java.awt.event.WindowEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor completaControParti_WindowOpened_javaawteventWindowEventMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.awt.event.WindowEvent.class };
                aMethod = getBeanClass().getMethod("completaControParti_WindowOpened", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "completaControParti_WindowOpened", 1);
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
 * Ottiene il descriptor del metodo completaControParti.annullaJButtonAction_actionPerformed(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor completaControPartiannullaJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.CompletaControPartiListener.class).getMethod("annullaJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CompletaControPartiListener.class), "annullaJButtonAction_actionPerformed", 1);
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
            aDescriptor.setShortDescription("Premuto tasto annulla");
            aDescriptor.setValue("preferred", new Boolean(true));
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor dell'insieme di eventi completaControParti.
 * @return java.beans.EventSetDescriptor
 */
    public java.beans.EventSetDescriptor completaControPartiEventSetDescriptor() {
        java.beans.EventSetDescriptor aDescriptor = null;
        try {
            try {
                java.beans.MethodDescriptor eventMethodDescriptors[] = { completaControPartiokJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor(), completaControPartiannullaJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() };
                java.lang.reflect.Method anAddMethod = null;
                try {
                    java.lang.Class anAddMethodParameterTypes[] = { dialogPackage.CompletaControPartiListener.class };
                    anAddMethod = getBeanClass().getMethod("addCompletaControPartiListener", anAddMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    anAddMethod = findMethod(getBeanClass(), "addCompletaControPartiListener", 1);
                }
                ;
                java.lang.reflect.Method aRemoveMethod = null;
                try {
                    java.lang.Class aRemoveMethodParameterTypes[] = { dialogPackage.CompletaControPartiListener.class };
                    aRemoveMethod = getBeanClass().getMethod("removeCompletaControPartiListener", aRemoveMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aRemoveMethod = findMethod(getBeanClass(), "removeCompletaControPartiListener", 1);
                }
                ;
                aDescriptor = new java.beans.EventSetDescriptor("completaControParti", dialogPackage.CompletaControPartiListener.class, eventMethodDescriptors, anAddMethod, aRemoveMethod);
            } catch (Throwable exception) {
                handleException(exception);
                java.lang.String eventMethodNames[] = { "okJButtonAction_actionPerformed", "annullaJButtonAction_actionPerformed" };
                aDescriptor = new java.beans.EventSetDescriptor(getBeanClass(), "completaControParti", dialogPackage.CompletaControPartiListener.class, eventMethodNames, "addCompletaControPartiListener", "removeCompletaControPartiListener");
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo completaControParti.okJButtonAction_actionPerformed(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor completaControPartiokJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.CompletaControPartiListener.class).getMethod("okJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CompletaControPartiListener.class), "okJButtonAction_actionPerformed", 1);
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
            aDescriptor.setShortDescription("Premuto tasto ok");
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
 * Ottiene il descriptor della proprieta dataNascJTextFieldText.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor dataNascJTextFieldTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getDataNascJTextFieldText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getDataNascJTextFieldText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setDataNascJTextFieldText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setDataNascJTextFieldText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("dataNascJTextFieldText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("dataNascJTextFieldText", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("dataNasc");
            aDescriptor.setShortDescription("Data di Nascita");
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
 * Ottiene il descriptor della proprieta fineRappDateThis.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor fineRappDateThisPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getFineRappDateThis", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getFineRappDateThis", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.util.Date.class };
                    aSetMethod = getBeanClass().getMethod("setFineRappDateThis", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setFineRappDateThis", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("fineRappDateThis", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("fineRappDateThis", getBeanClass());
            }
            ;
            aDescriptor.setBound(true);
            aDescriptor.setDisplayName("fineRappDate");
            aDescriptor.setShortDescription("Data Fine Rapporto");
            aDescriptor.setValue("preferred", new Boolean(true));
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
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
 * Ottiene il descriptor del metodo getAvvocatoJTextFieldText().
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor getAvvocatoJTextFieldTextMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = {};
                aMethod = getBeanClass().getMethod("getAvvocatoJTextFieldText", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getAvvocatoJTextFieldText", 0);
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
 * Ottiene la classe bean.
 * @return java.lang.Class
 */
    public static java.lang.Class getBeanClass() {
        return dialogPackage.CompletaControParti.class;
    }

    /**
 * Ottiene il nome della classe bean.
 * @return java.lang.String
 */
    public static java.lang.String getBeanClassName() {
        return "dialogPackage.CompletaControParti";
    }

    public java.beans.BeanDescriptor getBeanDescriptor() {
        java.beans.BeanDescriptor aDescriptor = null;
        try {
            aDescriptor = new java.beans.BeanDescriptor(dialogPackage.CompletaControParti.class);
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
            java.beans.EventSetDescriptor aDescriptorList[] = { completaControPartiEventSetDescriptor() };
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
            java.beans.MethodDescriptor aDescriptorList[] = { completaControParti_WindowOpened_javaawteventWindowEventMethodDescriptor(), getAvvocatoJTextFieldTextMethodDescriptor(), main_javalangString__MethodDescriptor(), okJButton_ActionPerformed_javaawteventActionEventMethodDescriptor(), setAvvocatoJTextFieldText_javalangStringMethodDescriptor() };
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
            java.beans.PropertyDescriptor aDescriptorList[] = { avvocatoJTextFieldTextPropertyDescriptor(), buttonFlagThisPropertyDescriptor(), cognomeJTextFieldTextPropertyDescriptor(), componentOrientationPropertyDescriptor(), dataNascJTextFieldTextPropertyDescriptor(), fineRappDateThisPropertyDescriptor(), inizRappDateThisPropertyDescriptor(), nomeJTextFieldTextPropertyDescriptor() };
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
 * Ottiene il descriptor della proprieta inizRappDateThis.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor inizRappDateThisPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getInizRappDateThis", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getInizRappDateThis", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.util.Date.class };
                    aSetMethod = getBeanClass().getMethod("setInizRappDateThis", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setInizRappDateThis", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("inizRappDateThis", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("inizRappDateThis", getBeanClass());
            }
            ;
            aDescriptor.setBound(true);
            aDescriptor.setDisplayName("inizRappDate");
            aDescriptor.setShortDescription("Data Inizio Rapporto");
            aDescriptor.setValue("preferred", new Boolean(true));
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
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
 * Ottiene il descriptor della proprieta nomeJTextFieldText.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor nomeJTextFieldTextPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getNomeJTextFieldText", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getNomeJTextFieldText", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setNomeJTextFieldText", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setNomeJTextFieldText", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("nomeJTextFieldText", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("nomeJTextFieldText", getBeanClass());
            }
            ;
            aDescriptor.setDisplayName("nome");
            aDescriptor.setShortDescription("Nome");
            aDescriptor.setValue("preferred", new Boolean(true));
            aDescriptor.setValue("enumerationValues", new Object[] {});
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo okJButton_ActionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor okJButton_ActionPerformed_javaawteventActionEventMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.awt.event.ActionEvent.class };
                aMethod = getBeanClass().getMethod("okJButton_ActionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "okJButton_ActionPerformed", 1);
            }
            ;
            try {
                java.beans.ParameterDescriptor aParameterDescriptor1 = new java.beans.ParameterDescriptor();
                aParameterDescriptor1.setName("arg1");
                aParameterDescriptor1.setDisplayName("actionEvent");
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
                aMethod = (dialogPackage.CompletaControPartiListener.class).getMethod("okJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.CompletaControPartiListener.class), "okJButtonAction_actionPerformed", 1);
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
 * Ottiene il descriptor del metodo setAvvocatoJTextFieldText(java.lang.String).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor setAvvocatoJTextFieldText_javalangStringMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.String.class };
                aMethod = getBeanClass().getMethod("setAvvocatoJTextFieldText", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "setAvvocatoJTextFieldText", 1);
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
