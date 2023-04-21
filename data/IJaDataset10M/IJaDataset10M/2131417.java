package logop.security;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class ForcedLoginPhaseListener implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        boolean onLoginPage = (-1 != context.getViewRoot().getViewId().lastIndexOf("login")) ? true : false;
        UserSession userSession = (UserSession) accessBeanFromFacesContext("userSession", context);
        boolean isLoggedIn = userSession.isLoggedIn();
        if (!onLoginPage && !isLoggedIn) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "login");
        }
    }

    public static Object accessBeanFromFacesContext(final String theBeanName, final FacesContext theFacesContext) {
        final Object returnObject = theFacesContext.getELContext().getELResolver().getValue(theFacesContext.getELContext(), null, theBeanName);
        return returnObject;
    }

    public void beforePhase(PhaseEvent arg0) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
