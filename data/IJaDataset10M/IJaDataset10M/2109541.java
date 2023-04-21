package org.acaraus.triviachatbot.engine;

import java.util.EventObject;

public class ChatDataEvent extends EventObject {

    Object load;

    String obj_type;

    public ChatDataEvent(Object source, Object load, String obj_type) {
        super(source);
        this.load = load;
        this.obj_type = obj_type;
    }

    public Object getData() {
        return load;
    }

    public String getObjtype() {
        return obj_type;
    }
}
