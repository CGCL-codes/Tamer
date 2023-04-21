package com.planet_ink.coffee_mud.Abilities.Druid;

import com.planet_ink.coffee_mud.Abilities.StdAbility;
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
public class Druid_Bite extends StdAbility {

    public String ID() {
        return "Druid_Bite";
    }

    public String name() {
        return "Bite";
    }

    private static final String[] triggerStrings = { "BITE" };

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    public String[] triggerStrings() {
        return triggerStrings;
    }

    protected int canAffectCode() {
        return 0;
    }

    protected int canTargetCode() {
        return Ability.CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_SKILL | Ability.DOMAIN_WEAPON_USE;
    }

    public int usageType() {
        return USAGE_MOVEMENT;
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (mob.isInCombat() && (mob.rangeToTarget() > 0)) return Ability.QUALITY_INDIFFERENT;
            if (!Druid_ShapeShift.isShapeShifted(mob)) return Ability.QUALITY_INDIFFERENT;
            if (mob.charStats().getBodyPart(Race.BODY_MOUTH) <= 0) return Ability.QUALITY_INDIFFERENT;
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        if (mob.isInCombat() && (mob.rangeToTarget() > 0)) {
            mob.tell("You are too far away to bite!");
            return false;
        }
        if (!Druid_ShapeShift.isShapeShifted(mob)) {
            mob.tell("You must be in your animal form to bite.");
            return false;
        }
        if (mob.charStats().getBodyPart(Race.BODY_MOUTH) <= 0) {
            mob.tell("You must have a mouth to bite!");
            return false;
        }
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, mob.charStats().getStat(CharStats.STAT_STRENGTH) - target.charStats().getStat(CharStats.STAT_STRENGTH) - 10, auto);
        if (success) {
            invoker = mob;
            int topDamage = adjustedLevel(mob, asLevel) + 2 + getX2Level(mob);
            int damage = CMLib.dice().roll(1, topDamage, 0);
            CMMsg msg = CMClass.getMsg(mob, target, this, CMMsg.MSK_MALICIOUS_MOVE | CMMsg.TYP_JUSTICE | (auto ? CMMsg.MASK_ALWAYS : 0), null);
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (msg.value() > 0) damage = (int) Math.round(CMath.div(damage, 2.0));
                CMLib.combat().postDamage(mob, target, this, damage, CMMsg.MASK_ALWAYS | CMMsg.TYP_JUSTICE, Weapon.TYPE_PIERCING, "^F^<FIGHT^><S-NAME> <DAMAGE> <T-NAME> with a piercing BITE!^</FIGHT^>^?");
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> fail(s) to bite <T-NAMESELF>.");
        return success;
    }
}
