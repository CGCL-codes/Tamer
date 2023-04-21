package org.elascript.interpreter;

import org.elascript.*;

public final class ScriptSecurityException extends InterpretingException {

    public ScriptSecurityException(int line) {
        super(line, "The operation has been forbidden.");
    }
}
