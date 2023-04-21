package com.planet_ink.coffee_mud.Abilities.Spells;

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
public class Spell_ResistParalyzation extends Spell {

    public String ID() {
        return "Spell_ResistParalyzation";
    }

    public String name() {
        return "Resist Paralysis";
    }

    public String displayText() {
        return "(Resist Paralysis)";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_SPELL | Ability.DOMAIN_ABJURATION;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        if (canBeUninvoked()) mob.tell("Your liberating protection fades away.");
        super.unInvoke();
    }

    public void affectCharStats(MOB affectedMOB, CharStats affectedStats) {
        super.affectCharStats(affectedMOB, affectedStats);
        affectedStats.setStat(CharStats.STAT_SAVE_PARALYSIS, affectedStats.getStat(CharStats.STAT_SAVE_PARALYSIS) + 100);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "<T-NAME> feel(s) liberatingly protected." : "^S<S-NAME> invoke(s) a liberating field of protection around <T-NAMESELF>.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                beneficialAffect(mob, target, asLevel, 0);
            }
        } else beneficialWordsFizzle(mob, target, "<S-NAME> attempt(s) to invoke liberating protection, but fail(s).");
        return success;
    }
}
