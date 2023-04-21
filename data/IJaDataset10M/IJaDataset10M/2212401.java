package uk.ac.lkl.migen.system.ai.feedback.intervention;

import uk.ac.lkl.migen.system.ai.um.IndicatorClass;

public class InterventionGeneratedOccurrence extends InterventionIndicatorOccurrence {

    public InterventionGeneratedOccurrence(Intervention intervention) {
        super(IndicatorClass.INTERVENTION_GENERATED, intervention);
    }

    public InterventionGeneratedOccurrence(Intervention intervention, long timestampLong) {
        super(IndicatorClass.INTERVENTION_GENERATED, intervention, timestampLong);
    }
}
