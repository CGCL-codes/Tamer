package com.sylli.oeuf.server.game.logic.action;

import com.sylli.oeuf.server.game.logic.BattleActor;

public class Healing extends ActionValue {

    public Healing(BattleActor source, BattleActor target, int value, ActionValueFlag flag, ActionValueElement element) {
        super(source, target, value, flag, element);
    }

    @Override
    public String toString() {
        return value + " health" + (flag.equals(ActionValueFlag.Critical) ? " [Critical]" : "");
    }
}
