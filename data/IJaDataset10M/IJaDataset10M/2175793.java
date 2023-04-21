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
public class Chant_CaveFishing extends Chant {

    public String ID() {
        return "Chant_CaveFishing";
    }

    public String name() {
        return "Cave Fishing";
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_ANIMALAFFINITY;
    }

    public int abstractQuality() {
        return Ability.QUALITY_INDIFFERENT;
    }

    protected int canAffectCode() {
        return 0;
    }

    protected int canTargetCode() {
        return CAN_ROOMS;
    }

    protected int previousResource = -1;

    public void unInvoke() {
        if ((affected instanceof Room) && (this.canBeUninvoked())) {
            ((Room) affected).showHappens(CMMsg.MSG_OK_VISUAL, "The fish start to disappear!");
            ((Room) affected).setResource(previousResource);
        }
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        Room target = mob.location();
        if (target == null) return false;
        Environmental waterSrc = null;
        if ((target.domainType() == Room.DOMAIN_INDOORS_WATERSURFACE) || (target.domainType() == Room.DOMAIN_INDOORS_UNDERWATER)) waterSrc = target; else if (target.domainType() == Room.DOMAIN_INDOORS_CAVE) {
            for (int i = 0; i < target.numItems(); i++) {
                Item I = target.fetchItem(i);
                if ((I instanceof Drink) && (I.container() == null) && (((Drink) I).liquidType() == RawMaterial.RESOURCE_FRESHWATER) && (!CMLib.flags().isGettable(I))) waterSrc = I;
            }
            if (waterSrc == null) {
                mob.tell("There is no water source here to fish in.");
                return false;
            }
        } else {
            mob.tell("This chant cannot be used outdoors.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            invoker = mob;
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> chant(s) to <T-NAME>.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                if (msg.value() <= 0) {
                    mob.location().showHappens(CMMsg.MSG_OK_VISUAL, "Fish start swimming around in " + target.name() + "!");
                    beneficialAffect(mob, target, asLevel, 0);
                    Chant_CaveFishing A = (Chant_CaveFishing) target.fetchEffect(ID());
                    if (A != null) {
                        mob.location().showHappens(CMMsg.MSG_OK_VISUAL, "Fish start swimming around in " + target.name() + "!");
                        A.previousResource = target.myResource();
                        target.setResource(RawMaterial.CODES.FISHES()[CMLib.dice().roll(1, RawMaterial.CODES.FISHES().length, -1)]);
                    }
                }
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> chant(s) to <T-NAME>, but the magic fades.");
        return success;
    }
}
