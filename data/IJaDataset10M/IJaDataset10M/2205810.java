package rescuecore.objects;

import rescuecore.Property;
import rescuecore.RescueObject;

public abstract class RealObject extends RescueObject {

    protected RealObject() {
        super();
    }

    public Property getProperty(int property) {
        return super.getProperty(property);
    }
}
