package org.wsml.reasoner.builtin.elly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.AttributeConstraintMolecule;
import org.omwg.logicalexpression.AttributeInferenceMolecule;
import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.BuiltInAtom;
import org.omwg.logicalexpression.CompoundMolecule;
import org.omwg.logicalexpression.Conjunction;
import org.omwg.logicalexpression.Constraint;
import org.omwg.logicalexpression.Disjunction;
import org.omwg.logicalexpression.Equivalence;
import org.omwg.logicalexpression.ExistentialQuantification;
import org.omwg.logicalexpression.Implication;
import org.omwg.logicalexpression.InverseImplication;
import org.omwg.logicalexpression.LogicProgrammingRule;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.LogicalExpressionVisitor;
import org.omwg.logicalexpression.MembershipMolecule;
import org.omwg.logicalexpression.Negation;
import org.omwg.logicalexpression.NegationAsFailure;
import org.omwg.logicalexpression.SubConceptMolecule;
import org.omwg.logicalexpression.TruthValue;
import org.omwg.logicalexpression.UniversalQuantification;
import org.omwg.logicalexpression.terms.ConstructedTerm;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.logicalexpression.terms.TermVisitor;
import org.omwg.ontology.ComplexDataValue;
import org.omwg.ontology.SimpleDataValue;
import org.omwg.ontology.Variable;
import org.sti2.elly.DataType;
import org.sti2.elly.Vocabulary;
import org.sti2.elly.api.basics.IAtom;
import org.sti2.elly.api.basics.IAtomicConcept;
import org.sti2.elly.api.basics.IAtomicRole;
import org.sti2.elly.api.basics.IConceptDescription;
import org.sti2.elly.api.basics.IDescription;
import org.sti2.elly.api.basics.IRoleDescription;
import org.sti2.elly.api.basics.IRule;
import org.sti2.elly.api.basics.ITuple;
import org.sti2.elly.api.factory.IBasicFactory;
import org.sti2.elly.api.factory.IBuiltinsFactory;
import org.sti2.elly.api.factory.ITermFactory;
import org.sti2.elly.api.terms.IConcreteTerm;
import org.sti2.elly.api.terms.IIndividual;
import org.sti2.elly.api.terms.ITerm;
import org.sti2.elly.api.terms.IVariable;
import org.sti2.elly.basics.BasicFactory;
import org.sti2.elly.basics.BuiltinsFactory;
import org.sti2.elly.terms.TermFactory;
import org.sti2.elly.transformation.factory.AbstractFactory;
import org.wsmo.common.BuiltIn;
import org.wsmo.common.IRI;
import org.wsmo.common.NumberedAnonymousID;
import org.wsmo.common.UnnumberedAnonymousID;

public class Wsml2EllyTranslator implements LogicalExpressionVisitor, TermVisitor {

    /**
	 * This enum allows to specify the expected type. When visiting a term, the type must be set such that a concept,
	 * role, or term can be created.
	 */
    private enum Type {

        CONCEPT("Concept Description"), DATA_TYPE("Datatype"), ROLE("Role Description"), TERM("Term");

        private String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static IBuiltinsFactory BUILTIN = BuiltinsFactory.getInstance();

    private static IBasicFactory BASIC = BasicFactory.getInstance();

    private static ITermFactory TERM = TermFactory.getInstance();

    static final IVariable varX = TERM.createVariable("x", false);

    static final IVariable varY = TERM.createVariable("y", false);

    static final IVariable varZ = TERM.createVariable("z", false);

    static final ITuple tupleX = BASIC.createTuple(varX);

    static final ITuple tupleY = BASIC.createTuple(varY);

    static final ITuple tupleXY = BASIC.createTuple(varX, varY);

    static final ITuple tupleXZ = BASIC.createTuple(varX, varZ);

    static final ITuple tupleYX = BASIC.createTuple(varY, varX);

    static final ITuple tupleYZ = BASIC.createTuple(varY, varZ);

    static final ITuple tupleXX = BASIC.createTuple(varX, varX);

    private final List<IRule> rules;

    private final Stack<ITerm> termStack;

    private final Stack<IDescription> descriptionStack;

    private final Stack<Queue<IAtom>> atomListStack;

    private final Map<Object, Object> wsml2EllyCache;

    private Type expectedType;

    public Wsml2EllyTranslator(List<IRule> rules) {
        if (rules == null) throw new IllegalArgumentException("rules must not be null");
        this.rules = rules;
        termStack = new Stack<ITerm>();
        descriptionStack = new Stack<IDescription>();
        atomListStack = new Stack<Queue<IAtom>>();
        wsml2EllyCache = new HashMap<Object, Object>();
    }

    @Override
    public void visit(ConstructedTerm t) {
        throw new UnsupportedOperationException("ConstructedTerms are not supported by ELP");
    }

    @Override
    public void visit(Variable t) {
        if (expectedType != Type.TERM) throw new RuntimeException("Unable to create a " + expectedType + " from Term " + t);
        IVariable eVariable = getOrCreateVariable(t);
        pushTerm(eVariable);
    }

    @Override
    public void visit(SimpleDataValue t) {
        if (expectedType != Type.TERM) throw new RuntimeException("Unable to create a " + expectedType + " from Term " + t);
        IConcreteTerm dataValue = Wsml2EllyDataValueTranslator.convertWsmo4jDataValueToEllyTerm(t);
        pushTerm(dataValue);
    }

    @Override
    public void visit(ComplexDataValue t) {
        if (expectedType != Type.TERM) throw new RuntimeException("Unable to create a " + expectedType + " from Term " + t);
        IConcreteTerm dataValue = Wsml2EllyDataValueTranslator.convertWsmo4jDataValueToEllyTerm(t);
        pushTerm(dataValue);
    }

    @Override
    public void visit(UnnumberedAnonymousID t) {
        String anonymousName = NameFactory.getAnonymousName();
        switch(expectedType) {
            case CONCEPT:
                pushDescription(BASIC.createAtomicConcept(anonymousName));
                break;
            case ROLE:
                pushDescription(BASIC.createAtomicRole(anonymousName));
                break;
            case TERM:
                pushTerm(TERM.createIndividual(anonymousName));
                break;
            case DATA_TYPE:
                throw new RuntimeException("Unable to create a " + expectedType + " from Term " + t);
            default:
                throw new RuntimeException("Unable to handle Type " + expectedType);
        }
    }

    @Override
    public void visit(NumberedAnonymousID t) {
        String anonymousName = NameFactory.getAnonymousName(t.getNumber());
        switch(expectedType) {
            case CONCEPT:
                pushDescription(BASIC.createAtomicConcept(anonymousName));
                break;
            case ROLE:
                pushDescription(BASIC.createAtomicRole(anonymousName));
                break;
            case TERM:
                pushTerm(TERM.createIndividual(anonymousName));
                break;
            case DATA_TYPE:
                throw new RuntimeException("Unable to create a " + expectedType + " from Term " + t);
            default:
                throw new RuntimeException("Unable to handle Type " + expectedType);
        }
    }

    @Override
    public void visit(IRI t) {
        switch(expectedType) {
            case CONCEPT:
                pushDescription(getOrCreateConcept(t));
                break;
            case ROLE:
                pushDescription(getOrCreateRole(t));
                break;
            case TERM:
                pushTerm(getOrCreateIndividual(t));
                break;
            case DATA_TYPE:
                DataType dataType = DataType.asDataType(t.toString());
                if (dataType == null) {
                    throw new RuntimeException("DataType " + t.toString() + " not supported");
                }
                IDescription dataTypeConcept = dataType.asConcept();
                pushDescription(dataTypeConcept);
                break;
            default:
                throw new RuntimeException("Unable to handle Type " + expectedType);
        }
    }

    @Override
    public void visitAtom(Atom expr) {
        if (expr instanceof BuiltInAtom) {
            IAtom builtin = translateBuiltIn((BuiltInAtom) expr);
            if (emptyStack()) {
                rules.add(BASIC.createFact(builtin));
            } else {
                addAtom(builtin);
            }
        } else {
            throw new UnsupportedOperationException("Atoms are not supported by ELP");
        }
    }

    /**
	 * AttributeConstraintMolecule represents a constraining attribute molecule (e.g. human[age ofType _integer)).
	 * Creates and adds a rule {@code DT(y) :- C(x) and r(x,y)} from {@code C[r ofType DT]}.
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitAttributeConstraintMolecule(org.omwg.logicalexpression.AttributeConstraintMolecule)
	 */
    @Override
    public void visitAttributeConstraintMolecule(AttributeConstraintMolecule expr) {
        assert emptyStack();
        expectedType = Type.ROLE;
        expr.getAttribute().accept(this);
        IDescription id2 = popDescription();
        expectedType = Type.CONCEPT;
        expr.getLeftParameter().accept(this);
        IDescription id1 = popDescription();
        expectedType = Type.DATA_TYPE;
        expr.getRightParameter().accept(this);
        IDescription dt = popDescription();
        List<IAtom> head = new ArrayList<IAtom>();
        List<IAtom> body = new ArrayList<IAtom>();
        body.add(BASIC.createAtom(id1, tupleX));
        body.add(BASIC.createAtom(id2, tupleXY));
        head.add(BASIC.createAtom(dt, tupleY));
        rules.add(BASIC.createRule(head, body));
    }

    /**
	 * AttributeInferenceMolecule Represents an inferring attribute molecule (e.g. human[ancestor impliesType human]).
	 * Creates and adds a rule {@code DT(y) :- C(x) and r(x,y)} from {@code C[r impliesType DT]}.
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitAttributeInferenceMolecule(org.omwg.logicalexpression.AttributeInferenceMolecule)
	 */
    @Override
    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        assert emptyStack();
        expectedType = Type.ROLE;
        expr.getAttribute().accept(this);
        IDescription id2 = popDescription();
        expectedType = Type.CONCEPT;
        expr.getLeftParameter().accept(this);
        IDescription id1 = popDescription();
        expectedType = Type.CONCEPT;
        expr.getRightParameter().accept(this);
        IDescription id3 = popDescription();
        List<IAtom> head = new ArrayList<IAtom>();
        List<IAtom> body = new ArrayList<IAtom>();
        body.add(BASIC.createAtom(id1, tupleX));
        body.add(BASIC.createAtom(id2, tupleXY));
        head.add(BASIC.createAtom(id3, tupleY));
        rules.add(BASIC.createRule(head, body));
    }

    /**
	 * AttributeValueMolecule Represents a value attribute molecule (e.g. human[age hasValue 4]). Creates and adds
	 * a fact {@code id2(X1,X2).}
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
	 */
    @Override
    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        expectedType = Type.ROLE;
        expr.getAttribute().accept(this);
        IRoleDescription id2 = (IRoleDescription) popDescription();
        expectedType = Type.TERM;
        expr.getLeftParameter().accept(this);
        ITerm x1 = popTerm();
        expectedType = Type.TERM;
        expr.getRightParameter().accept(this);
        ITerm x2 = popTerm();
        IAtom atom = null;
        if ((x1 instanceof IVariable) && (x1.equals(x2))) {
            ITuple tuple = BASIC.createTuple(x1);
            atom = BASIC.createAtom(BASIC.createSelfRestriction(id2), tuple);
        } else {
            ITuple tuple = BASIC.createTuple(x1, x2);
            atom = BASIC.createAtom(id2, tuple);
        }
        if (emptyStack()) {
            rules.add(BASIC.createFact(atom));
        } else {
            addAtom(atom);
        }
    }

    /**
	 * CompoundMolecule Represents a compound molecule which is a container for simple molecules E.g.,
	 * "x subConceptOf {y,z}" or "a[b hasValue c] memberOf d". Visits all {@link CompoundMolecule#listOperands()}
	 * Molecules, since all other {@code list} methods are just filters on all operands.
	 * 
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitCompoundMolecule(org.omwg.logicalexpression.CompoundMolecule)
	 */
    @Override
    public void visitCompoundMolecule(CompoundMolecule expr) {
        pushAtomList();
        for (LogicalExpression molecule : expr.listOperands()) {
            molecule.accept(this);
        }
        if (emptyStack()) {
            rules.add(BASIC.createFact(new ArrayList<IAtom>(popAtomList())));
        } else {
            peekAtomList().addAll(popAtomList());
        }
    }

    /**
	 * Conjunction represents specific kind of <code>Binary</code>. A conjunction whose operator is an and.
	 * Both Operands are visited and the resulting Descriptions are left on the
	 * {@link #atomStack}.
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitConjunction(org.omwg.logicalexpression.Conjunction)
	 */
    @Override
    public void visitConjunction(Conjunction expr) {
        pushAtomList();
        expr.getLeftOperand().accept(this);
        pushAtomList();
        expr.getRightOperand().accept(this);
        Queue<IAtom> atomListR = popAtomList();
        Queue<IAtom> atomListL = popAtomList();
        Queue<IAtom> atomList = new LinkedList<IAtom>();
        boolean createIntersection = true;
        createIntersection &= atomListR.size() == 1;
        IAtom atomL = null;
        for (IAtom atom : atomListL) {
            atomL = atom;
        }
        IAtom atomR = atomListR.element();
        createIntersection &= atomL.getTuple().equals(atomR.getTuple());
        if (!createIntersection) {
            atomList.addAll(atomListL);
            atomList.addAll(atomListR);
        } else {
            while (atomListL.size() > 1) {
                atomList.add(atomListL.remove());
            }
            assert atomListL.remove().equals(atomL);
            if (atomL.getDescription() instanceof IConceptDescription) {
                IDescription conceptIntersection = BASIC.createIntersectionConcept((IConceptDescription) atomL.getDescription(), (IConceptDescription) atomR.getDescription());
                atomList.add(BASIC.createAtom(conceptIntersection, atomL.getTuple()));
            } else if (atomL.getDescription() instanceof IRoleDescription) {
                IDescription roleIntersection = BASIC.createIntersectionRole((IRoleDescription) atomL.getDescription(), (IRoleDescription) atomR.getDescription());
                atomList.add(BASIC.createAtom(roleIntersection, atomL.getTuple()));
            }
        }
        if (emptyStack()) {
            rules.add(BASIC.createFact(new ArrayList<IAtom>(atomList)));
        } else {
            peekAtomList().addAll(atomList);
        }
    }

    @Override
    public void visitConstraint(Constraint expr) {
        assert emptyStack();
        pushAtomList();
        expr.getOperand().accept(this);
        Queue<IAtom> atomList = popAtomList();
        assert emptyStack();
        assert atomList.size() > 0;
        rules.add(BASIC.createIntegrityConstraint(new ArrayList<IAtom>(atomList)));
    }

    @Override
    public void visitDisjunction(Disjunction expr) {
        throw new UnsupportedOperationException("Disjunction is not supported by ELP");
    }

    @Override
    public void visitEquivalence(Equivalence expr) {
        visitImplication(expr.getLeftOperand(), expr.getRightOperand());
        visitImplication(expr.getRightOperand(), expr.getLeftOperand());
    }

    /**
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitExistentialQuantification(org.omwg.logicalexpression.ExistentialQuantification)
	 */
    @Override
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        pushAtomList();
        expr.getOperand().accept(this);
        Queue<IAtom> atomList = popAtomList();
        IAtom roleAtom = atomList.remove();
        IAtom conceptAtom = atomList.remove();
        assert atomList.isEmpty();
        if (!(conceptAtom.getDescription() instanceof IConceptDescription)) throw new IllegalArgumentException("ExistentialQuantification " + expr.toString() + " must be of syntax exists \\phi (G and F_1) with \\phi \\in V_V, G a b-molecule, F_1 an a-molecule");
        if (!(roleAtom.getDescription() instanceof IRoleDescription)) throw new IllegalArgumentException("ExistentialQuantification " + expr.toString() + " must be of syntax exists \\phi (G and F_1) with \\phi \\in V_V, G a b-molecule, F_1 an a-molecule");
        if (!(conceptAtom.getTuple().get(0).equals(roleAtom.getTuple().get(1)))) throw new IllegalArgumentException("ExistentialQuantification " + expr.toString() + " must be of syntax exists \\phi (G and F_1) with \\phi \\in V_V, G a b-molecule, F_1 an a-molecule");
        if (expr.listVariables().size() != 1) throw new IllegalArgumentException("ExistentialQuantification " + expr.toString() + " must be of syntax exists \\phi (G and F_1) with \\phi \\in V_V, G a b-molecule, F_1 an a-molecule");
        IConceptDescription concept = (IConceptDescription) conceptAtom.getDescription();
        IRoleDescription role = (IRoleDescription) roleAtom.getDescription();
        IConceptDescription existential = BASIC.createExistentialConcept(role, concept);
        ITuple tuple = BASIC.createTuple(roleAtom.getTuple().get(0));
        IAtom atom = BASIC.createAtom(existential, tuple);
        if (emptyStack()) {
            rules.add(BASIC.createFact(atom));
        } else {
            addAtom(atom);
        }
    }

    @Override
    public void visitImplication(Implication expr) {
        visitImplication(expr.getRightOperand(), expr.getLeftOperand());
    }

    @Override
    public void visitInverseImplication(InverseImplication expr) {
        visitImplication(expr.getLeftOperand(), expr.getRightOperand());
    }

    @Override
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        visitImplication(expr.getLeftOperand(), expr.getRightOperand());
    }

    private void visitImplication(LogicalExpression headExpression, LogicalExpression bodyExpression) {
        assert emptyStack();
        pushAtomList();
        headExpression.accept(this);
        pushAtomList();
        bodyExpression.accept(this);
        List<IAtom> body = new ArrayList<IAtom>(popAtomList());
        List<IAtom> head = new ArrayList<IAtom>(popAtomList());
        assert head.size() > 0;
        assert body.size() > 0;
        assert emptyStack();
        rules.add(BASIC.createRule(head, body));
    }

    /**
	 * MembershipMolecule Represents a molecule of the form "a memberOf b". Creates and pushes an Atom
	 * <code>b(a)</code>.
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitMemberShipMolecule(org.omwg.logicalexpression.MembershipMolecule)
	 */
    @Override
    public void visitMemberShipMolecule(MembershipMolecule expr) {
        expectedType = Type.TERM;
        expr.getLeftParameter().accept(this);
        ITerm x1 = popTerm();
        expectedType = Type.CONCEPT;
        expr.getRightParameter().accept(this);
        IDescription id2 = popDescription();
        IAtom atom = BASIC.createAtom(id2, BASIC.createTuple(x1));
        if (emptyStack()) {
            rules.add(BASIC.createFact(atom));
        } else {
            addAtom(atom);
        }
    }

    @Override
    public void visitNegation(Negation expr) {
        throw new UnsupportedOperationException("Negation is not supported by ELP");
    }

    @Override
    public void visitNegationAsFailure(NegationAsFailure expr) {
        throw new UnsupportedOperationException("NegationAsFailure is not supported by ELP");
    }

    /**
	 * SubConceptMolecule Represents a Molecule of the Form "a subConceptOf b".
	 * 
	 * id1(x)
	 * 
	 * @see org.omwg.logicalexpression.LogicalExpressionVisitor#visitSubConceptMolecule(org.omwg.logicalexpression.SubConceptMolecule)
	 */
    @Override
    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        assert emptyStack();
        expectedType = Type.CONCEPT;
        expr.getLeftParameter().accept(this);
        IDescription id1_subConcept = popDescription();
        expectedType = Type.CONCEPT;
        expr.getRightParameter().accept(this);
        IDescription id2_superConcept = popDescription();
        List<IAtom> head = Collections.singletonList(BASIC.createAtom(id2_superConcept, tupleX));
        List<IAtom> body = Collections.singletonList(BASIC.createAtom(id1_subConcept, tupleX));
        rules.add(BASIC.createRule(head, body));
    }

    @Override
    public void visitTruthValue(TruthValue expr) {
        IAtom atom;
        if (expr.getValue()) atom = BASIC.createAtom(Vocabulary.topConcept, tupleX); else atom = BASIC.createAtom(Vocabulary.bottomConcept, tupleX);
        if (emptyStack()) {
            rules.add(BASIC.createFact(atom));
        } else {
            addAtom(atom);
        }
    }

    @Override
    public void visitUniversalQuantification(UniversalQuantification expr) {
        throw new UnsupportedOperationException("UniversalQuantification is not supported by ELP");
    }

    private String asString(Term term) {
        return term.toString();
    }

    private IAtomicConcept getOrCreateConcept(Term concept) {
        IAtomicConcept ellyConcept = (IAtomicConcept) wsml2EllyCache.get(concept);
        if (ellyConcept == null) {
            ellyConcept = BASIC.createAtomicConcept(asString(concept));
            wsml2EllyCache.put(concept, ellyConcept);
        }
        return ellyConcept;
    }

    private IAtomicRole getOrCreateRole(Term role) {
        IAtomicRole ellyRole = (IAtomicRole) wsml2EllyCache.get(role);
        if (ellyRole == null) {
            ellyRole = BASIC.createAtomicRole(asString(role));
            wsml2EllyCache.put(role, ellyRole);
        }
        return ellyRole;
    }

    private IIndividual getOrCreateIndividual(Term term) {
        IIndividual ellyIndividual = (IIndividual) wsml2EllyCache.get(term);
        if (ellyIndividual == null) {
            ellyIndividual = TERM.createIndividual(asString(term));
            wsml2EllyCache.put(term, ellyIndividual);
        }
        return ellyIndividual;
    }

    private IVariable getOrCreateVariable(Variable variable) {
        IVariable ellyVariable = (IVariable) wsml2EllyCache.get(variable);
        if (ellyVariable == null) {
            ellyVariable = TERM.createVariable(asString(variable), variable.isSafe());
            wsml2EllyCache.put(variable, ellyVariable);
        }
        return ellyVariable;
    }

    private ITerm pushTerm(ITerm term) {
        return termStack.push(term);
    }

    private ITerm popTerm() {
        return termStack.pop();
    }

    private IDescription pushDescription(IDescription description) {
        return descriptionStack.push(description);
    }

    private IDescription popDescription() {
        return descriptionStack.pop();
    }

    private boolean addAtom(IAtom atom) {
        return atomListStack.peek().add(atom);
    }

    private Queue<IAtom> pushAtomList() {
        return atomListStack.push(new LinkedList<IAtom>());
    }

    private Queue<IAtom> popAtomList() {
        return atomListStack.pop();
    }

    private Queue<IAtom> peekAtomList() {
        return atomListStack.peek();
    }

    private boolean emptyStack() {
        return atomListStack.isEmpty();
    }

    private static class NameFactory extends AbstractFactory {

        private static final String anonymousNamePrefix = ellyPrefix + "__anonymous__";

        private NameFactory() {
        }

        public static String getAnonymousName(byte anonymousNumber) {
            return anonymousNamePrefix + anonymousNumber;
        }

        public static String getAnonymousName() {
            return anonymousNamePrefix + getCounter();
        }
    }

    private IAtom translateBuiltIn(BuiltInAtom builtInAtom) {
        BuiltIn builtIn = BuiltIn.from(builtInAtom.getIdentifier().toString());
        expectedType = Type.TERM;
        List<ITerm> terms = new ArrayList<ITerm>();
        for (Term term : builtInAtom.listParameters()) {
            term.accept(this);
            terms.add(popTerm());
        }
        if (builtIn.equals(BuiltIn.EQUAL) && (terms.get(0) instanceof IIndividual)) {
            assert (terms.get(1) instanceof IIndividual);
            return BASIC.createAtom(Vocabulary.equal, terms.get(0), terms.get(1));
        }
        List<ITerm> sortedTerms = sortListForIRIS(terms);
        assert terms.size() >= 1;
        assert terms.size() <= 2;
        switch(builtIn) {
            case EQUAL:
            case NUMERIC_EQUAL:
            case STRING_EQUAL:
            case DATE_EQUAL:
            case TIME_EQUAL:
            case DATETIME_EQUAL:
            case GYEAR_EQUAL:
            case GYEARMONTH_EQUAL:
            case GMONTHDAY_EQUAL:
            case GDAY_EQUAL:
            case GMONTH_EQUAL:
            case DURATION_EQUAL:
                return BUILTIN.createEqual(terms.get(0), terms.get(1));
            case INEQUAL:
            case NUMERIC_INEQUAL:
            case STRING_INEQUAL:
            case DATE_INEQUAL:
            case TIME_INEQUAL:
            case DATETIME_INEQUAL:
                return BUILTIN.createUnequal(terms.get(0), terms.get(1));
            case LESS_THAN:
            case DATE_LESS_THAN:
            case TIME_LESS_THAN:
            case DATETIME_LESS_THAN:
            case DAYTIMEDURATION_LESS_THAN:
            case YEARMONTHDURATION_LESS_THAN:
                return BUILTIN.createLess(terms.get(0), terms.get(1));
            case LESS_EQUAL:
                return BUILTIN.createLessEqual(terms.get(0), terms.get(1));
            case GREATER_THAN:
            case DATE_GREATER_THAN:
            case TIME_GREATER_THAN:
            case DATETIME_GREATER_THAN:
            case DAYTIMEDURATION_GREATER_THAN:
            case YEARMONTHDURATION_GREATER_THAN:
                return BUILTIN.createGreater(terms.get(0), terms.get(1));
            case GREATER_EQUAL:
                return BUILTIN.createGreaterEqual(terms.get(0), terms.get(1));
            case IS_DATATYPE:
                return BUILTIN.createIsDatatype(terms.get(0), terms.get(1));
            case IS_NOT_DATATYPE:
                return BUILTIN.createIsNotDatatype(terms.get(0), terms.get(1));
            case STRING_LENGTH:
                return BUILTIN.createStringLength(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_TO_UPPER:
                return BUILTIN.createStringToUpper(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_TO_LOWER:
                return BUILTIN.createStringToLower(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_URI_ENCODE:
                return BUILTIN.createStringUriEncode(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_IRI_TO_URI:
                return BUILTIN.createStringIriToUri(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_ESCAPE_HTML_URI:
                return BUILTIN.createStringEscapeHtmlUri(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_CONTAINS:
                return BUILTIN.createStringContains(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_STARTS_WITH:
                return BUILTIN.createStringStartsWith(terms.get(0), terms.get(1));
            case STRING_ENDS_WITH:
                return BUILTIN.createStringEndsWith(terms.get(0), terms.get(1));
            case STRING_MATCHES:
                return BUILTIN.createStringMatches(terms.get(0), terms.get(1));
            case YEAR_PART:
                return BUILTIN.createYearPart(sortedTerms.get(0), sortedTerms.get(1));
            case MONTH_PART:
                return BUILTIN.createMonthPart(sortedTerms.get(0), sortedTerms.get(1));
            case DAY_PART:
                return BUILTIN.createDayPart(sortedTerms.get(0), sortedTerms.get(1));
            case HOUR_PART:
                return BUILTIN.createHourPart(sortedTerms.get(0), sortedTerms.get(1));
            case MINUTE_PART:
                return BUILTIN.createMinutePart(sortedTerms.get(0), sortedTerms.get(1));
            case SECOND_PART:
                return BUILTIN.createSecondPart(sortedTerms.get(0), sortedTerms.get(1));
            case TIMEZONE_PART:
                return BUILTIN.createTimezonePart(sortedTerms.get(0), sortedTerms.get(1));
            case TEXT_FROM_STRING:
                return BUILTIN.createTextFromString(sortedTerms.get(0), sortedTerms.get(1));
            case STRING_FROM_TEXT:
                return BUILTIN.createStringFromText(sortedTerms.get(0), sortedTerms.get(1));
            case LANG_FROM_TEXT:
                return BUILTIN.createLangFromText(sortedTerms.get(0), sortedTerms.get(1));
            case TO_BASE64:
                return BUILTIN.createToBase64Binary(sortedTerms.get(0), sortedTerms.get(1));
            case TO_BOOLEAN:
                return BUILTIN.createToBoolean(sortedTerms.get(0), sortedTerms.get(1));
            case TO_DATE:
                return BUILTIN.createToDate(sortedTerms.get(0), sortedTerms.get(1));
            case TO_DATETIME:
                return BUILTIN.createToDateTime(sortedTerms.get(0), sortedTerms.get(1));
            case TO_DAYTIMEDURATION:
                return BUILTIN.createToDayTimeDuration(sortedTerms.get(0), sortedTerms.get(1));
            case TO_DECIMAL:
                return BUILTIN.createToDecimal(sortedTerms.get(0), sortedTerms.get(1));
            case TO_DOUBLE:
                return BUILTIN.createToDouble(sortedTerms.get(0), sortedTerms.get(1));
            case TO_DURATION:
                return BUILTIN.createToDuration(sortedTerms.get(0), sortedTerms.get(1));
            case TO_FLOAT:
                return BUILTIN.createToFloat(sortedTerms.get(0), sortedTerms.get(1));
            case TO_GDAY:
                return BUILTIN.createToGDay(sortedTerms.get(0), sortedTerms.get(1));
            case TO_GMONTH:
                return BUILTIN.createToGMonth(sortedTerms.get(0), sortedTerms.get(1));
            case TO_GMONTHDAY:
                return BUILTIN.createToGMonthDay(sortedTerms.get(0), sortedTerms.get(1));
            case TO_GYEAR:
                return BUILTIN.createToGYear(sortedTerms.get(0), sortedTerms.get(1));
            case TO_GYEARMONTH:
                return BUILTIN.createToGYearMonth(sortedTerms.get(0), sortedTerms.get(1));
            case TO_HEXBINARY:
                return BUILTIN.createToHexBinary(sortedTerms.get(0), sortedTerms.get(1));
            case TO_INTEGER:
                return BUILTIN.createToInteger(sortedTerms.get(0), sortedTerms.get(1));
            case TO_IRI:
                return BUILTIN.createToIRI(sortedTerms.get(0), sortedTerms.get(1));
            case TO_STRING:
                return BUILTIN.createToString(sortedTerms.get(0), sortedTerms.get(1));
            case TO_TEXT:
                return BUILTIN.createToText(sortedTerms.get(0), sortedTerms.get(1));
            case TO_TIME:
                return BUILTIN.createToTime(sortedTerms.get(0), sortedTerms.get(1));
            case TO_XMLLITERAL:
                return BUILTIN.createToXMLLiteral(sortedTerms.get(0), sortedTerms.get(1));
            case TO_YEARMONTHDURATION:
                return BUILTIN.createToYearMonthDuration(sortedTerms.get(0), sortedTerms.get(1));
            default:
                throw new IllegalArgumentException("Unknown Built-In");
        }
    }

    /**
	 * Changes the order of the terms for IRIS. The first entry becomes the last one.
	 * 
	 * @param terms
	 *            a list of terms in normal order.
	 * @return a list of terms where the first entry is the last one.
	 */
    private static List<ITerm> sortListForIRIS(List<ITerm> terms) {
        assert terms != null;
        List<ITerm> newTerms = new ArrayList<ITerm>();
        ITerm first = terms.get(0);
        for (int i = 1; i < terms.size(); i++) {
            newTerms.add(terms.get(i));
        }
        newTerms.add(first);
        return newTerms;
    }
}
