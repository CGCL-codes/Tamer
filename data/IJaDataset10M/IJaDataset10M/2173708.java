package org.xvr.s3D.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.xvr.s3D.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.xvr.s3D.S3DPackage
 * @generated
 */
public class S3DSwitch<T> extends Switch<T> {

    /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected static S3DPackage modelPackage;

    /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public S3DSwitch() {
        if (modelPackage == null) {
            modelPackage = S3DPackage.eINSTANCE;
        }
    }

    /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch(classifierID) {
            case S3DPackage.PROGRAM:
                {
                    program program = (program) theEObject;
                    T result = caseprogram(program);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.SET_DECLARATION:
                {
                    setDeclaration setDeclaration = (setDeclaration) theEObject;
                    T result = casesetDeclaration(setDeclaration);
                    if (result == null) result = casepreproc(setDeclaration);
                    if (result == null) result = casestatement(setDeclaration);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.FUNCTION:
                {
                    function function = (function) theEObject;
                    T result = casefunction(function);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.FUNC_DECL:
                {
                    funcDecl funcDecl = (funcDecl) theEObject;
                    T result = casefuncDecl(funcDecl);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.IFUNCTION_DEFINITION:
                {
                    IFunctionDefinition iFunctionDefinition = (IFunctionDefinition) theEObject;
                    T result = caseIFunctionDefinition(iFunctionDefinition);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.FUNC_NAME:
                {
                    funcName funcName = (funcName) theEObject;
                    T result = casefuncName(funcName);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.FORMAL_PARAM_LIST:
                {
                    formalParamList formalParamList = (formalParamList) theEObject;
                    T result = caseformalParamList(formalParamList);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.STATEMENT_BLOCK:
                {
                    statementBlock statementBlock = (statementBlock) theEObject;
                    T result = casestatementBlock(statementBlock);
                    if (result == null) result = casestatement(statementBlock);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.CLASS_DECL:
                {
                    classDecl classDecl = (classDecl) theEObject;
                    T result = caseclassDecl(classDecl);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.OPT_BODY_CLASS:
                {
                    optBodyClass optBodyClass = (optBodyClass) theEObject;
                    T result = caseoptBodyClass(optBodyClass);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.CLASS_LIST:
                {
                    classList classList = (classList) theEObject;
                    T result = caseclassList(classList);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.BODY_CLASS:
                {
                    bodyClass bodyClass = (bodyClass) theEObject;
                    T result = casebodyClass(bodyClass);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.CLASS_METHOD_DECL:
                {
                    classMethodDecl classMethodDecl = (classMethodDecl) theEObject;
                    T result = caseclassMethodDecl(classMethodDecl);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.VAR_MEMBER_DECL_LIST:
                {
                    varMemberDeclList varMemberDeclList = (varMemberDeclList) theEObject;
                    T result = casevarMemberDeclList(varMemberDeclList);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.VAR_MEMBER_DECL:
                {
                    varMemberDecl varMemberDecl = (varMemberDecl) theEObject;
                    T result = casevarMemberDecl(varMemberDecl);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.METHOD:
                {
                    method method = (method) theEObject;
                    T result = casemethod(method);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.METHOD_DECL:
                {
                    methodDecl methodDecl = (methodDecl) theEObject;
                    T result = casemethodDecl(methodDecl);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.STATEMENT:
                {
                    statement statement = (statement) theEObject;
                    T result = casestatement(statement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.IF_STATEMENT:
                {
                    ifStatement ifStatement = (ifStatement) theEObject;
                    T result = caseifStatement(ifStatement);
                    if (result == null) result = casestatement(ifStatement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.WHILE_STATEMENT:
                {
                    whileStatement whileStatement = (whileStatement) theEObject;
                    T result = casewhileStatement(whileStatement);
                    if (result == null) result = casestatement(whileStatement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.FOR_STATEMENT:
                {
                    forStatement forStatement = (forStatement) theEObject;
                    T result = caseforStatement(forStatement);
                    if (result == null) result = casestatement(forStatement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.FOR_LIST_EXPRESSION:
                {
                    forListExpression forListExpression = (forListExpression) theEObject;
                    T result = caseforListExpression(forListExpression);
                    if (result == null) result = caseforStatement(forListExpression);
                    if (result == null) result = casestatement(forListExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.FOREACH_STATEMENT:
                {
                    foreachStatement foreachStatement = (foreachStatement) theEObject;
                    T result = caseforeachStatement(foreachStatement);
                    if (result == null) result = casestatement(foreachStatement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.SWITCH_STATEMENT:
                {
                    switchStatement switchStatement = (switchStatement) theEObject;
                    T result = caseswitchStatement(switchStatement);
                    if (result == null) result = casestatement(switchStatement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.CASE_LIST_STM:
                {
                    caseListStm caseListStm = (caseListStm) theEObject;
                    T result = casecaseListStm(caseListStm);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.CASE_STATEMENT:
                {
                    caseStatement caseStatement = (caseStatement) theEObject;
                    T result = casecaseStatement(caseStatement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.OPT_CASE_DEFAULT:
                {
                    optCaseDefault optCaseDefault = (optCaseDefault) theEObject;
                    T result = caseoptCaseDefault(optCaseDefault);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.STATEMENT_LIST:
                {
                    statementList statementList = (statementList) theEObject;
                    T result = casestatementList(statementList);
                    if (result == null) result = caseoptCaseDefault(statementList);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.LIST_EXPRESSION:
                {
                    listExpression listExpression = (listExpression) theEObject;
                    T result = caselistExpression(listExpression);
                    if (result == null) result = caseforListExpression(listExpression);
                    if (result == null) result = caseforStatement(listExpression);
                    if (result == null) result = casestatement(listExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.EXP_STATEMENT:
                {
                    expStatement expStatement = (expStatement) theEObject;
                    T result = caseexpStatement(expStatement);
                    if (result == null) result = casestatement(expStatement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.VAR_DECL:
                {
                    varDecl varDecl = (varDecl) theEObject;
                    T result = casevarDecl(varDecl);
                    if (result == null) result = casestatement(varDecl);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.VAR_DECL_LIST:
                {
                    varDeclList varDeclList = (varDeclList) theEObject;
                    T result = casevarDeclList(varDeclList);
                    if (result == null) result = caseforListExpression(varDeclList);
                    if (result == null) result = casevarDecl(varDeclList);
                    if (result == null) result = caseforStatement(varDeclList);
                    if (result == null) result = casestatement(varDeclList);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.VAR_DECL_WITH_ASSIGN:
                {
                    varDeclWithAssign varDeclWithAssign = (varDeclWithAssign) theEObject;
                    T result = casevarDeclWithAssign(varDeclWithAssign);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.EXPRESSION:
                {
                    expression expression = (expression) theEObject;
                    T result = caseexpression(expression);
                    if (result == null) result = caseforeachStatement(expression);
                    if (result == null) result = caseswitchStatement(expression);
                    if (result == null) result = caseassignmentExpressionTail(expression);
                    if (result == null) result = casepostfixExpressionTail(expression);
                    if (result == null) result = caseactualParam(expression);
                    if (result == null) result = casestatement(expression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.ASSIGNMENT_EXPRESSION_TAIL:
                {
                    assignmentExpressionTail assignmentExpressionTail = (assignmentExpressionTail) theEObject;
                    T result = caseassignmentExpressionTail(assignmentExpressionTail);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.BOOL_EXPRESSION:
                {
                    boolExpression boolExpression = (boolExpression) theEObject;
                    T result = caseboolExpression(boolExpression);
                    if (result == null) result = caseifStatement(boolExpression);
                    if (result == null) result = casestatement(boolExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.REL_EXPRESSION:
                {
                    relExpression relExpression = (relExpression) theEObject;
                    T result = caserelExpression(relExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.PRIMARY_EXPRESSION:
                {
                    primaryExpression primaryExpression = (primaryExpression) theEObject;
                    T result = caseprimaryExpression(primaryExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.ARRAY_EXPRESSION:
                {
                    arrayExpression arrayExpression = (arrayExpression) theEObject;
                    T result = casearrayExpression(arrayExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.VECTOR_EXPRESSION:
                {
                    vectorExpression vectorExpression = (vectorExpression) theEObject;
                    T result = casevectorExpression(vectorExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.UNARY_EXPRESSION:
                {
                    unaryExpression unaryExpression = (unaryExpression) theEObject;
                    T result = caseunaryExpression(unaryExpression);
                    if (result == null) result = caseexpStatement(unaryExpression);
                    if (result == null) result = casestatement(unaryExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.POSTFIX_EXPRESSION:
                {
                    postfixExpression postfixExpression = (postfixExpression) theEObject;
                    T result = casepostfixExpression(postfixExpression);
                    if (result == null) result = caseunaryExpression(postfixExpression);
                    if (result == null) result = caseactualParam(postfixExpression);
                    if (result == null) result = caseexpStatement(postfixExpression);
                    if (result == null) result = casestatement(postfixExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.POSTFIX_EXPRESSION_TAIL:
                {
                    postfixExpressionTail postfixExpressionTail = (postfixExpressionTail) theEObject;
                    T result = casepostfixExpressionTail(postfixExpressionTail);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.SIMPLEX_EXPRESSION:
                {
                    simplexExpression simplexExpression = (simplexExpression) theEObject;
                    T result = casesimplexExpression(simplexExpression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.SIMPLE_EXPRESSION_HEAD:
                {
                    simpleExpressionHead simpleExpressionHead = (simpleExpressionHead) theEObject;
                    T result = casesimpleExpressionHead(simpleExpressionHead);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.ACTUAL_PARAM_LIST:
                {
                    actualParamList actualParamList = (actualParamList) theEObject;
                    T result = caseactualParamList(actualParamList);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.PARAM_LIST:
                {
                    paramList paramList = (paramList) theEObject;
                    T result = caseparamList(paramList);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.ACTUAL_PARAM:
                {
                    actualParam actualParam = (actualParam) theEObject;
                    T result = caseactualParam(actualParam);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case S3DPackage.PREPROC:
                {
                    preproc preproc = (preproc) theEObject;
                    T result = casepreproc(preproc);
                    if (result == null) result = casestatement(preproc);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>program</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>program</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseprogram(program object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>set Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>set Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casesetDeclaration(setDeclaration object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>function</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>function</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casefunction(function object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>func Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>func Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casefuncDecl(funcDecl object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>IFunction Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IFunction Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseIFunctionDefinition(IFunctionDefinition object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>func Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>func Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casefuncName(funcName object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>formal Param List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>formal Param List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseformalParamList(formalParamList object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>statement Block</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>statement Block</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casestatementBlock(statementBlock object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>class Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>class Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseclassDecl(classDecl object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>opt Body Class</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>opt Body Class</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseoptBodyClass(optBodyClass object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>class List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>class List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseclassList(classList object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>body Class</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>body Class</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casebodyClass(bodyClass object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>class Method Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>class Method Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseclassMethodDecl(classMethodDecl object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>var Member Decl List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>var Member Decl List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casevarMemberDeclList(varMemberDeclList object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>var Member Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>var Member Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casevarMemberDecl(varMemberDecl object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>method</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>method</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casemethod(method object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>method Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>method Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casemethodDecl(methodDecl object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casestatement(statement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>if Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>if Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseifStatement(ifStatement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>while Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>while Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casewhileStatement(whileStatement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>for Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>for Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseforStatement(forStatement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>for List Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>for List Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseforListExpression(forListExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>foreach Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>foreach Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseforeachStatement(foreachStatement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>switch Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>switch Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseswitchStatement(switchStatement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>case List Stm</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>case List Stm</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casecaseListStm(caseListStm object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>case Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>case Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casecaseStatement(caseStatement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>opt Case Default</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>opt Case Default</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseoptCaseDefault(optCaseDefault object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>statement List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>statement List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casestatementList(statementList object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>list Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>list Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caselistExpression(listExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>exp Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>exp Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseexpStatement(expStatement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>var Decl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>var Decl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casevarDecl(varDecl object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>var Decl List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>var Decl List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casevarDeclList(varDeclList object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>var Decl With Assign</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>var Decl With Assign</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casevarDeclWithAssign(varDeclWithAssign object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseexpression(expression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>assignment Expression Tail</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>assignment Expression Tail</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseassignmentExpressionTail(assignmentExpressionTail object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>bool Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>bool Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseboolExpression(boolExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>rel Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>rel Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caserelExpression(relExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>primary Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>primary Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseprimaryExpression(primaryExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casearrayExpression(arrayExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>vector Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>vector Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casevectorExpression(vectorExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>unary Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>unary Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseunaryExpression(unaryExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>postfix Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>postfix Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casepostfixExpression(postfixExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>postfix Expression Tail</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>postfix Expression Tail</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casepostfixExpressionTail(postfixExpressionTail object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>simplex Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>simplex Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casesimplexExpression(simplexExpression object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>simple Expression Head</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>simple Expression Head</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casesimpleExpressionHead(simpleExpressionHead object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>actual Param List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>actual Param List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseactualParamList(actualParamList object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>param List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>param List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseparamList(paramList object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>actual Param</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>actual Param</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseactualParam(actualParam object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>preproc</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>preproc</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casepreproc(preproc object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }
}
