package org.agile.dfs.rpc.client;

import java.io.IOException;
import java.lang.reflect.Method;
import org.agile.dfs.rpc.endpoint.EndpointFactory;
import org.agile.dfs.rpc.endpoint.Endpointable;
import org.agile.dfs.rpc.exception.RpcOperateException;
import org.agile.dfs.rpc.piple.RpcRequest;
import org.agile.dfs.rpc.piple.RpcResponse;
import org.agile.dfs.rpc.util.MulValueLocal;

@SuppressWarnings("unchecked")
public class RpcProxy {

    private static final MulValueLocal local = MulValueLocal.newInstance();

    private static final EndpointFactory endpointMgr = new EndpointFactory();

    private Endpointable endpoint;

    private Class interfaceClz;

    private RpcCaller caller;

    public RpcProxy(String endstring, Class clz) {
        this.interfaceClz = clz;
        this.endpoint = endpointMgr.findEndpoint(endstring);
        this.caller = new RpcCaller(endpoint);
    }

    public Object invoke(Method method, Object[] args) {
        RpcRequest req = new RpcRequest();
        req.setInterfaceClz(interfaceClz.getName());
        req.setMethodName(method.getName());
        req.setArgs(args);
        try {
            local.set("dfs.endpoint", endpoint);
            RpcResponse resp = caller.call(req);
            if (resp.isSucess()) {
                return resp.getResult();
            } else {
                throw new RpcOperateException("Remote handle error [ " + resp.getResult() + "]");
            }
        } catch (IOException e) {
            throw new RpcOperateException("Network io exception! ", e);
        } finally {
        }
    }
}
