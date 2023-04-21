package org.timepedia.chronoscope.client.event;

import com.google.gwt.event.shared.EventHandler;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 *
 */
@ExportPackage("chronoscope")
@ExportClosure
public interface OverlayChangeHandler extends EventHandler, Exportable {

    void onOverlayChanged(OverlayChangeEvent event);
}
