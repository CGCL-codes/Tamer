package com.manning.sdmia.ch11.event;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class TestEventHandler implements EventHandler {

    private boolean called = false;

    public TestEventHandler() {
        System.out.println("new TestEventHandler");
    }

    public void handleEvent(Event event) {
        String topicName = event.getTopic();
        System.out.println("Received event for topic " + topicName);
        System.out.println("with properties:");
        String[] propertyNames = event.getPropertyNames();
        for (String propertyName : propertyNames) {
            String propertyValue = (String) event.getProperty(propertyName);
            System.out.println("- " + propertyName + ": " + propertyValue);
        }
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }
}
