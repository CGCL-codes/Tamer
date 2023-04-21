package org.progeeks.parser;

import java.util.*;
import org.progeeks.parser.rule.*;

/**
 *  Contains the necessary information for a parser to process
 *  a series of tokens into a syntax production.  Work in progress.
 *
 *  @version   $Revision: 1.4 $
 *  @author    Paul Speed
 */
public class GrammarConfiguration {

    private Rule rootRule;

    private Set filteredTokens = new HashSet();

    private Map productionRules = new HashMap();

    public GrammarConfiguration() {
    }

    /**
     *  Sets the specified production factory to all production level rules.
     *  If null is specified then all rules return to the default AST
     *  production factory.
     */
    public void resetProductionFactories(ProductionFactory factory) {
        for (Iterator i = productionRules.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            Rule r = (Rule) e.getValue();
            r.setProductionFactory(factory);
        }
    }

    /**
     *  Sets a set of production factories for a set of named rules.
     *  Each key in the specified map refers to a rule name and the value
     *  is the factory to set to the rule.
     */
    public void setProductionFactories(Map factories) {
        for (Iterator i = factories.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            Rule r = getProductionRule((String) e.getKey());
            if (r == null) throw new RuntimeException("No rule found for production name:" + e.getKey());
            r.setProductionFactory((ProductionFactory) e.getValue());
        }
    }

    /**
     *  Sets the production factory for the specified named rule.
     */
    public void setProductionFactory(String rule, ProductionFactory factory) {
        Rule r = getProductionRule(rule);
        r.setProductionFactory(factory);
    }

    public void setRootRule(Rule root) {
        this.rootRule = root;
        rootRule.initialize();
    }

    public Rule getRootRule() {
        return (rootRule);
    }

    /**
     *  Adds the rule to this grammar as a production that can later
     *  be looked up by name.
     *  @return Returns the supplied rule to the caller for convenience.
     */
    public Rule addProductionRule(Rule rule) {
        if (!rule.hasName()) throw new IllegalArgumentException("Production rule does not have a name defined.");
        productionRules.put(rule.getName(), rule);
        return (rule);
    }

    /**
     *  Looks up a production rule by name.
     */
    public Rule getProductionRule(String name) {
        return ((Rule) productionRules.get(name));
    }

    /**
     *  Add a token to the set of token types that should be filtered
     *  when processing the token stream.  Note these types are
     *  very specific right now and do not account for subclasses... yet.
     */
    public void addFilteredTokenType(TokenType token) {
        filteredTokens.add(token);
    }

    /**
     *  Returns the set of token types that should be filtered when
     *  processing the token stream.
     */
    public Set getFilteredTokenTypes() {
        return (filteredTokens);
    }
}
