package com.ixora.rms.agents.websphere.v60.proxy;

public final class PmiClientProxyImpl_Stub extends java.rmi.server.RemoteStub implements com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy, java.rmi.Remote {

    private static final long serialVersionUID = 2;

    private static java.lang.reflect.Method $method_configure_0;

    private static java.lang.reflect.Method $method_end_1;

    private static java.lang.reflect.Method $method_getConfigs_2;

    private static java.lang.reflect.Method $method_getErrorCode_3;

    private static java.lang.reflect.Method $method_getErrorCode_4;

    private static java.lang.reflect.Method $method_getErrorMessage_5;

    private static java.lang.reflect.Method $method_getErrorMessage_6;

    private static java.lang.reflect.Method $method_getId_7;

    private static java.lang.reflect.Method $method_getInstrumentationLevel_8;

    private static java.lang.reflect.Method $method_getNLSValue_9;

    private static java.lang.reflect.Method $method_gets_10;

    private static java.lang.reflect.Method $method_listMembers_11;

    private static java.lang.reflect.Method $method_listNodes_12;

    private static java.lang.reflect.Method $method_listServers_13;

    private static java.lang.reflect.Method $method_setInstrumentationLevel_14;

    static {
        try {
            $method_configure_0 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("configure", new java.lang.Class[] { java.util.Properties.class });
            $method_end_1 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("end", new java.lang.Class[] {});
            $method_getConfigs_2 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getConfigs", new java.lang.Class[] {});
            $method_getErrorCode_3 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getErrorCode", new java.lang.Class[] {});
            $method_getErrorCode_4 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getErrorCode", new java.lang.Class[] { java.lang.String.class });
            $method_getErrorMessage_5 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getErrorMessage", new java.lang.Class[] {});
            $method_getErrorMessage_6 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getErrorMessage", new java.lang.Class[] { java.lang.String.class });
            $method_getId_7 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getId", new java.lang.Class[] {});
            $method_getInstrumentationLevel_8 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getInstrumentationLevel", new java.lang.Class[] { java.lang.String.class, java.lang.String.class });
            $method_getNLSValue_9 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("getNLSValue", new java.lang.Class[] { java.lang.String.class, java.lang.String.class });
            $method_gets_10 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("gets", new java.lang.Class[] { com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[].class, boolean.class });
            $method_listMembers_11 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("listMembers", new java.lang.Class[] { com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor.class, boolean.class });
            $method_listNodes_12 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("listNodes", new java.lang.Class[] {});
            $method_listServers_13 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("listServers", new java.lang.Class[] { com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor.class });
            $method_setInstrumentationLevel_14 = com.ixora.rms.agents.websphere.v50.proxy.PmiClientProxy.class.getMethod("setInstrumentationLevel", new java.lang.Class[] { java.lang.String.class, java.lang.String.class, java.lang.String[].class, int.class, boolean.class });
        } catch (java.lang.NoSuchMethodException e) {
            throw new java.lang.NoSuchMethodError("stub class initialization failed");
        }
    }

    public PmiClientProxyImpl_Stub(java.rmi.server.RemoteRef ref) {
        super(ref);
    }

    public void configure(java.util.Properties $param_Properties_1) throws java.rmi.RemoteException {
        try {
            ref.invoke(this, $method_configure_0, new java.lang.Object[] { $param_Properties_1 }, -1092559969828480657L);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public void end() throws java.rmi.RemoteException {
        try {
            ref.invoke(this, $method_end_1, null, 8955468713592660910L);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public com.ibm.websphere.pmi.PmiModuleConfig[] getConfigs() throws java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getConfigs_2, null, -5224504003344905248L);
            return ((com.ibm.websphere.pmi.PmiModuleConfig[]) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public int getErrorCode() throws java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getErrorCode_3, null, 278116748247717677L);
            return ((java.lang.Integer) $result).intValue();
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public int getErrorCode(java.lang.String $param_String_1) throws java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getErrorCode_4, new java.lang.Object[] { $param_String_1 }, 7641845944419624996L);
            return ((java.lang.Integer) $result).intValue();
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public java.lang.String getErrorMessage() throws java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getErrorMessage_5, null, 737982488608468808L);
            return ((java.lang.String) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public java.lang.String getErrorMessage(java.lang.String $param_String_1) throws java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getErrorMessage_6, new java.lang.Object[] { $param_String_1 }, -4707155933354696306L);
            return ((java.lang.String) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public long getId() throws java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getId_7, null, -6040770469254561000L);
            return ((java.lang.Long) $result).longValue();
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public com.ibm.websphere.pmi.client.PerfLevelSpec[] getInstrumentationLevel(java.lang.String $param_String_1, java.lang.String $param_String_2) throws com.ibm.websphere.pmi.PmiException, java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getInstrumentationLevel_8, new java.lang.Object[] { $param_String_1, $param_String_2 }, 8016293632570314198L);
            return ((com.ibm.websphere.pmi.client.PerfLevelSpec[]) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (com.ibm.websphere.pmi.PmiException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public java.lang.String getNLSValue(java.lang.String $param_String_1, java.lang.String $param_String_2) throws java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_getNLSValue_9, new java.lang.Object[] { $param_String_1, $param_String_2 }, 3986983640195101195L);
            return ((java.lang.String) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public com.ibm.websphere.pmi.client.CpdCollection[] gets(com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[] $param_arrayOf_PerfDescriptor_1, boolean $param_boolean_2) throws com.ibm.websphere.pmi.PmiException, java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_gets_10, new java.lang.Object[] { $param_arrayOf_PerfDescriptor_1, ($param_boolean_2 ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE) }, -4258395115160435221L);
            return ((com.ibm.websphere.pmi.client.CpdCollection[]) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (com.ibm.websphere.pmi.PmiException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[] listMembers(com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor $param_PerfDescriptor_1, boolean $param_boolean_2) throws com.ibm.websphere.pmi.PmiException, java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_listMembers_11, new java.lang.Object[] { $param_PerfDescriptor_1, ($param_boolean_2 ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE) }, -5352499618733713991L);
            return ((com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[]) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (com.ibm.websphere.pmi.PmiException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[] listNodes() throws com.ibm.websphere.pmi.PmiException, java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_listNodes_12, null, 7069328300424937298L);
            return ((com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[]) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (com.ibm.websphere.pmi.PmiException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[] listServers(com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor $param_PerfDescriptor_1) throws com.ibm.websphere.pmi.PmiException, java.rmi.RemoteException {
        try {
            Object $result = ref.invoke(this, $method_listServers_13, new java.lang.Object[] { $param_PerfDescriptor_1 }, 8874468723862751118L);
            return ((com.ixora.rms.agents.websphere.v50.proxy.PerfDescriptor[]) $result);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (com.ibm.websphere.pmi.PmiException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    public void setInstrumentationLevel(java.lang.String $param_String_1, java.lang.String $param_String_2, java.lang.String[] $param_arrayOf_String_3, int $param_int_4, boolean $param_boolean_5) throws com.ibm.websphere.pmi.PmiException, java.rmi.RemoteException {
        try {
            ref.invoke(this, $method_setInstrumentationLevel_14, new java.lang.Object[] { $param_String_1, $param_String_2, $param_arrayOf_String_3, new java.lang.Integer($param_int_4), ($param_boolean_5 ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE) }, 2311173418260826309L);
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (com.ibm.websphere.pmi.PmiException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }
}
