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
public class Prayer_RemoveCurse extends Prayer implements MendingSkill {

    public String ID() {
        return "Prayer_RemoveCurse";
    }

    public String name() {
        return "Remove Curse";
    }

    protected int canAffectCode() {
        return 0;
    }

    protected int canTargetCode() {
        return Ability.CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_BLESSING;
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    public long flags() {
        return Ability.FLAG_HOLY | Ability.FLAG_UNHOLY;
    }

    public boolean supportsMending(Environmental E) {
        if (!(E instanceof MOB)) return false;
        return CMLib.flags().domainAffects(E, Ability.DOMAIN_CURSING).size() > 0;
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (target instanceof MOB) {
                if (!supportsMending(target)) return Ability.QUALITY_INDIFFERENT;
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
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "^SA glow surrounds <T-NAME>.^?" : "^S<S-NAME> call(s) on " + hisHerDiety(mob) + " for <T-NAME> to be released from a curse.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                Item I = Prayer_Bless.getSomething(target, true);
                Item lastI = null;
                while (I != null) {
                    if (lastI == I) {
                        CMMsg msg2 = CMClass.getMsg(target, I, null, CMMsg.MASK_ALWAYS | CMMsg.MSG_DROP, "<S-NAME> release(s) <T-NAME>.");
                        target.location().send(target, msg2);
                    } else {
                        CMLib.flags().setRemovable(I, true);
                        CMLib.flags().setDroppable(I, true);
                    }
                    Prayer_Bless.endLowerCurses(I, adjustedLevel(mob, asLevel));
                    I.recoverEnvStats();
                    lastI = I;
                    I = Prayer_Bless.getSomething(target, true);
                }
                Prayer_Bless.endLowerCurses(target, adjustedLevel(mob, asLevel));
                target.recoverEnvStats();
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> call(s) on " + hisHerDiety(mob) + " to release <T-NAME> from a curse, but nothing happens.");
        return success;
    }
}
