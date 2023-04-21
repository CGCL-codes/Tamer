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
public class Prayer_DesecrateLand extends Prayer {

    public String ID() {
        return "Prayer_DesecrateLand";
    }

    public String name() {
        return "Desecrate Land";
    }

    public String displayText() {
        return "(Desecrate Land)";
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_WARDING;
    }

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    protected int canAffectCode() {
        return CAN_ROOMS;
    }

    protected int canTargetCode() {
        return CAN_ROOMS;
    }

    public long flags() {
        return Ability.FLAG_UNHOLY;
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if (affected == null) return super.okMessage(myHost, msg);
        if ((msg.sourceMinor() == CMMsg.TYP_CAST_SPELL) && (msg.tool() instanceof Ability) && ((((Ability) msg.tool()).classificationCode() & Ability.ALL_ACODES) == Ability.ACODE_PRAYER) && (!CMath.bset(((Ability) msg.tool()).flags(), Ability.FLAG_UNHOLY)) && (CMath.bset(((Ability) msg.tool()).flags(), Ability.FLAG_HOLY))) {
            msg.source().tell("This place is blocking holy magic!");
            return false;
        }
        return super.okMessage(myHost, msg);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        Environmental target = mob.location();
        if (target == null) return false;
        if (target.fetchEffect(ID()) != null) {
            mob.tell("This place is already desecrated.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> " + prayForWord(mob) + " to desecrate this place.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                setMiscText(mob.Name());
                if ((target instanceof Room) && (CMLib.law().doesOwnThisProperty(mob, ((Room) target)))) {
                    target.addNonUninvokableEffect((Ability) this.copyOf());
                    CMLib.database().DBUpdateRoom((Room) target);
                } else beneficialAffect(mob, target, asLevel, 0);
            }
        } else beneficialWordsFizzle(mob, target, "<S-NAME> " + prayForWord(mob) + " to desecrate this place, but <S-IS-ARE> not answered.");
        return success;
    }
}
