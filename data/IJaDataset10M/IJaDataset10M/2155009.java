package org.eml.MMAX2.annotation.query;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.eml.MMAX2.annotation.markables.MarkableLevel;
import org.eml.MMAX2.annotation.scheme.MMAX2Attribute;
import org.eml.MMAX2.api.AttributeAPI;
import org.eml.MMAX2.utils.MMAX2Constants;

public class MMAX2ComplexQueryTerm {

    int connector = -1;

    int connectorInstances = 0;

    int size = 0;

    boolean negated = false;

    String queryString = null;

    MarkableLevel level = null;

    int endsAt = -1;

    int startsAt = -1;

    ArrayList atomicQueryTerms = new ArrayList();

    ArrayList complexQueryTerms = new ArrayList();

    /** Creates new MMAX2ComplexQueryTerm */
    public MMAX2ComplexQueryTerm(String _queryString, MarkableLevel _level, int _from, int _to, boolean _negated) throws org.eml.MMAX2.annotation.query.MMAX2QueryException {
        queryString = _queryString.trim();
        level = _level;
        negated = _negated;
        if (queryString.charAt(_from) != '(' || queryString.charAt(_to) != ')') {
            throw new MMAX2QueryException("Query Syntax Error: Complex query must be surrounded by ( and ) !");
        }
        startsAt = _from;
        endsAt = _to;
        _from++;
        _from = skip(_from);
        _to--;
        _to = skap(_to);
        int lastTermsEnd = -1;
        while (true) {
            try {
                lastTermsEnd = parseQueryString(_from, _to);
            } catch (java.lang.StringIndexOutOfBoundsException ex) {
                throw new MMAX2QueryException("Query Syntax Error: Something's wrong with the query!");
            }
            if (lastTermsEnd == -1 || lastTermsEnd > _to) {
                throw new MMAX2QueryException("Query Syntax Error: Something's wrong with the query!");
            }
            _from = lastTermsEnd + 1;
            _from = skip(_from);
            if (_from >= endsAt) break;
        }
        if (getConnector() == -1 && size > 1) {
            throw new MMAX2QueryException("Query Syntax Error: No logical connector in complex query term:\n" + queryString.substring(startsAt, endsAt + 1));
        }
        if (connectorInstances != size - 1) {
            String temp = "";
            if (connector == MMAX2TermConnector.AND) {
                temp = "Probably you meant AND.";
            } else {
                temp = "Probably you meant OR.";
            }
            throw new MMAX2QueryException("Query Syntax Error: Missing logical connector in complex query term:\n" + queryString.substring(startsAt, endsAt + 1) + "\n" + temp);
        }
    }

    /** This method recursively executes the query specified in this complex query term. It returns an ArrayList of all matching 
        Markables. */
    public final ArrayList execute() {
        ArrayList finalResult = new ArrayList();
        ArrayList[] allResults = new ArrayList[this.size];
        ArrayList[] allInvertedResults = null;
        for (int z = 0; z < atomicQueryTerms.size(); z++) {
            allResults[z] = ((MMAX2AtomicQueryTerm) atomicQueryTerms.get(z)).execute();
        }
        for (int z = 0; z < complexQueryTerms.size(); z++) {
            allResults[z + atomicQueryTerms.size()] = ((MMAX2ComplexQueryTerm) complexQueryTerms.get(z)).execute();
        }
        if (connector == MMAX2TermConnector.AND) {
            if (negated) {
                finalResult = MMAX2QueryTree.invert(MMAX2QueryTree.intersect(allResults), level.getMarkables());
            } else {
                finalResult = MMAX2QueryTree.intersect(allResults);
            }
        } else {
            if (negated) {
                allInvertedResults = MMAX2QueryTree.invertAll(allResults, level.getMarkables());
                finalResult = MMAX2QueryTree.intersect(allInvertedResults);
            } else {
                finalResult = MMAX2QueryTree.merge(allResults);
            }
        }
        return finalResult;
    }

    /** This method receives zero-based indices of the first and last character to consider. It returns the end index of the last
        term parsed. From and to have been moved to non-ws-positions when this method is called. */
    public final int parseQueryString(int from, int to) throws java.lang.StringIndexOutOfBoundsException, MMAX2QueryException {
        MMAX2TermConnector currentConnector = null;
        MMAX2AtomicQueryTerm currentAtomicQueryTerm = null;
        MMAX2ComplexQueryTerm currentComplexQueryTerm = null;
        int end = -1;
        if (queryString.substring(from).startsWith("(") || (queryString.substring(from).startsWith("!(")) || (queryString.substring(from).startsWith("not("))) {
            if (queryString.substring(from).startsWith("(")) {
                end = getMatchingBracketPosition(from, "(", ")");
                currentComplexQueryTerm = new MMAX2ComplexQueryTerm(queryString, level, from, end - 1, false);
            } else if (queryString.substring(from).startsWith("!(")) {
                from++;
                end = getMatchingBracketPosition(from, "(", ")");
                currentComplexQueryTerm = new MMAX2ComplexQueryTerm(queryString, level, from, end - 1, true);
            } else if (queryString.substring(from).startsWith("not(")) {
                from = from + 3;
                end = getMatchingBracketPosition(from, "(", ")");
                currentComplexQueryTerm = new MMAX2ComplexQueryTerm(queryString, level, from, end - 1, true);
            }
            if (currentComplexQueryTerm != null) {
                size++;
                complexQueryTerms.add(currentComplexQueryTerm);
                if (currentComplexQueryTerm.getEndsAt() < to) {
                    currentConnector = getNextConnector(currentComplexQueryTerm.getEndsAt());
                    if (currentConnector != null) {
                        connectorInstances++;
                        if (this.connector == -1) {
                            this.connector = currentConnector.getType();
                        } else {
                            if (this.connector != currentConnector.getType()) {
                                String temp = "";
                                if (this.connector == MMAX2TermConnector.AND) {
                                    temp = "Found OR, expected AND.";
                                } else {
                                    temp = "Found AND, expected OR.";
                                }
                                throw new MMAX2QueryException("Query Syntax Error: Inconsistent connectors in complex query!\n" + temp);
                            }
                        }
                        return currentConnector.getEndsAt();
                    } else {
                        return currentComplexQueryTerm.getEndsAt();
                    }
                } else {
                    return currentComplexQueryTerm.getEndsAt();
                }
            } else {
                throw new MMAX2QueryException("Query Syntax Error: Error with complex term!");
            }
        } else if ((queryString.substring(from, from + 3).equalsIgnoreCase("and")) || (queryString.substring(from, from + 2).equalsIgnoreCase("&&")) || (queryString.substring(from, from + 2).equalsIgnoreCase("or")) || (queryString.substring(from, from + 2).equalsIgnoreCase("||"))) {
            currentConnector = getNextConnector(from);
            if (currentConnector != null) {
                connectorInstances++;
                if (connector == -1) {
                    connector = currentConnector.getType();
                } else {
                    if (connector != currentConnector.getType()) {
                        String temp = "";
                        if (this.connector == MMAX2TermConnector.AND) {
                            temp = "Found OR, expected AND.";
                        } else {
                            temp = "Found AND, expected OR.";
                        }
                        throw new MMAX2QueryException("Query Syntax Error: Inconsistent connectors in complex query!\n" + temp);
                    }
                }
                return currentConnector.getEndsAt();
            } else {
                throw new MMAX2QueryException("Query Syntax Error: Couldn't parse connector!");
            }
        } else {
            currentAtomicQueryTerm = getNextAtomicTerm(from, to);
            if (currentAtomicQueryTerm != null) {
                size++;
                atomicQueryTerms.add(currentAtomicQueryTerm);
                if (currentAtomicQueryTerm.getEndsAt() < to) {
                    currentConnector = getNextConnector(currentAtomicQueryTerm.getEndsAt());
                    if (currentConnector != null) {
                        connectorInstances++;
                        if (connector == -1) {
                            connector = currentConnector.getType();
                        } else {
                            if (connector != currentConnector.getType()) {
                                String temp = "";
                                if (this.connector == MMAX2TermConnector.AND) {
                                    temp = "Found OR, expected AND.";
                                } else {
                                    temp = "Found AND, expected OR.";
                                }
                                throw new MMAX2QueryException("Query Syntax Error: Inconsistent connectors in complex query!\n" + temp);
                            }
                        }
                        return currentConnector.getEndsAt();
                    } else {
                        return currentAtomicQueryTerm.getEndsAt();
                    }
                } else {
                    return currentAtomicQueryTerm.getEndsAt();
                }
            } else {
                throw new MMAX2QueryException("Query Syntax Error: <term> expected!");
            }
        }
    }

    public final MMAX2AtomicQueryTerm getNextAtomicTerm(int from, int to) throws MMAX2QueryException {
        MMAX2Attribute currentAttribute = null;
        ArrayList valueList = new ArrayList();
        String attributeName = "";
        int attributeType = -1;
        String value = "";
        boolean negated = false;
        boolean regExpMatch = false;
        boolean basedataQuery = false;
        if (level.getMarkableLevelName().equalsIgnoreCase("internal_basedata_representation")) {
            basedataQuery = true;
        }
        String compactString = "";
        int offset = 0;
        from = skip(from);
        if (from == queryString.length()) {
            return null;
        }
        if (queryString.charAt(from) == '!') {
            negated = true;
            from++;
            compactString = "NOT ";
        } else if (queryString.substring(from, from + 3).equalsIgnoreCase("not")) {
            negated = true;
            from = from + 3;
            compactString = "NOT ";
        }
        from = skip(from);
        if (queryString.charAt(from) == '*') {
            regExpMatch = true;
        }
        from = skip(from);
        try {
            attributeName = queryString.substring(from, queryString.indexOf("=", from));
        } catch (java.lang.StringIndexOutOfBoundsException ex) {
            throw new MMAX2QueryException("Query Syntax Error: '<attributeName>=' expected!");
        }
        from = from + attributeName.length();
        if (regExpMatch) {
            attributeName = attributeName.substring(1);
            offset = 1;
        }
        attributeName = attributeName.trim();
        if (regExpMatch) compactString = compactString + "*";
        compactString = compactString + attributeName + " = ";
        if (attributeName.equalsIgnoreCase("markable_text")) {
            attributeType = MMAX2Constants.MARKABLE_TEXT;
        } else if (attributeName.equalsIgnoreCase("basedata_text")) {
            attributeType = MMAX2Constants.BASEDATA_TEXT;
        } else if (attributeName.equalsIgnoreCase("base_level")) {
            attributeType = MMAX2Constants.BASE_LEVEL;
        }
        if (attributeType == MMAX2Constants.BASEDATA_TEXT && basedataQuery == false) {
            throw new MMAX2QueryException("Query Syntax Error: Attribute basedata_text only defined for level basedata!");
        }
        if (basedataQuery == true && attributeType == -1) {
            attributeType = MMAX2Constants.BASEDATA_ATTRIBUTES;
        }
        if (attributeType != MMAX2Constants.BASEDATA_TEXT && attributeType != MMAX2Constants.BASEDATA_ATTRIBUTES && attributeType != MMAX2Constants.BASE_LEVEL && attributeType != MMAX2Constants.MARKABLE_TEXT) {
            ArrayList attributes = level.getCurrentAnnotationScheme().getAttributes();
            for (int z = 0; z < attributes.size(); z++) {
                if (((MMAX2Attribute) attributes.get(z)).getLowerCasedAttributeName().equalsIgnoreCase(attributeName)) {
                    currentAttribute = (MMAX2Attribute) attributes.get(z);
                    attributeType = currentAttribute.getType();
                    break;
                }
            }
            if (currentAttribute == null) {
                throw new MMAX2QueryException("Query Semantics Error:  Attribute '" + attributeName + "' is undefined on level '" + level.getMarkableLevelName() + "'!");
            }
        }
        if (queryString.charAt(from) != '=') {
            throw new MMAX2QueryException("Query Syntax Error: '=' expected!");
        }
        from++;
        from = skip(from);
        if (!regExpMatch) {
            if (queryString.charAt(from) == '{') {
                from++;
                from = skip(from);
                try {
                    value = queryString.substring(from, queryString.indexOf("}", from));
                } catch (java.lang.StringIndexOutOfBoundsException ex) {
                    throw new MMAX2QueryException("Query Syntax Error: '}' expected!");
                }
                value = value.trim();
                compactString = compactString + value;
                String tempValue = "";
                StringTokenizer toki = new StringTokenizer(value);
                while (toki.hasMoreTokens()) {
                    tempValue = toki.nextToken(",");
                    tempValue = tempValue.trim();
                    if (attributeType == MMAX2Constants.MARKABLE_TEXT || attributeType == MMAX2Constants.BASEDATA_TEXT || attributeType == MMAX2Constants.BASEDATA_ATTRIBUTES) {
                        valueList.add(tempValue);
                    } else if (attributeType == AttributeAPI.MARKABLE_POINTER) {
                        boolean valIsNumeric = false;
                        int numVal = -1;
                        try {
                            numVal = Integer.parseInt(tempValue);
                        } catch (java.lang.NumberFormatException ex) {
                        }
                        if (tempValue.equalsIgnoreCase("empty") == false && tempValue.equalsIgnoreCase("target") == false && numVal == -1) {
                            throw new MMAX2QueryException("Query Semantics Error: Pointer-type attribute '" + attributeName + "' on level '" + level.getMarkableLevelName() + "' cannot be queried for value '" + tempValue + "'!");
                        }
                        valueList.add(tempValue);
                    } else if (attributeType == MMAX2Constants.BASE_LEVEL) {
                        boolean valIsNumeric = false;
                        int numVal = -1;
                        try {
                            numVal = Integer.parseInt(tempValue);
                        } catch (java.lang.NumberFormatException ex) {
                        }
                        if ((tempValue.equalsIgnoreCase("true") == false && tempValue.equalsIgnoreCase("false") == false) || numVal != -1) {
                            throw new MMAX2QueryException("Query Semantics Error: BASE_LEVEL attribute cannot be queried for value '" + tempValue + "'!\n Legal values are 'true' and 'false'!");
                        }
                        valueList.add(tempValue);
                    } else if (attributeType == AttributeAPI.MARKABLE_SET) {
                        boolean valIsNumeric = false;
                        int numVal = -1;
                        try {
                            numVal = Integer.parseInt(tempValue);
                        } catch (java.lang.NumberFormatException ex) {
                        }
                        if (tempValue.equalsIgnoreCase("empty") == false && tempValue.equalsIgnoreCase("initial") == false && tempValue.equalsIgnoreCase("final") == false && numVal == -1) {
                            throw new MMAX2QueryException("Query Semantics Error: Set-type attribute '" + attributeName + "' on level '" + level.getMarkableLevelName() + "' cannot be queried for value '" + tempValue + "'!");
                        } else {
                            valueList.add(tempValue);
                        }
                    } else {
                        if (currentAttribute.isDefined(tempValue) == false) {
                            throw new MMAX2QueryException("Query Semantics Error: Value '" + tempValue + "' is undefined for attribute '" + currentAttribute.getLowerCasedAttributeName() + "' on level '" + level.getMarkableLevelName() + "'!");
                        } else {
                            valueList.add(tempValue);
                        }
                    }
                }
            } else {
                throw new MMAX2QueryException("Query Semantics Error: Value enumeration must be enclosed in { and }!");
            }
        } else {
            from = skip(from);
            if (queryString.charAt(from) != '{') {
                throw new MMAX2QueryException("Query Syntax Error: regexp string must be enclosed in { and }! (Will be removed before matching!)");
            }
            int end = queryString.indexOf("}", from);
            if (end == -1) {
                throw new MMAX2QueryException("Query Syntax Error: regexp string must be enclosed in { and }! (Will be removed before matching!)");
            }
            value = queryString.substring(from + 1, end);
            value = value.trim();
            compactString = compactString + value;
            valueList.add(value);
        }
        return new MMAX2AtomicQueryTerm(attributeName, attributeType, (String[]) valueList.toArray(new String[0]), level, negated, regExpMatch, from + value.length() + offset, compactString);
    }

    private final int skip(int from) {
        try {
            while (queryString.charAt(from) == ' ') from++;
        } catch (java.lang.StringIndexOutOfBoundsException ex) {
        }
        return from;
    }

    private final int skap(int from) {
        try {
            while (queryString.charAt(from) == ' ') from--;
        } catch (java.lang.StringIndexOutOfBoundsException ex) {
        }
        return from;
    }

    public int getMatchingBracketPosition(int startposition, String openingBracket, String closingBracket) {
        int openedBrackets = 0;
        int closedBrackets = 0;
        String currentChar = "";
        for (int z = startposition + 1; z < queryString.length(); z++) {
            currentChar = queryString.substring(z, z + 1);
            if (currentChar.equals(closingBracket)) {
                if (openedBrackets == closedBrackets) {
                    return z + 1;
                } else {
                    closedBrackets++;
                }
            } else if (currentChar.equals(openingBracket)) {
                openedBrackets++;
            }
        }
        return -1;
    }

    public final int getEndsAt() {
        return endsAt;
    }

    public final int getConnector() {
        return connector;
    }

    public final MMAX2TermConnector getNextConnector(int from) {
        MMAX2TermConnector connector = null;
        from = skip(from);
        String conni = "";
        int end = queryString.indexOf(" ", from);
        if (end == -1) end = queryString.length();
        conni = queryString.substring(from, end);
        int endsAt = from + conni.length();
        conni = conni.trim();
        if (conni.equalsIgnoreCase("and") || conni.equalsIgnoreCase("&&")) {
            connector = new MMAX2TermConnector(MMAX2TermConnector.AND, endsAt);
        } else if (conni.equalsIgnoreCase("or") || conni.equalsIgnoreCase("||")) {
            connector = new MMAX2TermConnector(MMAX2TermConnector.OR, endsAt);
        }
        return connector;
    }

    public final void dumpTree(int depth) {
        String filler = "";
        for (int z = 0; z < depth; z++) {
            filler = filler + " ";
        }
        String root = "";
        if (this.connector == MMAX2TermConnector.AND) {
            root = "AND";
        } else if (this.connector == MMAX2TermConnector.OR) {
            root = "OR";
        }
        if (negated) root = "NOT " + root;
        System.out.println(filler + root);
        depth = depth + 2;
        filler = filler + "  ";
        for (int z = 0; z < atomicQueryTerms.size(); z++) {
            System.out.println(filler + ((MMAX2AtomicQueryTerm) atomicQueryTerms.get(z)).toCompactString());
        }
        for (int z = 0; z < complexQueryTerms.size(); z++) {
            ((MMAX2ComplexQueryTerm) complexQueryTerms.get(z)).dumpTree(depth);
        }
    }
}
