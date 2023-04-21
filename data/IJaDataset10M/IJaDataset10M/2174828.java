package de.jcmdlineparser.strategy.escape;

import de.jcmdlineparser.strategy.StrategyRules;

public class StringHandlerImpl implements StringHandler {

    private final StrategyRules rules;

    public StringHandlerImpl(StrategyRules rules) {
        this.rules = rules;
    }

    public String handleString(String string) {
        return new StringHanderNormal(rules).handleString(string);
    }
}
