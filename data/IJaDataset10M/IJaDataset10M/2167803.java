package hub.metrik.lang.eprovide.domainspecific;

import java.util.Collection;
import hub.metrik.lang.eprovide.debuggingstate.MVariable;
import org.eclipse.debug.core.DebugException;

public class FieldValueImpl extends ValueImpl {

    public FieldValueImpl(ModefDebugTarget debugTarget, VariableImpl ownerVariable) {
        super(debugTarget, ownerVariable, null);
    }

    @Override
    public String getValueString() throws DebugException {
        String valueString = "[";
        Collection<MVariable> innerVariables = ownerVariable.getModelElement().getInnerVariables();
        int last = innerVariables.size() - 1;
        int i = 0;
        for (MVariable var : innerVariables) {
            if (var.getMValue() != null) {
                valueString += var.getMValue().getLabelText();
            } else {
                valueString += "<undefined>";
            }
            if (i < last) {
                valueString += ", ";
            }
            i++;
        }
        valueString += "]";
        return valueString;
    }
}
