package jsbsim.models.flight_control;

import jsbsim.models.FGFCS;
import jsbsim.models.atmosphere.FGFCSComponent;
import org.jdom.Element;

/** Encapsulates a gradient component for the flight control system.
 */
public class FGGradient extends FGFCSComponent {

    public FGGradient(FGFCS fcs, Element element) {
        super(fcs, element);
        super.bind();
    }

    @Override
    public boolean Run() {
        return true;
    }
}
