package org.starobjects.tested.fitnesse.internal.fixtures.perform.checkthat.property;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.starobjects.tested.fitnesse.internal.fixtures.perform.PerformContext;
import org.starobjects.tested.fitnesse.internal.fixtures.perform.checkthat.ThatSubcommandAbstract;

public class Empty extends ThatSubcommandAbstract {

    public Empty() {
        super("is empty");
    }

    public NakedObject that(final PerformContext performContext) {
        OneToOneAssociation otoa = (OneToOneAssociation) performContext.getNakedObjectMember();
        NakedObject resultAdapter = otoa.get(performContext.getOnAdapter());
        if (resultAdapter != null) {
            String actualStr = getOwner().getAlias(resultAdapter);
            if (actualStr == null) {
                actualStr = resultAdapter.titleString();
            }
            getOwner().throwException(performContext.getThatBinding().getCurrentCell(), actualStr);
        }
        return null;
    }
}
