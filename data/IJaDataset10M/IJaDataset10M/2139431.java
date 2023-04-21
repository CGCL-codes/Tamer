package org.jmlspecs.jir.visitor;

import org.jmlspecs.jir.visitor.result.*;

public interface JIRExpVisitor extends JIRVisitor {

    void visit();

    void visitEnd();

    IExp visitMethodCallExp(String name, IExp[] params, JIRType type);

    IExp visitConstructorCallExp(JIRType type, IExp[] params);

    IExp visitLocal(String localName, JIRType type, boolean isParam);

    IExp visitClassExp(JIRType type);

    IExp visitResultExp(JIRType type);

    IExp visitPreExp(IExp exp);

    IExp visitOldExp(IExp exp, String label);

    IPred visitPredicate(IExp exp);

    IExp visitNumberLiteral(Number number, JIRType type);

    IExp visitBooleanLiteral(boolean bool);

    IExp visitStringLiteral(String str);

    IExp visitBinExp(JIROpType opType, IExp left, IExp right, JIRType resultType);

    /**
   * @param loc
   *          the location expression
   * @param allFields
   *          allFields and allElements cannot be true at the same time.
   * @param allElements
   *          allFields and allElements cannot be true at the same time.
   * @param isRanged
   *          true if a ranged expression is used.
   * @param low
   *          valid only if isRanged is true.
   * @param high
   *          valid only if isRanged is true.
   */
    IStoreRefExp visitStoreRefExp(IExp loc, boolean allFields, boolean allElements, boolean isRanged, IExp low, IExp high);

    IMemberFieldRef visitMemberFieldRef(IExp loc, boolean allFields, boolean allElements, boolean isRanged, IExp low, IExp high);

    /**
   * 
   * @param desc There are a number of possible valid method descriptions. It can be only a method name, or a method name
   * can be followed by a parameter signature. More detail can be found in the JML grammar. Currently, users should parse 
   * this desc value themselves.
   * @return
   */
    IExp visitMethodName(String desc);

    IExp visitInformalDescription(String description);

    IExp visitNotAssignedExp(JMLKeyword notSpecified);

    IExp visitNotAssignedExp(IStoreRefExp[] storeRefs);

    IExp visitNotModifiedExp(JMLKeyword notSpecified);

    IExp visitNotModifiedExp(IStoreRefExp[] storeRefs);

    IExp visitOnlyAccessedExp(IStoreRefExp[] storeRefs);

    IExp visitOnlyAccessedExp(JMLKeyword notSpecified);

    IExp visitOnlyAssignedExp(IStoreRefExp[] storeRefs);

    IExp visitOnlyAssignedExp(JMLKeyword notSpecified);

    IExp visitOnlyCapturedExp(IStoreRefExp[] storeRefs);

    IExp visitOnlyCalledExp(IResult[] methods);

    IFieldExp visitFieldExp(JIRType declaringType, String name, JIRType fieldType, JIRType receiverType);

    IExp visitFreshExp(IExp[] exps);

    IExp visitReachExp(IExp exp);

    IExp visitDurationExp(IExp exp);

    IExp visitWorkingSpaceExp(IExp exp);

    IExp visitSpaceExp(IExp exp);

    IExp visitNonnullelementsExp(IExp exp);

    IExp visitTypeExp(IExp exp);

    IExp visitTypeofExp(IExp exp);

    IExp visitElemTypeExp(IExp exp);

    IExp visitNullLiteral();

    IExp visitReceiver(JIRType type);

    IExp visitLocksetExp();

    IExp visitCastExp(JIRType castType, IExp castee);

    IExp visitInvariantForExp(JIRType type, IExp exp);

    IExp visitQuantifiedExp(Quantifier q, ISpecVarDecl[] decls, IPred rangePred, IExp body);

    IExp visitThisExp(JIRType type);

    IExp visitSuperExp(JIRType currentType);

    IExp visitArrayElementExp(IExp arrayExp, IExp indexExp);
}
