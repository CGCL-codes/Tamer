package org.jdmp.core.algorithm.basic;

import java.util.HashMap;
import java.util.Map;
import org.jdmp.core.algorithm.AbstractAlgorithm;
import org.jdmp.core.variable.Variable;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.exceptions.MatrixException;

public class CreateSunSpotData extends AbstractAlgorithm {

    private static final long serialVersionUID = 3142610145985234765L;

    public static final String DESCRIPTION = "creates a matrix with the sun spot data set";

    public CreateSunSpotData(Variable... variables) {
        super();
        setDescription(DESCRIPTION);
    }

    public Map<String, Object> calculateObjects(Map<String, Object> input) throws MatrixException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(TARGET, MatrixFactory.SUNSPOTDATASET());
        return result;
    }
}
