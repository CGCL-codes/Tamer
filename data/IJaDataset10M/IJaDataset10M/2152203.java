package org.matsim.core.mobsim.qsim.comparators;

import java.io.Serializable;
import java.util.Comparator;
import org.matsim.core.api.internal.MatsimComparator;
import org.matsim.core.mobsim.qsim.qnetsimengine.QVehicle;

/**
 * @author dstrippgen
 *
 * Comparator object, to sort the Vehicle objects in QueueLink.parkingList
 * according to their departure time
 */
public class QVehicleDepartureTimeComparator implements Comparator<QVehicle>, Serializable, MatsimComparator {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final QVehicle veh1, final QVehicle veh2) {
        if (veh1.getDriver().getActivityEndTime() > veh2.getDriver().getActivityEndTime()) return 1;
        if (veh1.getDriver().getActivityEndTime() < veh2.getDriver().getActivityEndTime()) return -1;
        return veh2.getId().compareTo(veh1.getId());
    }
}
