package org.pz.platypus.parser;

import org.pz.platypus.GDD;
import org.pz.platypus.Literals;

/**
 * Parses macros and looks them up in the macro tables.
 * TODO: need to handle multiple layers of macros and check for circular macros.
 *
 * @author alb
 */
public class MacroParser {

    /** following are for output of error messages */
    private GDD gdd;

    public MacroParser(final GDD gdd) {
        this.gdd = gdd;
    }

    /**
     * Principal function. Extracts the macro name and looks it up. It
     * dumps the expanded text into the expandedMacro parameter and returns
     * the number of chars parsed in the input text.
     * @param input the input Platypus text that contains a macro
     * @param parsePoint where to start parsing input
     * @return  the number of chars to move the isLineComment point forward
     */
    public int parse(char[] input, int parsePoint) {
        if (input == null || parsePoint < 0) {
            throw new IllegalArgumentException(gdd.getLit("ERROR.INVALID_PARAM_IN_MACROPARSER.PARSE"));
        }
        int startParsePoint = parsePoint;
        String macro;
        String expandedMacro;
        if (input[parsePoint] == '[') {
            parsePoint += 1;
        }
        if (input[parsePoint] != '$' && input[parsePoint] != '_') {
            return (0);
        }
        try {
            macro = extractMacroName(input, parsePoint);
        } catch (IllegalArgumentException malformedMacro) {
            parsePoint += malformedMacro.toString().length();
            return (parsePoint - startParsePoint);
        }
        if (macro.startsWith("_")) {
            expandedMacro = lookupSystemMacro(macro);
        }
        return (parsePoint + macro.length() - startParsePoint + 1);
    }

    /**
     * Look up a macro in the table of System stings
     * @param macro macro to look up
     * @return expanded macro from the table; null, if an error occurred
     */
    public String lookupSystemMacro(final String macro) {
        final String expandedMacro;
        expandedMacro = gdd.sysStrings.get(macro);
        if (expandedMacro == null) {
            gdd.log.warning(gdd.getLit("ERROR.MACRO_NOT_FOUND") + ": " + macro);
        }
        return (expandedMacro);
    }

    /**
     * Extract the macro name from the string of chars starting at parsePoint
     * @param input the chars from which to extract the macro. should begin with a $
     * @param parsePoint the point in input where the extracting should begin
     * @throws IllegalArgumentException in the event an invalid macro name was used
     * @return the macro name if all went well; null, if not.
     */
    public String extractMacroName(final char[] input, int parsePoint) throws IllegalArgumentException {
        StringBuilder macro = new StringBuilder(input.length);
        int i = 0;
        if (input[parsePoint] == '$' || input[parsePoint] == '_') {
            macro.append(input[parsePoint]);
            parsePoint++;
        } else {
            gdd.log.warning(gdd.getLit("INVALID_MACRO_NAME") + " " + macro.toString() + gdd.getLit("IGNORED"));
            throw new IllegalArgumentException(macro.toString());
        }
        for (i = parsePoint; i < input.length; i++) {
            if (Character.isLetterOrDigit(input[i]) || input[i] == '_') {
                macro.append(input[i]);
            } else {
                break;
            }
        }
        if (input[i] != ']') {
            gdd.log.warning(gdd.getLit("INVALID_MACRO_NAME") + " " + macro.toString() + gdd.getLit("IGNORED"));
            throw new IllegalArgumentException(macro.toString());
        }
        return (macro.toString());
    }
}
