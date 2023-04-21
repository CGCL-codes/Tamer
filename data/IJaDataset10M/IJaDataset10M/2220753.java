package adc.app.ui.wicket.security;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;

public class AuthenticationCheckAuthorizationStrategy implements IAuthorizationStrategy {

    @Override
    public boolean isActionAuthorized(Component component, Action action) {
        AuthenticationRequired autReq = component.getClass().getAnnotation(AuthenticationRequired.class);
        if (autReq == null) {
            return true;
        }
        return true;
    }

    @Override
    public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
        AuthenticationRequired autReq = componentClass.getAnnotation(AuthenticationRequired.class);
        if (autReq == null) {
            return true;
        }
        if (!ComponentAction.CREATE.equals(autReq.forAction())) {
            return true;
        }
        return true;
    }
}
