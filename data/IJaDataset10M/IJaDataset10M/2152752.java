package com.sylli.oeuf.server.game.logic.action;

import com.sylli.oeuf.server.game.logic.BattleActor;
import com.sylli.oeuf.server.game.logic.Buff;

public class ActionAddBuff extends ActionAppliance {

    private Buff bufftoadd;

    public ActionAddBuff(BattleActor source, BattleActor target, Buff bf) {
        super(source, target);
        bufftoadd = bf;
    }

    public Buff getBufftoadd() {
        return bufftoadd;
    }

    public void setBufftoadd(Buff bufftoadd) {
        this.bufftoadd = bufftoadd;
    }
}
