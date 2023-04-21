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
public class Prayer_SunCurse extends Prayer {

    public String ID() {
        return "Prayer_SunCurse";
    }

    public String name() {
        return "Sun Curse";
    }

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_CURSING;
    }

    public long flags() {
        return Ability.FLAG_UNHOLY;
    }

    public String displayText() {
        return "(Sun Curse)";
    }

    protected int canAffectCode() {
        return Ability.CAN_MOBS;
    }

    protected int canTargetCode() {
        return Ability.CAN_MOBS;
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        if (!(affected instanceof MOB)) return;
        if (((MOB) affected).location() == null) return;
        if (CMLib.flags().isInDark(((MOB) affected).location())) affectableStats.setSensesMask(affectableStats.sensesMask() | EnvStats.CAN_SEE_DARK); else affectableStats.setSensesMask(affectableStats.sensesMask() | EnvStats.CAN_NOT_SEE);
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (!super.tick(ticking, tickID)) return false;
        if ((affected == null) || (!(affected instanceof MOB))) return super.tick(ticking, tickID);
        MOB mob = (MOB) affected;
        if ((mob.location() != null) && (mob.location().getArea().getClimateObj().canSeeTheSun(mob.location())) && (CMLib.flags().isInTheGame(mob, false))) {
            mob.tell("\n\r\n\r\n\r\n\r**THE SUN IS BEATING ONTO YOUR SKIN**\n\r\n\r");
            Ability A = CMClass.getAbility("Spell_FleshStone");
            if (A != null) A.invoke(mob, mob, true, 0);
            unInvoke();
            return false;
        }
        return true;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if ((canBeUninvoked()) && (mob.fetchEffect("Spell_FleshStone") == null)) mob.tell("Your sun curse is lifted.");
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (!mob.location().getArea().getClimateObj().canSeeTheSun(mob.location())) return Ability.QUALITY_INDIFFERENT;
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if ((!auto) && (target.location() != null) && (target.location().getArea().getClimateObj().canSeeTheSun(target.location()))) {
            mob.tell("This cannot be prayed for while the sun is shining on you.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        int adjustment = target.envStats().level() - ((mob.envStats().level() + super.getXLEVELLevel(mob)) / 2);
        boolean success = proficiencyCheck(mob, -adjustment, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> " + prayWord(mob) + " for an unholy sun curse upon <T-NAMESELF>.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (msg.value() <= 0) {
                    success = maliciousAffect(mob, target, asLevel, 0, -1);
                    mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> <S-IS-ARE> under a mighty sun curse!");
                }
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> attempt(s) to sun curse <T-NAMESELF>, but flub(s) it.");
        return success;
    }
}
