package com.eric.formatter.view;

import java.util.HashMap;
import java.util.Map;

public abstract class Facade {

    private Map<String, Mediator> mediators = new HashMap<String, Mediator>();

    protected static Facade instance;

    public void registerMediator(String name, Mediator mediator) {
        mediators.put(name, mediator);
    }

    public Mediator retrieveMediator(String name) {
        return mediators.get(name);
    }

    public Map<String, Mediator> retrieveAllMediators() {
        return mediators;
    }
}
