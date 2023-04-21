package tudresden.ocl20.core;

import tudresden.ocl20.core.util.ReflectiveVisitor;
import tudresden.ocl20.core.jmi.ocl.commonmodel.*;
import tudresden.ocl20.core.jmi.ocl.types.*;
import tudresden.ocl20.core.jmi.ocl.expressions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The type evaluator implements a bootom-up type inference algorithm that determines the type of 
 * an OCL expression according to the WFRs for the OCL::Expressions package by considering the types of the subexpressions. 
 * Furthermore, while traversing the expression, the type evaluator is validating other WFRs that do not describe the type.
 * If one of those WFRs is hurt, a {@link WellFormednessException WellFormednessException} is thrown.
 * Due to its implementation as reflective visitor, this class offers a lot of public methods that should not be called from other classes.
 * Only the method {@link #getType getType} should be used.
 * @author  Stefan Ocke
 */
public class TypeEvaluator extends ReflectiveVisitor {

    OclLibrary oclLib;

    private static final boolean DEEPFLATTENING = false;

    /** Creates a new instance of TypeEvaluator */
    public TypeEvaluator(OclModel model) {
        super("evaluate");
        oclLib = model.getOclLibrary();
    }

    /** Evaluates the type of an OclExpression, VariableDeclaration or CollectionLiteralPart.
     * During this evaluation, the types of the subexpressions are determined as well (if not already evaluated before.)
     * @param me the OclExpression, VaraibleDeclaration or CollectionLiteralPart
     * @throws WellFormednessException
     * @return the type
     */
    public Classifier getType(ModelElement me) throws WellFormednessException {
        Classifier type = (Classifier) me.refGetValue("type");
        if (type == null) {
            try {
                visit(me);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (java.lang.reflect.InvocationTargetException e) {
                Throwable nestedException = e.getTargetException();
                if (nestedException instanceof WellFormednessException) {
                    throw (WellFormednessException) nestedException;
                } else {
                    nestedException.printStackTrace();
                }
            }
        }
        type = (Classifier) me.refGetValue("type");
        return type;
    }

    private Classifier considerOrderAndUniqueness(Multiplicity mult, Classifier type) {
        if (mult.isUniqueA()) {
            if (mult.isOrderedA()) {
                return type.getSequenceType();
            } else {
                return type.getSetType();
            }
        } else {
            if (mult.isOrderedA()) {
                return type.getSequenceType();
            } else {
                return type.getBagType();
            }
        }
    }

    private Classifier considerMultiplicity(Multiplicity mult, Classifier type) {
        if (mult.isMultipleA()) {
            return considerOrderAndUniqueness(mult, type);
        } else {
            return type;
        }
    }

    private Classifier evaluateSourceType(PropertyCallExp exp) throws WellFormednessException {
        OclExpression source = exp.getSource();
        if (source == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_SOURCE_EXP);
        }
        return getType(source);
    }

    public void evaluate(AttributeCallExp exp) throws WellFormednessException {
        Attribute attr = exp.getReferredAttribute();
        if (attr == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_ATTRIBUTE);
        }
        if (exp.getSource() != null || attr.isInstanceLevelA()) {
            evaluateSourceType(exp);
        } else if (exp.getSrcType() == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_SRCTYPE);
        }
        exp.setType(considerMultiplicity(attr, attr.getTypeA()));
    }

    public void evaluate(AssociationClassCallExp exp) throws WellFormednessException {
        AssociationClass ac = exp.getReferredAssociationClass();
        if (ac == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_ASSOCIATIONCLASS);
        }
        Classifier type = oclLib.getOclVoid();
        Classifier sourceType = evaluateSourceType(exp);
        List ends = ac.getEndsA();
        if (ends.size() == 2) {
            AssociationEnd destEnd;
            AssociationEnd srcEnd;
            AssociationEnd ae1 = (AssociationEnd) ends.get(0);
            AssociationEnd ae2 = (AssociationEnd) ends.get(1);
            srcEnd = exp.getNavigationSource();
            if (sourceType.conformsTo(ae1.getTypeA())) {
                destEnd = ae2;
            } else {
                destEnd = ae1;
            }
            if (sourceType.conformsTo(destEnd.getTypeA())) {
                if (srcEnd == null) {
                    throw new WellFormednessException(exp, WellFormednessException.EC_AMBIGUOUS_ASSOCCLASS_NAVI);
                } else if (srcEnd.equals(ae1)) {
                    destEnd = ae2;
                } else {
                    destEnd = ae1;
                }
            }
            List qualifiers = exp.getQualifiers();
            type = evaluateAssociationEnd(exp, destEnd, qualifiers, ac);
        } else {
            if (exp.getQualifiers().size() > 0) {
                throw new WellFormednessException(exp, WellFormednessException.EC_ILLEGAL_QUALIFIED_NAVI_N_ARY);
            }
            type = ac.getSetType();
        }
        exp.setType(type);
    }

    public void evaluate(AssociationEndCallExp exp) throws WellFormednessException {
        AssociationEnd ae = exp.getReferredAssociationEnd();
        if (ae == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_ASSOCIATIONEND);
        }
        Classifier type = null;
        Classifier srcType = evaluateSourceType(exp);
        if (srcType instanceof AssociationClass && ((AssociationClass) srcType).getEndsA().contains(ae)) {
            if (exp.getQualifiers().size() > 0) {
                throw new WellFormednessException(exp, WellFormednessException.EC_ILLEGAL_QUALIFIED_NAVI_ASSOCCLASS);
            }
            type = ae.getTypeA();
        } else if (ae.isBinaryA()) {
            List qualifiers = exp.getQualifiers();
            type = evaluateAssociationEnd(exp, ae, qualifiers);
        } else {
            if (exp.getQualifiers().size() > 0) {
                throw new WellFormednessException(exp, WellFormednessException.EC_ILLEGAL_QUALIFIED_NAVI_N_ARY);
            }
            type = considerOrderAndUniqueness(ae, ae.getTypeA());
        }
        exp.setType(type);
    }

    private Classifier evaluateAssociationEnd(NavigationCallExp exp, AssociationEnd ae, List qualifiers) throws WellFormednessException {
        return evaluateAssociationEnd(exp, ae, qualifiers, ae.getTypeA());
    }

    private Classifier evaluateAssociationEnd(NavigationCallExp exp, AssociationEnd ae, List qualifiers, Classifier basictype) throws WellFormednessException {
        List aeQualifiers = ae.getQualifiersA();
        if (aeQualifiers.isEmpty()) {
            if (qualifiers.size() > 0) {
                throw new WellFormednessException(exp, WellFormednessException.EC_ILLEGAL_QUALIFIED_NAVI_TO_UNQUALIFIED_END);
            }
            return considerMultiplicity(ae, basictype);
        } else {
            if (qualifiers.size() == 0) {
                return considerOrderAndUniqueness(ae, basictype);
            } else {
                if (aeQualifiers.size() != qualifiers.size()) {
                    throw new WellFormednessException(exp, WellFormednessException.EC_QUALIFIER_MISMATCH);
                }
                for (int i = 0; i < qualifiers.size(); i++) {
                    if (!getType(((OclExpression) qualifiers.get(i))).equals(((Attribute) aeQualifiers.get(i)).getTypeA())) {
                        throw new WellFormednessException(exp, WellFormednessException.EC_QUALIFIER_MISMATCH);
                    }
                }
                return considerMultiplicity(ae, basictype);
            }
        }
    }

    public void evaluate(BooleanLiteralExp exp) {
        Classifier type = oclLib.getOclBoolean();
        exp.setType(type);
    }

    public void evaluate(CollectionLiteralExp exp) throws WellFormednessException {
        tudresden.ocl20.core.jmi.ocl.expressions.CollectionKind kind = exp.getKind();
        if (kind == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_COLLECTIONKIND);
        }
        if (kind == tudresden.ocl20.core.jmi.ocl.expressions.CollectionKindEnum.COLLECTION) {
            throw new WellFormednessException(exp, WellFormednessException.EC_KIND_IS_COLLECTION);
        }
        Classifier type = null;
        Classifier elementType = oclLib.getOclVoid();
        Iterator it = exp.getParts().iterator();
        while (it.hasNext()) {
            CollectionLiteralPart p = (CollectionLiteralPart) it.next();
            elementType = (Classifier) elementType.commonSuperType(getType(p));
        }
        if (kind == tudresden.ocl20.core.jmi.ocl.expressions.CollectionKindEnum.SET) {
            type = (Classifier) elementType.getSetType();
        }
        if (kind == tudresden.ocl20.core.jmi.ocl.expressions.CollectionKindEnum.BAG) {
            type = (Classifier) elementType.getBagType();
        }
        if (kind == tudresden.ocl20.core.jmi.ocl.expressions.CollectionKindEnum.SEQUENCE) {
            type = (Classifier) elementType.getSequenceType();
        }
        exp.setType(type);
    }

    public void evaluate(CollectionItem ci) throws WellFormednessException {
        OclExpression item = ci.getItem();
        if (item == null) {
            throw new WellFormednessException(ci, WellFormednessException.EC_NO_COLLECTION_ITEM);
        }
        ci.setType(getType(item));
    }

    public void evaluate(CollectionRange cr) throws WellFormednessException {
        OclExpression first = cr.getFirst();
        if (first == null) {
            throw new WellFormednessException(cr, WellFormednessException.EC_COLL_RANGE_NO_FIRST);
        }
        OclExpression last = cr.getLast();
        if (last == null) {
            throw new WellFormednessException(cr, WellFormednessException.EC_COLL_RANGE_NO_LAST);
        }
        Classifier intType = oclLib.getOclInteger();
        Classifier typeFirst = getType(first);
        Classifier typeLast = getType(last);
        if (!typeFirst.equals(intType) || !typeLast.equals(intType)) {
            throw new WellFormednessException(cr, WellFormednessException.EC_COLL_RANGE_INT);
        }
        cr.setType(intType);
    }

    public void evaluate(EnumLiteralExp exp) throws WellFormednessException {
        EnumerationLiteral el = exp.getReferredEnumLiteral();
        if (el == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_ENUMLITERAL);
        }
        exp.setType(el.getEnumerationA());
    }

    public void evaluate(IfExp exp) throws WellFormednessException {
        OclExpression cond = exp.getCondition();
        if (cond == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_CONDITION);
        }
        if (!getType(cond).equals(oclLib.getOclBoolean())) {
            throw new WellFormednessException(exp, WellFormednessException.EC_CONDITION_NOT_BOOL);
        }
        OclExpression thenExp = exp.getThenExpression();
        if (thenExp == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_THEN_EXP);
        }
        OclExpression elseExp = exp.getElseExpression();
        if (elseExp == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_ELSE_EXP);
        }
        exp.setType(getType(thenExp).commonSuperType(getType(elseExp)));
    }

    public void evaluate(IntegerLiteralExp exp) {
        Classifier type = oclLib.getOclInteger();
        exp.setType(type);
    }

    private Classifier evaluateBodyType(LoopExp exp) throws WellFormednessException {
        OclExpression body = exp.getBody();
        if (body == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_BODY_EXP);
        }
        return getType(body);
    }

    private void evaluateIteratorTypes(LoopExp exp) throws WellFormednessException {
        Classifier srcType = evaluateSourceType(exp);
        if (!(srcType instanceof CollectionType)) {
            throw new WellFormednessException(exp, WellFormednessException.EC_SRC_NOT_COLLECTION);
        }
        Classifier elementType = ((CollectionType) srcType).getElementType();
        if (exp.getIterators().size() == 0) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_ITERATOR_VARS);
        }
        Iterator it = exp.getIterators().iterator();
        while (it.hasNext()) {
            VariableDeclaration vd = (VariableDeclaration) it.next();
            if (vd.getInitExpression() != null) {
                throw new WellFormednessException(vd, WellFormednessException.EC_ILLEGAL_ITERATOR_VAR_INIT);
            }
            Classifier vdType = vd.getType();
            if (vdType == null) {
                vd.setType(elementType);
            } else {
                if (vdType != elementType) {
                    throw new WellFormednessException(vd, WellFormednessException.EC_ITERATOR_VAR_WRONG_TYPE);
                }
            }
        }
    }

    public void evaluate(IteratorExp exp) throws WellFormednessException {
        evaluateIteratorTypes(exp);
        Classifier type = null;
        Classifier srcType = evaluateSourceType(exp);
        Classifier elementType = ((CollectionType) srcType).getElementType();
        Classifier bodyType = evaluateBodyType(exp);
        String name = exp.getNameA();
        if (name.equals("exists") || name.equals("forAll") || name.equals("select") || name.equals("reject") || name.equals("any")) {
            if (bodyType != oclLib.getOclBoolean()) {
                throw new WellFormednessException(exp, WellFormednessException.EC_BODY_NOT_BOOL);
            }
        }
        if (name.equals("exists") || name.equals("forAll") || name.equals("isUnique") || name.equals("one")) {
            type = oclLib.getOclBoolean();
        } else if (name.equals("any")) {
            type = elementType;
        } else if (name.equals("select") || name.equals("reject")) {
            type = srcType;
        } else if (name.equals("sortedBy")) {
            if (srcType instanceof SetType) {
                type = elementType.getSequenceType();
            } else {
                type = elementType.getSequenceType();
            }
        } else if (name.equals("collectNested")) {
            if (srcType instanceof SequenceType) {
                type = bodyType.getSequenceType();
            } else {
                type = bodyType.getBagType();
            }
        } else if (name.equals("collect")) {
            if (DEEPFLATTENING) {
                Classifier flatElementType;
                if (bodyType instanceof CollectionType) {
                    flatElementType = ((CollectionType) bodyType).getFlatElementType();
                } else {
                    flatElementType = bodyType;
                }
                if (srcType instanceof SequenceType) {
                    type = flatElementType.getSequenceType();
                } else {
                    type = flatElementType.getBagType();
                }
            } else {
                Classifier resultElementType;
                if (bodyType instanceof CollectionType) {
                    resultElementType = ((CollectionType) bodyType).getElementType();
                } else {
                    resultElementType = bodyType;
                }
                if (srcType instanceof SequenceType) {
                    type = resultElementType.getSequenceType();
                } else {
                    type = resultElementType.getBagType();
                }
            }
        } else {
            throw new WellFormednessException(exp, WellFormednessException.EC_UNKNOWN_ITERATOR_EXP);
        }
        exp.setType(type);
    }

    public void evaluate(IterateExp exp) throws WellFormednessException {
        evaluateIteratorTypes(exp);
        Classifier bodyType = evaluateBodyType(exp);
        VariableDeclaration result = exp.getResult();
        if (result == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_ACCUMUMLATOR_VAR);
        }
        OclExpression init = result.getInitExpression();
        if (init == null) {
            throw new WellFormednessException(result, WellFormednessException.EC_NO_INIT_EXP);
        }
        Classifier initType = getType(init);
        Classifier resultType = result.getType();
        if (resultType == null) {
            throw new WellFormednessException(result, WellFormednessException.EC_ACCU_VAR_NO_TYPE);
        } else {
            if (!initType.conformsTo(resultType)) {
                throw new WellFormednessException(result, WellFormednessException.EC_WRONG_VAR_INIT_TYPE);
            }
            if (!bodyType.conformsTo(resultType)) {
                throw new WellFormednessException(exp, WellFormednessException.EC_WRONG_BODY_TYPE);
            }
        }
        exp.setType(resultType);
    }

    public void evaluate(LetExp exp) throws WellFormednessException {
        OclExpression inExp = exp.getIn();
        if (inExp == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_IN_EXP);
        }
        VariableDeclaration vd = exp.getVariable();
        if (vd == null) {
            throw new WellFormednessException(vd, WellFormednessException.EC_NO_VARIABLE);
        }
        evaluateInitializedVariableDecl(vd);
        exp.setType(getType(inExp));
    }

    private void evaluateInitializedVariableDecl(VariableDeclaration vd) throws WellFormednessException {
        OclExpression initExp = vd.getInitExpression();
        if (initExp == null) {
            throw new WellFormednessException(vd, WellFormednessException.EC_NO_INIT_EXP);
        }
        Classifier initExpType = getType(initExp);
        if (vd.getType() == null) {
            vd.setType(initExpType);
        } else if (!initExpType.conformsTo(vd.getType())) {
            throw new WellFormednessException(vd, WellFormednessException.EC_WRONG_VAR_INIT_TYPE);
        }
    }

    private List evaluateOclMessageArgumentTypes(OclMessageExp exp) throws WellFormednessException {
        List types = new ArrayList();
        Iterator it = exp.getArguments().iterator();
        while (it.hasNext()) {
            OclMessageArg oma = (OclMessageArg) it.next();
            OclExpression argExp = oma.getExpression();
            UnspecifiedValueExp argUnspec = oma.getUnspecified();
            if (!(argExp != null ^ argUnspec != null)) {
                throw new WellFormednessException(oma, WellFormednessException.EC_INVALID_MSG_ARG);
            }
            Classifier type = null;
            if (argUnspec != null) {
                type = argUnspec.getType();
                if (type == null) {
                    throw new WellFormednessException(argUnspec, WellFormednessException.EC_UNSPECIFIED_NO_TYPE);
                }
            } else {
                type = getType(argExp);
            }
            types.add(type);
        }
        return types;
    }

    public void evaluate(OclMessageExp exp) throws WellFormednessException {
        List argTypes = evaluateOclMessageArgumentTypes(exp);
        Classifier type = null;
        Signal sentSignal = exp.getSentSignal();
        Operation calledOp = exp.getCalledOperation();
        OclExpression target = exp.getTarget();
        if (target == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_TARGET);
        }
        Classifier targetType = getType(target);
        if (targetType instanceof CollectionType) {
            throw new WellFormednessException(exp, WellFormednessException.EC_ILLEGAL_TARGET);
        }
        if (!(calledOp != null ^ sentSignal != null)) {
            throw new WellFormednessException(exp, WellFormednessException.EC_OP_OR_SIGNAL);
        }
        if (calledOp != null) {
            type = oclLib.makeOclMessageType(calledOp);
            if (!calledOp.hasMatchingSignature(argTypes)) {
                throw new WellFormednessException(exp, WellFormednessException.EC_PARAMETER_MISMATCH);
            }
            if (targetType.allOperations().contains(calledOp)) {
                throw new WellFormednessException(exp, WellFormednessException.EC_NOT_OP_OF_TARGET);
            }
        } else if (sentSignal != null) {
            type = oclLib.makeOclMessageType(sentSignal);
            if (!sentSignal.hasMatchingSignature(argTypes)) {
                throw new WellFormednessException(exp, WellFormednessException.EC_SIGNAL_ATTR_MISMATCH);
            }
        }
        exp.setType(type);
    }

    public void evaluate(OclOperationWithTypeArgExp exp) throws WellFormednessException {
        Classifier srcType = evaluateSourceType(exp);
        Classifier typeArg = exp.getTypeArgument();
        if (typeArg == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_TYPE_ARG);
        }
        Classifier type = null;
        if ("oclAsType".equals(exp.getNameA())) {
            type = typeArg;
            if (!type.conformsTo(srcType) && !srcType.conformsTo(type)) {
                throw new WellFormednessException(exp, WellFormednessException.EC_WRONG_TYPE_ARG);
            }
        } else if ("oclIsTypeOf".equals(exp.getNameA()) || "oclIsKindOf".equals(exp.getNameA())) {
            type = oclLib.getOclBoolean();
        } else {
            throw new WellFormednessException(exp, WellFormednessException.EC_UNKNOWN_TYPE_ARG_EXP);
        }
        exp.setType(type);
    }

    private List evaluateArgumentTypes(OperationCallExp exp) throws WellFormednessException {
        List result = new ArrayList();
        Iterator it = exp.getArguments().iterator();
        while (it.hasNext()) {
            OclExpression arg = (OclExpression) it.next();
            result.add(getType(arg));
        }
        return result;
    }

    public void evaluate(OperationCallExp exp) throws WellFormednessException {
        Operation op = (Operation) exp.getReferredOperation();
        if (op == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_OPERATION);
        }
        Classifier srcType;
        if (exp.getSource() != null || op.isInstanceLevelA()) {
            srcType = evaluateSourceType(exp);
        } else {
            srcType = exp.getSrcType();
            if (srcType == null) {
                throw new WellFormednessException(exp, WellFormednessException.EC_NO_SRCTYPE);
            }
        }
        Classifier type = null;
        List argTypes = evaluateArgumentTypes(exp);
        if (!op.hasMatchingSignature(argTypes)) {
            throw new WellFormednessException(exp, WellFormednessException.EC_PARAMETER_MISMATCH);
        }
        String name = op.getNameA();
        if (name.equals("allInstances")) {
            if (srcType instanceof Primitive || srcType instanceof CollectionType || srcType instanceof TupleType || srcType instanceof OclMessageType) {
                throw new WellFormednessException(exp, WellFormednessException.EC_INVALID_ALLINSTANCES);
            }
            type = srcType.getSetType();
        } else if (name.equals("product") && srcType instanceof CollectionType) {
            CollectionType c2 = (CollectionType) getType((OclExpression) exp.getArguments().get(0));
            List names = new ArrayList();
            List types = new ArrayList();
            names.add("first");
            names.add("second");
            types.add(((CollectionType) getType(exp.getSource())).getElementType());
            types.add(c2.getElementType());
            type = oclLib.makeTupleType(names, types).getSetType();
        } else {
            List outparams = op.getOutParametersA();
            Parameter retParam = op.getReturnParameterA();
            if (outparams.isEmpty() && retParam == null) {
            } else if (outparams.isEmpty()) {
                type = retParam.getTypeA();
            } else {
                Iterator it = outparams.iterator();
                List asAttributes = new ArrayList();
                while (it.hasNext()) {
                    asAttributes.add(((Parameter) it.next()).asAttribute());
                }
                if (retParam != null) {
                    asAttributes.add(retParam.asAttribute());
                }
                type = oclLib.makeTupleType(asAttributes);
            }
        }
        exp.setType(type);
    }

    public void evaluate(RealLiteralExp exp) {
        Classifier type = oclLib.getOclReal();
        exp.setType(type);
    }

    public void evaluate(StringLiteralExp exp) {
        Classifier type = oclLib.getOclString();
        exp.setType(type);
    }

    public void evaluate(TupleLiteralExp exp) throws WellFormednessException {
        Set attributeNames = new HashSet();
        List attributes = new ArrayList();
        Iterator it = exp.getTuplePart().iterator();
        while (it.hasNext()) {
            VariableDeclaration vd = (VariableDeclaration) it.next();
            evaluateInitializedVariableDecl(vd);
            if (attributeNames.contains(vd.getNameA())) {
                throw new WellFormednessException(exp, WellFormednessException.EC_TUPLE_PART_NAMES_NOT_UNIQUE);
            }
            attributeNames.add(vd.getNameA());
            attributes.add(vd.asAttribute());
        }
        Classifier type = oclLib.makeTupleType(attributes);
        exp.setType(type);
    }

    /** Never call directly. Use {@link #getType getType} instead. */
    public void evaluate(VariableExp exp) throws WellFormednessException {
        VariableDeclaration vd = exp.getReferredVariable();
        if (vd == null) {
            throw new WellFormednessException(exp, WellFormednessException.EC_NO_VARIABLE);
        }
        Classifier type = vd.getType();
        if (type == null) {
            throw new WellFormednessException(vd, WellFormednessException.EC_NO_VAR_TYPE);
        }
        exp.setType(type);
    }
}
