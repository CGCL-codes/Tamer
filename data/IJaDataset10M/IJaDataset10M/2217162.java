package jsbsim.math;

import jsbsim.input_output.FGPropertyManager;

/** Represents a property value
@author Jon Berndt
 */
public class FGPropertyValue implements FGParameter {

    private FGPropertyManager PropertyManager;

    public FGPropertyValue(FGPropertyManager propNode) {
        PropertyManager = propNode;
    }

    public double GetValue() {
        return PropertyManager.getDoubleValue();
    }
}
