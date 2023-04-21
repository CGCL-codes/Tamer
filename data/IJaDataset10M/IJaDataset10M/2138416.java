package org.localstorm.mcc.web.actions;

import org.localstorm.mcc.web.BaseActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.localstorm.tools.aop.runtime.Logged;

@UrlBinding("/actions/Logout")
public class LogoutActionBean extends BaseActionBean {

    @DefaultHandler
    @Logged
    public Resolution logout() {
        this.getSession().invalidate();
        return new RedirectResolution("/actions/gtd/nil/ViewFlightPlan", true);
    }
}
