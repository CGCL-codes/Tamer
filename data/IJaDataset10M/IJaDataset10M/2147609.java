package br.com.sysmap.crux.widgets.client.event.collapseexpand;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeExpandHandlers extends HasHandlers {

    HandlerRegistration addBeforeExpandHandler(BeforeExpandHandler handler);
}
