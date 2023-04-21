package uk.ac.ebi.intact.confidence.expansion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.confidence.model.InteractionSimplified;
import uk.ac.ebi.intact.confidence.model.ProteinSimplified;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Process an interaction and expand it using the matrix model.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$ altered by iarmean
 * @since <pre>13-Oct-2006</pre>
 */
public class MatrixExpansion extends BinaryExpansionStrategy {

    /**
     * Sets up a logger for that class.
     */
    public static final Log log = LogFactory.getLog(MatrixExpansion.class);

    /**
     * Apply the matrix expansion to the given interaction. Essentially, an interaction is created between any two
     * participant.
     *
     * @param interaction the interaction to expand.
     *
     * @return a non null collection of binary interaction
     */
    public Collection<InteractionSimplified> expand(InteractionSimplified interaction) {
        Collection<InteractionSimplified> interactions = new ArrayList<InteractionSimplified>();
        if (isBinary(interaction)) {
            log.debug("interaction " + interaction.getAc() + "/" + " was binary, no further processing involved.");
            interactions.add(interaction);
        } else {
            ProteinSimplified[] participants = interaction.getInteractors().toArray(new ProteinSimplified[] {});
            log.debug(participants.length + " participant(s) found.");
            for (int i = 0; i < participants.length; i++) {
                ProteinSimplified p1 = participants[i];
                for (int j = (i + 1); j < participants.length; j++) {
                    ProteinSimplified p2 = participants[j];
                    if (log.isDebugEnabled()) {
                        String p1Str = displayComponent(p1);
                        String p2Str = displayComponent(p2);
                        log.debug("Build new binary interaction [" + p1Str + "," + p2Str + "]");
                    }
                    InteractionSimplified newInteraction = buildInteraction(interaction, p1, p2);
                    interactions.add(newInteraction);
                }
            }
            log.debug("After expansion: " + interactions.size() + " binary interaction(s) were generated.");
        }
        return interactions;
    }

    private String displayComponent(ProteinSimplified p) {
        String role = p.getRole();
        String interactor = p.getUniprotAc().getAcNr();
        return interactor + ":" + role;
    }
}
