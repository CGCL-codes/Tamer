package org.nakedobjects.application.office.email;

import java.util.ArrayList;
import java.util.List;
import org.nakedobjects.application.office.PimObject;

public abstract class Message extends PimObject {

    private String subject;

    private final List<Object> contents = new ArrayList<Object>();

    public void forward(EmailAddress address) {
        OutgoingMessage message = newTransientInstance(OutgoingMessage.class);
        message.getTo().add(address);
        message.makePersistent();
        contents.add(message);
    }

    public List<Object> getContents() {
        return contents;
    }

    public String getSubject() {
        resolve();
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
