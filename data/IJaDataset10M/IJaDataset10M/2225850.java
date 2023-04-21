package org.eclipse.epsilon.dom.eol.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.dom.eol.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.dom.eol.EolPackage
 * @generated
 */
public class EolAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static EolPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EolAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = EolPackage.eINSTANCE;
        }
    }

    /**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EolSwitch<Adapter> modelSwitch = new EolSwitch<Adapter>() {

        @Override
        public Adapter caseDomElement(DomElement object) {
            return createDomElementAdapter();
        }

        @Override
        public Adapter caseEolModule(EolModule object) {
            return createEolModuleAdapter();
        }

        @Override
        public Adapter caseNamedElement(NamedElement object) {
            return createNamedElementAdapter();
        }

        @Override
        public Adapter caseTypedElement(TypedElement object) {
            return createTypedElementAdapter();
        }

        @Override
        public Adapter caseModelDeclaration(ModelDeclaration object) {
            return createModelDeclarationAdapter();
        }

        @Override
        public Adapter caseOperationDeclaration(OperationDeclaration object) {
            return createOperationDeclarationAdapter();
        }

        @Override
        public Adapter caseVariableDeclaration(VariableDeclaration object) {
            return createVariableDeclarationAdapter();
        }

        @Override
        public Adapter caseScopeOwner(ScopeOwner object) {
            return createScopeOwnerAdapter();
        }

        @Override
        public Adapter caseParameterDeclaration(ParameterDeclaration object) {
            return createParameterDeclarationAdapter();
        }

        @Override
        public Adapter caseStatement(Statement object) {
            return createStatementAdapter();
        }

        @Override
        public Adapter caseReturnStatement(ReturnStatement object) {
            return createReturnStatementAdapter();
        }

        @Override
        public Adapter caseDeleteStatement(DeleteStatement object) {
            return createDeleteStatementAdapter();
        }

        @Override
        public Adapter caseThrowStatement(ThrowStatement object) {
            return createThrowStatementAdapter();
        }

        @Override
        public Adapter caseBreakStatement(BreakStatement object) {
            return createBreakStatementAdapter();
        }

        @Override
        public Adapter caseBreakAllStatement(BreakAllStatement object) {
            return createBreakAllStatementAdapter();
        }

        @Override
        public Adapter caseContinueStatement(ContinueStatement object) {
            return createContinueStatementAdapter();
        }

        @Override
        public Adapter caseTransactionStatement(TransactionStatement object) {
            return createTransactionStatementAdapter();
        }

        @Override
        public Adapter caseAbortStatement(AbortStatement object) {
            return createAbortStatementAdapter();
        }

        @Override
        public Adapter caseStatementBlock(StatementBlock object) {
            return createStatementBlockAdapter();
        }

        @Override
        public Adapter caseConditionalStatement(ConditionalStatement object) {
            return createConditionalStatementAdapter();
        }

        @Override
        public Adapter caseIfStatement(IfStatement object) {
            return createIfStatementAdapter();
        }

        @Override
        public Adapter caseWhileStatement(WhileStatement object) {
            return createWhileStatementAdapter();
        }

        @Override
        public Adapter caseForStatement(ForStatement object) {
            return createForStatementAdapter();
        }

        @Override
        public Adapter caseAssignStatement(AssignStatement object) {
            return createAssignStatementAdapter();
        }

        @Override
        public Adapter caseSpecialAssignStatement(SpecialAssignStatement object) {
            return createSpecialAssignStatementAdapter();
        }

        @Override
        public Adapter caseExpressionStatement(ExpressionStatement object) {
            return createExpressionStatementAdapter();
        }

        @Override
        public Adapter caseImportStatement(ImportStatement object) {
            return createImportStatementAdapter();
        }

        @Override
        public Adapter caseExpression(Expression object) {
            return createExpressionAdapter();
        }

        @Override
        public Adapter caseOperatorExpression(OperatorExpression object) {
            return createOperatorExpressionAdapter();
        }

        @Override
        public Adapter caseBinaryOperatorExpression(BinaryOperatorExpression object) {
            return createBinaryOperatorExpressionAdapter();
        }

        @Override
        public Adapter caseUnaryOperatorExpression(UnaryOperatorExpression object) {
            return createUnaryOperatorExpressionAdapter();
        }

        @Override
        public Adapter caseFeatureCallExpression(FeatureCallExpression object) {
            return createFeatureCallExpressionAdapter();
        }

        @Override
        public Adapter casePropertyCallExpression(PropertyCallExpression object) {
            return createPropertyCallExpressionAdapter();
        }

        @Override
        public Adapter caseOperationCallExpression(OperationCallExpression object) {
            return createOperationCallExpressionAdapter();
        }

        @Override
        public Adapter caseSimpleOperationCallExpression(SimpleOperationCallExpression object) {
            return createSimpleOperationCallExpressionAdapter();
        }

        @Override
        public Adapter caseDeclarativeOperationCallExpression(DeclarativeOperationCallExpression object) {
            return createDeclarativeOperationCallExpressionAdapter();
        }

        @Override
        public Adapter caseCollectionExpression(CollectionExpression object) {
            return createCollectionExpressionAdapter();
        }

        @Override
        public Adapter caseLiteralCollectionExpression(LiteralCollectionExpression object) {
            return createLiteralCollectionExpressionAdapter();
        }

        @Override
        public Adapter caseRangeCollectionExpression(RangeCollectionExpression object) {
            return createRangeCollectionExpressionAdapter();
        }

        @Override
        public Adapter caseIntegerLiteralExpression(IntegerLiteralExpression object) {
            return createIntegerLiteralExpressionAdapter();
        }

        @Override
        public Adapter caseStringLiteralExpression(StringLiteralExpression object) {
            return createStringLiteralExpressionAdapter();
        }

        @Override
        public Adapter caseBooleanLiteralExpression(BooleanLiteralExpression object) {
            return createBooleanLiteralExpressionAdapter();
        }

        @Override
        public Adapter caseRealLiteralExpression(RealLiteralExpression object) {
            return createRealLiteralExpressionAdapter();
        }

        @Override
        public Adapter caseEnumerationLiteralExpression(EnumerationLiteralExpression object) {
            return createEnumerationLiteralExpressionAdapter();
        }

        @Override
        public Adapter caseNewExpression(NewExpression object) {
            return createNewExpressionAdapter();
        }

        @Override
        public Adapter caseVariableReferenceExpression(VariableReferenceExpression object) {
            return createVariableReferenceExpressionAdapter();
        }

        @Override
        public Adapter caseType(Type object) {
            return createTypeAdapter();
        }

        @Override
        public Adapter caseNamedType(NamedType object) {
            return createNamedTypeAdapter();
        }

        @Override
        public Adapter caseSimpleType(SimpleType object) {
            return createSimpleTypeAdapter();
        }

        @Override
        public Adapter caseModelElementType(ModelElementType object) {
            return createModelElementTypeAdapter();
        }

        @Override
        public Adapter caseCollectionType(CollectionType object) {
            return createCollectionTypeAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.DomElement <em>Dom Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.DomElement
	 * @generated
	 */
    public Adapter createDomElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.EolModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.EolModule
	 * @generated
	 */
    public Adapter createEolModuleAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.NamedElement
	 * @generated
	 */
    public Adapter createNamedElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.TypedElement
	 * @generated
	 */
    public Adapter createTypedElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ModelDeclaration <em>Model Declaration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ModelDeclaration
	 * @generated
	 */
    public Adapter createModelDeclarationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.OperationDeclaration <em>Operation Declaration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.OperationDeclaration
	 * @generated
	 */
    public Adapter createOperationDeclarationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.VariableDeclaration <em>Variable Declaration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.VariableDeclaration
	 * @generated
	 */
    public Adapter createVariableDeclarationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ScopeOwner <em>Scope Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ScopeOwner
	 * @generated
	 */
    public Adapter createScopeOwnerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ParameterDeclaration <em>Parameter Declaration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ParameterDeclaration
	 * @generated
	 */
    public Adapter createParameterDeclarationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.Statement <em>Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.Statement
	 * @generated
	 */
    public Adapter createStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ReturnStatement <em>Return Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ReturnStatement
	 * @generated
	 */
    public Adapter createReturnStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.DeleteStatement <em>Delete Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.DeleteStatement
	 * @generated
	 */
    public Adapter createDeleteStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ThrowStatement <em>Throw Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ThrowStatement
	 * @generated
	 */
    public Adapter createThrowStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.BreakStatement <em>Break Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.BreakStatement
	 * @generated
	 */
    public Adapter createBreakStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.BreakAllStatement <em>Break All Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.BreakAllStatement
	 * @generated
	 */
    public Adapter createBreakAllStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ContinueStatement <em>Continue Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ContinueStatement
	 * @generated
	 */
    public Adapter createContinueStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.TransactionStatement <em>Transaction Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.TransactionStatement
	 * @generated
	 */
    public Adapter createTransactionStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.AbortStatement <em>Abort Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.AbortStatement
	 * @generated
	 */
    public Adapter createAbortStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.StatementBlock <em>Statement Block</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.StatementBlock
	 * @generated
	 */
    public Adapter createStatementBlockAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ConditionalStatement <em>Conditional Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ConditionalStatement
	 * @generated
	 */
    public Adapter createConditionalStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.IfStatement <em>If Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.IfStatement
	 * @generated
	 */
    public Adapter createIfStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.WhileStatement <em>While Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.WhileStatement
	 * @generated
	 */
    public Adapter createWhileStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ForStatement <em>For Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ForStatement
	 * @generated
	 */
    public Adapter createForStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.AssignStatement <em>Assign Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.AssignStatement
	 * @generated
	 */
    public Adapter createAssignStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.SpecialAssignStatement <em>Special Assign Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.SpecialAssignStatement
	 * @generated
	 */
    public Adapter createSpecialAssignStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ExpressionStatement <em>Expression Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ExpressionStatement
	 * @generated
	 */
    public Adapter createExpressionStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ImportStatement <em>Import Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ImportStatement
	 * @generated
	 */
    public Adapter createImportStatementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.Expression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.Expression
	 * @generated
	 */
    public Adapter createExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.OperatorExpression <em>Operator Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.OperatorExpression
	 * @generated
	 */
    public Adapter createOperatorExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.BinaryOperatorExpression <em>Binary Operator Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.BinaryOperatorExpression
	 * @generated
	 */
    public Adapter createBinaryOperatorExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.UnaryOperatorExpression <em>Unary Operator Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.UnaryOperatorExpression
	 * @generated
	 */
    public Adapter createUnaryOperatorExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.FeatureCallExpression <em>Feature Call Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.FeatureCallExpression
	 * @generated
	 */
    public Adapter createFeatureCallExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.PropertyCallExpression <em>Property Call Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.PropertyCallExpression
	 * @generated
	 */
    public Adapter createPropertyCallExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.OperationCallExpression <em>Operation Call Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.OperationCallExpression
	 * @generated
	 */
    public Adapter createOperationCallExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.SimpleOperationCallExpression <em>Simple Operation Call Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.SimpleOperationCallExpression
	 * @generated
	 */
    public Adapter createSimpleOperationCallExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.DeclarativeOperationCallExpression <em>Declarative Operation Call Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.DeclarativeOperationCallExpression
	 * @generated
	 */
    public Adapter createDeclarativeOperationCallExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.CollectionExpression <em>Collection Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.CollectionExpression
	 * @generated
	 */
    public Adapter createCollectionExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.LiteralCollectionExpression <em>Literal Collection Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.LiteralCollectionExpression
	 * @generated
	 */
    public Adapter createLiteralCollectionExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.RangeCollectionExpression <em>Range Collection Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.RangeCollectionExpression
	 * @generated
	 */
    public Adapter createRangeCollectionExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.IntegerLiteralExpression <em>Integer Literal Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.IntegerLiteralExpression
	 * @generated
	 */
    public Adapter createIntegerLiteralExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.StringLiteralExpression <em>String Literal Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.StringLiteralExpression
	 * @generated
	 */
    public Adapter createStringLiteralExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.BooleanLiteralExpression <em>Boolean Literal Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.BooleanLiteralExpression
	 * @generated
	 */
    public Adapter createBooleanLiteralExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.RealLiteralExpression <em>Real Literal Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.RealLiteralExpression
	 * @generated
	 */
    public Adapter createRealLiteralExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression <em>Enumeration Literal Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression
	 * @generated
	 */
    public Adapter createEnumerationLiteralExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.NewExpression <em>New Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.NewExpression
	 * @generated
	 */
    public Adapter createNewExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.VariableReferenceExpression <em>Variable Reference Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.VariableReferenceExpression
	 * @generated
	 */
    public Adapter createVariableReferenceExpressionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.Type <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.Type
	 * @generated
	 */
    public Adapter createTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.NamedType <em>Named Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.NamedType
	 * @generated
	 */
    public Adapter createNamedTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.SimpleType <em>Simple Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.SimpleType
	 * @generated
	 */
    public Adapter createSimpleTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.ModelElementType <em>Model Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.ModelElementType
	 * @generated
	 */
    public Adapter createModelElementTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.dom.eol.CollectionType <em>Collection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.epsilon.dom.eol.CollectionType
	 * @generated
	 */
    public Adapter createCollectionTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
