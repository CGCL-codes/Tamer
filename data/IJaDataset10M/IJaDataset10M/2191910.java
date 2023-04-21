package as.ide.core.dom.statement;

import as.ide.core.dom.Arguments;
import as.ide.core.dom.Statement;
import as.ide.core.dom.tool.BlockPosition;

public class SuperStatement extends Statement {

    public SuperStatement(BlockPosition pos) {
        super(pos);
        isConstructor = false;
    }

    private boolean isConstructor;

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean b) {
        isConstructor = b;
    }

    public void addArguments(Arguments args) {
    }
}
