package com.cerny.bugtrack.util.view;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public interface ISessionMessagesManager {

    public abstract List<FacesMessage> getMessages();

    public abstract void addInfo(String messageTemplate);

    public abstract void add(Severity severity, String messageTemplate, Object... params);

    public abstract void add(FacesMessage message);
}
