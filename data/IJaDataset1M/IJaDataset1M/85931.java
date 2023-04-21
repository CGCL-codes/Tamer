package com.fh.auge.core.event;

import java.util.ArrayList;
import java.util.List;

public class ApplicationEventMulticaster implements ApplicationEventPublisher {

    private List<ApplicationListener> listeners = new ArrayList<ApplicationListener>();

    public void addApplicationListener(ApplicationListener listener) {
        listeners.add(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    public void removeApplicationListener(ApplicationListener listener) {
        listeners.remove(listener);
    }

    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }
}
