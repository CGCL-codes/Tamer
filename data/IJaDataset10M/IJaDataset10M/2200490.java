package com.metaparadigm.jsonrpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * An LocalArgResolver implementation that is registered by default
 * on the JSONRPCBridge and will replace an HttpServletRequest argument
 * on a called method with the current request object.
 */
public class HttpServletRequestArgResolver implements LocalArgResolver {

    public Object resolveArg(Object context) throws LocalArgResolveException {
        if (!(context instanceof HttpServletRequest)) throw new LocalArgResolveException("invalid context");
        return context;
    }
}
