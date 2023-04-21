package net.innig.macker.rule;

import net.innig.util.EnumeratedType;

public final class AccessRuleType extends EnumeratedType {

    public static final AccessRuleType ALLOW = new AccessRuleType("allow"), DENY = new AccessRuleType("deny");

    private AccessRuleType(String name) {
        super(name);
    }
}
