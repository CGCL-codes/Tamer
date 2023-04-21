package org.gwtoolbox.commons.ui.client.event.custom;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author Uri Boness
 */
public interface HasStopHandlers extends HasHandlers {

    HandlerRegistration addStopHandler(StopHandler handler);
}
