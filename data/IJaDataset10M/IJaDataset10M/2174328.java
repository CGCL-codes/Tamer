package petrivis.view;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;

/**
 * Display of the Petri Net
 * @author Jorge Munoz
 */
public class PNDisplay extends Display {

    public PNDisplay() {
        super(new Visualization());
        reset();
        setHighQuality(true);
        addControlListener(new NotDecoratorsDragControl());
        addControlListener(new ZoomControl());
        addControlListener(new PanControl());
        addControlListener(new WheelZoomControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new FireTransitionControl());
    }
}
