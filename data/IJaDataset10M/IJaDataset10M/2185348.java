package org.judo.frontcontroller;

import java.util.List;
import org.judo.validation.ValidationMessage;

public class ValidationFailed extends Exception {

    List<ValidationMessage> msgs;

    public ValidationFailed(List<ValidationMessage> msgs) {
        this.msgs = msgs;
    }

    public List<ValidationMessage> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<ValidationMessage> msgs) {
        this.msgs = msgs;
    }
}
