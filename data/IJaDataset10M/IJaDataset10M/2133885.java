package org.gaea.lib;

import java.util.Vector;

/**
 * Check dependency for OQL Library.
 * 
 * @author jsgoupil
 */
public class Dependency {

    /**
	 * List of missing dependencies error.
	 * 
	 * @return errors or empty vector
	 */
    public static Vector<String> getMissingDependencies() {
        Vector<String> missingDependencies = new Vector<String>();
        @SuppressWarnings("unused") Class c;
        try {
            c = org.antlr.runtime.Lexer.class;
        } catch (NoClassDefFoundError e) {
            missingDependencies.add("ANTLR - http://www.antlr.org");
        }
        return missingDependencies;
    }
}
