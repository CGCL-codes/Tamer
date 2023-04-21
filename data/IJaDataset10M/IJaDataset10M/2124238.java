package org.thechiselgroup.choosel.core.client.visualization.behaviors;

import java.util.Map;
import org.thechiselgroup.choosel.core.client.fx.Opacity;
import org.thechiselgroup.choosel.core.client.resources.ui.DetailsWidgetHelper;
import org.thechiselgroup.choosel.core.client.ui.popup.Popup;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupOpacityChangedEvent;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupOpacityChangedEventHandler;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.HighlightingModel;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

public class PopupWithHighlightingVisualItemBehavior extends PopupVisualItemBehavior {

    /**
     * Maps view item ids to popup highlighting managers.
     */
    private Map<String, HighlightingManager> highlightingManagers = CollectionFactory.createStringMap();

    private HighlightingModel hoverModel;

    public PopupWithHighlightingVisualItemBehavior(DetailsWidgetHelper detailsWidgetHelper, PopupManagerFactory popupManagerFactory, HighlightingModel hoverModel) {
        super(detailsWidgetHelper, popupManagerFactory);
        this.hoverModel = hoverModel;
    }

    @Override
    public void onVisualItemCreated(VisualItem visualItem) {
        super.onVisualItemCreated(visualItem);
        assert !highlightingManagers.containsKey(visualItem.getId());
        final HighlightingManager highlightingManager = new HighlightingManager(hoverModel, visualItem.getResources());
        Popup popup = getHandler(visualItem).getPopup();
        popup.addDomHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent e) {
                highlightingManager.setHighlighting(true);
            }
        }, MouseOverEvent.getType());
        popup.addDomHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                highlightingManager.setHighlighting(false);
            }
        }, MouseOutEvent.getType());
        popup.addHandler(new PopupOpacityChangedEventHandler() {

            @Override
            public void onOpacityChangeStarted(PopupOpacityChangedEvent event) {
                if (event.getOpacity() == Opacity.TRANSPARENT) {
                    highlightingManager.setHighlighting(false);
                }
            }
        }, PopupOpacityChangedEvent.TYPE);
        highlightingManagers.put(visualItem.getId(), highlightingManager);
    }

    @Override
    public void onVisualItemRemoved(VisualItem visualItem) {
        super.onVisualItemRemoved(visualItem);
        assert highlightingManagers.containsKey(visualItem.getId());
        HighlightingManager manager = highlightingManagers.remove(visualItem.getId());
        manager.dispose();
    }
}
