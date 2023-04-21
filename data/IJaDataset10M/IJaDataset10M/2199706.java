package simulator.manager.boat.engine;

import graphics.GraphicTransform;
import graphics.GraphicTransformContainer;
import simulator.manager.boat.AbstractBoatComponentManager;
import simulator.vectors.SpacialInfo;

/**
 * 
 * 
 * @author Mariano Tepper
 */
public class DefaultFuelEngineManager extends AbstractBoatComponentManager {

    public DefaultFuelEngineManager(FuelEngine fe, SpacialInfo si) {
        this.fe = fe;
        this.si = si;
    }

    protected void initializeGraphic() {
        GraphicTransform t = new GraphicTransform();
        t.set(si);
        g = new GraphicTransformContainer(t);
        ((GraphicTransformContainer) g).addChild(fe.getGraphic());
    }

    public FuelEngine getFuelEngine() {
        return fe;
    }

    public SpacialInfo getSpacialInfo() {
        return si;
    }

    FuelEngine fe;

    SpacialInfo si;
}
