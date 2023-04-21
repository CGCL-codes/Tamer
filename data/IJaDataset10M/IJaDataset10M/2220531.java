package serene.validation.schema.active.components;

import serene.validation.schema.active.components.APattern;
import serene.validation.schema.active.components.CharsActiveTypeItem;
import serene.validation.handlers.structure.impl.ActiveModelRuleHandlerPool;

public abstract class CharsAPattern extends NoChildrenAPattern implements CharsActiveTypeItem {

    CharsAPattern(ActiveModelRuleHandlerPool ruleHandlerPool) {
        super(ruleHandlerPool);
    }

    public String toString() {
        String s = "CharsAPattern";
        return s;
    }
}
