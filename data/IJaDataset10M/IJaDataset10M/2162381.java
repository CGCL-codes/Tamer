package org.jmlspecs.jml6.boogie.ast;

import java.util.List;

/**
 * Represents the most general type of scope.
 */
public interface IBoogieScope {

    public BoogieProgram getProgramScope();

    public BoogieProcedure getProcedureScope();

    public List<BoogieStatement> getStatements();

    public BoogieVariableDeclaration lookupVariable(String name);

    public BoogieTypeDeclaration lookupType(String name);

    public BoogieProcedure lookupProcedure(String name);

    public BoogieFunctionDeclaration lookupFunction(String name);

    public void addVariable(BoogieVariableDeclaration decl);

    public void addType(BoogieTypeDeclaration type);

    public void addFunction(BoogieFunctionDeclaration function);

    IBoogieBlockScope getBlockScope();
}
