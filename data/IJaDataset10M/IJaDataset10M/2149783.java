package com.planet_ink.coffee_mud.Abilities.Prayers;

import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class Prayer_FeignLife extends Prayer {

    public String ID() {
        return "Prayer_FeignLife";
    }

    public String name() {
        return "Feign Life";
    }

    public String displayText() {
        return "(Feign Life)";
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_DEATHLORE;
    }

    protected int canAffectCode() {
        return Ability.CAN_MOBS;
    }

    protected int canTargetCode() {
        return Ability.CAN_MOBS;
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    public long flags() {
        return Ability.FLAG_UNHOLY;
    }

    public void affectCharStats(MOB affected, CharStats affectableStats) {
        super.affectCharStats(affected, affectableStats);
        if (affected == null) return;
        affectableStats.setRaceName("Human");
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if (canBeUninvoked()) if ((mob.location() != null) && (!mob.amDead())) mob.location().show(mob, null, CMMsg.MSG_OK_VISUAL, "<S-YOUPOSS> fake life fades.");
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (mob.isInCombat()) return Ability.QUALITY_INDIFFERENT;
            if (mob.isMonster()) return Ability.QUALITY_INDIFFERENT;
            if (target instanceof MOB) {
                if (((MOB) target).charStats().raceName().equalsIgnoreCase("Human")) return Ability.QUALITY_INDIFFERENT;
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), (auto ? "<T-NAME> gain(s) fake life!" : "^S<S-NAME> " + prayWord(mob) + " for <T-NAMESELF> to gain fake life.^?"));
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                beneficialAffect(mob, target, asLevel, 0);
                target.recoverEnvStats();
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> " + prayWord(mob) + " for <T-NAMESELF> to have a fake life, but nothing happens.");
        return success;
    }
}
