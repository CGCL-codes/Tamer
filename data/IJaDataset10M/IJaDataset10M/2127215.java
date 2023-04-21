package as.ide.core.dom.statement;

import as.ide.core.dom.Statement;
import as.ide.core.dom.StatementBlock;
import as.ide.core.dom.VariableDef;
import as.ide.core.dom.tool.BlockPosition;

public class CatchStatement extends Statement {

    private StatementBlock stmtBlk;

    public CatchStatement(BlockPosition pos) {
        super(pos);
    }

    public void setStatements(StatementBlock sb) {
        this.stmtBlk = sb;
    }

    public VariableDef getVariable(String vrbName, int line, int col) {
        if (stmtBlk == null) return null;
        return stmtBlk.getVariable(vrbName, line, col);
    }
}
