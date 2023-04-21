package org.matsim.ptproject.qsim.qnetsimengine;

import java.util.HashMap;
import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.ptproject.qsim.QSim;
import org.matsim.ptproject.qsim.interfaces.NetsimEngine;
import org.matsim.ptproject.qsim.interfaces.NetsimLink;

public abstract class QLinkInternalI extends QBufferItem implements NetsimLink {

    private Map<String, Object> customAttributes = new HashMap<String, Object>();

    abstract void setQSimEngine(NetsimEngine qsimEngine);

    protected abstract boolean moveLink(double now);

    abstract boolean hasSpace();

    abstract void clearVehicles();

    abstract QVehicle removeParkedVehicle(Id vehicleId);

    abstract void activateLink();

    abstract void addFromIntersection(final QVehicle veh);

    abstract QSimEngineInternalI getQSimEngine();

    @Override
    public Map<String, Object> getCustomAttributes() {
        return customAttributes;
    }

    /**
	 * modifying the return type ...
	 */
    @Override
    public abstract QSim getMobsim();
}
