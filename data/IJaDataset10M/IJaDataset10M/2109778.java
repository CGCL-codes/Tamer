package net.sourceforge.jnipp.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple command-line parser.
 *
 * This is a simple helper class for parsing command-line arguments.  The parser
 * recognizes input of the following forms:
 *
 * -name=value
 * -name value
 * -name
 *
 * Note that the variable name must be prefixed with a '-' character, otherwise
 * it is assumed that it is a value.
 *
 * @author $Author: ptrewhella $
 * @version $Revision: 1.3 $
 */
public class CommandLineParser {

    /**
	 * The resultant map of name/value pairs.
	 *
	 * @see #CommandLineParser
	 */
    private Map paramMap = new HashMap();

    /**
	 * Public constructor.
	 *
	 * The only defined constructor accepts the argument array and attempts to
	 * parse it.  If successful, the <code>paramMap</code> contains all of the
	 * name/value pairs.
	 *
	 * @param args The argument array to be parsed.
	 * @see #paramMap
	 */
    public CommandLineParser(String[] args) {
        String currentArg = null, currentSwitch = null;
        char c = 0;
        boolean hasEntry = false;
        for (int i = 0; i < args.length; ++i) {
            currentArg = args[i];
            c = currentArg.charAt(0);
            if ((c == '-') || (c == '/')) {
                if ((currentSwitch != null) && (hasEntry == false)) {
                    paramMap.put(currentSwitch, "");
                    currentSwitch = null;
                }
                int eq = currentArg.indexOf("=", 0);
                if (eq != -1) {
                    paramMap.put(currentArg.substring(1, eq), currentArg.substring(eq + 1));
                    currentSwitch = null;
                } else {
                    currentSwitch = currentArg.substring(1);
                    hasEntry = false;
                }
            } else {
                if (currentSwitch == null) ;
                paramMap.put(currentSwitch, currentArg);
                hasEntry = true;
            }
            if (currentSwitch != null) if (hasEntry == false) paramMap.put(currentSwitch, "");
        }
    }

    /**
	 * Public accessor to single variable value.
	 *
	 * @param paramName The name of the parameter whose value is to be retrieved.
	 * @return The parameter value, or <code>null</code> if not set.
	 */
    public String getParamValue(String paramName) {
        return (String) paramMap.get(paramName);
    }

    /**
	 * Overloaded <code>toString()</code> method.
	 *
	 * Used for debugging purposes and returns the value of <code>paramMap.toString()</code>.
	 *
	 * @return String representation of class instance.
	 * @see #paramMap
	 */
    public String toString() {
        return paramMap.toString();
    }
}
