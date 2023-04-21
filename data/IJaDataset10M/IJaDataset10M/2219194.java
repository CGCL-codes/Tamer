package com.foursoft.fourever.variants.modification.impl;

import java.util.Iterator;
import com.foursoft.fourever.objectmodel.EntityInstance;
import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.fourever.objectmodel.StringInstance;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.variants.impl.VariantException;

public class OpElementEntfernen extends TeilAenderungsoperationPerformer {

    EntityInstance op = null;

    private String eingabewertEntferntesElement;

    private EntityInstance victimOfChange;

    public OpElementEntfernen(EntityInstance refTausch) throws TypeMismatchException {
        op = refTausch;
        eingabewertEntferntesElement = ((StringInstance) op.getOutgoingLink("EingabewertEntferntesElement").getFirstTarget()).getValue();
    }

    public void perform(EntityInstance concreteOperation) throws VariantException, ModificationAbortedException {
        try {
            parseConcreteOperation(concreteOperation);
            if (victimOfChange == null) throw new ModificationAbortedException("Could not perform ElementEntfernen operation: input elements were not completely provided.");
            victimOfChange.remove();
        } catch (ModificationAbortedException e) {
            throw e;
        } catch (Exception e) {
            throw new VariantException("Could not perform ElementEntfernen operation. " + e, e);
        }
    }

    protected void parseConcreteOperation(EntityInstance concreteOperation) throws VariantException, TypeMismatchException {
        Iterator<Instance> target = concreteOperation.getTargetInstances(eingabewertEntferntesElement);
        if (!target.hasNext()) {
            throw new VariantException("Could not find instance for '" + eingabewertEntferntesElement + "' in operation '" + op.getParentLink().getBinding().getBindingName() + "'", new Exception());
        }
        Instance inst = target.next();
        if (inst == null || !(inst instanceof EntityInstance)) throw new VariantException("Could not find instance for '" + eingabewertEntferntesElement + "' in operation '" + op.getParentLink().getBinding().getBindingName() + "'", new Exception());
        victimOfChange = (EntityInstance) inst;
    }
}
