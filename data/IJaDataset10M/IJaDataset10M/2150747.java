package com.googlecode.hmvc4java.controller;

import com.googlecode.hmvc4java.IInitializeable;
import com.googlecode.hmvc4java.component.IComponent;
import com.googlecode.hmvc4java.event.IEvent;
import com.googlecode.hmvc4java.event.IEventListener;

public interface IController extends IInitializeable, IEventListener {

    public IComponent getComponent();

    public void setComponent(IComponent component);

    public void fireEventUp(IEvent event);

    public void fireEventDown(IEvent event);

    public void addChildComponent(IComponent component);
}
