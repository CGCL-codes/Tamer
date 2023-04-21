package com.justin.foundation.messaging;

import javax.jms.Queue;

public interface IQueueLocator extends IJmsDestinationLocator {

    public Queue lookup();
}
