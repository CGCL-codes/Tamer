package net.joindesk.let.definite;

import java.lang.reflect.Method;

public class EventInfo {

    public static final String NO_FIELD = "net.joindesk.let.definite.EventInfo.noField";

    private Method method;

    private String[] field = new String[] { EventInfo.NO_FIELD };

    private String[] event;

    private String[] ref;

    public String[] getEvent() {
        return event;
    }

    public String[] getField() {
        return field;
    }

    public String[] getRef() {
        return ref;
    }

    public Method getMethod() {
        return method;
    }

    public void setEvent(String[] event) {
        this.event = event;
    }

    public void setField(String[] field) {
        if (field == null || field.length == 0) return;
        this.field = field;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setRef(String[] ref) {
        this.ref = ref;
    }
}
