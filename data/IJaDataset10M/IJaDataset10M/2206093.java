package com.netime.commons.standard.rule;

public abstract class BaseLogic {

    protected BaseLogic() {
    }

    public static final BaseLogic newInstance(String name) {
        if (name.equalsIgnoreCase("mustBe")) {
            return new LogicMustBe();
        } else if (name.equalsIgnoreCase("notIn")) {
            return new LogicNotIn("notIn");
        } else if (name.equalsIgnoreCase("lessThan")) {
            return new LogicLessThan("lessThan");
        } else if (name.equalsIgnoreCase("largerThan")) {
            return new LogicLargerThan("largerThan");
        } else if (name.equalsIgnoreCase("mustHave")) {
            return new LogicMustHave("mustHave");
        } else if (name.equalsIgnoreCase("mustNotHave")) {
            return new LogicMustNotHave("mustNotHave");
        }
        return null;
    }
}
