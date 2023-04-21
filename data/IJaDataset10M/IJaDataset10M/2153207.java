package MScheme.machine;

import MScheme.Value;
import MScheme.Code;
import MScheme.exceptions.RuntimeError;
import MScheme.exceptions.TypeError;

public abstract class Result extends Code {

    public static final String id = "$Id: Result.java 309 2001-09-06 11:50:20Z sielenk $";

    protected abstract Value getValue(Registers registers) throws RuntimeError;

    public final Code executionStep(Registers registers) throws RuntimeError, TypeError {
        return registers.getContinuation().invoke(registers, getValue(registers));
    }
}
