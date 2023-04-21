package net.sf.jguard.jsf.authentication.filters;

import javax.inject.Inject;
import net.sf.jguard.core.authentication.filters.AuthenticationFilter;
import net.sf.jguard.core.authorization.filters.AuthorizationFilter;
import net.sf.jguard.core.enforcement.GuestPolicyEnforcementPointFilter;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 */
public class JSFGuestPolicyEnforcementPointFilter extends GuestPolicyEnforcementPointFilter<FacesContext, FacesContext> {

    @Inject
    public JSFGuestPolicyEnforcementPointFilter(List<AuthenticationFilter<FacesContext, FacesContext>> guestAuthenticationFilters, List<AuthorizationFilter<FacesContext, FacesContext>> guestAuthorizationFilters, List<AuthenticationFilter<FacesContext, FacesContext>> restfulAuthenticationFilters, List<AuthorizationFilter<FacesContext, FacesContext>> restfulAuthorizationFilters) {
        super(guestAuthenticationFilters, guestAuthorizationFilters, restfulAuthenticationFilters, restfulAuthorizationFilters);
    }
}
