package org.xteam.sled.semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xteam.parser.runtime.Span;
import org.xteam.sled.ast.AbstractPattern;
import org.xteam.sled.semantic.exp.Exp;
import org.xteam.sled.semantic.exp.ExpCondition;
import org.xteam.sled.solver.SolverResult;

public class BranchResult {

    private List<Exp> constraints;

    private AbstractPattern pattern;

    private Map<String, Span> labels;

    private Map<String, Exp> values;

    public BranchResult(Map<String, Span> labels, SolverResult solverResult, AbstractPattern pattern) {
        this.labels = labels;
        this.constraints = new ArrayList<Exp>();
        for (ExpCondition e : solverResult.getConstraints()) {
            this.constraints.add(e);
        }
        this.values = solverResult.getValues();
        this.pattern = pattern;
    }

    public Map<String, Exp> getValues() {
        return values;
    }

    public Map<String, Span> getLabels() {
        return labels;
    }

    public AbstractPattern getPattern() {
        return pattern;
    }

    public List<Exp> getConstraints() {
        return constraints;
    }
}
