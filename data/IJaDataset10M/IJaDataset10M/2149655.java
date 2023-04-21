package net.sourceforge.hlm.simple.library.parameters.arguments;

import net.sourceforge.hlm.helpers.internal.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.parameters.arguments.*;
import net.sourceforge.hlm.simple.library.context.*;

public abstract class SimpleVariableArgument<T> extends SimpleArgument implements VariableArgument<T> {

    public SimpleVariableArgument(Context<?> outerContext) {
        super(outerContext);
    }

    public abstract SimpleContextPlaceholder<T> getTerm();

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }
}
