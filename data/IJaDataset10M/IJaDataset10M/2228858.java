package de.schlund.pfixcore.scriptedflow.vm;

import java.util.List;
import java.util.Map;
import de.schlund.pfixcore.scriptedflow.vm.pvo.ParamValueObject;

public class ExitInstruction implements Instruction {

    private Map<String, List<ParamValueObject>> params;

    public ExitInstruction(Map<String, List<ParamValueObject>> params) {
        this.params = params;
    }

    Map<String, List<ParamValueObject>> getParams() {
        return params;
    }
}
