package org.oslo.ocl20.syntax.ast.contexts;

import uk.ac.kent.cs.kmf.patterns.Visitor;
import uk.ac.kent.cs.kmf.patterns.VisitActions;

public interface contextsVisitor extends Visitor {

    public java.lang.Object visit(ConstraintKindAS host, java.lang.Object data);

    public java.lang.Object visit(OperationAS host, java.lang.Object data);

    public java.lang.Object visit(ConstraintAS host, java.lang.Object data);

    public java.lang.Object visit(VariableDeclarationAS host, java.lang.Object data);

    public java.lang.Object visit(PropertyContextDeclAS host, java.lang.Object data);

    public java.lang.Object visit(PackageDeclarationAS host, java.lang.Object data);

    public java.lang.Object visit(OperationContextDeclAS host, java.lang.Object data);

    public java.lang.Object visit(ClassifierContextDeclAS host, java.lang.Object data);

    public java.lang.Object visit(ContextDeclarationAS host, java.lang.Object data);
}
