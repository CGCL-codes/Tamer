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
public class Chant_SacredEarth extends Chant {

    public String ID() {
        return "Chant_SacredEarth";
    }

    public String name() {
        return "Sacred Earth";
    }

    public String displayText() {
        return "(Sacred Earth)";
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_ENDURING;
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

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof Room))) return;
        Room R = (Room) affected;
        if (canBeUninvoked()) R.showHappens(CMMsg.MSG_OK_VISUAL, "The sacred earth charm is ended.");
        super.unInvoke();
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if (!super.okMessage(myHost, msg)) return false;
        if ((msg.tool() instanceof Ability) && ((((Ability) msg.tool()).classificationCode() & Ability.ALL_DOMAINS) == Ability.DOMAIN_GATHERINGSKILL)) {
            msg.source().tell("The sacred earth will not allow you to violate it.");
            return false;
        }
        if ((msg.targetMinor() == CMMsg.TYP_DAMAGE) && (msg.target() != null) && (msg.target() instanceof MOB) && ((((MOB) msg.target()).charStats().getMyRace().racialCategory().equals("Vegetation")) || (((MOB) msg.target()).charStats().getMyRace().racialCategory().equals("Earth Elemental")))) {
            int recovery = (int) Math.round(CMath.div((msg.value()), 2.0));
            msg.setValue(msg.value() - recovery);
        }
        return true;
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            Room R = mob.location();
            if (R != null) {
                if (((R.domainType() & Room.INDOORS) > 0) || (R.domainType() == Room.DOMAIN_OUTDOORS_UNDERWATER) || (R.domainType() == Room.DOMAIN_OUTDOORS_WATERSURFACE) || (R.domainType() == Room.DOMAIN_OUTDOORS_AIR)) return Ability.QUALITY_INDIFFERENT;
            }
            if (mob.isInCombat()) {
                MOB victim = mob.getVictim();
                if (victim != null) {
                    if (((((MOB) victim).charStats().getMyRace().racialCategory().equals("Vegetation")) || (((MOB) victim).charStats().getMyRace().racialCategory().equals("Earth Elemental")))) return Ability.QUALITY_INDIFFERENT;
                }
                if (((!mob.charStats().getMyRace().racialCategory().equals("Vegetation")) && (!mob.charStats().getMyRace().racialCategory().equals("Earth Elemental")))) return Ability.QUALITY_INDIFFERENT;
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        Room target = mob.location();
        if (target == null) return false;
        if (target.fetchEffect(ID()) != null) {
            mob.tell("This earth is already sacred.");
            return false;
        }
        if ((((mob.location().domainType() & Room.INDOORS) > 0) || (mob.location().domainType() == Room.DOMAIN_OUTDOORS_UNDERWATER) || (mob.location().domainType() == Room.DOMAIN_OUTDOORS_WATERSURFACE) || (mob.location().domainType() == Room.DOMAIN_OUTDOORS_AIR)) && (!auto)) {
            mob.tell("This chant will not work here.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            invoker = mob;
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> chant(s) to the ground.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (msg.value() <= 0) {
                    mob.location().showHappens(CMMsg.MSG_OK_VISUAL, "The charm of the sacred earth begins here!");
                    beneficialAffect(mob, target, asLevel, 0);
                    for (int d = Directions.NUM_DIRECTIONS() - 1; d >= 0; d--) {
                        Room R = mob.location().getRoomInDir(d);
                        if ((R != null) && (R.fetchEffect(ID()) == null) && ((R.domainType() & Room.INDOORS) == 0) && (R.domainType() != Room.DOMAIN_OUTDOORS_UNDERWATER) && (R.domainType() != Room.DOMAIN_OUTDOORS_WATERSURFACE) && (R.domainType() != Room.DOMAIN_OUTDOORS_AIR)) beneficialAffect(mob, R, asLevel, 0);
                    }
                }
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> chant(s) to the ground, but the magic fades.");
        return success;
    }
}
