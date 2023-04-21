package org.jmlspecs.jml4.rac;

import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.jmlspecs.jml4.ast.JmlAllocationExpression;
import org.jmlspecs.jml4.ast.JmlArrayQualifiedTypeReference;
import org.jmlspecs.jml4.ast.JmlAssertOrAssumeStatement;
import org.jmlspecs.jml4.ast.JmlAssertStatement;
import org.jmlspecs.jml4.ast.JmlAssignableClause;
import org.jmlspecs.jml4.ast.JmlAssignment;
import org.jmlspecs.jml4.ast.JmlAssumeStatement;
import org.jmlspecs.jml4.ast.JmlBooleanQuantifier;
import org.jmlspecs.jml4.ast.JmlCastExpression;
import org.jmlspecs.jml4.ast.JmlCastExpressionWithoutType;
import org.jmlspecs.jml4.ast.JmlClause;
import org.jmlspecs.jml4.ast.JmlClinit;
import org.jmlspecs.jml4.ast.JmlConditionalExpression;
import org.jmlspecs.jml4.ast.JmlConstraintClause;
import org.jmlspecs.jml4.ast.JmlConstructorDeclaration;
import org.jmlspecs.jml4.ast.JmlDoStatement;
import org.jmlspecs.jml4.ast.JmlElemtypeExpression;
import org.jmlspecs.jml4.ast.JmlEnsuresClause;
import org.jmlspecs.jml4.ast.JmlExplicitConstructorCall;
import org.jmlspecs.jml4.ast.JmlFieldDeclaration;
import org.jmlspecs.jml4.ast.JmlForStatement;
import org.jmlspecs.jml4.ast.JmlFreshExpression;
import org.jmlspecs.jml4.ast.JmlGroupName;
import org.jmlspecs.jml4.ast.JmlInitiallyClause;
import org.jmlspecs.jml4.ast.JmlInvariantForType;
import org.jmlspecs.jml4.ast.JmlLocalDeclaration;
import org.jmlspecs.jml4.ast.JmlLoopAnnotations;
import org.jmlspecs.jml4.ast.JmlLoopInvariant;
import org.jmlspecs.jml4.ast.JmlLoopVariant;
import org.jmlspecs.jml4.ast.JmlMapsIntoClause;
import org.jmlspecs.jml4.ast.JmlMapsMemberRefExpr;
import org.jmlspecs.jml4.ast.JmlMemberFieldRef;
import org.jmlspecs.jml4.ast.JmlMessageSend;
import org.jmlspecs.jml4.ast.JmlMethodDeclaration;
import org.jmlspecs.jml4.ast.JmlMethodSpecification;
import org.jmlspecs.jml4.ast.JmlModifier;
import org.jmlspecs.jml4.ast.JmlName;
import org.jmlspecs.jml4.ast.JmlNumericQuantifier;
import org.jmlspecs.jml4.ast.JmlOldExpression;
import org.jmlspecs.jml4.ast.JmlQuantifiedExpression;
import org.jmlspecs.jml4.ast.JmlRepresentsClause;
import org.jmlspecs.jml4.ast.JmlRequiresClause;
import org.jmlspecs.jml4.ast.JmlResultReference;
import org.jmlspecs.jml4.ast.JmlReturnStatement;
import org.jmlspecs.jml4.ast.JmlSetComprehension;
import org.jmlspecs.jml4.ast.JmlSetStatement;
import org.jmlspecs.jml4.ast.JmlSignalsClause;
import org.jmlspecs.jml4.ast.JmlSignalsOnlyClause;
import org.jmlspecs.jml4.ast.JmlSpecCase;
import org.jmlspecs.jml4.ast.JmlSpecCaseBlock;
import org.jmlspecs.jml4.ast.JmlSpecCaseBody;
import org.jmlspecs.jml4.ast.JmlSpecCaseHeader;
import org.jmlspecs.jml4.ast.JmlSpecCaseRestAsClauseSeq;
import org.jmlspecs.jml4.ast.JmlStoreRefExpression;
import org.jmlspecs.jml4.ast.JmlSubtypeExpression;
import org.jmlspecs.jml4.ast.JmlTypeBodyDeclaration;
import org.jmlspecs.jml4.ast.JmlTypeDeclaration;
import org.jmlspecs.jml4.ast.JmlTypeExpression;
import org.jmlspecs.jml4.ast.JmlTypeofExpression;
import org.jmlspecs.jml4.ast.JmlUnaryExpression;
import org.jmlspecs.jml4.ast.JmlWhileStatement;

/**
* An AST Visitor interface for visiting every Jml-type node. 
* 
* @author Amritam Sarcar
*/
@SuppressWarnings("deprecation")
public interface JmlAstVisitor extends JavaAstVisitor {

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference, BlockScope) ArrayQualifiedTypeReference
	 */
    public boolean visit(JmlArrayQualifiedTypeReference arrayQualifiedTypeReference, BlockScope scope);

    /**
	 * @see #visit(JmlArrayQualifiedTypeReference, BlockScope) JmlArrayQualifiedTypeReference
	 */
    public void endVisit(JmlArrayQualifiedTypeReference arrayQualifiedTypeReference, BlockScope scope);

    /**
	 * @see #visit(JmlArrayQualifiedTypeReference, BlockScope) JmlArrayQualifiedTypeReference
	 */
    public boolean visit(JmlArrayQualifiedTypeReference arrayQualifiedTypeReference, ClassScope scope);

    /**
	 * @see #visit(JmlArrayQualifiedTypeReference, BlockScope) JmlArrayQualifiedTypeReference
	 */
    public void endVisit(JmlArrayQualifiedTypeReference arrayQualifiedTypeReference, ClassScope scope);

    /**
	 * Inside an annotation, an assert statement is JML assert statement. An 
	 * assert statements tells JML to check that the specified predicate is true 
	 * at the given point in the program. Examples include:
	 * <pre>
	 * //@ assert 0 < x;
	 * //@ assert false;
	 * </pre>
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.AssertStatement, BlockScope) AssertStatement
	 */
    public boolean visit(JmlAssertStatement assertStatement, BlockScope scope);

    /**
	 * @see #visit(JmlAssertStatement, BlockScope) JmlAssertStatement
	 */
    public void endVisit(JmlAssertStatement assertStatement, BlockScope scope);

    /**
	 * Statements which are assumed to be true (in static analysis tools). For 
	 * RAC, they are checked the same way as in {@link #visit(JmlAssertStatement, BlockScope) JmlAssertStatement}. Examples
	 * include:
	 * <pre>
	 * //@ assume x == 3;
	 * //@ assume 0 < x;
	 * </pre>
	 * @see #visit(JmlAssertStatement, BlockScope) JmlAssertStatement 
	 */
    public boolean visit(JmlAssumeStatement assumeStatement, BlockScope scope);

    /**
	 * @see #visit(JmlAssumeStatement, BlockScope) JmlAssumeStatement
	 */
    public void endVisit(JmlAssumeStatement assumeStatement, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.Assignment, BlockScope) Assignment
	 */
    public boolean visit(JmlAssignment assignment, BlockScope scope);

    /**
	 * @see #visit(JmlAssignment, BlockScope) JmlAssignment
	 */
    public void endVisit(JmlAssignment assignment, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.CastExpression, BlockScope) CastExpression
	 */
    public boolean visit(JmlCastExpression castExpression, BlockScope scope);

    /**
	 * @see #visit(JmlCastExpression, BlockScope) JmlCastExpression
	 */
    public void endVisit(JmlCastExpression castExpression, BlockScope scope);

    public boolean visit(JmlCastExpressionWithoutType castExpression, BlockScope scope);

    public void endVisit(JmlCastExpressionWithoutType castExpression, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration, ClassScope) ConstructorDeclaration 
	 */
    public boolean visit(JmlConstructorDeclaration constructorDeclaration, ClassScope scope);

    /**
	 * @see #visit(JmlConstructorDeclaration, ClassScope) JmlConstructorDeclaration  
	 */
    public void endVisit(JmlConstructorDeclaration constructorDeclaration, ClassScope scope);

    /**
	 * An ensures clause specifies a normal postcondition, i.e., a property that
	 * is guaranteed to hold at the end of the method (or constructor) invocation 
	 * in the case that this method (or constructor) invocation returns without 
	 * throwing an exception. Examples include:
	 * <pre>
	 * //@ ensures x == 5;
	 * //@ ensures x > 0 && x != 2;
	 * </pre> 
	 */
    public boolean visit(JmlEnsuresClause ensuresClause, BlockScope scope);

    /**
	 * @see #visit(JmlEnsuresClause, BlockScope) JmlEnsuresClause  
	 */
    public void endVisit(JmlEnsuresClause ensuresClause, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.FieldDeclaration, MethodScope) FieldDeclaration
	 */
    public boolean visit(JmlFieldDeclaration fieldDeclaration, MethodScope scope);

    /**
	 * @see #visit(JmlFieldDeclaration, MethodScope) JmlFieldDeclaration
	 */
    public void endVisit(JmlFieldDeclaration fieldDeclaration, MethodScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.LocalDeclaration, BlockScope) LocalDeclaration
	 */
    public boolean visit(JmlLocalDeclaration localDeclaration, BlockScope scope);

    /**
	 * @see #visit(JmlLocalDeclaration, BlockScope) JmlLocalDeclaration
	 */
    public void endVisit(JmlLocalDeclaration localDeclaration, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.MessageSend, BlockScope) MessageSend
	 */
    public boolean visit(JmlMessageSend messageSend, BlockScope scope);

    /**
	 * @see #visit(JmlMessageSend, BlockScope) JmlMessageSend
	 */
    public void endVisit(JmlMessageSend messageSend, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, ClassScope) MethodDeclaration 
	 */
    public boolean visit(JmlMethodDeclaration methodDeclaration, ClassScope scope);

    /**
	 * @see #visit(JmlMethodDeclaration, ClassScope) JmlMethodDeclaration
	 */
    public void endVisit(JmlMethodDeclaration methodDeclaration, ClassScope scope);

    /**
	 * Specifies a precondition of method or constructor. Examples include:
	 * <pre>
	 * //@ requires x > 0;
	 * //@ requires x == 0 || x == -1;
	 * </pre>
	 */
    public boolean visit(JmlRequiresClause requiresClause, BlockScope scope);

    /**
	 * @see #visit(JmlRequiresClause, BlockScope) JmlRequiresClause 
	 */
    public void endVisit(JmlRequiresClause requiresClause, BlockScope scope);

    /**
	 * The primary <b>\result</b> can only be used in non-void method. Its value 
	 * is the value returned by the method. Its type is the return type of the 
	 * method. Examples include:
	 * <pre>
	 * \result <= x
	 * \result == x
	 * </pre> 
	 */
    public boolean visit(JmlResultReference resultReference, BlockScope scope);

    /**
	 * @see #visit(JmlResultReference, BlockScope) JmlResultReference
	 */
    public void endVisit(JmlResultReference resultReference, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.ReturnStatement, BlockScope) ReturnStatement
	 */
    public boolean visit(JmlReturnStatement returnStatement, BlockScope scope);

    /**
	 * @see #visit(JmlReturnStatement, BlockScope) JmlReturnStatement
	 */
    public void endVisit(JmlReturnStatement returnStatement, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.WhileStatement, BlockScope) WhileStatement
	 */
    public boolean visit(JmlWhileStatement whileStatement, BlockScope scope);

    /**
	 * @see #visit(JmlWhileStatement, BlockScope) JmlWhileStatement
	 */
    public void endVisit(JmlWhileStatement whileStatement, BlockScope scope);

    public boolean visit(JmlLoopAnnotations jmlLoopAnnotations, BlockScope scope);

    public void endVisit(JmlLoopAnnotations jmlLoopAnnotations, BlockScope scope);

    /**
	 * Is used to help prove partial correctness of a loop statement. The loop 
	 * invariant holds at the beginning of each iteration of the loop. Informally,
	 * <pre>
	 * //@ maintaining J ;
	 * while (B) { S }
	 * </pre> 
	 * Examples include:
	 * <pre>
	 * //@ maintaining x > 0;
	 * while (x < 5) { x++; }
	 * </pre>
	 * In this example, <em> //@ maintaining x > 0; </em> is a JmlLoopInvariant.
	 */
    public boolean visit(JmlLoopInvariant jmlLoopInvariant, BlockScope scope);

    /**
	 * @see #visit(JmlLoopInvariant, BlockScope) JmlLoopInvariant 
	 */
    public void endVisit(JmlLoopInvariant jmlLoopInvariant, BlockScope scope);

    /**
	 * Is used to help prove termination of a loop statement. It specifies an
	 * expression of type long or int that must be no less than 0 when the loop 
	 * is executing, and must decrease by at least one (1) each time around the 
	 * loop. Informally,
	 * <pre>
	 * //@ decreasing E;
	 * while (B) { S }
	 * </pre>
	 * Examples include:
	 * <pre>
	 * //@ decreasing x;
	 * while (x != 0) { x--; }
	 * </pre> 
	 * In this example, <em> //@ decreasing x; </em> is a JmlLoopVariant.
	 */
    public boolean visit(JmlLoopVariant jmlLoopVariant, BlockScope scope);

    /**
	 * @see #visit(JmlLoopVariant, BlockScope) JmlLoopVariant 
	 */
    public void endVisit(JmlLoopVariant jmlLoopVariant, BlockScope scope);

    /**
	 * Refers to the value that the expression had in the pre-state of a method.
	 * Examples include:
	 * <pre>
	 * \old(x) != 0;
	 * \pre(x) > \result;
	 * </pre>
	 * In this example, <em> \old(x) </em> and <em> \pre(x) </em> are JmlOldExpression.
	 */
    public boolean visit(JmlOldExpression jmlOldExpression, BlockScope scope);

    /**
	 * @see #visit(JmlOldExpression, BlockScope) JmlOldExpression 
	 */
    public void endVisit(JmlOldExpression jmlOldExpression, BlockScope scope);

    /**
	 * @see JmlClause JmlClause
	 */
    public boolean visit(JmlClause jmlClause, BlockScope scope);

    /**
	 * @see #visit(JmlClause, BlockScope) JmlClause 
	 */
    public void endVisit(JmlClause jmlClause, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.TypeDeclaration, CompilationUnitScope) TypeDeclaration
	 */
    public boolean visit(JmlTypeDeclaration typeDecle, CompilationUnitScope scope);

    /**
	 * @see #visit(JmlTypeDeclaration, CompilationUnitScope) JmlTypeDeclaration
	 */
    public void endVisit(JmlTypeDeclaration typeDecle, CompilationUnitScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.AllocationExpression, BlockScope) AllocationExpression
	 */
    public boolean visit(JmlAllocationExpression allocationExpression, BlockScope scope);

    /**
	 * @see #visit(JmlAllocationExpression, BlockScope) JmlAllocationExpression
	 */
    public void endVisit(JmlAllocationExpression allocationExpression, BlockScope scope);

    /**
	 * @see #visit(JmlAssertStatement, BlockScope) JmlAssertStatement
	 * @see #visit(JmlAssumeStatement, BlockScope) JmlAssumeStatement
	 */
    public boolean visit(JmlAssertOrAssumeStatement assertOrAssumeStatement, BlockScope scope);

    /**
	 * @see #visit(JmlAssertOrAssumeStatement, BlockScope) JmlAssertOrAssumeStatement
	 */
    public void endVisit(JmlAssertOrAssumeStatement assertOrAssumeStatement, BlockScope scope);

    /**
	 * Gives a frame axiom for a specification. It says that, from the client�s
	 * point of view, only the locations named, and locations in the data groups 
	 * associated with these locations, can be assigned to during the execution 
	 * of the method. Examples include:
	 * <pre>
	 * //@ assignable \nothing;
	 * //@ assignable \everything;
	 * </pre>
	 */
    public boolean visit(JmlAssignableClause jmlAssignableClause, BlockScope scope);

    /**
	 * @see #visit(JmlAssignableClause, BlockScope) JmlAssignableClause
	 */
    public void endVisit(JmlAssignableClause jmlAssignableClause, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.Clinit, ClassScope) Clinit
	 */
    public boolean visit(JmlClinit clinit, ClassScope scope);

    /**
	 * @see #visit(JmlClinit, ClassScope) JmlClinit
	 */
    public void endVisit(JmlClinit clinit, ClassScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.ConditionalExpression, BlockScope) ConditionalExpression
	 */
    public boolean visit(JmlConditionalExpression conditionalExpression, BlockScope scope);

    /**
	 * @see #visit(JmlConditionalExpression, BlockScope) JmlConditionalExpression
	 */
    public void endVisit(JmlConditionalExpression conditionalExpression, BlockScope scope);

    /**
	 * Are relationships that should hold for the combination of each visible 
	 * state and any visible state that occurs later in the program�s execution.
	 * It is used to constrain the way that values change over time. Examples
	 * include:
	 * <pre>
	 * //@ constraint a == \old(a);
	 * //@ constraint b.length == \old(b.length);
	 * </pre> 
	 */
    public boolean visit(JmlConstraintClause jmlConstraintClause, BlockScope scope);

    /**
	 * @see #visit(JmlConstraintClause, BlockScope) JmlConstraintClause
	 */
    public void endVisit(JmlConstraintClause jmlConstraintClause, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.DoStatement, BlockScope) DoStatement
	 */
    public boolean visit(JmlDoStatement doStatement, BlockScope scope);

    /**
	 * @see #visit(JmlDoStatement, BlockScope) JmlDoStatement
	 */
    public void endVisit(JmlDoStatement doStatement, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.ForStatement, BlockScope) ForStatement
	 */
    public boolean visit(JmlForStatement forStatement, BlockScope scope);

    /**
	 * @see #visit(JmlForStatement, BlockScope) JmlForStatement
	 */
    public void endVisit(JmlForStatement forStatement, BlockScope scope);

    /**
	 * Is used to specify the initial state of model fields. Examples include:
	 * <pre>
	 * //@ public model int x;
	 * //@ public initially x == 0;
	 * </pre>
	 * In this example, <em> initially x == 0; <em> is a JmlInitiallyClause.  
	 */
    public boolean visit(JmlInitiallyClause jmlInitiallyClause, CompilationUnitScope scope);

    /**
	 * @see #visit(JmlInitiallyClause, CompilationUnitScope) JmlInitiallyClause
	 */
    public void endVisit(JmlInitiallyClause jmlInitiallyClause, CompilationUnitScope scope);

    /**
	 * Properties that have to hold in all visible states. Examples include:
	 * <pre>
	 * //@ invariant b != null && b.length == 6;
	 * </pre>
	 */
    public boolean visit(JmlInvariantForType jmlInvariantForType, CompilationUnitScope scope);

    /**
	 * @see #visit(JmlInvariantForType, CompilationUnitScope) JmlInvariantForType
	 */
    public void endVisit(JmlInvariantForType jmlInvariantForType, CompilationUnitScope scope);

    /**
	 * The first form of represents clauses (with <- or =) is called a functional 
	 * abstraction. This form defines the value of the store-ref-expression in a 
	 * visible state as the value of the spec-expression that follows the 
	 * l-arrow-or-eq. The second form (with \such_that) is called a relational 
	 * abstraction. This form constrains the value of the store-ref-expression 
	 * in a visible state to satisfy the given predicate. Examples include:
	 * <pre>
	 * protected int x_;
	 * //@       in x;
	 * //@ protected represents x <- x_;
	 * </pre>
	 * In this example, representx x <- x_; is a JmlRepresentsClause. 
	 */
    public boolean visit(JmlRepresentsClause representsClause, CompilationUnitScope scope);

    /**
	 * @see #visit(JmlRepresentsClause, CompilationUnitScope) JmlRepresentsClause
	 */
    public void endVisit(JmlRepresentsClause representsClause, CompilationUnitScope scope);

    /**
	 * Is the equivalent of an assignment statement but is within an annotation.
	 * It is used to assign a value to a ghost variable or to a ghost field.
	 * Examples include:
	 * <pre>
	 * //@ set i = 0;
	 * //@ set collection.elementType = \type(int);
	 * </pre> 
	 */
    public boolean visit(JmlSetStatement jmlSetStatement, BlockScope scope);

    /**
	 * @see #visit(JmlSetStatement, BlockScope) JmlSetStatement
	 */
    public boolean endVisit(JmlSetStatement jmlSetStatement, BlockScope scope);

    /**
	 * Specifies the exceptional or abnormal postcondition, i.e., the property 
	 * that is guaranteed to hold at the end of a method (or constructor) 
	 * invocation when this method (or constructor) invocation terminates 
	 * abruptly by throwing a given exception. Examples include:
	 * <pre>
	 * //@ signals (Exception) x < 0;
	 * </pre> 
	 */
    public boolean visit(JmlSignalsClause jmlSignalsClause, BlockScope scope);

    /**
	 * @see #visit(JmlSignalsClause, BlockScope) JmlSignalsClause
	 */
    public void endVisit(JmlSignalsClause jmlSignalsClause, BlockScope scope);

    /**
	 * Is an abbreviation for a signals-clause that specifies what exceptions 
	 * may be thrown by a method, and thus, implicitly, what exceptions may not 
	 * be thrown. Examples include:
	 * <pre>
	 * signals_only NullPointerException;
	 * </pre> 
	 */
    public boolean visit(JmlSignalsOnlyClause jmlSignalsOnlyClause, BlockScope scope);

    /**
	 * @see #visit(JmlSignalsOnlyClause, BlockScope) JmlSignalsOnlyClause
	 */
    public void endVisit(JmlSignalsOnlyClause jmlSignalsOnlyClause, BlockScope scope);

    public boolean visit(JmlTypeBodyDeclaration jmlTypeDecle, BlockScope scope);

    public void endVisit(JmlTypeBodyDeclaration jmlTypeDecle, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.UnaryExpression, BlockScope) UnaryExpression
	 */
    public boolean visit(JmlUnaryExpression unaryExpression, BlockScope scope);

    /**
	 * @see #visit(JmlUnaryExpression, BlockScope) JmlUnaryExpression
	 */
    public void endVisit(JmlUnaryExpression unaryExpression, BlockScope scope);

    public boolean visit(JmlSpecCaseBlock jmlSpecCaseBlock, BlockScope scope);

    public void endVisit(JmlSpecCaseBlock jmlSpecCaseBlock, BlockScope scope);

    public boolean visit(JmlSpecCaseBody jmlSpecCaseBody, BlockScope scope);

    public void endVisit(JmlSpecCaseBody jmlSpecCaseBody, BlockScope scope);

    public boolean visit(JmlSpecCaseRestAsClauseSeq jmlSpecCaseRestAsClauseSeq, BlockScope scope);

    public void endVisit(JmlSpecCaseRestAsClauseSeq jmlSpecCaseRestAsClauseSeq, BlockScope scope);

    /**
	 * Asserts that objects were freshly allocated. Examples include,
	 * <pre>
	 * \fresh(x,y)
	 * </pre>
	 * In this example, <em> \fresh(x,y) </em> asserts that x and y are not null 
	 * and that the objects bound to these identifiers were not allocated in the 
	 * pre-state. 
	 */
    public boolean visit(JmlFreshExpression jmlFreshExpression, BlockScope scope);

    /**
	 * @see #visit(JmlFreshExpression, BlockScope) JmlFreshExpression
	 */
    public void endVisit(JmlFreshExpression jmlFreshExpression, BlockScope scope);

    public boolean visit(JmlMapsIntoClause jmlMapsIntoClause, BlockScope scope);

    public void endVisit(JmlMapsIntoClause jmlMapsIntoClause, BlockScope scope);

    public boolean visit(JmlMapsMemberRefExpr jmlMapsMemberRefExpr, BlockScope scope);

    public void endVisit(JmlMapsMemberRefExpr jmlMapsMemberRefExpr, BlockScope scope);

    public boolean visit(JmlMemberFieldRef jmlMemberFieldRef, BlockScope scope);

    public void endVisit(JmlMemberFieldRef jmlMemberFieldRef, BlockScope scope);

    public boolean visit(JmlMethodSpecification jmlMethodSpecification, BlockScope scope);

    public void endVisit(JmlMethodSpecification jmlMethodSpecification, BlockScope scope);

    public boolean visit(JmlName jmlName, BlockScope scope);

    public void endVisit(JmlName jmlName, BlockScope scope);

    /**
	 * It is used to succinctly define sets. Examples include:
	 * <pre>
	 * new JMLObjectSet {Integer i | myIntSet.has(i) &&
       i != null && 0 <= i.intValue() && i.intValue() <= 10 }
	 * </pre>
	 * In the above example, <em> JMLObjectSet </em> is the subset of non-null 
	 * Integer objects found in the set myIntSet whose values are between 0 and 
	 * 10, inclusive.
	 */
    public boolean visit(JmlSetComprehension jmlSetComprehension, BlockScope scope);

    /**
	 * @see #visit(JmlSetComprehension, BlockScope) JmlSetComprehension
	 */
    public void endVisit(JmlSetComprehension jmlSetComprehension, BlockScope scope);

    public boolean visit(JmlSpecCase jmlSpecCase, BlockScope scope);

    public void endVisit(JmlSpecCase jmlSpecCase, BlockScope scope);

    /**
	 * Method specification for checking precondition of method or constructor.
	 * Examples include:
	 * <pre>
	 * //@ requires x > 0;
	 * //@ requires x != 1;
	 * </pre>
	 * @see #visit(JmlRequiresClause, BlockScope) JmlRequiresClause
	 */
    public boolean visit(JmlSpecCaseHeader jmlSpecCaseHeader, BlockScope scope);

    /**
	 * @see #visit(JmlSpecCaseHeader, BlockScope) JmlSpecCaseHeader
	 */
    public void endVisit(JmlSpecCaseHeader jmlSpecCaseHeader, BlockScope scope);

    public boolean visit(JmlStoreRefExpression jmlStoreRefExpression, BlockScope scope);

    public void endVisit(JmlStoreRefExpression jmlStoreRefExpression, BlockScope scope);

    public boolean visit(JmlSubtypeExpression jmlSubtypeExpression, BlockScope scope);

    public void endVisit(JmlSubtypeExpression jmlSubtypeExpression, BlockScope scope);

    public boolean visit(JmlBooleanQuantifier jmlBooleanQuantifier, BlockScope scope);

    public void endVisit(JmlBooleanQuantifier jmlBooleanQuantifier, BlockScope scope);

    public boolean visit(JmlGroupName jmlGroupName, BlockScope scope);

    public void endVisit(JmlGroupName jmlGroupName, BlockScope scope);

    /**
	 * In addition to the Java modifiers that can be legally attached to a class 
	 * or interface definition [Gosling-etal00], in JML one can use the following 
	 * modifiers.
	 * <pre>
	 * pure model
	 * spec_java_math spec_safe_math spec_bigint_math
	 * code_java_math code_safe_math code_bigint_math
	 * nullable_by_default
	 * </pre> 
	 */
    public boolean visit(JmlModifier jmlModifier, BlockScope scope);

    /**
	 * @see #visit(JmlModifier, BlockScope) JmlModifier
	 */
    public void endVisit(JmlModifier jmlModifier, BlockScope scope);

    public boolean visit(JmlNumericQuantifier jmlNumericQuantifier, BlockScope scope);

    public void endVisit(JmlNumericQuantifier jmlNumericQuantifier, BlockScope scope);

    public boolean visit(JmlTypeExpression expression, BlockScope scope);

    /**
	 * @see #visit(JmlTypeExpression, BlockScope) JmlTypeExpression
	 */
    public void endVisit(JmlTypeExpression expression, BlockScope scope);

    public boolean visit(JmlElemtypeExpression jmlElemtypeExpression, BlockScope scope);

    public void endVisit(JmlElemtypeExpression jmlElemtypeExpression, BlockScope scope);

    /**
	 * @see org.jmlspecs.jml4.rac.JavaAstVisitor#visit(org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall, BlockScope) ExplicitConstructorCall
	 */
    public boolean visit(JmlExplicitConstructorCall jmlExplicitConstructorCall, BlockScope scope);

    /**
	 * @see #visit(JmlExplicitConstructorCall, BlockScope) JmlExplicitConstructorCall
	 */
    public void endVisit(JmlExplicitConstructorCall jmlExplicitConstructorCall, BlockScope scope);

    public boolean visit(JmlTypeofExpression jmlTypeofExpression, BlockScope scope);

    public void endVisit(JmlTypeofExpression jmlTypeofExpression, BlockScope scope);

    public boolean visit(JmlMethodSpecification jmlMethodSpecification, ClassScope classScope);

    public void endVisit(JmlMethodSpecification jmlMethodSpecification, ClassScope classScope);

    public boolean visit(JmlQuantifiedExpression jmlQuantifiedExpression, BlockScope blockScope);

    public void endVisit(JmlQuantifiedExpression jmlQuantifiedExpression, BlockScope blockScope);
}
