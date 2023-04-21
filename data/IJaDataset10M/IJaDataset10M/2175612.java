package org.isakiev.xl.model.functions.predefined;

import org.isakiev.xl.model.Value;
import org.isakiev.xl.model.functions.Argument;
import org.isakiev.xl.model.functions.ArgumentVisitor;
import org.isakiev.xl.model.functions.Function;

public class Max implements Function {

    @Override
    public String getName() {
        return "Max";
    }

    @Override
    public Value execute(Argument... arguments) {
        MaxArgumentVisitor visitor = new MaxArgumentVisitor();
        for (Argument argument : arguments) {
            argument.accept(visitor);
        }
        return visitor.getResult();
    }

    private static class MaxArgumentVisitor implements ArgumentVisitor {

        private Value maximum;

        private boolean maximumSpecified = false;

        private Value defaultValue = Value.ZERO;

        @Override
        public void visit(Value value) {
            if (!maximumSpecified) {
                maximum = value;
                maximumSpecified = true;
            } else if (!maximum.isErroneous()) {
                if (value.isErroneous() || value.getValue() > maximum.getValue()) {
                    maximum = value;
                }
            }
        }

        public Value getResult() {
            return maximumSpecified ? maximum : defaultValue;
        }
    }
}
