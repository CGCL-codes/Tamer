package org.thechiselgroup.choosel.core.client.resources;

import com.google.gwt.event.shared.EventHandler;

public interface ResourceGroupingChangedHandler extends EventHandler {

    void onResourceCategoriesChanged(ResourceGroupingChangedEvent e);
}
