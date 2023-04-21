package com.realtournament.web.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AbstractProcessingFilter;

public class LoginErrorPhaseListener implements PhaseListener {

    private static final long serialVersionUID = -1216620620302322995L;

    public void beforePhase(final PhaseEvent arg0) {
        Exception e = (Exception) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
        if (e instanceof BadCredentialsException) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY, null);
            WebHelper.addMessage("Username or password not valid.", "Login error", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void afterPhase(final PhaseEvent arg0) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
