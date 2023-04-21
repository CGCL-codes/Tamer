package com.newisys.verilog;

import java.util.Iterator;

/**
 * Represents a module path.
 * 
 * @author Trevor Robinson
 */
public interface VerilogModPath extends VerilogDeclObject {

    PathType getPathType();

    Polarity getPolarity();

    Polarity getDataPolarity();

    VerilogModule getModule();

    VerilogExpr getCondition();

    VerilogExpr getDelay();

    Iterator<VerilogPathTerm> getInTerms();

    Iterator<VerilogPathTerm> getOutTerms();

    Iterator<VerilogPathTerm> getDataInTerms();
}
