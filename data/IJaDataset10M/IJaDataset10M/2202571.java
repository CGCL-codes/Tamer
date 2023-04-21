package ng.ast;

import ng.ast.util.Fqn;

/**
 * @author John
 * 
 */
public interface CompilationUnit extends Node {

    void setPackage(Fqn packageName);

    void addImport(Import importStatement);

    void addClassDeclaration(ClassDeclaration classDeclaration);

    void addInterfaceDeclaration(InterfaceDeclaration interfaceDeclaration);

    void addStatement(Statement statement);
}
