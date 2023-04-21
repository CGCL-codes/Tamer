package com.gurumades.triggers.client;

import com.gurumades.triggers.client.Trigger;

public class MockTrigger implements Trigger {

    protected String name;

    public MockTrigger(String name) {
        super();
        this.name = name;
    }

    public MockTrigger() {
        super();
    }
}
