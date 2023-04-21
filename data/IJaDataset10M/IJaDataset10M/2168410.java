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
public class Prayer_DispelUndead extends Prayer {

    public String ID() {
        return "Prayer_DispelUndead";
    }

    public String name() {
        return "Dispel Undead";
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_NEUTRALIZATION;
    }

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    public long flags() {
        return Ability.FLAG_HOLY;
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (target instanceof MOB) {
                if (!((MOB) target).charStats().getMyRace().racialCategory().equals("Undead")) return Ability.QUALITY_INDIFFERENT;
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if ((success) && (target.charStats().getMyRace().racialCategory().equals("Undead"))) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto) | CMMsg.MASK_MALICIOUS, auto ? "The evil inside <T-NAME> is exorcised!" : "^S<S-NAME> " + prayForWord(mob) + " to dispel the coldness inside <T-NAMESELF>!^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                int harming = CMLib.dice().roll(1, 30, 10);
                if (msg.value() > 0) harming = (int) Math.round(CMath.div(harming, 2.0));
                CMLib.combat().postDamage(mob, target, this, harming, CMMsg.MASK_ALWAYS | CMMsg.TYP_JUSTICE, Weapon.TYPE_BURSTING, "The holy spell <DAMAGE> <T-NAME>!");
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> point(s) at <T-NAMESELF> and " + prayWord(mob) + ", but nothing happens.");
        return success;
    }
}
