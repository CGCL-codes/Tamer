package org.warko.app.listener;

import org.warko.app.event.StreamApplicationEvent;

public interface StreamApplicationListener {

    public void streamEventOccured(StreamApplicationEvent evt);
}
