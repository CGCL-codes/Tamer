package net.sf.doolin.app.svena.gui.support;

import net.sf.doolin.app.svena.gui.bean.AuthzBean;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.display.DisplayState;

public class PathNotExistingDSH extends AbstractAuthzDSH {

    public PathNotExistingDSH() {
        super(AuthzBean.PATH);
    }

    @Override
    public DisplayState getDisplayState(ActionContext actionContext) {
        AuthzBean authz = getAuthz(actionContext);
        boolean existingPath = authz.isPathExisting(authz.getPath());
        if (existingPath) {
            return DisplayState.DISABLED;
        } else {
            return DisplayState.ENABLED;
        }
    }
}
