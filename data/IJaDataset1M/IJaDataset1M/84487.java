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
public class Chant_Homeopathy extends Chant {

    public String ID() {
        return "Chant_Homeopathy";
    }

    public String name() {
        return "Homeopathy";
    }

    public String displayText() {
        return "(Homeopathy)";
    }

    protected int canAffectCode() {
        return 0;
    }

    protected int canTargetCode() {
        return CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_PRESERVING;
    }

    public int abstractQuality() {
        return Ability.QUALITY_OK_OTHERS;
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "Something is happening to <T-NAME>!" : "^S<S-NAME> chant(s) homeopathically to <T-NAME>^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                Ability D = null;
                for (int t = 0; t < target.numEffects(); t++) {
                    Ability A = target.fetchEffect(t);
                    if ((A != null) && (A instanceof DiseaseAffect)) D = A;
                }
                int roll = CMLib.dice().rollPercentage();
                if ((roll > 66) || (D == null)) mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-YOUPOSS> condition is unchanged."); else if (roll > 33) {
                    mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> glow(s) a bit.");
                    D.unInvoke();
                } else {
                    mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "Something is definitely happening to <S-NAME>!");
                    for (int i = 0; i < 1000; i++) if (!D.tick(target, Tickable.TICKID_MOB)) break;
                }
            }
        } else beneficialVisualFizzle(mob, null, "<S-NAME> chant(s) to <T-NAMESELF>, but nothing happens.");
        return success;
    }
}
