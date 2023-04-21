package com.seitenbau.testing.shared.config;

import com.seitenbau.testing.shared.tracer.listener.ConsoleListener;

public class ConsoleTracerConfiguration extends TracerConfiguration {

    public ConsoleTracerConfiguration() {
        super();
        ListenerConfiguration listenerConfiguration = new ListenerConfiguration();
        listenerConfiguration.setClassName(ConsoleListener.class.getCanonicalName());
        addListener(listenerConfiguration);
    }
}
