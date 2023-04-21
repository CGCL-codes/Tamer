package org.jmlspecs.jir.binding;

import java.util.ArrayList;
import org.sireum.util.Util;

public final class JirNaryOpBinding<Expression> extends JirOpBinding {

    protected JirNaryOp op;

    protected ArrayList<Expression> optionalArguments;

    public static final int DESCRIPTOR = 13;

    public JirNaryOpBinding() {
    }

    @Override
    public final int getDescriptor() {
        return JirNaryOpBinding.DESCRIPTOR;
    }

    public final JirNaryOp getOp() {
        return this.op;
    }

    public final ArrayList<Expression> getOptionalArguments() {
        return this.optionalArguments;
    }

    public final void setOp(final JirNaryOp op) {
        assert op != null;
        this.op = op;
    }

    public final void setOptionalArguments(final ArrayList<Expression> optionalArguments) {
        assert Util.nonNullElements(optionalArguments);
        this.optionalArguments = optionalArguments;
    }
}
