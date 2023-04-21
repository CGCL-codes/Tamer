package org.apache.rat.pd.heuristic.functions;

import java.io.PrintStream;

/**
 * This class can match C# functions. More info on: {@link}
 * http://en.wikipedia.org/wiki/C_Sharp_(programming_language)
 * 
 * @author maka
 */
public class CSharpFunctionHeuristicChecker extends FunctionHeuristicChecker {

    private static final String C_SHARP_CLOSED_BRACKET = "\\}";

    private static final String C_SHARP_OPENED_BRACKET = "\\{";

    /**
     * This is regular expression for C# function matching . More info on:
     * {@link} http://en.wikipedia.org/wiki/C_Sharp_(programming_language)
     */
    private static final String C_SHARP_FUNCTION_REGEX = "^[\t ]*[(public)(protected)(private)(static)(void)(abstract)\\w+]* +(.*)\\([^()]*?\\)(\\s)*" + "\\{[\\s\\S]*\\}[\n\r]*";

    /**
     * @param limit minimal length of function which will be considered
     * @param out print stream for logging purposes
     */
    public CSharpFunctionHeuristicChecker(int limit, PrintStream out) {
        super(limit, C_SHARP_FUNCTION_REGEX, C_SHARP_OPENED_BRACKET, C_SHARP_CLOSED_BRACKET, "c#", out);
    }
}
