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
public class Prayer_CureDeafness extends Prayer implements MendingSkill {

    public String ID() {
        return "Prayer_CureDeafness";
    }

    public String name() {
        return "Cure Deafness";
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
        MOB caster = CMClass.getMOB("StdMOB");
        caster.baseEnvStats().setLevel(CMProps.getIntVar(CMProps.SYSTEMI_LASTPLAYERLEVEL));
        caster.envStats().setLevel(CMProps.getIntVar(CMProps.SYSTEMI_LASTPLAYERLEVEL));
        boolean canMend = returnOffensiveAffects(caster, E).size() > 0;
        caster.destroy();
        return canMend;
    }

    public Vector returnOffensiveAffects(MOB caster, Environmental fromMe) {
        MOB newMOB = CMClass.getMOB("StdMOB");
        Vector offenders = new Vector();
        for (int a = 0; a < fromMe.numEffects(); a++) {
            Ability A = fromMe.fetchEffect(a);
            if (A != null) {
                newMOB.recoverEnvStats();
                A.affectEnvStats(newMOB, newMOB.envStats());
                if ((!CMLib.flags().canHear(newMOB)) && ((A.invoker() == null) || ((A.invoker() != null) && (A.invoker().envStats().level() <= caster.envStats().level() + 1 + (2 * getXLEVELLevel(caster)))))) offenders.addElement(A);
            }
        }
        newMOB.destroy();
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
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        Vector offensiveAffects = returnOffensiveAffects(mob, target);
        if ((success) && (offensiveAffects.size() > 0)) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "A visible glow surrounds <T-NAME>." : "^S<S-NAME> " + prayWord(mob) + " for <T-NAMESELF> to hear <S-HIS-HER> prayer.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                for (int a = offensiveAffects.size() - 1; a >= 0; a--) ((Ability) offensiveAffects.elementAt(a)).unInvoke();
            }
        } else beneficialWordsFizzle(mob, target, auto ? "" : "<S-NAME> " + prayWord(mob) + " for <T-NAMESELF>, but nothing happens.");
        return success;
    }
}
