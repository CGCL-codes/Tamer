package abc.aspectj.visit.patternmatcher;

import java.util.LinkedList;
import soot.Modifier;
import soot.SootMethod;
import soot.SootClass;
import soot.SootMethodRef;
import abc.weaving.aspectinfo.MethodCategory;

class AIMethodPattern implements abc.weaving.aspectinfo.MethodPattern {

    private abc.aspectj.ast.MethodPattern pattern;

    public AIMethodPattern(abc.aspectj.ast.MethodPattern pattern) {
        this.pattern = pattern;
    }

    public abc.aspectj.ast.MethodPattern getPattern() {
        return pattern;
    }

    public boolean matchesExecution(SootMethod method) {
        int mods = MethodCategory.getModifiers(method);
        String name = MethodCategory.getName(method);
        SootClass realcl = MethodCategory.getClass(method);
        LinkedList ftypes = new LinkedList(method.getParameterTypes());
        int skip_first = MethodCategory.getSkipFirst(method);
        int skip_last = MethodCategory.getSkipLast(method);
        while (skip_first-- > 0) ftypes.removeFirst();
        while (skip_last-- > 0) ftypes.removeLast();
        boolean matches = PatternMatcher.v().matchesType(pattern.getType(), method.getReturnType().toString()) && pattern.getName().name().getPattern().matcher(name).matches() && PatternMatcher.v().matchesFormals(pattern.getFormals(), ftypes) && PatternMatcher.v().matchesModifiers(pattern.getModifiers(), mods) && PatternMatcher.v().matchesThrows(pattern.getThrowspats(), method.getExceptions());
        if (Modifier.isStatic(mods)) {
            matches = matches && PatternMatcher.v().matchesClass(pattern.getName().base(), realcl);
        } else {
            matches = matches && (PatternMatcher.v().matchesClass(pattern.getName().base(), realcl) || PatternMatcher.v().matchesClassWithMethodMatching(pattern.getName().base(), realcl, name, ftypes, method.getReturnType(), false));
        }
        if (abc.main.Debug.v().patternMatches) {
            System.err.println("Matching method execution pattern " + pattern + " against (" + mods + " " + realcl + "." + name + ") " + method + ": " + matches);
        }
        return matches;
    }

    public boolean matchesCall(SootMethodRef methodref) {
        SootMethod method = methodref.resolve();
        boolean matches = PatternMatcher.v().matchesType(pattern.getType(), method.getReturnType().toString()) && pattern.getName().name().getPattern().matcher(MethodCategory.getName(method)).matches() && PatternMatcher.v().matchesFormals(pattern.getFormals(), method.getParameterTypes()) && PatternMatcher.v().matchesModifiers(pattern.getModifiers(), method.getModifiers()) && PatternMatcher.v().matchesThrows(pattern.getThrowspats(), method.getExceptions());
        if (Modifier.isStatic(method.getModifiers())) {
            matches = matches && PatternMatcher.v().matchesClass(pattern.getName().base(), method.getDeclaringClass());
        } else {
            matches = matches && PatternMatcher.v().matchesClassWithMethodMatching(pattern.getName().base(), methodref.declaringClass(), method.getName(), method.getParameterTypes(), method.getReturnType(), false);
        }
        if (abc.main.Debug.v().patternMatches) {
            System.err.println("Matching method call pattern " + pattern + " against " + methodref + ": " + matches);
        }
        return matches;
    }

    public String toString() {
        return pattern.toString();
    }

    public boolean equivalent(abc.weaving.aspectinfo.MethodPattern otherpat) {
        return pattern.equivalent(otherpat.getPattern());
    }
}
