package com.hifi.core.api.ui.listeners;

import java.util.EventListener;
import com.hifi.core.api.ui.events.SourceListEvent;

public interface ISimpleListEventListener extends EventListener {

    public void onSourceContextEvent(SourceListEvent evt);

    public void onSourceSelect(SourceListEvent evt);
}
