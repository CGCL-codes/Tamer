package org.sodeja.explicit;

import java.util.List;
import org.sodeja.runtime.scheme.SchemeExpression;

class ApplyOperandLoop implements Executable {

    @Override
    public String execute(Machine machine) {
        machine.argl.save();
        List<SchemeExpression> operands = machine.unev.getValue();
        SchemeExpression headOperand = operands.get(0);
        machine.exp.setValue(headOperand);
        if (operands.size() == 1) {
            return "ev-appl-last-arg";
        }
        machine.env.save();
        machine.unev.save();
        machine.cont.setValue("ev-appl-accumulate-arg");
        return "eval-dispatch";
    }
}
