package com.bluesky.jwf.controller;

import java.util.HashMap;
import java.util.Map;
import com.bluesky.jwf.component.Component;
import com.bluesky.jwf.component.event.EventHandler;

public class BaseController<M, V> implements Controller<M, V> {

    private Map<String, EventHandler> eventHandlers;

    public BaseController() {
        eventHandlers = new HashMap<String, EventHandler>();
    }

    @Override
    public M getModel() {
        return null;
    }

    @Override
    public void setEventHandler(String eventName, EventHandler handler) {
        eventHandlers.put(eventName, handler);
    }

    @Override
    public void setModel(M model) {
    }

    @Override
    public V getView() {
        return null;
    }

    @Override
    public void setView(V v) {
    }

    @Override
    public void updateModel() {
    }

    @Override
    public void updateView() {
    }

    @Override
    public void handleEvent(String eventName) {
        if (eventHandlers.containsKey(eventName)) {
            EventHandler handler = eventHandlers.get(eventName);
            if (handler != null) handler.handle((Component) getView(), null, null);
        }
    }
}
