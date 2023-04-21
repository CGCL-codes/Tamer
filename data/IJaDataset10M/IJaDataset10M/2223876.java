package org.jdmp.core.algorithm.basic;

import java.util.HashMap;
import java.util.Map;
import org.jdmp.core.JDMP;
import org.jdmp.core.algorithm.AbstractAlgorithm;
import org.jdmp.core.variable.Variable;
import org.ujmp.core.exceptions.MatrixException;

public class Version extends AbstractAlgorithm {

    private static final long serialVersionUID = -5926495980259773197L;

    public static final String DESCRIPTION = "displays version information about JDMP";

    public Version(Variable... variables) {
        super();
        setDescription(DESCRIPTION);
    }

    public Map<String, Object> calculateObjects(Map<String, Object> input) throws MatrixException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(TARGET, JDMP.JDMPVERSION);
        return result;
    }
}
