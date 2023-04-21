package org.jdmp.core.algorithm.basic;

import java.util.HashMap;
import java.util.Map;
import org.jdmp.core.algorithm.AbstractAlgorithm;
import org.jdmp.core.variable.Variable;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.util.MathUtil;

public class Abs extends AbstractAlgorithm {

    private static final long serialVersionUID = 8963883977304138269L;

    public static final String DESCRIPTION = "target = abs(source)";

    public Abs(Variable... variables) {
        super();
        setDescription(DESCRIPTION);
        addVariableKey(SOURCE);
        addVariableKey(TARGET);
        setEdgeLabel(SOURCE, "Source");
        setEdgeLabel(TARGET, "Target");
        setEdgeDirection(SOURCE, EdgeDirection.Incoming);
        setEdgeDirection(TARGET, EdgeDirection.Outgoing);
        setVariables(variables);
    }

    public Map<String, Object> calculateObjects(Map<String, Object> input) throws MatrixException {
        Map<String, Object> result = new HashMap<String, Object>();
        Matrix source = MathUtil.getMatrix(input.get(SOURCE));
        result.put(TARGET, source.abs(Ret.NEW));
        return result;
    }

    public void setSourceVariable(Variable source) {
        setVariable(SOURCE, source);
    }

    public void setTargetVariable(Variable target) {
        setVariable(TARGET, target);
    }
}
