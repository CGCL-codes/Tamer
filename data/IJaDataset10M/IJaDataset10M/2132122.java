package wicket.in.action.common;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;

public interface IAuthorizationStrategy {

    public static final IAuthorizationStrategy ALLOW_ALL = new IAuthorizationStrategy() {

        public boolean isActionAuthorized(Component c, Action action) {
            return true;
        }

        public boolean isInstantiationAuthorized(final Class c) {
            return true;
        }
    };

    boolean isActionAuthorized(Component component, Action action);

    boolean isInstantiationAuthorized(Class componentClass);
}
