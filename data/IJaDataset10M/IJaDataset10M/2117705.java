package com.cerny.bugtrack.util.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

@Name("sessionMessagesManager")
@Scope(ScopeType.SESSION)
public class SessionMessagesManager implements ISessionMessagesManager, Serializable {

    private static final long serialVersionUID = 1L;

    private transient List<FacesMessage> messages;

    public List<FacesMessage> getMessages() {
        if (messages != null) {
            List<FacesMessage> out = new ArrayList<FacesMessage>(messages.size());
            out.addAll(messages);
            messages = null;
            return out;
        } else {
            return null;
        }
    }

    /**
     * add message direct
     * 
     * @param message
     */
    public void add(FacesMessage message) {
        if (messages == null) {
            messages = new ArrayList<FacesMessage>();
        }
        messages.add(message);
    }

    /**
     * add message
     * 
     * @param severity
     * @param messageTemplate
     * @param params
     */
    public void add(Severity severity, String messageTemplate, Object... params) {
        FacesMessage fm = FacesMessages.createFacesMessage(severity, messageTemplate, params);
        add(fm);
    }

    /**
     * short cut
     * @param messageTemplate
     */
    public void addInfo(String messageTemplate) {
        add(FacesMessage.SEVERITY_INFO, messageTemplate, new Object());
    }
}
