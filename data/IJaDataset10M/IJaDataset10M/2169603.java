package perun.isle.nursing;

import java.io.Serializable;
import perun.isle.DigitalOrganism;
import perun.isle.virtualmachine.GeneticCodeTape;

/**
 *  A new digital organism has to be placed somewhere in the lifeSpace.
 *  Classes implementing this interface can perform this task.
 */
public interface Nurse extends Serializable {

    /**
	 * A new organism should be placed somewhere in the parent's neighbourhood.
	 * @param newborn The newly created organism.
	 * @param pos The position of the parent organism.
	 * @param inherited_length The parent's effective length. The newborns use this in their first update.
	 * @return new digital organism
	 */
    DigitalOrganism placeNewBorn(GeneticCodeTape newborn, int seedID, Object pos);
}
