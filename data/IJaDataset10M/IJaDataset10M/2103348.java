package org.jdmp.core.algorithm.basic;

import java.util.HashMap;
import java.util.Map;
import org.jdmp.core.algorithm.AbstractAlgorithm;
import org.jdmp.core.variable.Variable;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.exceptions.MatrixException;

public class Env extends AbstractAlgorithm {

    private static final long serialVersionUID = -8242367467772492055L;

    public static final String DESCRIPTION = "shows the system environment";

    public Env(Variable... variables) {
        super();
        setDescription(DESCRIPTION);
        addVariableKey(TARGET);
        setEdgeLabel(TARGET, "Target");
        setEdgeDirection(TARGET, EdgeDirection.Outgoing);
        setVariables(variables);
    }

    public Map<String, Object> calculateObjects(Map<String, Object> input) throws MatrixException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(TARGET, MatrixFactory.systemEnvironment());
        return result;
    }
}
