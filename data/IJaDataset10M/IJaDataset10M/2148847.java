package org.matsim.api.core.v01.population;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.core.api.internal.MatsimFactory;

/**
 * @author dgrether
 */
public interface PopulationFactory extends MatsimFactory {

    Person createPerson(Id id);

    Plan createPlan();

    /**
	 * creates an Activity from a coordinate.  The link which attaches the coordinate to the link
	 * will be created automatically somewhere in the process (if you use the Control(l)er).
	 * @param actType
	 * @param coord
	 * @return
	 * <p/>
	 * It might in fact make sense to add a creational method that takes coord <i>and</i> link id.  kai, aug'10
	 */
    Activity createActivityFromCoord(String actType, Coord coord);

    /**
	 * creates an Activity from a link id.  Presumably sets (and then keeps) coord at null.  This does
	 * <i> not </i> add the activity into the plan.
	 * @param actType - the type of the activity, which needs to correspond to some string in the config file
	 * @param coord - the coordinate of the activity.  needs to make sense in the coordinate system of the nodes
	 * @return the activity
	 */
    Activity createActivityFromLinkId(String actType, Id linkId);

    Leg createLeg(String legMode);
}
