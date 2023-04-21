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
public class Prayer_Earthshield extends Prayer {

    public String ID() {
        return "Prayer_Earthshield";
    }

    public String name() {
        return "Earthshield";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_SELF;
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_HOLYPROTECTION;
    }

    public long flags() {
        return Ability.FLAG_HOLY | Ability.FLAG_UNHOLY;
    }

    public String displayText() {
        return "(In Earthshield)";
    }

    protected int canAffectCode() {
        return Ability.CAN_MOBS;
    }

    protected int canTargetCode() {
        return Ability.CAN_MOBS;
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        if (affected == null) return;
        if (!(affected instanceof MOB)) return;
        affectableStats.setArmor(affectableStats.armor() - 5 - (adjustedLevel(invoker(), 0) / 5));
    }

    public void affectCharState(MOB affected, CharState affectableState) {
        super.affectCharState(affected, affectableState);
        affectableState.setMovement(affectableState.getMovement() / 2);
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if (canBeUninvoked()) if ((mob.location() != null) && (!mob.amDead())) mob.location().show(mob, null, CMMsg.MSG_OK_VISUAL, "<S-YOUPOSS> earth shield vanishes.");
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = mob;
        if ((auto) && (givenTarget != null) && (givenTarget instanceof MOB)) target = (MOB) givenTarget;
        if (target.fetchEffect(this.ID()) != null) {
            mob.tell(target, null, null, "<S-NAME> <S-IS-ARE> already affected by " + name() + ".");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> " + prayWord(mob) + ".^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (msg.value() <= 0) {
                    success = beneficialAffect(mob, target, asLevel, 0);
                    mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> <S-IS-ARE> covered by an Earth Shield!");
                }
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> " + prayWord(mob) + ", but flub(s) it.");
        return success;
    }
}
