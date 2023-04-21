package tm.javaLang.analysis;

import java.util.Vector;
import tm.clc.analysis.*;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TypeNode;
import tm.interfaces.SourceCoords;
import tm.javaLang.ast.TyClass;
import tm.javaLang.parser.JavaParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;

/*******************************************************************************
Class: Java_CTSymbolTable

Overview:
This class represents the compile time symbol table for Java.
*******************************************************************************/
public class Java_CTSymbolTable extends CTSymbolTable implements JavaParserConstants {

    private SHProgram programScope;

    private LFlags lookupContext = new LFlags();

    /** Create a compile time symbol table for the current program */
    public Java_CTSymbolTable() {
        programScope = SHProgram.getProgramSH();
        currentScope = programScope;
    }

    /** In some languages signifies the entering of a scope without a declaration (e.g.
     * a block scope). All scopes in our implementation of Java have a declaration so
     * it is an error to call this method.
     */
    public void enterScope() {
        Assert.check("enterScope() should not be called without a declaration argument");
    }

    /** Revert to an pre-existing scope. Subsidiary files have to be compiled
     * at the top scope level (program scope), but their compilation may be
     * invoked in the middle of compiling another file, requiring a reversion
     * to the original scope after the secondary compilation is complete.
     */
    public void enterScope(ScopeHolder entry) {
        currentScope = entry;
    }

    public void createBlockScope(Declaration decl) {
        currentScope = ((SHCommon) currentScope).createBlockScope(decl);
        debugReport();
    }

    public void createFunctionScope(Declaration decl) {
        currentScope = ((SHCommon) currentScope).createFunctionScope(decl);
        debugReport();
    }

    public void createPackageScope(ScopedName name, SpecifierSet specSet, SourceCoords coords) {
        Declaration existingPackage = programScope.lookupPackage(name, new LFlags(Java_LFlags.PACKAGE)).getSingleMember();
        currentScope = (existingPackage != null) ? existingPackage.getScopeHolder() : ((SHCommon) currentScope).createPackageScope(name, specSet, coords);
        debugReport();
    }

    public void addImportDeclaration(Declaration decl, boolean onDemand) {
        ((SHCompilationUnit) currentScope).addImportDeclaration(decl, onDemand);
    }

    public void createCompilationUnitScope(Declaration decl) {
        Declaration existingCU = currentScope.lookUp(decl.getName(), new LFlags(Java_LFlags.COMPILATION_UNIT)).getSingleMember();
        currentScope = (existingCU != null) ? existingCU.getScopeHolder() : ((SHCommon) currentScope).createCompilationUnitScope(decl);
        debugReport();
    }

    public void createTypeScope(Declaration decl) {
        currentScope = ((SHCommon) currentScope).createTypeScope(decl);
        debugReport();
    }

    public void createLocalScope(Declaration decl) {
        currentScope = ((SHCommon) currentScope).createLocalScope(decl);
        debugReport();
    }

    public void exitScope() {
        while (currentScope instanceof SHLocal) currentScope = (SHCommon) currentScope.getEnclosingScope();
        currentScope = (SHCommon) currentScope.getEnclosingScope();
    }

    public void exitAllScopes() {
        currentScope = programScope;
    }

    public Java_ScopedName createFQName(ScopedName simpleName) {
        SHCommon baseScope = ((SHCommon) getCurrentScope()).getBaseScope();
        Java_ScopedName name = (Java_ScopedName) (baseScope.getOwnDeclaration().getName()).clone();
        name.append(simpleName);
        return name;
    }

    /** Build the <em>relative path</em> for this <code>Declaration</code>.
	 * from the indicated scope. The path is an array of integers whose
	 * length indicates how many classes away the declaration is.
	 * <br><strong>note:</strong> The integers represent through which related
         * class the path runs, as follows:
         * (1) a non-negative integer gives the number of the superclass in the list
         *      of the class's superclasses
         * (2) a -1 indicates the next outer class
         * Thus a path of [-1,0,2] would mean the declaration occurred in the
         * 2nd supertype of the 0'th supertype of the immediate outer type
         * of the type in which the starting scope occurs
	 * @param match a declaration known to be in the class hierarchy 
	 * @param scope the starting scope from which the path is to begin. If omitted,
         *           the default is the currentScope
         * @return the path or a null if the declaration's scope is not in the type hierarchy
	 */
    protected int[] getRelativePath(Declaration match) {
        return getRelativePath(match, (SHCommon) currentScope);
    }

    protected int[] getRelativePath(Declaration match, SHCommon scope) {
        Vector pathV = new Vector();
        int[] path = null;
        scope.buildRelativePath(match, pathV);
        path = new int[pathV.size()];
        for (int i = 0; i < pathV.size(); i++) path[i] = ((Integer) pathV.elementAt(i)).intValue();
        return path;
    }

    public DeclarationSet lookup(ScopedName name, LFlags flags) {
        DeclarationSet matches = null;
        try {
            matches = currentScope.lookup(name, flags);
        } catch (LookupException e) {
            System.err.println("Nested exception trace is");
            e.printStackTrace(System.err);
            Assert.check("Internal error: LookupException in Java");
        }
        return matches;
    }

    public Declaration lookupMemberField(TyAbstractClass type, ScopedName name) {
        SHType scope = (SHType) ((TyAbstractClassDeclared) type).getDeclaration().getDefinition();
        Declaration found = scope.lookUp(name, Java_LFlags.FIELD_VARIABLE_LF).getSingleMember();
        Assert.error(found != null, "Can't find " + name.getName());
        Assert.error(isAccessible(found), "Can't access " + name.getName());
        return found;
    }

    public DeclarationSet lookupMemberMethods(TyAbstractClassDeclared type, ScopedName name) {
        return lookupMemberMethods((SHType) type.getDeclaration().getDefinition(), name);
    }

    public DeclarationSet lookupMemberMethods(SHType typeSH, ScopedName name) {
        return typeSH.lookUp(name, Java_LFlags.METHOD_LF);
    }

    public boolean isAccessible(Declaration decl) {
        return ((SHCommon) currentScope).isAccessible(decl);
    }

    public String toString() {
        return "Java CT Symbol Table";
    }

    /** Look in symbol table for a class
     * @param className the name of the class to look for
     * @return The TyClass for the class.
     */
    public TyClass getTypeNodeForClass(ScopedName className) {
        LFlags classFlags = new LFlags(LFlags.CLASS);
        DeclarationSet declSet = lookup(className, classFlags);
        Declaration decl = declSet.getSingleMember();
        Assert.check(decl != null, "Could not find " + className.getName());
        TypeNode type = decl.getType();
        Assert.check(type instanceof TyClass);
        return (TyClass) type;
    }

    private void debugReport() {
        Debug.getInstance().msg(Debug.COMPILE, "Created " + currentScope.toString() + " in scope of " + (currentScope.getClassDeclaration() == null ? "no " : currentScope.getClassDeclaration().toString()) + " class");
    }

    public void dumpContents(String context, Debug d) {
        d.msg(Debug.COMPILE, "****" + toString() + ' ' + context + " ***********************");
        d.msg(Debug.COMPILE, toString());
        programScope.dumpContents("", d);
        d.msg(Debug.COMPILE, "----" + toString() + ' ' + context + " -----------------------");
    }

    public void dumpContents(Debug d) {
        dumpContents(" ", d);
    }
}
