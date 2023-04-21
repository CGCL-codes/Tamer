package apollo.dataadapter.ensj.controller;

import java.awt.event.*;
import apollo.dataadapter.ensj.*;
import apollo.dataadapter.ensj.view.*;
import apollo.dataadapter.ensj.controller.*;

/**
 * Generic class to forward any old events onto central handler.
**/
public abstract class EventRouter {

    String key;

    Controller handler;

    public EventRouter(Controller handler, String key) {
        this.handler = handler;
        this.key = key;
    }

    protected Controller getHandler() {
        return handler;
    }

    protected String getKey() {
        return key;
    }
}
