package org.w4tj.controller.msg;

import java.util.HashMap;
import java.util.Map;
import org.w4tj.controller.Controller;

public class DefaultMessage implements Message {

    private static final long serialVersionUID = 1L;

    private final long _id;

    private final Controller _sender;

    private Map<String, Object> _args;

    public DefaultMessage(Controller sender, long id) {
        _sender = sender;
        _id = id;
    }

    public DefaultMessage(Controller sender, long id, Map<String, Object> args) {
        this(sender, id);
        _args = args;
    }

    @Override
    public Map<String, Object> getArgs() {
        if (_args == null) {
            _args = new HashMap<String, Object>();
        }
        return _args;
    }

    @Override
    public long getId() {
        return _id;
    }

    @Override
    public Controller getSender() {
        return _sender;
    }
}
