package test.org.protune;

import java.io.Serializable;
import org.protune.core.Action;
import org.protune.core.ActionWellPerformed;
import org.protune.core.Notification;

public class DummyAction extends Action implements Serializable {

    static final long serialVersionUID = 71;

    public String toWrite;

    public DummyAction(String s) {
        toWrite = "";
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '_') toWrite += 'a'; else toWrite += s.charAt(i);
    }

    public Notification perform() {
        System.out.println(toWrite);
        return new ActionWellPerformed(this);
    }

    public String toString() {
        return toWrite;
    }
}
