package MScheme.machine;

import MScheme.code.Code;
import MScheme.values.Value;
import MScheme.exceptions.*;

class ValueContinuation extends Continuation {

    private final Code _literal;

    ValueContinuation(Machine machine, Value value) {
        super(machine);
        _literal = value.getLiteral();
    }

    protected Code execute(Machine machine, Value value) throws RuntimeError, TypeError {
        return _literal;
    }
}
