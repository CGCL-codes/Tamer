package org.starobjects.tested.fitnesse.internal.fixtures.perform;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;
import org.starobjects.tested.fitnesse.internal.fixtures.UsingNakedObjectsViewer.Mode;
import fit.Parse;

public class GetActionParameterDefault extends PerformAbstractTypeParams {

    private NakedObject result;

    public GetActionParameterDefault(Mode mode) {
        super("get action parameter default", Type.ACTION, NumParameters.ONE, mode);
    }

    @Override
    public void doHandle(final PerformContext performContext) {
        final NakedObject onAdapter = performContext.getOnAdapter();
        final NakedObjectMember nakedObjectMember = performContext.getNakedObjectMember();
        final Parse arg0Cell = performContext.getArg0Binding().getCurrentCell();
        int requestedParamNum = -1;
        try {
            requestedParamNum = Integer.valueOf(arg0Cell.text());
        } catch (NumberFormatException ex) {
            getOwner().throwException(arg0Cell, ex.getMessage());
        }
        NakedObjectAction noa = (NakedObjectAction) nakedObjectMember;
        int parameterCount = noa.getParameterCount();
        if (requestedParamNum < 0 || requestedParamNum > parameterCount - 1) {
            getOwner().throwException(arg0Cell, "(must be between 0 and " + (parameterCount - 1) + ")");
        }
        NakedObject[] defaults = noa.getDefaults(onAdapter);
        result = defaults[requestedParamNum];
    }

    public NakedObject getResult() {
        return result;
    }
}
