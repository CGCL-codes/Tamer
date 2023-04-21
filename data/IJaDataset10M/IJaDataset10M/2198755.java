package com.sylli.oeuf.server.game.logic.action;

import com.sylli.oeuf.server.game.logic.BattleActor;
import com.sylli.oeuf.server.game.logic.DeBuff;

public class ActionAddDebuff extends ActionAppliance {

    private DeBuff debufftoadd;

    public ActionAddDebuff(BattleActor source, BattleActor target, DeBuff db) {
        super(source, target);
        debufftoadd = db;
    }

    public DeBuff getDebufftoadd() {
        return debufftoadd;
    }

    public void setDebufftoadd(DeBuff debufftoadd) {
        this.debufftoadd = debufftoadd;
    }
}
