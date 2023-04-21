package org.thechiselgroup.choosel.visualization_component.map.client;

import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemContainer;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemRenderer;
import com.google.gwt.maps.client.MapWidget;

public interface MapRenderer extends VisualItemRenderer {

    void init(MapWidget map, VisualItemContainer container);

    void onAttach();

    void onDetach();
}
