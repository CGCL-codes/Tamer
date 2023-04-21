package com.fusteeno.util.event;

import java.util.EventListener;

public interface ListListener extends EventListener {

    void elementAdded(ListEvent event);

    void elementRemoved(ListEvent event);
}
