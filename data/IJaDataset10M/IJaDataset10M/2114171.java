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
public class Spell_Frailty extends Spell {

    public String ID() {
        return "Spell_Frailty";
    }

    public String name() {
        return "Frailty";
    }

    public String displayText() {
        return "(Frailty)";
    }

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_SPELL | Ability.DOMAIN_ENCHANTMENT;
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if (!super.okMessage(myHost, msg)) return false;
        if ((affected == null) || (!(affected instanceof MOB))) return true;
        MOB mob = (MOB) affected;
        if ((msg.amITarget(mob)) && (msg.targetMinor() == CMMsg.TYP_DAMAGE) && ((msg.tool() == null) || (!(msg.tool() instanceof Ability)) || ((((Ability) msg.tool()).classificationCode() & Ability.ALL_ACODES) == Ability.ACODE_SKILL) || ((((Ability) msg.tool()).classificationCode() & Ability.ALL_ACODES) == Ability.ACODE_THIEF_SKILL))) {
            int recovery = (int) Math.round(CMath.div((msg.value()), 3.0));
            msg.setValue(msg.value() + recovery);
        }
        return true;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if (canBeUninvoked()) {
            if (mob.location() != null) mob.location().show(mob, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> seem(s) less frail.");
        }
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, (mob.envStats().level() + (2 * getXLEVELLevel(mob))) - target.envStats().level(), auto);
        if (success) {
            invoker = mob;
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> incant(s) to <T-NAMESELF>.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (msg.value() <= 0) {
                    mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> seem(s) frail!");
                    maliciousAffect(mob, target, asLevel, 10, -1);
                }
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> incant(s) to <T-NAMESELF>, but the spell fades.");
        return success;
    }
}
