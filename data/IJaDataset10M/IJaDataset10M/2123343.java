package playground.johannes.plans.plain;

import org.matsim.api.basic.v01.TransportMode;

/**
 * @author illenberger
 *
 */
public interface PlainLeg extends PlainPlanElement {

    public TransportMode getMode();

    public void setMode(TransportMode mode);

    public PlainRoute getRoute();

    public void setRoute(PlainRoute route);
}
