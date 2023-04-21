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
public class Spell_Claireaudience extends Spell {

    public String ID() {
        return "Spell_Claireaudience";
    }

    public String name() {
        return "Claireaudience";
    }

    public String displayText() {
        return "";
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_SPELL | Ability.DOMAIN_DIVINATION;
    }

    public int abstractQuality() {
        return Ability.QUALITY_OK_OTHERS;
    }

    public static final DVector scries = new DVector(2);

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        if (canBeUninvoked()) scries.removeElement(mob);
        if ((canBeUninvoked()) && (invoker != null)) invoker.tell("The sounds of '" + mob.name() + "' fade.");
        super.unInvoke();
    }

    public void executeMsg(Environmental myHost, CMMsg msg) {
        super.executeMsg(myHost, msg);
        if ((affected instanceof MOB) && (msg.amISource((MOB) affected)) && (msg.sourceMinor() == CMMsg.TYP_SPEAK) && (invoker != null) && (invoker.location() != ((MOB) affected).location()) && (msg.othersMessage() != null)) invoker.executeMsg(invoker, msg);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        if ((auto || mob.isMonster()) && ((commands.size() < 1) || (((String) commands.firstElement()).equals(mob.name())))) {
            commands.clear();
            MOB M = null;
            int tries = 0;
            while (((++tries) < 100) && (M == null)) {
                Room R = CMLib.map().getRandomRoom();
                if (R.numInhabitants() > 0) M = R.fetchInhabitant(CMLib.dice().roll(1, R.numInhabitants(), -1));
                if ((M != null) && (M.name().equals(mob.name()))) M = null;
            }
            if (M != null) commands.addElement(M.Name());
        }
        if (commands.size() < 1) {
            StringBuffer scryList = new StringBuffer("");
            for (int e = 0; e < scries.size(); e++) if (scries.elementAt(e, 2) == mob) scryList.append(((e > 0) ? ", " : "") + ((MOB) scries.elementAt(e, 1)).name());
            if (scryList.length() > 0) mob.tell("Cast on or revoke from whom?  You currently have " + name() + " on the following: " + scryList.toString() + "."); else mob.tell("Cast on whom?");
            return false;
        }
        String mobName = CMParms.combine(commands, 0).trim().toUpperCase();
        MOB target = null;
        if (givenTarget instanceof MOB) target = (MOB) givenTarget;
        if (target != null) target = mob.location().fetchInhabitant(mobName);
        if (target == null) {
            try {
                Vector targets = CMLib.map().findInhabitants(mob.location().getArea().getProperMap(), mob, mobName, 10);
                if (targets.size() == 0) targets = CMLib.map().findInhabitants(CMLib.map().rooms(), mob, mobName, 10);
                if (targets.size() > 0) target = (MOB) targets.elementAt(CMLib.dice().roll(1, targets.size(), -1));
            } catch (NoSuchElementException nse) {
            }
        }
        if (target instanceof Deity) target = null;
        Room newRoom = mob.location();
        if (target != null) newRoom = target.location(); else {
            mob.tell("You can't seem to focus on '" + mobName + "'.");
            return false;
        }
        if (mob == target) {
            mob.tell("You can't cast this on yourself!");
            return false;
        }
        Ability A = target.fetchEffect(ID());
        if ((A != null) && (A.invoker() == mob)) {
            A.unInvoke();
            return true;
        } else if ((A != null) || (scries.contains(target))) {
            mob.tell("You can't seem to focus on '" + mobName + "'.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> invoke(s) claireaudience, calling '" + mobName + "'.^?");
            CMMsg msg2 = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), null);
            if ((mob.location().okMessage(mob, msg)) && ((newRoom == mob.location()) || (newRoom.okMessage(mob, msg2)))) {
                mob.location().send(mob, msg);
                if (newRoom != mob.location()) newRoom.send(target, msg2);
                scries.addElement(target, mob);
                beneficialAffect(mob, target, asLevel, 0);
            }
        } else beneficialVisualFizzle(mob, null, "<S-NAME> attempt(s) to invoke claireaudience, but fizzle(s) the spell.");
        return success;
    }
}
