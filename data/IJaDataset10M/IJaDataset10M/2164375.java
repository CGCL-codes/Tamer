package net.innig.macker.rule;

import java.util.*;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

public final class MackerRegex {

    public MackerRegex(String regexStr) throws MackerRegexSyntaxException {
        this(regexStr, true);
    }

    public MackerRegex(String regexStr, boolean allowParts) throws MackerRegexSyntaxException {
        if (regexStr == null) throw new NullPointerException("regexStr == null");
        buildStaticPatterns();
        this.regexStr = regexStr;
        parts = null;
        regex = null;
        prevVarValues = new HashMap();
        if (!(allowParts ? allowable : allowableNoParts).match(regexStr)) throw new MackerRegexSyntaxException(regexStr);
    }

    public String getPatternString() {
        return regexStr;
    }

    private final String regexStr;

    public boolean matches(EvaluationContext context, String s) throws UndeclaredVariableException, MackerRegexSyntaxException {
        return getMatch(context, s) != null;
    }

    public String getMatch(EvaluationContext context, String s) throws UndeclaredVariableException, MackerRegexSyntaxException {
        parseExpr(context);
        Boolean match = (Boolean) matchCache.get(s);
        if (Boolean.FALSE.equals(match)) return null;
        if (Boolean.TRUE.equals(match)) return (String) matchResultCache.get(s);
        match = regex.match('.' + s) ? Boolean.TRUE : Boolean.FALSE;
        matchCache.put(s, match);
        if (match.booleanValue()) {
            String matchResult = regex.getParen(regex.getParenCount() - 1);
            matchResultCache.put(s, matchResult);
            return matchResult;
        } else return null;
    }

    private void parseExpr(EvaluationContext context) throws UndeclaredVariableException, MackerRegexSyntaxException {
        buildStaticPatterns();
        if (parts == null) {
            parts = new ArrayList();
            for (int pos = 0; pos >= 0; ) {
                boolean hasAnotherVar = var.match(regexStr, pos);
                int expEnd = hasAnotherVar ? var.getParenStart(0) : regexStr.length();
                if (pos < expEnd) parts.add(new ExpPart(parseSubexpr(regexStr.substring(pos, expEnd))));
                if (hasAnotherVar) parts.add(new VarPart(var.getParen(1)));
                pos = hasAnotherVar ? var.getParenEnd(0) : -1;
            }
        }
        boolean changed = (regex == null);
        for (Iterator i = prevVarValues.entrySet().iterator(); !changed && i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (!context.getVariableValue(name).equals(value)) changed = true;
        }
        if (changed) {
            StringBuffer builtRegexStr = new StringBuffer("^\\.?");
            for (Iterator i = parts.iterator(); i.hasNext(); ) {
                Part part = (Part) i.next();
                if (part instanceof VarPart) {
                    String varName = ((VarPart) part).varName;
                    String varValue = context.getVariableValue(varName);
                    prevVarValues.put(varName, varValue);
                    builtRegexStr.append(parseSubexpr(varValue));
                } else if (part instanceof ExpPart) builtRegexStr.append(((ExpPart) part).exp);
            }
            builtRegexStr.append('$');
            try {
                regex = new RE(builtRegexStr.toString());
            } catch (RESyntaxException rese) {
                System.out.println("builtRegexStr = " + builtRegexStr);
                throw new MackerRegexSyntaxException(regexStr, rese);
            }
            matchCache = new HashMap();
            matchResultCache = new HashMap();
        }
    }

    private String parseSubexpr(String exp) {
        exp = partBoundary.subst(exp, "[\\.\\$]");
        exp = packageBoundary.subst(exp, "\\.");
        exp = innerClassBoundary.subst(exp, "\\$");
        exp = star.subst(exp, "@");
        exp = matchAcross.subst(exp, ".*");
        exp = matchWithin.subst(exp, "[^\\.]*");
        return exp;
    }

    private static void buildStaticPatterns() {
        if (allowable == null) try {
            star = new RE("\\*");
            matchWithin = new RE("@");
            matchAcross = new RE("@@");
            partBoundary = new RE("\\.");
            packageBoundary = new RE("/");
            innerClassBoundary = new RE("\\$");
            String varS = "\\$\\{([A-Za-z0-9_\\.\\-]+)\\}";
            String partS = "(([A-Za-z_]|[\\(\\)]|\\*|" + varS + ")" + "([A-Za-z0-9_]|[\\(\\)]|\\*|" + varS + ")*)";
            var = new RE(varS);
            allowable = new RE("^([\\$\\./]?" + partS + ")+$", RE.MATCH_SINGLELINE);
            allowableNoParts = new RE("^" + partS + "$", RE.MATCH_SINGLELINE);
        } catch (RESyntaxException rese) {
            rese.printStackTrace(System.out);
            throw new RuntimeException("Can't initialize RegexPattern: " + rese);
        }
    }

    private RE regex;

    private List parts;

    private Map prevVarValues, matchCache, matchResultCache;

    private static RE star, matchWithin, matchAcross, partBoundary, packageBoundary, innerClassBoundary, var, allowable, allowableNoParts;

    private class Part {
    }

    private class VarPart extends Part {

        public VarPart(String varName) {
            this.varName = varName;
        }

        public String varName;

        public String toString() {
            return "var(" + varName + ")";
        }
    }

    private class ExpPart extends Part {

        public ExpPart(String exp) {
            this.exp = exp;
        }

        public String exp;

        public String toString() {
            return "exp(" + exp + ")";
        }
    }

    public String toString() {
        return '"' + regexStr + '"';
    }
}
