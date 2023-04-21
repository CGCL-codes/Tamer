package dialogPackage;

/**
 * La classe bean info per dialogPackage.EditAgenda.
 */
public class EditAgendaBeanInfo extends java.beans.SimpleBeanInfo {

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
                aMethod = (dialogPackage.EditAgendaListener.class).getMethod("annullaJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.EditAgendaListener.class), "annullaJButtonAction_actionPerformed", 1);
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
 * Ottiene il descriptor del metodo categoriaJButton_ActionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor categoriaJButton_ActionPerformed_javaawteventActionEventMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.awt.event.ActionEvent.class };
                aMethod = getBeanClass().getMethod("categoriaJButton_ActionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "categoriaJButton_ActionPerformed", 1);
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
 * Ottiene il descriptor del metodo competenzeJButton_ActionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor competenzeJButton_ActionPerformed_javaawteventActionEventMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.awt.event.ActionEvent.class };
                aMethod = getBeanClass().getMethod("competenzeJButton_ActionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "competenzeJButton_ActionPerformed", 1);
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
 * Ottiene il descriptor del metodo editAgenda_Initialize().
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor editAgenda_InitializeMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = {};
                aMethod = getBeanClass().getMethod("editAgenda_Initialize", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "editAgenda_Initialize", 0);
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
 * Ottiene il descriptor del metodo editAgenda.annullaJButtonAction_actionPerformed(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor editAgendaannullaJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.EditAgendaListener.class).getMethod("annullaJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.EditAgendaListener.class), "annullaJButtonAction_actionPerformed", 1);
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
 * Ottiene il descriptor dell'insieme di eventi editAgenda.
 * @return java.beans.EventSetDescriptor
 */
    public java.beans.EventSetDescriptor editAgendaEventSetDescriptor() {
        java.beans.EventSetDescriptor aDescriptor = null;
        try {
            try {
                java.beans.MethodDescriptor eventMethodDescriptors[] = { editAgendaokJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor(), editAgendaannullaJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() };
                java.lang.reflect.Method anAddMethod = null;
                try {
                    java.lang.Class anAddMethodParameterTypes[] = { dialogPackage.EditAgendaListener.class };
                    anAddMethod = getBeanClass().getMethod("addEditAgendaListener", anAddMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    anAddMethod = findMethod(getBeanClass(), "addEditAgendaListener", 1);
                }
                ;
                java.lang.reflect.Method aRemoveMethod = null;
                try {
                    java.lang.Class aRemoveMethodParameterTypes[] = { dialogPackage.EditAgendaListener.class };
                    aRemoveMethod = getBeanClass().getMethod("removeEditAgendaListener", aRemoveMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aRemoveMethod = findMethod(getBeanClass(), "removeEditAgendaListener", 1);
                }
                ;
                aDescriptor = new java.beans.EventSetDescriptor("editAgenda", dialogPackage.EditAgendaListener.class, eventMethodDescriptors, anAddMethod, aRemoveMethod);
            } catch (Throwable exception) {
                handleException(exception);
                java.lang.String eventMethodNames[] = { "okJButtonAction_actionPerformed", "annullaJButtonAction_actionPerformed" };
                aDescriptor = new java.beans.EventSetDescriptor(getBeanClass(), "editAgenda", dialogPackage.EditAgendaListener.class, eventMethodNames, "addEditAgendaListener", "removeEditAgendaListener");
            }
            ;
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo editAgenda.okJButtonAction_actionPerformed(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor editAgendaokJButtonAction_actionPerformed_javautilEventObjectMethodEventDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = (dialogPackage.EditAgendaListener.class).getMethod("okJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.EditAgendaListener.class), "okJButtonAction_actionPerformed", 1);
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
        return dialogPackage.EditAgenda.class;
    }

    /**
 * Ottiene il nome della classe bean.
 * @return java.lang.String
 */
    public static java.lang.String getBeanClassName() {
        return "dialogPackage.EditAgenda";
    }

    public java.beans.BeanDescriptor getBeanDescriptor() {
        java.beans.BeanDescriptor aDescriptor = null;
        try {
            aDescriptor = new java.beans.BeanDescriptor(dialogPackage.EditAgenda.class);
        } catch (Throwable exception) {
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo getButtonFlagThis().
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor getButtonFlagThisMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = {};
                aMethod = getBeanClass().getMethod("getButtonFlagThis", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "getButtonFlagThis", 0);
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
            java.beans.EventSetDescriptor aDescriptorList[] = { editAgendaEventSetDescriptor() };
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
            java.beans.MethodDescriptor aDescriptorList[] = { categoriaJButton_ActionPerformed_javaawteventActionEventMethodDescriptor(), competenzeJButton_ActionPerformed_javaawteventActionEventMethodDescriptor(), editAgenda_InitializeMethodDescriptor(), getButtonFlagThisMethodDescriptor(), main_javalangString__MethodDescriptor(), okJButton_ActionPerformed_javaawteventActionEventMethodDescriptor(), onorariJButton_ActionPerformed_javaawteventActionEventMethodDescriptor(), praticaJButton_ActionPerformed_javaawteventActionEventMethodDescriptor(), pulisciTuttoMethodDescriptor(), setButtonFlagThis_javalangBooleanMethodDescriptor(), vediCatImp_SceltaJButtonAction_javautilEventObjectMethodDescriptor(), vediCompetenze_SceltaJButtonAction_javautilEventObjectMethodDescriptor(), vediOnorari_SceltaJButtonAction_javautilEventObjectMethodDescriptor(), vediPratiche_SceltaJButtonAction_javautilEventObjectMethodDescriptor() };
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
            java.beans.PropertyDescriptor aDescriptorList[] = { buttonFlagThisPropertyDescriptor(), componentOrientationPropertyDescriptor(), descrPraticheThisPropertyDescriptor() };
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
                aMethod = (dialogPackage.EditAgendaListener.class).getMethod("okJButtonAction_actionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod((dialogPackage.EditAgendaListener.class), "okJButtonAction_actionPerformed", 1);
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
 * Ottiene il descriptor del metodo onorariJButton_ActionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor onorariJButton_ActionPerformed_javaawteventActionEventMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.awt.event.ActionEvent.class };
                aMethod = getBeanClass().getMethod("onorariJButton_ActionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "onorariJButton_ActionPerformed", 1);
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
 * Ottiene il descriptor del metodo praticaJButton_ActionPerformed(java.awt.event.ActionEvent).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor praticaJButton_ActionPerformed_javaawteventActionEventMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.awt.event.ActionEvent.class };
                aMethod = getBeanClass().getMethod("praticaJButton_ActionPerformed", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "praticaJButton_ActionPerformed", 1);
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
 * Ottiene il descriptor del metodo pulisciTutto().
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor pulisciTuttoMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = {};
                aMethod = getBeanClass().getMethod("pulisciTutto", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "pulisciTutto", 0);
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
 * Ottiene il descriptor del metodo setButtonFlagThis(java.lang.Boolean).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor setButtonFlagThis_javalangBooleanMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.lang.Boolean.class };
                aMethod = getBeanClass().getMethod("setButtonFlagThis", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "setButtonFlagThis", 1);
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
 * Ottiene il descriptor del metodo vediCatImp_SceltaJButtonAction(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor vediCatImp_SceltaJButtonAction_javautilEventObjectMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = getBeanClass().getMethod("vediCatImp_SceltaJButtonAction", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "vediCatImp_SceltaJButtonAction", 1);
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
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo vediCompetenze_SceltaJButtonAction(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor vediCompetenze_SceltaJButtonAction_javautilEventObjectMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = getBeanClass().getMethod("vediCompetenze_SceltaJButtonAction", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "vediCompetenze_SceltaJButtonAction", 1);
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
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo vediOnorari_SceltaJButtonAction(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor vediOnorari_SceltaJButtonAction_javautilEventObjectMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = getBeanClass().getMethod("vediOnorari_SceltaJButtonAction", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "vediOnorari_SceltaJButtonAction", 1);
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
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor del metodo vediPratiche_SceltaJButtonAction(java.util.EventObject).
 * @return java.beans.MethodDescriptor
 */
    public java.beans.MethodDescriptor vediPratiche_SceltaJButtonAction_javautilEventObjectMethodDescriptor() {
        java.beans.MethodDescriptor aDescriptor = null;
        try {
            java.lang.reflect.Method aMethod = null;
            try {
                java.lang.Class aParameterTypes[] = { java.util.EventObject.class };
                aMethod = getBeanClass().getMethod("vediPratiche_SceltaJButtonAction", aParameterTypes);
            } catch (Throwable exception) {
                handleException(exception);
                aMethod = findMethod(getBeanClass(), "vediPratiche_SceltaJButtonAction", 1);
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
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }

    /**
 * Ottiene il descriptor della proprieta descrPraticheThis.
 * @return java.beans.PropertyDescriptor
 */
    public java.beans.PropertyDescriptor descrPraticheThisPropertyDescriptor() {
        java.beans.PropertyDescriptor aDescriptor = null;
        try {
            try {
                java.lang.reflect.Method aGetMethod = null;
                try {
                    java.lang.Class aGetMethodParameterTypes[] = {};
                    aGetMethod = getBeanClass().getMethod("getDescrPraticheThis", aGetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aGetMethod = findMethod(getBeanClass(), "getDescrPraticheThis", 0);
                }
                ;
                java.lang.reflect.Method aSetMethod = null;
                try {
                    java.lang.Class aSetMethodParameterTypes[] = { java.lang.String.class };
                    aSetMethod = getBeanClass().getMethod("setDescrPraticheThis", aSetMethodParameterTypes);
                } catch (Throwable exception) {
                    handleException(exception);
                    aSetMethod = findMethod(getBeanClass(), "setDescrPraticheThis", 1);
                }
                ;
                aDescriptor = new java.beans.PropertyDescriptor("descrPraticheThis", aGetMethod, aSetMethod);
            } catch (Throwable exception) {
                handleException(exception);
                aDescriptor = new java.beans.PropertyDescriptor("descrPraticheThis", getBeanClass());
            }
            ;
            aDescriptor.setBound(true);
        } catch (Throwable exception) {
            handleException(exception);
        }
        ;
        return aDescriptor;
    }
}
