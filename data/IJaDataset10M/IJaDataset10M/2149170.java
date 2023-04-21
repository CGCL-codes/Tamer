package nts.typo;

import nts.command.AnyIfPrim;

public class IfVModePrim extends AnyIfPrim {

    public IfVModePrim(String name) {
        super(name);
    }

    protected final boolean holds() {
        return TypoCommand.getBld().isVertical();
    }
}
