package com.planet_ink.coffee_mud.Abilities.Druid;

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
public class Chant_BullStrength extends Chant {

    public String ID() {
        return "Chant_BullStrength";
    }

    public String name() {
        return "Bull Strength";
    }

    public String displayText() {
        return "(Bull Strength)";
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_SHAPE_SHIFTING;
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_SELF;
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    public void affectCharStats(MOB affected, CharStats affectableStats) {
        super.affectCharStats(affected, affectableStats);
        affectableStats.setStat(CharStats.STAT_STRENGTH, affectableStats.getStat(CharStats.STAT_STRENGTH) + 6 + getXLEVELLevel(invoker()));
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if (canBeUninvoked()) mob.tell("You don't feel quite so strong.");
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = mob;
        if ((auto) && (givenTarget != null) && (givenTarget instanceof MOB)) target = (MOB) givenTarget;
        if (target == null) return false;
        if (target.fetchEffect(ID()) != null) {
            mob.tell(target, null, null, "<S-NAME> already <S-HAS-HAVE> the strength of a bull.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            invoker = mob;
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "<S-NAME> gain(s) the strength of a bull!" : "^S<S-NAME> chant(s) for the strength of a bull!^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                beneficialAffect(mob, target, asLevel, 0);
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> chant(s), but nothing more happens.");
        return success;
    }
}
