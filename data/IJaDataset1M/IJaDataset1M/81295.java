package org.jdmp.core.algorithm.basic;

import java.util.HashMap;
import java.util.Map;
import org.jdmp.core.JDMP;
import org.jdmp.core.algorithm.AbstractAlgorithm;
import org.jdmp.core.variable.Variable;
import org.ujmp.core.exceptions.MatrixException;

public class About extends AbstractAlgorithm {

    private static final long serialVersionUID = 5328096248640416339L;

    public static final String DESCRIPTION = "displays information about JDMP";

    public About(Variable... variables) {
        super();
        setDescription(DESCRIPTION);
    }

    public Map<String, Object> calculateObjects(Map<String, Object> input) throws MatrixException {
        Map<String, Object> result = new HashMap<String, Object>();
        String s = "\n";
        s += "Copyright (C) " + JDMP.COPYRIGHT + " " + JDMP.AUTHOR + "\n";
        s += "\n";
        s += "Java Data Mining Package (JDMP) Version:             " + JDMP.JDMPVERSION + "\n";
        s += "Universal Java Matrix Package (UJMP) Version:        " + JDMP.UJMPVERSION + "\n";
        s += "Java Runtime Version:                                " + System.getProperty("java.runtime.version") + "\n";
        s += "\n";
        s += "The Java Data Mining Package (JDMP) is an open source Java library\n";
        s += "for data analysis and machine learning. It uses the Universal Java\n";
        s += "Matrix Package (UJMP) as a mathematical back-end for calculations\n";
        s += "and I/O functions.\n";
        s += "\n";
        result.put(TARGET, s);
        return result;
    }
}
