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
public class Prayer_RemovePoison extends Prayer implements MendingSkill {

    public String ID() {
        return "Prayer_RemovePoison";
    }

    public String name() {
        return "Remove Poison";
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_RESTORATION;
    }

    public int abstractQuality() {
        return Ability.QUALITY_OK_OTHERS;
    }

    public long flags() {
        return Ability.FLAG_HOLY;
    }

    public boolean supportsMending(Environmental E) {
        if (!(E instanceof MOB)) return false;
        boolean canMend = returnOffensiveAffects(E).size() > 0;
        return canMend;
    }

    public static Vector returnOffensiveAffects(Environmental fromMe) {
        Vector offenders = new Vector();
        for (int a = 0; a < fromMe.numEffects(); a++) {
            Ability A = fromMe.fetchEffect(a);
            if ((A != null) && ((A.classificationCode() & Ability.ALL_ACODES) == Ability.ACODE_POISON)) offenders.addElement(A);
        }
        return offenders;
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (target instanceof MOB) {
                if (supportsMending((MOB) target)) return super.castingQuality(mob, target, Ability.QUALITY_BENEFICIAL_OTHERS);
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        Environmental target = getAnyTarget(mob, commands, givenTarget, Wearable.FILTER_UNWORNONLY);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        Vector offensiveAffects = returnOffensiveAffects(target);
        if ((success) && (offensiveAffects.size() > 0)) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "<T-NAME> feel(s) purified of <T-HIS-HER> poisons." : "^S<S-NAME> " + prayWord(mob) + " that <T-NAME> be purified of <T-HIS-HER> poisons.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                for (int a = offensiveAffects.size() - 1; a >= 0; a--) ((Ability) offensiveAffects.elementAt(a)).unInvoke();
                if ((target instanceof Drink) && (((Drink) target).liquidHeld() == RawMaterial.RESOURCE_POISON)) {
                    ((Drink) target).setLiquidHeld(RawMaterial.RESOURCE_FRESHWATER);
                    target.baseEnvStats().setAbility(0);
                }
                if (!CMLib.flags().stillAffectedBy(target, offensiveAffects, false)) {
                    if (target instanceof MOB) {
                        ((MOB) target).tell("You feel much better!");
                        ((MOB) target).recoverCharStats();
                        ((MOB) target).recoverMaxState();
                    }
                }
                target.recoverEnvStats();
            }
        } else if (!auto) beneficialWordsFizzle(mob, target, auto ? "" : "<S-NAME> " + prayWord(mob) + " that <T-NAME> be purified of <T-HIS-HER> poisons, but there is no answer.");
        return success;
    }
}
