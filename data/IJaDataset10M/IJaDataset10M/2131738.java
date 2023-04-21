package riverbed.jelan.parser.softparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import riverbed.jelan.lexer.Token;
import riverbed.jelan.parser.Parser;
import riverbed.jelan.parser.Rule;
import riverbed.jelan.parser.RuleVisitor;

/**
 * A language grammar to be applied by a parser.  
 */
public class ParseGrammar {

    private Rule rule;

    public ParseGrammar(Rule rule) {
        this.rule = rule;
    }

    public Rule.Match require(Parser parser) {
        return rule.require(parser);
    }

    public List<String> check() {
        List<String> errors = new ArrayList<String>();
        GrammarCheck visitor = new GrammarCheck(errors);
        rule.accept(visitor);
        return errors;
    }

    private static class RuleInfo {

        private Set<Token> tokenSet;

        private Set<Class<?>> classSet;

        private boolean mayBeEmpty;

        public RuleInfo() {
            this(new HashSet<Class<?>>(), new HashSet<Token>(), false);
        }

        public RuleInfo(Set<Class<?>> classSet, Set<Token> tokenSet, boolean mayBeEmpty) {
            this.classSet = classSet;
            this.tokenSet = tokenSet;
            this.mayBeEmpty = mayBeEmpty;
        }

        protected Set<Token> getTokenSet() {
            return tokenSet;
        }

        protected Set<Class<?>> getClassSet() {
            return classSet;
        }

        protected boolean mayBeEmpty() {
            return mayBeEmpty;
        }

        public void merge(Rule rule, RuleInfo other, GrammarCheck gc) {
            for (Token t : other.getTokenSet()) if (!tokenSet.add(t)) gc.addError("Duplicate token " + t + " detected in rule " + rule);
            for (Class<?> c : other.getClassSet()) if (!classSet.add(c)) gc.addError("Duplicate class " + c + " detected in rule " + rule);
            mayBeEmpty = mayBeEmpty | other.mayBeEmpty();
        }

        /**
         * @param b 
         * 
         */
        public void setMayBeEmpty(boolean b) {
            mayBeEmpty = b;
        }
    }

    static class GrammarCheck implements RuleVisitor {

        /** Destination for all log output */
        private static final Log log = LogFactory.getLog(GrammarCheck.class);

        private Set<Rule> ruleSet = new HashSet<Rule>();

        private Map<Rule, RuleInfo> ruleInfoMap;

        private RuleInfo currentRuleInfo = new RuleInfo(new HashSet<Class<?>>(), new HashSet<Token>(), false);

        private List<String> errors;

        /**
         * @param errors
         */
        public GrammarCheck(Map<Rule, RuleInfo> ruleStartTokenSet, List<String> errors) {
            this.errors = errors;
            this.ruleInfoMap = ruleStartTokenSet;
        }

        public GrammarCheck(List<String> errors) {
            this(new HashMap<Rule, RuleInfo>(), errors);
        }

        void addError(String error) {
            errors.add(error);
            if (log.isWarnEnabled()) {
                log.warn("Grammar check: " + error);
            }
        }

        public boolean visitNeeded(Rule rule) {
            RuleInfo ruleInfo = ruleInfoMap.get(rule);
            if (ruleInfo == null) {
                if (!ruleSet.add(rule)) {
                    addError(rule + " recursion");
                    return false;
                }
                return true;
            }
            currentRuleInfo.merge(rule, ruleInfo, this);
            return false;
        }

        public RuleInfo startRuleVisit(Rule rule) {
            RuleInfo saveRuleInfo = currentRuleInfo;
            currentRuleInfo = new RuleInfo();
            return saveRuleInfo;
        }

        public void endRuleVisit(Rule rule, RuleInfo savedRuleInfo) {
            RuleInfo ruleInfo = currentRuleInfo;
            ruleInfoMap.put(rule, ruleInfo);
            savedRuleInfo.merge(rule, ruleInfo, this);
            currentRuleInfo = savedRuleInfo;
            ruleSet.remove(rule);
        }

        public void visitCallRule(CallRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (visitNeeded(rule)) {
                RuleInfo savedRuleInfo = startRuleVisit(rule);
                rule.getChildRule().accept(this);
                endRuleVisit(rule, savedRuleInfo);
            }
        }

        public void visitChoiceRule(ChoiceRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (visitNeeded(rule)) {
                RuleInfo savedRuleInfo = startRuleVisit(rule);
                for (int i = 0; i < rule.getRuleCount(); i++) rule.getChildRule(i).accept(this);
                endRuleVisit(rule, savedRuleInfo);
            }
        }

        public void visitEndRule(Rule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
        }

        public void visitOptRule(OptRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (visitNeeded(rule)) {
                RuleInfo savedRuleInfo = startRuleVisit(rule);
                rule.getChildRule().accept(this);
                currentRuleInfo.setMayBeEmpty(true);
                endRuleVisit(rule, savedRuleInfo);
            }
        }

        public void visitRepeatRule(RepeatRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (visitNeeded(rule)) {
                RuleInfo savedRuleInfo = startRuleVisit(rule);
                rule.getChildRule().accept(this);
                if (rule.getRepeatCount() == 0) currentRuleInfo.setMayBeEmpty(true);
                endRuleVisit(rule, savedRuleInfo);
            }
        }

        public void visitSaveTokenRule(SaveTokenRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (visitNeeded(rule)) {
                RuleInfo savedRuleInfo = startRuleVisit(rule);
                rule.getChildRule().accept(this);
                endRuleVisit(rule, savedRuleInfo);
            }
        }

        public void visitSeqRule(SeqRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (visitNeeded(rule)) {
                RuleInfo savedRuleInfo = startRuleVisit(rule);
                Iterator<Rule> i = rule.getChildRules();
                boolean finished = false;
                while (!finished && i.hasNext()) {
                    currentRuleInfo.setMayBeEmpty(false);
                    i.next().accept(this);
                    finished = !currentRuleInfo.mayBeEmpty();
                }
                endRuleVisit(rule, savedRuleInfo);
                while (i.hasNext()) {
                    GrammarCheck gc = new GrammarCheck(ruleInfoMap, errors);
                    i.next().accept(gc);
                }
            }
        }

        public void visitStartRule(Rule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
        }

        public void visitTokenChoiceRule(TokenChoiceRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (visitNeeded(rule)) {
                RuleInfo savedRuleInfo = startRuleVisit(rule);
                Iterator<Token> i = rule.getChildTokens();
                while (i.hasNext()) {
                    visitTokenRule(i.next());
                }
                endRuleVisit(rule, savedRuleInfo);
            }
        }

        public void visitTokenRule(Token token) {
            if (log.isDebugEnabled()) log.debug("visit " + token);
            if (!currentRuleInfo.getTokenSet().add(token)) {
                addError("Duplicate start token " + token);
            }
        }

        public void visitTokenTypeRule(TokenTypeRule rule) {
            if (log.isDebugEnabled()) log.debug("visit " + rule);
            if (!currentRuleInfo.getClassSet().add(rule.getTokenType())) {
                addError("Duplicate start token type " + rule.getTokenType());
            }
        }
    }
}
