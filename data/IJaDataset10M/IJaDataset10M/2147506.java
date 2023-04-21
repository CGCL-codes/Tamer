package org.agile.dfs.rpc.piple;

public class RpcRequest {

    private String interfaceClz;

    private String methodName;

    private Object[] args;

    public String getInterfaceClz() {
        return interfaceClz;
    }

    public void setInterfaceClz(String interfaceClz) {
        this.interfaceClz = interfaceClz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
