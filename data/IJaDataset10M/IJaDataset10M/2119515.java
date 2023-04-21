package org.sodeja.runtime.scheme3;

import org.sodeja.runtime.Frame;
import org.sodeja.runtime.Library;
import org.sodeja.runtime.scheme.model.Symbol;

public abstract class CompiledSchemeLibrary implements Library<CompiledSchemeExpression> {

    public void extend(Frame<CompiledSchemeExpression> frame, String name, Object value) {
        frame.addObject(new NameExpression(new Symbol(name)), value);
    }
}
