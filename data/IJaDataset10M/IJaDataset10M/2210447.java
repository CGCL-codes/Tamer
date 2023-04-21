package org.zamia.instgraph;

import org.zamia.SourceLocation;
import org.zamia.ZamiaException;
import org.zamia.instgraph.IGItemAccess.AccessType;
import org.zamia.instgraph.interpreter.IGInterpreterCode;
import org.zamia.instgraph.interpreter.IGJumpNCStmt;
import org.zamia.instgraph.interpreter.IGJumpStmt;
import org.zamia.instgraph.interpreter.IGLabel;
import org.zamia.util.HashSetArray;
import org.zamia.zdb.ZDB;

/**
 * 
 * @author Guenter Bartsch
 * 
 */
@SuppressWarnings("serial")
public class IGSequentialNext extends IGSequentialStatement {

    private IGOperation fCond;

    private String fNextLabel;

    public IGSequentialNext(String aNextLabel, IGOperation aCond, String aLabel, SourceLocation aSrc, ZDB aZDB) {
        super(aLabel, aSrc, aZDB);
        fCond = aCond;
        fNextLabel = aNextLabel;
    }

    @Override
    public void computeAccessedItems(IGItem aFilterItem, AccessType aFilterType, int aDepth, HashSetArray<IGItemAccess> aAccessedItems) {
        if (fCond != null) {
            fCond.computeAccessedItems(false, aFilterItem, aFilterType, aDepth, aAccessedItems);
        }
    }

    @Override
    public void generateCode(IGInterpreterCode aCode) throws ZamiaException {
        IGLabel l = null;
        if (fNextLabel == null) {
            l = aCode.getLoopExitLabel();
        } else {
            l = aCode.getLoopExitLabel(fNextLabel);
        }
        if (l == null) {
            throw new ZamiaException("Next statement: label not found.", computeSourceLocation());
        }
        IGLabel elseLabel = new IGLabel();
        if (fCond != null) {
            fCond.generateCode(true, aCode);
            aCode.add(new IGJumpNCStmt(elseLabel, computeSourceLocation(), getZDB()));
        }
        aCode.add(new IGJumpStmt(l, computeSourceLocation(), getZDB()));
        aCode.defineLabel(elseLabel);
    }

    @Override
    public IGItem getChild(int aIdx) {
        return fCond;
    }

    @Override
    public int getNumChildren() {
        return 1;
    }

    @Override
    public String toString() {
        return "IGSequentialNext(cond=" + fCond + ", label=" + fNextLabel + ")";
    }
}
