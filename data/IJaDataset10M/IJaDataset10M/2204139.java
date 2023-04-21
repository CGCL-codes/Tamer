package com.googlecode.jazure.sdk.event;

import java.util.EventObject;

public interface EventPublisher {

    void publishEvent(EventObject event);
}
