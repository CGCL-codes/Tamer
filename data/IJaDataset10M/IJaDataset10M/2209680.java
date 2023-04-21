package org.jdmp.core.algorithm.basic;

import java.util.HashMap;
import java.util.Map;
import org.jdmp.core.algorithm.AbstractAlgorithm;
import org.jdmp.core.variable.Variable;
import org.ujmp.core.Matrix;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.util.MathUtil;

public class Specificity extends AbstractAlgorithm {

    private static final long serialVersionUID = -2540851195821954113L;

    public static final String DESCRIPTION = "target = specificity(source)";

    public Specificity(Variable... variables) {
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
        Matrix target = Matrix.factory.zeros(source.getRowCount(), 1);
        long cols = source.getColumnCount();
        long rows = source.getRowCount();
        for (int k = 0; k < rows; k++) {
            int tp = 0;
            int tn = 0;
            int fn = 0;
            int fp = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int count = source.getAsInt(r, c);
                    boolean expected = r == k;
                    boolean predicted = c == k;
                    if (expected && predicted) {
                        tp += count;
                    } else if (expected && (!predicted)) {
                        fn += count;
                    } else if ((!expected) && predicted) {
                        fp += count;
                    } else {
                        tn += count;
                    }
                }
            }
            target.setAsDouble(MathUtil.specificity(tn, fp), k, 0);
        }
        result.put(TARGET, target);
        return result;
    }
}
