package com.kongur.network.erp.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import com.eyeieye.melody.web.cookyjar.Cookyjar;
import com.kongur.network.erp.domain.ErpAgent;

/**
 *
 * @author fish
 *
 */
public class ErpAgentArgumentResolver implements WebArgumentResolver {

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        if (methodParameter.getParameterType().equals(ErpAgent.class)) {
            Cookyjar cookyjar = (Cookyjar) webRequest.getAttribute(Cookyjar.CookyjarInRequest, RequestAttributes.SCOPE_REQUEST);
            if (cookyjar != null) {
                return cookyjar.getObject(ErpAgent.class);
            }
        }
        return UNRESOLVED;
    }
}
