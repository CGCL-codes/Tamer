package net.innig.macker.rule;

import net.innig.macker.event.MackerIsMadException;
import net.innig.macker.event.ListenerException;
import net.innig.macker.structure.ClassManager;
import net.innig.macker.structure.ClassInfo;
import java.util.*;
import org.apache.commons.lang.exception.NestableRuntimeException;

public class RuleSet extends Rule {

    public static RuleSet getMackerDefaults() {
        if (defaults == null) try {
            defaults = new RuleSet();
            defaults.setPattern("from", new RegexPattern("${from-full}"));
            defaults.setPattern("to", new RegexPattern("${to-full}"));
        } catch (MackerRegexSyntaxException mrse) {
            throw new NestableRuntimeException("Macker built-ins are broken", mrse);
        }
        return defaults;
    }

    private static RuleSet defaults;

    public RuleSet(RuleSet parent) {
        super(parent);
        if (parent == null) throw new IllegalArgumentException("parent == null");
        patterns = new HashMap();
        rules = new ArrayList();
    }

    private RuleSet() {
        super(null);
        rules = Collections.EMPTY_LIST;
        patterns = new HashMap();
    }

    public String getName() {
        if (name == null) return (getParent() != null) ? getParent().getName() : "<anonymous ruleset>";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return name != null;
    }

    public boolean declaresPattern(String name) {
        return patterns.keySet().contains(name);
    }

    public Pattern getPattern(String name) {
        Pattern pat = (Pattern) patterns.get(name);
        if (pat != null) return pat;
        if (getParent() != null) return getParent().getPattern(name);
        return null;
    }

    public void setPattern(String name, Pattern pattern) {
        if (name == null) throw new NullPointerException("name cannot be null");
        if (pattern == null) throw new NullPointerException("pattern cannot be null");
        patterns.put(name, pattern);
    }

    public Collection getAllPatterns() {
        return patterns.values();
    }

    public void clearPattern(String name) {
        patterns.remove(name);
    }

    public Collection getRules() {
        return rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public Pattern getSubsetPattern() {
        return subsetPat;
    }

    public void setSubsetPattern(Pattern subsetPat) {
        this.subsetPat = subsetPat;
    }

    public boolean isInSubset(EvaluationContext context, ClassInfo classInfo) throws RulesException {
        if (subsetPat != null && !subsetPat.matches(context, classInfo)) return false;
        if (getParent() != null) return getParent().isInSubset(context, classInfo);
        return true;
    }

    public void check(EvaluationContext parentContext, ClassManager classes) throws RulesException, MackerIsMadException, ListenerException {
        EvaluationContext context = new EvaluationContext(this, parentContext);
        context.broadcastStarted();
        boolean finished = false;
        try {
            for (Iterator ruleIter = rules.iterator(); ruleIter.hasNext(); ) {
                Rule rule = (Rule) ruleIter.next();
                rule.check(context, classes);
            }
            context.broadcastFinished();
            finished = true;
        } finally {
            if (!finished) context.broadcastAborted();
        }
    }

    public String toString() {
        return getClass().getName() + '[' + name + ", parent=" + getParent() + ']';
    }

    private String name;

    private Map patterns;

    private Collection rules;

    private Pattern subsetPat;
}
