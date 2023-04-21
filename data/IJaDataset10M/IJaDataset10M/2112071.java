package su.msu.cs.lvk.xml2pixy.transform.astvisitor;

import su.msu.cs.lvk.xml2pixy.transform.SymbolTable;

/**
 * @see AddVisitor
 */
public class DivVisitor extends AddVisitor {

    public DivVisitor(SymbolTable symbolTable) {
        super(symbolTable);
    }

    public DivVisitor() {
        super();
    }
}
