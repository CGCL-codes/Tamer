package org.marcont2.rulegenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for parsing strings containing values for subjects, predicates and objects.
 * @author Piotr Piotrowski
 */
public class VariableMatcher {

    /**
     * Regular expression pattern matching variables
     */
    private static Pattern captureVariables = Pattern.compile("\\{\\$(\\w+)\\}");

    /**
     * Regular expression pattern matching functions
     */
    private static Pattern captureFunctions = Pattern.compile("\\{marcont[:](\\w+)\\((([']?\\$?[a-zA-Z0-9_:]+[']?[,]?[ ]*)*)\\)\\}");

    /**
     * Regular expression pattern matching short uris
     */
    private static Pattern captureShortUri = Pattern.compile("^(\\w+)[:](\\w+)$");

    /**
     * Regular expression pattern matching variables names referencing premises
     */
    private static Pattern capturePremises = Pattern.compile("(P[SPO])(\\d+)");

    /**
     * Regular expression pattern matching variables names referencing consequents
     */
    private static Pattern captureConsequents = Pattern.compile("(C[SPO])(\\d+)");

    /**
     * Pattern for simplified matching premise variables references.
     */
    private static Pattern premiseRef = Pattern.compile("(\\$P[SPO])(\\d+)");

    /**
     * Pattern for simplified matching consequent variables references.
     */
    private static Pattern consequentRef = Pattern.compile("(\\$C[SPO])(\\d+)");

    /** Creates a new instance of VariableMatcher */
    public VariableMatcher() {
    }

    /**
     * Checks for references of variables and places them in collection.
     * Returns map containing pairs:
     * "parameters" -> Collection<String> - referenced parameters names
     * "premises" -> Collection<Integer> - referenced premises indexes
     * "consequents" -> Collection<Integer> - referenced consequents indexes
     * If value is null it is treated like "".
     * @param value string in which to search for references
     * @return collection containing all references
     */
    public static Map<String, Collection<?>> getReferences(String value) {
        List<String> parameters = new ArrayList<String>();
        List<Integer> premises = new ArrayList<Integer>();
        List<Integer> consequents = new ArrayList<Integer>();
        Map<String, Collection<?>> references = new HashMap<String, Collection<?>>();
        references.put("parameters", parameters);
        references.put("premises", premises);
        references.put("consequents", consequents);
        if (value != null && !"".equals(value)) {
            Matcher matcher = captureVariables.matcher(value);
            while (matcher.find()) {
                putVariable(matcher.group(1), references);
            }
            matcher = captureFunctions.matcher(value);
            while (matcher.find()) {
                String[] funParams = matcher.group(2).split(",[ ]*");
                for (String str : funParams) {
                    if (str.startsWith("$")) {
                        putVariable(str.substring(1), references);
                    }
                }
            }
        }
        return references;
    }

    /**
     * Helper method clasifying variable name as reference to premise, consequent or parameter
     * @param varName variable name to clasify
     * @param references collection with references. Collection details: {@link #getReferences(String)}
     */
    private static void putVariable(String varName, Map<String, Collection<?>> references) {
        Matcher tmpMatcher;
        if ((tmpMatcher = capturePremises.matcher(varName)).matches()) {
            Collection<Integer> premises = (Collection<Integer>) references.get("premises");
            premises.add(new Integer(tmpMatcher.group(2)));
        } else if ((tmpMatcher = captureConsequents.matcher(varName)).matches()) {
            Collection<Integer> consequents = (Collection<Integer>) references.get("consequents");
            consequents.add(new Integer(tmpMatcher.group(2)));
        } else {
            Collection<String> parameters = (Collection<String>) references.get("parameters");
            parameters.add(varName);
        }
    }

    /**
     * Substitutes all occurrences of references to premises with index greater than index for references to (index - 1).
     * @param src source string to be changed
     * @param index index of premise above which references are to be changed
     * @return new string with references substituted
     */
    public static String removePremise(String src, int index) {
        if (src == null || "".equals(src)) {
            return src;
        }
        Matcher m = premiseRef.matcher(src);
        StringBuffer buf = new StringBuffer();
        while (m.find()) {
            int i = Integer.parseInt(m.group(2));
            if (i > index) {
                m.appendReplacement(buf, "$1" + (i - 1));
            }
        }
        m.appendTail(buf);
        return buf.toString();
    }

    /**
     * Substitutes all occurrences of references to consequents with index greater than index for references to (index - 1).
     * @param src source string to be changed
     * @param index index of consequent above which references are to be changed
     * @return new string with references substituted
     */
    public static String removeConsequent(String src, int index) {
        if (src == null || "".equals(src)) {
            return src;
        }
        Matcher m = consequentRef.matcher(src);
        StringBuffer buf = new StringBuffer();
        while (m.find()) {
            int i = Integer.parseInt(m.group(2));
            if (i > index) {
                m.appendReplacement(buf, "$1" + (i - 1));
            }
        }
        m.appendTail(buf);
        return buf.toString();
    }

    /**
     * Substitutes all occurrences of references to variable oldName for references to variable newName.
     * @param src source string to be changed
     * @param oldName old name of the variable
     * @param newName new name of the variable
     * @return new string with references substituted
     */
    public static String correctVariable(String src, String oldName, String newName) {
        if (src == null || "".equals(src)) {
            return src;
        }
        Pattern p = Pattern.compile("\\$" + oldName);
        Matcher m = p.matcher(src);
        return m.replaceAll("\\$" + newName);
    }
}
