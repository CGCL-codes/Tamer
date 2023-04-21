package org.wsml.reasoner.transformation.le.foldecomposition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.AttributeConstraintMolecule;
import org.omwg.logicalexpression.AttributeInferenceMolecule;
import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.CompoundMolecule;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.MembershipMolecule;
import org.omwg.logicalexpression.Molecule;
import org.omwg.logicalexpression.SubConceptMolecule;
import org.omwg.logicalexpression.terms.Term;
import org.wsml.reasoner.impl.WSMO4JManager;
import org.wsml.reasoner.transformation.le.LEUtil;
import org.wsml.reasoner.transformation.le.NormalizationRule;
import org.wsmo.common.Identifier;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;

public class FOLMoleculeDecompositionRule implements NormalizationRule {

    public static final String sub = "urn://sub#";

    public static final String isa = "urn://isa#";

    public static final String hasValue = "urn://hv#";

    public static final String ofType = "urn://oftp#";

    public static final String impliesType = "urn://imtp#";

    private LogicalExpressionFactory leFactory;

    private WsmoFactory wsmoFactory;

    public FOLMoleculeDecompositionRule(WSMO4JManager wsmoManager) {
        this.leFactory = wsmoManager.getLogicalExpressionFactory();
        this.wsmoFactory = wsmoManager.getWSMOFactory();
    }

    public LogicalExpression apply(LogicalExpression expression) {
        if (expression instanceof CompoundMolecule) {
            return LEUtil.buildNaryConjunction(leFactory, decomposeMolecule((CompoundMolecule) expression));
        } else {
            return moleculeToAtom((Molecule) expression);
        }
    }

    public boolean isApplicable(LogicalExpression expression) {
        return expression instanceof Molecule;
    }

    protected Set<LogicalExpression> decomposeMolecule(CompoundMolecule compoundMolecule) {
        Set<LogicalExpression> simpleMolecules = new HashSet<LogicalExpression>();
        for (LogicalExpression le : compoundMolecule.listOperands()) {
            if (le instanceof Molecule) {
                simpleMolecules.add(moleculeToAtom((Molecule) le));
            }
        }
        return simpleMolecules;
    }

    private Atom moleculeToAtom(Molecule m) {
        Identifier id = null;
        List<Term> params = new ArrayList<Term>();
        params.add(m.getLeftParameter());
        if (m instanceof SubConceptMolecule) {
            id = wsmoFactory.createIRI(sub);
        }
        if (m instanceof MembershipMolecule) {
            id = wsmoFactory.createIRI(isa);
        }
        if (m instanceof AttributeConstraintMolecule) {
            id = wsmoFactory.createIRI(ofType);
            params.add(((AttributeConstraintMolecule) m).getAttribute());
        }
        if (m instanceof AttributeInferenceMolecule) {
            id = wsmoFactory.createIRI(impliesType);
            params.add(((AttributeInferenceMolecule) m).getAttribute());
        }
        if (m instanceof AttributeValueMolecule) {
            id = wsmoFactory.createIRI(hasValue);
            params.add(((AttributeValueMolecule) m).getAttribute());
        }
        params.add(m.getRightParameter());
        return leFactory.createAtom(id, params);
    }

    public String toString() {
        return "X[A1,...,An]\n\t=>\n X[A1] and ... and X[An]\n";
    }
}
