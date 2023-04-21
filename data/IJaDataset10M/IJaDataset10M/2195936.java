package com.struts2valestack.test;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.PreResultListener;

public class PreResultListenerTest implements PreResultListener {

    private String mes = "";

    public void beforeResult(ActionInvocation actionInvocation, String arg1) {
        actionInvocation.getStack().set("test", this.getMes());
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
