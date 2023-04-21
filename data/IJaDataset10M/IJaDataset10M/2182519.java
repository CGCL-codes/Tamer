package playground.kai.ids;

import org.matsim.api.core.v01.Id;

/**The necessary steps seem to be:<ul>
 * <li> Generate interface PersonId.
 * <li> Make IdImpl implement PersonId.
 * <li> Make PersonImpl internally store PersonId instead of Id, and return PersonId.
 * <li> Generate PopulationIdFactory.createId(...) that creates PersonId.
 * <li> Set Scenario.createId(...) to deprecated.
 * <li> Set new IdImpl(...) to deprecated.
 * <li> As a perspective, I would like to make new IdImpl(...) protected so that it cannot be used any longer. 
 * </ul>
 * @author nagel
 */
public interface PersonId extends Id {
}
