package parser;

import parser.interfaces.RuleActionFactory;

public class RAFactory implements RuleActionFactory<RA> {

    @Override
    public RA create(Rule r) {
        return new RA(r);
    }
}
