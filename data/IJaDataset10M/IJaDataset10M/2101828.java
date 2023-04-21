package petrieditor.visual.renderer;

import petrieditor.visual.view.PlaceComponent;
import petrieditor.visual.view.TransitionComponent;
import java.awt.*;

/**
 * @author wiktor
 */
public class SimulationRenderer extends DefaultRendererBase {

    protected Color getTransitionColor(TransitionComponent component) {
        return component.getModel().isEnabled() ? Color.ORANGE : Color.BLACK;
    }

    protected Color getPlaceColor(PlaceComponent component) {
        return component.isHover() ? HOVER_COLOR : Color.BLACK;
    }
}
