package edu.gsbme.MMLParser2.MathML.MEE.Evaluate;

import java.util.HashMap;

public class StandardFunctions {

    private static StandardFunctions stdFunc;

    HashMap<String, MEEAlgorithm> standardFunc;

    private StandardFunctions() {
        standardFunc = new HashMap<String, MEEAlgorithm>();
    }

    public static StandardFunctions get() {
        if (stdFunc == null) {
            stdFunc = new StandardFunctions();
            return stdFunc;
        }
        return stdFunc;
    }
}
