package uk.ac.ebi.intact.sanity.rules.experiment;

import uk.ac.ebi.intact.model.Experiment;
import uk.ac.ebi.intact.sanity.commons.SanityRuleException;
import uk.ac.ebi.intact.sanity.commons.annotation.SanityRule;
import uk.ac.ebi.intact.sanity.commons.rules.GeneralMessage;
import uk.ac.ebi.intact.sanity.commons.rules.MessageLevel;
import uk.ac.ebi.intact.sanity.commons.rules.Rule;
import uk.ac.ebi.intact.sanity.commons.rules.MessageDefinition;
import uk.ac.ebi.intact.sanity.rules.RuleGroup;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Check on experiment without participant detection method.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk), Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.0
 */
@SanityRule(target = Experiment.class, group = { RuleGroup.INTACT, RuleGroup.IMEX })
public class ExperimentWithNoCvIdentification implements Rule<Experiment> {

    public Collection<GeneralMessage> check(Experiment experiment) throws SanityRuleException {
        Collection<GeneralMessage> messages = new ArrayList<GeneralMessage>();
        if (experiment.getCvIdentification() == null) {
            messages.add(new GeneralMessage(MessageDefinition.EXPERIMENT_WITHOUT_PARTICIPANT_DETECT, experiment));
        }
        return messages;
    }
}
