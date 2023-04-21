package org.xvr.s3D.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.xvr.s3D.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class S3DFactoryImpl extends EFactoryImpl implements S3DFactory {

    /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static S3DFactory init() {
        try {
            S3DFactory theS3DFactory = (S3DFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.xvr.org/S3D");
            if (theS3DFactory != null) {
                return theS3DFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new S3DFactoryImpl();
    }

    /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public S3DFactoryImpl() {
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
            case S3DPackage.PROGRAM:
                return createprogram();
            case S3DPackage.SET_DECLARATION:
                return createsetDeclaration();
            case S3DPackage.FUNCTION:
                return createfunction();
            case S3DPackage.FUNC_DECL:
                return createfuncDecl();
            case S3DPackage.IFUNCTION_DEFINITION:
                return createIFunctionDefinition();
            case S3DPackage.FUNC_NAME:
                return createfuncName();
            case S3DPackage.FORMAL_PARAM_LIST:
                return createformalParamList();
            case S3DPackage.STATEMENT_BLOCK:
                return createstatementBlock();
            case S3DPackage.CLASS_DECL:
                return createclassDecl();
            case S3DPackage.OPT_BODY_CLASS:
                return createoptBodyClass();
            case S3DPackage.CLASS_LIST:
                return createclassList();
            case S3DPackage.BODY_CLASS:
                return createbodyClass();
            case S3DPackage.CLASS_METHOD_DECL:
                return createclassMethodDecl();
            case S3DPackage.VAR_MEMBER_DECL_LIST:
                return createvarMemberDeclList();
            case S3DPackage.VAR_MEMBER_DECL:
                return createvarMemberDecl();
            case S3DPackage.METHOD:
                return createmethod();
            case S3DPackage.METHOD_DECL:
                return createmethodDecl();
            case S3DPackage.STATEMENT:
                return createstatement();
            case S3DPackage.IF_STATEMENT:
                return createifStatement();
            case S3DPackage.WHILE_STATEMENT:
                return createwhileStatement();
            case S3DPackage.FOR_STATEMENT:
                return createforStatement();
            case S3DPackage.FOR_LIST_EXPRESSION:
                return createforListExpression();
            case S3DPackage.FOREACH_STATEMENT:
                return createforeachStatement();
            case S3DPackage.SWITCH_STATEMENT:
                return createswitchStatement();
            case S3DPackage.CASE_LIST_STM:
                return createcaseListStm();
            case S3DPackage.CASE_STATEMENT:
                return createcaseStatement();
            case S3DPackage.OPT_CASE_DEFAULT:
                return createoptCaseDefault();
            case S3DPackage.STATEMENT_LIST:
                return createstatementList();
            case S3DPackage.LIST_EXPRESSION:
                return createlistExpression();
            case S3DPackage.EXP_STATEMENT:
                return createexpStatement();
            case S3DPackage.VAR_DECL:
                return createvarDecl();
            case S3DPackage.VAR_DECL_LIST:
                return createvarDeclList();
            case S3DPackage.VAR_DECL_WITH_ASSIGN:
                return createvarDeclWithAssign();
            case S3DPackage.EXPRESSION:
                return createexpression();
            case S3DPackage.ASSIGNMENT_EXPRESSION_TAIL:
                return createassignmentExpressionTail();
            case S3DPackage.BOOL_EXPRESSION:
                return createboolExpression();
            case S3DPackage.REL_EXPRESSION:
                return createrelExpression();
            case S3DPackage.PRIMARY_EXPRESSION:
                return createprimaryExpression();
            case S3DPackage.ARRAY_EXPRESSION:
                return createarrayExpression();
            case S3DPackage.VECTOR_EXPRESSION:
                return createvectorExpression();
            case S3DPackage.UNARY_EXPRESSION:
                return createunaryExpression();
            case S3DPackage.POSTFIX_EXPRESSION:
                return createpostfixExpression();
            case S3DPackage.POSTFIX_EXPRESSION_TAIL:
                return createpostfixExpressionTail();
            case S3DPackage.SIMPLEX_EXPRESSION:
                return createsimplexExpression();
            case S3DPackage.SIMPLE_EXPRESSION_HEAD:
                return createsimpleExpressionHead();
            case S3DPackage.ACTUAL_PARAM_LIST:
                return createactualParamList();
            case S3DPackage.PARAM_LIST:
                return createparamList();
            case S3DPackage.ACTUAL_PARAM:
                return createactualParam();
            case S3DPackage.PREPROC:
                return createpreproc();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public program createprogram() {
        programImpl program = new programImpl();
        return program;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public setDeclaration createsetDeclaration() {
        setDeclarationImpl setDeclaration = new setDeclarationImpl();
        return setDeclaration;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public function createfunction() {
        functionImpl function = new functionImpl();
        return function;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public funcDecl createfuncDecl() {
        funcDeclImpl funcDecl = new funcDeclImpl();
        return funcDecl;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public IFunctionDefinition createIFunctionDefinition() {
        IFunctionDefinitionImpl iFunctionDefinition = new IFunctionDefinitionImpl();
        return iFunctionDefinition;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public funcName createfuncName() {
        funcNameImpl funcName = new funcNameImpl();
        return funcName;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public formalParamList createformalParamList() {
        formalParamListImpl formalParamList = new formalParamListImpl();
        return formalParamList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public statementBlock createstatementBlock() {
        statementBlockImpl statementBlock = new statementBlockImpl();
        return statementBlock;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public classDecl createclassDecl() {
        classDeclImpl classDecl = new classDeclImpl();
        return classDecl;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public optBodyClass createoptBodyClass() {
        optBodyClassImpl optBodyClass = new optBodyClassImpl();
        return optBodyClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public classList createclassList() {
        classListImpl classList = new classListImpl();
        return classList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public bodyClass createbodyClass() {
        bodyClassImpl bodyClass = new bodyClassImpl();
        return bodyClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public classMethodDecl createclassMethodDecl() {
        classMethodDeclImpl classMethodDecl = new classMethodDeclImpl();
        return classMethodDecl;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public varMemberDeclList createvarMemberDeclList() {
        varMemberDeclListImpl varMemberDeclList = new varMemberDeclListImpl();
        return varMemberDeclList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public varMemberDecl createvarMemberDecl() {
        varMemberDeclImpl varMemberDecl = new varMemberDeclImpl();
        return varMemberDecl;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public method createmethod() {
        methodImpl method = new methodImpl();
        return method;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public methodDecl createmethodDecl() {
        methodDeclImpl methodDecl = new methodDeclImpl();
        return methodDecl;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public statement createstatement() {
        statementImpl statement = new statementImpl();
        return statement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ifStatement createifStatement() {
        ifStatementImpl ifStatement = new ifStatementImpl();
        return ifStatement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public whileStatement createwhileStatement() {
        whileStatementImpl whileStatement = new whileStatementImpl();
        return whileStatement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public forStatement createforStatement() {
        forStatementImpl forStatement = new forStatementImpl();
        return forStatement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public forListExpression createforListExpression() {
        forListExpressionImpl forListExpression = new forListExpressionImpl();
        return forListExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public foreachStatement createforeachStatement() {
        foreachStatementImpl foreachStatement = new foreachStatementImpl();
        return foreachStatement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public switchStatement createswitchStatement() {
        switchStatementImpl switchStatement = new switchStatementImpl();
        return switchStatement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public caseListStm createcaseListStm() {
        caseListStmImpl caseListStm = new caseListStmImpl();
        return caseListStm;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public caseStatement createcaseStatement() {
        caseStatementImpl caseStatement = new caseStatementImpl();
        return caseStatement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public optCaseDefault createoptCaseDefault() {
        optCaseDefaultImpl optCaseDefault = new optCaseDefaultImpl();
        return optCaseDefault;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public statementList createstatementList() {
        statementListImpl statementList = new statementListImpl();
        return statementList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public listExpression createlistExpression() {
        listExpressionImpl listExpression = new listExpressionImpl();
        return listExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public expStatement createexpStatement() {
        expStatementImpl expStatement = new expStatementImpl();
        return expStatement;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public varDecl createvarDecl() {
        varDeclImpl varDecl = new varDeclImpl();
        return varDecl;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public varDeclList createvarDeclList() {
        varDeclListImpl varDeclList = new varDeclListImpl();
        return varDeclList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public varDeclWithAssign createvarDeclWithAssign() {
        varDeclWithAssignImpl varDeclWithAssign = new varDeclWithAssignImpl();
        return varDeclWithAssign;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public expression createexpression() {
        expressionImpl expression = new expressionImpl();
        return expression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public assignmentExpressionTail createassignmentExpressionTail() {
        assignmentExpressionTailImpl assignmentExpressionTail = new assignmentExpressionTailImpl();
        return assignmentExpressionTail;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolExpression createboolExpression() {
        boolExpressionImpl boolExpression = new boolExpressionImpl();
        return boolExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public relExpression createrelExpression() {
        relExpressionImpl relExpression = new relExpressionImpl();
        return relExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public primaryExpression createprimaryExpression() {
        primaryExpressionImpl primaryExpression = new primaryExpressionImpl();
        return primaryExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public arrayExpression createarrayExpression() {
        arrayExpressionImpl arrayExpression = new arrayExpressionImpl();
        return arrayExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public vectorExpression createvectorExpression() {
        vectorExpressionImpl vectorExpression = new vectorExpressionImpl();
        return vectorExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public unaryExpression createunaryExpression() {
        unaryExpressionImpl unaryExpression = new unaryExpressionImpl();
        return unaryExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public postfixExpression createpostfixExpression() {
        postfixExpressionImpl postfixExpression = new postfixExpressionImpl();
        return postfixExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public postfixExpressionTail createpostfixExpressionTail() {
        postfixExpressionTailImpl postfixExpressionTail = new postfixExpressionTailImpl();
        return postfixExpressionTail;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public simplexExpression createsimplexExpression() {
        simplexExpressionImpl simplexExpression = new simplexExpressionImpl();
        return simplexExpression;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public simpleExpressionHead createsimpleExpressionHead() {
        simpleExpressionHeadImpl simpleExpressionHead = new simpleExpressionHeadImpl();
        return simpleExpressionHead;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public actualParamList createactualParamList() {
        actualParamListImpl actualParamList = new actualParamListImpl();
        return actualParamList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public paramList createparamList() {
        paramListImpl paramList = new paramListImpl();
        return paramList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public actualParam createactualParam() {
        actualParamImpl actualParam = new actualParamImpl();
        return actualParam;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public preproc createpreproc() {
        preprocImpl preproc = new preprocImpl();
        return preproc;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public S3DPackage getS3DPackage() {
        return (S3DPackage) getEPackage();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
    @Deprecated
    public static S3DPackage getPackage() {
        return S3DPackage.eINSTANCE;
    }
}
