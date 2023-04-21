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
public class Chant_RedMoon extends Chant {

    public String ID() {
        return "Chant_RedMoon";
    }

    public String name() {
        return "Red Moon";
    }

    public String displayText() {
        return "(Red Moon)";
    }

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    protected int canAffectCode() {
        return CAN_ROOMS;
    }

    protected int canTargetCode() {
        return 0;
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_MOONALTERING;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof Room))) return;
        Room R = (Room) affected;
        if (canBeUninvoked()) R.showHappens(CMMsg.MSG_OK_VISUAL, "The red moon sets.");
        super.unInvoke();
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if (!super.okMessage(myHost, msg)) return false;
        if ((msg.targetMinor() == CMMsg.TYP_DAMAGE) && (msg.target() != null) && (msg.target() instanceof MOB)) {
            MOB mob = (MOB) msg.target();
            int recovery = (int) Math.round(CMath.div((msg.value()), 2.0));
            if (CMLib.flags().isEvil(mob)) recovery = recovery * -1;
            msg.setValue(msg.value() + recovery);
        }
        return true;
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (!super.tick(ticking, tickID)) return false;
        if (affected == null) return false;
        if (affected instanceof Room) {
            Room R = (Room) affected;
            if (!R.getArea().getClimateObj().canSeeTheMoon(R, this)) unInvoke();
        }
        return true;
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (!CMLib.flags().isEvil(mob)) return Ability.QUALITY_INDIFFERENT;
            if ((mob.isInCombat()) && CMLib.flags().isEvil(mob.getVictim())) return Ability.QUALITY_INDIFFERENT;
            Room R = mob.location();
            if (R != null) {
                if (!R.getArea().getClimateObj().canSeeTheMoon(R, null)) return Ability.QUALITY_INDIFFERENT;
                if (R.fetchEffect(ID()) != null) return Ability.QUALITY_INDIFFERENT;
                for (int a = 0; a < R.numEffects(); a++) {
                    Ability A = R.fetchEffect(a);
                    if ((A != null) && ((A.classificationCode() & Ability.ALL_DOMAINS) == Ability.DOMAIN_MOONALTERING)) return Ability.QUALITY_INDIFFERENT;
                }
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        Room target = mob.location();
        if (target == null) return false;
        if (!target.getArea().getClimateObj().canSeeTheMoon(target, null)) {
            mob.tell("You must be able to see the moon for this magic to work.");
            return false;
        }
        if (target.fetchEffect(ID()) != null) {
            mob.tell("This place is already under the red moon.");
            return false;
        }
        for (int a = 0; a < target.numEffects(); a++) {
            Ability A = target.fetchEffect(a);
            if ((A != null) && ((A.classificationCode() & Ability.ALL_DOMAINS) == Ability.DOMAIN_MOONALTERING)) {
                mob.tell("The moon is already under " + A.name() + ", and can not be changed until this magic is gone.");
                return false;
            }
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            invoker = mob;
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> chant(s) to the sky.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (msg.value() <= 0) {
                    mob.location().showHappens(CMMsg.MSG_OK_VISUAL, "The Red Moon Rises!");
                    beneficialAffect(mob, target, asLevel, 0);
                }
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> chant(s) to the sky, but the magic fades.");
        return success;
    }
}
