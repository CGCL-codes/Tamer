package uk.ac.ebi.intact.confidence.expansion;

import java.util.Arrays;
import uk.ac.ebi.intact.confidence.model.ProteinSimplified;
import uk.ac.ebi.intact.confidence.model.InteractionSimplified;

/**
 * ABstraction of an expansion strategy.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since <pre>13-Oct-2006</pre>
 */
public abstract class BinaryExpansionStrategy implements ExpansionStrategy {

    /**
     * Determines if the interaction is binary. That is involve exactly two participants.
     *
     * @param interaction the interaction to check on.
     *
     * @return true if the interaction is binary.
     */
    protected boolean isBinary(InteractionSimplified interaction) {
        if (interaction == null) {
            throw new IllegalArgumentException("Interaction must not be null.");
        }
        if (interaction.getInteractors().size() == 2) {
            return true;
        }
        return false;
    }

    /**
     * Build a new Interaction with no participants but holding a copy of all other attributes of the given interaction.
     * <br/> WARNING: all attributes of the newly created interaction are the same as the source's (that is same object
     * instance). This is not an object clone.
     *
     * @param source the source interaction.
     *
     * @return a target interaction which is a copy of the source interaction but the participants or
     *         inferredInteractions.
     */
    protected InteractionSimplified copyInteraction(InteractionSimplified source) {
        InteractionSimplified target = new InteractionSimplified();
        target.setAc(source.getAc());
        return target;
    }

    /**
     * Builds a new interaction object based the given interaction template.
     * <br/> Participants are replaced by the two given ones.
     *
     * @param interactionTemplate the interaction template.
     * @param p1 participant to add to the newly created interaction.
     * @param p2 participant to add to the newly created interaction.
     *
     * @return a new interaction having p1 and p2 as participant.
     */
    protected InteractionSimplified buildInteraction(InteractionSimplified interactionTemplate, ProteinSimplified p1, ProteinSimplified p2) {
        InteractionSimplified i = copyInteraction(interactionTemplate);
        i.setInteractors(Arrays.asList(p1, p2));
        return i;
    }
}
