package org.dyno.visual.swing.plugin.spi;

import java.awt.Point;
import java.awt.Rectangle;

public interface IEditorAdapter {

    void setHotspot(Point hotspot);

    IEditor getEditorAt();

    void setWidgetValue(Object value);

    Object getWidgetValue();

    Rectangle getEditorBounds();
}
