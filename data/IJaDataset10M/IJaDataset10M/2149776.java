package net.sf.julie.library.io;

import net.sf.julie.Interpretable;
import net.sf.julie.types.LiteralBoolean;
import net.sf.julie.types.Procedure;
import net.sf.julie.types.io.OutputPort;

public class IsOutputPort extends Procedure {

    @Override
    public Interpretable interpret() {
        return LiteralBoolean.valueOf((arguments.get(0) instanceof OutputPort));
    }

    @Override
    public String getName() {
        return "output-port?";
    }
}
