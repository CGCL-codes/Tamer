package abc.aspectj.visit.patternmatcher;

import soot.Type;
import abc.aspectj.ast.TypePatternExpr;
import abc.weaving.aspectinfo.TypePattern;

class AITypePattern implements TypePattern {

    TypePatternExpr pattern;

    public AITypePattern(TypePatternExpr pattern) {
        this.pattern = pattern;
    }

    public TypePatternExpr getPattern() {
        return pattern;
    }

    public boolean matchesType(Type t) {
        boolean matches = PatternMatcher.v().matchesType(pattern, t.toString());
        if (abc.main.Debug.v().patternMatches) {
            System.err.println("Matching type pattern " + pattern + " against " + t + ": " + matches);
        }
        return matches;
    }

    public String toString() {
        return pattern.toString();
    }

    public boolean equivalent(TypePattern p) {
        return pattern.equivalent(p.getPattern());
    }
}
