package com.sylli.oeuf.server.game.logic.skill.passive;

import java.util.ArrayList;
import java.util.Collection;
import com.sylli.oeuf.server.game.logic.ActionProduct;
import com.sylli.oeuf.server.game.logic.BattleActor;
import com.sylli.oeuf.server.game.logic.CastingTarget;
import com.sylli.oeuf.server.game.logic.EventParameter;
import com.sylli.oeuf.server.game.logic.PassiveSkill;
import com.sylli.oeuf.server.game.logic.action.CombatLog;
import com.sylli.oeuf.server.game.logic.action.Damage;
import com.sylli.oeuf.server.game.logic.invocation.InvocationAction;
import com.sylli.oeuf.server.game.logic.invocation.InvocationCondition;
import com.sylli.oeuf.server.game.logic.invocation.InvocationEntry;
import com.sylli.oeuf.server.game.logic.invocation.InvocationEvent;

public class Exorcist_UndeadMastery extends PassiveSkill {

    public Exorcist_UndeadMastery() {
        super("undead mastery", -1);
        getEntries().put(InvocationEvent.STD_IE_ON_DEAL_DAMAGE, new InvocationEntry(InvocationCondition.ALWAYS_TRUE_IC, new InvocationAction() {

            @Override
            public Collection<ActionProduct> act(int rank, BattleActor caster, CastingTarget target, EventParameter ep) {
                float addition = (float) (0.05 * rank);
                float modifier = (float) (1.0 + addition);
                Collection<ActionProduct> product = new ArrayList<ActionProduct>();
                if (ep instanceof Damage) {
                    Damage d = (Damage) ep;
                    d.setValue((int) (d.getValue() * modifier));
                    product.add(new CombatLog(caster, caster.getProperTarget(CastingTarget.FocusTarget), caster.getName() + "'s damage is increased by " + formatter.format(addition * 100) + "% [" + name + "]."));
                }
                return product;
            }
        }));
    }
}
