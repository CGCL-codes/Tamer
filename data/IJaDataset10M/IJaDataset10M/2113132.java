package uk.ac.ebi.intact.psimitab.converters;

import psidev.psi.mi.tab.model.BinaryInteractionImpl;
import uk.ac.ebi.intact.model.Interaction;

/**
 * Defines how to process an interaction and alter the creating of a binary interaction. If the
 * BinaryInteractionImpl given is an extension of the class, it becomes possible to populate
 * additional columns.
 *
 * @author Nadin Neuhauser
 * @version $Id$
 * @since 2.0.0
 */
public interface BinaryInteractionHandler<T extends BinaryInteractionImpl> {

    /**
     * Does the extra processing on the BinaryInteractionImpl.
     *
     * @param bi          Binary interaction to be processed.
     * @param interaction Source interaction.
     */
    public void process(T bi, Interaction interaction) throws Intact2TabException;

    /**
	 * This method merge could called in ClusterInteractorPairProssesor to write the correct format of lists.
	 *
	 * @param interaction
	 * @param target
	 */
    public void mergeCollection(T interaction, T target);
}
