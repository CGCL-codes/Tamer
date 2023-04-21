package org.ctor.dev.llrps2.session;

import org.ctor.dev.llrps2.model.GameRule;

public final class GameRuleMapper {

    private GameRuleMapper() {
    }

    public static GameRule sessionToModel(String ruleId) {
        if (ruleId.equals("1")) {
            return GameRule.Normal;
        }
        throw new IllegalArgumentException("unknown rule ID: " + ruleId);
    }

    public static String modelToSession(GameRule rule) {
        switch(rule) {
            case Normal:
                return "1";
            default:
                throw new IllegalArgumentException("unknown rule: " + rule);
        }
    }
}
