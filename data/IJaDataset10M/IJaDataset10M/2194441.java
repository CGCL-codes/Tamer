package org.eclipse.epsilon.dom.eol.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.epsilon.dom.eol.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EolFactoryImpl extends EFactoryImpl implements EolFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static EolFactory init() {
        try {
            EolFactory theEolFactory = (EolFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/epsilon/eol");
            if (theEolFactory != null) {
                return theEolFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new EolFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EolFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case EolPackage.EOL_MODULE:
                return createEolModule();
            case EolPackage.MODEL_DECLARATION:
                return createModelDeclaration();
            case EolPackage.OPERATION_DECLARATION:
                return createOperationDeclaration();
            case EolPackage.VARIABLE_DECLARATION:
                return createVariableDeclaration();
            case EolPackage.PARAMETER_DECLARATION:
                return createParameterDeclaration();
            case EolPackage.RETURN_STATEMENT:
                return createReturnStatement();
            case EolPackage.DELETE_STATEMENT:
                return createDeleteStatement();
            case EolPackage.THROW_STATEMENT:
                return createThrowStatement();
            case EolPackage.BREAK_STATEMENT:
                return createBreakStatement();
            case EolPackage.BREAK_ALL_STATEMENT:
                return createBreakAllStatement();
            case EolPackage.CONTINUE_STATEMENT:
                return createContinueStatement();
            case EolPackage.TRANSACTION_STATEMENT:
                return createTransactionStatement();
            case EolPackage.ABORT_STATEMENT:
                return createAbortStatement();
            case EolPackage.STATEMENT_BLOCK:
                return createStatementBlock();
            case EolPackage.IF_STATEMENT:
                return createIfStatement();
            case EolPackage.WHILE_STATEMENT:
                return createWhileStatement();
            case EolPackage.FOR_STATEMENT:
                return createForStatement();
            case EolPackage.ASSIGN_STATEMENT:
                return createAssignStatement();
            case EolPackage.SPECIAL_ASSIGN_STATEMENT:
                return createSpecialAssignStatement();
            case EolPackage.EXPRESSION_STATEMENT:
                return createExpressionStatement();
            case EolPackage.IMPORT_STATEMENT:
                return createImportStatement();
            case EolPackage.BINARY_OPERATOR_EXPRESSION:
                return createBinaryOperatorExpression();
            case EolPackage.UNARY_OPERATOR_EXPRESSION:
                return createUnaryOperatorExpression();
            case EolPackage.PROPERTY_CALL_EXPRESSION:
                return createPropertyCallExpression();
            case EolPackage.SIMPLE_OPERATION_CALL_EXPRESSION:
                return createSimpleOperationCallExpression();
            case EolPackage.DECLARATIVE_OPERATION_CALL_EXPRESSION:
                return createDeclarativeOperationCallExpression();
            case EolPackage.LITERAL_COLLECTION_EXPRESSION:
                return createLiteralCollectionExpression();
            case EolPackage.RANGE_COLLECTION_EXPRESSION:
                return createRangeCollectionExpression();
            case EolPackage.INTEGER_LITERAL_EXPRESSION:
                return createIntegerLiteralExpression();
            case EolPackage.STRING_LITERAL_EXPRESSION:
                return createStringLiteralExpression();
            case EolPackage.BOOLEAN_LITERAL_EXPRESSION:
                return createBooleanLiteralExpression();
            case EolPackage.REAL_LITERAL_EXPRESSION:
                return createRealLiteralExpression();
            case EolPackage.ENUMERATION_LITERAL_EXPRESSION:
                return createEnumerationLiteralExpression();
            case EolPackage.NEW_EXPRESSION:
                return createNewExpression();
            case EolPackage.VARIABLE_REFERENCE_EXPRESSION:
                return createVariableReferenceExpression();
            case EolPackage.SIMPLE_TYPE:
                return createSimpleType();
            case EolPackage.MODEL_ELEMENT_TYPE:
                return createModelElementType();
            case EolPackage.COLLECTION_TYPE:
                return createCollectionType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch(eDataType.getClassifierID()) {
            case EolPackage.OPERATOR:
                return createOperatorFromString(eDataType, initialValue);
            case EolPackage.STRING:
                return createStringFromString(eDataType, initialValue);
            case EolPackage.INTEGER:
                return createIntegerFromString(eDataType, initialValue);
            case EolPackage.BOOLEAN:
                return createBooleanFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch(eDataType.getClassifierID()) {
            case EolPackage.OPERATOR:
                return convertOperatorToString(eDataType, instanceValue);
            case EolPackage.STRING:
                return convertStringToString(eDataType, instanceValue);
            case EolPackage.INTEGER:
                return convertIntegerToString(eDataType, instanceValue);
            case EolPackage.BOOLEAN:
                return convertBooleanToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EolModule createEolModule() {
        EolModuleImpl eolModule = new EolModuleImpl();
        return eolModule;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelDeclaration createModelDeclaration() {
        ModelDeclarationImpl modelDeclaration = new ModelDeclarationImpl();
        return modelDeclaration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OperationDeclaration createOperationDeclaration() {
        OperationDeclarationImpl operationDeclaration = new OperationDeclarationImpl();
        return operationDeclaration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VariableDeclaration createVariableDeclaration() {
        VariableDeclarationImpl variableDeclaration = new VariableDeclarationImpl();
        return variableDeclaration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ParameterDeclaration createParameterDeclaration() {
        ParameterDeclarationImpl parameterDeclaration = new ParameterDeclarationImpl();
        return parameterDeclaration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ReturnStatement createReturnStatement() {
        ReturnStatementImpl returnStatement = new ReturnStatementImpl();
        return returnStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DeleteStatement createDeleteStatement() {
        DeleteStatementImpl deleteStatement = new DeleteStatementImpl();
        return deleteStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ThrowStatement createThrowStatement() {
        ThrowStatementImpl throwStatement = new ThrowStatementImpl();
        return throwStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BreakStatement createBreakStatement() {
        BreakStatementImpl breakStatement = new BreakStatementImpl();
        return breakStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BreakAllStatement createBreakAllStatement() {
        BreakAllStatementImpl breakAllStatement = new BreakAllStatementImpl();
        return breakAllStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ContinueStatement createContinueStatement() {
        ContinueStatementImpl continueStatement = new ContinueStatementImpl();
        return continueStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TransactionStatement createTransactionStatement() {
        TransactionStatementImpl transactionStatement = new TransactionStatementImpl();
        return transactionStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AbortStatement createAbortStatement() {
        AbortStatementImpl abortStatement = new AbortStatementImpl();
        return abortStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StatementBlock createStatementBlock() {
        StatementBlockImpl statementBlock = new StatementBlockImpl();
        return statementBlock;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IfStatement createIfStatement() {
        IfStatementImpl ifStatement = new IfStatementImpl();
        return ifStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public WhileStatement createWhileStatement() {
        WhileStatementImpl whileStatement = new WhileStatementImpl();
        return whileStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ForStatement createForStatement() {
        ForStatementImpl forStatement = new ForStatementImpl();
        return forStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AssignStatement createAssignStatement() {
        AssignStatementImpl assignStatement = new AssignStatementImpl();
        return assignStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SpecialAssignStatement createSpecialAssignStatement() {
        SpecialAssignStatementImpl specialAssignStatement = new SpecialAssignStatementImpl();
        return specialAssignStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ExpressionStatement createExpressionStatement() {
        ExpressionStatementImpl expressionStatement = new ExpressionStatementImpl();
        return expressionStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ImportStatement createImportStatement() {
        ImportStatementImpl importStatement = new ImportStatementImpl();
        return importStatement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BinaryOperatorExpression createBinaryOperatorExpression() {
        BinaryOperatorExpressionImpl binaryOperatorExpression = new BinaryOperatorExpressionImpl();
        return binaryOperatorExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UnaryOperatorExpression createUnaryOperatorExpression() {
        UnaryOperatorExpressionImpl unaryOperatorExpression = new UnaryOperatorExpressionImpl();
        return unaryOperatorExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PropertyCallExpression createPropertyCallExpression() {
        PropertyCallExpressionImpl propertyCallExpression = new PropertyCallExpressionImpl();
        return propertyCallExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SimpleOperationCallExpression createSimpleOperationCallExpression() {
        SimpleOperationCallExpressionImpl simpleOperationCallExpression = new SimpleOperationCallExpressionImpl();
        return simpleOperationCallExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DeclarativeOperationCallExpression createDeclarativeOperationCallExpression() {
        DeclarativeOperationCallExpressionImpl declarativeOperationCallExpression = new DeclarativeOperationCallExpressionImpl();
        return declarativeOperationCallExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LiteralCollectionExpression createLiteralCollectionExpression() {
        LiteralCollectionExpressionImpl literalCollectionExpression = new LiteralCollectionExpressionImpl();
        return literalCollectionExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RangeCollectionExpression createRangeCollectionExpression() {
        RangeCollectionExpressionImpl rangeCollectionExpression = new RangeCollectionExpressionImpl();
        return rangeCollectionExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IntegerLiteralExpression createIntegerLiteralExpression() {
        IntegerLiteralExpressionImpl integerLiteralExpression = new IntegerLiteralExpressionImpl();
        return integerLiteralExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StringLiteralExpression createStringLiteralExpression() {
        StringLiteralExpressionImpl stringLiteralExpression = new StringLiteralExpressionImpl();
        return stringLiteralExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BooleanLiteralExpression createBooleanLiteralExpression() {
        BooleanLiteralExpressionImpl booleanLiteralExpression = new BooleanLiteralExpressionImpl();
        return booleanLiteralExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RealLiteralExpression createRealLiteralExpression() {
        RealLiteralExpressionImpl realLiteralExpression = new RealLiteralExpressionImpl();
        return realLiteralExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EnumerationLiteralExpression createEnumerationLiteralExpression() {
        EnumerationLiteralExpressionImpl enumerationLiteralExpression = new EnumerationLiteralExpressionImpl();
        return enumerationLiteralExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NewExpression createNewExpression() {
        NewExpressionImpl newExpression = new NewExpressionImpl();
        return newExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VariableReferenceExpression createVariableReferenceExpression() {
        VariableReferenceExpressionImpl variableReferenceExpression = new VariableReferenceExpressionImpl();
        return variableReferenceExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SimpleType createSimpleType() {
        SimpleTypeImpl simpleType = new SimpleTypeImpl();
        return simpleType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelElementType createModelElementType() {
        ModelElementTypeImpl modelElementType = new ModelElementTypeImpl();
        return modelElementType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CollectionType createCollectionType() {
        CollectionTypeImpl collectionType = new CollectionTypeImpl();
        return collectionType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Operator createOperatorFromString(EDataType eDataType, String initialValue) {
        Operator result = Operator.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertOperatorToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String createStringFromString(EDataType eDataType, String initialValue) {
        return (String) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertStringToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Integer createIntegerFromString(EDataType eDataType, String initialValue) {
        return (Integer) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertIntegerToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Boolean createBooleanFromString(EDataType eDataType, String initialValue) {
        return (Boolean) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertBooleanToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EolPackage getEolPackage() {
        return (EolPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static EolPackage getPackage() {
        return EolPackage.eINSTANCE;
    }
}
