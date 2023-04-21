package org.starobjects.tested.fitnesse.internal.fixtures.perform.checkthat.collection;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.starobjects.tested.fitnesse.internal.fixtures.perform.PerformContext;
import org.starobjects.tested.fitnesse.internal.fixtures.perform.checkthat.ProposedArgumentValidityAbstract;
import org.starobjects.tested.fitnesse.internal.fixtures.perform.checkthat.ValidityAssertion;

public class ProposedRemoveFrom extends ProposedArgumentValidityAbstract {

    public ProposedRemoveFrom(final ValidityAssertion assertion) {
        super(assertion);
    }

    protected Consent determineConsent(final PerformContext performContext, NakedObject toValidateAdapter) {
        final NakedObject onAdapter = performContext.getOnAdapter();
        final OneToManyAssociation otma = (OneToManyAssociation) performContext.getNakedObjectMember();
        return otma.isValidToRemove(onAdapter, toValidateAdapter);
    }
}
