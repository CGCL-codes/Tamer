package org.ft4j.handler;

import org.ft4j.Caller;

public class RuntimeThrowing implements FTHandler {

    private String iFaultName = "unknown";

    public RuntimeThrowing(String pFaultName) {
        iFaultName = pFaultName;
    }

    public Object handle(String pCode, Object pCheck, Caller pCaller) {
        throw new RuntimeException(FT_PREFIX + iFaultName + ":code=" + pCode + ",caller=" + pCaller + ",check=" + pCheck);
    }
}
