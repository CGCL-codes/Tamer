package org.dcopolis.algorithm.dpop;

import java.util.LinkedList;
import java.util.HashSet;
import java.io.Serializable;
import org.dcopolis.problem.Variable;
import org.dcopolis.problem.DisCOP;
import org.dcopolis.problem.Context;

public class AssignmentHyperCube implements Serializable {

    private static final long serialVersionUID = -6806141123599037027L;

    LinkedList<Assignment> assignments;

    Variable parent;

    private class ContextGenerator {

        Variable v;

        Object domain[];

        int domainIdx;

        ContextGenerator nextMostSignificant;

        boolean more;

        public ContextGenerator(Variable parent, HashSet<Variable> pseudoparents) {
            this(parent);
            ContextGenerator last = this;
            for (Variable v : pseudoparents) last = new ContextGenerator(v, last);
        }

        ContextGenerator(Variable v) {
            this(v, (ContextGenerator) null);
        }

        ContextGenerator(Variable v, ContextGenerator nextLeastSignificant) {
            if (nextLeastSignificant != null) nextLeastSignificant.nextMostSignificant = this;
            this.v = v;
            domain = v.getDomain().toArray(new Object[0]);
            domainIdx = 0;
            more = (domain.length > 0) && (nextMostSignificant == null ? true : nextMostSignificant.hasMore());
        }

        public boolean hasMore() {
            return more;
        }

        Context getNext(Context c) {
            c.setValue(v, domain[domainIdx]);
            if (nextMostSignificant != null) return nextMostSignificant.getNext(c); else return c;
        }

        public Context getNext() {
            Context c = getNext(new Context());
            increment();
            return c;
        }

        void increment() {
            if (++domainIdx >= domain.length) {
                if (nextMostSignificant == null) {
                    more = false;
                } else {
                    domainIdx = 0;
                    nextMostSignificant.increment();
                    if (!nextMostSignificant.hasMore()) {
                        more = false;
                    }
                }
            }
        }
    }

    public AssignmentHyperCube(DPOPAgent agent, Variable parent, HashSet<Variable> pseudoparents, DisCOP problem) {
        this.parent = parent;
        assignments = new LinkedList<Assignment>();
        ContextGenerator generator = new ContextGenerator(parent, pseudoparents);
        int numConstraints = problem.getConstraints().size();
        while (generator.hasMore()) {
            Context c = generator.getNext();
            assignments.add(new Assignment(c, problem.currentCost(c)));
            for (int i = 0; i < numConstraints; i++) agent.incrementConstraintChecks();
        }
    }

    public Variable getParent() {
        return parent;
    }

    public LinkedList<Assignment> getAssignments() {
        return assignments;
    }

    public String toString() {
        String assignment = "AssignmentHyperCube: {\n";
        for (Assignment a : assignments) {
            assignment = assignment + a.toString();
        }
        return assignment + "}";
    }
}
