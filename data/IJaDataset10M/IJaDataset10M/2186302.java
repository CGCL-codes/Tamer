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
public class Spell_WellDressed extends Spell {

    public String ID() {
        return "Spell_WellDressed";
    }

    public String name() {
        return "Well Dressed";
    }

    public String displayText() {
        return "(Well Dressed)";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_SPELL | Ability.DOMAIN_ILLUSION;
    }

    protected int dressCode = 1;

    public void affectCharStats(MOB affected, CharStats affectableStats) {
        super.affectCharStats(affected, affectableStats);
        affectableStats.setStat(CharStats.STAT_CHARISMA, affectableStats.getStat(CharStats.STAT_CHARISMA) + dressCode);
    }

    public void setMiscText(String newText) {
        super.setMiscText(newText);
        if (CMath.isInteger(newText)) dressCode = CMath.s_int(newText);
    }

    public String text() {
        return "" + dressCode;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        super.unInvoke();
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (target instanceof MOB) {
                if ((((MOB) target).isInCombat()) && (!((MOB) target).charStats().getCurrentClass().baseClass().equalsIgnoreCase("Bard"))) return Ability.QUALITY_INDIFFERENT;
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        int newDressCode = 1;
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> speak(s) exquisitely to <T-NAMESELF>.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (target.location() == mob.location()) {
                    beneficialAffect(mob, target, asLevel, 0);
                    Ability A = target.fetchEffect(ID());
                    if (A != null) A.setMiscText("" + newDressCode);
                }
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> speak(s) exquisitely to <T-NAMESELF>, but nothing more happens.");
        return success;
    }
}
