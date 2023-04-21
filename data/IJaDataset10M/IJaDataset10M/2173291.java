package org.brandao.brutos;

import org.brandao.brutos.interceptor.InterceptorHandler;
import org.brandao.brutos.scope.Scope;
import org.brandao.brutos.mapping.Controller;
import org.brandao.brutos.mapping.MethodForm;

/**
 * Implementação padrão do ActionResolver.
 * 
 * @author Afonso Brandao
 */
public class DefaultActionResolver implements ActionResolver {

    public ResourceAction getResourceAction(Controller controller, InterceptorHandler handler) {
        Scope scope = handler.getContext().getScopes().get(ScopeType.PARAM);
        return getResourceAction(controller, String.valueOf(scope.get(controller.getMethodId())), handler);
    }

    public ResourceAction getResourceAction(Controller controller, String actionId, InterceptorHandler handler) {
        MethodForm method = controller.getMethodByName(actionId);
        return method == null ? null : getResourceAction(method);
    }

    public ResourceAction getResourceAction(MethodForm action) {
        return new DefaultResourceAction(action);
    }
}
